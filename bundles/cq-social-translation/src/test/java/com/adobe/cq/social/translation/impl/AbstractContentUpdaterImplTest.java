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

import static org.junit.Assert.assertNotNull;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationManager;
import com.adobe.granite.translation.api.TranslationService;
import com.day.cq.wcm.api.Page;

public class AbstractContentUpdaterImplTest {
    private AbstractContentUpdaterImpl absContentUpd;
    private final Node m_node = Mockito.mock(Node.class);
    private final Session m_session = Mockito.mock(Session.class);
    private final TranslationManager m_transMgr = Mockito.mock(TranslationManager.class);
    private final ResourceResolver m_resourceResolver = Mockito.mock(ResourceResolver.class);
    private final Resource m_resource = Mockito.mock(Resource.class);
    private final ModifiableValueMap m_modValueMap = Mockito.mock(ModifiableValueMap.class);
    private final SocialUtils m_socialUtils = Mockito.mock(SocialUtils.class);
    private final Page m_page = Mockito.mock(Page.class);
    private final TranslationService m_tranSrvc = Mockito.mock(TranslationService.class);

    @Before
    public void setUp() throws Exception {
        final String strProp = "language";
        final String strPath = "testPath";
        absContentUpd = new AbstractContentUpdaterImpl(m_node, m_session, strProp, m_transMgr, m_resourceResolver);

        Mockito.when(m_node.getPath()).thenReturn(strPath);
        Mockito.when(m_resourceResolver.getResource("testPath")).thenReturn(m_resource);
        Mockito.when(m_resource.adaptTo(ModifiableValueMap.class)).thenReturn(m_modValueMap);
        Mockito.when(m_modValueMap.get(strProp, (String) null)).thenReturn(strProp);
        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resourceResolver);
        Mockito.when(m_resource.getPath()).thenReturn(strPath);
        Mockito.when(m_resourceResolver.adaptTo(SocialUtils.class)).thenReturn(m_socialUtils);
        Mockito.when(m_socialUtils.getContainingPage(m_resource)).thenReturn(m_page);
        Mockito.when(m_page.getPath()).thenReturn(strPath);
        Mockito.when(m_resourceResolver.map(strPath)).thenReturn(strPath);
        Mockito.when(m_transMgr.createTranslationService(m_resource)).thenReturn(m_tranSrvc);
        Mockito.when(m_tranSrvc.detectLanguage(strProp, TranslationConstants.ContentType.HTML)).thenReturn(strProp);
    }

    @Test
    public void testCall() {
        assertNotNull(absContentUpd.call());
    }
}
