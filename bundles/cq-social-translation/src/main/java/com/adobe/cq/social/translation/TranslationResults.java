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

import java.util.Map;

/**
 * This is a class that stores the values of the translated data from CLSMT.
 */
public class TranslationResults {
    private final String status;
    private final Map<String, String> translation;
    private String attribution;
    private String display;

    /**
     * @param status The status of the translation results.
     * @param translation The translated content
     */
    TranslationResults(final String status, final Map<String, String> translation) {
        this.status = status;
        this.translation = translation;
        this.attribution = null;
        this.display = null;
    }

    /**
     * @param status The status of the translation results.
     * @param translation The translated content
     * @param attribution The attribution message
     */
    TranslationResults(final String status, final Map<String, String> translation, final String attribution) {
        this(status, translation);
        this.attribution = attribution;
        this.display = null;
    }

    /**
     * @param status The status of the translation results.
     * @param translation The translated content
     * @param attribution The attribution message
     * @param displayType The display style of comments and translation
     */
    TranslationResults(final String status, final Map<String, String> translation, final String attribution,
        final String displayType) {
        this(status, translation);
        this.attribution = attribution;
        this.display = displayType;
    }

    /**
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return String
     */
    public Map<String, String> getTranslation() {
        return translation;
    }

    /**
     * set the attribution message
     * @param attr - attribution message that is being set.
     */
    public void setAttribution(String attr) {
        attribution = attr;
    }

    /**
     * @return the attribution message
     */
    public String getAttribution() {
        return attribution;
    }

    public String getDisplay() {
        return display;
    }

    protected void setDisplay(String display) {
        this.display = display;
    }
}
