package com.example.dapurami;


import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<List_data> {
    ArrayList<List_data> listdata;
    Context context;
    int resource;
    public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<List_data> listdata) {
        super(context, resource, listdata);
        this.listdata=listdata;
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.grid_list,null,true);
        }
        List_data listdata=getItem(position);
        ImageView img=(ImageView)convertView.findViewById(R.id.image_view);
        Picasso.with(context)
                .load(listdata.getImageurl())
                .into(img);


        return convertView;
    }
}