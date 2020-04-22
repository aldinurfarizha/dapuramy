package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class cart extends AppCompatActivity {
    ListView mListView;
    ArrayList<cart_model> mList;
    cart_adapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new cart_adapter(this, R.layout.cart_list, mList);
        mListView.setAdapter(mAdapter);

        //get all data from sqlite
        Cursor cursor = splash_screen.mSQLiteHelper.getData("SELECT * FROM cart");
        mList.clear();
        while (cursor.moveToNext()){
            int id_order = cursor.getInt(0);
            String id_product = cursor.getString(1);
            String product_name = cursor.getString(2);
            String qty = cursor.getString(3);
            String img= cursor.getString(4);

            //add to list
            mList.add(new cart_model(id_order, id_product, product_name, qty,img ));
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size()==0){
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No record found...", Toast.LENGTH_SHORT).show();
        }
    }
}
