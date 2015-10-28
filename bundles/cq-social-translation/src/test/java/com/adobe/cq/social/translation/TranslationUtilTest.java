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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.srp.SocialResource;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.granite.security.user.UserProperties;
import com.adobe.granite.security.user.UserPropertiesManager;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationException;
import com.adobe.granite.translation.api.TranslationManager;
import com.adobe.granite.translation.api.TranslationResult;
import com.adobe.granite.translation.api.TranslationService;
import com.adobe.granite.translation.api.TranslationService.TranslationServiceInfo;
import com.adobe.granite.translation.core.MachineTranslationCloudConfig;
import com.adobe.granite.translation.core.MachineTranslationUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * <code>TranslationUtilTest</code>...
 */

public class TranslationUtilTest {
    private static final Logger log = LoggerFactory.getLogger(TranslationUtilTest.class);
    private Resource m_resource;
    private ValueMap m_valueMap;
    private TranslationManager m_translationMgr;
    private ResourceResolver m_resourceResolver;
    private SocialUtils m_socialUtils;
    private Page m_page;
    private PageManager m_pageMgr;
    private TranslationService m_tranSvc;
    private UserPropertiesManager m_usrPropMgr;
    private UserProperties m_userProps;
    private MachineTranslationUtil m_mcTranslationUtil;
    private MachineTranslationCloudConfig m_mcTransCloudConfig;
    private TranslationResult m_tranResult;
    private TranslationServiceInfo m_tranSrvcInfo;

    private static TranslationConstants.ContentType m_contentType = TranslationConstants.ContentType.HTML;
    private static final String RESOURCE_PATH = "/nonugc/page/path";
    private static final String LANGUAGE_PROP = "language";
    private static final String AS_LANGUAGE_PROP = "mtlanguage";
    private static final String CONTENT = "This is a test";
    private static final String TRANSLATION_NODE_NAME = "translation";
    private static final String USER_ID = "user1";
    private static final String USER_LANGUAGE = "en";
    private final String[] properties = {LANGUAGE_PROP};
    private static final String STR_ATTR = "Translated By Microsoft";

    @Before
    public void setup() throws Exception {
        m_resource = mock(Resource.class);
        m_valueMap = mock(ValueMap.class);
        m_translationMgr = mock(TranslationManager.class);
        m_resourceResolver = mock(ResourceResolver.class);
        m_socialUtils = mock(SocialUtils.class);
        m_page = mock(Page.class);
        m_pageMgr = mock(PageManager.class);
        m_tranSvc = mock(TranslationService.class);
        m_usrPropMgr = mock(UserPropertiesManager.class);
        m_userProps = mock(UserProperties.class);
        m_mcTranslationUtil = mock(MachineTranslationUtil.class);
        m_mcTransCloudConfig = mock(MachineTranslationCloudConfig.class);
        m_tranResult = mock(TranslationResult.class);
        m_tranSrvcInfo = mock(TranslationServiceInfo.class);

        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resourceResolver);
        Mockito.when(m_resource.getPath()).thenReturn(RESOURCE_PATH);
        Mockito.when(m_resource.adaptTo(ValueMap.class)).thenReturn(m_valueMap);
        Mockito.when(m_resource.getChild(TRANSLATION_NODE_NAME)).thenReturn(m_resource);

        Mockito.when(m_resourceResolver.adaptTo(SocialUtils.class)).thenReturn(m_socialUtils);
        Mockito.when(m_resourceResolver.adaptTo(UserPropertiesManager.class)).thenReturn(m_usrPropMgr);
        Mockito.when(m_resourceResolver.map(RESOURCE_PATH)).thenReturn(RESOURCE_PATH);
        Mockito.when(m_resourceResolver.getUserID()).thenReturn(USER_ID);
        Mockito.when(m_resourceResolver.getResource(RESOURCE_PATH)).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.adaptTo(PageManager.class)).thenReturn(m_pageMgr);
        Mockito.when(m_resourceResolver.adaptTo(MachineTranslationUtil.class)).thenReturn(m_mcTranslationUtil);

        Mockito.when(m_page.getPath()).thenReturn(RESOURCE_PATH);
        Mockito.when(m_page.getParent(1)).thenReturn(m_page);
        Mockito.when(m_page.getLanguage(false)).thenReturn(null);
        Mockito.when(m_pageMgr.getContainingPage(m_resource)).thenReturn(m_page);

        Mockito.when(m_socialUtils.getContainingPage(m_resource)).thenReturn(m_page);
        Mockito.when(m_socialUtils.mapUGCPath(m_resource)).thenReturn(RESOURCE_PATH);
        Mockito.when(m_translationMgr.createTranslationService(m_resource)).thenReturn(m_tranSvc);
        Mockito.when(m_tranSvc.detectLanguage(CONTENT, m_contentType)).thenReturn(LANGUAGE_PROP);
        Mockito.when(m_tranSvc.getTranslationServiceInfo()).thenReturn(m_tranSrvcInfo);
        Mockito.when(m_tranSrvcInfo.getTranslationServiceAttribution()).thenReturn(STR_ATTR);

        Mockito.when(m_valueMap.get(LANGUAGE_PROP, (String) null)).thenReturn(LANGUAGE_PROP);
        Mockito.when(m_valueMap.get(LANGUAGE_PROP, "")).thenReturn(LANGUAGE_PROP);
        Mockito.when(m_usrPropMgr.getUserProperties(USER_ID, "profile")).thenReturn(m_userProps);
        Mockito.when(m_userProps.getProperty(LANGUAGE_PROP)).thenReturn(USER_LANGUAGE);
        Mockito.when(m_mcTranslationUtil.getAppliedMachineTranslationCloudConfigs(m_resource)).thenReturn(
            m_mcTransCloudConfig);
    }

    @Test
    public void testGetLanguageCodeWithNullTranslationService() throws TranslationException {
        final String strContent = null;
        Mockito.when(m_translationMgr.createTranslationService(m_resource)).thenReturn(null);

        final String strLangCode =
            TranslationUtil.getLanguageCode(strContent, m_contentType, m_resource, m_translationMgr);
        assertEquals(strLangCode, null);
    }

    @Test
    public void testGetLanguageCodeWithNullTranslationMgr() throws TranslationException {
        final String strLangCode = TranslationUtil.getLanguageCode(CONTENT, m_contentType, m_resource, null);
        assertNull(strLangCode);
    }

    @Test
    public void testGetLanguageCode() throws TranslationException {
        final String strLangCode =
            TranslationUtil.getLanguageCode(CONTENT, m_contentType, m_resource, m_translationMgr);
        assertEquals(strLangCode, LANGUAGE_PROP);
    }

    @Test
    public void testAddLanguageCodeWithNulls() {
        final Session session = null;
        final String property = null;
        final TranslationConstants.ContentType contentType = null;
        final TranslationManager tm = null;

        final String strLang = TranslationUtil.addLanguageCode(m_resource, session, property, contentType, tm);
        assertEquals(strLang, null);
    }

    @Test
    public void testAddLanguageCode() throws PersistenceException {
        final Session session = null;
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("jcr:primaryType", "sling:Folder");

        final ModifiableValueMap m_Map = mock(ModifiableValueMap.class);
        Mockito.when(m_resource.adaptTo(ModifiableValueMap.class)).thenReturn(m_Map);
        Mockito.when(m_Map.get(LANGUAGE_PROP, (String) null)).thenReturn(CONTENT);
        Mockito.when(m_resource.getChild(TRANSLATION_NODE_NAME)).thenReturn(null);
        Mockito.when(m_resourceResolver.create(m_resource, TRANSLATION_NODE_NAME, properties)).thenReturn(m_resource);

        final String strLang =
            TranslationUtil.addLanguageCode(m_resource, session, LANGUAGE_PROP, m_contentType, m_translationMgr);
        assertEquals(LANGUAGE_PROP, strLang);
    }

    @Test
    public void testDoDisplayTranslation() {
        final ClientUtilities m_clientUtils = mock(ClientUtilities.class);
        Mockito.when(m_clientUtils.getAuthorizedUserId()).thenReturn(USER_ID);

        final boolean isDisplayTranslation =
            TranslationUtil.doDisplayTranslation(m_resourceResolver, m_resource, m_clientUtils);
        assertFalse(isDisplayTranslation);
    }

    @Test
    public void testGetUserLanguageWithNullUserID() {
        final String usrLanguage = TranslationUtil.getUserLanguage(m_resourceResolver, null);
        assertEquals(USER_LANGUAGE, usrLanguage);
    }

    @Test
    public void testGetUserLanguage() {
        final String usrLanguage = TranslationUtil.getUserLanguage(m_resourceResolver, USER_ID);
        assertEquals(USER_LANGUAGE, usrLanguage);
    }

    @Test
    public void testGetUGCLanguage() {
        final String strUGCLang = TranslationUtil.getUGCLanguage(m_resource);
        assertEquals(LANGUAGE_PROP, strUGCLang);
    }

    @Test
    public void testGetUGCLanguageAS() {
        final SocialResource m_socialResource = Mockito.mock(SocialResource.class);
        Mockito.when(m_socialResource.getChild(TRANSLATION_NODE_NAME)).thenReturn(m_socialResource);
        Mockito.when(m_socialResource.adaptTo(ValueMap.class)).thenReturn(m_valueMap);
        Mockito.when(m_valueMap.containsKey(AS_LANGUAGE_PROP)).thenReturn(true);
        Mockito.when(m_valueMap.get(AS_LANGUAGE_PROP, (String) null)).thenReturn(AS_LANGUAGE_PROP);

        final String strUGCLang = TranslationUtil.getUGCLanguage(m_socialResource);
        assertEquals(AS_LANGUAGE_PROP, strUGCLang);
    }

    @Test
    public void testGetPageLanguage() {
        Mockito.when(m_mcTransCloudConfig.getUgcPath()).thenReturn("test/path");
        final String strPageLang = TranslationUtil.getPageLanguage(m_resourceResolver, m_resource, RESOURCE_PATH, "");
        assertEquals(USER_LANGUAGE, strPageLang);
    }

    @Test
    public void testGetResourceLanguage() {
        final String strLang = TranslationUtil.getResourceLanguage(m_resourceResolver, m_resource);
        assertEquals(strLang, null);
    }

    @Test
    public void testUriToResourcePath() {
        final String strUri = TranslationUtil.uriToResourcePath(RESOURCE_PATH);
        assertEquals(strUri, RESOURCE_PATH);
    }

    @Test
    public void testGetTranslationForSameLanguage() {
        assertEquals(
            TranslationUtil.getTranslation(USER_LANGUAGE, USER_LANGUAGE, CONTENT, m_resource, m_translationMgr), null);
    }

    @Test
    public void testGetTranslation() {
        com.adobe.granite.translation.api.TranslationResult result = null;
        result = TranslationUtil.getTranslation(USER_LANGUAGE, "fr", CONTENT, m_resource, m_translationMgr);
        assertEquals(result, null);
    }

    @Test
    public void testGetTranslationForPropertiesArray() {
        TranslationResults tranResult = null;
        Mockito.when(m_valueMap.get(LANGUAGE_PROP, "")).thenReturn(LANGUAGE_PROP);

        tranResult = TranslationUtil.getTranslation(USER_LANGUAGE, "fr", properties, m_resource, m_translationMgr);
        assertNotNull(tranResult);
    }

    @Test
    public void testGetTranslationForArrayWithAttribution() throws TranslationException {
        TranslationResults tranResult = null;
        final String[] properties = {LANGUAGE_PROP};

        try {
            Mockito.when(
                m_tranSvc.translateString(CONTENT, USER_LANGUAGE, "fr", TranslationConstants.ContentType.HTML,
                    "general")).thenReturn(m_tranResult);

            tranResult =
                TranslationUtil.getTranslation(USER_LANGUAGE, "fr", properties, m_resource, m_translationMgr);
            assertNotNull(tranResult);

        } catch (final TranslationException te) {
            log.error(te.toString());
            throw new TranslationException(te.toString(), te, null);
        }
    }

    @Test
    public void testGetTranslationUpdate() {
        assertEquals(
            TranslationUtil.getTranslationUpdate(USER_LANGUAGE, "fr", m_resource, m_translationMgr, properties), null);
    }

    @Test
    public void testSaveTranslation() throws Exception {
        final String TRANSLATION_NODE_NAME = "translation";
        final Map<String, String> translation = new HashMap<String, String>();
        final ModifiableValueMap m_Map = mock(ModifiableValueMap.class);
        final Map<String, Object> properties = new HashMap<String, Object>();

        Mockito.when(m_resource.getChild(TRANSLATION_NODE_NAME)).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.create(m_resource, TRANSLATION_NODE_NAME, null)).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.create(m_resource, USER_LANGUAGE, null)).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.create(m_resource, USER_LANGUAGE, properties)).thenReturn(m_resource);
        Mockito.when(m_resource.adaptTo(ModifiableValueMap.class)).thenReturn(m_Map);

        TranslationUtil.saveTranslation(USER_LANGUAGE, translation, m_resource, null);
    }

    @Test
    public void testTranslateOnSave() throws Exception {
        final TranslationResult m_transResult = mock(TranslationResult.class);
        final Session m_session = mock(Session.class);
        final ModifiableValueMap m_Map = mock(ModifiableValueMap.class);

        Mockito.when(
            m_tranSvc.translateString(LANGUAGE_PROP, "fr", USER_LANGUAGE, TranslationConstants.ContentType.HTML,
                "general")).thenReturn(m_transResult);
        Mockito.when(m_resource.getChild(USER_LANGUAGE)).thenReturn(m_resource);
        Mockito.when(m_resource.adaptTo(ModifiableValueMap.class)).thenReturn(m_Map);

        TranslationUtil.translateOnSave("fr", USER_LANGUAGE, m_resource, m_session, m_translationMgr, properties);
    }

    @Test
    public void testUGCTranslationMeta() {
        final UGCTranslationMeta result =
            TranslationUtil.ugcTranslationMeta(m_resourceResolver, m_resource, USER_LANGUAGE, properties,
                RESOURCE_PATH, USER_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetNonUgcResourceBasedOnResourcePath() {

        assertEquals(m_resource, TranslationUtil.getNonUgcResource(m_resource));
    }

    @Test
    public void testGetNonUgcResourceBasedOnJCRContent() {
        final String strJCRContent = "_jcr_content";
        final String strNonUgcPath = "/nonugc/page/path/_jcr_content";

        final String nonUgcPath = StringUtils.substringBeforeLast(strNonUgcPath, strJCRContent);
        Mockito.when(m_page.getPath()).thenReturn(nonUgcPath);
        Mockito.when(m_resourceResolver.resolve(nonUgcPath)).thenReturn(m_resource);
        assertEquals(m_resource, TranslationUtil.getNonUgcResource(m_resource));
    }

    @Test
    public void testStoreModifiedTranslation() {
        final String originalText = "Hola amigos";
        final String modifiedText = "Hello friends";

        final boolean retVal =
            TranslationUtil.storeModifiedTranslation(m_resource, m_translationMgr, originalText, "es", USER_LANGUAGE,
                modifiedText, m_contentType, USER_ID, 0);

        assertEquals(true, retVal);

    }

    @Test
    public void testBrowserLanguage() {
        final String acceptLanguage = "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4";

        assertEquals("en-US", TranslationUtil.getBrowserLanguage(acceptLanguage));
    }

}
