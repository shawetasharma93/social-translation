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

package com.adobe.cq.social.translation;

import java.util.ArrayList;

/**
 * This is a class that stores the values of the translation meta data.
 */
public class UGCTranslationMeta {
    private String ugcLanuage = "";
    private ArrayList<String> toBeTranslated = new ArrayList<String>();
    private String pageLanguage = "";

    /**
     * @return String
     */
    public String getUgcLanguage() {
        return ugcLanuage;
    }

    /**
     * @param ugcLanuage The language of the UGC
     */
    public void setUgcLanuage(final String ugcLanuage) {
        this.ugcLanuage = ugcLanuage;
    }

    /**
     * @return String
     */
    public ArrayList<String> getToBeTranslated() {
        return toBeTranslated;
    }

    /**
     * @param toBeTranslated The content to be translated
     */
    public void setToBeTranslated(final ArrayList<String> toBeTranslated) {
        this.toBeTranslated = toBeTranslated;
    }

    /**
     * @return String
     */
    public String getPageLanguage() {
        return pageLanguage;
    }

    /**
     * @param pageLanguage The language of the page
     */
    public void setPageLanguage(final String pageLanguage) {
        this.pageLanguage = pageLanguage;
    }

}
