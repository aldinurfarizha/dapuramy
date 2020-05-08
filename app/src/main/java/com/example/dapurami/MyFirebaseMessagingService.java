package com.example.dapurami;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
User user;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        send_token(s);

        Log.e("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
    public void send_token(String s){
        user = SharedPrefManager.getInstance(this).getUser();
        final String token=s;
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

        VolleySingleton.getInstance(MyFirebaseMessagingService.this).addToRequestQueue(stringRequest);
    }
}
