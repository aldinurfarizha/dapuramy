package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vooting extends AppCompatActivity {
    private ArrayList<List_data_vote> list_data;
    ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView gridView;
    MyAdapter_vote adapter;
    List<String> list;
    Button btn_vote;
    String vote;
    User user;
    RelativeLayout unvote,progress_style;
    LinearLayout voting;
    AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vooting);
        user = SharedPrefManager.getInstance(this).getUser();
        shimmerRecyclerView=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view);
        gridView=(RecyclerView) findViewById(R.id.gridView);
        list_data=new ArrayList<>();
        shimmerRecyclerView.showShimmerAdapter();
        unvote=(RelativeLayout)findViewById(R.id.unvote);
        voting=(LinearLayout)findViewById(R.id.vote);
        btn_vote=(Button)findViewById(R.id.btn_vote_now);
        progress_style=(RelativeLayout)findViewById(R.id.progress_style);
        avi=(AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.show();
        cek_vote();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        btn_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vote==null){
                    Toast.makeText(Vooting.this,"Select Product Before Vote" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                list = Arrays.asList(vote.split("\\|", -1));
                if(list.size()<=0){
                    Toast.makeText(Vooting.this,"Please Select Product Minimum 1" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(list.size()>=5){
                    Toast.makeText(Vooting.this,"Only Allowed for 3 Product !" ,Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Vooting.this,"Please Wait" ,Toast.LENGTH_SHORT).show();
                for(int x = 0; x<list.size()-1; x++){

                    final String id_customer= String.valueOf(user.getId());
                    final String product= list.get(x);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_VOTE_NOW,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        //converting response to json object
                                        JSONObject obj = new JSONObject(response);

                                        //if no error in response
                                        if (!obj.getBoolean("error")) {



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
                            params.put("id_customer", id_customer );
                            params.put("id_product", product);
                            return params;
                        }
                    };

                    VolleySingleton.getInstance(Vooting.this).addToRequestQueue(stringRequest);
                    //Log.e("Array", list.get(x));
               }

                Toast.makeText(getApplicationContext(), "Vote Succses, Hope your favorit product display Soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Vooting.this, home.class);
                startActivity(intent);
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            vote = intent.getStringExtra("vote");

        }

    };
    private void cek_vote() {

        final String id = Integer.toString(user.getId());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CEK_VOTE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject=new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getData();
                                            }
                                        }, 1750);
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

                VolleySingleton.getInstance(Vooting.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, URLs.URL_GET_FOR_VOTE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++){
                        shimmerRecyclerView.hideShimmerAdapter();
                        gridView.setVisibility(View.VISIBLE);
                        JSONObject ob=array.getJSONObject(i);
                        List_data_vote listData=new List_data_vote(ob.getString("id_product"),ob.getString("product_name"), ob.getString("description"), ob.getString("stock"), ob.getString("price"), ob.getString("picture"));
                        list_data.add(listData);
                    }
                    adapter=new MyAdapter_vote(list_data);
                    RecyclerView.LayoutManager layoutManager=(new GridLayoutManager(Vooting.this, 2));

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

