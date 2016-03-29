package com.placementoffice.hemanthreddy.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class
        LoginScreen extends AppCompatActivity implements View.OnClickListener {

    int i=0;
    private TextView userid, password,r_rollno,r_regid,pass,pass1;

    public static String rollno;

    Button button,v_login,v_register,register,submit_password;

    ViewFlipper viewFlipper;

    LinearLayout bts_layout;
    RelativeLayout password_layout;

    Toolbar toolbar;

    CheckBox showpassword;
    ProgressDialog progressDialog;

    UserSessionManager userSessionManager;

    private GoogleApiClient client;

    Context getinstance() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        userSessionManager = new UserSessionManager(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Student Login");
        setSupportActionBar(toolbar);
        userid = (TextView) findViewById(R.id.rollno);
        password = (TextView) findViewById(R.id.password);
        pass = (TextView) findViewById(R.id.pass1);
        pass1 = (TextView) findViewById(R.id.pass2);

        button = (Button) findViewById(R.id.login);
        button.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        submit_password = (Button) findViewById(R.id.submit);
        submit_password.setOnClickListener(this);
        v_login = (Button) findViewById(R.id.bt_login);
        v_login.setOnClickListener(this);
        v_register = (Button) findViewById(R.id.bt_register);
        v_register.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        r_regid = (TextView) findViewById(R.id.r_regid);
        r_rollno = (TextView) findViewById(R.id.r_rollno);

        bts_layout = (LinearLayout) findViewById(R.id.lt_buttons);
        password_layout = (RelativeLayout) findViewById(R.id.enterpassword);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        showpassword = (CheckBox) findViewById(R.id.show_password);
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



    }
    @Override
    public void onBackPressed()
    {
        if(i==0)
        {
            Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT).show();
            i++;
        }
        else
        {
            i--;
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void requestLogin() {
        final String pass = password.getText().toString();
        rollno = userid.getText().toString();
        Toast.makeText(getApplicationContext(),rollno+""+pass,Toast.LENGTH_LONG).show();
        userSessionManager.createSession(rollno,pass);
        if(TextUtils.isEmpty(rollno))
        {
            userid.setError("rollno cant be empty!");
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            password.setError("password cant be empty!");
            return;
        }
        if (rollno != null && pass != null) {
            final int flag;
             String url = new URLConstants().BaseURL+"student_login.php";
            progressDialog.setMessage("logging in.....");
            progressDialog.show();
             StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                    try {
                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);
                        boolean a = jsonObject.getBoolean("error");
                        if(a)
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error_details"),Toast.LENGTH_LONG).show();
                        else
                        {
                           // userSessionManager.createSession(rollno);
                            //starting the gcm intent service to register the device with the GCM servers and main server
                            Toast.makeText(getApplicationContext(),"gcm intent",Toast.LENGTH_SHORT).show();
                            Intent gcm_intent = new Intent(getApplicationContext(),GCM.class);
                            gcm_intent.putExtra("rollno",rollno);
                            startService(gcm_intent);
                            Toast.makeText(getApplicationContext(),"gcm called",Toast.LENGTH_SHORT).show();

                            //starting the home screen intent
                            Intent notice_board_intent = new Intent(getApplicationContext(),HomeScreen.class);
                            startActivity(notice_board_intent);
                        }
                    }catch (JSONException e)
                    {
                        Toast.makeText(getApplicationContext(),"json error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                 }
             }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     progressDialog.dismiss();
                      Toast.makeText(getApplicationContext(),"error"+error.getMessage(),Toast.LENGTH_LONG).show();
                 }
             })
             {
             @Override public Map<String,String> getParams()
             {
             Map<String,String> params = new HashMap<String,String>();
             params.put("rollno",rollno);
             params.put("password", pass);
             return params;
             }
             };
            MySingleton.getInstance(this).addToRequestQueue(request);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login)
        this.requestLogin();
        else if (v.getId() == R.id.register)
        {
            //toolbar.setTitle("Student Registration");
             this.register_Student(r_rollno.getText().toString(), r_regid.getText().toString());

        }
        else if (v.getId() == R.id.bt_login)
        {
            toolbar.setTitle("Student Login");
            if(viewFlipper.getDisplayedChild() == 1)
            {
                viewFlipper.showPrevious();
                v_login.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                v_register.setBackgroundColor(57894);
            }
        }
        else if(v.getId() == R.id.bt_register)
        {
            toolbar.setTitle("Student Registration");
            if(viewFlipper.getDisplayedChild() == 0)
            {
                viewFlipper.showNext();
                v_register.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                v_login.setBackgroundColor(4);
            }
        }
        else if(v.getId() == R.id.submit)
        {
            //code to validate and save password to server
            savePasswordToServer();
        }

    }

    private void savePasswordToServer() {
        if(TextUtils.isEmpty(pass1.getText().toString()))
        {
            pass1.setError("password can't be empty");
            return;
        }
        if(TextUtils.isEmpty(pass.getText().toString()))
        {
            pass.setError("password can't be empty");
        }
        if (pass1.getText().toString().equals(pass.getText().toString()))
            {
                String url = new URLConstants().BaseURL+"save_password.php";
                progressDialog.setMessage("Saving Password and Registering...");
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if(error)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("error_details"),Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Password saved and Registered Successfully",Toast.LENGTH_LONG).show();
                        }catch (JSONException error){
                            Toast.makeText(getApplicationContext(),"json error "+error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Volley error "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    public Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<>();
                        params.put("rollno",new UserSessionManager(getApplicationContext()).getRollno());
                        params.put("password",pass1.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestQueue(request);
            }
            else
            {
                pass1.setError("Password doesn't match");
                return;
            }
    }

    private void register_Student(final String  rollnum, final String registereid) {
        /**block of code to check wheather the rollno is existed or not and the entered register id matches or no
         * then change the loyou to enter password
         */
        if(TextUtils.isEmpty(rollnum))
        {
            r_rollno.setError("rollno can't be empty");
            return;
        }
        if(TextUtils.isEmpty(registereid))
        {
            r_regid.setError("register id can't be empty");
            return;
        }
        String url = new URLConstants().BaseURL+"student_register.php";
        progressDialog.setMessage("Validating Registration...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
              try
              {
                  JSONObject jsonObject = new JSONObject(response);
                  boolean e = jsonObject.getBoolean("error");
                  if(e)
                  {
                      Toast.makeText(getApplicationContext(),jsonObject.getString("error_details"),Toast.LENGTH_LONG).show();
                  }
                  else{
                      Toast.makeText(getApplicationContext(),"registered succesfully",Toast.LENGTH_LONG).show();
                      bts_layout.setVisibility(View.GONE);
                      viewFlipper.setVisibility(View.GONE);
                      password_layout.setVisibility(View.VISIBLE);
                      toolbar.setTitle("Set Password");
                  }
              }catch (JSONException e)
              {
                    Toast.makeText(getApplicationContext(),"jsonexception "+e.getMessage(),Toast.LENGTH_LONG).show();
              }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        )
        {
        @Override
        public Map<String, String> getParams()
        {
            Map<String, String> params = new HashMap<String, String>();
            params.put("rollno",rollnum);
            params.put("registerid",registereid);
            return params;
        }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

}
