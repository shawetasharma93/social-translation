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

package com.adobe.cq.social.translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.community.api.CommunityContext;
import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.srp.SocialResource;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.cq.social.ugcbase.TranslationUpdate;
import com.adobe.granite.security.user.UserProperties;
import com.adobe.granite.security.user.UserPropertiesManager;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationException;
import com.adobe.granite.translation.api.TranslationManager;
import com.adobe.granite.translation.api.TranslationResult;
import com.adobe.granite.translation.api.TranslationService;
import com.adobe.granite.translation.core.MachineTranslationCloudConfig;
import com.adobe.granite.translation.core.MachineTranslationUtil;
import com.day.cq.commons.LanguageUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * Translation util class.
 */
public class TranslationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(TranslationUtil.class);

    public static final String TRANSLATION_NODE_NAME = "translation";
    public static final String LANGUAGE_PROP = "language";
    public static final String AS_LANGUAGE_PROP = "mtlanguage";
    public static final String TRANSLATION_DATE_PROP = "translationDate";
    public static final String LANGUAGE_DETECTED_DATE_PROP = "languageDetectedDate";
    public static final String POST_EDITED_PROP = "postEdited";
    public static final String JCR_CONTENT_STR = "_jcr_content";
    public static final String USER_PROFILE = "profile";
    public static final String USER_PREFERENCES = "preferences";
    public static final String CONTENT_CATEGORY = "general";
    public static final String UGC_WRITER = "ugc-writer";
    public static final String AVAILABLE_LANGUAGES = "availableLanguages";
    public static final String BROWSER_LANGUAGE = "Accept-Language";

    /**
     * @param resource The UGC node that has been detected the language code
     * @param session The session to save the changes. If null, the session will not be saved.
     * @return String Return the language code, or null if the language lookup is non deterministic.
     */
    public static String addLanguageCode(final Resource resource, final Session session, final String property,
        final TranslationConstants.ContentType contentType, final TranslationManager tm) {
        LOG.trace("In function: addLanguageCode");
        String lanCode = null;
        final ValueMap resourceProperties = resource.adaptTo(ModifiableValueMap.class);
        String determineString = null;
        // determine the language using the content with the max length of the properties.
        if (property != null) {
            determineString = resourceProperties.get(property, (String) null);
        }
        try {
            if (determineString != null) {
                lanCode = getLanguageCode(determineString, contentType, resource, tm);

                // We will allow for null sessions here, in case the calling class wants to do a non
                // save-operation
                if (lanCode != null) {
                    final Resource translationResource = getTranslationNode(resource, session, true);
                    if (translationResource != null) {
                        final ValueMap translationResourceProperties =
                            translationResource.adaptTo(ModifiableValueMap.class);

                        // TODO: Need to verify if language detection will happen on AS side or not.
                        // 'language' is a reserverd property on AS side.
                        if (resource instanceof SocialResource) {
                            translationResourceProperties.put(AS_LANGUAGE_PROP, lanCode);
                        } else {
                            translationResourceProperties.put(LANGUAGE_PROP, lanCode);
                        }
                        translationResource.getResourceResolver().commit();

                        if (session != null) {
                            session.save();
                        }
                    }
                } else {
                    LOG.debug("Either the session or the translation node were null, not saving");
                }

            }
        } catch (final RepositoryException e) {
            LOG.error("Error setting language property", e);
        } catch (final PersistenceException pe) {
            LOG.error("Error setting language property.", pe);
        }

        return lanCode;
    }

    /**
     * This function returns the Resource corresponding to the "Translation" node (child node of the jcr:content node)
     * @param resource the Resource to add or get the child node from
     * @return Resource corresponding to the child "translation" node
     */
    private static Resource getTranslationNode(final Resource resource, final Session session, final boolean bCreate) {
        LOG.trace("In getTranslationNode");

        Resource translationNode = null;

        try {
            if (resource != null) {
                translationNode = resource.getChild(TRANSLATION_NODE_NAME);

                if (translationNode == null && bCreate) {
                    LOG.debug("Didn't find Translation node, creating");

                    final Map<String, Object> properties = new HashMap<String, Object>();
                    properties.put("jcr:primaryType", "sling:Folder");

                    final ResourceResolver rr = resource.getResourceResolver();
                    translationNode = rr.create(resource, TRANSLATION_NODE_NAME, properties);
                    rr.commit();
                }
                if (session != null) {
                    LOG.debug("Saving session");

                    session.save();
                }
            }
        } catch (final Exception ex) {
            LOG.warn(ex.getMessage() + " " + ex.toString());
            LOG.error("Error while getting/creating Translation node.", ex);
        }

        return translationNode;
    }

    /**
     * @param content The content to get language detected.
     * @return String Return the language code
     */
    public static String getLanguageCode(final String content, final TranslationConstants.ContentType contentType,
        final Resource resource, final TranslationManager tm) {
        LOG.debug("In function: getLanguageCode");
        String lanCode = null;
        try {

            if (tm != null) {

                final TranslationService ts = tm.createTranslationService(getNonUgcResource(resource));

                if (ts != null) {
                    lanCode = ts.detectLanguage(content, contentType);
                } else {
                    LOG.debug("TranslationService was null for resource: {}", resource.getPath());
                }
            } else {
                LOG.debug("TranslationManager was null, returning null for detected string");
            }
        } catch (final TranslationException te) {
            if (te.getErrorCode() == TranslationException.ErrorCode.MISSING_CREDENTIALS) {
                LOG.debug("Applied credentials were null or blank.  The default configuration might be applied");
            } else {
                LOG.error(te.toString());
            }
        }
        LOG.debug("lanCode: {}", lanCode);
        return lanCode;
    }

    public static Resource getNonUgcResource(final Resource resource) {
        LOG.trace("In Function: getNonUgcResource");

        final ResourceResolver rr = resource.getResourceResolver();
        final SocialUtils socialUtils = rr.adaptTo(SocialUtils.class);

        if (socialUtils != null) {
            final Page nonUgcPage = socialUtils.getContainingPage(resource);

            if (nonUgcPage != null) {
                String nonUgcPath = nonUgcPage.getPath();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("resource.getPath(): {}", resource.getPath());
                    LOG.debug("nonUgcPath: {}", nonUgcPath);
                }

                final String resourcePath = socialUtils.mapUGCPath(resource);
                if (StringUtils.equals(resourcePath, nonUgcPath)) {
                    return resource;
                } else {
                    LOG.debug("Using non UGC resource");
                    if (StringUtils.contains(nonUgcPath, JCR_CONTENT_STR)) {
                        nonUgcPath = StringUtils.substringBeforeLast(nonUgcPath, JCR_CONTENT_STR);
                    }

                    LOG.debug("nonUgcPath AFTER: {}", nonUgcPath);
                    final Resource nonUgcResource = rr.resolve(nonUgcPath);
                    if (nonUgcResource != null) {
                        return nonUgcResource;
                    } else {
                        LOG.warn("Cannot get the associated content resource");
                    }
                }
            }
        }
        return resource;
    }

    /**
     * Gets the UGCTranslationMeta of the current node and determines if show the translation button Show translation
     * button only when UGC and page languages are valid and different.
     * @param resourceResolver ResourceResolver of the UGC node
     * @param resource Resource of the UGC node
     * @return boolean True if display the translation button.
     */
    public static boolean doDisplayTranslation(final ResourceResolver resourceResolver, final Resource resource,
        final ClientUtilities clientUtils) {
        if (resourceResolver == null) {
            return false;
        }

        if (!clientUtils.isTranslationServiceConfigured(resource)) {
            return false;
        }

        final String ugcLan = getUGCLanguage(resource);

        if (ugcLan == null) {
            return false;
        }

        boolean languageSupported = false;
        CommunityContext context = resource.adaptTo(CommunityContext.class);

        if (context == null)
            return false;

        String ALLOW_MC_TRANSLATION = "allowMachineTranslation";
        String[] availableLanguages = null;

        String siteID = context.getSiteId();
        if (siteID != null && !"".equals(siteID)) {
            String sitePath = context.getSitePath();
            final Resource siteResource = resource.getResourceResolver().getResource(sitePath);

            if (siteResource != null) {
                final ValueMap configProperties = siteResource.adaptTo(ValueMap.class);
                Boolean allowTranslation = configProperties.get(ALLOW_MC_TRANSLATION, Boolean.class);

                if (allowTranslation == null || !allowTranslation.booleanValue()) {
                    LOG.trace("TranslationUtils.doDisplayTranslation: Machine Translation not allowed, returning false.");
                    return false;
                }
                availableLanguages = configProperties.get(AVAILABLE_LANGUAGES, (String[]) null);
            }
        } else {
            availableLanguages = TranslationVariables.getLanguageCode();
        }

        /**
         * Iterate through supported languages from the OSGI config, and make sure that the matching language is
         * supported
         */
        for (final String supportLan : availableLanguages) {
            if (languagesAreEquivalent(supportLan, ugcLan)) { // if the langauge is supported in felix console options
                languageSupported = true;
                break;
            }
        }

        String toLang = null;
        if (clientUtils != null) {
            LOG.debug("Comment user: {}", clientUtils.getAuthorizedUserId());
            String strRequestURI = null, strReferer = null, strRequestLanguage = null;
            if (clientUtils.getRequest() != null) {
                strRequestURI = clientUtils.getRequest().getRequestURI();
                strReferer = clientUtils.getRequest().getHeader("referer");
                strRequestLanguage = clientUtils.getRequest().getHeader(BROWSER_LANGUAGE);
            }
            toLang =
                getTranslationTargetLanguage(resourceResolver, getNonUgcResource(resource),
                    clientUtils.getAuthorizedUserId(), strRequestURI, strReferer, strRequestLanguage);
        }

        if (toLang == null) {
            return false;
        }
        return languageSupported && !languagesAreEquivalent(ugcLan, toLang);
    }

    public static String checkLanguageCode(String toLang, final Resource resource) {

        String[] availableLanguages = null;
        CommunityContext context = resource.adaptTo(CommunityContext.class);
        String siteID = context.getSiteId();
        String sitePath = context.getSitePath();

        if (siteID != null && !"".equals(siteID)) {
            final Resource siteResource = resource.getResourceResolver().getResource(sitePath);
            if (siteResource != null) {
                final ValueMap configProperties = siteResource.adaptTo(ValueMap.class);
                availableLanguages = configProperties.get(AVAILABLE_LANGUAGES, (String[]) null);
            }
        } else {
            availableLanguages = TranslationVariables.getLanguageCode();
        }

        if (toLang.length() > 2) {  // Check if the language code is a 5 character code

            // Check If languge is present with different name like zh_Tw and zh-tw
            for (int i = 0; i < availableLanguages.length; i++) {
                if (availableLanguages[i].length() > 2) {
                    String toLangLeftStr = toLang.substring(0, 2);
                    String toLangRightStr = toLang.substring(3, toLang.length());

                    String avlblLeftStr = availableLanguages[i].substring(0, 2);
                    String avlblRightStr = availableLanguages[i].substring(3, availableLanguages[i].length());

                    if (toLangLeftStr.equalsIgnoreCase(avlblLeftStr)
                            && toLangRightStr.equalsIgnoreCase(avlblRightStr)) {
                        toLang = availableLanguages[i];
                        return toLang;
                    }
                }
            }

            // If 5 character code is not present check for the base language i.e, 2 length language code
            String baseLanguage = toLang.substring(0, 2);
            if (Arrays.asList(availableLanguages).contains(baseLanguage))
                return baseLanguage;

        }

        return toLang;
    }

    public static String getTranslationTargetLanguage(final ResourceResolver resourceResolver,
        final Resource resource, final String strUserID, final String strRequestURI, final String strReferer,
        final String strRequestAcceptLanguage) {
        LOG.trace("In getTranslationTargetLanguage");
        String toLang = getUserLanguage(resourceResolver, strUserID);
        if (toLang == null || "".equals(toLang)) {
            toLang = getBrowserLanguage(strRequestAcceptLanguage);

            if (StringUtils.isEmpty(toLang)) {
                toLang = getPageLanguage(resourceResolver, getNonUgcResource(resource), strRequestURI, strReferer);
            }
        }

        toLang = changeLanguageCode(toLang);
        String[] availableLanguages = null;
        CommunityContext context = resource.adaptTo(CommunityContext.class);
        String siteID = context.getSiteId();
        String sitePath = context.getSitePath();

        if (siteID != null && !"".equals(siteID)) {
            final Resource siteResource = resource.getResourceResolver().getResource(sitePath);
            if (siteResource != null) {
                final ValueMap configProperties = siteResource.adaptTo(ValueMap.class);
                availableLanguages = configProperties.get(AVAILABLE_LANGUAGES, (String[]) null);
            }
        } else {
            availableLanguages = TranslationVariables.getLanguageCode();
        }

        Boolean languagePresent = Arrays.asList(availableLanguages).contains(toLang);
        if (null != toLang && availableLanguages != null && !languagePresent) {
            toLang = checkLanguageCode(toLang, resource);
        }
        LOG.debug("Returning toLang: {}", toLang);
        return toLang;
    }

    public static String getUserLanguage(final Resource resource, final ResourceResolver resourceResolver,
        String userId) {
        String userLanguage = "";

        final UserPropertiesManager userPropertiesManager = resourceResolver.adaptTo(UserPropertiesManager.class);

        if (userPropertiesManager != null) {

            try {
                UserProperties userProps = null;
                if (userId != null && !"".equals(userId)) {
                    userProps = userPropertiesManager.getUserProperties(userId, USER_PROFILE);
                } else {
                    userId = resourceResolver.getUserID();
                    LOG.debug("Getting language for user {}", userId);
                    userProps = userPropertiesManager.getUserProperties(userId, USER_PROFILE);
                }
                if (userProps != null) {
                    userLanguage = userProps.getProperty(LANGUAGE_PROP);
                    if (StringUtils.isEmpty(userLanguage)) {
                        userProps = userPropertiesManager.getUserProperties(userId, USER_PREFERENCES);
                        if (userProps != null) {
                            userLanguage = userProps.getProperty(LANGUAGE_PROP);
                        }
                    }

                    if (!StringUtils.isEmpty(userLanguage) && resource != null) {
                        userLanguage = checkLanguageCode(userLanguage, resource);
                    }
                }
            } catch (final RepositoryException re) {
                LOG.error(re.toString());
            }
        }

        return userLanguage;
    }

    public static String getUserLanguage(final ResourceResolver resourceResolver, String userId) {
        return getUserLanguage(null, resourceResolver, userId);
    }

    public static String getUGCLanguage(final Resource resource) {
        ValueMap properties = null;
        String retVal = null;
        if (resource != null) {
            final Resource translationNode = getTranslationNode(resource, null, false);
            if (translationNode != null) {
                properties = translationNode.adaptTo(ValueMap.class);

                String strLanguageKey = AS_LANGUAGE_PROP;
                if (!properties.containsKey(AS_LANGUAGE_PROP)) {
                    strLanguageKey = LANGUAGE_PROP;
                }
                retVal = properties.get(strLanguageKey, (String) null);
                return retVal;
            } else {
                LOG.debug("translationNode was null");
            }
        } else {
            LOG.debug("resource was null");
        }

        return retVal;
    }

    /**
     * First the uri is checked to see if it is a request to /content/usergenerated, if it is NOT the uri page locale
     * is used if it is we check if the "Common Store" solution is applied to the ugc resource if the "Common Store"
     * is NOT applied we use the locale of the ugc resource if it is applied, we use the result of getUserLanguage()
     * if there is no applied user language we use the referer uri for locale.
     * @param resourceResolver
     * @param ugcResource
     * @param uri
     * @param referer
     * @return
     */
    public static String getPageLanguage(final ResourceResolver resourceResolver, final Resource ugcResource,
        final String uri, final String referer) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("uri: {}", uri);
            LOG.debug("referer: {}", referer);
        }

        if (resourceResolver == null) {
            LOG.warn("resourceResolver was null!");
            return null;
        }

        String pageLang = "";

        if (uri != null && !"".equals(uri)) {
            final String uriResourcePath = uriToResourcePath(uri);
            LOG.debug("uriResourcePath: {}", uriResourcePath);

            if (!uriResourcePath.startsWith(SocialUtils.PATH_UGC)) {

                final Resource uriResource =
                    resourceResolver.getResource(LanguageUtil.getLanguageRoot(uriResourcePath));

                if (uriResource != null) {
                    LOG.debug("Getting pageLang from uri");
                    pageLang = getResourceLanguage(resourceResolver, uriResource);
                }
            }
        }

        if (pageLang == null || "".equals(pageLang)) {
            // Check if common store is applied to the commentsNode resource
            final Resource commentsNodeResource = getNonUgcResource(ugcResource);

            final MachineTranslationUtil mtu = resourceResolver.adaptTo(MachineTranslationUtil.class);
            final MachineTranslationCloudConfig cloudConfig =
                mtu.getAppliedMachineTranslationCloudConfigs(commentsNodeResource);

            String strCloudConfigUgcPath = null;
            if (cloudConfig != null) {
                strCloudConfigUgcPath = cloudConfig.getUgcPath();
            }
            if (strCloudConfigUgcPath != null && !"".equals(strCloudConfigUgcPath)) {
                LOG.debug("Getting pageLang from user");
                pageLang = getUserLanguage(resourceResolver, null);
            } else {
                LOG.debug("Getting pageLang from UGC");
                pageLang = getResourceLanguage(resourceResolver, ugcResource);
            }
        }

        if (pageLang == null || "".equals(pageLang)) {
            LOG.debug("Getting pageLang from referer");
            final String refererPath = uriToResourcePath(referer);
            pageLang =
                getResourceLanguage(resourceResolver,
                    resourceResolver.getResource(LanguageUtil.getLanguageRoot(refererPath)));
        }

        LOG.debug("Returning pageLang: {}", pageLang);
        return pageLang;
    }

    public static String getResourceLanguage(final ResourceResolver rr, final Resource resource) {
        final PageManager pm = rr.adaptTo(PageManager.class);
        final Page containingPage = pm != null ? pm.getContainingPage(resource) : null;
        if (containingPage == null) {
            return null;
        }

        final Locale language = containingPage.getLanguage(false);
        if (language == null) {
            return null;
        } else {
            return language.toString();
        }

    }

    public static String uriToResourcePath(final String uri) {
        if (uri == null || "".equals(uri)) {
            return null;
        }

        String resourcePath = "";
        final int slashslash = uri.indexOf("//");
        if (slashslash > -1) {
            resourcePath = uri.substring(uri.indexOf('/', slashslash + 2));
        } else {
            resourcePath = uri;
        }

        return StringUtils.substringBefore(resourcePath, ".");
    }

    /**
     * Gets the UGC translation information including what to translate, ugc language and page language.
     * @param resource From User generated node
     * @param resourceResolver The Resource Resolver
     * @return UGCTranslationMeta
     */
    public static UGCTranslationMeta ugcTranslationMeta(final ResourceResolver resourceResolver,
        final Resource resource, final String toLanguage, final String[] properties, final String pagePath,
        final String userId) {
        LOG.trace("In function: ugcTranslationMeta");
        final UGCTranslationMeta meta = new UGCTranslationMeta();
        String ugcLan = "";
        String pageLanguage = toLanguage;
        if (pageLanguage == null || "".equals(pageLanguage)) {
            if (userId != null && !"".equals(userId)) {
                pageLanguage = getUserLanguage(resourceResolver, userId);
            }
            if (pageLanguage == null || "".equals(pageLanguage)) {
                pageLanguage = getPageLanguage(resourceResolver, getNonUgcResource(resource), pagePath, null);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("pageLanguage: {}", pageLanguage);
            LOG.debug("resource path: {}", resource.getPath());
        }

        meta.setPageLanguage(pageLanguage);

        ugcLan = getUGCLanguage(resource);

        if (ugcLan != null) {
            LOG.debug("ugcLan: {}", ugcLan);
            for (final String supportLan : TranslationVariables.getLanguageCode()) {
                if (languagesAreEquivalent(supportLan, ugcLan)) {
                    // if the language is supported in felix console options
                    meta.setUgcLanuage(ugcLan);
                }
            }
        } else {
            LOG.warn("ugcLang returned null");
        }
        if (properties != null && properties.length > 0) {
            final ValueMap commentProperties = resource.adaptTo(ValueMap.class);
            if (LOG.isDebugEnabled()) {
                LOG.debug("commentProperties: {}", commentProperties.keySet());
            }
            final ArrayList<String> toBeTranslated = new ArrayList<String>();
            for (final String property : properties) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Checking for property: {}", property);
                }
                final String prop = commentProperties.get(property, (String) null);
                if (prop != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding prop {} to toBeTranslated ArrayList", prop);
                    }
                    toBeTranslated.add(prop);
                }
            }
            meta.setToBeTranslated(toBeTranslated);
        } else {
            LOG.warn("properties was null");
        }

        return meta;
    }

    /**
     * @param fromLang From which language
     * @param toLang To which language
     * @param str The string to translate
     * @param resource the resource
     * @param tm translation manager for translations
     * @return TranslationResult
     */
    public static com.adobe.granite.translation.api.TranslationResult getTranslation(final String fromLang,
        final String toLang, final String str, final Resource resource, final TranslationManager tm) {
        LOG.debug("In function: getTranslationFromAPI");
        com.adobe.granite.translation.api.TranslationResult result = null;

        String fromLangTemp = changeLanguageCode(fromLang);
        String toLangTemp = changeLanguageCode(toLang);

        if (languagesAreEquivalent(fromLangTemp, toLangTemp)) {
            LOG.debug("From language is the same as to language, returning null TranslationResult", fromLangTemp);
            return null;
        }

        try {

            if (tm != null) {

                final TranslationService ts = tm.createTranslationService(getNonUgcResource(resource));

                if (ts != null) {
                    result =
                        ts.translateString(str, fromLangTemp, toLangTemp, TranslationConstants.ContentType.HTML,
                            CONTENT_CATEGORY);
                } else {
                    LOG.warn("Failed to get a Translation Service for resource path: {}", resource.getPath());
                }
            } else {
                LOG.warn("TranslationManager was null");
            }
        } catch (final TranslationException te) {
            LOG.error(te.toString());
        }

        return result;
    }

    /**
     * @param langCode
     * @param bool just to check whether to consider norwegian or not
     * @return
     */
    private static String changeLanguageCode(final String langCode) {

        if (!StringUtils.isEmpty(langCode)) {
            if (langCode.equals("in"))
                return "id";               // For indonesian

            if (langCode.equals("iw"))
                return "he";               // For hebrew
        }
        return langCode;
    }

    /**
     * @param toLan To which language
     * @param translation The string to translate
     * @param commentResource The comment node resource to translate
     * @param adminSession Admin Session to save the changes in jcr. If the admin session is null, then the session
     *            will not be saved and no version will be checked in.
     */
    public static void saveTranslation(final String toLan, final Map<String, String> translation,
        final Resource commentResource, final Session adminSession) {
        try {
            LOG.trace("Inside fn saveTranslation");

            if (commentResource != null) {
                final Resource translationResource = getTranslationNode(commentResource, adminSession, true);

                if (translationResource != null) {
                    Resource languageResource = translationResource.getChild(toLan);
                    if (languageResource == null) {
                        final Map<String, Object> properties = new HashMap<String, Object>();
                        final ResourceResolver rr = translationResource.getResourceResolver();
                        languageResource = rr.create(translationResource, toLan, properties);
                        rr.commit();
                    }

                    if (languageResource != null) {
                        final ValueMap languageResourceProperties =
                            languageResource.adaptTo(ModifiableValueMap.class);
                        for (final String s : translation.keySet()) {
                            languageResourceProperties.put(s, translation.get(s));
                        }

                        languageResourceProperties.put(TRANSLATION_DATE_PROP, new GregorianCalendar());
                        languageResourceProperties.put(POST_EDITED_PROP, false);
                        languageResource.getResourceResolver().commit();
                    }

                    if (adminSession != null) {
                        adminSession.save();
                    }
                } else {
                    LOG.trace("Translation node is null");
                }
            } else {
                LOG.trace("comment Resource is null");
            }
        } catch (final PersistenceException ex) {
            LOG.error("Error while saving translation", ex);
        } catch (final RepositoryException ex) {
            LOG.error("Error while saving translation", ex);
        }
    }

    /**
     * @param fromLan From which language
     * @param toLan To which language
     * @param resource The comment node to translate
     * @param session Session to save the changes
     */
    public static void translateOnSave(final String fromLan, final String toLan, final Resource resource,
        final Session session, final TranslationManager tm, final String[] properties) {
        LOG.debug("Resource path: {}", resource.getPath());
        final TranslationResults translation = getTranslation(fromLan, toLan, properties, resource, tm);
        if ("Success".equals(translation.getStatus())) {
            TranslationUtil.saveTranslation(toLan, translation.getTranslation(), resource, session);
        }
    }

    /**
     * A functional implementation of a Translation Update - with no mutable side effects.
     * @param fromLan From which language
     * @param toLan To which language
     * @param resource The comment node to translate
     * @param tm TranslationManager service
     * @param properties Translation properties
     * @return TranslationUpdate encapsulating the results of the translation, or null
     */
    public static TranslationUpdate getTranslationUpdate(final String fromLan, final String toLan,
        final Resource resource, final TranslationManager tm, final String[] properties) {
        final TranslationResults translation = getTranslation(fromLan, toLan, properties, resource, tm);
        if ("Success".equals(translation.getStatus())) {
            return new TranslationUpdate(resource.getPath(), toLan, translation.getTranslation());
        } else {
            return null;
        }
    }

    /**
     * @param fromLan From which language
     * @param toLan To which language
     * @return TranslationResult
     */
    public static TranslationResults getTranslation(final String fromLan, final String toLan,
        final String[] properties, final Resource resource, final TranslationManager tm) {
        TranslationResults result = null;
        if (languagesAreEquivalent(fromLan, toLan)) {
            LOG.trace("From language was the same as to Language, returnin gnull TranslatioNResults", toLan);
            return null;
        }
        String resultStatus = "";
        boolean hasSuccess = false; // once there's one or more property get translated successfully.
        boolean failed = false; // once there's one property failed to be translated, not persist.

        /**
         * We only want to process properties that exist on the resource, and aren't empty strings
         */
        final Map<String, String> propertiesThatExistOnResource = new HashMap<String, String>();
        final ValueMap resourceValueMap = resource.adaptTo(ValueMap.class);
        for (final String property : properties) {
            final String tempValue = resourceValueMap.get(property, "");
            /**
             * An empty string is not a canidate for translation
             */
            if (!tempValue.isEmpty()) {
                propertiesThatExistOnResource.put(property, tempValue);
            }
        }

        final Map<String, String> translated = new HashMap<String, String>();
        final Set<Entry<String, String>> entrySet = propertiesThatExistOnResource.entrySet();
        if (entrySet.size() > 0) {
            for (final Entry<String, String> thisEntry : entrySet) {
                int tried = 0;
                final String property = thisEntry.getKey();
                TranslationResult translationResult = null;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("property: {}", property);
                }
                final String temp = thisEntry.getValue();
                translationResult = TranslationUtil.getTranslation(fromLan, toLan, temp, resource, tm);
                if (translationResult != null) {
                    translated.put(property, translationResult.getTranslation());
                    hasSuccess = true;
                } else {
                    translated.put(property, temp);
                    failed = true;
                }
            }
            if (hasSuccess && failed) {
                resultStatus = "Partial Success";
            }
            if (hasSuccess && !failed) {
                resultStatus = "Success";
            }
            if (!hasSuccess) {
                resultStatus = "Fail";
            }
        } else {
            resultStatus = "Not processing empty properties / keySet";
        }

        // TODO: Remove attribution after SCF is implemented for translation
        String attribution = null;
        try {
            attribution =
                tm.createTranslationService(getNonUgcResource(resource)).getTranslationServiceInfo()
                    .getTranslationServiceAttribution();
        } catch (final Exception e) {
            LOG.trace("error getting attribution ", e);
        }
        result = new TranslationResults(resultStatus, translated, attribution);
        return result;
    }

    /**
     * Compares the language codes to determine if they are equivalent
     * @param fromLan Language code to compare from
     * @param toLan Language code to compare to
     * @return
     */
    public static Boolean languagesAreEquivalent(final String fromLan, final String toLan) {

        /**
         * To be null safe
         */
        if (fromLan == null || toLan == null) {
            return false;
        }
        final int fromLength = fromLan.length();
        final int toLength = toLan.length();

        /**
         * If the language codes are less than 2 in length, then they are not valid; regardless
         */
        if (fromLength < 2 || toLength < 2) {
            return false;
        }

        /**
         * First we check if both language codes are the same length. If they are, then we want to strictly match the
         * language codes Otherwise, we will match the first two characters. We already know that the strings are long
         * enough to do this substring match
         */
        if (fromLength == toLength) {
            return StringUtils.equalsIgnoreCase(fromLan, toLan);
        } else {
            return StringUtils.equalsIgnoreCase(fromLan.substring(0, 2), toLan.substring(0, 2));
        }

    }

    public static boolean storeModifiedTranslation(final Resource resource, final TranslationManager transMgr,
        final String originalText, final String srcLang, final String targetLang, final String modifiedText,
        final TranslationConstants.ContentType contentType, final String userID, final int rating) {

        boolean retVal = false;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside storeModifiedTranslations");
            LOG.debug("originalText: {}", originalText);
            LOG.debug("sourceLanguage: {}", srcLang);
            LOG.debug("targetLanguage: {}", targetLang);
            LOG.debug("modifiedTranslatedText: {}", modifiedText);
            LOG.debug("userID: {}", userID);
            LOG.debug("rating: {}", rating);
        }

        try {
            if (resource != null && transMgr != null) {
                final Resource nonugcResource = TranslationUtil.getNonUgcResource(resource);
                final TranslationService transSrvc = transMgr.createTranslationService(nonugcResource);

                if (transSrvc != null) {
                    transSrvc.storeTranslation(originalText, srcLang, targetLang, modifiedText, contentType,
                        CONTENT_CATEGORY, userID, rating, resource.getPath());

                    retVal = true;
                }

            }
        } catch (final TranslationException ex) {
            LOG.error("Error while updating modified translations. ", ex.toString());
        }

        return retVal;
    }

    public static String getBrowserLanguage(final String languageRequestHeader) {
        String browserLanguage = null;
        String langDelimiter = ",";
        String preferenceDelimiter = ";";

        if (StringUtils.isNotEmpty(languageRequestHeader)) {
            if (languageRequestHeader.contains(langDelimiter)) {

                for (String language : languageRequestHeader.split(langDelimiter)) {
                    if (!language.contains(preferenceDelimiter)) {
                        browserLanguage = language;
                        break;
                    }
                }
            } else {
                browserLanguage = languageRequestHeader;
            }
        }
        return browserLanguage;
    }

}
