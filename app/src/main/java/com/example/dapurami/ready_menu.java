package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ready_menu extends AppCompatActivity {
    private ArrayList<List_data> list_data;
    ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView gridView;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        shimmerRecyclerView=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view);
        gridView=(RecyclerView) findViewById(R.id.gridView);
        list_data=new ArrayList<>();
        shimmerRecyclerView.showShimmerAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1750);

    }

    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, URLs.URL_GET_ALL_READY_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++){
                        shimmerRecyclerView.hideShimmerAdapter();
                        gridView.setVisibility(View.VISIBLE);
                        JSONObject ob=array.getJSONObject(i);
                        List_data listData=new List_data(ob.getString("id_product"),ob.getString("product_name"), ob.getString("description"), ob.getString("stock"), ob.getString("price"), ob.getString("picture"));
                        list_data.add(listData);
                    }
                    adapter=new MyAdapter(list_data);
                    RecyclerView.LayoutManager layoutManager=(new GridLayoutManager(ready_menu.this, 2));

                    gridView.setLayoutManager(layoutManager);

                    gridView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 500);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } //ambil list maknan
}
