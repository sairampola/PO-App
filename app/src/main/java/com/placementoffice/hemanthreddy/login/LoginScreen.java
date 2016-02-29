package com.placementoffice.hemanthreddy.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.placementoffice.hemanthreddy.login.gcm.GCM;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {


    private TextView userid, password, time;

    public static String rollno;

    Button button;

    UserSessionManager userSessionManager;

    public GCM gcmObj;
    Activity activity;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Context getinstance() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        userSessionManager = new UserSessionManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login to PO APP");
        setSupportActionBar(toolbar);
        userid = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);

        button = (Button) findViewById(R.id.login);
        button.setOnClickListener(this);

        //gcmObj = new GCM();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



    }


    public void requestLogin() {
        final String pass = password.getText().toString();
        rollno = userid.getText().toString();
        if (rollno != null && pass != null) {

            userSessionManager.createSession("hemanth", rollno);
            Intent intent = new Intent(this, GCM.class);
            intent.putExtra("type", "register");
            intent.putExtra("rollno", rollno);
            startService(intent);
            Intent gintent = new Intent(this, HomeScreen.class);
            startActivity(gintent);
            /**
             String url = "";

             StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override public void onResponse(String response) {
            if (response == "1") {
            Toast.makeText(getApplicationContext(),"Logget in as "+email,Toast.LENGTH_LONG).show();
            } else {
            Toast.makeText(getApplicationContext(),"invalid usernema and password",Toast.LENGTH_LONG).show();
            }
            }
            }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"error while logging"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
            })
             {
             @Override public Map<String,String> getParams()
             {
             Map<String,String> params = new HashMap<String,String>();
             params.put("rollno",rollmo);
             params.put("password",pass);
             return params;
             }
             };*/
        }
    }


    @Override
    public void onClick(View v) {
        this.requestLogin();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.placementoffice.hemanthreddy.login/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.placementoffice.hemanthreddy.login/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
