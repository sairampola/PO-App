package com.placementoffice.hemanthreddy.login.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.placementoffice.hemanthreddy.login.HomeScreen;
import com.placementoffice.hemanthreddy.login.MsgSharedPreference;

import org.json.JSONObject;

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
//l;kjldijbgimdglvlkblgidjiobld
    int x,a;
    NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {

        String title = bundle.getString("title");
        String don = bundle.getString("don");
        String id = bundle.getString("id");
        //String message  = bundle.getString("data");
        Log.e("msg came","fuck u");
        MsgSharedPreference obj = new MsgSharedPreference(getApplicationContext());
        obj.setMessage(title,don,id);
        obj.setIsMsgReceived(true);
        Log.e("ismsg",""+obj.isMsgReceived());
        Intent resultIntent = new Intent(getApplicationContext(), HomeScreen.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("don",don);
        resultIntent.putExtra("id",id);

        Log.e("title",title);
        Log.e("from",from);

        notificationUtils = new NotificationUtils(getApplicationContext());
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotification("PO Notice",title,resultIntent);
    }
}
