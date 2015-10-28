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

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.ClientUtilityFactory;
import com.adobe.cq.social.scf.QueryRequestInfo;
import com.adobe.cq.social.translation.api.SiteTranslationLanguage;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.granite.xss.XSSAPI;

public class SiteTranslationLanguageSocialComponentFactoryTest {

    private SiteTranslationLanguageSocialComponentFactory translationFactory;
    private Resource m_resource;
    private ClientUtilities m_clientUtils;
    private ResourceResolver m_resolver;
    private SlingHttpServletRequest m_request;
    ClientUtilityFactory m_clientUtilFactory;
    private QueryRequestInfo m_queryInfo;

    @Before
    public void setUp() throws Exception {
        m_resource = Mockito.mock(Resource.class);
        m_clientUtils = Mockito.mock(ClientUtilities.class);
        m_resolver = Mockito.mock(ResourceResolver.class);
        m_request = Mockito.mock(SlingHttpServletRequest.class, Mockito.RETURNS_DEEP_STUBS);
        m_clientUtilFactory = Mockito.mock(ClientUtilityFactory.class);
        m_queryInfo = Mockito.mock(QueryRequestInfo.class);
        translationFactory = new SiteTranslationLanguageSocialComponentFactory();

        Mockito.when(
            m_clientUtilFactory.getClientUtilities(Matchers.isNull(XSSAPI.class),
                Matchers.any(ResourceResolver.class), Matchers.isNull(SocialUtils.class))).thenReturn(m_clientUtils);

        FieldUtils.writeField(translationFactory, "clientUtilFactory", m_clientUtilFactory, true);
        Mockito.when(m_clientUtils.getRequest()).thenReturn(m_request);
        Mockito.when(m_request.getResource()).thenReturn(m_resource);
        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resolver);
    }

    @Test
    public void testGetSocialComponentFromResource() {
        SiteTranslationLanguage siteTranslation =
            (SiteTranslationLanguage) translationFactory.getSocialComponent(m_resource);

        Assert.assertNotNull(siteTranslation);
    }

    @Test
    public void testGetSocialComponentFromRequest() {
        SiteTranslationLanguage siteTrans =
            (SiteTranslationLanguage) translationFactory.getSocialComponent(m_resource, m_request);

        Assert.assertNotNull(siteTrans);
    }

    @Test
    public void testGetSocialComponentFromQueryInfo() {
        SiteTranslationLanguage siteTranslation =
            (SiteTranslationLanguage) translationFactory.getSocialComponent(m_resource, m_clientUtils, m_queryInfo);

        Assert.assertNotNull(siteTranslation);
    }

    @Test
    public void testGetSupportedResourceType() {
        Assert.assertEquals(SiteTranslationLanguage.RESOURCE_TYPE, translationFactory.getSupportedResourceType());
    }

}
