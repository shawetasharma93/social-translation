/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2013 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/

package com.adobe.cq.social.translation.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.osgi.OsgiUtil;
import org.apache.sling.discovery.DiscoveryService;
import org.apache.sling.discovery.InstanceDescription;
import org.apache.sling.discovery.TopologyView;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.scf.core.SocialEvent;
import com.adobe.cq.social.scf.core.SocialEvent.SocialActions;
import com.adobe.cq.social.translation.TranslationUtil;
import com.adobe.cq.social.translation.TranslationVariables;
import com.adobe.cq.social.ugcbase.AsyncReverseReplicator;
import com.adobe.cq.social.ugcbase.TranslationSaveQueue;
import com.adobe.cq.social.ugcbase.TranslationUpdate;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationException;
import com.adobe.granite.translation.api.TranslationManager;
import com.adobe.granite.translation.core.MachineTranslationUtil;

/**
 * A listener that listens to the user generated content change of cq:Comment.
 */

@Service(value = EventHandler.class)
@Component(label = "AEM Communities UGC Language Detector", description = "UGC Language Detector", immediate = true,
        metatype = true)
@Properties({
    @Property(name = EventConstants.EVENT_TOPIC, value = {SocialEvent.SOCIAL_EVENT_TOPIC_PREFIX + "*"}),
    @Property(name = EventConstants.EVENT_FILTER, value = "(path=/content/usergenerated/*)"),
    @Property(name = "service.description",
            value = "A listener detect the language when user generated content has been created or saved.")})
public class UGCLanguageDetector implements EventHandler {
    private static final String MICROSOFT_TRANSLATION_PROVIDER_NAME = "microsoft";
    private static final Logger LOG = LoggerFactory.getLogger(UGCLanguageDetector.class);
    private static final String CACHE_WHEN_POST = "cache_when_post";

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private SlingRepository repository;

    @Reference
    private TranslationManager translationManager;

    @Reference
    private AsyncReverseReplicator replicator;

    @Reference
    private TranslationSaveQueue translationSaveQueue;

    @Reference
    private DiscoveryService discoveryService;

    /**
     * Sling settings service.
     */
    @Reference
    protected SlingSettingsService settingsService;

    /**
     * JcrResourceResolverFactory Resolver factory used for the request.
     */
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private ResourceResolverFactory resourceResolverFactory;

    private Map<String, String> resourceTypes;

    private String[] resourceProperties;

    /**
     * Simulating a multimap since we do not have google's multimap available Maps a resourceType to ..N number of
     * properties
     */
    private Map<String, List<String>> defaultResourcePropertyMappings;

    @Property(label = "Resource and property types to listen for", cardinality = 100,
            value = {"social/commons/components/comments/comment jcr:description"},
            description = "Enter the sling:resourceType and primary property used to determine the language of the "
                    + "content (space delimited). Only one property per resource type. ")
    public static String RESOURCE_TYPE = "translate.listener.type";

    @Property(label = "Resource and property types to translate by default", cardinality = 100, value = {
        "social/commons/components/comments/comment jcr:description",
        "social/commons/components/comments/comment jcr:title"},
            description = "Enter the sling:resourceType and property to translate (space delimited).")
    public static String RESOURCE_PROPERTY_LIST = "translate.property.list";

    // Default configuration values
    protected static final int DEFAULT_POOL_SIZE = 5;
    protected static final int DEFAULT_MAX_POOL_SIZE = 10;
    protected static final int DEFAULT_QUEUE_SIZE = 50;
    protected static final long DEFAULT_KEEP_ALIVE_TIME = 1L;

    /**
     * The name of the property that defines the pool size.
     */
    @Property(intValue = DEFAULT_POOL_SIZE)
    public final static String PROPERTY_POOL_SIZE = "poolSize";

    /**
     * The name of the property that defines the max pool size.
     */
    @Property(intValue = DEFAULT_MAX_POOL_SIZE)
    public static final String PROPERTY_MAX_POOL_SIZE = "maxPoolSize";

    @Property(intValue = DEFAULT_QUEUE_SIZE)
    public static final String PROPERTY_QUEUE_SIZE = "queueSize";

    /**
     * The name of the property that defines the keep alive time.
     */
    @Property(longValue = DEFAULT_KEEP_ALIVE_TIME)
    public static final String PROPERTY_KEEP_ALIVE = "keepAliveTime";

    /**
     * Executor service thread pools.
     */
    protected ExecutorService executorService;

    /**
     * @param context Component Context
     * @throws RepositoryException when repository or resourceResolverFactory are null
     * @throws IllegalStateException when not in right state
     */
    protected void activate(final ComponentContext context) throws RepositoryException, IllegalStateException {
        // Listen for changes to comment nodes under usergenerated path

        if (repository == null) {
            throw new RepositoryException("Unsatisfied Reference to SlingRepository");
        }

        if (resourceResolverFactory == null) {
            throw new RepositoryException("Unsatisfied Reference to ResourceResolverFactory");
        }
        resourceTypes = new HashMap<String, String>();
        final Dictionary<?, ?> properties = context.getProperties();
        resourceProperties = OsgiUtil.toStringArray(properties.get(RESOURCE_PROPERTY_LIST), new String[]{});
        final String[] rp = OsgiUtil.toStringArray(properties.get(RESOURCE_TYPE), new String[]{});
        if (rp.length > 0) {
            for (final String aRp : rp) {
                final String[] splits = aRp.split("\\s+");
                if (splits.length == 2) {
                    final String s1 = splits[0];
                    final String s2 = splits[1];
                    resourceTypes.put(s1, s2);
                }
            }
        }

        defaultResourcePropertyMappings = new HashMap<String, List<String>>();
        final String[] rawResourcePropertyMappings =
            OsgiUtil.toStringArray(properties.get(RESOURCE_PROPERTY_LIST), new String[]{});
        for (final String rawLine : rawResourcePropertyMappings) {
            final String[] splits = rawLine.split("\\s+");
            if (splits.length == 2) {
                final String resourceType = splits[0];
                final String resourceProperty = splits[1];
                List<String> mapping = defaultResourcePropertyMappings.get(resourceType);
                if (mapping == null) {
                    mapping = new ArrayList<String>();
                }
                mapping.add(resourceProperty);
                defaultResourcePropertyMappings.put(resourceType, mapping);
            }
        }
        /**
         * Set these variables so other UGC dependent code can access them in TranslationVariables
         */
        TranslationVariables.setResourcePropertyMappings(resourceTypes);

        final int poolSize = OsgiUtil.toInteger(context.getProperties().get(PROPERTY_POOL_SIZE), DEFAULT_POOL_SIZE);
        final int maxPoolSize =
            OsgiUtil.toInteger(context.getProperties().get(PROPERTY_MAX_POOL_SIZE), DEFAULT_MAX_POOL_SIZE);
        final int queueSize =
            OsgiUtil.toInteger(context.getProperties().get(PROPERTY_QUEUE_SIZE), DEFAULT_QUEUE_SIZE);
        final long keepAlive =
            OsgiUtil.toLong(context.getProperties().get(PROPERTY_KEEP_ALIVE), DEFAULT_KEEP_ALIVE_TIME);

        executorService =
            new ThreadPoolExecutor(
                poolSize, // core thread pool size The number of threads to keep in the pool, even if the thread is
                // even
                maxPoolSize, // maximum thread pool size
                keepAlive,   // time to wait before resizing pool
                TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(queueSize, true),
                new ThreadPoolExecutor.CallerRunsPolicy());  // execute the task using the caller's thread when the
        // queue is full
    }

    protected void deactivate(final ComponentContext ctx) throws Exception {
        if (executorService != null) {
            // wait for all of the executor threads to finish
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    // pool didn't terminate after the first try
                    executorService.shutdownNow();
                }
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    // pool didn't terminate after the second try
                }
            } catch (final InterruptedException ex) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Check the given array of attributes and return true if any string in the array is the validProperty
     * @param attributes the array of strings to check.
     * @param validProperty the property we are looking for.
     * @return true if any string in the array equals the validProperty
     */
    private boolean checkAttributes(final String[] attributes, final String validProperty) {
        LOG.debug("In Function: checkAttributes");
        if (attributes == null) {
            return false;
        }

        for (final String attribute : attributes) {
            if (validProperty.equals(attribute)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Plumbing method to check resourceTypes against config
     * @param eventResource the resource that was modified to fire the event
     * @return the resource type, or null
     */
    private String getResType(final Resource eventResource) {
        String resType = null;
        if (eventResource == null) {
            return null;
        }
        /**
         * Iterate through allowed resource types, and check against our event resource's resource type
         */
        for (final String type : resourceTypes.keySet()) {
            if (eventResource.isResourceType(type)) {
                resType = type;
            }
        }
        return resType;
    }

    private class TranslationDetectionJob implements Runnable {
        TranslationManager translationManager;
        SocialEvent<SocialActions> event;
        AsyncReverseReplicator replicator;

        public TranslationDetectionJob(final TranslationManager translationManager,
            final SocialEvent<SocialActions> event, final AsyncReverseReplicator replicator) {
            this.translationManager = translationManager;
            this.event = event;
            this.replicator = replicator;
        }

        @Override
        public void run() {
            // get the resource event information
            final String resourcePath = (String) event.getProperty(SlingConstants.PROPERTY_PATH);
            LOG.debug("TranslationDetectionJob - found resourcePath: " + resourcePath);

            if (resourceTypes != null && resourceTypes.size() > 0) {
                /**
                 * Instantiate these variables first, in case the resolverFactory fails
                 */

                Resource eventResource;

                /**
                 * Get session, and resource Resolver. Need the session to be administrative so we can persist the
                 * translation
                 */
                final Session serviceSession;
                final ResourceResolver serviceResolver;
                try {
                    serviceSession = ServiceUserWrapper.loginService(repository, TranslationUtil.UGC_WRITER, null);
                    final Map<String, Object> authInfo = new HashMap<String, Object>();
                    authInfo.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, serviceSession);
                    serviceResolver = resourceResolverFactory.getResourceResolver(authInfo);
                } catch (final RepositoryException e) {
                    LOG.error("error in creating service session: ", e);
                    return;
                } catch (final LoginException e) {
                    LOG.error("Error while content updation.", e);
                    return;
                }

                try {

                    eventResource = serviceResolver.getResource(resourcePath);
                    final String resType = getResType(eventResource);

                    if (eventResource != null && resType != null) {

                        // Ensure we have credentials to call through the translation services... if not,
                        // then short circuit.
                        LOG.debug("Found eventResource configured for MT: {} ", eventResource.getPath());
                        final List<String> validProperties = defaultResourcePropertyMappings.get(resType);
                        /**
                         * Only detect a language if the validProperty is added or changed
                         */
                        final String validLanguageDetectionProperty = resourceTypes.get(resType);
                        String fromLan = "";
                        fromLan =
                            TranslationUtil.addLanguageCode(eventResource, serviceSession,
                                validLanguageDetectionProperty, TranslationConstants.ContentType.HTML,
                                translationManager);

                        if (StringUtils.isEmpty(fromLan)) {
                            LOG.trace("From language empty or no watched properties updated, leaving event handler");
                            return;
                        }
                        /**
                         * Get the language origin (fromLan), and then conditionally save depending on the currently
                         * configured caching / save options
                         */
                        LOG.debug("DETECTOR - language code: " + fromLan);
                        if (CACHE_WHEN_POST.equals(TranslationVariables.getCaching())) {

                            final String[] lanCode = TranslationVariables.getLanguageCode();

                            for (final String aLanCode : lanCode) {
                                if (LOG.isTraceEnabled()) {
                                    LOG.trace("Creating translation update for" + aLanCode);
                                }
                                if (!TranslationUtil.languagesAreEquivalent(aLanCode, fromLan)) {
                                    final TranslationUpdate translationUpdate =
                                        TranslationUtil.getTranslationUpdate(fromLan, aLanCode, eventResource,
                                            translationManager,
                                            validProperties.toArray(new String[validProperties.size()]));
                                    if (translationUpdate != null) {
                                        if (LOG.isTraceEnabled()) {
                                            LOG.trace("Translation update created : " + translationUpdate.getToLan());
                                        }
                                        translationSaveQueue.getUpdateQueue().add(translationUpdate);
                                        LOG.trace("Translation added to update queue");
                                        translationSaveQueue.registerUpdate();
                                        LOG.trace("Translation Update registered");
                                    } else {
                                        LOG.warn("Translation Update was null for language ", aLanCode);
                                    }
                                } else {
                                    if (LOG.isTraceEnabled()) {
                                        LOG.trace("Not Creating translation update, languages are equivalent from : "
                                                + fromLan + "to " + aLanCode);
                                    }
                                    try {
                                        final Resource TranslationNodeResource =
                                            eventResource.getChild(TranslationUtil.TRANSLATION_NODE_NAME);

                                        if (TranslationNodeResource != null) {
                                            final Resource langResource = TranslationNodeResource.getChild(fromLan);

                                            if (langResource != null) {
                                                serviceResolver.delete(langResource);
                                            }
                                            if (serviceSession != null) {
                                                serviceSession.save();
                                            }
                                        }
                                    } catch (final PersistenceException pe) {
                                        LOG.warn("Unable to remove old language node.", pe);
                                    }
                                }
                            }
                        }

                        if (replicator != null) {
                            // Reverse Replicate to get "translation" node to Author environment
                            LOG.debug("Calling reverse replication for: {}", resourcePath + "/"
                                    + TranslationUtil.TRANSLATION_NODE_NAME);
                            replicator.reverseReplicate(resourcePath + "/" + TranslationUtil.TRANSLATION_NODE_NAME);
                        }
                    }
                } catch (final RepositoryException e) {
                    LOG.error("Error occurred while processing event", e);
                } finally {
                    if (serviceSession != null && serviceSession.isLive()) {
                        serviceSession.logout();
                    }

                    if (serviceResolver != null && serviceResolver.isLive()) {
                        serviceResolver.close();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleEvent(final Event event) {
        LOG.trace("In function: handleEvent");
        SocialEvent<SocialActions> socialEvent = null;
        try {
            if (event instanceof SocialEvent<?>) {
                socialEvent = (SocialEvent<SocialActions>) event;
            }
        } catch (final ClassCastException cce) {
            LOG.debug("In isEventValidForTranslation: Setting socialEvent to null.", cce);
            socialEvent = null;
        }

        if (socialEvent == null) {
            return;
        }

        if (isEventValidForTranslation(socialEvent)) {
            final Runnable jobToRun = new TranslationDetectionJob(translationManager, socialEvent, replicator);
            executorService.submit(jobToRun);
        }
    }

    private boolean isEventValidForTranslation(final SocialEvent<SocialActions> event) {
        boolean bRetVal = false;
        final TopologyView view = discoveryService.getTopology();
        final InstanceDescription currentInstance = view.getLocalInstance();

        if (translationManager != null && currentInstance != null && !isAuthorMode()) {
            /**
             * Get session, and resource Resolver. Need the session to be administrative so we can persist the
             * translation
             */
            final Session serviceSession;
            final ResourceResolver serviceResolver;
            try {
                serviceSession = ServiceUserWrapper.loginService(repository, TranslationUtil.UGC_WRITER, null);
                final Map<String, Object> authInfo = new HashMap<String, Object>();
                authInfo.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, serviceSession);
                serviceResolver = resourceResolverFactory.getResourceResolver(authInfo);
            } catch (final RepositoryException e) {
                LOG.error("error in creating service session: ", e);
                return bRetVal;
            } catch (final LoginException e) {
                LOG.error("Error while content updation.", e);
                return bRetVal;
            }

            try {
                /**
                 * Instantiate these variables first, in case the resolverFactory fails
                 */
                Resource eventResource;

                final String resourcePath = (String) event.getProperty(SlingConstants.PROPERTY_PATH);
                LOG.debug("DETECTOR - found resourcePath: {}", resourcePath);
                eventResource = serviceResolver.getResource(resourcePath);
                final String resType = getResType(eventResource);
                if (eventResource != null && resType != null) {

                    final MachineTranslationUtil mtu = serviceResolver.adaptTo(MachineTranslationUtil.class);

                    final Resource nonUGCResource = TranslationUtil.getNonUgcResource(eventResource);
                    if (mtu != null && translationManager.isTranslationServiceConfigured(nonUGCResource)) {
                        bRetVal = true;
                    }
                }
            } catch (final TranslationException te) {
                // Happens if there is no configured MT Connector from the tm.getAvailableFactoryNames() call
                LOG.trace(te.toString());
            } finally {
                if (serviceSession != null && serviceSession.isLive()) {
                    serviceSession.logout();
                }

                if (serviceResolver != null && serviceResolver.isLive()) {
                    serviceResolver.close();
                }
            }
        } else {
            LOG.trace("Instance was not the leader or is in the Author environment or MT is turned off");
        }

        return bRetVal;
    }

    /**
     * Return if it is in author mode.
     * @return if it is in author mode.
     */
    private boolean isAuthorMode() {
        return settingsService != null && settingsService.getRunModes().contains("author");
    }
}
