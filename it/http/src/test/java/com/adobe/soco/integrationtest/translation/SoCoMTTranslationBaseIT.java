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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.testing.tools.http.RequestExecutor;
import org.apache.sling.testing.tools.retry.RetryLoop;
import org.apache.sling.testing.tools.sling.TimeoutsProvider;
import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.adobe.cq.testing.client.security.CQPermissions;
import com.adobe.granite.testing.ClientException;
import com.adobe.granite.testing.client.SecurityClient;
import com.adobe.granite.testing.client.security.AbstractAuthorizable;
import com.adobe.granite.testing.client.security.Authorizable;
import com.adobe.granite.testing.client.security.AuthorizableManager;
import com.adobe.granite.testing.client.security.Group;
import com.adobe.granite.testing.client.security.User;
import com.adobe.granite.testing.client.security.UserProfile;
import com.adobe.granite.testing.util.FormEntityBuilder;
import com.adobe.granite.testing.util.HttpUtils;
import com.adobe.granite.testing.util.JsonUtils;
import com.adobe.granite.testing.util.QuickstartOptions;
import com.adobe.soco.integrationtest.commons.util.CommonsITBase;
import com.adobe.soco.integrationtest.commons.util.CommonsITClient;
import com.adobe.soco.integrationtest.util.RandomString;
import com.adobe.soco.integrationtest.util.SoCoClient;
import com.adobe.soco.integrationtest.util.SocoOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoCoMTTranslationBaseIT extends CommonsITBase {

    @Rule
    public TestName name = new TestName();

    protected static final String USER_NAME = "admin";
    protected static final String USER_PASSWORD = "admin";
    protected static final String TRANSLATION_NODE = "translation";
    protected static final String AS_LANGUAGE_PROP = "mtlanguage";
    protected static final String JCR_DESCRIPTION = "jcr:description";

    protected static final String ENCODING = "UTF-8";
    protected static final String AUTH_URL = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    protected static final String GRANT_TYPE = "client_credentials";
    protected static final String SCOPE = "http://api.microsofttranslator.com";
    protected static final String CLIENT_ID = "AdobeCLSMT_Test";
    protected static final String CLIENT_SECRET = "ZHdpFrVJobdSI9Tk/jO3msiH8rCITSmDTvcTCRuDL2s=";
    protected static final String BING_DETECTION_SERVICE_URL =
        "https://api.microsofttranslator.com/V2/Http.svc/Detect";
    protected static final int DEFAULT_TIME_OFFSET = 500;

    protected final int WAIT_INTERVAL = 500; // wait time interval in 500 msec

    protected static final int PERSIST_DEFAUlT = 1;
    protected static final int AUTO_PERSIST_OFF = 2;
    protected static final int AUTO_PERSIST_ON = 3;
    protected static final int MAX_NUM_RETRY = 5;
    protected static final int WAIT_TRANSLATION_NODE_TIME = 500;

    protected static final boolean ALLOW_PERFORMANCE = false;

    protected static final long SLEEP_MILLISECOND = 1000 * 5; // 1 min Change
    /** Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(SoCoMTTranslationBaseIT.class);

    // this
    // back to 60
    // secs

    protected static class AccessToken {
        public static String strAccessToken;
        protected static long expired_time;

        public static String getAccessToken() throws HttpException, IOException, JSONException {
            if (isAccessTokenExpired()) {
                strAccessToken = null;
                final HttpClient client = new HttpClient();
                final PostMethod post = new PostMethod(AUTH_URL);

                post.addParameter("grant_type", GRANT_TYPE);
                post.addParameter("scope", SCOPE);

                // Don't specify the charset for request body.
                // client_id and client_secret should not be URLEncoded even M$
                // guys
                // mentioned in their docs.
                post.addParameter("client_id", CLIENT_ID);
                post.addParameter("client_secret", CLIENT_SECRET);

                client.executeMethod(post);
                final int status_code = post.getStatusCode();
                if (status_code == HttpStatus.SC_OK) {
                    final String str = post.getResponseBodyAsString();

                    final JSONObject obj = new JSONObject(str);
                    strAccessToken = obj.getString("access_token");
                    final String expires_in = obj.getString("expires_in");
                    // Multiple response expires in (seconds) by 1000 to convert
                    // to milliseconds
                    final int expiresInMil = Integer.parseInt(expires_in) * 1000;
                    expired_time = System.currentTimeMillis() + expiresInMil - DEFAULT_TIME_OFFSET;
                }
                if (post != null) {
                    post.releaseConnection();
                }

            }
            return strAccessToken;
        }

        protected static boolean isAccessTokenExpired() {
            final long current = System.currentTimeMillis();
            return ((strAccessToken == null) || (current > expired_time));
        }
    };

    // we need to set common store as well

    @Override
    @Before
    public void setup() throws ClientException {
        setUseHBS(true);

        /* Ignore any Performance tests unless ALLOW_PERFORANCE=true */
        org.junit.Assume.assumeTrue(ALLOW_PERFORMANCE || !name.getMethodName().contains("Perf"));

        System.out.println("--- Running " + name.getMethodName() + " against " + m_strPagePath + " :"
                + getTimeStamp());

        if (ENABLE_REPLICATION_CHECK) {
            checkReplicationQueues(this.getClass().getName(), name.getMethodName());
        }

        System.out.println("Social Comments page path= " + m_strPagePath);
        if (m_strPagePath == null || !commonsAdminAuthorClient.exists(m_strPagePath)) {
            setCommonStoreLanguage(this.m_strCommonStoreLangCode);
            setupGeometrixxOutdoorsSocialComments(getCurrentClassName());
            m_strPagePath = getSocialCommentsPath();
        }

        // apply Common store before doing anything else
        final String strConfig[] = {m_strCommonStorePath};
        TranslationCloudConfig.applyCloudConfigToPage(m_clientInstance, m_strPagePath, strConfig);

        final String serverURL = QuickstartOptions.getServerUrlPublish();
        rootContext = QuickstartOptions.getRootContextPublish();

        m_machineTranslationConsole =
            new MachineTranslationConsoleControl(serverURL, rootContext, USER_NAME, USER_PASSWORD);

        // we need lang, client instance, path, user, lang_array, machine
        // console
        m_machineTranslationConsole.setConsoleConfiguration(m_languageArray, m_iPersistOption);
    }

    String m_strLang;
    CommonsITClient m_clientInstance;
    String m_strPagePath;
    String m_strUserLanguage;
    ArrayList<String> m_languageArray;
    MachineTranslationConsoleControl m_machineTranslationConsole;
    String m_strCommonStorePath;
    String m_strCommonStoreLangCode;
    int m_iPersistOption;

    public SoCoMTTranslationBaseIT(final String strLang, final CommonsITClient clientInstance,
        final String strUserLanguage, final ArrayList<String> languageArray, final int iPersistOption,
        final String strCommonStoreLangCode) throws UnsupportedEncodingException, ClientException {
        this.m_clientInstance = clientInstance;
        this.m_strUserLanguage = strUserLanguage;
        this.m_strLang = strLang;
        this.m_languageArray = languageArray;
        this.m_strCommonStorePath =
            TranslationCloudConfig.getCommonStorePath(this.m_clientInstance, strCommonStoreLangCode);
        this.m_strCommonStoreLangCode = strCommonStoreLangCode;
        if (this.m_strCommonStoreLangCode == null) {
            // default common store lang is en
            this.m_strCommonStoreLangCode = "en";
        }
        this.m_iPersistOption = iPersistOption;
    }

    protected void baseTestCommentTranslation() throws ClientException, InterruptedException, HttpException,
        IOException, JSONException, SAXException, ParserConfigurationException {
        // get a random string based on provided language
        final Map<String, String> languageMap = LanguageDataSet.getRandomStringMap();
        final String strLanguageComment = languageMap.get(m_strLang);

        // first create comment
        final String strCommentPath = m_clientInstance.createSocialComment(m_strPagePath, strLanguageComment);

        verifyCommonStoreLanguage(strCommentPath);
        // check for comment language detection
        waitForTranslationNode(strCommentPath, m_strLang);
        // if auto persist on or not,
        if (m_machineTranslationConsole.isAutoPersistOn()) {
            // check for all translated string
            waitForAllLanguageNodes(strCommentPath, m_strLang);
        } else {
            // now check there is no jrc node present
            Thread.sleep(SLEEP_MILLISECOND);
            checkNoChildPresent(strCommentPath);
        }
    }

    private JsonNode getChildNode(final String strPath, final String strPropertyName) throws ClientException {
        final String strQuery = String.format("%s.json", strPath);
        final String strOutput = m_clientInstance.get(strQuery);
        final JsonNode jsonNode = JsonUtils.getJsonNodeFromString(strOutput);
        final JsonNode childNode = jsonNode.get(strPropertyName);
        return childNode;
    }

    protected String getNodeProperty(final String strPath, final String strPropertyName) throws ClientException {
        final RetryLoop.Condition c = new RetryLoop.Condition() {

            @Override
            public boolean isTrue() throws Exception {
                return getChildNode(strPath, strPropertyName) != null;
            }

            @Override
            public String getDescription() {
                return "childNode is null";
            }
        };

        new RetryLoop(c, TimeoutsProvider.getInstance().getTimeout(WAIT_TRANSLATION_NODE_TIME), TimeoutsProvider
            .getInstance().getTimeout(WAIT_INTERVAL));

        final JsonNode childNode = getChildNode(strPath, strPropertyName);
        return childNode == null ? null : childNode.getTextValue();
    }

    protected int getChildCount(final String strPath) throws ClientException {
        final String strQuery = "/bin/querybuilder.json?group.p.or=true&group.1_path=" + strPath;
        final String strOutput = m_clientInstance.get(strQuery);
        final JsonNode jsonNode = JsonUtils.getJsonNodeFromString(strOutput);
        final JsonNode childNode = jsonNode.get("hits");
        return childNode.size();
    }

    protected void checkNoChildPresent(final String strCommentPath) throws ClientException {
        final String translatedPath = strCommentPath + "/" + TRANSLATION_NODE;
        assertTrue(getChildCount(translatedPath) == 0);
    }

    protected void verifyCommonStoreLanguage(final String strCommentPath) {
        // path is like /content/usergenerated/asi/jcr/content/geometrixx-outdoors/en
        // check for 5th
        final String paths[] = strCommentPath.split("/");
        if (paths != null && paths.length >= 8) {
            final String strLang = paths[7];
            assertTrue("Expected common store path " + m_strCommonStoreLangCode + " but got " + strLang,
                m_strCommonStoreLangCode.equalsIgnoreCase(strLang));
        } else {
            assertTrue("Common Store path is less than 6 " + strCommentPath, false);
        }
    }

    protected void waitForAllLanguageNodes(final String strCommentPath, final String strLangCommentAdded)
        throws ClientException, HttpException, IOException, JSONException, SAXException, ParserConfigurationException {
        final Iterator<String> it = m_languageArray.iterator();
        while (it.hasNext()) {

            final String strCurrentLanguage = it.next();
            if (!strCurrentLanguage.equalsIgnoreCase(strLangCommentAdded)) {
                final String translatedPath = strCommentPath + "/" + TRANSLATION_NODE + "/" + strCurrentLanguage;
                final RetryLoop.Condition c = new RetryLoop.Condition() {

                    @Override
                    public boolean isTrue() throws Exception {
                        // later on we can have translated string check
                        return m_clientInstance.exists(translatedPath);
                    }

                    @Override
                    public String getDescription() {
                        return "Comment Node of Translation String" + " not found at " + translatedPath;
                    }
                };
                new RetryLoop(c, TimeoutsProvider.getInstance().getTimeout(WAIT_TRANSLATION_NODE_TIME),
                    TimeoutsProvider.getInstance().getTimeout(500));

                // Check for translated string now
                final String strTranslatedString = getNodeProperty(translatedPath, JCR_DESCRIPTION);
                String strDetectedLang = null;
                int count = 0;
                do {
                    count++;
                    strDetectedLang = detectLanguage(strTranslatedString);
                } while (strDetectedLang == null && count <= MAX_NUM_RETRY);

                final String strConvertedLang = LanguageDataSet.getSupportedLanguageString(strDetectedLang);
                if( !strCurrentLanguage.equals(strConvertedLang) ){
                	System.out.println(" strCommentPathstrCommentPathstrCommentPath "+strCommentPath);
                }
                assertTrue("Expected translation language " + strCurrentLanguage + " but got " + strConvertedLang
                        + " unconverted Language was " + strDetectedLang,
                    strCurrentLanguage.equalsIgnoreCase(strConvertedLang));
            }
        }
    }

    protected void waitForTranslationNode(final String strCommentPath, final String expectedLanguage)
        throws InterruptedException, ClientException {
        final String translatedPath = strCommentPath + "/" + TRANSLATION_NODE;
        final RetryLoop.Condition c = new RetryLoop.Condition() {

            @Override
            public boolean isTrue() throws Exception {
                return m_clientInstance.exists(translatedPath);
            }

            @Override
            public String getDescription() {
                return "Comment Node of Translation String" + " not found at " + translatedPath;
            }
        };
        new RetryLoop(c, TimeoutsProvider.getInstance().getTimeout(WAIT_TRANSLATION_NODE_TIME), TimeoutsProvider
            .getInstance().getTimeout(WAIT_INTERVAL));
        // we should check if the jcr:descrption has language set or not
        final String strLangCode = getNodeProperty(translatedPath, AS_LANGUAGE_PROP);
        assertTrue("Expected language code " + expectedLanguage + " but got " + strLangCode,
            expectedLanguage.equalsIgnoreCase(strLangCode));
    }

    protected static String detectLanguage(final String toDetectSource) throws HttpException, IOException,
        JSONException, SAXException, ParserConfigurationException {

        String langID = null;
        final String access_token = AccessToken.getAccessToken();

        final String url = BING_DETECTION_SERVICE_URL + "?text=" + URLEncoder.encode(toDetectSource, ENCODING);

        final GetMethod httpget = new GetMethod(url);
        httpget.addRequestHeader(new Header("Authorization", "Bearer" + " " + access_token));
        final HttpClient client = new HttpClient();
        final int status_code = client.executeMethod(httpget);
        if (status_code == HttpStatus.SC_OK) {
            final InputStream instream = httpget.getResponseBodyAsStream();
            try {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder docbuilder = factory.newDocumentBuilder();
                final Document doc = docbuilder.parse(instream);
                langID = doc.getDocumentElement().getChildNodes().item(0).getNodeValue();
            } finally {
                instream.close();
            }
        }
        httpget.releaseConnection();
        return langID;
    }

    protected SoCoClient createTestUser(String strLang, final boolean isAdmin) throws ClientException {
        SecurityClient sPublishClient = adminPublish.getClient(SecurityClient.class);

        final String randomToken = new RandomString(getCurrentClassName()).nextString();
        final Date date = new Date();
        if (strLang == null) {
            strLang = "";
        }
        final String user = "testuser_" + strLang + "_" + randomToken + "_" + date.getTime();

        if (sPublishClient == null) {
            sPublishClient = adminPublish.getClient(SecurityClient.class);
        }

        final CQPermissions permissionsObj = new CQPermissions(sPublishClient);
        Group adminGroup = null;
        if (isAdmin) {
            adminGroup = sPublishClient.getManager().getGroup("administrators");
            assertNotNull("Didn't find admin group!", adminGroup);
        }

        final HashMap<String, String> profileProps = new HashMap<String, String>();
        profileProps.put(UserProfile.PROPERTY_EMAIL, user);
        profileProps.put(UserProfile.PROPERTY_GIVEN_NAME, user);
        profileProps.put(UserProfile.PROPERTY_FAMILY_NAME, user);

        if (isAdmin) {
            addUser(sPublishClient, user, user, null, profileProps, strLang, new Group[]{adminGroup});
        } else {
            addUser(sPublishClient, user, user, null, profileProps, strLang, new Group[]{});
        }
        permissionsObj.changePermissions(user, "/etc/workflow", true, true, true, true, true, true, true, 200);

        final SoCoClient testClient =
            new SoCoClient(QuickstartOptions.getServerUrlPublish(), QuickstartOptions.getRootContextPublish(), user,
                user);
        return testClient;
    }

    protected User addUser(final SecurityClient client, final String userId, final String password,
        final String intermediatePath, final Map<String, String> profileMap, final String strLang,
        final Group[] assignedGroups) throws ClientException {

        final FormEntityBuilder feb = new FormEntityBuilder();
        feb.addParameter("_charset_", "utf-8");
        feb.addParameter("createUser", "1");
        feb.addParameter("authorizableId", userId);
        feb.addParameter("rep:password", password);
        if (intermediatePath != null) {
            feb.addParameter("intermediatePath", intermediatePath);
        }
        Iterator<String> it;
        if (profileMap != null) {
            final Set<String> profileProps = profileMap.keySet();
            for (it = profileProps.iterator(); it.hasNext();) {
                final String propName = it.next();
                final String propValue = profileMap.get(propName);
                if (propValue != null) {
                    feb.addParameter("./profile/" + propName, propValue);
                }
            }
        }
        if (strLang != null && !strLang.isEmpty()) {
            feb.addParameter("./preferences/language", strLang);
        }

        final AuthorizableManager mgr = client.getManager();
        final RequestExecutor exec = mgr.doPost(feb, 201);

        HttpUtils.verifyHttpStatus(exec, HttpUtils.getExpectedStatus(201, 201));
        final User user = new User(client, userId);
        if (assignedGroups != null) {
            for (final Group assignedGroup : assignedGroups) {
                assignedGroup.addMembers(new Authorizable[]{user}, new int[]{200});
            }
        }
        return user;
    }
}
