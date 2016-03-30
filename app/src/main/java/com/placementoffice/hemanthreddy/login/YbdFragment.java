package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class YbdFragment extends Fragment {

    ListView listView;
    YBDAdapter ybdAdapter;
    List<YDBclass> ybdlist = new ArrayList<YDBclass>();
    ProgressDialog pDialog;
    final String url = new URLConstants().BaseURL + "getybds.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_ybd,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listView);
        ybdAdapter = new YBDAdapter(getActivity(), ybdlist);
        listView.setAdapter(ybdAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YDBclass obj = ybdlist.get(position);
                Toast.makeText(getContext(), obj.getJobid(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),YbdView.class);
                intent.putExtra("jobid",obj.getJobid());
                intent.putExtra("eligible",obj.isEligible());
                startActivity(intent);
            }
        });
        this.getYBDs(20);
    }

    private void getYBDs(final int i) {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        if(TextUtils.isEmpty(new UserSessionManager(getContext()).getbranch()))
            this.getbranch();
        Toast.makeText(getContext(),new UserSessionManager(getContext()).getbranch(),Toast.LENGTH_SHORT).show();
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
                        YDBclass obj = new YDBclass();
                        obj.setCompanyname(jsonObject.getString("name"));
                        obj.setDesignation(jsonObject.getString("role"));
                        obj.setLastdate(jsonObject.getString("lastdate"));
                        obj.setJobid(jsonObject.getString("jobid"));
                        String branch = new UserSessionManager(getContext()).getbranch().toLowerCase();
                        if(jsonObject.getString(branch).equals("1"))
                            obj.setIsEligible(true);
                        else
                            obj.setIsEligible(false);
                        ybdlist.add(obj);
                    }
                } catch (JSONException error) {
                    Toast.makeText(getContext(), "json error " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
                ybdAdapter.notifyDataSetChanged();

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
                params.put("count", "" + i);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }
    private void getbranch() {
        String url = new URLConstants().BaseURL+"getbranch.php";
        Toast.makeText(getContext(),"getbranch",Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject object = new JSONObject(response);
                    Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                    UserSessionManager user = new UserSessionManager(getContext());
                    user.setbranch(object.getString("branch"));
                }catch (JSONException error)
                {
                    Toast.makeText(getContext(),"json error" + error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            public Map<String,String> getParams()
            {
                Map<String ,String> params = new HashMap<>();
                params.put("rollno",new UserSessionManager(getContext()).getRollno());
                return  params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
