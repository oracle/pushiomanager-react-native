/*
* Copyright © 2022, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/

package com.pushio.manager.rn;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.pushio.manager.PIOBadgeSyncListener;
import com.pushio.manager.PIOConfigurationListener;
import com.pushio.manager.PIOConversionEvent;
import com.pushio.manager.PIOConversionListener;
import com.pushio.manager.PIOInteractiveNotificationCategory;
import com.pushio.manager.PIOLogger;
import com.pushio.manager.PIOMCMessage;
import com.pushio.manager.PIOMCMessageError;
import com.pushio.manager.PIOMCMessageListener;
import com.pushio.manager.PIOMCRichContentListener;
import com.pushio.manager.PIORegionCompletionListener;
import com.pushio.manager.PIORegionEventType;
import com.pushio.manager.PIORegionException;
import com.pushio.manager.PIORsysIAMHyperlinkListener;
import com.pushio.manager.PushIOManager;
import com.pushio.manager.exception.PIOMCMessageException;
import com.pushio.manager.exception.PIOMCRichContentException;
import com.pushio.manager.exception.ValidationException;
import com.pushio.manager.preferences.PushIOPreference;
import com.pushio.manager.tasks.PushIOEngagementListener;
import com.pushio.manager.tasks.PushIOListener;
import com.pushio.manager.PIODeepLinkListener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Map;

import android.graphics.Color;

public class RCTPushIOManager extends ReactContextBaseJavaModule {
    private static final String LOG_TAG = "pushio-rn";
    private final PushIOManager mPushIOManager;

    public RCTPushIOManager(ReactApplicationContext reactContext) {
        super(reactContext);
        mPushIOManager = com.pushio.manager.PushIOManager.getInstance(reactContext.getApplicationContext());
    }

    @Override
    public String getName() {
        return "PushIOManager";
    }

    @ReactMethod
    public void getLibVersion(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, PushIOManager.getLibVersion());
        } else {
            PIOLogger.v("RN gLV callback is null");
        }
    }

    @ReactMethod
    public void setLoggingEnabled(boolean isEnabled) {
        PushIOManager.setLoggingEnabled(isEnabled);
    }

    @ReactMethod
    public void setLogLevel(int logLevel) {
        PushIOManager.setLogLevel(logLevel);
    }

    @ReactMethod
    public void registerApp(boolean useLocation, final Callback callback) {
        mPushIOManager.registerPushIOListener(new PushIOListener() {
            @Override
            public void onPushIOSuccess() {
                if (callback != null) {
                    callback.invoke(null, "success");
                }
            }

            @Override
            public void onPushIOError(String errorReason) {
                if (callback != null) {
                    callback.invoke(errorReason, null);
                }
            }
        });
        mPushIOManager.registerApp(useLocation);
    }

    @ReactMethod
    public void unregisterApp(final Callback callback) {
        mPushIOManager.unregisterApp();
    }

    @ReactMethod
    public void configure(String configFileName, final Callback callback) {
        mPushIOManager.configure(configFileName, new PIOConfigurationListener() {
            @Override
            public void onSDKConfigured(Exception e) {
                if (callback != null) {
                    if (e == null) {
                        callback.invoke(null, "success");
                    } else {
                        callback.invoke(e.getMessage(), null);
                    }
                }
            }
        });
    }

    @ReactMethod
    public void overwriteApiKey(String apiKey) {
        mPushIOManager.overwriteApiKey(apiKey);
    }

    @ReactMethod
    public void overwriteAccountToken(String accountToken) {
        mPushIOManager.overwriteAccountToken(accountToken);
    }

    @ReactMethod
    public void trackEvent(String eventName, ReadableMap properties) {
        if (properties != null) {
            mPushIOManager.trackEvent(eventName, properties.toHashMap());
        } else {
            mPushIOManager.trackEvent(eventName);
        }
    }

    @ReactMethod
    public void setExternalDeviceTrackingID(String edti) {
        mPushIOManager.setExternalDeviceTrackingID(edti);
    }

    @ReactMethod
    public void getExternalDeviceTrackingID(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getExternalDeviceTrackingID());
        } else {
            PIOLogger.v("RN gEDTI callback is null");
        }
    }

    @ReactMethod
    public void registerUserId(String userId) {
        mPushIOManager.registerUserId(userId);
    }

    @ReactMethod
    public void unregisterUserId() {
        mPushIOManager.unregisterUserId();
    }

    @ReactMethod
    public void getRegisteredUserId(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getRegisteredUserId());
        } else {
            PIOLogger.v("RN gRUI callback is null");
        }
    }

    @ReactMethod
    public void trackEngagement(int metric, ReadableMap properties, final Callback callback) {
        Map<String, String> props = null;
        if (properties != null) {
            props = RCTPIOCommonUtils.convertMap(properties.toHashMap());
        }

        mPushIOManager.trackEngagement(metric, props, new PushIOEngagementListener() {
            @Override
            public void onEngagementSuccess() {
                if (callback != null) {
                    callback.invoke(null, "success");
                }
            }

            @Override
            public void onEngagementError(String reason) {
                if (callback != null) {
                    callback.invoke(reason, null);
                }
            }
        });
    }

    @ReactMethod
    public void declarePreference(String key, String label, String type, final Callback callback) {
        try {
            mPushIOManager.declarePreference(key, label, PushIOPreference.Type.valueOf(type));
            if (callback != null) {
                callback.invoke(null, "success");
            }
        } catch (ValidationException e) {
            PIOLogger.v("RN dP " + e.getMessage());
            if (callback != null) {
                callback.invoke(e.getMessage(), null);
            }
        }
    }

    @ReactMethod
    public void setStringPreference(String key, String value, final Callback callback) {
        try {
            boolean result = mPushIOManager.setPreference(key, value);

            if (callback != null) {
                callback.invoke(null, result);
            }

        } catch (ValidationException e) {
            PIOLogger.v("RN sP " + e.getMessage());
            if (callback != null) {
                callback.invoke(e.getMessage(), null);
            }
        }
    }

    @ReactMethod
    public void setNumberPreference(String key, Double value, final Callback callback) {
        try {
            boolean result = mPushIOManager.setPreference(key, value);

            if (callback != null) {
                callback.invoke(null, result);
            }

        } catch (ValidationException e) {
            PIOLogger.v("RN sP " + e.getMessage());
            if (callback != null) {
                callback.invoke(e.getMessage(), null);
            }
        }
    }

    @ReactMethod
    public void setBooleanPreference(String key, Boolean value, final Callback callback) {
        try {
            boolean result = mPushIOManager.setPreference(key, value);

            if (callback != null) {
                callback.invoke(null, result);
            }

        } catch (ValidationException e) {
            PIOLogger.v("RN sP " + e.getMessage());
            if (callback != null) {
                callback.invoke(e.getMessage(), null);
            }
        }
    }

    @ReactMethod
    public void getPreference(String key, final Callback callback) {

        PushIOPreference preference = mPushIOManager.getPreference(key);

        if (callback != null) {
            if (preference != null) {
                WritableMap writableMap = new WritableNativeMap();
                writableMap.putString("key", preference.getKey());
                writableMap.putString("label", preference.getLabel());

                final PushIOPreference.Type type = preference.getType();
                final Object value = preference.getValue();

                if (type == PushIOPreference.Type.STRING) {
                    writableMap.putString("value", String.valueOf(value));

                } else if (type == PushIOPreference.Type.NUMBER) {

                    if (value instanceof Double) {
                        writableMap.putDouble("value", (Double) value);
                    } else if (value instanceof Integer) {
                        writableMap.putInt("value", (Integer) value);
                    }

                } else if (type == PushIOPreference.Type.BOOLEAN) {
                    writableMap.putBoolean("value", (Boolean) value);
                }

                writableMap.putString("type", type.toString());
                callback.invoke(null, writableMap);
            } else {
                callback.invoke(null, null);
            }
        }
    }

    @ReactMethod
    public void getPreferences(final Callback callback) {
        List<PushIOPreference> preferences = mPushIOManager.getPreferences();

        if (callback != null) {
            if (preferences != null) {
                WritableArray writableArray = new WritableNativeArray();

                for (PushIOPreference preference : preferences) {
                    WritableMap writableMap = new WritableNativeMap();
                    writableMap.putString("key", preference.getKey());
                    writableMap.putString("label", preference.getLabel());

                    final PushIOPreference.Type type = preference.getType();
                    final Object value = preference.getValue();

                    if (type == PushIOPreference.Type.STRING) {
                        writableMap.putString("value", String.valueOf(value));

                    } else if (type == PushIOPreference.Type.NUMBER) {

                        if (value instanceof Double) {
                            writableMap.putDouble("value", (Double) value);
                        } else if (value instanceof Integer) {
                            writableMap.putInt("value", (Integer) value);
                        }

                    } else if (type == PushIOPreference.Type.BOOLEAN) {
                        writableMap.putBoolean("value", (Boolean) value);
                    }

                    writableMap.putString("type", type.toString());
                    writableArray.pushMap(writableMap);
                }
                callback.invoke(null, writableArray);
            } else {
                callback.invoke(null, null);
            }
        }
    }

    @ReactMethod
    public void removePreference(String key) {
        mPushIOManager.removePreference(key);
    }

    @ReactMethod
    public void clearAllPreferences() {
        mPushIOManager.clearAllPreferences();
    }

    @ReactMethod
    public void getAPIKey(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getAPIKey());
        } else {
            PIOLogger.v("RN gAK callback is null");
        }
    }

    @ReactMethod
    public void getAccountToken(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getAccountToken());
        } else {
            PIOLogger.v("RN gAT callback is null");
        }
    }

    @ReactMethod
    public void getAdvertisingID(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getAdvertisingID());
        } else {
            PIOLogger.v("RN gAD callback is null");
        }
    }

    @ReactMethod
    public void setAdvertisingID(String adid) {
        mPushIOManager.setAdvertisingID(adid);
    }

    @ReactMethod
    public void getDeviceID(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getDeviceId());
        } else {
            PIOLogger.v("RN gDI callback is null");
        }
    }

    @ReactMethod
    public void setExecuteRsysWebURL(boolean executeRsysWebUrl) {
        mPushIOManager.setExecuteRsysWebUrl(executeRsysWebUrl, new PIORsysIAMHyperlinkListener() {
            @Override
            public void onSuccess(String requestUrl, String deeplinkUrl, String weblinkUrl) {

                WritableMap map = new WritableNativeMap();
                map.putString("requestUrl", requestUrl);
                map.putString("deeplinkUrl", deeplinkUrl);
                map.putString("weblinkUrl", weblinkUrl);

                emitEvent("PIORsysWebURLResolvedNotification", map);
            }

            @Override
            public void onFailure(String requestUrl, String errorReason) {
                WritableMap map = new WritableNativeMap();
                map.putString("requestUrl", requestUrl);
                map.putString("error", errorReason);

                emitEvent("PIORsysWebURLResolvedNotification", map);
            }
        });
    }

    @ReactMethod
    public void getExecuteRsysWebUrl(final Callback callback) {
        boolean result = mPushIOManager.getExecuteRsysWebUrl();

        if (callback != null) {
            callback.invoke(null, result);
        }
    }

    @ReactMethod
    public void getConversionUrl(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getConversionUrl());
        } else {
            PIOLogger.v("RN gCU callback is null");
        }
    }

    @ReactMethod
    public void getRIAppId(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getRIAppId());
        } else {
            PIOLogger.v("RN gRAI callback is null");
        }
    }

    @ReactMethod
    public void setDefaultSmallIcon(int resId) {
        mPushIOManager.setDefaultSmallIcon(resId);
    }

    @ReactMethod
    public void setDefaultLargeIcon(int resId) {
        mPushIOManager.setDefaultLargeIcon(resId);
    }

    @ReactMethod
    public void setNotificationsStacked(boolean notificationsStacked) {
        mPushIOManager.setNotificationsStacked(notificationsStacked);
    }

    @ReactMethod
    public void getNotificationStacked(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getNotificationStacked());
        } else {
            PIOLogger.v("RN gNS callback is null");
        }
    }

    @ReactMethod
    public void getEngagementTimestamp(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getEngagementTimestamp());
        } else {
            PIOLogger.v("RN gET callback is null");
        }
    }

    @ReactMethod
    public void getEngagementMaxAge(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, Double.longBitsToDouble(mPushIOManager.getEngagementMaxAge()));
        } else {
            PIOLogger.v("RN gEMA callback is null");
        }
    }

    @ReactMethod
    public void resetEngagementContext() {
        mPushIOManager.resetEngagementContext();
    }

    @ReactMethod
    public void isMessageCenterEnabled(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.isMessageCenterEnabled());
        } else {
            PIOLogger.v("RN iMCE callback is null");
        }
    }

    @ReactMethod
    public void setMessageCenterEnabled(boolean messageCenterEnabled) {
        mPushIOManager.setMessageCenterEnabled(messageCenterEnabled);
    }

    @ReactMethod
    public void fetchMessagesForMessageCenter(String messageCenter, final Callback callback) {
        try {
            mPushIOManager.fetchMessagesForMessageCenter(messageCenter, new PIOMCMessageListener() {
                @Override
                public void onSuccess(String messageCenter, List<PIOMCMessage> messages) {
                    if (callback != null) {
                        WritableMap writableMap = new WritableNativeMap();
                        writableMap.putString("messageCenter", messageCenter);
                        writableMap.putArray("messages", RCTPIOCommonUtils.writableArrayFromMCMessages(messages));
                        callback.invoke(null, writableMap);
                    }
                }

                @Override
                public void onFailure(String messageCenter, PIOMCMessageError errorMessage) {
                    if (callback != null) {
                        WritableMap writableMap = new WritableNativeMap();
                        writableMap.putString("messageCenter", messageCenter);
                        callback.invoke(errorMessage.getErrorDescription(), writableMap);
                    }
                }
            });
        } catch (PIOMCMessageException e) {
            if (callback != null) {
                WritableMap writableMap = new WritableNativeMap();
                writableMap.putString("messageCenter", messageCenter);
                callback.invoke(e.getMessage(), writableMap);
            }
        }
    }

    @ReactMethod
    public void fetchRichContentForMessage(String messageId, final Callback callback) {
        try {
            mPushIOManager.fetchRichContentForMessage(messageId, new PIOMCRichContentListener() {
                @Override
                public void onSuccess(String messageId, String richContent) {
                    if (callback != null) {
                        WritableMap map = new WritableNativeMap();
                        map.putString("messageID", messageId);
                        map.putString("richContent", richContent);
                        callback.invoke(null, map);
                    }
                }

                @Override
                public void onFailure(String messageId, PIOMCMessageError errorMessage) {
                    if (callback != null) {
                        WritableMap map = new WritableNativeMap();
                        map.putString("messageID", messageId);
                        callback.invoke(errorMessage.getErrorDescription(), map);
                    }
                }
            });
        } catch (PIOMCRichContentException e) {
            PIOLogger.v("RN fRCFM " + e.getMessage());
            if (callback != null) {
                WritableMap map = new WritableNativeMap();
                map.putString("messageID", messageId);
                callback.invoke(e.getMessage(), map);
            }
        }
    }

    @ReactMethod
    public void setInAppFetchEnabled(boolean isEnabled) {
        mPushIOManager.setInAppFetchEnabled(isEnabled);
    }

    @ReactMethod
    public void setCrashLoggingEnabled(boolean isEnabled) {
        mPushIOManager.setCrashLoggingEnabled(isEnabled);
    }

    @ReactMethod
    public void isCrashLoggingEnabled(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.isCrashLoggingEnabled());
        } else {
            PIOLogger.v("RN iCLE callback is null");
        }
    }

    @ReactMethod
    public void setDeviceToken(String deviceToken) {
        mPushIOManager.setDeviceToken(deviceToken);
    }

    @ReactMethod
    public void setMessageCenterBadgingEnabled(boolean isEnabled) {
        mPushIOManager.setMessageCenterBadgingEnabled(isEnabled);
    }

    @ReactMethod
    public void setBadgeCount(int badgeCount, boolean forceSetBadge, final Callback callback) {

        mPushIOManager.setBadgeCount(badgeCount, forceSetBadge, new PIOBadgeSyncListener() {
            @Override
            public void onBadgeSyncedSuccess(String response) {
                if (callback != null) {
                    callback.invoke(null, response);
                }
            }

            @Override
            public void onBadgeSyncedFailure(String errorReason) {
                if (callback != null) {
                    callback.invoke(errorReason, null);
                }
            }
        });
    }

    @ReactMethod
    public void getBadgeCount(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.getBadgeCount());
        } else {
            PIOLogger.v("RN gBC callback is null");
        }
    }

    @ReactMethod
    public void resetBadgeCount(boolean forceSetBadge, final Callback callback) {

        mPushIOManager.resetBadgeCount(forceSetBadge, new PIOBadgeSyncListener() {
            @Override
            public void onBadgeSyncedSuccess(String response) {
                if (callback != null) {
                    callback.invoke(null, response);
                }
            }

            @Override
            public void onBadgeSyncedFailure(String errorReason) {
                if (callback != null) {
                    callback.invoke(errorReason, null);
                }
            }
        });
    }

    @ReactMethod
    public void resetMessageCenter() {
        mPushIOManager.resetMessageCenter();
    }

    @ReactMethod
    public void clearInAppMessages() {
        mPushIOManager.clearInAppMessages();
    }

    @ReactMethod
    public void clearInteractiveNotificationCategories() {
        mPushIOManager.clearInteractiveNotificationCategories();
    }

    @ReactMethod
    public void isResponsysPush(ReadableMap remoteMessageMap, final Callback callback) {
        if (callback != null) {
            callback.invoke(null,
                    mPushIOManager.isResponsysPush(RCTPIOCommonUtils.remoteMessageFromReadableMap(remoteMessageMap)));
        } else {
            PIOLogger.v("RN iRP callback is null");
        }
    }

    @ReactMethod
    public void handleMessage(ReadableMap remoteMessageMap) {
        mPushIOManager.handleMessage(RCTPIOCommonUtils.remoteMessageFromReadableMap(remoteMessageMap));
    }

    @ReactMethod
    public void onGeoRegionEntered(ReadableMap geoRegionReadableMap, final Callback callback) {
        mPushIOManager.onGeoRegionEntered(
                RCTPIOCommonUtils.geoRegionFromReadableMap(geoRegionReadableMap, PIORegionEventType.GEOFENCE_ENTRY),
                new PIORegionCompletionListener() {
                    @Override
                    public void onRegionReported(String regionId, PIORegionEventType regionType, PIORegionException e) {
                        if (callback != null) {
                            WritableMap map = new WritableNativeMap();
                            map.putString("regionID", regionId);
                            map.putString("regionType", regionType.toString());
                            if (e == null) {
                                callback.invoke(null, map);
                            } else {
                                callback.invoke(e.getErrorDescription(), map);
                            }

                        }
                    }
                });
    }

    @ReactMethod
    public void onGeoRegionExited(ReadableMap geoRegionReadableMap, final Callback callback) {
        mPushIOManager.onGeoRegionExited(
                RCTPIOCommonUtils.geoRegionFromReadableMap(geoRegionReadableMap, PIORegionEventType.GEOFENCE_EXIT),
                new PIORegionCompletionListener() {
                    @Override
                    public void onRegionReported(String regionId, PIORegionEventType regionType, PIORegionException e) {
                        if (callback != null) {
                            WritableMap map = new WritableNativeMap();
                            map.putString("regionID", regionId);
                            map.putString("regionType", regionType.toString());
                            if (e == null) {
                                callback.invoke(null, map);
                            } else {
                                callback.invoke(e.getErrorDescription(), map);
                            }
                        }
                    }
                });
    }

    @ReactMethod
    public void onBeaconRegionEntered(ReadableMap beaconRegionReadableMap, final Callback callback) {
        mPushIOManager.onBeaconRegionEntered(
                RCTPIOCommonUtils.beaconRegionFromReadableMap(beaconRegionReadableMap, PIORegionEventType.BEACON_ENTRY),
                new PIORegionCompletionListener() {
                    @Override
                    public void onRegionReported(String regionId, PIORegionEventType regionType, PIORegionException e) {
                        if (callback != null) {
                            WritableMap map = new WritableNativeMap();
                            map.putString("regionID", regionId);
                            map.putString("regionType", regionType.toString());
                            if (e == null) {
                                callback.invoke(null, map);
                            } else {
                                callback.invoke(e.getErrorDescription(), map);
                            }

                        }
                    }
                });
    }

    @ReactMethod
    public void onBeaconRegionExited(ReadableMap beaconRegionReadableMap, final Callback callback) {
        mPushIOManager.onBeaconRegionExited(
                RCTPIOCommonUtils.beaconRegionFromReadableMap(beaconRegionReadableMap, PIORegionEventType.BEACON_EXIT),
                new PIORegionCompletionListener() {
                    @Override
                    public void onRegionReported(String regionId, PIORegionEventType regionType, PIORegionException e) {
                        if (callback != null) {
                            WritableMap map = new WritableNativeMap();
                            map.putString("regionID", regionId);
                            map.putString("regionType", regionType.toString());
                            if (e == null) {
                                callback.invoke(null, map);
                            } else {
                                callback.invoke(e.getErrorDescription(), map);
                            }
                        }
                    }
                });
    }

    @ReactMethod
    public void addInteractiveNotificationCategory(ReadableMap readableMap, final Callback callback) {
        boolean result = mPushIOManager
                .addInteractiveNotificationCategory(RCTPIOCommonUtils.notificationCategoryFromReadableMap(readableMap));

        if (callback != null) {
            callback.invoke(null, result);
        }
    }

    @ReactMethod
    public void getInteractiveNotificationCategory(String categoryId, final Callback callback) {
        PIOInteractiveNotificationCategory notificationCategory = mPushIOManager
                .getInteractiveNotificationCategory(categoryId);

        if (callback != null) {
            if (notificationCategory != null) {
                callback.invoke(null, RCTPIOCommonUtils.writableMapFromNotificationCategory(notificationCategory));
            } else {
                callback.invoke(null, null);
            }
        }
    }

    @ReactMethod
    public void deleteInteractiveNotificationCategory(String categoryId) {
        mPushIOManager.deleteInteractiveNotificationCategory(categoryId);
    }

    @ReactMethod
    public void trackMessageCenterDisplayEngagement(String messageID) {
        mPushIOManager.trackMessageCenterDisplayEngagement(messageID);
    }

    @ReactMethod
    public void trackMessageCenterOpenEngagement(String messageID) {
        mPushIOManager.trackMessageCenterOpenEngagement(messageID);
    }

    @ReactMethod
    public void onMessageCenterViewVisible() {
        try {
            mPushIOManager.onMessageCenterViewVisible();
        } catch (PIOMCMessageException e) {
            PIOLogger.v("RN oMCVV " + e.getMessage());
        }
    }

    @ReactMethod
    public void onMessageCenterViewFinish() {
        try {
            mPushIOManager.onMessageCenterViewFinish();
        } catch (PIOMCMessageException e) {
            PIOLogger.v("RN oMCVF " + e.getMessage());
        }
    }

    @ReactMethod
    public void trackEmailConversion(String uri, final Callback callback) {
        if (!TextUtils.isEmpty(uri)) {
            Intent launchIntent = new Intent();
            launchIntent.setData(Uri.parse(uri));

            mPushIOManager.trackEmailConversion(launchIntent, callback == null ? null : new PIODeepLinkListener() {
                @Override
                public void onDeepLinkReceived(final String deeplinkUrl, final String webLinkUrl) {
                    Log.v(LOG_TAG, "dl: " + deeplinkUrl + ", wl: " + webLinkUrl);

                    WritableMap map = new WritableNativeMap();
                    map.putString("deeplinkUrl", deeplinkUrl);
                    map.putString("webLinkUrl", webLinkUrl);
                    callback.invoke(null, map);
                }
            });
        }
    }

    @ReactMethod
    public void setDelayRichPushDisplay(boolean flag) {
        mPushIOManager.delayRichPushDisplay(flag);
    }

    @ReactMethod
    public void isRichPushDelaySet(final Callback callback) {
        if (callback != null) {
            callback.invoke(null, mPushIOManager.isRichPushDelaySet());
        } else {
            PIOLogger.v("RN iRPDS callback is null");
        }
    }

    @ReactMethod
    public void showRichPushMessage() {
        mPushIOManager.showRichPushMessage();
    }

    @ReactMethod
    public void trackConversionEvent(ReadableMap eventReadableMap, final Callback callback) {
        PIOConversionEvent conversionEvent = RCTPIOCommonUtils.conversionEventFromReadableMap(eventReadableMap);

        if (conversionEvent == null) {
            callback.invoke("Invalid Event", null);
            return;
        }

        mPushIOManager.trackConversionEvent(conversionEvent, callback == null ? null : new PIOConversionListener() {
            @Override
            public void onSuccess() {
                callback.invoke(null, "success");
            }

            @Override
            public void onFailure(Exception e) {
                callback.invoke(e.getMessage(), null);
            }
        });
    }

    private void emitEvent(String eventName, WritableMap map) {
        try {
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, map);
        } catch (Exception e) {
            PIOLogger.v("RN eE " + e);
        }
    }

    @ReactMethod
    public void setNotificationSmallIconColor(String colorHex) {
        if (!TextUtils.isEmpty(colorHex)) {
            final int color = Color.parseColor(colorHex);
            mPushIOManager.setNotificationSmallIconColor(color);
        }

    }

    @ReactMethod
    public void setNotificationSmallIcon(String resourceName) {
        if (!TextUtils.isEmpty(resourceName)) {
            Context applicationContext = getReactApplicationContext().getApplicationContext();
            int resourceId = applicationContext.getResources().getIdentifier(
                    resourceName, "drawable", applicationContext.getPackageName());

            if (resourceId <= 0) {
                resourceId = applicationContext.getResources().getIdentifier(
                        resourceName, "mipmap", applicationContext.getPackageName());
            }

            if (resourceId > 0) {
                setDefaultSmallIcon(resourceId);
            }
        }
    }

    @ReactMethod
    public void setNotificationLargeIcon(String resourceName) {
        if (!TextUtils.isEmpty(resourceName)) {
            Context applicationContext = getReactApplicationContext().getApplicationContext();
            int resourceId = applicationContext.getResources().getIdentifier(
                    resourceName, "drawable", applicationContext.getPackageName());

            if (resourceId <= 0) {
                resourceId = applicationContext.getResources().getIdentifier(
                        resourceName, "mipmap", applicationContext.getPackageName());
            }

            if (resourceId > 0) {
                setDefaultLargeIcon(resourceId);
            }
        }
    }

}
