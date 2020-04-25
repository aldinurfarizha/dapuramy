package com.example.dapurami;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import static com.example.dapurami.splash_screen.mSQLiteHelper;

public class detail_makanan extends AppCompatActivity {
String food_title, image_url, descriptiion, price, stock, id_product;
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
        id_product = intent.getExtras().getString("id_product");
         tv_food_title=(TextView)findViewById(R.id.nama_makanan_detail);
        tv_description=(TextView)findViewById(R.id.deskripsi_makanan_detail);
        tv_price=(TextView)findViewById(R.id.harga_makanan_detail);
        tv_stock=(TextView)findViewById(R.id.stock_detail);

         gambar_makanan=(ImageView)findViewById(R.id.gambar_makanan);
         load();
         add_to_cart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 LayoutInflater inflater = (LayoutInflater) detail_makanan.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 final View formsView = inflater.inflate(R.layout.qty, null, false);
                 final EditText qtyev = (EditText) formsView.findViewById(R.id.qty);

                 new AlertDialog.Builder(detail_makanan.this)
                         .setView(formsView)
                         .setTitle("Masukan Jumlah Pemesanan")
                         .setPositiveButton("OK",
                                 new DialogInterface.OnClickListener() {
                                     @TargetApi(11)
                                     public void onClick(
                                             DialogInterface dialog, int id) {
                                         String qty = qtyev.getText().toString();
                                         Integer qtya=Integer.valueOf(qtyev.getText().toString());
                                         Integer pricea=Integer.valueOf(price);
                                         final Integer price_final=pricea*qtya;
                                         try {
                                             mSQLiteHelper.insertData(
                                                     id_product,
                                                     id_product.trim(),
                                                     food_title.trim(),
                                                     qty.trim(),
                                                     image_url.trim(),
                                                     price_final.toString().trim()
                                             );
                                             Toast.makeText(detail_makanan.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                             //reset views

                                         }
                                         catch (Exception e){
                                             e.printStackTrace();
                                         }

                                         dialog.cancel();
                                     }
                                 }).show();


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
