package com.example.dapurami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class splash_screen extends AppCompatActivity {
Button getstarted;
    public static SQLiteHelper mSQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getstarted=(Button) findViewById(R.id.btn_getstarted);
        mSQLiteHelper = new SQLiteHelper(this, "cart.sqlite", null, 1);

        //creating table in database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS cart(id_order INTEGER , id_product VARCHAR, product_name VARCHAR, qty VARCHAR, img VARCHAR)");
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(splash_screen.this,after_splash.class);
                startActivity(i);
            }
        });
    }

}
