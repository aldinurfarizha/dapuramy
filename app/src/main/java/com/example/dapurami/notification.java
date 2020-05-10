package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class notification extends AppCompatActivity {
    private ArrayList<notification_model> list_data;
    ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView gridView;
    notification_adapter adapter;
    User user;
    RelativeLayout unvote,progress_style;
    LinearLayout voting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        user = SharedPrefManager.getInstance(this).getUser();
        shimmerRecyclerView=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view);
        gridView=(RecyclerView) findViewById(R.id.gridView_notification);
        list_data=new ArrayList<>();
        shimmerRecyclerView.showShimmerAdapter();
        unvote=(RelativeLayout)findViewById(R.id.unvote);
        voting=(LinearLayout)findViewById(R.id.vote);
        progress_style=(RelativeLayout)findViewById(R.id.progress_style);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cek_notif();
            }
        }, 1000);

    }



    private void getdata2() {

        final String id = Integer.toString(user.getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_NOTIFICATION,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    JSONArray array=jsonObject.getJSONArray("data");
                                    for (int i=0; i<array.length(); i++){
                                        gridView.setVisibility(View.VISIBLE);
                                        JSONObject ob=array.getJSONObject(i);
                                        notification_model listData=new notification_model(ob.getString("id_notification"),ob.getString("message"), ob.getString("tanggal"), ob.getString("id_order"));
                                        list_data.add(listData);
                                    }
                                    adapter=new notification_adapter(list_data);
                                    RecyclerView.LayoutManager layoutManager=(new LinearLayoutManager(notification.this, LinearLayoutManager.VERTICAL, false));

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
                        params.put("id_user", id);

                        return params;
                    }
                };

                VolleySingleton.getInstance(notification.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
    private void cek_notif() {

        final String id = Integer.toString(user.getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CEK_NOTIF,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getdata2();
                                            }
                                        }, 100);
                                        shimmerRecyclerView.hideShimmerAdapter();
                                        voting.setVisibility(View.VISIBLE);
                                        progress_style.setVisibility(View.GONE);
                                    }
                                    else{

                                        unvote.setVisibility(View.VISIBLE);
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

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);

                        return params;
                    }
                };

                VolleySingleton.getInstance(notification.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
}

