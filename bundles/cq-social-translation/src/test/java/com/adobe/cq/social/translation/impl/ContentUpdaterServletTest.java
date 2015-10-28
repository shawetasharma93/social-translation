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

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.adobe.cq.social.translation.ContentUpdaterService;

public class ContentUpdaterServletTest {

    private final ContentUpdaterServlet contUpdateServ = new ContentUpdaterServlet();
    private final SlingHttpServletRequest m_httpReq = mock(SlingHttpServletRequest.class);
    private final SlingHttpServletResponse m_httpResp = mock(SlingHttpServletResponse.class);
    private final JSONObject m_JSONObj = mock(JSONObject.class);
    private final PrintWriter pw = new PrintWriter(System.out, true);
    private final ContentUpdaterService m_contUpdateService = mock(ContentUpdaterService.class);

    @Before
    public void setUp() throws Exception {
        contUpdateServ.contentUpdaterService = m_contUpdateService;
        Mockito.when(m_httpResp.getWriter()).thenReturn(pw);
        Mockito.when(m_contUpdateService.getStatusJSON()).thenReturn(m_JSONObj);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        contUpdateServ.doGet(m_httpReq, m_httpResp);
        pw.close();
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        contUpdateServ.doPost(m_httpReq, m_httpResp);
        pw.close();
    }
}
