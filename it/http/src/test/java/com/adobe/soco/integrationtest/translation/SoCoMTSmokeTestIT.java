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
import com.adobe.granite.testing.category.FailingTestOnOak;
import com.adobe.granite.testing.category.SmokeTest;
import com.adobe.granite.testing.util.QuickstartOptions;
import com.adobe.soco.integrationtest.commons.util.CommonsITClient;

//@Category(FailingTestOnOak.class)
// CQ-27069
@RunWith(Parameterized.class)
public class SoCoMTSmokeTestIT extends SoCoMTTranslationBaseIT {

    public SoCoMTSmokeTestIT(final String strTestCaseName, final String strLang,
        final CommonsITClient clientInstance, final String strUserLanguage, final ArrayList<String> languageArray,
        final int iPersistOption, final String strCommonStoreLangCode) throws UnsupportedEncodingException,
        ClientException {
        super(strLang, clientInstance, strUserLanguage, languageArray, iPersistOption, strCommonStoreLangCode);
    }

    @Category(SmokeTest.class)
    @Test
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

        final List<String> defaultArray = LanguageDataSet.getDefaultLanguageArray();

        return Arrays
            .asList(new Object[][]{
                // Changing Persistance to Default
                {
                    "TESTCASE: ENGLISH(EN) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for DEFAULT LANGUAGES, NO COMMON STORE, ADMIN USER",
                    "en", commonsAdminPublishClient, null, defaultArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: FRENCH(fr) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for DEFAULT LANGUAGES, NO COMMON STORE, Japanese USER",
                    "fr", commonsAdminPublishClient, "ja", defaultArray, PERSIST_DEFAUlT, null},
                {
                    "TESTCASE: GERMAN(DE) comment on PUBLISH instance, PERSISTANCE set OFF for DEFAULT LANGUAGES, NO COMMON STORE, German USER",
                    "de", commonsAdminPublishClient, "de", defaultArray, AUTO_PERSIST_OFF, null},
                // Changing Persistance to Default
                {
                    "TESTCASE: JAPANESE(JA) comment on PUBLISH instance,PERSISTANCE set OFF for DEFAULT LANGUAGES,COMMON STORE set as GERMAN, Spanish USER",
                    "ja", commonsAdminPublishClient, "es", defaultArray, AUTO_PERSIST_OFF, "de"},
                {
                    "TESTCASE: SPANISH(ES) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for DEFAULT LANGUAGES,COMMON STORE set as GERMAN, Korean USER",
                    "es", commonsAdminPublishClient, "ko-kr", defaultArray, PERSIST_DEFAUlT, "de"},
                // {
                // "TESTCASE: CHINESE SIMPLIFED(ZH_CN) comment on PUBLISH instance,PERSISTANCE OFF for DEFAULT for DEFAULT LANGUAGES,COMMON STORE set as GERMAN, Italian USER",
                // "zh_cn", commonsAdminPublishClient, "it", defaultArray, AUTO_PERSIST_OFF, "de"},
                // {
                // "TESTCASE: CHINESE SIMPLIFED(ZH_TW) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for DEFAULT LANGUAGES,COMMON STORE set as JAPANESE, Portuguese USER",
                // "zh_tw", commonsAdminPublishClient, "pt-br", defaultArray, PERSIST_DEFAUlT, "ja"},
                {
                    "TESTCASE: KOREAN(KO) comment on PUBLISH instance,PERSISTANCE OFF for DEFAULT for DEFAULT LANGUAGES,COMMON STORE set as JAPANESE, Chinese Simplified USER",
                    "ko", commonsAdminPublishClient, "zh-cn", defaultArray, AUTO_PERSIST_OFF, "ja"},
                {
                    "TESTCASE: ITALIAN(IT) comment on PUBLISH instance,PERSISTANCE set as DEFAULT for DEFAULT LANGUAGES,COMMON STORE set as JAPANESE, FRENCH USER",
                    "it", commonsAdminPublishClient, "fr", defaultArray, PERSIST_DEFAUlT, "ja"},
                {
                    "TESTCASE: PORTUGUESE(PT) comment on PUBLISH instance,PERSISTANCE OFF for DEFAULT for DEFAULT LANGUAGES,COMMON STORE set as JAPANESE, English USER",
                    "pt", commonsAdminPublishClient, "en", defaultArray, AUTO_PERSIST_OFF, "ja"},

            });
    }
};