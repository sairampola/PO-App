package com.placementoffice.hemanthreddy.login.gcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.placementoffice.hemanthreddy.login.HomeScreen;
import com.placementoffice.hemanthreddy.login.LoginScreen;
import com.placementoffice.hemanthreddy.login.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hemanthreddy on 2/24/2016.
 */
public class GCM extends IntentService{

    MyApplication myApplication;

    Context context;
    String register_id,rollno;
    SharedPreferences pref;
    int i = 20;
    public GCM()
    {
        super(GCM.class.getSimpleName());
    }
    public void registerUser(String email)
    {
        pref = myApplication.getSharedPreference(context);
        register_id = "";
        register_id = pref.getString("regid","");
        try
        {
            if (TextUtils.isEmpty(register_id)) {
                Toast.makeText(context,"sdfs",Toast.LENGTH_SHORT).show();
                InstanceID instanceID = InstanceID.getInstance(this);
                register_id = instanceID.getToken((GCM_Application_Constants.GOOGLE_SENDER_ID),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.e("re", "sdfdsfsdfs");
                Toast.makeText(context,register_id,Toast.LENGTH_SHORT).show();
                Log.e("reg", register_id);
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("register id");
                alertDialog.setMessage(register_id);


                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "thanks", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();

            }


        }catch(Exception e)
        {
            Log.e("error",e.getMessage());
        }
        if(!TextUtils.isEmpty(register_id))
        {
            saveToSharedPreference(register_id,rollno);
        }
    }

    private void saveToSharedPreference(String register_id,String rollno) {
        //saving rollno and register id to shared preference
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regid",register_id);
        editor.putString("rollno",rollno);
        editor.commit();
        saveToServer(register_id,rollno);
    }

    private void saveToServer(final String register_id, final String rollno) {
        StringRequest request;
        request = new StringRequest(Request.Method.POST, GCM_Application_Constants.APP_SERVER_URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("error");
                    if(!status) {
                        while ((i--) != 0) {
                            saveToServer(register_id, rollno);
                        }
                        if (i == 0) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                            alertDialog.setTitle("error");
                            alertDialog.setMessage("Coz of Server busy cannot register to server and also cannot get notifications");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "thanks", Toast.LENGTH_SHORT).show();
                                }
                            });
                            alertDialog.show();
                        }
                    }

                } catch (JSONException e) {
                    Log.e("json exception", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POST error","error while saving regid to the server");
            }
        })
            {
                @Override
                public Map<String,String> getParams ()
                {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("rollno",rollno);
                    params.put("regid",register_id);
                    return params;
                }
            };
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        context = getApplicationContext();
        String f = intent.getStringExtra("type");
        rollno = intent.getStringExtra("rollno");
        myApplication = new MyApplication();
        //if(f == "register")
       registerUser(rollno);
       // else
          //  unRegisterUser(rollno);

    }

    private void unRegisterUser(String rollno) {
        pref = myApplication.getSharedPreference(context);
        String f = pref.getString("regid","");
        if(TextUtils.isEmpty(f))
            return;
        else
        {
            final String r = pref.getString("rollno","");
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();

            StringRequest request;
            request = new StringRequest(Request.Method.POST, GCM_Application_Constants.APP_SERVER_URL_UNREGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("error");
                        if(!status) {
                            while ((i--) != 0) {
                                unRegisterUser(r);
                            }

                        }

                    } catch (JSONException e) {
                        Log.e("json exception", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("POST error","unable unregister hte user");
                }
            })
            {
                @Override
                public Map<String,String> getParams ()
                {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("rollno",r);
                    params.put("regid",register_id);
                    return params;
                }
            };
            MySingleton.getInstance(context).addToRequestQueue(request);


        }
    }
}
