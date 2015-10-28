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

public interface ContentUpdaterService extends Runnable {

    /**
     * @return - The current status of the job
     */
    public String getStatus();

    /**
     * @return - JSON format of this Runnable's status
     */
    public org.json.JSONObject getStatusJSON();

    /**
     * @return Whether or not the update is complete
     */
    public boolean isComplete();

}
