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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.adobe.cq.social.scf.ClientUtilities;

public class AbstractSiteTranslationLanguageImplTest {

    private Resource m_resource;
    private ClientUtilities m_clientUtils;
    private ValueMap m_valueMap;
    private AbstractSiteTranslationLanguageImpl siteLanguage;

    @Before
    public void setUp() throws Exception {
        m_resource = Mockito.mock(Resource.class);
        m_clientUtils = Mockito.mock(ClientUtilities.class);
        m_valueMap = Mockito.mock(ValueMap.class);

        Mockito.when(m_resource.adaptTo(ValueMap.class)).thenReturn(m_valueMap);
        siteLanguage = new AbstractSiteTranslationLanguageImpl(m_resource, m_clientUtils);
    }

    @Test
    public void testGetLanguageCode() {
        Mockito.when(m_valueMap.get(Matchers.anyString(), Matchers.anyString())).thenReturn(
            SiteTranslationLanguage.PROPERTY_LANGUAGE_CODE);

        String code = siteLanguage.getLanguageCode();
        Assert.assertEquals(SiteTranslationLanguage.PROPERTY_LANGUAGE_CODE, code);
    }

    @Test
    public void testGetLanguageName() {
        Mockito.when(m_valueMap.get(Matchers.anyString(), Matchers.anyString())).thenReturn(
            SiteTranslationLanguage.PROPERTY_LANGUAGE_NAME);

        String code = siteLanguage.getLanguageName();
        Assert.assertEquals(SiteTranslationLanguage.PROPERTY_LANGUAGE_NAME, code);
    }

    @Test
    public void testGetLanguageSetting() {
        Mockito.when(m_valueMap.get(Matchers.anyString(), Matchers.anyString())).thenReturn(
            SiteTranslationLanguage.PROPERTY_LANGUAGE_SETTING);

        String code = siteLanguage.getLanguageSetting();
        Assert.assertEquals(SiteTranslationLanguage.PROPERTY_LANGUAGE_SETTING, code);
    }

}
