package com.example.dapurami;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class notification_adapter extends RecyclerView.Adapter<notification_adapter.MahasiswaViewHolder> {


    private ArrayList<notification_model> dataList2;
    Context context;

    public notification_adapter(ArrayList<notification_model> dataList2) {
        this.context = context;
        this.dataList2 = dataList2;

    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_notification, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, final int position) {
        //final Context context = holder.order_number.getContext();
        holder.description.setText(dataList2.get(position).getMessage());
        holder.date.setText(dataList2.get(position).getTanggal());
        holder.order_number.setText("Order Number :"+dataList2.get(position).getId_order());




    }

    @Override
    public int getItemCount() {
        return (dataList2 != null) ? dataList2.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView description, date, order_number;
        private CardView item_order;
        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            item_order=(CardView)itemView.findViewById(R.id.item_order);
            description=(TextView)itemView.findViewById(R.id.notif_description);
            date=(TextView)itemView.findViewById(R.id.notif_date);
            order_number=(TextView)itemView.findViewById(R.id.order_number);


        }
    }
}