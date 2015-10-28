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
package com.adobe.cq.social.translation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.community.api.CommunityContext;
import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.cq.social.ugcbase.TranslationSaveQueue;
import com.adobe.cq.social.ugcbase.TranslationUpdate;
import com.adobe.granite.security.user.UserProperties;
import com.adobe.granite.security.user.UserPropertiesManager;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationException;
import com.adobe.granite.translation.api.TranslationManager;

public class TranslationSCFUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TranslationSCFUtil.class);
    private static final String JCR_DESCRIPTION_PROP = "jcr:description";
    private static final String JCR_TITLE_PROP = "jcr:title";
    private static final String NO_CACHING = "no_caching";
    private static final String CACHE_WHEN_CALL = "cache_when_call";
    public static final String REFERER = "referer";
    private static final String BROWSER_LANGUAGE = "Accept-Language";
    private static final String COMMENT_MODIFIED = "cq:lastModified";
    private static final String PERSIST_TRANSLATION = "persistTranslation";

    public static void updateEditTranslation(Resource resource, Session session, String userID, String requestURI,
        String requestReferer, String requestBrowserLang, String modifiedTranslation,
        TranslationSaveQueue translationSaveQueue) {
        LOG.trace("Inside updateEditTranslation: ");

        String srcLanguage = TranslationUtil.getUGCLanguage(resource);

        String caching = null;
        String targetLanguage = null;

        final CommunityContext context = resource.adaptTo(CommunityContext.class);

        if (context == null) {
            LOG.error("updateEditTranslation: CommunityContext is null.");
            return;
        }

        String siteID = context.getSiteId();
        if (siteID != null && !"".equals(siteID)) {
            LOG.trace("Inside updateEditTranslation: This is a community site.");

            String sitePath = context.getSitePath();
            LOG.trace("updateEditTranslation - Site path of community site: {}", sitePath);

            // get translation options from configuration folder
            final ResourceResolver resourceResolver = resource.getResourceResolver();
            final Resource siteResource = resourceResolver.getResource(sitePath);
            if (siteResource != null) {
                final ValueMap configProperties = siteResource.adaptTo(ValueMap.class);
                Object persistenceOption = configProperties.get(PERSIST_TRANSLATION, (Object) null);

                if (persistenceOption != null) {
                    if (persistenceOption instanceof Boolean) {
                        Boolean persistOptionBoolean = (Boolean) persistenceOption;
                        if (persistOptionBoolean != null) {
                            if (persistOptionBoolean.booleanValue())
                                caching = CACHE_WHEN_CALL;
                            else
                                caching = NO_CACHING;
                        }
                    }
                } else
                    caching = NO_CACHING;
            }
        } else {
            caching = TranslationVariables.getCaching();
        }

        targetLanguage =
            TranslationUtil.getTranslationTargetLanguage(resource.getResourceResolver(), resource, userID,
                requestURI, requestReferer, requestBrowserLang);

        if (LOG.isDebugEnabled()) {
            LOG.debug("resourcePath: {}", resource.getPath());
            LOG.debug("userID: {}", userID);
            LOG.debug("requestURI: {}", requestURI);
            LOG.debug("requestReferer: {}", requestReferer);
            LOG.debug("requestBrowserLang: {}", requestBrowserLang);
            LOG.debug("srcLanguage: {}", srcLanguage);
            LOG.debug("targetLanguage: {}", targetLanguage);
            LOG.debug("modifiedTranslation: {}", modifiedTranslation);
        }

        int rating = 6; // TODO

        final SocialUtils socialUtils = resource.getResourceResolver().adaptTo(SocialUtils.class);
        final TranslationManager tm = socialUtils.getTranslationManager();

        if (tm != null) {
            final Map<String, String> translation = new HashMap<String, String>();
            final ValueMap resourceValueMap = resource.adaptTo(ValueMap.class);
            final String originText = resourceValueMap.get(JCR_DESCRIPTION_PROP, "");

            TranslationUtil.storeModifiedTranslation(resource, tm, originText, srcLanguage, targetLanguage,
                modifiedTranslation, TranslationConstants.ContentType.HTML, userID, rating);
            translation.put(JCR_DESCRIPTION_PROP, modifiedTranslation);

            if (!NO_CACHING.equals(caching)) {
                final TranslationUpdate translationUpdate =
                    new TranslationUpdate(resource.getPath(), targetLanguage, translation);
                translationSaveQueue.getUpdateQueue().add(translationUpdate);
                LOG.trace("Translation added to update queue");
                translationSaveQueue.registerUpdate();
            }
        }
    }

    public static TranslationResults getTranslationSCF(final Resource resource, final ClientUtilities clientUtils) {
        LOG.trace("Inside getTranslationSCF: ");

        boolean isSiteForum = false;
        String caching = null;
        String targetLanguage = null;

        // Figure out if site config or felix console config comes into picture
        final CommunityContext context = resource.adaptTo(CommunityContext.class);

        if (context == null) {
            LOG.error("getTranslationSCF: CommunityContext is null.");
            return null;
        }

        if (!TranslationUtil.doDisplayTranslation(resource.getResourceResolver(), resource, clientUtils))
            return null;

        String siteID = context.getSiteId();
        if (siteID != null && !"".equals(siteID)) {
            LOG.trace("Inside getTranslationSCF: This is a community site.");

            isSiteForum = true;
            String sitePath = context.getSitePath();
            LOG.trace("getTranslationSCF - Site path of community site: {}", sitePath);

            // get translation options from configuration folder
            final ResourceResolver resourceResolver = resource.getResourceResolver();
            final Resource siteResource = resourceResolver.getResource(sitePath);
            if (siteResource != null) {
                final ValueMap configProperties = siteResource.adaptTo(ValueMap.class);
                Object persistenceOption = configProperties.get(PERSIST_TRANSLATION, (Object) null);

                if (persistenceOption != null) {
                    if (persistenceOption instanceof Boolean) {
                        Boolean persistOptionBoolean = (Boolean) persistenceOption;
                        if (persistOptionBoolean != null) {
                            if (persistOptionBoolean.booleanValue())
                                caching = CACHE_WHEN_CALL;
                            else
                                caching = NO_CACHING;
                        }
                    }
                } else
                    caching = NO_CACHING;
            }
        } else {
            caching = TranslationVariables.getCaching();
        }
        targetLanguage =
            TranslationUtil.getTranslationTargetLanguage(resource.getResourceResolver(), resource, clientUtils
                .getAuthorizedUserId(), clientUtils.getRequest().getRequestURI(),
                clientUtils.getRequest().getHeader(REFERER), clientUtils.getRequest().getHeader(BROWSER_LANGUAGE));

        return getTranslationResults(resource, targetLanguage, caching, isSiteForum, clientUtils);
    }

    private static TranslationResults getTranslationResults(final Resource commentResource, final String toLanguage,
        final String caching, boolean isSiteForum, final ClientUtilities clientUtils) {
        TranslationResults translationResults = null;
        final Map<String, String> translation = new HashMap<String, String>();
        boolean isTranslationCached = false;

        if (LOG.isDebugEnabled()) {
            LOG.debug("commentResourcePath: {}", commentResource.getPath());
            LOG.debug("toLanguage: {}", toLanguage);
            LOG.debug("caching: {}", caching);
            LOG.debug("isSiteForum: {}", isSiteForum);
        }

        if (toLanguage != null && !"".equals(toLanguage)) {
            Resource transResource =
                commentResource.getChild(TranslationUtil.TRANSLATION_NODE_NAME + "/" + toLanguage);
            if (transResource != null) {
                boolean bEditComment = false;
                final ValueMap resourceProperties = commentResource.adaptTo(ValueMap.class);
                final ValueMap translationProps = transResource.adaptTo(ValueMap.class);
                Date commentModifiedDate = null;
                Date translationDate = null;

                Object commentModifiedObject = resourceProperties.get(COMMENT_MODIFIED, (Object) null);
                if (commentModifiedObject != null) {
                    if (commentModifiedObject instanceof Calendar) {
                        Calendar commentModifiedCalendar = (Calendar) commentModifiedObject;
                        if (commentModifiedCalendar != null) {
                            commentModifiedDate = commentModifiedCalendar.getTime();
                        }
                    } else if (commentModifiedObject instanceof Date) {
                        commentModifiedDate = (Date) commentModifiedObject;
                    }
                }

                Object translationDateObject =
                    translationProps.get(TranslationUtil.TRANSLATION_DATE_PROP, (Object) null);
                if (translationDateObject != null) {
                    if (translationDateObject instanceof GregorianCalendar) {
                        GregorianCalendar translationDateCalendar = (GregorianCalendar) translationDateObject;
                        if (translationDateCalendar != null) {
                            translationDate = translationDateCalendar.getTime();
                        }
                    } else if (translationDateObject instanceof Date) {
                        translationDate = (Date) translationDateObject;
                    }
                }

                if (translationDate != null) {
                    int cachingDuration;
                    if (commentModifiedDate != null) {
                        bEditComment = commentModifiedDate.after(translationDate);
                        LOG.debug("getTranslationResults: Comment has been modified - " + bEditComment);
                    }

                    if (isSiteForum) {
                        cachingDuration = 0;
                    } else {
                        cachingDuration = TranslationVariables.getCachingDuration();
                    }

                    if (!bEditComment && !isTranslationExpired(translationDate, cachingDuration)) {
                        LOG.debug("getTranslationResults: Getting translation from cache. ");
                        translation.put(JCR_DESCRIPTION_PROP,
                            translationProps.get(JCR_DESCRIPTION_PROP, (String) null));
                        translation.put(JCR_TITLE_PROP, translationProps.get(JCR_TITLE_PROP, (String) null));
                        isTranslationCached = true;

                        translationResults =
                            new TranslationResults("", translation, TranslationSCFUtil.getTranslationAttribution(
                                commentResource, clientUtils), TranslationVariables.getDisplay());

                    }
                }
            }

            if (!isTranslationCached) {
                LOG.debug("getTranslation: Getting fresh translation. ");

                final SocialUtils socialUtils = clientUtils.getSocialUtils();
                final TranslationManager tm = socialUtils.getTranslationManager();

                String srcLanguage = TranslationUtil.getUGCLanguage(commentResource);
                String[] properties = {JCR_TITLE_PROP, JCR_DESCRIPTION_PROP};

                if (tm != null) {
                    translationResults =
                        TranslationUtil.getTranslation(srcLanguage, toLanguage, properties, commentResource, tm);
                    if (translationResults != null) {
                        translationResults.setDisplay(TranslationVariables.getDisplay());
                        translationResults.setAttribution(getTranslationAttribution(commentResource, clientUtils));
                    }
                    if (!NO_CACHING.equals(caching) && "Success".equalsIgnoreCase(translationResults.getStatus())) {
                        final TranslationUpdate translationUpdate =
                            new TranslationUpdate(commentResource.getPath(), toLanguage,
                                translationResults.getTranslation());
                        TranslationSaveQueue translationSaveQueue = socialUtils.getTranslationSaveQueue();
                        if (translationSaveQueue != null) {
                            translationSaveQueue.getUpdateQueue().add(translationUpdate);
                            LOG.trace("getTranslationResults: Translation added to update queue");
                            translationSaveQueue.registerUpdate();
                        } else {
                            LOG.error("TranslationSCFUtil: translationSaveQueue is null.");
                        }
                    }
                }
            }
        }

        return translationResults;
    }

    public static String getTranslationAttribution(Resource resource, ClientUtilities clientUtils) {
        String attribution = null;
        SocialUtils socialUtils = clientUtils.getSocialUtils();
        TranslationManager tm = socialUtils.getTranslationManager();

        if (TranslationVariables.isEnableAttribution()) {
            try {
                if (tm != null) {
                    attribution =
                        tm.createTranslationService(TranslationUtil.getNonUgcResource(resource))
                            .getTranslationServiceInfo().getTranslationServiceAttribution();
                }
            } catch (final TranslationException ex) {
                LOG.trace("Error getting translation service attribution", ex);
            }
        }

        return attribution;
    }

    private static boolean isTranslationExpired(final Date translationDate, final int cachingMonths) {
        LOG.trace("In Function: isTranslationExpired");
        boolean retVal = false;

        if (translationDate != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1 * cachingMonths);
            boolean isTranslationCacheExpired = translationDate.before(calendar.getTime());
            retVal = (cachingMonths > 0) && isTranslationCacheExpired;
        }

        return retVal;
    }

    public static boolean isSmartRenderingOn(final Resource resource, final ClientUtilities clientUtils) {
        boolean smartRenderingRetVal = false;

        String userId = clientUtils.getAuthorizedUserId();
        String PN_PROFILE_SMART_RENDERING = "smartRendering";
        String strProfile = "profile";
        boolean enabledUserSmartRendering = false;
        ResourceResolver resolver = resource.getResourceResolver();
        UserPropertiesManager upm = resolver.adaptTo(UserPropertiesManager.class);

        if (upm != null) {
            try {
                final UserProperties userProperties = upm.getUserProperties(userId, strProfile);
                if (userProperties != null) {
                    final String[] propertyNames = userProperties.getPropertyNames();
                    if (propertyNames != null) {
                        for (final String property : propertyNames) {
                            if (property.equalsIgnoreCase(PN_PROFILE_SMART_RENDERING)) {
                                enabledUserSmartRendering = true;
                                String propertyValue =
                                    userProperties.getProperty(PN_PROFILE_SMART_RENDERING, null, String.class);
                                if (propertyValue != null && !"".equals(propertyValue)) {
                                    if (propertyValue.equalsIgnoreCase("on"))
                                        smartRenderingRetVal = true;
                                }
                                break;
                            }
                        }
                    }
                }

                if (!enabledUserSmartRendering) {
                    smartRenderingRetVal = TranslationVariables.isEnableSmartRendering();
                }

            } catch (RepositoryException ex) {
                LOG.error("TranslationSCFUtils.isSmartRenderingOn: " + ex.getMessage());
                return false;
            }
        }

        return smartRenderingRetVal;
    }

}
