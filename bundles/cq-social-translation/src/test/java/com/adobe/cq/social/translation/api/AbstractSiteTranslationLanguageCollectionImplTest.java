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
package com.adobe.cq.social.translation.api;

import java.util.Iterator;

import junit.framework.Assert;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.SocialComponentFactory;
import com.adobe.cq.social.scf.SocialComponentFactoryManager;

public class AbstractSiteTranslationLanguageCollectionImplTest {
    private Resource m_resource;
    private ClientUtilities m_clientUtils;
    private ResourceResolver m_resolver;
    private Iterator<Resource> m_iterator;
    private SocialComponentFactoryManager m_factoryMgr;
    private SocialComponentFactory m_factory;
    private SiteTranslationLanguage m_language;
    private AbstractSiteTranslationLanguageCollectionImpl siteLanguageCollection;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        m_resource = Mockito.mock(Resource.class);
        m_clientUtils = Mockito.mock(ClientUtilities.class);
        m_resolver = Mockito.mock(ResourceResolver.class);
        m_iterator = Mockito.mock(Iterator.class);
        m_factoryMgr = Mockito.mock(SocialComponentFactoryManager.class);
        m_factory = Mockito.mock(SocialComponentFactory.class);
        m_language = Mockito.mock(SiteTranslationLanguage.class);

        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resolver);
        Mockito.when(m_resolver.getResource(Matchers.anyString())).thenReturn(m_resource);
        Mockito.when(m_resource.listChildren()).thenReturn(m_iterator);
        Mockito.when(m_clientUtils.getSocialComponentFactoryManager()).thenReturn(m_factoryMgr);
        Mockito.when(m_factoryMgr.getSocialComponentFactory(SiteTranslationLanguage.RESOURCE_TYPE)).thenReturn(
            m_factory);
        Mockito.when(m_iterator.hasNext()).thenReturn(true, false);
        Mockito.when(m_iterator.next()).thenReturn(m_resource);
        Mockito.when(m_resource.isResourceType(SiteTranslationLanguage.RESOURCE_TYPE)).thenReturn(true);
        Mockito.when(m_factory.getSocialComponent(m_resource)).thenReturn(m_language);
    }

    @Test
    public void testConstructorAndInit() {
        siteLanguageCollection = new AbstractSiteTranslationLanguageCollectionImpl(m_resource, m_clientUtils);

        Assert.assertNotNull(siteLanguageCollection);
        Mockito.verify(m_factory, Mockito.times(1)).getSocialComponent(m_resource);

        int count = siteLanguageCollection.getTotalSize();
        Assert.assertEquals(1, count);
        Assert.assertNotNull(siteLanguageCollection.getItems());
    }
}
