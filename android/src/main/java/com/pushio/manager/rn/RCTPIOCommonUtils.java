/*
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/

package com.pushio.manager.rn;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.firebase.messaging.RemoteMessage;
import com.pushio.manager.PIOConversionEvent;
import com.pushio.manager.PIOBeaconRegion;
import com.pushio.manager.PIOGeoRegion;
import com.pushio.manager.PIOInteractiveNotificationButton;
import com.pushio.manager.PIOInteractiveNotificationCategory;
import com.pushio.manager.PIOLogger;
import com.pushio.manager.PIOMCMessage;
import com.pushio.manager.PIORegionEventType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

class RCTPIOCommonUtils {

    private static final String FB_KEY_COLLAPSE_KEY = "collapseKey";
    private static final String FB_KEY_DATA = "data";
    private static final String FB_KEY_FROM = "from";
    private static final String FB_KEY_MESSAGE_ID = "messageId";
    private static final String FB_KEY_MESSAGE_TYPE = "messageType";
    private static final String FB_KEY_TTL = "ttl";
    private static final String FB_KEY_PRIORITY = "priority";
    private static final String FB_KEY_SENT_TIME = "sentTime";
    private static final String FB_KEY_TO = "to";
    private static final String DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    static Map<String, String> convertMap(@NonNull Map<String, Object> map) {
        Map<String, String> newMap = new HashMap<>();
        for (String key : map.keySet()) {
            newMap.put(key, String.valueOf(map.get(key)));
        }
        return newMap;
    }

    static RemoteMessage remoteMessageFromReadableMap(ReadableMap readableMap) {
        RemoteMessage.Builder builder = new RemoteMessage.Builder("rsys_internal");

        if (readableMap.hasKey(FB_KEY_TTL)) {
            builder.setTtl(readableMap.getInt(FB_KEY_TTL));
        }

        if (readableMap.hasKey(FB_KEY_MESSAGE_ID)) {
            builder.setMessageId(readableMap.getString(FB_KEY_MESSAGE_ID));
        }

        if (readableMap.hasKey(FB_KEY_MESSAGE_TYPE)) {
            builder.setMessageType(readableMap.getString(FB_KEY_MESSAGE_TYPE));
        }

        if (readableMap.hasKey(FB_KEY_COLLAPSE_KEY)) {
            builder.setCollapseKey(readableMap.getString(FB_KEY_COLLAPSE_KEY));
        }

        if (readableMap.hasKey(FB_KEY_DATA)) {
            ReadableMap messageData = readableMap.getMap(FB_KEY_DATA);
            if (messageData != null) {
                ReadableMapKeySetIterator iterator = messageData.keySetIterator();
                while (iterator.hasNextKey()) {
                    String key = iterator.nextKey();
                    builder.addData(key, messageData.getString(key));
                }
            }
        }

        return builder.build();
    }

    static PIOGeoRegion geoRegionFromReadableMap(ReadableMap readableMap, PIORegionEventType regionEventType) {

        PIOGeoRegion region = new PIOGeoRegion();

        final String geofenceId = readableMap.getString("geofenceId");
        final String geofenceName = readableMap.getString("geofenceName");

        if (TextUtils.isEmpty(geofenceId) || TextUtils.isEmpty(geofenceName)) {
            return null;
        }

        region.setGeofenceId(geofenceId);
        region.setGeofenceName(geofenceName);
        region.setRegionEventType(regionEventType);
        region.setZoneName(readableMap.getString("zoneName"));
        region.setZoneId(readableMap.getString("zoneId"));
        region.setSource(readableMap.getString("source"));
        region.setDeviceBearing(readableMap.getDouble("deviceBearing"));
        region.setDeviceSpeed(readableMap.getDouble("deviceSpeed"));
        region.setDwellTime(readableMap.getInt("dwellTime"));

        if (readableMap.hasKey("extra")) {
            ReadableMap extraData = readableMap.getMap("extra");
            if (extraData != null) {
                ReadableMapKeySetIterator iterator = extraData.keySetIterator();
                Map<String, String> customParams = new HashMap<>();
                while (iterator.hasNextKey()) {
                    String key = iterator.nextKey();
                    customParams.put(key, extraData.getString(key));
                }
                region.setExtra(customParams);
            }
        }

        return region;
    }

    static PIOBeaconRegion beaconRegionFromReadableMap(ReadableMap readableMap, PIORegionEventType regionEventType) {

        PIOBeaconRegion region = new PIOBeaconRegion();

        final String beaconId = readableMap.getString("beaconId");
        final String beaconName = readableMap.getString("beaconName");

        if (TextUtils.isEmpty(beaconId) || TextUtils.isEmpty(beaconName)) {
            return null;
        }

        region.setBeaconId(beaconId);
        region.setBeaconName(beaconName);

        region.setBeaconTag(readableMap.getString("beaconTag"));
        region.setBeaconProximity(readableMap.getString("beaconProximity"));
        region.setiBeaconUUID(readableMap.getString("iBeaconUUID"));
        region.setiBeaconMajor(readableMap.getInt("iBeaconMajor"));
        region.setiBeaconMinor(readableMap.getInt("iBeaconMinor"));
        region.setEddyStoneID1(readableMap.getString("eddyStoneId1"));
        region.setEddyStoneID2(readableMap.getString("eddyStoneId2"));

        region.setRegionEventType(regionEventType);
        region.setZoneName(readableMap.getString("zoneName"));
        region.setZoneId(readableMap.getString("zoneId"));
        region.setSource(readableMap.getString("source"));
        region.setDwellTime(readableMap.getInt("dwellTime"));

        if (readableMap.hasKey("extra")) {
            ReadableMap extraData = readableMap.getMap("extra");
            if (extraData != null) {
                ReadableMapKeySetIterator iterator = extraData.keySetIterator();
                Map<String, String> customParams = new HashMap<>();
                while (iterator.hasNextKey()) {
                    String key = iterator.nextKey();
                    customParams.put(key, extraData.getString(key));
                }
                region.setExtra(customParams);
            }
        }

        return region;
    }

    static PIOInteractiveNotificationCategory notificationCategoryFromReadableMap(ReadableMap readableMap) {
        final String category = readableMap.getString("orcl_category");
        final ReadableArray buttons = readableMap.getArray("orcl_btns");

        if (TextUtils.isEmpty(category) || buttons == null) {
            return null;
        }

        PIOInteractiveNotificationCategory notificationCategory = new PIOInteractiveNotificationCategory();
        notificationCategory.setCategory(category);

        for (int i = 0; i < buttons.size(); ++i) {
            ReadableMap map = buttons.getMap(i);
            if (map != null) {
                PIOInteractiveNotificationButton notificationButton = new PIOInteractiveNotificationButton();
                notificationButton.setId(map.getString("id"));
                notificationButton.setAction(map.getString("action"));
                notificationButton.setLabel(map.getString("label"));

                notificationCategory.addInteractiveNotificationButton(notificationButton);
            }
        }

        return notificationCategory;
    }

    static WritableMap writableMapFromNotificationCategory(PIOInteractiveNotificationCategory notificationCategory) {
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putString("orcl_category", notificationCategory.getCategory());

        WritableArray writableArray = new WritableNativeArray();

        for (PIOInteractiveNotificationButton button : notificationCategory.getInteractiveNotificationButtons()) {
            WritableMap map = new WritableNativeMap();
            map.putString("id", button.getId());
            map.putString("action", button.getAction());
            map.putString("label", button.getLabel());

            writableArray.pushMap(map);
        }

        writableMap.putArray("orcl_btns", writableArray);
        return writableMap;
    }

    static WritableArray writableArrayFromMCMessages(List<PIOMCMessage> messages) {
        if (messages == null) {
            return null;
        }

        WritableArray writableMessages = new WritableNativeArray();

        for (PIOMCMessage message : messages) {
            WritableMap writableMap = new WritableNativeMap();
            writableMap.putString("messageID", message.getId());
            writableMap.putString("subject", message.getSubject());
            writableMap.putString("message", message.getMessage());
            writableMap.putString("iconURL", message.getIconUrl());
            writableMap.putString("messageCenterName", message.getMessageCenterName());
            writableMap.putString("deeplinkURL", message.getDeeplinkUrl());
            writableMap.putString("richMessageHTML", message.getRichMessageHtml());
            writableMap.putString("richMessageURL", message.getRichMessageUrl());
            writableMap.putString("sentTimestamp", getDateAsString(message.getSentTimestamp()));
            writableMap.putString("expiryTimestamp", getDateAsString(message.getExpiryTimestamp()));

            Map<String,String> data = message.getCustomKeyValue();
            
            if (data != null && !data.isEmpty()) {
                WritableMap dataMap = new WritableNativeMap();

                for (Map.Entry<String, String> entry : data.entrySet()) {
                    dataMap.putString(entry.getKey(), entry.getValue());
                }

                writableMap.putMap("customKeyValuePairs", dataMap);
            }

            writableMessages.pushMap(writableMap);
        }

        return writableMessages;
    }

    static PIOConversionEvent conversionEventFromReadableMap(ReadableMap eventReadableMap) {
        if (eventReadableMap == null) {
            return null;
        }

        PIOConversionEvent conversionEvent = new PIOConversionEvent();
        conversionEvent.setConversionType(eventReadableMap.getInt("conversionType"));
        conversionEvent.setOrderId(eventReadableMap.getString("orderId"));

        try {
            conversionEvent.setOrderAmount(eventReadableMap.getDouble("orderTotal"));
            conversionEvent.setOrderQuantity(eventReadableMap.getInt("orderQuantity"));
        } catch (NumberFormatException e) {
            PIOLogger.v("RN cEFRM " + e);
        }

        if (eventReadableMap.hasKey("customProperties")) {
            ReadableMap customPropertiesReadableMap = eventReadableMap.getMap("customProperties");

            if (customPropertiesReadableMap != null) {
                conversionEvent.setProperties(convertMap(customPropertiesReadableMap.toHashMap()));
            }
        }

        return conversionEvent;
    }

    static WritableMap writableMapFromString(String key, String value) {
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putString(key, value);
        return writableMap;
    }

    static WritableMap writableMapFromRemoteMessage(RemoteMessage remoteMessage) {
        WritableMap writableMap = new WritableNativeMap();
        writableMap.putInt(FB_KEY_TTL, remoteMessage.getTtl());
        writableMap.putInt(FB_KEY_PRIORITY, remoteMessage.getPriority());
        writableMap.putDouble(FB_KEY_SENT_TIME, remoteMessage.getSentTime());

        final String messageId = remoteMessage.getMessageId();
        if (!TextUtils.isEmpty(messageId)) {
            writableMap.putString(FB_KEY_MESSAGE_ID, messageId);
        }

        final String collapseKey = remoteMessage.getCollapseKey();
        if (!TextUtils.isEmpty(collapseKey)) {
            writableMap.putString(FB_KEY_COLLAPSE_KEY, collapseKey);
        }

        final String from = remoteMessage.getFrom();
        if (!TextUtils.isEmpty(from)) {
            writableMap.putString(FB_KEY_FROM, from);
        }

        final String messageType = remoteMessage.getMessageType();
        if (!TextUtils.isEmpty(messageType)) {
            writableMap.putString(FB_KEY_MESSAGE_TYPE, messageType);
        }

        final String to = remoteMessage.getTo();
        if (!TextUtils.isEmpty(to)) {
            writableMap.putString(FB_KEY_TO, to);
        }

        Map<String, String> data = remoteMessage.getData();
        if (data != null && !data.isEmpty()) {
            WritableMap dataMap = new WritableNativeMap();

            for (Map.Entry<String, String> entry : data.entrySet()) {
                dataMap.putString(entry.getKey(), entry.getValue());
            }

            writableMap.putMap("data", dataMap);
        }

        return writableMap;
    }

    private static String getDateAsString(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT_ISO8601, Locale.getDefault());
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        }

        return null;
    }
}
