package com.example.dapurami;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class detail_order_adapter extends RecyclerView.Adapter<detail_order_adapter.MahasiswaViewHolder> {


    private ArrayList<detail_order_model> dataList2;
    Context context;

    public detail_order_adapter(ArrayList<detail_order_model> dataList2) {
        this.context = context;
        this.dataList2 = dataList2;

    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_detail_order, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, final int position) {
        final Context context = holder.picture.getContext();
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.product_name.setText(dataList2.get(position).getProduct_name());
        holder.qty.setText("qty :"+dataList2.get(position).getQty());
        Integer price=Integer.valueOf(dataList2.get(position).getPrice());
        Integer qty=Integer.valueOf(dataList2.get(position).getQty());
        Integer price_total=price*qty;
        Double hargarupiah= Double.valueOf(price_total);
        holder.price.setText(formatRupiah.format((double)hargarupiah));
        Picasso.with(context)
                .load(URLs.GET_IMAGE+dataList2.get(position).getPicture())
                .into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return (dataList2 != null) ? dataList2.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView product_name, price, qty;
        private ImageView picture;
        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            picture=(ImageView) itemView.findViewById(R.id.picture_food);
            product_name=(TextView)itemView.findViewById(R.id.product_name);
            price=(TextView)itemView.findViewById(R.id.price);
            qty=(TextView)itemView.findViewById(R.id.qty);
        }
    }
}