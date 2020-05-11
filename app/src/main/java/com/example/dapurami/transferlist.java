package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class transferlist extends AppCompatActivity {
    private ArrayList<transfer_model> list_data;
    ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView gridView;
    transfer_adapter adapter;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = SharedPrefManager.getInstance(this).getUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferlist);
        shimmerRecyclerView=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view);
        gridView=(RecyclerView) findViewById(R.id.gridView_order);
        list_data=new ArrayList<>();
        shimmerRecyclerView.showShimmerAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getdata2();
            }
        }, 1000);

    }



    private void getdata2() {

        final String id = Integer.toString(user.getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_TRANSFER_LIST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    JSONArray array=jsonObject.getJSONArray("data");
                                    for (int i=0; i<array.length(); i++){
                                        shimmerRecyclerView.hideShimmerAdapter();
                                        gridView.setVisibility(View.VISIBLE);
                                        JSONObject ob=array.getJSONObject(i);
                                        transfer_model listData=new transfer_model(ob.getString("id_order"),ob.getString("price_total"), ob.getString("img"));
                                        list_data.add(listData);
                                    }
                                    adapter=new transfer_adapter(list_data);
                                    RecyclerView.LayoutManager layoutManager=(new LinearLayoutManager(transferlist.this, LinearLayoutManager.VERTICAL, false));

                                    gridView.setLayoutManager(layoutManager);

                                    gridView.setAdapter(adapter);
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

                VolleySingleton.getInstance(transferlist.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
}

