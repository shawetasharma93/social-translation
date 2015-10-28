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

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.translation.api.AbstractSiteTranslationLanguageCollectionImpl;
import com.adobe.cq.social.translation.api.SiteTranslationLanguageCollection;

@SuppressWarnings("unchecked")
public class SiteTranslationLanguageCollectionImpl extends AbstractSiteTranslationLanguageCollectionImpl implements
    SiteTranslationLanguageCollection {

    public SiteTranslationLanguageCollectionImpl(final Resource resource, final ClientUtilities clientUtils) {
        super(resource, clientUtils);
    }

}
