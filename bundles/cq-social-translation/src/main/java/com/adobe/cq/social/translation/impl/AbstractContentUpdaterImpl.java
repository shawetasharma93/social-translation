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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.translation.AbstractContentUpdater;
import com.adobe.cq.social.translation.TranslationUtil;
import com.adobe.granite.translation.api.TranslationConstants;
import com.adobe.granite.translation.api.TranslationManager;

public class AbstractContentUpdaterImpl extends AbstractContentUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractContentUpdaterImpl.class);

    public AbstractContentUpdaterImpl(final Node nd, final Session session, final String contentProp,
        final TranslationManager tm, final ResourceResolver resourceResolver) {
        super(nd, session, contentProp, tm, resourceResolver);
    }

    public AbstractContentUpdaterImpl(final List<Node> nds, final Session session, final String contentProp,
        final TranslationManager tm, final ResourceResolver resourceResolver) {
        super(nds, session, contentProp, tm, resourceResolver);
    }

    /**
     * This will iterate through the constructed map of nodes
     * @return - Mapping between the passed in Nodes and the content language mapping
     */
    @Override
    public Map<Node, String> call() {
        LOG.trace("Scrying resource");

        final Map<Node, String> nodeWorkResults = new HashMap<Node, String>();
        Boolean sessionIsWorthSaving = false;

        /**
         * Iterate through every node, and build the result
         */
        for (final Node node : this.nodes) {
            if (node != null) {
                try {
                    final String path = node.getPath();
                    LOG.trace("Resource at " + path + " is not null, setting updated property");
                    final String languageCodeUpdated = this.addLanguageCode(node, this.contentProperty);
                    nodeWorkResults.put(node, languageCodeUpdated);

                    if (languageCodeUpdated != null && !languageCodeUpdated.isEmpty()) {
                        sessionIsWorthSaving = true;
                    }

                } catch (final RepositoryException e) {
                    LOG.error("Unable to save the updated stamp", e);
                    nodeWorkResults.put(node, null);
                }
            } else {
                LOG.trace("Resource was null, assigning false");
                nodeWorkResults.put(node, null);
            }
        }
        /**
         * Save the session, only once per set of nodes, for performance
         */
        try {
            if (sessionIsWorthSaving) {
                session.save();
            }
        } catch (final InvalidItemStateException e) {
            LOG.error("InvalidItemStateException", e);
        } catch (final NoSuchNodeTypeException e) {
            LOG.error("NoSuchNodeTypeException", e);
        } catch (final LockException e) {
            LOG.error("LockException", e);
        } catch (final VersionException e) {
            LOG.error("VersionException", e);
        } catch (final ReferentialIntegrityException e) {
            LOG.error("ReferentialIntegrityException", e);
        } catch (final ConstraintViolationException e) {
            LOG.error("ConstraintViolationException", e);
        } catch (final ItemExistsException e) {
            LOG.error("ItemExistsException", e);
        } catch (final AccessDeniedException e) {
            LOG.error("AccessDeniedException", e);
        } catch (final RepositoryException e) {
            LOG.error("RepositoryException", e);
        }
        /**
         * After looping through every node, pass back the result set
         */
        return nodeWorkResults;
    }

    /**
     * This method sets, and returns the language determination from the content node.
     * @param node - The node to lookup the language of
     * @param property - The property where the content resides
     * @return - The language string, or null if the lookup was nondeterministic
     * @throws RepositoryException
     */
    private String addLanguageCode(final Node node, final String property) throws RepositoryException {
        final String nodePath = node.getPath();
        final Resource thisResource = this.resourceResolver.getResource(nodePath);
        if (thisResource != null && translationManager != null) {
            // We pass in a null session to avoid the session.save() operation
            return TranslationUtil.addLanguageCode(thisResource, null, property,
                TranslationConstants.ContentType.HTML, translationManager);
        }
        return null;
    }

}
