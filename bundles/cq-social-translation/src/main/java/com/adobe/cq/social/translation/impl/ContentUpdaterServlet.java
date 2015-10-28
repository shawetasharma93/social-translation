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
package com.adobe.cq.social.translation.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import com.adobe.cq.social.translation.ContentUpdaterService;

@SlingServlet(paths = "/bin/contentUpdaterService", methods = {"GET", "POST"})
public class ContentUpdaterServlet extends SlingAllMethodsServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Reference
    ContentUpdaterService contentUpdaterService;

    /**
     * This will return the current status of the ContentUpdaterService JSON status
     * @param request - The SlingHttpServletRequest
     * @param response - The SlingHttpServletResponse, will contain JSON formatted by the ContentUpdaterService
     * @throws javax.servlet.ServletException when something is wrong with the server
     * @throws java.io.IOException when an error occurs in an I/O operation
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("application/json");
        final PrintWriter writer = response.getWriter();
        writer.append(contentUpdaterService.getStatusJSON().toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * This is a convienent entry point to this updater service.
     * @param request - The SlingHttpServletRequest
     * @param response - The SlingHttpServletResponse
     * @throws javax.servlet.ServletException when something is wrong with the server
     * @throws java.io.IOException when an error occurs in an I/O operation
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html");
        final PrintWriter writer = response.getWriter();
        writer.append("Running Content Updater Service");
        writer.append("<a href='/bin/contentUpdaterService'>For status updates </a>");
        writer.close();
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        contentUpdaterService.run();

    }

}
