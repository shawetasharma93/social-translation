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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.social.translation.TranslationUtil;
import com.adobe.cq.social.translation.TranslationVariables;
import com.adobe.cq.social.ugcbase.TranslationSaveQueue;
import com.adobe.cq.social.ugcbase.TranslationUpdate;

@Component(immediate = true, enabled = true, label = "Translation Save Queue")
@Service(value = TranslationSaveQueue.class)
public class TranslationSaveQueueImpl implements TranslationSaveQueue {

    private static final Logger LOG = LoggerFactory.getLogger(TranslationSaveQueueImpl.class);

    /**
     * MAX_SAVES is a fail safe against the recursive calls to registerUpdate()
     */
    private static final int MAX_SAVES = 200;

    /**
     * The name of the http://sling.apache.org/documentation/bundles/scheduler-service-commons-scheduler.html
     * scheduler Job name
     */
    private static final String SAVE_JOB_NAME = "TranslationSaveJob";

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private SlingRepository repository;

    @Reference
    private final Scheduler scheduler = null;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC)
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * This queue is the basis for all of the getters in this service
     */
    private ConcurrentLinkedQueue<TranslationUpdate> concurrentLinkedQueue;

    /**
     * numberOfUpdates is checked against MAX_SAVES, as a failsafe against infinite recursion
     */
    private int numberOfUpdates;

    /**
     * Session context that the content update will operate within
     */
    private Session serviceSession;

    /**
     * Resource resolver to be passed to the Abstract ContentUpdater
     */
    private ResourceResolver resourceResolver;

    /**
     * When this service is activated, it will create a new empty queue, with 0 updates logged
     * @param context (autowired by OSGI)
     */
    protected void activate(final org.osgi.service.component.ComponentContext context) {
        this.concurrentLinkedQueue = new ConcurrentLinkedQueue<TranslationUpdate>();
        this.numberOfUpdates = 0;
        try {
            this.serviceSession = ServiceUserWrapper.loginService(repository, TranslationUtil.UGC_WRITER, null);
            final Map<String, Object> authInfo = new HashMap<String, Object>();
            authInfo.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, this.serviceSession);
            this.resourceResolver = resourceResolverFactory.getResourceResolver(authInfo);
        } catch (final RepositoryException e) {
            LOG.error("error in creating service session: ", e);
        } catch (final LoginException e) {
            LOG.error("Error while content updation.", e);
        }
    }

    @Override
    public synchronized ConcurrentLinkedQueue<TranslationUpdate> getUpdateQueue() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("in getUpdateQueue() , returning with size" + concurrentLinkedQueue.size());
        }
        return concurrentLinkedQueue;
    }

    @Override
    public void saveQueue() {
        LOG.trace("In saveQueue()");
        final ConcurrentLinkedQueue<TranslationUpdate> updateQueue = this.getUpdateQueue();
        saveQueue(updateQueue.toArray(new TranslationUpdate[updateQueue.size()]));
    }

    @Override
    public void saveQueue(final int numberOfUpdates) {
        LOG.debug("Saving queue for " + numberOfUpdates + " updates..");
        final ConcurrentLinkedQueue<TranslationUpdate> updateQueue = this.getUpdateQueue();
        final List<TranslationUpdate> updatesToSave = new LinkedList<TranslationUpdate>();
        /**
         * Iterate through the queue, and increment the updatestToSave list
         */
        int i = 0;
        for (final Iterator<TranslationUpdate> it = updateQueue.iterator(); it.hasNext() && i < numberOfUpdates; i++) {
            updatesToSave.add(it.next());
        }
        saveQueue(updatesToSave.toArray(new TranslationUpdate[updatesToSave.size()]));
    }

    /**
     * Very simple job that will check the queue, and if it's between 0 and the batchLimit (from
     * {@link com.adobe.cq.social.translation.TranslationVariables} in length than the queue will get processed
     */
    private class UpdateJob implements Runnable {
        @Override
        public void run() {
            final int queueSize = getUpdateQueue().size();
            /**
             * Attempting to be defensive here, if the queue is empty, unregister this job, otherwise save up to the
             * batch_limit {@link com.adobe.cq.social.translation.TranslationVariables}
             */
            if (queueSize > 0) {
                saveQueue(queueSize);
            } else {
                LOG.trace("NOT saving queue,empty. Unscheduling save job ");
                try {
                    scheduler.removeJob(SAVE_JOB_NAME);
                } catch (final NoSuchElementException e) {
                    LOG.warn("Error removing updateJob", e);
                }
            }
        }
    }

    @Override
    public void registerUpdate() {
        final int currentQueueSize = this.concurrentLinkedQueue.size();
        if (LOG.isTraceEnabled()) {
            LOG.trace("in registerUpdate() , current queue size = " + currentQueueSize + " , number of tries so far"
                    + numberOfUpdates);
        }
        /**
         * if the sessionSaveInterval is less than 1, than we do no batching, but immediately save / clear the queue
         */
        numberOfUpdates += 1;
        if (TranslationVariables.getSessionSaveInterval() < 1) {
            saveQueue();
        } else {
            /**
             * Once we hit here, we first check against our failsafe (MAX_SAVES) Then, if the queue is greater than
             * the batching limit, we attempt to do saves, in groups up to the Batch Limit from
             * {@link com.adobe.cq.social.translation.TranslationVariables}
             */
            final int currentBatchLimit = TranslationVariables.getTranslationSaveBatchLimit();
            if (currentBatchLimit > 0) {
                if (numberOfUpdates < MAX_SAVES && currentQueueSize > currentBatchLimit) {
                    LOG.trace("Saving queue @ BATCH_LIMIT of " + currentBatchLimit);
                    saveQueue(currentBatchLimit);
                    /**
                     * Recurse here to clear the queue. Every recursion increments 'numberOfUpdates'
                     */
                    registerUpdate();
                } else {
                    /**
                     * Break the recursion, reset numberOfUpdates
                     */
                    LOG.trace("NOT Saving queue, under batch limit of " + currentBatchLimit
                            + " or too many updates : " + numberOfUpdates);
                    numberOfUpdates = 0;
                }
            }
        }
        if (getUpdateQueue().size() > 0) {
            /**
             * Register job / queue flush The wait period is in seconds, and dictates how long the system waits before
             * attempting to batch save
             */
            final Runnable jobToRun = new UpdateJob();
            final long waitPeriod = TranslationVariables.getSessionSaveInterval();
            LOG.trace("Scheduling job in the future @ " + waitPeriod);
            try {
                this.scheduler.addPeriodicJob(SAVE_JOB_NAME, jobToRun, null, waitPeriod, false);
            } catch (final Exception e) {
                LOG.error("Exception in adding periodic job", e);
                /**
                 * In case the scheduler fails, run it immediately, making sure to unschedule the job
                 */
                jobToRun.run();
                try {
                    scheduler.removeJob(SAVE_JOB_NAME);
                } catch (final NoSuchElementException e3) {
                    LOG.warn("Error removing updateJob", e3);
                }
            } finally {
                /**
                 * If the queue is empty at this point, then we unregister the updateJob
                 */
                if (getUpdateQueue().size() < 1) {
                    LOG.trace("unscheduling job, since the queue is now empty");
                    try {
                        scheduler.removeJob(SAVE_JOB_NAME);
                    } catch (final NoSuchElementException e) {
                        LOG.warn("Error removing updateJob", e);
                    }
                }
            }
        }
    }

    /**
     * This will save the array of updates to the JCR
     * @param updates Array of TranslationUpdates
     */
    private void saveQueue(final TranslationUpdate[] updates) {

        if (updates.length > 0) {

            try {

                /**
                 * Iterate through every update, resolve its resource from the resource path, Use TranslationUtil to
                 * save the translation (passing in a null session)
                 */
                for (final TranslationUpdate update : updates) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("looping through updates to save, at " + update.getResourcePath());
                    }
                    final Resource updateResource = resourceResolver.getResource(update.getResourcePath());
                    if (updateResource != null) {
                        TranslationUtil.saveTranslation(update.getToLan(), update.getTranslation(), updateResource,
                            serviceSession);
                    } else {
                        LOG.warn("Error getting resource @ " + update.getResourcePath());
                    }
                }
                /**
                 * After we have updated all of those nodes, we are ready to save
                 */
                LOG.trace("Saving service session");
                serviceSession.save();
            } catch (final Exception e) {
                LOG.error("Error in saveQueue()", e);
            } finally {
                LOG.trace("in finally for saveQueue, removing saves from queue");
                /**
                 * Remove all the updates that were saved from the queue
                 */
                for (final TranslationUpdate update : updates) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Removing " + update.getResourcePath());
                    }
                    getUpdateQueue().remove(update);
                }
                if (LOG.isTraceEnabled()) {
                    LOG.trace("Removes complete, queue size " + getUpdateQueue().size());
                }

            }
        } else {
            LOG.trace("in saveQueue(TranslationUpdate[]), no updates");
        }

    }

    /**
     * Clean up here
     * @param context of the component
     */
    protected void deactivate(final org.osgi.service.component.ComponentContext context) {

        if (serviceSession != null && serviceSession.isLive()) {
            serviceSession.logout();
        }

        if (resourceResolver != null && resourceResolver.isLive()) {
            resourceResolver.close();
            resourceResolver = null;
        }

    }

}
