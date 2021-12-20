package com.example.cyshare_frontend.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class driver {
public static HashMap<Long,String> driverBookings= new HashMap<>();
public static HashMap<Long,String> PassengerBookings = new HashMap<>();

    public driver()
    {

    }

    public void updateDriver(RequestQueue vQueue)
    {   updateDriverBookings(vQueue);
    updatePassengerBookings(vQueue);
    }

    public void updateDriverBookings(RequestQueue vQueue)
    {
        String url = "";//url for getting bookings for particular user using user name
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.i("items", "" + response.length());
                    try {
                        String temp="";
                        JSONObject j= new JSONObject();
                        for (int i = 0; i < response.length(); i++) {
                            j = response.getJSONObject(i);
                            temp += "Given name: " + j.getString("name") + "\n";
                            temp += "Date: " + j.getString("date") + "\n";
                            temp += "Time: " + j.getString("time") + "\n";
                            temp += "Source Loc: " ;//todo
                            temp += "Dest Loc: "; //todo
                        }
                        driverBookings.put(j.getLong("bid"),temp);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> Log.i("VOLLEY", error.getMessage() + " 404-200"));

        // add request to volley thread
        vQueue.add(jsonObjectRequest);
    }
    public void updatePassengerBookings(RequestQueue vQueue)
    { String url = "";//url for getting bookings for particular user using user name
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.i("items", "" + response.length());
                    try {
                        String temp="";
                        JSONObject j= new JSONObject();
                        for (int i = 0; i < response.length(); i++) {



                            j = response.getJSONObject(i);

                            temp += "Given name: " + j.getString("name") + "\n";
                            temp += "Date: " + j.getString("date") + "\n";
                            temp += "Time: " + j.getString("time") + "\n";
                            temp += "Source Loc: "+"\n" ;//todo
                            temp += "Dest Loc: "+"\n"; //todo
                        }
                        driverBookings.put(j.getLong("bid"),temp);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> Log.i("VOLLEY", error.getMessage() + " 404-200"));

        // add request to volley thread
        vQueue.add(jsonObjectRequest);

    }
}
