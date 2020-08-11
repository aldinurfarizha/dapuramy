package com.example.dapurami;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class detail_order extends AppCompatActivity {
TextView id_order, total_harga, orderdate;
String order_number, price_total, stts, payment_method,order_date;
Button back, btn_cancle;
    private ArrayList<detail_order_model> list_data;
    ShimmerRecyclerView shimmerRecyclerView;
    private RecyclerView gridView;
    detail_order_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        id_order=(TextView)findViewById(R.id.order_number);
        total_harga=(TextView)findViewById(R.id.total);
        back=(Button)findViewById(R.id.back_order);
        orderdate=(TextView)findViewById(R.id.order_date);
        Intent intent = getIntent();
        order_number = intent.getExtras().getString("id_order");
        stts = intent.getExtras().getString("status");
        price_total = intent.getExtras().getString("total_harga");
        payment_method = intent.getExtras().getString("method");
        order_date = intent.getExtras().getString("order_date");
        id_order.setText("Order Number: "+order_number);
        btn_cancle=(Button)findViewById(R.id.btn_cancle);
        orderdate.setText(order_date);
        if(stts.equals("0")){
            btn_cancle.setVisibility(View.VISIBLE);
        }
        else{
            btn_cancle.setVisibility(View.GONE);
        }
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(detail_order.this);
                builder.setTitle("Are You Sure Cancle this Order ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       cancle_order();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            cancle_order();
            }
        });
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double hargarupiah= Double.parseDouble(price_total);
        total_harga.setText(formatRupiah.format((double)hargarupiah));
        switch (payment_method){
            case "COD":
                cod_method();
            break;
            case "CASH":
                cash_method();
                break;
            case "TRANSFER":
                transfer_method();
                break;

        }
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void cod_method(){
        String[] descriptionData = {"Order Has\n Been Placed", "Order in Proccess", "Order In Delivery"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setMaxStateNumber(StateProgressBar.StateNumber.THREE);
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
        switch (stts){
            case "0":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                break;
            case "1":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case "2":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
        }



    }

    public  void cash_method(){
        String[] descriptionData = {"Order Has\n Been Placed", "Order in Proccess", "Order Is Ready"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setMaxStateNumber(StateProgressBar.StateNumber.THREE);
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
        switch (stts){
            case "0":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                break;
            case "1":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case "3":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
        }
    }

    public void transfer_method(){
        String[] descriptionData = {"Order Has\n Been Placed","Transfered\n Verification", "Order In\n Proccess", "Order In\n Delivery"};
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setMaxStateNumber(StateProgressBar.StateNumber.FOUR);
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
        switch (stts){
            case "0":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                break;
            case "5":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case "1":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case "2":
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
        }
    }

    private void getdata2() {

        final String id_order = order_number;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_DETAIL_TRANSACSTION,
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
                                        detail_order_model listData=new detail_order_model(ob.getString("id_product"),ob.getString("product_name"), ob.getString("price"), ob.getString("picture"), ob.getString("qty"));
                                        list_data.add(listData);
                                    }
                                    adapter=new detail_order_adapter(list_data);
                                    RecyclerView.LayoutManager layoutManager=(new LinearLayoutManager(detail_order.this, LinearLayoutManager.VERTICAL, false));

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
                        params.put("id_order", id_order);

                        return params;
                    }
                };

                VolleySingleton.getInstance(detail_order.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
    private void cancle_order() {

        final String id_order = order_number;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CANCLE_ORDER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            finish();
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
                        params.put("id_order", id_order);

                        return params;
                    }
                };

                VolleySingleton.getInstance(detail_order.this).addToRequestQueue(stringRequest);
            }
        }, 1000);

    }
}
