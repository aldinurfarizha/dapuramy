package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dapurami.ui.home.HomeFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
Button login;
EditText phonenumber, password;
ProgressBar progressBar;
ProgressDialog progressDialog;
AVLoadingIndicatorView avi;
ImageView back;
TextView register;
RelativeLayout progress_style;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User user = SharedPrefManager.getInstance(this).getUser();

        login=(Button) findViewById(R.id.btn_login);
        phonenumber=(EditText)findViewById(R.id.input_phone_number);
        password=(EditText)findViewById(R.id.input_phone_password);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        progressDialog = new ProgressDialog(login.this);
        register=(TextView)findViewById(R.id.register);
        back=(ImageView)findViewById(R.id.btn_back);
        progress_style=(RelativeLayout)findViewById(R.id.progress_style);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), after_splash.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), register.class));
            }
        });
        avi=(AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.show();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, home.class));
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });


    }

    private void userLogin() {
        //first getting the values
        final String phone_number = phonenumber.getText().toString();
        final String phone_password = password.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(phone_number)) {
            phonenumber.setError("Please enter your Phone Number");
            phonenumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone_password)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }

        progress_style.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                login.setVisibility(View.GONE);

                                try {
                                    //converting response to json object
                                    JSONObject obj = new JSONObject(response);

                                    //if no error in response
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
                                        startActivity(new Intent(getApplicationContext(), home.class));
                                    } else {
                                        login.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        progress_style.setVisibility(View.GONE);



                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Check Your Connection", Toast.LENGTH_SHORT).show();
                                progress_style.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("phone_number", phone_number);
                        params.put("password", phone_password);
                        return params;
                    }
                };

                VolleySingleton.getInstance(login.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }

}
