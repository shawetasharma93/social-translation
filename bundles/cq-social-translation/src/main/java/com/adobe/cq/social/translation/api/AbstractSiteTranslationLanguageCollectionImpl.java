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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.social.scf.ClientUtilities;
import com.adobe.cq.social.scf.CollectionPagination;
import com.adobe.cq.social.scf.SocialComponentFactory;
import com.adobe.cq.social.scf.core.BaseSocialComponent;
import com.adobe.cq.social.scf.core.CollectionSortedOrder;

public class AbstractSiteTranslationLanguageCollectionImpl extends BaseSocialComponent implements
    SiteTranslationLanguageCollection {

    /** The site translation languages. */
    protected List<SiteTranslationLanguage> availableLanguages = new ArrayList<SiteTranslationLanguage>();

    public AbstractSiteTranslationLanguageCollectionImpl(final Resource resource, final ClientUtilities clientUtils) {
        super(resource, clientUtils);
        init(resource, clientUtils);
    }

    protected void init(final Resource resource, final ClientUtilities clientUtilities) {
        getLanguages(this.getRootPath(), clientUtilities);
    }

    protected String getRootPath() {
        return DEFAULT_LANGUAGES_ROOT;
    }

    protected void getLanguages(final String path, final ClientUtilities clientUtilities) {
        if (!StringUtils.isEmpty(path)) {
            final Resource resource = this.resource.getResourceResolver().getResource(path);
            if (resource != null) {
                final Iterator<Resource> resourceIterator = resource.listChildren();
                final SocialComponentFactory translationLanguageSocialFactory =
                    clientUtilities.getSocialComponentFactoryManager().getSocialComponentFactory(
                        this.getTranslationLanguageResourceType());
                final String languageResourceType = this.getTranslationLanguageResourceType();
                while (resourceIterator.hasNext()) {
                    final Resource child = resourceIterator.next();
                    if (child.isResourceType(languageResourceType)) {
                        final SiteTranslationLanguage language =
                            (SiteTranslationLanguage) translationLanguageSocialFactory.getSocialComponent(child);
                        if (language != null) {
                            availableLanguages.add(language);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the template resource type.
     * @return the template resource type
     */
    protected String getTranslationLanguageResourceType() {
        return SiteTranslationLanguage.RESOURCE_TYPE;
    }

    @Override
    public int getTotalSize() {
        return availableLanguages.size();
    }

    @Override
    public List getItems() {
        return availableLanguages;
    }

    @Override
    public void setPagination(final CollectionPagination pagination) {
        // no-op
    }

    @Override
    public void setSortedOrder(final CollectionSortedOrder sortedOrder) {
        // no-op
    }

}
