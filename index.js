/*
* Copyright © 2020, Oracle and/or its affiliates. All rights reserved.
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

    static setLogLevel(loggingLevel) {
        RCTPushIOManager.setLogLevel(loggingLevel);
    }

    static getLibVersion(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getLibVersion(callback);
        } else {
            RCTPushIOManager.frameworkVersion(callback);
        }
    }

    static setLoggingEnabled(isEnabled) {
        RCTPushIOManager.setLoggingEnabled(isEnabled);
    }

    static registerApp(useLocation, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.registerApp(useLocation, callback);
        } else {
            RCTPushIOManager.registerApp(callback);
        }
    }

    static unregisterApp(callback) {
        RCTPushIOManager.unregisterApp(callback);
    }

    static configure(fileName, callback) {
        RCTPushIOManager.configure(fileName, callback);
    }

    static overwriteApiKey(apiKey) {
        RCTPushIOManager.overwriteApiKey(apiKey);
    }

    static overwriteAccountToken(accountToken) {
        RCTPushIOManager.overwriteAccountToken(accountToken);
    }

    static trackEvent(eventName, properties) {
        RCTPushIOManager.trackEvent(eventName, properties);
    }

    static setExternalDeviceTrackingID(externalDeviceTrackingID) {
        RCTPushIOManager.setExternalDeviceTrackingID(externalDeviceTrackingID);
    }

    static getExternalDeviceTrackingID(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getExternalDeviceTrackingID(callback);
        } else {
            RCTPushIOManager.externalDeviceTrackingID(callback);
        }
    }

    static registerUserId(userId) {
        RCTPushIOManager.registerUserId(userId);
    }

    static getRegisteredUserId(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getRegisteredUserId(callback);
        } else {
            RCTPushIOManager.getUserId(callback);
        }
    }

    static unregisterUserId() {
        RCTPushIOManager.unregisterUserId();
    }

    static trackEngagement(metric, properties, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.trackEngagement(metric, properties, callback);
        } else {
            var value = ((metric < 6) ? (metric - 1) : metric);
            RCTPushIOManager.trackEngagement(value, properties, callback);
        }
    }

    static trackMessageCenterOpenEngagement(messageId) {
        RCTPushIOManager.trackMessageCenterOpenEngagement(messageId);
    }

    static trackMessageCenterDisplayEngagement(messageId) {
        RCTPushIOManager.trackMessageCenterDisplayEngagement(messageId);
    }

    static onMessageCenterViewDisplayed() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewDisplayed();
        } else {
            RCTPushIOManager.messageCenterViewWillAppear();
        }
    }

    static onMessageCenterViewFinished() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewFinished();
        } else {
            RCTPushIOManager.messageCenterViewWillDisappear();
        }
    }

    static declarePreference(key, label, valueType, callback) {
        RCTPushIOManager.declarePreference(key, label, valueType, callback);
    }

    static setStringPreference(key, value, callback) {
        RCTPushIOManager.setStringPreference(key, value, callback);
    }

    static setNumberPreference(key, value, callback) {
        RCTPushIOManager.setNumberPreference(key, value, callback);
    }

    static setBooleanPreference(key, value, callback) {
        RCTPushIOManager.setBooleanPreference(key, value, callback);
    }

    static getPreference(key, callback) {
        RCTPushIOManager.getPreference(key, callback);
    }

    static getPreferences(callback) {
        RCTPushIOManager.getPreferences(callback);
    }

    static removePreference(key) {
        RCTPushIOManager.removePreference(key);
    }

    static clearAllPreferences() {
        RCTPushIOManager.clearAllPreferences();
    }

    static getAPIKey(callback) {
        RCTPushIOManager.getAPIKey(callback);
    }

    static getAccountToken(callback) {
        RCTPushIOManager.getAccountToken(callback);
    }

    static setAdvertisingID(advertisingId) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setAdvertisingID(advertisingId);
        } else {
            RCTPushIOManager.setAdvertisingIdentifier(advertisingId);
        }
    }

    static getAdvertisingID(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getAdvertisingID(callback);
        } else {
            RCTPushIOManager.advertisingIdentifier(callback);
        }
    }

    static getDeviceID(callback) {
        RCTPushIOManager.getDeviceID(callback);
    }

    static setExecuteRsysWebUrl(executeRsysWebURL) {
        RCTPushIOManager.setExecuteRsysWebURL(executeRsysWebURL);
    }

    static getExecuteRsysWebUrl(callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getExecuteRsysWebUrl(callback);
        } else {
            RCTPushIOManager.executeRsysWebURL(callback);
        }
    }

    static resetEngagementContext() {
        RCTPushIOManager.resetEngagementContext();
    }

    static getEngagementMaxAge(callback) {
        RCTPushIOManager.getEngagementMaxAge(callback);
    }

    static getEngagementTimestamp(callback) {
        RCTPushIOManager.getEngagementTimestamp(callback);
    }

    static setMessageCenterEnabled(messageCenterEnabled) {
        RCTPushIOManager.setMessageCenterEnabled(messageCenterEnabled);
    }

    static isMessageCenterEnabled(callback) {
        RCTPushIOManager.isMessageCenterEnabled(callback);
    }

    static fetchMessagesForMessageCenter(messageCenter, callback) {
        RCTPushIOManager.fetchMessagesForMessageCenter(messageCenter, callback);
    }

    static fetchRichContentForMessage(messageId, callback) {
        RCTPushIOManager.fetchRichContentForMessage(messageId, callback);
    }

    static setBadgeCount(badgeCount, forceSetBadge, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setBadgeCount(badgeCount, forceSetBadge, callback);
        } else {
            RCTPushIOManager.setBadgeCount(badgeCount, callback);
        }
    }

    static getBadgeCount(callback) {
        RCTPushIOManager.getBadgeCount(callback);
    }

    static resetBadgeCount(forceSetBadge, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.resetBadgeCount(forceSetBadge, callback);
        } else {
            RCTPushIOManager.resetBadgeCount(callback);
        }
    }

    static resetMessageCenter() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.resetMessageCenter();
        } else {
            RCTPushIOManager.clearMessageCenterMessages();
        }
    }

    static clearInAppMessages() {
        RCTPushIOManager.clearInAppMessages();
    }

    static setInAppFetchEnabled(isEnabled) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setInAppFetchEnabled(isEnabled);
        } else {
            RCTPushIOManager.setInAppMessageFetchEnabled(isEnabled);
        }
    }

    static setCrashLoggingEnabled(isEnabled) {
        RCTPushIOManager.setCrashLoggingEnabled(isEnabled);
    }

    static isCrashLoggingEnabled(callback) {
        RCTPushIOManager.isCrashLoggingEnabled(callback);
    }

    static setDeviceToken(deviceToken) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setDeviceToken(deviceToken);
        } else {
            console.log("API not supported. Please use `didRegisterForRemoteNotificationsWithDeviceToken`");
        }
    }

    static isResponsysPush(message, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.isResponsysPush(message, callback);
        } else {
            RCTPushIOManager.isResponsysPayload(message, callback);
        }
    }

    static onGeoRegionEntered(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onGeoRegionEntered(region, callback);
        } else {
            RCTPushIOManager.didEnterGeoRegion(region, callback);
        }
    }

    static onGeoRegionExited(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onGeoRegionExited(region, callback);
        } else {
            RCTPushIOManager.didExitGeoRegion(region, callback);
        }
    }

    static onBeaconRegionEntered(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onBeaconRegionEntered(region, callback);
        } else {
            RCTPushIOManager.didEnterBeaconRegion(region, callback);
        }
    }

    static onBeaconRegionExited(region, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onBeaconRegionExited(region, callback);
        } else {
            RCTPushIOManager.didExitBeaconRegion(region, callback);

        }
    }

    static trackMessageCenterDisplayEngagement(messageID) {
        RCTPushIOManager.trackMessageCenterDisplayEngagement(messageID);
    }

    static trackMessageCenterOpenEngagement(messageID) {
        RCTPushIOManager.trackMessageCenterOpenEngagement(messageID);
    }

    static messageCenterViewDidMount() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewVisible();
        } else {
            RCTPushIOManager.messageCenterViewWillAppear();
        }
    }

    static messageCenterViewWillUnmount() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.onMessageCenterViewFinish();
        } else {
            RCTPushIOManager.messageCenterViewWillDisappear();
        }
    }

    // Android-Only APIs
    static setMessageCenterBadgingEnabled(isBadgingEnabled) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.setMessageCenterBadgingEnabled(isBadgingEnabled);
        } else {
            console.log("API not supported");
        }
    }

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

    static handleMessage(message) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.handleMessage(message);
        } else {
            console.log("API not supported");
        }
    }

    static clearInteractiveNotificationCategories() {
        if (Platform.OS === 'android') {
            RCTPushIOManager.clearInteractiveNotificationCategories();
        } else {
            console.log("API not supported");
        }
    }

    static addInteractiveNotificationCategory(notificationCategory, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.addInteractiveNotificationCategory(notificationCategory, callback);
        } else {
            console.log("API not supported");
        }
    }

    static deleteInteractiveNotificationCategory(categoryId) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.deleteInteractiveNotificationCategory(categoryId);
        } else {
            console.log("API not supported");
        }
    }

    static getInteractiveNotificationCategory(categoryId, callback) {
        if (Platform.OS === 'android') {
            RCTPushIOManager.getInteractiveNotificationCategory(categoryId, callback);
        } else {
            console.log("API not supported");
        }
    }



    // iOS-Only APIs
    static registerForRemoteNotifications() {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.registerForRemoteNotifications();
        } else {
            console.log("API not supported");
        }
    }

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

    static registerForNotificationAuthorizations(authOptions, categories, callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.registerForNotificationAuthorizations(authOptions, categories, callback);
        } else {
            console.log("API not supported");
        }
    }

    static registerForAllRemoteNotificationTypesWithCategories(categories, callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.registerForAllRemoteNotificationTypesWithCategories(categories, callback);
        } else {
            console.log("API not supported");
        }
    }

    static isLoggingEnabled(callback) {
        if (Platform.OS === 'ios') {
            RCTPushIOManager.isLoggingEnabled(callback);
        } else {
            console.log("API not supported");
        }
    }

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

    static showRichPushMessage() {
        RCTPushIOManager.showRichPushMessage();
    }

    static isRichPushDelaySet(callback) {
        RCTPushIOManager.isRichPushDelaySet(callback);
    }

    static trackConversionEvent(event, callback) {
        if (Platform.OS === 'ios') {
            event["conversionType"] = ((event["conversionType"] < 6) ? (event["conversionType"] - 1) : event["conversionType"]);
        }
        RCTPushIOManager.trackConversionEvent(event, callback);
    }
}
