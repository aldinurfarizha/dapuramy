package com.example.dapurami;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cart_adapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<cart_model> recordList;

    public cart_adapter(Context context, int layout, ArrayList<cart_model> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView txtName, txtAge, txtPhone;
        ImageView gbr_cart;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtAge = row.findViewById(R.id.txtAge);
            holder.txtPhone = row.findViewById(R.id.txtPhone);
            holder.gbr_cart = row.findViewById(R.id.gambar_cart);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        cart_model model = recordList.get(i);

        holder.txtName.setText(model.getProduct_name());
        holder.txtAge.setText(model.getQty());
        holder.txtPhone.setText(model.getId_product());
        Picasso.with(context)
                .load(model.getImg())
                .into(holder.gbr_cart);

        return row;
    }
}