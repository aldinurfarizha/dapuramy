package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class register extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 1;
    EditText edtname, edtphone_number, edtpassword, edtaddress;
Button btncreate_account;
LinearLayout btn_identity;
ProgressBar progressBar;
ImageView imageView;
ScrollView form_register;
RelativeLayout progres_style;
    boolean check = true;
    Intent intent ;
    Bitmap bitmap;
    TextView keterangan_foto;
    public  static final int RequestPermissionCode  = 1 ;
    ProgressDialog progressDialog;
    String ImageNameFieldOnServer = "image_name" ;

    String ImagePathFieldOnServer = "image_path" ;
    String GetImageNameFromEditText;
    String ImageUploadPathOnSever ="https://192.168.176.252/apidapuramy/capture_img_upload_to_server.php" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        form_register=(ScrollView)findViewById(R.id.register_form);
        progres_style=(RelativeLayout)findViewById(R.id.progress_style_register);
        edtname=(EditText) findViewById(R.id.input_name);
        edtphone_number=(EditText)findViewById(R.id.input_phone_number);
        edtpassword=(EditText)findViewById(R.id.input_password);
        btncreate_account=(Button)findViewById(R.id.btn_create_account);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        edtaddress=(EditText)findViewById(R.id.address);
        btn_identity=(LinearLayout) findViewById(R.id.btn_identity);
        keterangan_foto=(TextView) findViewById(R.id.keterangan_photo);
        imageView=(ImageView)findViewById(R.id.identity_card);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(register.this, login.class));
            return;
        }
        EnableRuntimePermissionToAccessCamera();
        btncreate_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  registerUser();

                GetImageNameFromEditText= edtname.getText().toString();
                uploadBitmap(bitmap);
            }
        });
btn_identity.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       activeTakePhoto();
    }
});
    }


    private void activeTakePhoto() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(register.this, Manifest.permission.CAMERA))
        {
            Toast.makeText(getApplicationContext(), "You not Give Permission Camera, So Open Application Setting and Give Camera Permission for this Application (Dapuramy)", Toast.LENGTH_SHORT).show();

        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK & null != data) {

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    //to generate random file name
                    String fileName = "tempimg.jpg";

                    try {
                       bitmap = (Bitmap) data.getExtras().get("data");
                        //captured image set in imageview
                        imageView.setImageBitmap(bitmap);


                        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        keterangan_foto.setVisibility(View.GONE);
                        btn_identity.setBackgroundColor(Color.TRANSPARENT);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(register.this,
                Manifest.permission.CAMERA))
        {

        } else {
            ActivityCompat.requestPermissions(register.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadBitmap(final Bitmap bitmap) {

        final String name = edtname.getText().toString().trim();
        final String phone_number = edtphone_number.getText().toString().trim();
        final String password = edtpassword.getText().toString().trim();
        final String address = edtaddress.getText().toString().trim();


        //first we will do the validations

        if (TextUtils.isEmpty(name)) {
            edtname.setError("Please enter name");
            edtname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone_number)) {
            edtphone_number.setError("Please enter your phone number");
            edtphone_number.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtpassword.setError("Enter a password");
            edtpassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtaddress.setError("Enter a Address");
            edtaddress.requestFocus();
            return;
        }
        if(bitmap==null){
            Toast.makeText(getApplicationContext(), "Upload Identity Card Please", Toast.LENGTH_SHORT).show();
            return;
        }
        progres_style.setVisibility(View.VISIBLE);
        form_register.setVisibility(View.GONE);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id_customer"),
                                        userJson.getString("name"),
                                        userJson.getString("phone_number"),
                                        userJson.getString("address"),
                                        userJson.getString("status")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                Intent i = new Intent(register.this,home.class);
                                startActivity(i);
                            } else {
                                progres_style.setVisibility(View.GONE);
                                form_register.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("names", name);
                params.put("phone_number", phone_number);
                params.put("password", password);
                params.put("address", address);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}