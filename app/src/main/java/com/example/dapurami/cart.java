package com.example.dapurami;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.dapurami.splash_screen.mSQLiteHelper;


public class cart extends AppCompatActivity {
    ListView mListView;
    ArrayList<cart_model> mList;
    cart_adapter mAdapter = null;
    Integer price_total = 0;
    TextView price_txt;
    Button checkout;
    ImageView empty_cart;
    String method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        final User user = SharedPrefManager.getInstance(this).getUser();
        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new cart_adapter(this, R.layout.cart_list, mList);
        mListView.setAdapter(mAdapter);
        price_txt = (TextView) findViewById(R.id.txt_price_total);
        checkout = (Button) findViewById(R.id.btn_checkout);
        empty_cart = (ImageView) findViewById(R.id.empty_cart_iv);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = splash_screen.mSQLiteHelper.getData("SELECT * FROM cart");
                int total=cursor.getCount();
                String status= user.getStatus();
                String verified="VERIFIED";
                if(status.equals(verified)){
                    if(total==0){
                        Toast.makeText(cart.this, "Order some Food or Drink before checkout ;)", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        LayoutInflater inflater = (LayoutInflater) cart.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View formsView = inflater.inflate(R.layout.payment_method, null, false);
                        final RadioGroup rg = (RadioGroup) formsView.findViewById(R.id.rg);

                        new AlertDialog.Builder(cart.this)
                                .setView(formsView)
                                .setTitle("Select Payment Method")
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @TargetApi(11)
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                int pilih = rg.getCheckedRadioButtonId();
                                                RadioButton radio_selected = (RadioButton) formsView.findViewById(pilih);
                                                String payment_selected=radio_selected.getText().toString();
                                                place_transaction(payment_selected);

                                                dialog.cancel();
                                            }
                                        }).show();
                    }

                }
                else{
                    Toast.makeText(cart.this, "Your Account is Not Verified ;)", Toast.LENGTH_SHORT).show();
                }



            }
        });
       get_product();
    }




    public void get_product() {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Cursor cursor = splash_screen.mSQLiteHelper.getData("SELECT * FROM cart");
        mList.clear();
        while (cursor.moveToNext()) {
            int id_order = cursor.getInt(0);
            String id_product = cursor.getString(1);
            String product_name = cursor.getString(2);
            String qty = cursor.getString(3);
            String img = cursor.getString(4);
            String price = cursor.getString(5);
            int price_slice = Integer.valueOf(cursor.getString(5));
            price_total = price_total + price_slice;
            //add to list
            mList.add(new cart_model(id_order, id_product, product_name, qty, img, price));
        }

        mAdapter.notifyDataSetChanged();
        if (mList.size() == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Cart is Empty ;(", Toast.LENGTH_SHORT).show();
        } else {
            empty_cart.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            Double price_total_rp = Double.valueOf(price_total);
            price_txt.setText(formatRupiah.format(price_total_rp));
        }
    }

    public void update_product() {
        mList = new ArrayList<>();
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Cursor cursor = splash_screen.mSQLiteHelper.getData("SELECT * FROM cart");
        mList.clear();
        while (cursor.moveToNext()) {

            int id_order = cursor.getInt(0);
            String id_product = cursor.getString(1);
            String product_name = cursor.getString(2);
            String qty = cursor.getString(3);
            String img = cursor.getString(4);
            String price = cursor.getString(5);
            int price_slice = Integer.valueOf(cursor.getString(5));
            price_total = price_total + price_slice;
            //add to list
            mList.add(new cart_model(id_order, id_product, product_name, qty, img, price));
        }

        if (mList.size() == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Your Cart is Empty ;(", Toast.LENGTH_SHORT).show();
        } else {
            empty_cart.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            Double price_total_rp = Double.valueOf(price_total);
            Log.d("jancoe", "update_product:" + price_total_rp);
        }
    }

    public void place_order() {
        Cursor cursor = splash_screen.mSQLiteHelper.getData("SELECT * FROM cart");
        while (cursor.moveToNext()) {
        final String id_product = cursor.getString(1);
        final String qty = cursor.getString(3);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PLACE_ORDER,
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
                params.put("id_product", id_product);
                params.put("qty", qty);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
            splash_screen.mSQLiteHelper.deleteallData();
            Toast.makeText(getApplicationContext(), "Order Has been placed, please wait for next notify", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(cart.this, home.class);
            startActivity(intent);

    }


    }

    public void place_transaction(String payment_selected){
        User user = SharedPrefManager.getInstance(this).getUser();
        final String id_customer= String.valueOf(user.getId());
        final String prct=String.valueOf(price_total);
        final String methode= payment_selected;
       // final String method=;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PLACE_TRANSACTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        place_order();
                                    }
                                }, 1000);

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
                params.put("id_customer", id_customer);
                params.put("price_total", prct);
                params.put("method", methode);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }
    }

