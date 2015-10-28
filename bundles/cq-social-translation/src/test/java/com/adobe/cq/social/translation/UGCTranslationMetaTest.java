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

import java.util.ArrayList;

import org.junit.Test;

public class UGCTranslationMetaTest {
    private UGCTranslationMeta ugcTransMeta;

    private static final String STR_UGC_LANG = "en";
    private final ArrayList<String> toBeTranslated = new ArrayList<String>();
    private static final String STR_PAGE_LANG = "en";

    @Test
    public void testUGCTranslation() {
        ugcTransMeta = new UGCTranslationMeta();

        ugcTransMeta.setPageLanguage(STR_PAGE_LANG);
        ugcTransMeta.setToBeTranslated(toBeTranslated);
        ugcTransMeta.setUgcLanuage(STR_UGC_LANG);

        assertEquals(STR_PAGE_LANG, ugcTransMeta.getPageLanguage());
        assertEquals(STR_UGC_LANG, ugcTransMeta.getUgcLanguage());
        assertEquals(toBeTranslated, ugcTransMeta.getToBeTranslated());
    }
}
