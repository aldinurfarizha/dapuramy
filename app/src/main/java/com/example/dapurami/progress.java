package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class progress extends AppCompatActivity {
User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        user = SharedPrefManager.getInstance(this).getUser();
        refresh_account();
    }
    public void refresh_account(){
        final String id = Integer.toString(user.getId());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REFRESH_PROFIL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.getBoolean("error")) {
                                        JSONObject userJson = obj.getJSONObject("user");
                                        User user = new User(
                                                userJson.getInt("id_customer"),
                                                userJson.getString("name"),
                                                userJson.getString("phone_number"),
                                                userJson.getString("address"),
                                                userJson.getString("status")
                                        );
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                                Intent intent=new Intent(progress.this, home.class);
                                                startActivity(intent);
                                            }
                                        }, 1000);

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
                                finish();
                                Intent intent=new Intent(progress.this, home.class);
                                startActivity(intent);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);

                        return params;
                    }
                };

                VolleySingleton.getInstance(progress.this).addToRequestQueue(stringRequest);
            }
        }, 100);
    }
}
