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

import static org.mockito.Mockito.mock;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.discovery.DiscoveryService;
import org.apache.sling.discovery.InstanceDescription;
import org.apache.sling.discovery.TopologyView;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;

import com.adobe.cq.social.translation.TranslationUtil;
import com.adobe.cq.social.translation.TranslationVariables;
import com.adobe.cq.social.translation.TranslationVariables.CACHE;
import com.adobe.cq.social.ugcbase.AsyncReverseReplicator;
import com.adobe.cq.social.ugcbase.TranslationSaveQueue;
import com.adobe.cq.social.ugcbase.TranslationUpdate;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationManager;
import com.adobe.granite.translation.api.TranslationResult;
import com.adobe.granite.translation.api.TranslationService;
import com.adobe.granite.translation.api.TranslationService.TranslationServiceInfo;
import com.adobe.granite.translation.connector.msft.core.MicrosoftTranslationCloudConfig;

@SuppressWarnings("deprecation")
public class UGCLanguageDetectorTest {
    @InjectMocks
    private final UGCLanguageDetector ugcLangDetector = new UGCLanguageDetector();

    @Mock
    private SlingRepository repository;

    @Mock
    private TranslationManager tm;

    @Mock
    private TranslationSaveQueue translationSaveQueue;

    @Mock
    private DiscoveryService discoveryService;

    @Mock
    protected SlingSettingsService settingsService;

    @Mock
    private JcrResourceResolverFactory resolverFactory;

    @Mock
    private Map<String, String> resourceTypes;

    @Mock
    private AsyncReverseReplicator replicator;

    private final ComponentContext m_compContext = Mockito.mock(ComponentContext.class);
    private final Dictionary<Object, Object> m_dic = new Hashtable<Object, Object>();
    private final TopologyView m_view = Mockito.mock(TopologyView.class);
    private final InstanceDescription m_currentInstance = Mockito.mock(InstanceDescription.class);
    private final Resource m_resource = Mockito.mock(Resource.class);
    private final ResourceResolver m_resourceResolver = Mockito.mock(ResourceResolver.class);
    private final MicrosoftTranslationCloudConfig m_mcTransCloudConfig = Mockito
        .mock(MicrosoftTranslationCloudConfig.class);
    private final ModifiableValueMap m_mvMap = Mockito.mock(ModifiableValueMap.class);
    private final TranslationService m_tranSrvc = Mockito.mock(TranslationService.class);
    private final TranslationResult m_tranResult = Mockito.mock(TranslationResult.class);
    private final TranslationServiceInfo m_tranSrvcInfo = mock(TranslationServiceInfo.class);
    private final ConcurrentLinkedQueue<TranslationUpdate> concLinkedQ =
        new ConcurrentLinkedQueue<TranslationUpdate>();
    private Event m_event;
    private final Map<String, String> factName = new HashMap<String, String>();

    private static final String PROPERTY_PATH = "/content/usergenerated/";
    private static final String PROPERTY = "jcr:description";
    private static final String strContent = "Hello World";
    private static final String fromLang = "en";
    private static final String toLang = "fr";
    private static final String STR_ATTR = "Translations By Microsoft";
    private static String CLOUD_CONFIG_PROPERTY = "cq:cloudserviceconfigs";
    private static String STR_CONFIG_PROP = "test/Configs";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        m_dic.put(UGCLanguageDetector.RESOURCE_PROPERTY_LIST,
            new String[]{"social/commons/components/comments/comment jcr:description"});
        m_dic.put(UGCLanguageDetector.RESOURCE_TYPE,
            new String[]{"social/commons/components/comments/comment jcr:description"});
        m_dic.put(SlingConstants.PROPERTY_PATH, PROPERTY_PATH);
        m_dic.put("resourceChangedAttributes", new String[]{PROPERTY});
        m_dic.put("resourceAddedAttributes", new String[]{PROPERTY});

        m_event = new Event("topic", m_dic);
        factName.put("1", "name");

        Mockito.when(m_compContext.getProperties()).thenReturn(m_dic);
        Mockito.when(this.discoveryService.getTopology()).thenReturn(m_view);
        Mockito.when(m_view.getLocalInstance()).thenReturn(m_currentInstance);
        Mockito.when(m_currentInstance.isLeader()).thenReturn(true);
        Mockito.when(this.tm.getAvailableFactoryNames()).thenReturn(factName);
        Mockito.when(this.tm.createTranslationService(m_resource)).thenReturn(m_tranSrvc);
        Mockito.when(m_tranSrvc.detectLanguage(strContent, TranslationConstants.ContentType.HTML)).thenReturn(
            fromLang);
        Mockito.when(
            m_tranSrvc
                .translateString(strContent, fromLang, toLang, TranslationConstants.ContentType.HTML, "general"))
            .thenReturn(m_tranResult);
        Mockito.when(m_tranResult.getTranslation()).thenReturn("Bonjour tout le monde");
        Mockito.when(m_tranSrvc.getTranslationServiceInfo()).thenReturn(m_tranSrvcInfo);
        Mockito.when(m_tranSrvcInfo.getTranslationServiceAttribution()).thenReturn(STR_ATTR);
        Mockito.when(this.resolverFactory.getResourceResolver(this.repository.loginAdministrative(null))).thenReturn(
            m_resourceResolver);
        Mockito.when(m_resourceResolver.getResource(PROPERTY_PATH)).thenReturn(m_resource);
        Mockito.when(m_resource.isResourceType("social/commons/components/comments/comment")).thenReturn(true);
        Mockito.when(m_resource.getPath()).thenReturn(JcrConstants.JCR_CONTENT);
        Mockito.when(m_resource.getResourceResolver()).thenReturn(m_resourceResolver);
        Mockito.when(m_mcTransCloudConfig.getClientId()).thenReturn("admin");
        Mockito.when(m_mcTransCloudConfig.getClientSecret()).thenReturn("admin");
        Mockito.when(this.translationSaveQueue.getUpdateQueue()).thenReturn(concLinkedQ);

    }

    @Test
    public void testActivate() throws Exception {
        ugcLangDetector.activate(m_compContext);
    }

    @Test
    public void testHandleEvent() throws Exception {
        Mockito.when(m_mvMap.get(CLOUD_CONFIG_PROPERTY)).thenReturn(STR_CONFIG_PROP);
        Mockito.when(m_resourceResolver.getResource(m_resource, CLOUD_CONFIG_PROPERTY)).thenReturn(null);
        Mockito.when(m_resource.adaptTo(ModifiableValueMap.class)).thenReturn(m_mvMap);
        Mockito.when(m_resource.adaptTo(ValueMap.class)).thenReturn(m_mvMap);
        Mockito.when(m_resource.getChild(TranslationUtil.TRANSLATION_NODE_NAME)).thenReturn(m_resource);
        Mockito.when(m_resource.getChild(fromLang)).thenReturn(m_resource);
        Mockito.when(m_mvMap.get(PROPERTY, (String) null)).thenReturn(strContent);
        Mockito.when(m_mvMap.get(PROPERTY, "")).thenReturn(strContent);
        TranslationVariables.setCaching(CACHE.CACHE_WHEN_POST);
        TranslationVariables.setLanguageCode(new String[]{fromLang, toLang});

        ugcLangDetector.activate(m_compContext);
        ugcLangDetector.handleEvent(m_event);
    }

}
