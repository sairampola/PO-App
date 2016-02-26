package com.placementoffice.hemanthreddy.login.gcm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hemanthreddy on 2/26/2016.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    public SharedPreferences getSharedPreference(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("GCM_DETAILS",0);

        return pref;
    }

}
