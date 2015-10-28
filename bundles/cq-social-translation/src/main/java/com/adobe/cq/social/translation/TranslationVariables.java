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

import org.apache.felix.scr.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.translation.api.TranslationConfig;
import com.adobe.granite.translation.api.TranslationException;

/**
 * This is a class that stores the values of the translation options configured in the Felix console.
 */
public class TranslationVariables {
    private static final Logger LOG = LoggerFactory.getLogger(TranslationVariables.class);

    private static String clientID = "";

    private static String[] languageCode = {};

    private static LEVEL level = LEVEL.COMMENT;

    private static DISPLAY display = DISPLAY.REPLACE;

    private static CACHE caching = CACHE.NO_CACHING;

    private static int cachingDuration = 1;

    private static long sessionSaveInterval = 2L;

    private static int translationSaveBatchLimit = 250;

    private static SEARCH search = SEARCH.NO_SEARCHING;

    private static boolean edit;

    private static boolean metrics;

    private static Map<String, String> resourcePropertyMappings;

    private static boolean enableAttribution;

    private static boolean enableSmartRendering;

    @Reference
    private static TranslationConfig translationConfig;

    /**
     * @param clientId Client's ID NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public static void setClientID(final String clientId) {
        clientID = clientId;
    }

    /**
     * @param lanCode CLSMT Language code
     */
    public static void setLanguageCode(final String[] lanCode) {
        languageCode = lanCode;
    }

    /**
     * @param lvl Translation level and display option
     */
    public static void setLevel(final LEVEL lvl) {
        level = lvl;
    }

    /**
     * @param dis Translation level and display option
     */
    public static void setDisplay(final DISPLAY dis) {
        display = dis;
    }

    /**
     * @param cachingLevel Options to cache the translation
     */
    public static void setCaching(final CACHE cachingLevel) {
        caching = cachingLevel;
    }

    /**
     * @param duration Number of months to cache the translation
     */
    public static void setCachingDuration(final int duration) {
        cachingDuration = duration;
    }

    /**
     * @param interval Number of seconds (Long) to wait before forcing session save
     */
    public static void setSessionSaveInterval(final long interval) {
        sessionSaveInterval = interval;
    }

    /**
     * @param searchOption How to search through the translation NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES
     *            - 5/30/2014
     */
    @Deprecated
    public static void setSearch(final SEARCH searchOption) {
        search = searchOption;
    }

    /**
     * @param editOption Whether allow users to edit translation NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES
     *            - 5/30/2014
     */
    @Deprecated
    public static void setEdit(final boolean editOption) {
        edit = editOption;
    }

    /**
     * @return boolean NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public static boolean isEdit() {
        return edit;
    }

    /**
     * @return boolean NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public static boolean isMetrics() {
        return metrics;
    }

    /**
     * @return true if attribution is enabled
     */
    public static boolean isEnableAttribution() {
        return enableAttribution;
    }

    /**
     * @param metricsOption Metrics settings NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public static void setMetrics(final boolean metricsOption) {
        metrics = metricsOption;
    }

    /**
     * @param enabled Attribution boolean
     */
    public static void setEnableAttribution(final boolean enabled) {
        enableAttribution = enabled;
    }

    /**
     * @return String NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public static String getClientID() {
        return clientID;
    }

    /**
     * @return String
     */
    public static String[] getLanguageCode() {
        return languageCode;
    }

    /**
     * @return String
     */
    public static String getLevel() {
        return level.getLeve();
    }

    /**
     * @return String
     */
    public static String getDisplay() {
        return display.getDisplay();
    }

    /**
     * @return String[][] NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES 5/30/2014 Remove
     *         getLanguageName(String) method along with this method.
     */
    @Deprecated
    public static String[][] getSupportedLanguages() {
        String[][] languages;
        final int len = languageCode.length;
        if (len > 0) {
            languages = new String[len][2];
            for (int i = 0; i < len; i++) {
                languages[i][0] = languageCode[i];
                languages[i][1] = getLanguageName(languageCode[i]);
            }
        } else {
            languages = new String[0][0];
        }
        return languages;

    }

    @Deprecated
    private static String getLanguageName(final String langCode) {
        String strLangName = null;

        try {
            final Map<String, String> languageCodeNames = translationConfig.getLanguages();
            strLangName = languageCodeNames.get(langCode);
        } catch (final TranslationException te) {
            LOG.trace("Error getting language name for language code.", te);
        }

        return strLangName;
    }

    /**
     * Returns a mapping between Resource types and their associated properties, that the
     * {@link com.adobe.cq.social.translation.impl.UGCLanguageDetector} service uses
     * @return - HashMap between resourceTypes and corresponding JCR properties that contains the text to be watched
     */
    public static Map<String, String> getResourcePropertyMappings() {
        return resourcePropertyMappings;
    }

    /**
     * Set's the defined mappings between Resource types and their associated properties, that the
     * {@link com.adobe.cq.social.translation.impl.UGCLanguageDetector} service uses
     * @param rpMappings the defined mappings between resource types and their corresponding JCR properties that
     *            contains the text to be watched
     */
    public static void setResourcePropertyMappings(final Map<String, String> rpMappings) {
        resourcePropertyMappings = rpMappings;
    }

    /**
     * @return String
     */
    public static String getCaching() {
        return caching.getCache();
    }

    /**
     * @return integer
     */
    public static int getCachingDuration() {
        return cachingDuration;
    }

    /**
     * @return Interval (in seconds (long) ) after which to clear the language save queue
     */
    public static long getSessionSaveInterval() {
        return sessionSaveInterval;
    }

    /**
     * @return String NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public static String getSearch() {
        return search.getSearch();
    }

    /**
     * This value is irrelevant if the session save interval is 0
     * @return the translation save batching limit, which the {@link com.adobe.cq.social.ugcbase.TranslationSaveQueue}
     *         service uses.
     */
    public static int getTranslationSaveBatchLimit() {
        return translationSaveBatchLimit;
    }

    /**
     * This value is irrelevant if the session save interval is 0
     * @param translationSaveBatchLimit the translation save batching limit, which the
     *            {@link com.adobe.cq.social.ugcbase.TranslationSaveQueue} service uses
     */
    public static void setTranslationSaveBatchLimit(final int translationSaveBatchLimit) {
        TranslationVariables.translationSaveBatchLimit = translationSaveBatchLimit;
    }

    /**
     * Translation level.
     */
    public enum LEVEL {
        /**
         * Translation level for page, comment and thread.
         */
        COMMENT("comment"), THREAD("thread");

        private final String level;

        /**
         * Set level for page, comment and thread.
         * @param lvl The level for translation.
         */
        LEVEL(final String lvl) {
            level = lvl;
        }

        /**
         * @return String
         */
        String getLeve() {
            return this.level;
        }
    }

    /**
     * Translation display for side by side, popup and replace.
     */
    public enum DISPLAY {

        /**
         * Translation display for side by side, popup and replace.
         */
        SIDE("side"), REPLACE("replace");

        private final String display;

        /**
         * Set level for page, comment and thread.
         * @param dis The level for translation.
         */
        DISPLAY(final String dis) {
            display = dis;
        }

        /**
         * @return String
         */
        public String getDisplay() {
            return this.display;
        }
    }

    /**
     * Translation cache options.
     */
    public enum CACHE {

        /**
         * Translation cache options for no caching, cache when ugc has been posted and cache when user call
         * translation function.
         */
        NO_CACHING("no_caching"), CACHE_WHEN_POST("cache_when_post"), CACHE_WHEN_CALL("cache_when_call");

        private final String cache;

        /**
         * Set cache options for no caching, cache when ugc has been posted and cache when user call translation
         * function.
         * @param cacheOption The cache option for translation.
         */
        CACHE(final String cacheOption) {
            cache = cacheOption;
        }

        /**
         * @return String
         */
        public String getCache() {
            return this.cache;
        }
    }

    /**
     * Translation search options. NOT BEING USED - DEAD CODE - REMOVE AFTER 2 RELEASES - 5/30/2014
     */
    @Deprecated
    public enum SEARCH {

        /**
         * Translation search for no searching, search in one index and search in separate index.
         */
        NO_SEARCHING("no_searching"), ONE_INDEX("one_index"), SEPERATE_INDEX("separate_index");

        private final String search;

        /**
         * Set search for no searching, search in one index and search in separate index.
         * @param searchOption The search option for translation.
         */
        SEARCH(final String searchOption) {
            search = searchOption;
        }

        /**
         * @return String
         */
        public String getSearch() {
            return this.search;
        }
    }

    public static boolean isEnableSmartRendering() {
        return enableSmartRendering;
    }

    public static void setEnableSmartRendering(boolean enabled) {
        enableSmartRendering = enabled;
    }
}
