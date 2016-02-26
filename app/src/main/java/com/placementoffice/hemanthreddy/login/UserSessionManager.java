package com.placementoffice.hemanthreddy.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by hemanthreddy on 2/23/2016.
 */
public class UserSessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "loginuser";

    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_ROLLNO = "email";

    public UserSessionManager(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSession(String name,String rollno)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_ROLLNO,rollno);

        editor.commit();
    }
    public void checklogin()
    {
        if(!isUserLoggedIn())
        {

            Intent intent = new Intent(context,LoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    boolean isUserLoggedIn()
    {
       return pref.getBoolean(IS_LOGIN,false);
    }
    public void logoutUser()
    {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context,LoginScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
