package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class your_order extends AppCompatActivity {
    private ArrayList<order_model> list_data;
    ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView gridView;
    order_adapter adapter;
    User user;
    AVLoadingIndicatorView avi;
    RelativeLayout no_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharedPrefManager.getInstance(this).getUser();
        setContentView(R.layout.activity_your_order);
        gridView=(RecyclerView) findViewById(R.id.gridView_order);
        avi=(AVLoadingIndicatorView)findViewById(R.id.avi);
        no_order=(RelativeLayout)findViewById(R.id.no_order);
        list_data=new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getdata2();
            }
        }, 1750);

    }



    private void getdata2() {

        final String id = Integer.toString(user.getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_YOUR_ORDER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        JSONArray array=jsonObject.getJSONArray("data");
                                        for (int i=0; i<array.length(); i++){
                                            avi.setVisibility(View.GONE);
                                            gridView.setVisibility(View.VISIBLE);
                                            JSONObject ob=array.getJSONObject(i);
                                            order_model listData=new order_model(ob.getString("id_order"),ob.getString("price_total"), ob.getString("method"), ob.getString("order_date"), ob.getString("status_order"));
                                            list_data.add(listData);
                                        }
                                        adapter=new order_adapter(list_data);
                                        RecyclerView.LayoutManager layoutManager=(new LinearLayoutManager(your_order.this, LinearLayoutManager.VERTICAL, false));
                                        gridView.setLayoutManager(layoutManager);
                                        gridView.setAdapter(adapter);

                                    } else {
                                        avi.setVisibility(View.GONE);
                                        no_order.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                getdata2();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);

                        return params;
                    }
                };

                VolleySingleton.getInstance(your_order.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
}

