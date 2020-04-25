package com.example.dapurami;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.dapurami.splash_screen.mSQLiteHelper;

public class cart_adapter extends BaseAdapter {
    public static cart cart;
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
        TextView txtproduct, txtqty, txtprice;
        ImageView gbr_cart, delete_product;
        Context context;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        final SQLiteHelper sqLiteHelper = null;
        final String id_product;
        View row = view;
        this.context=context;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtproduct = row.findViewById(R.id.txt_product);
            holder.txtqty = row.findViewById(R.id.txt_qty);
            holder.txtprice = row.findViewById(R.id.txt_price);
            holder.gbr_cart = row.findViewById(R.id.gambar_cart);
            holder.delete_product = row.findViewById(R.id.delete_cart_product);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }
        final cart_model model = recordList.get(i);
        id_product=model.getId_product();
        holder.txtproduct.setText(model.getProduct_name());
        holder.txtqty.setText(model.getQty());
        Double price_rp=Double.valueOf(model.getPrice());
        holder.txtprice.setText(formatRupiah.format(price_rp));
        Picasso.with(context)
                .load(model.getImg())
                .into(holder.gbr_cart);
        holder.delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id=Integer.valueOf(id_product);
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mSQLiteHelper.deleteData(id);
                                    Toast tea = Toast.makeText(context.getApplicationContext(), "Succsess Deleted ", Toast.LENGTH_LONG);
                                    tea.show();
                                    cart.mList.remove(recordList.get(i));
                                    cart.mAdapter.notifyDataSetChanged();
                                }

                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        return row;
    }
}