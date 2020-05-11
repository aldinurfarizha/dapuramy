package com.example.dapurami;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class profile extends AppCompatActivity {
    EditText name, address, phone_number;
    String s_name, s_address, s_phone_number, s_id_customer;
    private ArrayList<model_profile> models;
    Button btn_save_change;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        models = new ArrayList<>();
        name=(EditText)findViewById(R.id.input_name);
        address=(EditText)findViewById(R.id.input_address);
        phone_number=(EditText)findViewById(R.id.phone_number);
        btn_save_change=(Button)findViewById(R.id.btn_save_change);
        user = SharedPrefManager.getInstance(this).getUser();
        getdata2();
        btn_save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to Change?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       save_change();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

public void save_change(){
    final String f_id_customer = s_id_customer;
    final String f_names = name.getText().toString().trim();
    final String f_address = address.getText().toString().trim();

    if (TextUtils.isEmpty(f_names)) {
        name.setError("Masukan Angka Standmeter");
        name.requestFocus();
        return;
    }
    if (TextUtils.isEmpty(f_address)) {
        address.setError("Masukan Angka Pembelian");
        address.requestFocus();
        return;
    }

    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SAVE_CHANGE_PROFILE,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(profile.this, home.class);
                            startActivity(intent);

                        } else {

                            Toast.makeText(getApplicationContext(), obj.getString("message")+" Try Again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Poor Connection, Try Again", Toast.LENGTH_SHORT).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("id_customer", f_id_customer);
            params.put("name", f_names);
            params.put("address", f_address);
            return params;
        }
    };

    VolleySingleton.getInstance(profile.this).addToRequestQueue(stringRequest);
}
    private void getdata2() {

        final String id = Integer.toString(user.getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_PROFILE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    JSONArray array=jsonObject.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject ob = array.getJSONObject(i);
                                        model_profile listData = new model_profile(ob.getString("id_customer"), ob.getString("name"), ob.getString("address"), ob.getString("phone_number"));
                                        models.add(listData);
                                    }
                                    s_id_customer=models.get(0).getId_customer();
                                    s_phone_number = models.get(0).getPhone_number();
                                    s_name = models.get(0).getName();
                                    s_address = models.get(0).getAddress();

                                    name.setText(s_name);
                                    address.setText(s_address);
                                    phone_number.setText(s_phone_number);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Check Your Connection", Toast.LENGTH_SHORT).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);

                        return params;
                    }
                };

                VolleySingleton.getInstance(profile.this).addToRequestQueue(stringRequest);
            }
        }, 100);

    }




}
