package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
EditText edtname, edtphone_number, edtpassword, edtaddress;
Button btncreate_account;
ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtname=(EditText) findViewById(R.id.input_name);
        edtphone_number=(EditText)findViewById(R.id.input_phone_number);
        edtpassword=(EditText)findViewById(R.id.input_password);
        btncreate_account=(Button)findViewById(R.id.btn_create_account);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        edtaddress=(EditText)findViewById(R.id.address);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(register.this, login.class));
            return;
        }
        btncreate_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }



    private void registerUser() {
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

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
                                Intent i = new Intent(register.this,home.class);
                                startActivity(i);
                            } else {

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone_number", phone_number);
                params.put("password", password);
                params.put("address", address);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
