package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
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

public class  HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static  String t1,t2;
    UserSessionManager userSessionManager;
    Toolbar toolbar;
    int i = 0;
    TextView s_name,s_rollno;

    ListView listView;
    NoticeAdapter noticeAdapter;
    List<Notice> noticeList = new ArrayList<Notice>();
    ProgressDialog pDialog;
    FragmentManager mFragmentManager;

    FragmentTransaction mFragmentTransaction;
    final String url = new URLConstants().BaseURL + "getnoticess.php";

    public Context instance() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        userSessionManager = new UserSessionManager(this);
      // s_name = (TextView) findViewById(R.id.stu_name);
       // s_rollno = (TextView) findViewById(R.id.stu_rollno);
        //s_rollno.setText(LoginScreen.rollno);
        //s_name.setText(LoginScreen.name);
        // getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        //Intent intent = this.getIntent();
        //Toast.makeText(getApplicationContext(),intent.getStringExtra("msg"),Toast.LENGTH_LONG).show();
////////////////////////////////////////////////////////////////////////////*
       // userSessionManager = new UserSessionManager(this);
       // userSessionManager.checklogin();
///////////////////////////////////////////


        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new HomeFragment()).commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notice Board");
        setSupportActionBar(toolbar);
 ////////////////////////////////////////
        //listView = (ListView) findViewById(R.id.noticeList);
        //noticeAdapter = new NoticeAdapter(this, noticeList);
        //listView.setAdapter(noticeAdapter);
/////////////////////////////////////////

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //navigationView.addHeaderView(header);
        s_name = (TextView) header.findViewById(R.id.stu_name);
        s_name.setText(LoginScreen.name);
        s_rollno = (TextView) header.findViewById(R.id.stu_rollno);
        s_rollno.setText(LoginScreen.rollno);
        invalidateOptionsMenu();
        //////////////////////this.getNoticess(20);
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             Notice obj = noticeList.get(position);
                //Toast.makeText(getApplicationContext(),obj.getTitle(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),NoticeView.class);
                intent.putExtra("id",obj.getId());
                startActivity(intent);
            }
        });*/////////////////////////////////////////////////////////

    }

    public void getNoticess(final int count) {
        pDialog = new ProgressDialog(this);
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
                    Toast.makeText(getApplicationContext(), "json error " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
                noticeAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("count", "" + count);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (i == 0) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                i++;
            } else {
                i--;
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nb) {
            toolbar.setTitle("Notice Board");
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new HomeFragment()).commit();
        } else if (id == R.id.change_password) {
            Intent intent = new Intent(this, Settings.class);
            intent.putExtra("extra", "password");
            startActivity(intent);

        } else if (id == R.id.update_photo) {
            Intent intent = new Intent(this, Settings.class);
            intent.putExtra("extra", "upload");
            startActivity(intent);
        } else if (id == R.id.nav_share) {

            toolbar.setTitle("Apply YBD");
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new YbdFragment()).commit();
        } else if (id == R.id.logout) {
            //GCM gcm = new GCM();
            //gcm.unregisterUser();
            this.unRegisterUser();
            userSessionManager.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void unRegisterUser() {
        SharedPreferences pref;
        MyApplication myApplication = new MyApplication();
        pref = myApplication.getSharedPreference(this);

        final String f = pref.getString("regid", "");
        final String rollno = userSessionManager.getRollno();
        Toast.makeText(this,f+rollno,Toast.LENGTH_SHORT).show();
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
            MySingleton.getInstance(this).addToRequestQueue(request);


        }
    }

   /** @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //navigationView.addHeaderView(header);
        s_name = (TextView) header.findViewById(R.id.stu_name);
        s_name.setText(LoginScreen.name);
        s_rollno = (TextView) header.findViewById(R.id.stu_rollno);
        s_rollno.setText(LoginScreen.rollno);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public void onResume()
    {
        super.onResume();
        s_name.setText(userSessionManager.getname());
        //s_rollno = (TextView) header.findViewById(R.id.stu_rollno);
        s_rollno.setText(userSessionManager.getRollno());
        //Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
       MsgSharedPreference obj = new MsgSharedPreference(this);
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
