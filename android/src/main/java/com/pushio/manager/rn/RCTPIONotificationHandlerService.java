package com.pushio.manager.rn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.messaging.RemoteMessage;
import com.pushio.manager.PIOLogger;
import com.pushio.manager.PushIOManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RCTPIONotificationHandlerService extends JobIntentService {

    private static final String ORCL_RN_STORE = "orcl_rn_store";
    private static final String ORCL_RN_STORE_KEY_EID_SET = "orcl_rn_store_key_eid_set";
    private static final String ORCL_RN_STORE_KEY_TS = "orcl_rn_store_key_ts";
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        PushIOManager pushIOManager = PushIOManager.getInstance(getApplicationContext());

        Bundle extras = intent.getExtras();

        if (extras == null) {
            PIOLogger.v("RCTPIONHS oHW empty Intent");
            return;
        }

        RemoteMessage remoteMessage = new RemoteMessage(extras);

        if (pushIOManager.isResponsysPush(remoteMessage)) {

            if (isDuplicatePush(remoteMessage)) {
                PIOLogger.v("RCTPIONHS oHW Duplicate push: " + remoteMessage.getMessageId());
                return;
            }

            cleanupEID();

            pushIOManager.handleMessage(remoteMessage);

        } else {
            PIOLogger.v("RCTPIONHS oHW NRP");
        }
    }

    private boolean isDuplicatePush(RemoteMessage remoteMessage) {

        final String eid = getEID(remoteMessage);

        if (TextUtils.isEmpty(eid)) {
            PIOLogger.v("RCTPIONHS iDP EID Missing");
            return true;
        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ORCL_RN_STORE, Activity.MODE_PRIVATE);

        Set<String> eids = sharedPreferences.getStringSet(ORCL_RN_STORE_KEY_EID_SET, null);

        if (eids == null) {

            PIOLogger.v("RCTPIONHS iDP Setting eid: " + eid);

            storeEID(null, eid);

            return false;

        } else {

            if (eids.contains(eid)) {

                PIOLogger.v("RCTPIONHS iDP Duplicate push: " + eid);

                return true;

            } else {

                PIOLogger.v("RCTPIONHS iDP Adding eid: " + eid);

                storeEID(eids, eid);

                return false;
            }

        }
    }

    private String getEID(RemoteMessage remoteMessage) {

        if (remoteMessage != null) {
            Map<String, String> messageData = remoteMessage.getData();
            if (messageData != null && messageData.containsKey("ei")) {
                return messageData.get("ei");
            }
        }

        return null;
    }

    private void storeEID(Set<String> oldSet, String eid){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ORCL_RN_STORE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        Set<String> eidSet;

        if(oldSet != null){
            eidSet = new HashSet<>(oldSet);
        }else{
            eidSet = new HashSet<>();
        }

        eidSet.add(eid);

        sharedPreferencesEditor.putStringSet(ORCL_RN_STORE_KEY_EID_SET, eidSet);
        sharedPreferencesEditor.commit();
    }

    private void cleanupEID() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(ORCL_RN_STORE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        final Set<String> eids = sharedPreferences.getStringSet(ORCL_RN_STORE_KEY_EID_SET, null);

        if (eids != null && eids.size() >= 500) {
            sharedPreferencesEditor.putString(ORCL_RN_STORE_KEY_TS, getDateAsString(new Date()));
            sharedPreferencesEditor.putStringSet(ORCL_RN_STORE_KEY_EID_SET, new HashSet<String>());
            sharedPreferencesEditor.commit();
            return;
        }

        final String lastCleanupTSStr = sharedPreferences.getString(ORCL_RN_STORE_KEY_TS, null);

        if (!TextUtils.isEmpty(lastCleanupTSStr)) {

            Date lastCleanupTS = getDateFromString(lastCleanupTSStr);
            Date now = new Date();

            if (getDateInterval(lastCleanupTS, now) > 12) {
                sharedPreferencesEditor.putString(ORCL_RN_STORE_KEY_TS, getDateAsString(new Date()));
                sharedPreferencesEditor.putStringSet(ORCL_RN_STORE_KEY_EID_SET, new HashSet<String>());
            }

        } else {
            sharedPreferencesEditor.putString(ORCL_RN_STORE_KEY_TS, getDateAsString(new Date()));
        }

        sharedPreferencesEditor.commit();
    }

    private String getDateAsString(Date date) {
        DateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.getDefault());
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    private Date getDateFromString(String timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.getDefault());
            return sdf.parse(timestamp);
        } catch (ParseException e) {
            return null;
        }
    }

    private long getDateInterval(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return -1;
        }

        long diffInMillis = date2.getTime() - date1.getTime();
        return TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
    
}
