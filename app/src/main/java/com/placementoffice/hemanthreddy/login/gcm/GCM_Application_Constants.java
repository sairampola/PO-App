package com.placementoffice.hemanthreddy.login.gcm;

/**
 * Created by hemanthreddy on 2/24/2016.
 */
public interface GCM_Application_Constants {

    //php application url to store the registered ID

     String APP_SERVER_URL_REGISTER = "http://10.10.30.212:80/gcm/store_user_regid.php";

    //uri to unregiter
    String  APP_SERVER_URL_UNREGISTER = "http://10.10.30.212:80/gcm/unsubscribe_nofication.php";
    //Google project number
     String GOOGLE_APP_ID = "";

    //message key
    String MEG_KEY = "m";

    //google sender id
   // String google_sender_id = "";

    //sender id
     String GOOGLE_SENDER_ID = "609868438128";

    //notification id
     int NOTIFICATION_ID = 5636;
}
