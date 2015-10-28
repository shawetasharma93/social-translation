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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.adobe.cq.social.translation.TranslationUtil;
import com.adobe.cq.social.translation.TranslationVariables;
import com.adobe.cq.social.ugcbase.SocialUtils;
import com.adobe.granite.translation.api.TranslationManager;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;

public class ContentUpdaterServiceImplTest {
    @InjectMocks
    private final ContentUpdaterServiceImpl contUpdateSrvc = new ContentUpdaterServiceImpl();

    @Mock
    private SlingRepository repository;

    @Mock
    private QueryBuilder builder;

    @Mock
    private TranslationManager translationManager;

    @Mock
    private ResourceResolverFactory resourceResolverFactory;

    @Mock
    private Session session;

    @Mock
    private ResourceResolver resourceResolver;

    Node m_node = Mockito.mock(Node.class);
    Query m_query = Mockito.mock(Query.class);
    Map<String, String> q_map = new HashMap<String, String>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.repository.loginService(TranslationUtil.UGC_WRITER, null)).thenReturn(session);
        Mockito.when(this.repository.loginAdministrative(null)).thenReturn(session);

        Mockito.when(session.getNode(SocialUtils.PATH_UGC)).thenReturn(m_node);
        Mockito.when(m_node.getPath()).thenReturn("/content/usergenerated");

        q_map.put("path", "/content/usergenerated");
        q_map.put("property", "language");
        Mockito.when(builder.createQuery(PredicateGroup.create(q_map), session)).thenReturn(m_query);

    }

    @Test
    public void testRun() {
        final Map<String, String> rpMappings = new HashMap<String, String>();

        TranslationVariables.setResourcePropertyMappings(rpMappings);
        contUpdateSrvc.run();
    }

    @Test
    public void testGetStatusJSON() {
        assertNotNull(contUpdateSrvc.getStatusJSON());
    }

    @Test
    public void testGetStatus() {
        assertEquals(contUpdateSrvc.getStatus(), "");
    }

    @Test
    public void testGetPathsWorkedUpon() {
        assertNotNull(contUpdateSrvc.getPathsWorkedUpon());
    }

    @Test
    public void testGetUpdates() {
        assertNotNull(contUpdateSrvc.getStatusUpdates());
    }

}
