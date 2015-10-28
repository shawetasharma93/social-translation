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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyOption;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.translation.TranslationVariables;
import com.adobe.cq.social.translation.TranslationVariables.CACHE;
import com.adobe.cq.social.translation.TranslationVariables.DISPLAY;

/**
 * Translation service. Provides the translation options in the felix console and initiates the values.
 */
@Component(immediate = true, metatype = true, enabled = true,
        label = "AEM Communities Translation Configuration Manager")
@Service(value = TranslationServiceConfigManager.class)
public class TranslationServiceConfigManager {
    private static final Logger LOG = LoggerFactory.getLogger(TranslationServiceConfigManager.class);

    /**
     * Supported language that can be translated.
     */
    @Property(label = "Enabled Languages", cardinality = Integer.MAX_VALUE, options = {
        @PropertyOption(name = "ar", value = "Arabic"), @PropertyOption(name = "bg", value = "Bulgarian"),
        @PropertyOption(name = "zh_CN", value = "Simplified Chinese"),
        @PropertyOption(name = "zh_TW", value = "Traditional Chinese"),
        @PropertyOption(name = "cs", value = "Czech"), @PropertyOption(name = "da", value = "Danish"),
        @PropertyOption(name = "nl", value = "Dutch"), @PropertyOption(name = "en", value = "English"),
        @PropertyOption(name = "et", value = "Estonian"), @PropertyOption(name = "fi", value = "Finnish"),
        @PropertyOption(name = "fr", value = "French"), @PropertyOption(name = "de", value = "German"),
        @PropertyOption(name = "el", value = "Greek"), @PropertyOption(name = "ht", value = "Haitian Creole"),
        @PropertyOption(name = "he", value = "Hebrew"), @PropertyOption(name = "hu", value = "Hungarian"),
        @PropertyOption(name = "id", value = "Indonesian"), @PropertyOption(name = "it", value = "Italian"),
        @PropertyOption(name = "ja", value = "Japanese"), @PropertyOption(name = "ko", value = "Korean"),
        @PropertyOption(name = "lv", value = "Latvian"), @PropertyOption(name = "lt", value = "Lithuanian"),
        @PropertyOption(name = "no", value = "Norwegian"), @PropertyOption(name = "pl", value = "Polish"),
        @PropertyOption(name = "pt", value = "Portuguese"), @PropertyOption(name = "ro", value = "Romanian"),
        @PropertyOption(name = "ru", value = "Russian"), @PropertyOption(name = "sk", value = "Slovak"),
        @PropertyOption(name = "sl", value = "Slovenian"), @PropertyOption(name = "es", value = "Spanish"),
        @PropertyOption(name = "sv", value = "Swedish"), @PropertyOption(name = "th", value = "Thai"),
        @PropertyOption(name = "tr", value = "Turkish"), @PropertyOption(name = "uk", value = "Ukrainian"),
        @PropertyOption(name = "vi", value = "Vietnamese")}, value = {"en", "fr", "de", "ja", "it", "es", "pt",
        "zh_CN", "zh_TW", "ko"})
    public static String PROP_TRANSLATE_LANGUAGE = "translate.language";
    /**
     * Translate Level and display options.
     */
    @Property(label = "Select Display Options", value = "replace", options = {
        @PropertyOption(name = "replace", value = "Replace source content with the target translation"),
        @PropertyOption(name = "side", value = "Show target translation side by side with source content")})
    public static String PROP_TRANSLATE_DISPLAY = "translate.display";

    /**
     * Show attribution.
     */
    @Property(label = "Attribution", boolValue = true,
            description = "Append translation service provider attribution to translated result. Attribution text "
                    + "is editable in the Cloud Service configuration page.")
    public static String PROP_TRANSLATE_ATTRIBUTION = "translate.attribution";

    /**
     * Whether to persist Translations.
     */
    @Property(label = "Select Persistence Options", value = "cache_when_call", options = {
        @PropertyOption(name = "cache_when_call",
                value = "Translate contributions on user request and persist afterwards"),
        @PropertyOption(name = "no_caching", value = "Don't persist translations."),
        @PropertyOption(name = "cache_when_post",
                value = "Automatically translate new contributions and persist the new translations")})
    public static String PROP_TRANSLATE_CACHE = "translate.caching";

    /**
     * Re-translate after X months.
     */
    @Property(label = "Set Machine Translation Refresh Interval", value = "0",
            description = "Refresh translation after designated interval (number of months). Enter 0 to disable "
                    + "this feature.")
    public static String PROP_TRANSLATE_CACHE_DURATION = "translate.caching.duration";

    /**
     * Flush JCR Session Writes after XXXX seconds
     */
    @Property(label = "Set Machine Translation session save interval", value = "2",
            description = "Force session save after designated interval (number of seconds). Enter 0 to disable "
                    + "batch writing")
    public static String PROP_TRANSLATE_SESSION_SAVE_INTERVAL = "translate.session.save.interval";

    /**
     * Set queue limit to this value
     */
    @Property(label = "Set Machine Translation Save Queue batching limit", value = "250",
            description = "If the session save interval is 0, then this value is irrelevant")
    public static String PROP_TRANSLATE_SESSION_SAVE_BATCHLIMIT = "translate.session.save.batchLimit";

    /**
     * Set Smart Rendering option.
     */
    @Property(label = "Smart Rendering", boolValue = false,
            description = "Always show contributions in user’s preferred language.")
    public static String PROP_TRANSLATE_SMART_RENDERING = "translate.smart.rendering";

    /**
     * @param componentContext Component Context
     */
    protected void activate(final ComponentContext componentContext) {
        LOG.debug("#########SoCo Translation is on!#####");
        final Dictionary dic = componentContext.getProperties();

        String[] lanCode = {};
        if (dic.get(PROP_TRANSLATE_LANGUAGE) != null) {
            lanCode = (String[]) dic.get(PROP_TRANSLATE_LANGUAGE);
        }
        final String displayOption = dic.get(PROP_TRANSLATE_DISPLAY).toString();
        DISPLAY display;
        if (displayOption != null && !"".equals(displayOption)) {
            display = DISPLAY.valueOf(displayOption.toUpperCase());
        } else {
            display = DISPLAY.REPLACE;
        }

        final CACHE caching;
        if (dic.get(PROP_TRANSLATE_CACHE) != null) {
            caching = CACHE.valueOf(dic.get(PROP_TRANSLATE_CACHE).toString().toUpperCase());
        } else {
            caching = CACHE.CACHE_WHEN_CALL;
        }

        final String cachingDuration = dic.get(PROP_TRANSLATE_CACHE_DURATION).toString();
        int cachingMonths = 0;
        try {
            cachingMonths = Integer.parseInt(cachingDuration);
        } catch (final NumberFormatException e) {
            cachingMonths = 0;
        }

        final String sessionSaveInterval = dic.get(PROP_TRANSLATE_SESSION_SAVE_INTERVAL).toString();
        long sessionIntervalSeconds;
        try {
            sessionIntervalSeconds = Long.parseLong(sessionSaveInterval);
        } catch (final NumberFormatException e) {
            sessionIntervalSeconds = 2L;
        }

        final String batchLimitString = dic.get(PROP_TRANSLATE_SESSION_SAVE_BATCHLIMIT).toString();
        int batchLimitInt;
        try {
            batchLimitInt = Integer.parseInt(batchLimitString);
        } catch (final NumberFormatException e) {
            LOG.warn("Error parsing Translation Batch limit, defaulting to 250", e);
            batchLimitInt = 250;
        }

        boolean allowAttribution = true;
        try {
            allowAttribution = Boolean.parseBoolean(dic.get(PROP_TRANSLATE_ATTRIBUTION).toString());
        } catch (final Exception e) {
            LOG.trace("Attribution dictionary parsing threw an error", e);
        }

        boolean allowSmartRendering = false;
        try {
            allowSmartRendering = Boolean.parseBoolean(dic.get(PROP_TRANSLATE_SMART_RENDERING).toString());
        } catch (final Exception ex) {
            LOG.trace("Smart Rendering option - dictionary parsing throwing an error.", ex);
            allowSmartRendering = false;
        }

        LOG.debug(lanCode.toString());
        TranslationVariables.setDisplay(display);
        TranslationVariables.setLanguageCode(lanCode);
        TranslationVariables.setCaching(caching);
        TranslationVariables.setCachingDuration(cachingMonths);
        TranslationVariables.setEnableAttribution(allowAttribution);
        TranslationVariables.setSessionSaveInterval(sessionIntervalSeconds);
        TranslationVariables.setTranslationSaveBatchLimit(batchLimitInt);
        TranslationVariables.setEnableSmartRendering(allowSmartRendering);
    }
}
