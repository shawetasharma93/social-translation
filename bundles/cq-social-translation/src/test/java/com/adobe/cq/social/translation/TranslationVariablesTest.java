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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.adobe.cq.social.translation.TranslationVariables.CACHE;
import com.adobe.cq.social.translation.TranslationVariables.DISPLAY;
import com.adobe.cq.social.translation.TranslationVariables.LEVEL;
import com.adobe.cq.social.translation.TranslationVariables.SEARCH;

public class TranslationVariablesTest {

    @Test
    public void testTranslationVariablesConstructor() {
        final String clientID = "";
        final String[] lanCode = {"en_US", "fr_FR", "ja_JP"};
        final LEVEL level = LEVEL.COMMENT;
        final DISPLAY display = DISPLAY.SIDE;
        final CACHE caching = CACHE.NO_CACHING;
        final int cachingDuration = 1;
        final SEARCH search = SEARCH.NO_SEARCHING;
        final boolean edit = true;
        final boolean metrics = true;
        final String[][] supportedLan = {{"en_US", "English"}, {"fr_FR", "French"}, {"ja_JP", "Japanese"}};

        TranslationVariables.setClientID(clientID);
        TranslationVariables.setLevel(level);
        TranslationVariables.setDisplay(display);
        TranslationVariables.setLanguageCode(lanCode);
        TranslationVariables.setCaching(caching);
        TranslationVariables.setCachingDuration(cachingDuration);
        TranslationVariables.setSearch(search);
        TranslationVariables.setEdit(edit);
        TranslationVariables.setMetrics(metrics);

        assertEquals(clientID, TranslationVariables.getClientID());
        assertArrayEquals(lanCode, TranslationVariables.getLanguageCode());
        assertEquals("comment", TranslationVariables.getLevel());
        assertEquals("side", TranslationVariables.getDisplay());
        assertEquals("no_caching", TranslationVariables.getCaching());
        assertEquals(cachingDuration, TranslationVariables.getCachingDuration());
        assertEquals("no_searching", TranslationVariables.getSearch());
        assertEquals(true, TranslationVariables.isEdit());
        assertEquals(true, TranslationVariables.isMetrics());
    }
}