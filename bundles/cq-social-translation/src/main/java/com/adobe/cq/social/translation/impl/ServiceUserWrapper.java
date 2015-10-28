/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2015 Adobe Systems Incorporated
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

import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;

/** */
public final class ServiceUserWrapper {
    private ServiceUserWrapper() {
    }

    /**
     * @param repo repo
     * @param subServiceName subservicename
     * @param workspace workspace
     * @return session
     * @throws LoginException on failure
     * @throws RepositoryException on failure
     */
    public static Session loginService(final SlingRepository repo, final String subServiceName, final String workspace)
        throws LoginException, RepositoryException {
        return repo.loginAdministrative(workspace);
    }

    /**
     * @param rrf rrf
     * @param authenticationInfo authenticationInfo
     * @return resolver
     * @throws org.apache.sling.api.resource.LoginException on failure
     */
    public static ResourceResolver getServiceResourceResolver(final ResourceResolverFactory rrf,
        final Map<String, Object> authenticationInfo) throws org.apache.sling.api.resource.LoginException {
        Map<String, Object> strippedAuthInfo = new HashMap<String, Object>();
        strippedAuthInfo.putAll(authenticationInfo);
        strippedAuthInfo.remove(ResourceResolverFactory.SUBSERVICE);
        if (strippedAuthInfo.size() > 0) {
            throw new org.apache.sling.api.resource.LoginException("Unsupported authenticationInfo: "
                    + strippedAuthInfo.toString());
        }
        return rrf.getServiceResourceResolver(null);
    }
}
