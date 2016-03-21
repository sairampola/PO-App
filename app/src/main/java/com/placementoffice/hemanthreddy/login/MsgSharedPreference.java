package com.placementoffice.hemanthreddy.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hemanthreddy on 3/16/2016.
 */
public class MsgSharedPreference {
    SharedPreferences preferences;
     final String MSG_PREF_NAME = "pushmessage";
    static boolean isMsgReceived = false;
   // String title,don,id;

    public void clearMsgPref()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
    }

    public boolean isMsgReceived() {
        return isMsgReceived;
    }

    public void setIsMsgReceived(boolean isMsgReceive) {
        isMsgReceived = isMsgReceive;
    }

    public MsgSharedPreference(Context context) {
        preferences = context.getSharedPreferences(MSG_PREF_NAME,0);
    }
    public void setMessage(String title,String don,String id)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("title",title);
        editor.putString("don",don);
        editor.putString("id",id);
        editor.commit();
    }

    public String getTitle() {
        return preferences.getString("title","");
    }

    public String getDon() {
        return preferences.getString("don","");
    }

    public String getId() {
        return preferences.getString("id","");
    }
}
