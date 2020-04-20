package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class detail_makanan extends AppCompatActivity {
String food_title, image_url, descriptiion, price, stock;
TextView tv_food_title, tv_description, tv_price, tv_stock;
ImageView gambar_makanan;
Button add_to_cart;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_makanan);
        this.context=context;
        Intent intent = getIntent();
        add_to_cart=(Button)findViewById(R.id.btn_addtocart);
         food_title = intent.getExtras().getString("food_title");
        image_url = intent.getExtras().getString("image_url");
        descriptiion = intent.getExtras().getString("deskripsi");
        price = intent.getExtras().getString("harga");
        stock = intent.getExtras().getString("stock");
         tv_food_title=(TextView)findViewById(R.id.nama_makanan_detail);
        tv_description=(TextView)findViewById(R.id.deskripsi_makanan_detail);
        tv_price=(TextView)findViewById(R.id.harga_makanan_detail);
        tv_stock=(TextView)findViewById(R.id.stock_detail);

         gambar_makanan=(ImageView)findViewById(R.id.gambar_makanan);
         load();
         add_to_cart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(getApplicationContext(), image_url, Toast.LENGTH_SHORT).show();
             }
         });
    }

    public void load(){
        tv_food_title.setText(food_title);
        tv_description.setText(descriptiion);
        tv_stock.setText("(Stock: "+stock+" )");
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double hargarupiah= Double.parseDouble(price);
        tv_price.setText(formatRupiah.format((double)hargarupiah));
        Picasso.with(context)
                .load(image_url)
                .into(gambar_makanan);
    }
}
