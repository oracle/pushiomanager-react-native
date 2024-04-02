/*
* Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
*
* Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
*/

package com.pushio.manager.rn;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.MainThread;

import java.util.ArrayList;
import java.util.List;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.pushio.manager.PIOAppLifecycleManager;
import com.pushio.manager.PIOLogger;
import com.pushio.manager.PushIOManager;

public enum RCTPIODeviceEventEmitter {

    INSTANCE;

    static final String EVENT_NEW_TOKEN = "pio_new_token";
    static final String EVENT_NEW_PUSH_MESSAGE = "pio_new_push_message";

    private List<String> eventListeners;

    RCTPIODeviceEventEmitter() {
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
        }
    }

    void addListener(String eventName) {
        if (!TextUtils.isEmpty(eventName)) {

            if (!eventListeners.contains(eventName)) {
                eventListeners.add(eventName);
            } else {
                PIOLogger.v("RN aL " + eventName + " is already registered");
            }

        } else {
            PIOLogger.v("RN aL eventName is null or empty");
        }
    }

    void removeAllListeners() {
        if (eventListeners != null) {
            eventListeners.clear();
        }
    }

    boolean isAppListeningForEvent(String eventName) {

        if (eventListeners != null) {
            return eventListeners.contains(eventName);
        }

        return false;
    }

    @MainThread
    void emit(Context applicationContext, String eventName, WritableMap eventPayload) {

        try {

            ReactApplication application = (ReactApplication) applicationContext;

            if (application == null) {
                PIOLogger.v("RN emit Error sending Event <" + eventName + ">, application is null");
                return;
            }

            ReactNativeHost reactNativeHost = application.getReactNativeHost();

            if (reactNativeHost == null) {
                PIOLogger.v("RN emit Error sending Event <" + eventName + ">, reactNativeHost is null");
                return;
            }

            ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();

            if (reactInstanceManager == null) {
                PIOLogger.v("RN emit Error sending Event <" + eventName + ">, reactInstanceManager is null");
                return;
            }

            ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

            if (reactContext != null) {

                reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit(eventName, eventPayload);

            }

        } catch (Exception e) {
            PIOLogger.v("RN emit Error sending Event <" + eventName + ">, " + e.getMessage());
        }
    }
}
