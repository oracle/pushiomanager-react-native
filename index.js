/*
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v1.0 as shown at https://oss.oracle.com/licenses/upl.
*/

import { NativeModules, NativeEventEmitter, DeviceEventEmitter, Platform } from 'react-native';

const RCTPushIOManager = NativeModules.PushIOManager;
const RCTPushIOEventEmitter = Platform.select({
    ios: new NativeEventEmitter(NativeModules.PushIOEventEmitter),
    android: DeviceEventEmitter
});

export default class PushIOManager {
    /**
    *@callback callback
    *@param {failure} error object
    *@param {success}  info object
    */

    /**
     * @typedef {object} Preference
     * @property {string} key - Unique Identifier for this preference.
     * @property {string} label - Human-Readable description of this preference.
     * @property {string} type - Data type of this preference. Possible values: 'STRING', 'NUMBER', 'BOOLEAN'.
     * @property {string} value - Preference value.
     */

    /**
     * @typedef {object} MessageCenterMessage
     * @property {string} messageID
     * @property {string} subject
     * @property {string} message
     * @property {string} iconURL
     * @property {string} messageCenterName
     * @property {string} deeplinkURL
     * @property {string} richMessageHTML
     * @property {string} richMessageURL
     * @property {string} sentTimestamp
     * @property {string} expiryTimestamp
     */

    /**
     * @typedef {object} InteractiveNotificationCategory
     * @property {string} orcl_category
     * @property {InteractiveNotificationButton[]} orcl_btns
     */

    /**
     * @typedef {object} InteractiveNotificationButton
     * @property {string} id
     * @property {string} action 
     * @property {string} label
     */

    /**
     * @typedef {object} RemoteMessage
     * @property {string} to
     * @property {string=} collapseKey 
     * @property {string=} messageId
     * @property {string=} messageType
     * @property {string=} ttl
     * @property {object} data
     */

    /**
     * @typedef {object} GeoRegion
     * @property {string} geofenceId
     * @property {string} geofenceName 
     * @property {string} zoneName
     * @property {string} zoneId
     * @property {string} source
     * @property {number} deviceBearing
     * @property {number} deviceSpeed
     * @property {number} dwellTime
     * @property {object} extra
     */

    /**
     * @typedef {object} BeaconRegion
     * @property {string} beaconId
     * @property {string} beaconName 
     * @property {string} beaconTag
     * @property {string} beaconProximity
     * @property {string} iBeaconUUID
     * @property {number} iBeaconMajor
     * @property {number} iBeaconMinor
     * @property {string} eddyStoneId1
     * @property {string} eddyStoneId2
     * @property {string} zoneName
     * @property {string} zoneId
     * @property {string} source
     * @property {number} dwellTime
     * @property {object} extra
     */

    /**
     * @typedef {object} ConversionEvent
     * @property {string} orderId
     * @property {number} orderTotal
     * @property {number} orderQuantity
     * @property {number} conversionType
     * @property {object} customProperties
     */

    /**
     * 
    * loggingLevel; to be used with [setLogLevel()]{@link setLogLevel}
    * @typedef {Enumerator}loggingLevel
    * @param {number} NONE:0
    * @param {number} ERROR :1
    * @param {number} INFO: 2
    * @param {number} WARN: 3
    * @param {number} DEBUG: 4
    * @param {number} VERBOSE: 5
    * @readonly
    * @enum {number}
    * @memberof PushIOManager
    * 
    */

    /**
     * Sets the log level to print in console. 
     * @param {number} loggingLevel Numeric value to set type of logging
     */

    static setLogLevel(loggingLevel) {
        RCTPushIOManager.setLogLevel(loggingLevel);
    }

    /**
    * Gets the Responsys SDK version.
    * @param {callback} callback Success callback with the SDK version value. 
    */

    static getLibVersion(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getLibVersion(callback);
        } else {
            RCTPushIOManager.frameworkVersion(callback);
        }
    }

    /**
    * Sets Enable/Disable logging. By default logging is enabled with default Info {@link loggingLevel}. 
    * <br/>Developer can change the log level by calling {@link setLogLevel}.
    * @param {boolean} isEnabled true, enable console log printing. 
    * <br/> false, disable console log printing.
    * 
    */

    static setLoggingEnabled(isEnabled) {
        RCTPushIOManager.setLoggingEnabled(isEnabled);
    }

    /**
    * Registers this app installation with Responsys.
    * 
    * @param {boolean} useLocation Whether to send location data along with the registration request. Passing `true` will show the default system location permission dialog prompt.
    * (User location is not available on iOS platform.)
    * @param {callback} callback callback with boolean value TRUE if register event created and stored successfully, FALSE otherwise.
    * @see {@tutorial Config}
    */


    static registerApp(useLocation, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.registerApp(useLocation, callback);
        } else {
            RCTPushIOManager.registerApp(callback);
        }
    }


    /**
    * Registers this app installation with Responsys.
    * 
    * @param {boolean} enablePushNotification Whether to enable push notifications. Passing `true` will show the default system push notification permission dialog prompt.
    * (Not available on iOS platform.)
    * @param {boolean} useLocation Whether to send location data along with the registration request. Passing `true` will show the default system location permission dialog prompt.
    * (User location is not available on iOS platform.)
    * @param {callback} callback callback with boolean value TRUE if register event created and stored successfully, FALSE otherwise.
    * @see {@tutorial Config}
    */


    static registerApp(enablePushNotification, useLocation, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.registerApp(enablePushNotification, useLocation, callback);
        } else {
            RCTPushIOManager.registerApp(callback);
        }
    }

    /**
     * Unregister this app installation with Responsys. This will prevent the app from receiving push notifications.
     * @see {@tutorial Config}
     * @param {callback} callback callback with boolean value TRUE if unregister event created and stored successfully, 
     * <br/>FALSE otherwise.
     * 
     */

    static unregisterApp(callback) {
        RCTPushIOManager.unregisterApp(callback);
    }

    /**
     * Configures the SDK using the provided config file name.
     * <br/>For Android, the file should be placed in the android <i>src/main/assets</i> directory
     * @see {@tutorial Config}
     * @param {string} fileName Name of the json config file.
     * @param {callback} callback callback with the result of configuration.
     * 
     * 
     */

    static configure(fileName, callback) {
        RCTPushIOManager.configure(fileName, callback);
    }

    /**
     * Overwrites the Responsys API key provided in the pushio_config.json - this method should be called immediately after creating a new PushIOManager.
     *
     * @param {string} apiKey The new API key.
     */
    static overwriteApiKey(apiKey) {
        RCTPushIOManager.overwriteApiKey(apiKey);
    }

    /**
     * Overwrites the Responsys API key provided in the pushio_config.json -
     * this method should be called immediately after creating a new PushIOManager.
     *
     * @param {string} accountToken The new Account token
     */

    static overwriteAccountToken(accountToken) {
        RCTPushIOManager.overwriteAccountToken(accountToken);
    }

    /**
     * Records pre-defined and custom events.
     * <br/>You can set extra properties specific to this event via the properties parameter.
     * 
     * @param {string} eventName name of the event to be tracked
     * @param {object} properties event properties to attach with the given event name.
     * @param {function} callback callback.
     */

    static trackEvent(eventName, properties) {
        RCTPushIOManager.trackEvent(eventName, properties);
    }

    /**
     * Sets the External Device Tracking ID. Useful if you have another ID for this device.
     * @param {string} externalDeviceTrackingID External Device Tracking ID.
     */

    static setExternalDeviceTrackingID(externalDeviceTrackingID) {
        RCTPushIOManager.setExternalDeviceTrackingID(externalDeviceTrackingID);
    }

    /**
     * Gets the External Device Tracking ID.
     * @param {callback} callback callback with External Device Tracking ID
     */

    static getExternalDeviceTrackingID(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getExternalDeviceTrackingID(callback);
        } else {
            RCTPushIOManager.externalDeviceTrackingID(callback);
        }
    }

    /**
     * Associates this app installation with the provided userId in Responsys.
     * <br/> UserId can be set to target individual users for push notifications, sent along with push registration.
     * @param {string} userId User ID
     */

    static registerUserId(userId) {
        RCTPushIOManager.registerUserId(userId);
    }

    /**
     * Gets the User ID set earlier using [registerUserId]{@link registerUserId}.
     * @param {callback} callback callback with userId
     */

    static getRegisteredUserId(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getRegisteredUserId(callback);
        } else {
            RCTPushIOManager.getUserId(callback);
        }
    }

    /**
     * Removes association between this app installation and the User ID that 
     * was set earlier using [registerUserId]{@link registerUserId}.
     * <br/>Generally used when the user logs out.
     */

    static unregisterUserId() {
        RCTPushIOManager.unregisterUserId();
    }

    /**
     * Sends push engagement information to Responsys.
     * <br>Tracks the engagement for the provided engagement metric type with additional properties
     * 
     * @param {engagementType} metric One of [engagementType]{@link engagementType}
     * @param {object=} properties Custom data to be sent along with this request.
     * @param {callback} callback callback. 
     */

    static trackEngagement(metric, properties, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.trackEngagement(metric, properties, callback);
        } else {
            var value = ((metric < 6) ? (metric - 1) : metric);
            RCTPushIOManager.trackEngagement(value, properties, callback);
        }
    }

    /**
     * Sends the MessageCenter open engagement for the provided message id to Responsys. Call this whenever message is opened.
     * <br/><br/>This should be called when the message-detail view is visible to the user
     * @param {string} messageID engagement message id to be reported
     */

    static trackMessageCenterOpenEngagement(messageId) {
        RCTPushIOManager.trackMessageCenterOpenEngagement(messageId);
    }

    /**
     * Sends Message Center message engagement to Responsys.
     * <br/>This should be called when the message-list view is visible to the user.
     * @param {string} messageID engagement message id to be reported
     */

    static trackMessageCenterDisplayEngagement(messageId) {
        RCTPushIOManager.trackMessageCenterDisplayEngagement(messageId);
    }
    /**
     * Create a session when MessagaCenter/Inbox view will appear. 
     */

    static onMessageCenterViewDisplayed() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewDisplayed();
        } else {
            RCTPushIOManager.messageCenterViewWillAppear();
        }
    }
    /**
     * Sync all the MessageCenter display engagements to Responsys server
     */

    static onMessageCenterViewFinished() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewFinished();
        } else {
            RCTPushIOManager.messageCenterViewWillDisappear();
        }
    }

    /**
     * Declares a preference that will be used later with [set...Preference()]{@link setStringPreference}
     * 
     * @param {string} key Unique ID for this preference.
     * @param {string} label Human-Readable description of this preference.
     * @param {string} valueType Data type of this preference. Possible values: 'STRING', 'NUMBER', 'BOOLEAN'.
     * @param {callback} callback Success callback. 
     * 
     * @see {@link setBoolPreference}
     * @see {@link setNumberPreference}
     * @see {@link setStringPreference}
     */

    static declarePreference(key, label, valueType, callback) {
        RCTPushIOManager.declarePreference(key, label, valueType, callback);
    }

    /**
     * Saves the key/value along with the label provided earlier in [declarePreference]{@link declarePreference}
     * 
     * @param {string} key Unique ID for this preference.
     * @param {string} value Value of type String.
     * @param {callback} callback callback with boolean value TRUE if string preference value assigned, FALSE otherwise
     */

    static setStringPreference(key, value, callback) {
        RCTPushIOManager.setStringPreference(key, value, callback);
    }
    /**
     * Saves the key/value along with the label provided earlier in [declarePreference]{@link declarePreference}
     * 
     * @param {string} key Unique ID for this preference.
     * @param {number} value Value of type Number.
     * @param {callback} callback Success callback. 
     */

    static setNumberPreference(key, value, callback) {
        RCTPushIOManager.setNumberPreference(key, value, callback);
    }

    /**
     * Saves the key/value along with the label provided earlier in [declarePreference]{@link declarePreference}
     * 
     * @param {string} key Unique ID for this preference.
     * @param {boolean} value Value of type Boolean.
     * @param {callback} callback callback with preference if preference is found for the key, NULL otherwise.
     */

    static setBooleanPreference(key, value, callback) {
        RCTPushIOManager.setBooleanPreference(key, value, callback);
    }

    /**
     * Gets all preferences set earlier using [set...Preference()]{@link setStringPreference}.
     * @param {String} key  Unique ID of preference.
     * @param {callback} callback callback with preference if preference is found for the key, NULL otherwise.
     */

    static getPreference(key, callback) {
        RCTPushIOManager.getPreference(key, callback);
    }
    /**
     * Gets all preferences set earlier using [set...Preference()]{@link setStringPreference}.
     * @param {function} callback Success callback with {Preference[]} Array of [Preference]{@link Preference} in success callback.
     */

    static getPreferences(callback) {
        RCTPushIOManager.getPreferences(callback);
    }
    /**
     * Removes preference data for the given key.
     * 
     * @param {string} key Unique ID for this preference.
     */

    static removePreference(key) {
        RCTPushIOManager.removePreference(key);
    }

    /**
     * Removes all preference data.
     */

    static clearAllPreferences() {
        RCTPushIOManager.clearAllPreferences();
    }

    /**
    * Gets the API Key used by the device to register with Responsys.
    * @param {function} callback callback with configured APIKey. nil if no api-key configured.
    */

    static getAPIKey(callback) {
        RCTPushIOManager.getAPIKey(callback);
    }
    /**
    * Gets the Account Token used by the device to register with Responsys.
    * @param {callback} callback callback with configured AccountToken. nil if no AccountToken configured
    */

    static getAccountToken(callback) {
        RCTPushIOManager.getAccountToken(callback);
    }

    /**
    * Sets the advertising ID.
    * @param {string} advertisingId External Device Tracking ID. 
    */

    static setAdvertisingID(advertisingId) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setAdvertisingID(advertisingId);
        } else {
            RCTPushIOManager.setAdvertisingIdentifier(advertisingId);
        }
    }

    /**
     * Gets the advertising ID.
     * <br/> In iOS, its Advertising Identifier (IDFA) (@link setAdvertisingID}
     * @param {callback} callback Success callback with advertising Identifier. 
     */

    static getAdvertisingID(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getAdvertisingID(callback);
        } else {
            RCTPushIOManager.advertisingIdentifier(callback);
        }
    }

    /**
     * Gets the Responsys Device ID.
     * @param {callback} callback Success callback with device ID value. 
     */

    static getDeviceID(callback) {
        RCTPushIOManager.getDeviceID(callback);
    }

    /**
     * sets the Responsys web URL.
     * 
     * @param {string} executeRsysWebURL url
     */

    static setExecuteRsysWebUrl(executeRsysWebURL) {
        RCTPushIOManager.setExecuteRsysWebURL(executeRsysWebURL);
    }

    /**
     * Gets the Responsys web URL.
     * 
     * @param {callback} callback Success callback with device ID value. 
     */

    static getExecuteRsysWebUrl(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getExecuteRsysWebUrl(callback);
        } else {
            RCTPushIOManager.executeRsysWebURL(callback);
        }
    }

    /**
     * Removes push engagement related data for a session.
     * <br/>This will prevent further engagements from being reported until the app is opened again via a push notification. 
     */

    static resetEngagementContext() {
        RCTPushIOManager.resetEngagementContext();
    }

    /**
     * Gets the maximum age of engagement
     * @param {callback} callback callback with number value of server returned max age value (when application invoked from email).
     * <br/> Return -1 if no max age (from server) fetched. 
     */

    static getEngagementMaxAge(callback) {
        RCTPushIOManager.getEngagementMaxAge(callback);
    }

    /**
     * Gets the engagement date recorded when engagement information fetched from server and stored locally.
    * @param {callback} callback callback with string value of engagement date,time in ISO 8601 format
    */

    static getEngagementTimestamp(callback) {
        RCTPushIOManager.getEngagementTimestamp(callback);
    }
    /** 
     * Enable the fetch messages for all message center names from the server
     * @param {boolean} messageCenterEnabled boolean value to enable the messages fetch. 
     * <br/> True to enable ,False to disable.
     */

    static setMessageCenterEnabled(messageCenterEnabled) {
        RCTPushIOManager.setMessageCenterEnabled(messageCenterEnabled);
    }

    /**
     * Gets the status of MessageCenter enabled
     * @param {function} callback callback with boolean value. 
     */

    static isMessageCenterEnabled(callback) {
        RCTPushIOManager.isMessageCenterEnabled(callback);
    }

    /**
     * Fetch the list of Message Center messages for given MessageCenter name.
     * 
     * @param {string} messageCenter  Name of MessageCenter to fetch the list of messages
     * @param {callback} callback Success callback with messageCenter and messages, Failure callback with messageCenter and errorReason
     */

    static fetchMessagesForMessageCenter(messageCenter, callback) {
        RCTPushIOManager.fetchMessagesForMessageCenter(messageCenter, callback);
    }

    /**
     * Fetches rich content for the given message ID.
     * 
     * @param {string} messageID
     * @param {callback} callback Success callback with messageId and richContent, Failure callback with messageId and errorReason
     */

    static fetchRichContentForMessage(messageId, callback) {
        RCTPushIOManager.fetchRichContentForMessage(messageId, callback);
    }
    /**
     * Sets the badge count on app icon for the no. of Message Center messages.
     * 
     * @param {number} badgeCount
     * @param {boolean} forceSetBadge Force a server-sync for the newly set badge count.
     * @param {callback} callback callback. 
    */

    static setBadgeCount(badgeCount, forceSetBadge, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setBadgeCount(badgeCount, forceSetBadge, callback);
        } else {
            RCTPushIOManager.setBadgeCount(badgeCount, callback);
        }
    }

    /**
     * Gets the current badge count for Message Center messages.
     * 
     * @param {callback} callback callback as a number value. 
     */

    static getBadgeCount(callback) {
        RCTPushIOManager.getBadgeCount(callback);
    }

    /**
     * Resets the badge count for Message Center messages.<br/>This is equivalent to calling [setBadgeCount(0, true)]{@link PushIOManager#setsetBadgeCount}
     * 
     * @param {boolean} forceSetBadge Force a server-sync for the newly set badge count.
     * @param {callback} callback callback. 
     */

    static resetBadgeCount(forceSetBadge, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.resetBadgeCount(forceSetBadge, callback);
        } else {
            RCTPushIOManager.resetBadgeCount(callback);
        }
    }

    /**
     * Removes all Message Center messages from the SDK's cache.<br/><br/>This does not affect your local cache of the messages.
     */

    static resetMessageCenter() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.resetMessageCenter();
        } else {
            RCTPushIOManager.clearMessageCenterMessages();
        }
    }

    /**
    * Removes all In-App messages from the SDK's cache.
    * 
    */

    static clearInAppMessages() {
        RCTPushIOManager.clearInAppMessages();
    }
    /**
     * Enable/Disable the in-app messages pre-fetch. 
     * <br/>If enabled, all the in-app messages are pre-fetch and stored in the SDK, and triggered from local storage. 
     * <br/>If disabled then in-app messages are not pre-fetched, so not available to be triggered for the event i.e.: $ExplicitAppOpen.
     * @param {Boolean} isEnabled 
     */

    static setInAppFetchEnabled(isEnabled) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setInAppFetchEnabled(isEnabled);
        } else {
            RCTPushIOManager.setInAppMessageFetchEnabled(isEnabled);
        }
    }

    /**
     * Enable the crash logging of PushIO sdk. 
     * <br/>It will not make and capture any crashes of apps. By default it is enable. You can set `NO` 
     * <br/>if you do not want PushIO sdk to collect crashes.
     * @param {Boolean} isEnabled boolean value to enable the crash logging.
     */
    static setCrashLoggingEnabled(isEnabled) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setCrashLoggingEnabled(isEnabled);
        } else {
            console.log("API not supported");
        }
    }
    /**
     * Check if crash logging is enabled for PushIOManager SDK. We capture only crashes related to sdk.
     * @param {callback} callback with boolean value
     */

    static isCrashLoggingEnabled(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.isCrashLoggingEnabled(callback);
        } else {
            console.log("API not supported");
        }
    }

    /**
     * Sets the device token
     * <br/> available in Android only
     * @param {string} deviceToken 
     */

    static setDeviceToken(deviceToken) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setDeviceToken(deviceToken);
        } else {
            console.log("API not supported. Please use `didRegisterForRemoteNotificationsWithDeviceToken`");
        }
    }
    /**
     * Checks if the push payload provided is sent by Responsys platform or not.
     * @param {object} message 
     * @param {callback} callback 
     */

    static isResponsysPush(message, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.isResponsysPush(message, callback);
        } else {
            RCTPushIOManager.isResponsysPayload(message, callback);
        }
    }

    /**
     * Informs the SDK that the user has entered a geo-fence.
     * @param {GeoRegion} region
     * @param {callback} callback callback with regionID and regionType.
     */

    static onGeoRegionEntered(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onGeoRegionEntered(region, callback);
        } else {
            RCTPushIOManager.didEnterGeoRegion(region, callback);
        }
    }

    /**
     * Informs the SDK that the user has exited a geo-fence.
     * 
     * @param {GeoRegion} region
     * @param {callback} callback callback with regionID and regionType.
     */

    static onGeoRegionExited(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onGeoRegionExited(region, callback);
        } else {
            RCTPushIOManager.didExitGeoRegion(region, callback);
        }
    }

    /**
     * Informs the SDK that the user has entered a beacon region.
     * 
     * @param {BeaconRegion} region
     * @param {callback} callback callback with regionID and regionType.
     */

    static onBeaconRegionEntered(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onBeaconRegionEntered(region, callback);
        } else {
            RCTPushIOManager.didEnterBeaconRegion(region, callback);
        }
    }

    /**
     * Informs the SDK that the user has exited a beacon region.
     * 
     * @param {BeaconRegion} region
     * @param {callback} callback callback with regionID and regionType.
     */

    static onBeaconRegionExited(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onBeaconRegionExited(region, callback);
        } else {
            RCTPushIOManager.didExitBeaconRegion(region, callback);

        }
    }

    /**
     * Sends Message Center message engagement to Responsys.
     * <br/>This should be called when the message-list view is visible to the user.
     * @param {string} messageID
     */

    static trackMessageCenterDisplayEngagement(messageID) {
        RCTPushIOManager.trackMessageCenterDisplayEngagement(messageID);
    }

    /**
     * Sends Message Center message engagement to Responsys.
     * <br/>This should be called when the message-detail view is visible to the user.
     * @param {string} messageID
     */

    static trackMessageCenterOpenEngagement(messageID) {
        RCTPushIOManager.trackMessageCenterOpenEngagement(messageID);
    }
    /**
     * Create a session when Message Center/Inbox view will appear.
     */

    static messageCenterViewDidMount() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewVisible();
        } else {
            RCTPushIOManager.messageCenterViewWillAppear();
        }
    }
    /**
     * Sync all the MessageCenter display engagements to responsys server.
     */

    static messageCenterViewWillUnmount() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewFinish();
        } else {
            RCTPushIOManager.messageCenterViewWillDisappear();
        }
    }

    // Android-Only APIs
    /**
     * Sets Enable/Disable the Message Center badging
     * @param {Boolean} isBadgingEnabled 
     */
    static setMessageCenterBadgingEnabled(isBadgingEnabled) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setMessageCenterBadgingEnabled(isBadgingEnabled);
        } else {
            console.log("API not supported");
        }
    }
    /**
     * 
     * @param {*} areNotificationsStacked 
     */

    static setNotificationsStacked(areNotificationsStacked) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setNotificationsStacked(areNotificationsStacked);
        } else {
            console.log("API not supported");
        }
    }

    static getNotificationStacked(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getNotificationStacked(callback);
        } else {
            console.log("API not supported");
        }
    }

    static setDefaultSmallIcon(resourceId) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setDefaultSmallIcon(resourceId);
        } else {
            console.log("API not supported");
        }
    }

    static setDefaultLargeIcon(resourceId) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setDefaultLargeIcon(resourceId);
        } else {
            console.log("API not supported");
        }
    }

    /**
     * Request the SDK to process and display notification using the provided {@link com.google.firebase.messaging.RemoteMessage}
     *
     * @param message
     */

    static handleMessage(message) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.handleMessage(message);
        } else {
            console.log("API not supported");
        }
    }
    /**
     * Removes all app-defined Interactive Notification categories from the SDK's cache.
     */

    static clearInteractiveNotificationCategories() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.clearInteractiveNotificationCategories();
        } else {
            console.log("API not supported");
        }
    }
    /**
     * Adds a new app-defined Interactive Notification category.
     * @param {InteractiveNotificationCategory} notificationCategory 
     * @param {callback} callback 
     */

    static addInteractiveNotificationCategory(notificationCategory, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.addInteractiveNotificationCategory(notificationCategory, callback);
        } else {
            console.log("API not supported");
        }
    }
    /**
     * Removes app-defined Interactive Notification category.
     * @param {string} categoryId 
     */

    static deleteInteractiveNotificationCategory(categoryId) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.deleteInteractiveNotificationCategory(categoryId);
        } else {
            console.log("API not supported");
        }
    }
    /**
     * Gets a single Interactive Notification category for the given category ID.
     * @param {string} categoryId 
     * @param {callback} callback 
     */

    static getInteractiveNotificationCategory(categoryId, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getInteractiveNotificationCategory(categoryId, callback);
        } else {
            console.log("API not supported");
        }
    }



    // iOS-Only APIs
    /**
     * 
     */
    static registerForRemoteNotifications() {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.registerForRemoteNotifications();
        } else {
            console.log("API not supported");
        }
    }

    /**
     * registerForAllRemoteNotificationTypes Listener is added
     * @param {callback} callback 
     */

    static registerForAllRemoteNotificationTypes(callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOEventEmitter.addListener('registerForAllRemoteNotificationTypes', result => {
                if (result.error) {
                    callback(result.error, result.response)
                } else {
                    callback(null, result.response)
                }
            });

            RCTPushIOManager.registerForAllRemoteNotificationTypes();
        } else {
            console.log("API not supported");
        }
    }

    /**
    * Asks user permissions for all push notifications types. i.e.: Sound/Badge/Alert types.
    * If readyForRegistrationCompHandler is not set, then provided completionHandler is assigned to it, to let application have access when SDK receives deviceToken.
    * Only available on iOS platform.
     * @param {int} authOptions Notification auth types i.e.: Sound/Badge/Alert.
     * @param {InteractiveNotificationCategory[]} categories categories Contains the notification categories definitions.
     * @param {*} callback 
     */

    static registerForNotificationAuthorizations(authOptions, categories, callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.registerForNotificationAuthorizations(authOptions, categories, callback);
        } else {
            console.log("API not supported");
        }
    }

    /**
     * Asks user permissions for all push notifications types. i.e.: Sound/Badge/Alert types. You can pass the notification categories definitions to register. 
     * 
     * Only available on iOS platform.
     *
     * @param {InteractiveNotificationCategory[]} categories Contains the notification categories definitions.
     * @param {callback} callback  callback.
     */

    static registerForAllRemoteNotificationTypesWithCategories(categories, callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.registerForAllRemoteNotificationTypesWithCategories(categories, callback);
        } else {
            console.log("API not supported");
        }
    }

    /**
     * Check if log printing is enabled in debug console.
     * @param {boolean} isLoggingEnabled
     * @param {callback} callback with boolean value
     */

    static isLoggingEnabled(callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.isLoggingEnabled(callback);
        } else {
            console.log("API not supported");
        }
    }
    /**
     * Checks if the push notification response provided is sent by Responsys platform or not.
     * @param {*} response 
     * @param {callback} callback with boolean value.TRUE if push payload is sent by Responsys platform otherwise FALSE.
     */

    static isResponsysNotificationResponse(response, callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.isResponsysNotificationResponse(response, callback);
        } else {
            console.log("API not supported");
        }
    }

    static setConfigType(configType) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.setConfigType(configType);
        } else {
            console.log("API not supported");
        }
    }

    static configType(callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.configType(callback);
        } else {
            console.log("API not supported");
        }
    }

    /**
     Reset all SDK data. i.e.: DeviceID, UserId, Preferences.
    */

    static resetAllData() {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.resetAllData();
        } else {
            console.log("API not supported");
        }
    }

    static trackEmailConversion(uri, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.trackEmailConversion(uri, callback);
        } else {
            console.log("API not supported");
        }
    }

    static addInAppMessageCTALinkListener(callback) {
        return RCTPushIOEventEmitter.addListener('PIORsysWebURLResolvedNotification', callback);
    }
    /**
     * To Add listener for deelink 
     * @param {Boolean} isSet 
     * @param {callback} callback 
     */

    static setOpenURLListener(isSet, callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.setOpenURLListener(isSet);
        }

        if (isSet) {
            return RCTPushIOEventEmitter.addListener('PIOHandleOpenURL', callback);
        } else {
            return RCTPushIOEventEmitter.removeListener('PIOHandleOpenURL', callback);
        }
    }

    static setDelayRichPushDisplay(delayRichPush) {
        RCTPushIOManager.setDelayRichPushDisplay(delayRichPush);
    }
    /**
     * Set the delay to display  Rich Push. If this flag is set  Rich Push will be not displayed immediately and to display it call `showRichPushMessage` API.
     */

    static showRichPushMessage() {
        RCTPushIOManager.showRichPushMessage();
    }

    /**
     * Gets the status ofthe delay Rich Push flag
     * @param {callback} callback with boolean value
     */
    static isRichPushDelaySet(callback) {
        RCTPushIOManager.isRichPushDelaySet(callback);
    }
    /**
     * Tracks the conversions for PUSHIO_ENGAGEMENT_METRIC_INAPP_PURCHASE and PUSHIO_ENGAGEMENT_METRIC_PURCHASE events.
     * @param {ConversionEvent} event 
     * @param {callback} callback 
     */

    static trackConversionEvent(event, callback) {
        if (Platform.OS === 'ios') {
            event["conversionType"] = ((event["conversionType"] < 6) ? (event["conversionType"] - 1) : event["conversionType"]);
        }
        RCTPushIOManager.trackConversionEvent(event, callback);
    }


    /**
     *  Sets delay in registration. Registration will be delayed while launching the app/from coming to background
     * @param {Boolean} delayRegistration 
     * 
     */

    static setDelayRegistration(delayRegistration) {
        if (Platform.OS === 'android') {
            console.log("API not supported");
        } else {
            RCTPushIOManager.setDelayRegistration(delayRegistration);
        }
    }
    /**
     * This api provides the status, if `setDelayRegistration` is enabled of not. 
     * @param {callback} callback with boolean value. true if registration will be delayed otherwise false
     *
     */

    static isDelayRegistration(callback) {
        if (Platform.OS === 'android') {
            console.log("API not supported");
        } else {
            RCTPushIOManager.isDelayRegistration(callback);
        }
    }

    static setNotificationSmallIconColor(color) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setNotificationSmallIconColor(color);
        } else {
            console.log("API not supported");
        }
    }

    static setInAppMessageBannerHeight(height, callback) {
        if (typeof height != 'number') {
            console.log("Banner height should be a Number type");
            return;
        }
        RCTPushIOManager.setInAppMessageBannerHeight(height, callback);
    }

    static getInAppMessageBannerHeight(callback) {
        RCTPushIOManager.getInAppMessageBannerHeight(callback);
    }

    static setStatusBarHiddenForIAMBannerInterstitial(hideStatusBar) {
        RCTPushIOManager.setStatusBarHiddenForIAMBannerInterstitial(hideStatusBar)
    }

    static isStatusBarHiddenForIAMBannerInterstitial(callback) {
        RCTPushIOManager.isStatusBarHiddenForIAMBannerInterstitial(callback);
    }

    static addMessageCenterUpdateListener(callback) {
        RCTPushIOEventEmitter.addListener('PIOMessageCenterUpdateNotification', callback);
    }

    static setNotificationSmallIcon(resourceName) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setNotificationSmallIcon(resourceName);
        } else {
            console.log("API not supported");
        }
    }

    static setNotificationLargeIcon(resourceName) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setNotificationLargeIcon(resourceName);
        } else {
            console.log("API not supported");
        }
    }

    static onPushTokenReceived(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.addNativeEventListener('pio_new_token');
            RCTPushIOEventEmitter.addListener('pio_new_token', callback);
        } else {
            console.log("API not supported");
        }
    }

    static onPushNotificationReceived(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.addNativeEventListener('pio_new_push_message');
            RCTPushIOEventEmitter.addListener('pio_new_push_message', callback);
        } else {
            console.log("API not supported");
        }
    }

    /**
     * customise in-app view close button with title, background color, title color and image.
     *
     */

    static setInAppCustomCloseButton(closebutton) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.setInAppCustomCloseButton(closebutton)
        }  else {
            console.log("API not supported");
        }
    }

}
