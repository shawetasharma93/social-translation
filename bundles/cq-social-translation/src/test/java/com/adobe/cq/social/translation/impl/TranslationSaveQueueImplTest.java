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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceResolverFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.ComponentContext;

import com.adobe.cq.social.ugcbase.TranslationUpdate;

@SuppressWarnings("deprecation")
public class TranslationSaveQueueImplTest {
    @InjectMocks
    private final TranslationSaveQueueImpl tranSaveQueue = new TranslationSaveQueueImpl();

    @Mock
    private ConcurrentLinkedQueue<TranslationUpdate> concurrentLinkedQueue;

    @Mock
    private SlingRepository repository;

    @Mock
    private Scheduler scheduler;

    @Mock
    private JcrResourceResolverFactory jcrResourceResolverFactory;

    ComponentContext m_compContext = Mockito.mock(ComponentContext.class);
    ResourceResolver m_resourceResolver = Mockito.mock(ResourceResolver.class);
    Resource m_resource = Mockito.mock(Resource.class);
    Session m_session = Mockito.mock(Session.class);
    Map<String, String> testMap = new HashMap<String, String>();
    private TranslationUpdate tranUpdate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testMap.put("test", "case");

        tranUpdate = new TranslationUpdate("testPath", "en_US", testMap);
        FieldUtils.writeField(tranSaveQueue, "scheduler", scheduler, true);
        Mockito.when(jcrResourceResolverFactory.getResourceResolver(m_session)).thenReturn(m_resourceResolver);
        Mockito.when(jcrResourceResolverFactory.getResourceResolver(Mockito.anyMap())).thenReturn(m_resourceResolver);
        Mockito.when(m_resourceResolver.getResource("testPath")).thenReturn(m_resource);
    }

    @Test
    public void testGetUpdateQueue() {
        tranSaveQueue.activate(m_compContext);
        assertNotNull(tranSaveQueue.getUpdateQueue());
    }

    @Test
    public void testRegisterUpdate() {
        tranSaveQueue.activate(m_compContext);
        this.concurrentLinkedQueue = tranSaveQueue.getUpdateQueue();
        this.concurrentLinkedQueue.add(tranUpdate);
        tranSaveQueue.registerUpdate();
    }

    @Test
    public void testSaveQueue() throws Exception {
        final String TRANSLATION_NODE_NAME = "translation";
        final String USER_LANGUAGE = "en_US";
        final Node m_node = Mockito.mock(Node.class);
        final ModifiableValueMap m_Map = mock(ModifiableValueMap.class);

        Mockito.when(m_resource.adaptTo(Node.class)).thenReturn(m_node);
        Mockito.when(m_node.hasNode(TRANSLATION_NODE_NAME)).thenReturn(false);
        Mockito.when(m_node.getNode(TRANSLATION_NODE_NAME)).thenReturn(m_node);
        Mockito.when(m_node.addNode("en_US")).thenReturn(m_node);
        Mockito.when(m_node.getPath()).thenReturn("nonugc/test/path");
        Mockito.when(m_resource.getChild(TRANSLATION_NODE_NAME)).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.create(m_resource, TRANSLATION_NODE_NAME, null)).thenReturn(m_resource);
        Mockito.when(m_resourceResolver.create(m_resource, USER_LANGUAGE, null)).thenReturn(m_resource);
        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resourceResolver);
        Mockito.when(m_resource.adaptTo(ModifiableValueMap.class)).thenReturn(m_Map);
        Mockito.when(this.repository.loginAdministrative(null)).thenReturn(m_session);

        tranSaveQueue.activate(m_compContext);
        this.concurrentLinkedQueue = tranSaveQueue.getUpdateQueue();
        this.concurrentLinkedQueue.add(tranUpdate);
        tranSaveQueue.saveQueue();
    }

}
