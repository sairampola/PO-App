package com.placementoffice.hemanthreddy.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout layout_password,layout_upload;

    EditText pass1,pass2,oldpassword;

    CheckBox show_password;

    Button change_password,browse,upload;

    ProgressDialog progressDialog;

    private ImageView imageView;
    private Bitmap bitmap;

    Toolbar toolbar;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL = new URLConstants().BaseURL+"change_photo.php";
    String change_password_url = new URLConstants().BaseURL+"change_password.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        layout_password = (RelativeLayout) findViewById(R.id.layout_change_password);
        layout_upload = (RelativeLayout) findViewById(R.id.upload_photo);
        browse = (Button) findViewById(R.id.browse);
        browse.setOnClickListener(this);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(intent);

            }
        });

        Intent intent = getIntent();
        String extra = intent.getStringExtra("extra");

        if(extra.equals("upload"))
        {
            toolbar.setTitle("Upload Photo");
            layout_password.setVisibility(View.GONE);
            layout_upload.setVisibility(View.VISIBLE);
        }
        else {
            toolbar.setTitle("Change Password");
            layout_password.setVisibility(View.VISIBLE);
            layout_upload.setVisibility(View.GONE);
        }


        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        oldpassword = (EditText) findViewById(R.id.oldpassword);

        show_password = (CheckBox) findViewById(R.id.show_password);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    pass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    oldpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    oldpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });

        change_password = (Button) findViewById(R.id.submit);
        change_password.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        imageView = (ImageView) findViewById(R.id.imageView2);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit)
        {
            if(TextUtils.isEmpty(pass1.getText().toString()))
            {
                pass1.setError("Field cant be empty");
                return;
            }
            if(TextUtils.isEmpty(pass2.getText().toString()))
            {
                pass2.setError("Field cant be empty");
                return;
            }
            if(!pass1.getText().toString().equals(pass2.getText().toString()))
            {
                pass2.setError("password doesn't match");
                return;
            }
            UserSessionManager obj = new UserSessionManager(this);
            if(obj.getPassword().equals(pass2))
            {
                oldpassword.setError("Incorrect Password");
                return;
            }
            change_Password(obj.getRollno(), pass1.getText().toString());
        }
        else if(v.getId() == R.id.browse)
        {
            this.showFileChooser();
        }
        else if(v.getId() == R.id.upload)
        {
            this.uploadImage();
        }
    }

    private void change_Password(final String rollno, final String password) {

        progressDialog.setMessage("Changing Password...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, change_password_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if(error)
                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error_details"),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password Change Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
                        startActivity(intent);
                    }
                }catch (JSONException error)
                {
                    Toast.makeText(getApplicationContext(),"error JSON "+error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"error Volley "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String, String> getParams()
            {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("rollno",rollno);
                params.put("password",password);
                return  params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
