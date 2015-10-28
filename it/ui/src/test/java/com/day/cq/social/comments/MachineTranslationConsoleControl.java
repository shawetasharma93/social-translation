package com.day.cq.social.comments;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.sling.testing.tools.http.RequestBuilder;
import org.apache.sling.testing.tools.http.RequestExecutor;

import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.util.FormEntityBuilder;
import com.adobe.soco.integrationtest.util.SoCoClient;

/**
 * Small Class for Manipulating the Machine Tranasation Console for testing purposes Currently sets up console to
 * permit translation between EN,FR,DE,IT,ES and to persist the translations.
 */
public class MachineTranslationConsoleControl extends SoCoClient {

    private RequestBuilder builder;
    private boolean useSocialComponents;
    private static ArrayList<String> createdPages;

    /**
     * Constructor.
     * @param serverURL
     * @param rootContext
     * @param userName
     * @param password
     */
    public MachineTranslationConsoleControl(String serverURL, String rootContext, String userName, String password) {
        super(serverURL, rootContext, userName, password);
        this.baseURL = serverURL;
        builder = new RequestBuilder(serverURL + rootContext);

        if (createdPages == null) {
            createdPages = new ArrayList<String>();
        }
    }

    /**
     * Get RequestBuilder function.
     */
    public RequestBuilder getRequestBuilder() {
        return builder;
    }

    /**
     * Set useSocialComponent function.
     */
    public void setUseSocialComponents(boolean value) {
        useSocialComponents = value;
    }

    /**
     * Get useSocialComponent function.
     */
    public boolean useSocialComponents() {
        return useSocialComponents;
    }

    /**
     * Edit the Console component. Deprecated Now - keeping for reference
     */
    public void editConsole() throws ClientException {

        // Location of MT control consol
        final String postPath =
            "/system/console/configMgr/com" + ".adobe.cq.social.translation.impl."
                    + "TranslationServiceConfigManager?";

        // We need an empty entitybuilder to feed to http.doPost
        FormEntityBuilder console = new FormEntityBuilder();

        // Load Values to build Post String
        String applyTrueFalse = "apply=true";
        String ajaxConfigManager = "&action=ajaxConfigManager";
        String translateClientId = "&translate.clientid=";

        // languages to be tested can be modified here
        // todo add code to load language options dynamically from calling
        // method
        String addLangParameter1 = "&translate.language=en_US";
        String addLangParameter2 = "&translate.language=fr_FR";
        String addLangParameter3 = "&translate.language=it_IT";
        String addLangParameter4 = "&translate.language=de_DE";
        String addLangParameter5 = "&translate.language=es_ES";

        String translatePersistence = "&translate.caching=2";
        String testString3 =
            postPath + applyTrueFalse + ajaxConfigManager + translateClientId + addLangParameter1 + addLangParameter2
                    + addLangParameter3 + addLangParameter4 + addLangParameter5 + translatePersistence;

        RequestExecutor exec = http.doPost(testString3, console.getEntity());

    }

    /**
     * Edit the Console component.
     */
    public void dynamicEditConsole(ArrayList<String> localLanguageArray) throws ClientException {

        // Location of MT control consol
        final String postPath =
            "/system/console/configMgr/com." + "adobe.cq.social.translation.impl."
                    + "TranslationServiceConfigManager?";

        // We need an empty entitybuilder to feed to http.doPost
        FormEntityBuilder console = new FormEntityBuilder();

        // Load Values to build Post String
        String applyTrueFalse = "apply=true";
        String ajaxConfigManager = "&action=ajaxConfigManager";

        String translatePattern = "&translate.language=";
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = localLanguageArray.iterator();

        // iterate over the incoming languages to build our POST string
        while (it.hasNext()) {
            String temp = (String) it.next();
            builder.append(translatePattern).append(temp);

        }

        String tmpString = builder.toString();
        String translatePersistence = "&translate.caching=cache_when_post";
        String propertylist = "&propertylist=translate.language%2Ctranslate.caching";
        String testString3 =
            postPath + applyTrueFalse + ajaxConfigManager + tmpString + translatePersistence + propertylist;

        RequestExecutor exec = http.doPost(testString3, console.getEntity());

    }

}
