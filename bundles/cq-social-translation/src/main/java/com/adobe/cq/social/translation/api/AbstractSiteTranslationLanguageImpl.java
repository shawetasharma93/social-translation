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
package com.adobe.cq.social.translation.api;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.core.BaseSocialComponent;

public class AbstractSiteTranslationLanguageImpl extends BaseSocialComponent implements SiteTranslationLanguage {

    protected final Resource resource;
    protected final ValueMap properties;

    public AbstractSiteTranslationLanguageImpl(final Resource resource, final ClientUtilities clientUtils) {
        super(resource, clientUtils);
        this.resource = resource;
        this.properties = this.resource.adaptTo(ValueMap.class);
    }

    @Override
    public String getLanguageCode() {
        return properties == null ? "" : properties.get(SiteTranslationLanguage.PROPERTY_LANGUAGE_CODE, "");
    }

    @Override
    public String getLanguageName() {
        return properties == null ? "" : properties.get(SiteTranslationLanguage.PROPERTY_LANGUAGE_NAME, "");
    }

    @Override
    public String getLanguageSetting() {
        return properties == null ? "" : properties.get(SiteTranslationLanguage.PROPERTY_LANGUAGE_SETTING, "");
    }

}
