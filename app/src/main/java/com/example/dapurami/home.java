package com.example.dapurami;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.dapurami.ui.ViewPagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ArrayList<List_data> list_data;
    private ArrayList<List_data2> list_data2;
    private RecyclerView gridView, gridView2;
    MyAdapter adapter;
    drink_adapter adapter2;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    RelativeLayout btn_notification, btn_ready_menu, btn_profile, btn_voting;
    private int dotscount;
    private ImageView[] dots;
    RequestQueue rq;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    ShimmerRecyclerView shimmerRecyclerView, shimmerRecyclerView2, shimmerRecyclerView3;
    TextView food_menu,drink_menu;
    String newToken;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cart);
        toolbar.setOverflowIcon(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = SharedPrefManager.getInstance(this).getUser();
        food_menu=(TextView)findViewById(R.id.food_menu_av);
        drink_menu=(TextView)findViewById(R.id.drink_menu_av);
        gridView=(RecyclerView) findViewById(R.id.gridView);
        gridView2=(RecyclerView) findViewById(R.id.gridView2);
        btn_notification=(RelativeLayout)findViewById(R.id.notification_btn);
        btn_ready_menu=(RelativeLayout)findViewById(R.id.btn_ready_menu);
        btn_profile=(RelativeLayout)findViewById(R.id.btn_profile);
        btn_voting=(RelativeLayout)findViewById(R.id.btn_voting);
        shimmerRecyclerView=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view);
        shimmerRecyclerView2=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view2);
        shimmerRecyclerView3=(ShimmerRecyclerView)findViewById(R.id.shimmer_recycler_view3);
        list_data=new ArrayList<>();
        list_data2=new ArrayList<>();
        shimmerRecyclerView.showShimmerAdapter();
        shimmerRecyclerView2.showShimmerAdapter();
        shimmerRecyclerView3.showShimmerAdapter();
        newToken=FirebaseInstanceId.getInstance().getToken();
        refresh_account();

        // adapter=new MyAdapter(getApplicationContext(),list_data);

        btn_ready_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, ready_menu.class);
                startActivity(intent);
            }
        });

        btn_voting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, Vooting.class);
                startActivity(intent);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, profile.class);
                startActivity(intent);
            }
        });

        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, notification.class);
                startActivity(intent);
            }
        });

        food_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, food_menu.class);
                startActivity(intent);
            }
        });
        drink_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home.this, drink_menu.class);
                startActivity(intent);
            }
        });
        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();

        sliderImg = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                getData2();
                sendRequest();
                send_token();
            }
        }, 1750);


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
                    Intent intent=new Intent(home.this, food_menu.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_tools){
                    Intent intent=new Intent(home.this, your_order.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_slideshow){
                    Intent intent=new Intent(home.this, Vooting.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_send){
                    Intent intent=new Intent(home.this, notification.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_share){
                    Intent intent=new Intent(home.this, profile.class);
                    startActivity(intent);
                }
                if(id==R.id.nav_transfer){
                    Intent intent=new Intent(home.this, transferlist.class);
                    startActivity(intent);
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
    public void refresh_account(){
        final String id = Integer.toString(user.getId());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REFRESH_PROFIL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.getBoolean("error")) {
                                        JSONObject userJson = obj.getJSONObject("user");
                                        User user = new User(
                                                userJson.getInt("id_customer"),
                                                userJson.getString("name"),
                                                userJson.getString("phone_number"),
                                                userJson.getString("address"),
                                                userJson.getString("status")
                                        );
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Check Your Connection", Toast.LENGTH_SHORT).show();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);

                        return params;
                    }
                };

                VolleySingleton.getInstance(home.this).addToRequestQueue(stringRequest);
            }
        }, 100);
    }
    public void send_token(){
        user = SharedPrefManager.getInstance(this).getUser();
        final String token=newToken;
        final String id_user= Integer.toString(user.getId());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GET_SEND_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {



                            } else {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token_fcm", token );
                params.put("id_customer", id_user );
                return params;
            }
        };

        VolleySingleton.getInstance(home.this).addToRequestQueue(stringRequest);

    }
    private void getData() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET, URLs.URL_GET_READY_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++){
                        shimmerRecyclerView.hideShimmerAdapter();
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
                        shimmerRecyclerView2.hideShimmerAdapter();
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
                        getData2();
                    }
                }, 1000);

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } //ambil list minuman

    public void sendRequest(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLs.URL_BANNER, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
 shimmerRecyclerView3.hideShimmerAdapter();
 viewPager.setVisibility(View.VISIBLE);
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
                sendRequest();
            }
        });

        CustomVolleyRequest.getInstance(this).addToRequestQueue(jsonArrayRequest);

    } //ambil slider

}
