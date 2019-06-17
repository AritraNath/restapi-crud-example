package com.aritra.restapp.Handlers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aritra.restapp.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckAPIStatus {
    private boolean flag = false;

    public boolean isOnline(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = Constants.BASE_URL + Constants.TEST_URL;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

//                    Log.e("RESPONSE_STATUS", response.getString("status"));
                    if(response.getString("status").trim().equals("Up")) {
                        flag = true;
//                        Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("REQ_FAIL", "JSON Request Failed!");
                Toast.makeText(context, "Something happened!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
        return flag;
    }
}
