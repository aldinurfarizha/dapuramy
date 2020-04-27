package com.example.dapurami;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class cart extends AppCompatActivity {
    ListView mListView;
    ArrayList<cart_model> mList;
    cart_adapter mAdapter = null;
    Integer price_total = 0;
    TextView price_txt;
    Button checkout;
    ImageView empty_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new cart_adapter(this, R.layout.cart_list, mList);
        mListView.setAdapter(mAdapter);
        price_txt = (TextView) findViewById(R.id.txt_price_total);
        checkout = (Button) findViewById(R.id.btn_checkout);
        empty_cart=(ImageView)findViewById(R.id.empty_cart_iv);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    public void update_product(){
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
            Log.d("jancoe", "update_product:"+price_total_rp);
        }
    }
}
