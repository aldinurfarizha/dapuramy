package com.example.dapurami;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dapurami.ui.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ArrayList<List_data> list_data;
    private ArrayList<List_data2> list_data2;
    private RecyclerView gridView, gridView2;
    MyAdapter adapter;
    drink_adapter adapter2;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    RelativeLayout btn_notification;
    private int dotscount;
    private ImageView[] dots;
    RequestQueue rq;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    ProgressBar food_progress, drink_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cart);
        toolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView=(RecyclerView) findViewById(R.id.gridView);
        gridView2=(RecyclerView) findViewById(R.id.gridView2);
        btn_notification=(RelativeLayout)findViewById(R.id.notification_btn);
        food_progress=(ProgressBar)findViewById(R.id.progress_food);
        drink_progress=(ProgressBar)findViewById(R.id.progress_drink);
        list_data=new ArrayList<>();
        list_data2=new ArrayList<>();
        // adapter=new MyAdapter(getApplicationContext(),list_data);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        getData2();
        getData();

        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();

        sliderImg = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        sendRequest();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        User user = SharedPrefManager.getInstance(this).getUser();

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        View header = navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.user_name);
        TextView phone_number= header.findViewById(R.id.phone_number);
        TextView verified_user=header.findViewById(R.id.verified_user);
        TextView not_verified_user=header.findViewById(R.id.not_verified_user);
        String status= user.getStatus();
        String verified="VERIFIED";
        if(status.equals(verified)){
            verified_user.setVisibility(View.VISIBLE);
        }
        else{
            not_verified_user.setVisibility(View.VISIBLE);
        }
        username.setText(String.valueOf(user.getName()));
        phone_number.setText(String.valueOf(user.getPhone_number()));
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                if (id==R.id.nav_logout){
                    finish();
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                }
                if(id==R.id.nav_gallery){
                    NavigationView navigationView= findViewById(R.id.nav_gallery);
                    navigationView.setNavigationItemSelectedListener(this);
                }
                //This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(menuItem,navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        Drawable send = (Drawable) menu.findItem(R.drawable.cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(home.this, cart.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, URLs.URL_GET_READY_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++){
                        food_progress.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                        JSONObject ob=array.getJSONObject(i);
                        List_data listData=new List_data(ob.getString("id_product"),ob.getString("product_name"), ob.getString("description"), ob.getString("stock"), ob.getString("price"), ob.getString("picture"));
                        list_data.add(listData);
                    }
                    adapter=new MyAdapter(list_data);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(home.this, LinearLayoutManager.HORIZONTAL,false);

                    gridView.setLayoutManager(layoutManager);

                    gridView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Connection poor, try to getting Data", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                }, 500);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } //ambil list maknan

    private void getData2() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, URLs.URL_GET_READY_DRINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++){
                        drink_progress.setVisibility(View.GONE);
                        gridView2.setVisibility(View.VISIBLE);
                        JSONObject ob=array.getJSONObject(i);
                        List_data2 listData2=new List_data2(ob.getString("id_product"),ob.getString("product_name"), ob.getString("description"), ob.getString("stock"), ob.getString("price"), ob.getString("picture"));
                        list_data2.add(listData2);
                    }
                    adapter2=new drink_adapter(list_data2);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(home.this, LinearLayoutManager.HORIZONTAL,false);

                    gridView2.setLayoutManager(layoutManager);

                    gridView2.setAdapter(adapter2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Connection poor, try to getting Data", Toast.LENGTH_SHORT).show();
                        getData2();
                    }
                }, 500);

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } //ambil list minuman
    public void sendRequest(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLs.URL_BANNER, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    SliderUtils sliderUtils = new SliderUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        sliderUtils.setSliderImageUrl(jsonObject.getString("image_url"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);

                }

                viewPagerAdapter = new ViewPagerAdapter(sliderImg, home.this);

                viewPager.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for(int i = 0; i < dotscount; i++){

                    dots[i] = new ImageView(home.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        CustomVolleyRequest.getInstance(this).addToRequestQueue(jsonArrayRequest);

    } //ambil slider

}
