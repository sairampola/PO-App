package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
 EditText feedback;
    Button submit;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (EditText) findViewById(R.id.feedback);
        submit = (Button) findViewById(R.id.s_feedback);
        progressDialog = new ProgressDialog(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Submitting Feedback...");
                progressDialog.show();
                String url = new URLConstants().BaseURL+"feedback.php";
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Feedback Suiccessfully Sent",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    public Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("feedback",feedback.getText().toString());
                        params.put("rollno",new UserSessionManager(getApplicationContext()).getRollno());
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });
    }
}
