package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class YbdView extends AppCompatActivity {

    TextView companyname,cutoff,paypackage,note,recuriment,bond,role;

    Button applyjob;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ybd_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Details...");
        progressDialog.show();
        companyname = (TextView) findViewById(R.id.company_name);
        cutoff = (TextView) findViewById(R.id.cutoff);
        paypackage = (TextView) findViewById(R.id.paypackage);
        note = (TextView) findViewById(R.id.note);
        recuriment = (TextView) findViewById(R.id.recruiment);
        bond  = (TextView) findViewById(R.id.servicebond);
        role = (TextView) findViewById(R.id.role);
        applyjob = (Button) findViewById(R.id.apply);
        Intent intent = this.getIntent();
        final String jobid = intent.getStringExtra("jobid");
        boolean eligible = intent.getBooleanExtra("eligible",false);
        if(!eligible)
            applyjob.setVisibility(View.GONE);

        applyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Applying...");
                progressDialog.show();
                String url = new URLConstants().BaseURL+"applyjob.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            progressDialog.dismiss();
                            JSONObject object = new JSONObject(response);
                            boolean e = object.getBoolean("error");
                            if(e)
                            {
                                Toast.makeText(getApplicationContext(),"error "+object.getString("error_details"),Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),object.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException error)
                        {
                            Toast.makeText(getApplicationContext(),"json object error"+error.getMessage()+" "+response,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"volley error "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                public Map<String, String> getParams()
                    {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("rollno",LoginScreen.rollno);
                        params.put("jobid",jobid);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });

        String url = new URLConstants().BaseURL+"getybddetails.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject  = new JSONObject(response);
                    companyname.setText(jsonObject.getString("name"));
                    cutoff.setText(jsonObject.getString("cut_off"));
                    paypackage.setText(jsonObject.getString("package"));
                    note.setText("All The BEST");
                    recuriment.setText(jsonObject.getString("recruitment_type"));
                    role.setText(jsonObject.getString("role"));
                    bond.setText(jsonObject.getString("service_bond"));
                    progressDialog.dismiss();
                }catch (JSONException error)
                {
                    Toast.makeText(getApplicationContext(),"json error"+error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error " + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            public Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("jobid",jobid);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
