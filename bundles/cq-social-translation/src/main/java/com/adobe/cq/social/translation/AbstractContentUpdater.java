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

package com.adobe.cq.social.translation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.granite.translation.api.TranslationManager;

public abstract class AbstractContentUpdater implements Callable<Map<Node, String>> {

    /**
     * The nodes to update
     */
    protected List<Node> nodes;

    /**
     * The session to act from
     */
    protected final Session session;

    /**
     * The content property to look for
     */
    protected final String contentProperty;

    /**
     * Translation manager will be instantiated within the constructor
     */
    protected final TranslationManager translationManager;

    /**
     * The resourceResolver used by this class and it's sub classes
     */
    protected final ResourceResolver resourceResolver;

    /**
     * Instantiates a content updater based upon the given node and session
     * @param nd - The node to act upon
     * @param session - The session to use. It will be left open, and must be closed by the calling class. The session
     *            will be saved after processing the entire set of nodes.
     * @param contentProp - The content property (i.e. jcr:description) to process
     * @param tm - translation manager
     * @param resourceResolver - to resolve resources
     */
    public AbstractContentUpdater(final Node nd, final Session session, final String contentProp,
        final TranslationManager tm, final ResourceResolver resourceResolver) {
        this(java.util.Collections.singletonList(nd), session, contentProp, tm, resourceResolver);
    }

    /**
     * Instantiates a content updater based upon the given nodes and session
     * @param nds - The list of nodes to act upon
     * @param session - The session to use. It will be left open, and must be closed by the calling class. The session
     *            will be saved after processing the entire set of nodes.
     * @param contentProp - The content property (i.e. jcr:description) to process
     * @param tm - translation manager
     * @param resourceResolver - to resolve resources
     */
    public AbstractContentUpdater(final List<Node> nds, final Session session, final String contentProp,
        final TranslationManager tm, final ResourceResolver resourceResolver) {
        this.nodes = nds;
        this.session = session;
        this.contentProperty = contentProp;
        this.translationManager = tm;
        this.resourceResolver = resourceResolver;
    }

    /**
     * @return A mapping between the resource acted upon, and the language code / string of the content. The language
     *         String will be null if the language lookup was non-deterministic
     */
    @Override
    public abstract Map<Node, String> call();

}
