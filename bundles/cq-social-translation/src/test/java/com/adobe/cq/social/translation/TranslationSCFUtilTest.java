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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.adobe.cq.social.community.api.CommunityContext;
import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.cq.social.ugcbase.TranslationSaveQueue;
import com.adobe.cq.social.ugcbase.TranslationUpdate;
import com.adobe.granite.security.user.UserProperties;
import com.adobe.granite.security.user.UserPropertiesManager;
import com.adobe.granite.translation.api.TranslationException;
import com.adobe.granite.translation.api.TranslationManager;
import com.adobe.granite.translation.api.TranslationService;
import com.adobe.granite.translation.api.TranslationService.TranslationServiceInfo;

public class TranslationSCFUtilTest {
    private Resource m_resource;
    private ResourceResolver m_resourceResolver;
    private Session m_session;
    private ValueMap m_valueMap;
    private CommunityContext m_commContext;
    private UserPropertiesManager m_usrPropMgr;
    private UserProperties m_userProps;
    private SocialUtils m_socialUtils;
    private ClientUtilities m_clientUtilities;
    private SlingHttpServletRequest m_request;
    private final ConcurrentLinkedQueue<TranslationUpdate> concLinkedQ =
        new ConcurrentLinkedQueue<TranslationUpdate>();
    private TranslationService m_tranSvc;
    private TranslationServiceInfo m_tranSrvcInfo;

    @Mock
    private TranslationSaveQueue translationSaveQ;

    @Mock
    private TranslationManager tm;

    private static final String USER_ID = "user";
    private static final String LANGUAGE_PROP = "language";
    private static final String USER_LANGUAGE = "en";
    private static final String[] availableLangs = {"en", "fr"};
    private static final String AVAILABLE_LANGUAGES = "availableLanguages";
    private static final String sitePath = "/content/sites/en.html";
    private static final String PERSIST_TRANSLATION = "persistTranslation";
    private String requestURI = "http://localhost:4503/content/community-components/en/forum/";
    private String requestReferer = "/content/community-components/de/forum";
    private String requestLanguage = "en";

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        m_resource = mock(Resource.class);
        m_resourceResolver = mock(ResourceResolver.class);
        m_session = mock(Session.class);
        m_valueMap = mock(ValueMap.class);
        m_commContext = mock(CommunityContext.class);
        m_usrPropMgr = mock(UserPropertiesManager.class);
        m_userProps = mock(UserProperties.class);
        m_socialUtils = mock(SocialUtils.class);
        m_clientUtilities = mock(ClientUtilities.class);
        m_request = mock(SlingHttpServletRequest.class);
        m_tranSvc = mock(TranslationService.class);
        m_tranSrvcInfo = mock(TranslationServiceInfo.class);
        Boolean bool = new Boolean(true);
        TranslationVariables.setEnableAttribution(false);

        Mockito.when(m_valueMap.get("cq:lastModified", (Calendar) null)).thenReturn(new GregorianCalendar());
        Mockito.when(m_valueMap.get(TranslationUtil.TRANSLATION_DATE_PROP, (GregorianCalendar) null)).thenReturn(
            new GregorianCalendar());
        Mockito.when(m_resource.getChild(Mockito.anyString())).thenReturn(m_resource);
        Mockito.when(m_resource.adaptTo(CommunityContext.class)).thenReturn(m_commContext);
        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resourceResolver);
        Mockito.when(m_resource.adaptTo(ValueMap.class)).thenReturn(m_valueMap);
        Mockito.when(m_resourceResolver.getResource(Mockito.anyString())).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.adaptTo(UserPropertiesManager.class)).thenReturn(m_usrPropMgr);
        Mockito.when(m_valueMap.containsKey(Mockito.anyString())).thenReturn(true);
        Mockito.when(m_valueMap.get(PERSIST_TRANSLATION, (Object) null)).thenReturn(bool);
        Mockito.when(m_valueMap.get("allowMachineTranslation", Boolean.class)).thenReturn(bool);
        Mockito.when(m_valueMap.get(AVAILABLE_LANGUAGES, (String[]) null)).thenReturn(availableLangs);
        Mockito.when(m_valueMap.get(TranslationUtil.AS_LANGUAGE_PROP, (String) null)).thenReturn("fr");
        Mockito.when(m_usrPropMgr.getUserProperties(USER_ID, "profile")).thenReturn(m_userProps);
        Mockito.when(m_userProps.getProperty(LANGUAGE_PROP)).thenReturn(USER_LANGUAGE);
        Mockito.when(m_commContext.getSiteId()).thenReturn(sitePath);
        Mockito.when(m_commContext.getSitePath()).thenReturn(sitePath);
        Mockito.when(m_clientUtilities.isTranslationServiceConfigured(m_resource)).thenReturn(true);
        Mockito.when(m_clientUtilities.getAuthorizedUserId()).thenReturn(USER_ID);
        Mockito.when(m_clientUtilities.getRequest()).thenReturn(m_request);
        Mockito.when(m_clientUtilities.getRequest().getRequestURI()).thenReturn(requestURI);
        Mockito.when(m_clientUtilities.getRequest().getHeader("referer")).thenReturn(requestReferer);
        Mockito.when(m_clientUtilities.getSocialUtils()).thenReturn(m_socialUtils);
        Mockito.when(m_socialUtils.getTranslationManager()).thenReturn(this.tm);
        Mockito.when(tm.createTranslationService(m_resource)).thenReturn(m_tranSvc);
    }

    @Test
    public void testUpdateEditTranslaton() throws Exception {
        String modifiedTranslation = "Hello everyone";

        Mockito.when(m_valueMap.get("jcr:description", "")).thenReturn(modifiedTranslation);
        Mockito.when(m_resourceResolver.adaptTo(SocialUtils.class)).thenReturn(m_socialUtils);
        Mockito.when(this.translationSaveQ.getUpdateQueue()).thenReturn(concLinkedQ);

        TranslationSCFUtil.updateEditTranslation(m_resource, m_session, USER_ID, requestURI, requestReferer,
            requestLanguage, modifiedTranslation, translationSaveQ);

        Mockito.verify(this.tm, Mockito.times(1)).createTranslationService(m_resource);
        Mockito.verify(this.translationSaveQ, Mockito.times(1)).registerUpdate();
    }

    @Test
    public void testGetTranslationSCF() {

        assertNotNull(TranslationSCFUtil.getTranslationSCF(m_resource, m_clientUtilities));
    }

    @Test
    public void testGetTranslationSCFAndCacheResults() throws TranslationException {
        String text = "Hello everyone";

        Mockito.when(m_commContext.getSiteId()).thenReturn(null);
        Mockito.when(m_valueMap.get("jcr:title", "")).thenReturn(text);
        Mockito.when(m_valueMap.get("jcr:description", "")).thenReturn(text);

        TranslationVariables.setLanguageCode(availableLangs);
        Mockito.when(m_valueMap.get("cq:lastModified", (Calendar) null)).thenReturn(new GregorianCalendar());

        assertNotNull(TranslationSCFUtil.getTranslationSCF(m_resource, m_clientUtilities));
    }

    @Test
    public void testGetTranslationAttribution() {
        Mockito.when(m_tranSvc.getTranslationServiceInfo()).thenReturn(m_tranSrvcInfo);
        Mockito.when(m_tranSrvcInfo.getTranslationServiceAttribution()).thenReturn("Attributed by Translator.");
        TranslationVariables.setEnableAttribution(true);

        assertNotNull(TranslationSCFUtil.getTranslationAttribution(m_resource, m_clientUtilities));
    }

    @Test
    public void testIsSmartRenderingOnAdminOption() {
        TranslationVariables.setEnableSmartRendering(true);

        assertTrue(TranslationSCFUtil.isSmartRenderingOn(m_resource, m_clientUtilities));
    }

    @Test
    public void testIsSmartRenderingOnUserOption() throws RepositoryException {
        String PN_PROFILE_SMART_RENDERING = "smartRendering";
        String[] properties = {PN_PROFILE_SMART_RENDERING};
        Mockito.when(m_userProps.getPropertyNames()).thenReturn(properties);
        Mockito.when(m_userProps.getProperty(PN_PROFILE_SMART_RENDERING, null, String.class)).thenReturn("on");

        assertTrue(TranslationSCFUtil.isSmartRenderingOn(m_resource, m_clientUtilities));
    }

}
