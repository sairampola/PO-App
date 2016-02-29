package com.placementoffice.hemanthreddy.login.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.google.android.gms.gcm.GcmListenerService;
import com.placementoffice.hemanthreddy.login.HomeScreen;

/**
 * Created by hemanthreddy on 2/24/2016.
 */
public class GCMBroadcastReceiver extends GcmListenerService {
   // @Override
   // public void onReceive(Context context, Intent intent) {
      //  ComponentName componentName = new ComponentName(context.getPackageName(),
              //  GCMNotificationIntentService.class.getName());
       // startWakefulService(context,(intent.setComponent(componentName)));
       // setResultCode(Activity.RESULT_OK);
    //}

    NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {
        String title = bundle.getString("title");
        String message  = bundle.getString("message");

        Intent resultIntent = new Intent(getApplicationContext(), HomeScreen.class);
        resultIntent.putExtra("message",message);

        notificationUtils = new NotificationUtils(getApplicationContext());
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotification(title,message,resultIntent);
    }
}
