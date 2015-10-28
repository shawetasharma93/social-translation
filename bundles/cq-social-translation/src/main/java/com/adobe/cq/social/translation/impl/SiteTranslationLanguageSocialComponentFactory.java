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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.QueryRequestInfo;
import com.adobe.cq.social.scf.SocialComponent;
import com.adobe.cq.social.scf.SocialComponentFactory;
import com.adobe.cq.social.scf.core.AbstractSocialComponentFactory;
import com.adobe.cq.social.translation.api.SiteTranslationLanguage;

@Component
@Service
public class SiteTranslationLanguageSocialComponentFactory extends AbstractSocialComponentFactory implements
    SocialComponentFactory {

    @Override
    public SocialComponent getSocialComponent(final Resource resource) {
        return new SiteTranslationLanguageImpl(resource, getClientUtilities(resource.getResourceResolver()));
    }

    @Override
    public SocialComponent getSocialComponent(final Resource resource, final SlingHttpServletRequest request) {
        return new SiteTranslationLanguageImpl(resource, getClientUtilities(request));
    }

    @Override
    public SocialComponent getSocialComponent(final Resource resource, final ClientUtilities clientUtils,
        final QueryRequestInfo requestInfo) {
        return new SiteTranslationLanguageImpl(resource, clientUtils);
    }

    @Override
    public String getSupportedResourceType() {
        return SiteTranslationLanguage.RESOURCE_TYPE;
    }

}
