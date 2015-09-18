package com.kazav.gabi.emt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class PushHendler extends GcmListenerService {

    private String ACTION_GET_LOCATION = "GetMyLoc";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.w("Got push", data.toString());
        String loc_flag = data.getString(ACTION_GET_LOCATION);
        if (loc_flag == null) { AppFlow.sendNotification(data, this, MainPage.class, OpenTicket.class); }
        else {
             GetLoc locs = new GetLoc(this);
             locs.get_loc();
         }
    }
}
