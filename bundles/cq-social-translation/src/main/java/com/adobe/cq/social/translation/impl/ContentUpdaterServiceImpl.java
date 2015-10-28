/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2014 Adobe Systems Incorporated
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.translation.AbstractContentUpdater;
import com.adobe.cq.social.translation.ContentUpdaterService;
import com.adobe.cq.social.translation.TranslationUtil;
import com.adobe.cq.social.translation.TranslationVariables;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.granite.translation.api.TranslationManager;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

@Component(immediate = true, enabled = true, label = "Language Content Updater")
@Service(value = ContentUpdaterService.class)
public class ContentUpdaterServiceImpl implements ContentUpdaterService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentUpdaterServiceImpl.class);
    // This will be the root of our search
    private static final String CONTENT_UPDATE_ROOT = SocialUtils.PATH_UGC;
    // Tells queryBuilder to set the hit size to unlimited
    private static final Long HITS_PER_PAGE = 0L;
    // Passes on Nodes in batches of 500 to the language detection service, saving after every 500
    private static final int UPDATE_BATCH_SIZE = 500;
    // Sets the futures to execute in a parallel thread pool of this number
    private static final int THREAD_POOL_LENGTH = 3;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private SlingRepository repository;

    @Reference
    private QueryBuilder builder;

    @Reference
    private TranslationManager translationManager;

    /**
     * JcrResourceResolverFactory Resolver factory used to get a resourceResolver
     */
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Session context that the content update will operate within
     */
    private Session serviceSession;

    /**
     * Resource resolver to be passed to the Abstract ContentUpdater
     */
    private ResourceResolver resourceResolver;

    // Highly mutable variable that will be updated as a sort of progress log
    private List<String> statusUpdates = new ArrayList<String>();

    // Highly mutable variable that will be updated as we act upon the nodes
    private List<String> pathsWorkedUpon = new ArrayList<String>();

    // This flag is set once all nodes have been processed from the updater operation
    private boolean isComplete = false;

    @Override
    public String getStatus() {
        String stringifiedStatus = "";
        for (final String update : statusUpdates) {
            stringifiedStatus += (update + "\n");
        }
        return stringifiedStatus;
    }

    protected List<String> getStatusUpdates() {
        return this.statusUpdates;
    }

    protected List<String> getPathsWorkedUpon() {
        return this.pathsWorkedUpon;
    }

    @Override
    public org.json.JSONObject getStatusJSON() {
        final org.json.JSONObject jsonResponse = new org.json.JSONObject();
        try {
            jsonResponse.put("isComplete", isComplete());
            jsonResponse.put("statusUpdates", getStatusUpdates().toArray());
            jsonResponse.put("pathsWorkedUpon", getPathsWorkedUpon().toArray());
        } catch (final JSONException e) {
            LOG.error("Error generating JSON", e);
        }
        return jsonResponse;
    }

    /**
     * Sets the status update to be rendered as either a multi-line string, JSON object, or logged out
     * @param update - The status to update
     */
    private void updateStatus(final String update) {
        LOG.trace(update);
        this.statusUpdates.add(update);
    }

    /**
     * Sets the status update to be rendered as either a multi-line string, JSON object, or logged out
     * @param update - The update to append to the progress log
     * @param path - The path that was operated on
     */
    private void updateStatus(final String update, final String path) {
        this.updateStatus(update);
        this.pathsWorkedUpon.add(path);

    }

    private void clearStatus() {
        this.statusUpdates = new ArrayList<String>();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public void run() {
        // Let's make sure that the progress log is empty before a run
        clearStatus();
        this.isComplete = false;
        this.pathsWorkedUpon = new ArrayList<String>();
        this.statusUpdates = new ArrayList<String>();

        try {
            /**
             * Gets the resourceProperties (from UGClanguageDetector)
             */
            final Map<String, String> resourceProperties = TranslationVariables.getResourcePropertyMappings();

            if (resourceProperties != null && resourceProperties.size() > 0 && serviceSession != null) {

                final Node rootNode = serviceSession.getNode(CONTENT_UPDATE_ROOT);

                if (rootNode != null) {
                    // Since we are sure that the root node isn't null, we can search for it's path
                    updateStatus("Recursing through the root UGC node @ " + rootNode.getPath());
                    /**
                     * This is where the real work happens
                     */
                    queryPath(rootNode.getPath(), resourceProperties);
                } else {
                    updateStatus("Could not find root UGC node @ path " + CONTENT_UPDATE_ROOT);
                }
            } else {
                updateStatus("Unable to login with administrative session, or there are no UGC resources set");
            }

        } catch (final PathNotFoundException e) {
            LOG.error("Error finding path", e);
        } catch (final RepositoryException e) {
            LOG.error("Repository Exception", e);
        } finally {

            /**
             * Finally done! update the status, set the flag to complete, and move on.
             */
            updateStatus("Operation complete, but nodes may still be updaitng");
            isComplete = true;

        }
    }

    protected void activate(final org.osgi.service.component.ComponentContext context) {

        try {
            this.serviceSession = ServiceUserWrapper.loginService(repository, TranslationUtil.UGC_WRITER, null);
            final Map<String, Object> authInfo = new HashMap<String, Object>();
            authInfo.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, this.serviceSession);
            this.resourceResolver = resourceResolverFactory.getResourceResolver(authInfo);
        } catch (final RepositoryException e) {
            LOG.error("error in creating service session: ", e);
        } catch (final LoginException e) {
            LOG.error("Error while content updation.", e);
        }

    }

    /**
     * Clean up here
     * @param context Component context
     */
    protected void deactivate(final org.osgi.service.component.ComponentContext context) {

        if (serviceSession != null && serviceSession.isLive()) {
            serviceSession.logout();
        }

        if (resourceResolver != null && resourceResolver.isLive()) {
            resourceResolver.close();
            resourceResolver = null;
        }

    }

    /**
     * Recurses through this node structure, calling the AbstractContentUpdater callable along the way. Implementation
     * may use a query, search, or some other type of call
     * @param rootPath - The path to the root page
     * @param resourceProperty - The node property to search for
     */
    private void queryPath(final String rootPath, final Map<String, String> resourceProperty) {

        LOG.trace("Querying on the path " + rootPath);

        /**
         * All of the ContentUpdaters will execute within this context
         */
        final ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_LENGTH);
        final Set<Future<Map<Node, String>>> resultSet = new HashSet<Future<Map<Node, String>>>();

        /**
         * These two sets are a sort of caching mechanism so we will avoid expensive resourceresolver calls
         */
        final Set<String> foundResourceTypes = new HashSet<String>();
        for (final String resourceType : resourceProperty.keySet()) {
            foundResourceTypes.add(resourceType);
        }
        final Set<String> resourceTypesNotToProcess = new HashSet<String>();
        /**
         * This chunk will process all allowed resource / property pairs, and execute a query for them in the JCR
         */
        for (final String resourceType : resourceProperty.keySet()) {

            final String validProperty = resourceProperty.get(resourceType);
            updateStatus("Looking for resource type : " + resourceType + " , and property : " + validProperty);

            /**
             * Here are all of our query params
             */
            final Map<String, String> map = new HashMap<String, String>();
            map.put("path", rootPath);
            map.put("property", validProperty);

            /**
             * Instantiate the query builder based on the simple mapping predicates
             */
            final Query query = builder.createQuery(PredicateGroup.create(map), serviceSession);
            query.setHitsPerPage(HITS_PER_PAGE);

            updateStatus("Executing query ....");
            final SearchResult result = query.getResult();

            updateStatus("Results : " + String.valueOf(result.getTotalMatches()));

            List<Node> batchOfNodes = new ArrayList<Node>();
            for (final Iterator<Node> nodeIterator = result.getNodes(); nodeIterator.hasNext();) {
                final Node n = nodeIterator.next();
                String nodeProperty;
                boolean processMe = false;
                try {
                    nodeProperty = n.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getString();
                    if (foundResourceTypes.contains(nodeProperty)) {
                        processMe = true;
                    } else if (resourceTypesNotToProcess.contains(nodeProperty)) {
                        processMe = false;
                    } else {
                        final Resource nodeResource = this.resourceResolver.getResource(n.getPath());
                        final String thisNodeResType = nodeResource.getResourceType();
                        if (nodeResource.isResourceType(thisNodeResType)) {
                            processMe = true;
                            if (!foundResourceTypes.contains(thisNodeResType)) {
                                foundResourceTypes.add(nodeResource.getResourceType());
                            }
                        } else {
                            if (!resourceTypesNotToProcess.contains(thisNodeResType)) {
                                resourceTypesNotToProcess.add(thisNodeResType);
                            }
                        }
                    }
                } catch (final PathNotFoundException e) {
                    LOG.warn("Could not find resource type for node", e);
                } catch (final RepositoryException e) {
                    LOG.warn("Repository exception for node", e);
                } catch (final NullPointerException e) {
                    LOG.warn("Could not find sling resource type for node", e);
                }
                if (processMe) {
                    batchOfNodes.add(n);
                }
                if (!batchOfNodes.isEmpty() && (batchOfNodes.size() % UPDATE_BATCH_SIZE == 0)) {
                    LOG.trace("Processing batch of size " + UPDATE_BATCH_SIZE);
                    /**
                     * Instantiate a new AbstractContentUpdater for the batched bunch of nodes, and add to the
                     * resultSet
                     */
                    final AbstractContentUpdater abstractContentUpdater =
                        new AbstractContentUpdaterImpl(batchOfNodes, serviceSession, validProperty,
                            translationManager, this.resourceResolver);
                    resultSet.add(pool.submit(abstractContentUpdater));
                    /**
                     * Clear out the batch, to be filled with more sets of nodes
                     */
                    batchOfNodes = new ArrayList<Node>();
                }
            }
            if (!batchOfNodes.isEmpty()) {
                LOG.trace("Processing batch of size " + batchOfNodes.size());
                /**
                 * Instantiate a new AbstractContentUpdater for the batched bunch of nodes, and add to the resultSet
                 */
                final AbstractContentUpdater abstractContentUpdater =
                    new AbstractContentUpdaterImpl(batchOfNodes, serviceSession, validProperty, translationManager,
                        this.resourceResolver);
                resultSet.add(pool.submit(abstractContentUpdater));
            }
            processResultSet(resultSet);
        }
    }

    /**
     * Iterate through all of the callables that have been built into this set. This method is mainly for logging /
     * reporting back on the results of the futures
     * @param resultSet - A list of Futures that contain mappings between nodes and their language mappings.
     */
    private void processResultSet(final Set<Future<Map<Node, String>>> resultSet) {
        LOG.trace("Processing Content Updaters");
        for (final Future<Map<Node, String>> future : resultSet) {
            Map<Node, String> resultMap = null;

            /**
             * Attempt to get the result of the future
             */
            try {
                resultMap = future.get();
            } catch (final InterruptedException e) {
                LOG.error("Interrupted exception in future processing", e);
            } catch (final ExecutionException e) {
                LOG.error("Execution exception in future processing", e);
            }

            /**
             * If the future returned a successful mapping, iterate through the nodes and upate the progress Log.
             */
            if (resultMap != null && !resultMap.isEmpty() && !resultMap.keySet().isEmpty()) {
                for (final Node processedNode : resultMap.keySet()) {
                    if (processedNode != null) {
                        try {
                            final String updatedPath = processedNode.getPath();
                            updateStatus("Processed path for : " + updatedPath, updatedPath);
                        } catch (final RepositoryException e) {
                            LOG.error("Repository exception in queryPath", e);
                        }
                    }
                }
            } else {
                updateStatus("Empty results from processing Content Update");
            }
        }
    }

}
