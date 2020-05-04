package com.example.dapurami;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter_vote extends RecyclerView.Adapter<MyAdapter_vote.MahasiswaViewHolder> {


    private ArrayList<List_data_vote> dataList;
    Context context;

    public MyAdapter_vote(ArrayList<List_data_vote> dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.grid_list_vote, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MahasiswaViewHolder holder, final int position) {
        final Context context = holder.img.getContext();
        final List_data_vote list_data_vote = dataList.get(position);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double hargarupiah= Double.parseDouble(dataList.get(position).getPrice());
        holder.judul_makanan.setText(dataList.get(position).getProduct_name());
        holder.deskripsi.setText(dataList.get(position).getDescription());
        holder.harga.setText(formatRupiah.format((double)hargarupiah));
        holder.stock.setText("(Stock :"+dataList.get(position).getStock()+" )");
        holder.vote.setChecked(list_data_vote.isSelected());
        holder.vote.setTag(dataList.get(position));

        holder.vote.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   String data = "";
                                                   List_data_vote list_data_vote1 = (List_data_vote) holder.vote.getTag();

                                                   list_data_vote1.setSelected(holder.vote.isChecked());

                                                   dataList.get(position).setSelected(holder.vote.isChecked());

                                                   for (int j = 0; j < dataList.size(); j++) {

                                                       if (dataList.get(j).isSelected() == true) {
                                                           data = data + "\n" + dataList.get(j).getProduct_name().toString() + "   " + dataList.get(j).getPrice().toString();
                                                       }
                                                   }
                                                   Toast.makeText(context, "Selected Fruits : \n " + data, Toast.LENGTH_SHORT).show();
                                               }
                                           });



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
        private CheckBox vote;
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
            vote=(CheckBox)itemView.findViewById(R.id.checkbox_vote);
        }
    }
}