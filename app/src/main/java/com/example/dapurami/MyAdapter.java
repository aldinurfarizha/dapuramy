package com.example.dapurami;


import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MahasiswaViewHolder> {


    private ArrayList<List_data> dataList;
    Context context;

    public MyAdapter(ArrayList<List_data> dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.grid_list, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, final int position) {
        final Context context = holder.img.getContext();
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double hargarupiah= Double.parseDouble(dataList.get(position).getPrice());
        holder.judul_makanan.setText(dataList.get(position).getProduct_name());
        holder.deskripsi.setText(dataList.get(position).getDescription());
        holder.harga.setText(formatRupiah.format((double)hargarupiah));
        holder.stock.setText("(Stock :"+dataList.get(position).getStock()+" )");




        Picasso.with(context)
                .load(URLs.GET_IMAGE+dataList.get(position).getPicture())
                .into(holder.img);




        holder.food_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), detail_makanan.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                final String food_title = dataList.get(position).getProduct_name();
                final String image_url = URLs.GET_IMAGE+dataList.get(position).getPicture();
                final String deskripsi = dataList.get(position).getDescription();
                final String harga = dataList.get(position).getPrice();
                final String stock= dataList.get(position).getStock();
                final String id_product= dataList.get(position).getId_product();

                intent.putExtra("food_title", food_title);
                intent.putExtra("image_url", image_url);
                intent.putExtra("deskripsi", deskripsi);
                intent.putExtra("harga",harga);
                intent.putExtra("stock",stock);
                intent.putExtra("id_product",id_product);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView judul_makanan, deskripsi, stock, harga;
        private Button addtocart;
        private CardView food_item;
        private ImageView img;
        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            food_item=(CardView)itemView.findViewById(R.id.food_item);
            judul_makanan=(TextView)itemView.findViewById(R.id.judul_makanan);
            deskripsi=(TextView)itemView.findViewById(R.id.deskripsi_makanan);
            harga=(TextView)itemView.findViewById(R.id.harga_makanan);
            stock=(TextView)itemView.findViewById(R.id.stock);
            img=(ImageView)itemView.findViewById(R.id.image_view);
        }
    }
}