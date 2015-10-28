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

import static org.apache.http.HttpStatus.SC_OK;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.sling.testing.tools.http.RequestExecutor;

import com.adobe.cq.testing.client.CQ5Client;
import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.GraniteConstants;
import com.adobe.granite.testing.util.HttpUtils;
import com.adobe.granite.testing.util.SlingResponseHandler;
import com.adobe.soco.integrationtest.commons.util.CommonsITClient;
import com.adobe.soco.integrationtest.util.RandomString;

public class TranslationCloudConfig {

    protected static final String MICROSOFT_TRANSLATION_PROVIDER = "microsoft";
    private static Map<String, String> commonStoreSet = null;

    private static String createGenericCloudConfig(final CQ5Client clientInstance, final String configName,
        final String strBaseConfigPath, final String strTemplatePath, final Map<String, StringBody> propertyMap)
        throws ClientException {
        if (configName == null) {
            throw new ClientException("Parameters to configure Integration Config Page must not " + "be null!");
        }

        // create the config page
        final SlingResponseHandler s =
            clientInstance.createPage(configName, configName, strBaseConfigPath, strTemplatePath);
        // check returned status
        HttpUtils.verifyHttpStatus(s.getRequestExecutor(), HttpUtils.getExpectedStatus(SC_OK, 200));

        final MultipartEntity multiPartEntity = new MultipartEntity();
        try {
            multiPartEntity
                .addPart(GraniteConstants.PARAMETER_CHARSET, new StringBody(GraniteConstants.CHARSET_UTF8));

            for (final String strKey : propertyMap.keySet()) {
                final StringBody strValue = propertyMap.get(strKey);
                multiPartEntity.addPart(strKey, strValue);
            }

        } catch (final UnsupportedEncodingException e) {
            throw new ClientException("Could not create Multipart POST for storing config!", e);
        }

        // store the configuration parameters on the page
        final RequestExecutor exec = clientInstance.http().doPost(s.getPath() + "/jcr:content", multiPartEntity);
        // check returned status
        HttpUtils.verifyHttpStatus(exec, HttpUtils.getExpectedStatus(SC_OK, 200));
        // return the path of the new config page
        return s.getPath();
    }

    public static String createTIFCloudConfig(final CQ5Client clientInstance, final String configName,
        final String translationProvider, final String category, final String commonStoreLang)
        throws ClientException, UnsupportedEncodingException {
        final Map<String, StringBody> propertyMap = new HashMap<String, StringBody>();
        propertyMap.put("./sling:resourceType", new StringBody("cq/translation/components/mt-cloudconfig"));
        propertyMap.put("./defaultTranslationProvider", new StringBody(translationProvider));
        propertyMap.put("./defaultCategory", new StringBody(category));
        if (commonStoreLang != null && !commonStoreLang.isEmpty()) {
            propertyMap.put("./ugcPath", new StringBody(commonStoreLang));
        }
        return createGenericCloudConfig(clientInstance, configName, "/etc/cloudservices/translation",
            "/libs/cq/translation/templates/mt-servicepage", propertyMap);
    }

    public static String createMSFTCloudConfig(final CQ5Client clientInstance, final String configName,
        final String strClientID, final String strClientSecret, final String strAttribution, final String strLabel,
        final String strWorkspaceID) throws ClientException, UnsupportedEncodingException {

        final Map<String, StringBody> propertyMap = new HashMap<String, StringBody>();
        propertyMap.put("./sling:resourceType", new StringBody("cq/translation/components/msft/msft-cloudconfig"));
        propertyMap.put("./clientid", new StringBody(strClientID));
        propertyMap.put("./clientsecret", new StringBody(strClientSecret));
        propertyMap.put("./serviceattribution", new StringBody(strAttribution));
        propertyMap.put("./servicelabel", new StringBody(strLabel));
        propertyMap.put("./workspaceId", new StringBody(strWorkspaceID));

        return createGenericCloudConfig(clientInstance, configName, "/etc/cloudservices/msft-translation",
            "/libs/cq/translation/templates/msft/msft-servicepage", propertyMap);
    }

    public static void applyCloudConfigToPage(final CQ5Client clientInstance, final String strPagePath,
        final String[] cloudConfigList) throws ClientException {
        final List<String[]> props = new ArrayList<String[]>();
        final String strConfigCombined = StringUtils.join(cloudConfigList, ";");
        props.add(new String[]{"cq:cloudserviceconfigs", strConfigCombined});
        clientInstance.setPageProperties(strPagePath, props);
    }

    public static String getCommonStorePath(final CommonsITClient adminPublishClient,
        final String strCommonStoreLangCode) throws UnsupportedEncodingException, ClientException {
        if (commonStoreSet == null) {
            commonStoreSet = new HashMap<String, String>();
        }
        String strRetVal = commonStoreSet.get(strCommonStoreLangCode);
        if (strRetVal == null) {
            strRetVal =
                createTIFCloudConfig(adminPublishClient, new RandomString("TIF").nextString(),
                    MICROSOFT_TRANSLATION_PROVIDER, "general", strCommonStoreLangCode);
            commonStoreSet.put(strCommonStoreLangCode, strRetVal);
        }
        return strRetVal;
    }
}
