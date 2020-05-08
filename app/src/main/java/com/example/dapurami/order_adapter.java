package com.example.dapurami;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static java.security.AccessController.getContext;

public class order_adapter extends RecyclerView.Adapter<order_adapter.MahasiswaViewHolder> {


    private ArrayList<order_model> dataList2;
    Context context;

    public order_adapter(ArrayList<order_model> dataList2) {
        this.context = context;
        this.dataList2 = dataList2;

    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_order, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, final int position) {
        final Context context = holder.order_number.getContext();
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double hargarupiah= Double.parseDouble(dataList2.get(position).getPrice_total());
        holder.order_number.setText(dataList2.get(position).getId());
        holder.payment_method.setText(dataList2.get(position).getMethod());
        holder.order_date.setText(dataList2.get(position).getOrder_date());
        String status=dataList2.get(position).getStatus_order();
        switch (Integer.valueOf(status)){
            case 0:
                holder.status.setText("Order Has Been Placed");
                break;
            case 1:
                holder.status.setText("Order in Procsess");
                break;
        }

        holder.price_total.setText(formatRupiah.format((double)hargarupiah));

        holder.item_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), detail_order.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                final String id_order= dataList2.get(position).getId();
                final String total_harga= dataList2.get(position).getPrice_total();
                final String status=dataList2.get(position).getStatus_order();
                final String payment_method=dataList2.get(position).getMethod();
                final String orderdate=dataList2.get(position).getOrder_date();
                intent.putExtra("id_order",id_order);
                intent.putExtra("total_harga",total_harga);
                intent.putExtra("status",status);
                intent.putExtra("method", payment_method);
                intent.putExtra("order_date",orderdate);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList2 != null) ? dataList2.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView order_number, price_total, payment_method, order_date,status;
        private CardView item_order;
        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            item_order=(CardView)itemView.findViewById(R.id.item_order);
            order_number=(TextView)itemView.findViewById(R.id.order_number);
            price_total=(TextView)itemView.findViewById(R.id.price_total);
            payment_method=(TextView)itemView.findViewById(R.id.method);
            order_date=(TextView)itemView.findViewById(R.id.order_date);
            status=(TextView)itemView.findViewById(R.id.status_order);

        }
    }
}