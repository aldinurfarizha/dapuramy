package com.example.dapurami;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class transfer_adapter extends RecyclerView.Adapter<transfer_adapter.MahasiswaViewHolder> {


    private ArrayList<transfer_model> dataList2;
    Context context;

    public transfer_adapter(ArrayList<transfer_model> dataList2) {
        this.context = context;
        this.dataList2 = dataList2;

    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_transfer, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, final int position) {
        final Context context = holder.order_number.getContext();
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double hargarupiah= Double.parseDouble(dataList2.get(position).getTotal());
        holder.order_number.setText(dataList2.get(position).getId_order());
        final String status=dataList2.get(position).getUpload();
        if (status.isEmpty()){
            holder.status.setText("Need Upload Transfer Picture");
        }
        else{
            holder.status.setText("Upload Succsess, Wait for next notify");
        }

        holder.price_total.setText(formatRupiah.format((double)hargarupiah));

        holder.item_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.isEmpty()){
                    Intent intent = new Intent(context.getApplicationContext(), upload_transfer.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    final String id_order= dataList2.get(position).getId_order();
                    intent.putExtra("id_order",id_order);
                    context.startActivity(intent);
                }
                else{

                    Toast.makeText(context, "You have Upload Transfer Pic, Please Wait For next Notify", Toast.LENGTH_SHORT).show();
                }

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
            status=(TextView)itemView.findViewById(R.id.status_order);

        }
    }
}