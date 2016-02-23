package com.placementoffice.hemanthreddy.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener{

    private static boolean isUserSignedIn;

    private TextView userid,password;

    public static String email;

    Button button;

    UserSessionManager userSessionManager;


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
    }


    public void requestLogin()
    {
        final  String pass = password.getText().toString();
        email = userid.getText().toString();
        if(email != null && pass != null)
        {
            userSessionManager.createSession("hemanth",email);
            Intent intent = new Intent(this,HomeScreen.class);
            startActivity(intent);
            /**
            String url = "";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response == "1") {
                       Toast.makeText(getApplicationContext(),"Logget in as "+email,Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"invalid usernema and password",Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"error while logging"+error.getMessage(),Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                public Map<String,String> getParams()
                {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("email",email);
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
}
