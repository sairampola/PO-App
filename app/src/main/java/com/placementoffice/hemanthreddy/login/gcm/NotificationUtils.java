package com.placementoffice.hemanthreddy.login.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.Notifications;
import com.placementoffice.hemanthreddy.login.R;

/**
 * Created by hemanthreddy on 2/25/2016.
 */
public class NotificationUtils {

    Context context;

    public NotificationUtils(Context context)
    {
        this.context = context;
    }
    public void showNotification(String title,String message,Intent intent)
    {
        //check for empty push message
        if(TextUtils.isEmpty(message))
            return;
        Log.e("msg","received");
        //Log.e("data",intent.getStringExtra("data"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT
        );
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(GCM_Application_Constants.NOTIFICATION_ID,notification);
    }
}
