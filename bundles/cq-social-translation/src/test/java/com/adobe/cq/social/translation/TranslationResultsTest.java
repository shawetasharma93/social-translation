/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2012 Adobe Systems Incorporated
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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TranslationResultsTest {
    private TranslationResults tranResults;
    private final Map<String, String> translation = new HashMap<String, String>();

    private static final String STR_STATUS = "SUCCESS";
    private static final String STR_ATTRIBUTION = "By Microsoft";
    private static final String STR_DISPLAY_TYPE = "sidebyside";

    @Before
    public void setup() throws Exception {
        tranResults = new TranslationResults(STR_STATUS, translation, STR_ATTRIBUTION, STR_DISPLAY_TYPE);
    }

    @Test
    public void testGetStatus() {
        assertEquals(STR_STATUS, tranResults.getStatus());
    }

    @Test
    public void testGetAttribution() {
        assertEquals(STR_ATTRIBUTION, tranResults.getAttribution());
    }

    @Test
    public void testGetTranslation() {
        assertEquals(translation, tranResults.getTranslation());
    }

    @Test
    public void testGetDisplay() {
        assertEquals(STR_DISPLAY_TYPE, tranResults.getDisplay());
    }
}
