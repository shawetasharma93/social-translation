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
package com.adobe.soco.integrationtest.translation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpException;
import org.apache.sling.commons.json.JSONException;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.annotation.IgnoreIfProperty;
import com.adobe.granite.testing.category.FailingTestOnOak;
import com.adobe.granite.testing.util.QuickstartOptions;
import com.adobe.soco.integrationtest.commons.util.CommonsITClient;

//@Category(FailingTestOnOak.class)
// CQ-27069
@RunWith(Parameterized.class)
public class SoCoMTCompleteTestIT extends SoCoMTTranslationBaseIT {

    public SoCoMTCompleteTestIT(final String strTestCaseName, final String strLang,
        final CommonsITClient clientInstance, final String strUserLanguage, final ArrayList<String> languageArray,
        final int iPersistOption, final String strCommonStoreLangCode) throws UnsupportedEncodingException,
        ClientException {
        super(strLang, clientInstance, strUserLanguage, languageArray, iPersistOption, strCommonStoreLangCode);
    }

    @Test
    @IgnoreIfProperty(name = "test.filterrule.type", value = "nocomplete")
    public void testCommentTranslation() throws ClientException, InterruptedException, HttpException, IOException,
        JSONException, SAXException, ParserConfigurationException {
        baseTestCommentTranslation();
    }

    @SuppressWarnings("rawtypes")
    @Parameters(name = "{0}")
    public static Collection runtimeParams() throws ClientException, UnsupportedEncodingException {

        final CommonsITClient commonsAdminPublishClient =
            new CommonsITClient(QuickstartOptions.getServerUrlPublish(), QuickstartOptions.getRootContextPublish(),
                getAdminUser(), getAdminPassword());

        final List<String> langArray = LanguageDataSet.getLanguageArray();
        return Arrays
            .asList(new Object[][]{
                // The First 35 testcases are testing all languages with default
                // persist value, and no common store, the next 35 are with
                // persist off, and the third set with persist on.
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "en", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "fr", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "de", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "it", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "es", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "zh_cn", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ko", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "pt", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ja", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ar", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "bg", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "cs", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "zh_tw", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "da", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "nl", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, null},
                // Changing Persistance to Default
              /*  {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "en", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "fr", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "de", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "it", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "es", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "zh_cn", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ko", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "pt", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ja", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ar", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "bg", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "cs", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "zh_tw", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "da", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "nl", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, null},
                // Changing Persistance to NO PERSISTANCE
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "en", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "fr", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "de", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "it", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "es", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "zh_cn", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ko", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "pt", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ja", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "ar", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "bg", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "cs", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "zh_tw", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "da", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "nl", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, null},*/
                // Changing User to French
                // Auto PERSIST on for first 35 cases.
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "en", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "fr", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "de", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "es", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "zh_cn", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ko", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "pt", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ja", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ar", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "bg", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "cs", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "zh_tw", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "da", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "nl", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, null},

                // Changing Persistance to Default
              /*  {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "en", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "fr", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "de", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "es", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "zh_cn", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ko", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "pt", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ja", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ar", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "bg", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "cs", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "zh_tw", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "da", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "nl", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, null},

                // Changing Persistance to NO PERSISTANCE
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "en", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "fr", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "de", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "es", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "zh_cn", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ko", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "pt", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ja", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "ar", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "bg", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "cs", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "zh_tw", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "da", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES, NO COMMON STORE, FRENCH USER",
                    "nl", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, null},*/

                // Setting Common Store to GERMAN
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "en", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "fr", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "de", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "it", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "es", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "zh_cn", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ko", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "pt", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ja", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ar", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "bg", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "cs", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "zh_tw", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "da", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "nl", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_ON, "de"},

                // Changing Persistance to Default
              /*  {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "en", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "fr", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "de", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "it", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "es", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "zh_cn", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ko", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "pt", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ja", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ar", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "bg", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "cs", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "zh_tw", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "da", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "nl", commonsAdminPublishClient, null, langArray, PERSIST_DEFAUlT, "de"},

                // Changing Persistance to NO PERSISTANCE
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "en", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "fr", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "de", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "it", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "es", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "zh_cn", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ko", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "pt", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ja", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "ar", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "bg", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "cs", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "zh_tw", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "da", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, ADMIN USER",
                    "nl", commonsAdminPublishClient, null, langArray, AUTO_PERSIST_OFF, "de"},*/

                // Changing User to French
                // Auto PERSIST on for first 35 cases.
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "en", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "fr", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "de", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "es", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "zh_cn", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ko", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "pt", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ja", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ar", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "bg", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "cs", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "zh_tw", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "da", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as AUTO-PERSIST for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "nl", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_ON, "de"},

                // Changing Persistance to Default
               /* {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "en", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "fr", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "de", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "es", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "zh_cn", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ko", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "pt", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ja", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ar", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "bg", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "cs", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "zh_tw", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "da", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "nl", commonsAdminPublishClient, "fr", langArray, PERSIST_DEFAUlT, "de"},

                // Changing Persistance to NO PERSISTANCE
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "en", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance, PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "fr", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: GERMAN(de) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "de", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "es", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: CHINESE SIMPLIFIED(ZH_CN) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "zh_cn", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ko", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: POTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "pt", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ja", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: ARABIC(AR) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "ar", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: BULGARIAN(BG) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "bg", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: CZECH(CS) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "cs", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: CHINESE TRADIONAL(ZH_TW) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "zh_tw", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: DANISH(DA) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "da", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: DUTCH(NL) comment on PUBLISH instance,PERSISTANCE set OFF for ALL LANGUAGES,COMMON STORE set as GERMAN, FRENCH USER",
                    "nl", commonsAdminPublishClient, "fr", langArray, AUTO_PERSIST_OFF, "de"},*/

            });
    }

};