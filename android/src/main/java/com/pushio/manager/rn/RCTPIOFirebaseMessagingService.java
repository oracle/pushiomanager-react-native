/*
 * Copyright © 2024, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

package com.pushio.manager.rn;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pushio.manager.PIOLogger;
import com.pushio.manager.PushIOManager;

public class RCTPIOFirebaseMessagingService extends FirebaseMessagingService {
    @SuppressLint("WrongThread")
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        PIOLogger.v("RN oNT New Device Token: " + token);

        PushIOManager.getInstance(getApplicationContext()).setDeviceToken(token);

        if (RCTPIODeviceEventEmitter.INSTANCE.isAppListeningForEvent(RCTPIODeviceEventEmitter.EVENT_NEW_TOKEN)) {
            RCTPIODeviceEventEmitter.INSTANCE.emit(getApplicationContext(), RCTPIODeviceEventEmitter.EVENT_NEW_TOKEN,
                    RCTPIOCommonUtils.writableMapFromString("deviceToken", token));
        }
    }

    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        PIOLogger.v("RN oMR ID: " + remoteMessage.getMessageId());

        PushIOManager pushIOManager = PushIOManager.getInstance(getApplicationContext());

        if (RCTPIODeviceEventEmitter.INSTANCE.isAppListeningForEvent(RCTPIODeviceEventEmitter.EVENT_NEW_PUSH_MESSAGE)) {
            RCTPIODeviceEventEmitter.INSTANCE.emit(getApplicationContext(),
                    RCTPIODeviceEventEmitter.EVENT_NEW_PUSH_MESSAGE,
                    RCTPIOCommonUtils.writableMapFromRemoteMessage(remoteMessage));
        } else {
            if (pushIOManager.isResponsysPush(remoteMessage)) {
                pushIOManager.handleMessage(remoteMessage);
            } else {
                PIOLogger.v("RN oMR NRP");
            }
        }
    }
}
