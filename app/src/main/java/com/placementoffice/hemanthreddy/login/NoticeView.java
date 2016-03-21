package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NoticeView extends AppCompatActivity {

    WebView webView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_view);
        webView = (WebView) findViewById(R.id.webView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Notice...");
        Intent intent = this.getIntent();
        String id = intent.getStringExtra("id");
        //Toast.makeText(getApplicationContext(),"webview"+title,Toast.LENGTH_LONG).show();
        String data = this.get_notice_details(id);

        webView.setWebViewClient(new MyWebViewClient());

    }

    private String get_notice_details(final String id) {
        String url = new URLConstants().BaseURL+"notice_details.php";
        final String[] data = new String[1];
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getApplicationContext(),"response",Toast.LENGTH_LONG).show();
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                        data[0] = jsonObject.getString("content");
                    //Toast.makeText(getApplicationContext(),data[0],Toast.LENGTH_LONG).show();
                    webView.loadData(data[0], "text/html", null);
                    progressDialog.dismiss();
                }catch(JSONException error)
                {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
        public Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        return data[0];
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        //show the web page in webview but not in web browser
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           // String data ="<div style=\"text-align: left;\"><b>The following students are shorlisted for Interviews by Futures First scheduled on 14.9.2012 at 10.30 AM</b></div><div style=\"text-align: left;\"><b><br></b></div><div style=\"text-align: left;\"><i><b>List follows as:</b>&nbsp;</i></div><div><div style=\"margin: 0px; padding: 0px; line-height: 15.933333396911621px; outline: none; font-family: arial, helvetica, clean, sans-serif; text-align: start; background-color: rgb(255, 255, 255); \"><ol style=\"font-size: 13px; \"><li>Rahul Talari (1005-09-735029)</li><li>Kanist Agarwal (1005-09-735012)</li><li>A Uday Kumar Reddy (1005-09-734057)</li><li>Bandi nagaraju (1005-09-733033)</li><li>A ganesh Sashank (1005-09-736065)</li></ol><div><font size=\"2\"><b>Reporting Time: </b>10.30 AM</font></div><div><font size=\"2\"><br></font></div><div><b><font size=\"4\">Venue:</font></b><font size=\"2\">&nbsp;</font></div><div style=\"font-size: 13px; \"><div style=\"margin: 0px; padding: 0px; outline: none; \">Futures First,</div><div style=\"margin: 0px; padding: 0px; outline: none; \">Krishe Sapphire, 5th floor,</div><div style=\"margin: 0px; padding: 0px; outline: none; \">Madhapur, Hitech City Main Road.</div><div style=\"margin: 0px; padding: 0px; outline: none; \">Hyderabad-500081</div><div style=\"margin: 0px; padding: 0px; outline: none; \">Phone No : 04042426666</div><div style=\"margin: 0px; padding: 0px; outline: none; \"><br></div><div style=\"margin: 0px; padding: 0px; outline: none; \">All the above students should carry the following Documents</div><div style=\"margin: 0px; padding: 0px; outline: none; \">1. Resumes 2 Sets</div><div style=\"margin: 0px; padding: 0px; outline: none; \">2. Passport Photos</div><div style=\"margin: 0px; padding: 0px; outline: none; \">3. College ID Card</div><div style=\"margin: 0px; padding: 0px; outline: none; \"><br></div><div style=\"margin: 0px; padding: 0px; outline: none; \">All the Best!!</div><div style=\"margin: 0px; padding: 0px; outline: none; \"><br></div><div style=\"margin: 0px; padding: 0px; outline: none; \">Placement Office,</div><div style=\"margin: 0px; padding: 0px; outline: none; \">OUCE</div></div><div style=\"font-size: 13px; \"><br></div></div></div>";

                   view.loadUrl(url);
                  //  view.loadData(data,"text/html",null);
            return true;
        }
    }
}


