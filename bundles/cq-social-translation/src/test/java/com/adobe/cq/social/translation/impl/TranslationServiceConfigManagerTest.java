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

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.component.ComponentContext;

public class TranslationServiceConfigManagerTest {
    private TranslationServiceConfigManager transSrvcConfigMgr;
    ComponentContext m_compContext = Mockito.mock(ComponentContext.class);
    Dictionary<String, String> m_dic = new Hashtable<String, String>();

    @Before
    public void setUp() throws Exception {
        transSrvcConfigMgr = new TranslationServiceConfigManager();
        m_dic.put(TranslationServiceConfigManager.PROP_TRANSLATE_DISPLAY, "replace");
        m_dic.put(TranslationServiceConfigManager.PROP_TRANSLATE_ATTRIBUTION, "1");
        m_dic.put(TranslationServiceConfigManager.PROP_TRANSLATE_CACHE, "no_caching");
        m_dic.put(TranslationServiceConfigManager.PROP_TRANSLATE_CACHE_DURATION, "2");
        m_dic.put(TranslationServiceConfigManager.PROP_TRANSLATE_SESSION_SAVE_INTERVAL, "2400");
        m_dic.put(TranslationServiceConfigManager.PROP_TRANSLATE_SESSION_SAVE_BATCHLIMIT, "250");

        Mockito.when(m_compContext.getProperties()).thenReturn(m_dic);
    }

    @Test
    public void testActivate() {
        transSrvcConfigMgr.activate(m_compContext);
    }
}
