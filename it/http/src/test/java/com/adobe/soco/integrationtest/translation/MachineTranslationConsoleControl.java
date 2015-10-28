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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.testing.tools.http.RequestBuilder;
import org.junit.Test;

import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.util.FormEntityBuilder;
import com.adobe.soco.integrationtest.util.SoCoClient;

/**
 * Small Class for Manipulating the Machine Translation Console for testing purposes Currently sets up console to
 * permit translation between EN,FR,DE,IT,ES and to persist the translations.
 */
public class MachineTranslationConsoleControl extends SoCoClient {

    private final RequestBuilder builder;
    private boolean useSocialComponents;
    private static ArrayList<String> createdPages;
    private int m_iPersistOption;

    /**
     * Constructor.
     * @param serverURL
     * @param rootContext
     * @param userName
     * @param password
     */
    public MachineTranslationConsoleControl(final String serverURL, final String rootContext, final String userName,
        final String password) {
        super(serverURL, rootContext, userName, password);
        this.baseURL = serverURL;
        builder = new RequestBuilder(serverURL + rootContext);
        m_iPersistOption = 1;

        if (createdPages == null) {
            createdPages = new ArrayList<String>();
        }
    }

    /**
     * Get RequestBuilder function.
     */
    @Override
    public RequestBuilder getRequestBuilder() {
        return builder;
    }

    /**
     * Set useSocialComponent function.
     */
    @Override
    public void setUseSocialComponents(final boolean value) {
        useSocialComponents = value;
    }

    /**
     * Get useSocialComponent function.
     */
    @Override
    public boolean useSocialComponents() {
        return useSocialComponents;
    }

    private static final String SYSTEM_CONSOLE_URL =
        "/system/console/configMgr/com.adobe.cq.social.translation.impl.TranslationServiceConfigManager?apply=true&action=ajaxConfigManager";

    private String getPersistanceOption(final int iPersistOption) {
        if (iPersistOption == 1) {
            return "cache_when_call";
        } else if (iPersistOption == 2) {
            return "no_caching";
        }
        return "cache_when_post";
    }

    public void setConsoleConfiguration(final List<String> langArray, final int iPersistOption)
        throws ClientException {
        m_iPersistOption = iPersistOption;
        // Location of MT control consol
        final String postPath = SYSTEM_CONSOLE_URL;

        // We need an empty entitybuilder to feed to http.doPost
        final FormEntityBuilder console = new FormEntityBuilder();

        final String translatePattern = "&translate.language=";
        final StringBuilder builder = new StringBuilder();
        final Iterator<String> it = langArray.iterator();

        // iterate over the incoming languages to build our POST string
        while (it.hasNext()) {
            final String temp = it.next();
            builder.append(translatePattern).append(temp);
        }

        final String tmpString = builder.toString();
        final String translatePersistence = "&translate.caching=" + getPersistanceOption(m_iPersistOption);
        final String propertylist = "&propertylist=translate.language%2Ctranslate.caching";
        final String testString3 = postPath + tmpString + translatePersistence + propertylist;

        http().doPost(testString3, console.getEntity());

    }

    @Test
    public void contentUpdateOnPublish() throws ClientException, InterruptedException {
        // Location of MT control consol
        final String postPath = "/bin/contentUpdaterService";

        // We need an empty entitybuilder to feed to http.doPost
        final FormEntityBuilder console = new FormEntityBuilder();

        http().doPost(postPath, console.getEntity());
    }

    public boolean isAutoPersistOn() {
        return m_iPersistOption == 3;
    }

    public boolean isPersistOff() {
        return m_iPersistOption == 2;
    }

    public boolean isPersistAfterward() {
        return m_iPersistOption == 1;
    }

    public void dynamicEditConsole(final ArrayList<String> langArray) throws ClientException {
        setConsoleConfiguration(langArray, 3);
    }
}
