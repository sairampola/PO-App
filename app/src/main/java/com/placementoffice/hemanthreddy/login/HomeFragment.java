package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.placementoffice.hemanthreddy.login.gcm.GCM_Application_Constants;
import com.placementoffice.hemanthreddy.login.gcm.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    UserSessionManager userSessionManager;



    int i = 0;

    ListView listView;
    NoticeAdapter noticeAdapter;
    List<Notice> noticeList = new ArrayList<Notice>();
    ProgressDialog pDialog;
    final String url = new URLConstants().BaseURL + "getnoticess.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homef_layout,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSessionManager = new UserSessionManager(getContext());
        userSessionManager.checklogin();

        listView = (ListView) view.findViewById(R.id.noticeList);
        noticeAdapter = new NoticeAdapter(getActivity(), noticeList);
        listView.setAdapter(noticeAdapter);
        this.getNoticess(20);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notice obj = noticeList.get(position);
                //Toast.makeText(getApplicationContext(),obj.getTitle(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), NoticeView.class);
                intent.putExtra("id", obj.getId());
                startActivity(intent);
            }
        });
    }
    public void getNoticess(final int count) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {

                    response = response.trim();
                    String unFormatedJsonString = response;
                    unFormatedJsonString = unFormatedJsonString.replace("\\", "");
                    int len = unFormatedJsonString.length();
                    unFormatedJsonString = unFormatedJsonString.substring(1, len - 1);
                    // Toast.makeText(getApplicationContext(),unFormatedJsonString,Toast.LENGTH_LONG).show();
                    JSONArray jsonArray = new JSONArray(unFormatedJsonString);
                    //Toast.makeText(getApplicationContext(),"JsonArray created",Toast.LENGTH_LONG).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Notice notice = new Notice();
                        notice.setTitle(jsonObject.getString("title"));
                        notice.setDate(jsonObject.getString("don"));
                        notice.setId(jsonObject.getString("id"));
                        noticeList.add(notice);
                    }
                } catch (JSONException error) {
                    Toast.makeText(getContext(), "json error " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
                noticeAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("count", "" + count);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
    private void unRegisterUser() {
        SharedPreferences pref;
        MyApplication myApplication = new MyApplication();
        pref = myApplication.getSharedPreference(getContext());

        final String f = pref.getString("regid", "");
        final String rollno = userSessionManager.getRollno();
        Toast.makeText(getContext(),f+rollno,Toast.LENGTH_SHORT).show();
        if(TextUtils.isEmpty(f))
            return;
        else
        {


            StringRequest request;
            request = new StringRequest(Request.Method.POST, GCM_Application_Constants.APP_SERVER_URL_UNREGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("error");
                        if(!status) {

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
                    params.put("rollno",rollno);
                    return params;
                }
            };
            MySingleton.getInstance(getContext()).addToRequestQueue(request);


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
        MsgSharedPreference obj = new MsgSharedPreference(getContext());
        Log.e("msg received",""+obj.isMsgReceived());
        //Toast.makeText(this,obj.getTitle(),Toast.LENGTH_SHORT).show();
        if(obj.isMsgReceived())
        {
            String title = obj.getTitle();
            String don = obj.getDon();
            String id = obj.getId();

            Notice notice = new Notice();
            notice.setTitle(title);
            notice.setDate(don);
            notice.setId(id);
            noticeList.add(0,notice);
            noticeAdapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(0);
            obj.clearMsgPref();
            obj.setIsMsgReceived(false);
        }

    }

}
