/*
 * @Author Tanay Parikh
 * @Author Josh Lawrinenko
 * @Author Bhuwan Joshi
 */

package com.example.cyshare_frontend.controller;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public final class backend_io {

    /**
     * Method for requesting backend table information.
     * Requests json object from server then parses into map.
     *
     * @param vQueue     for creating volley thread
     * @param url        where to request data from
     * @param serverData map used for storing the json array(s)
     */
    public static void serverGetUserTables(RequestQueue vQueue, String url, HashMap<String, ArrayList<String>> serverData) {
        // create GET request
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            response -> {
                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject userObject = response.getJSONObject(i);
                        ArrayList<String> arr = new ArrayList<>();

                        arr.add(userObject.getString("id"));
                        arr.add(userObject.getString("password"));
                        arr.add(userObject.getString("role"));

//                        Log.i("id:", userObject.getString("id"));
//                        Log.i("pass:", userObject.getString("password"));
//                        Log.i("role:", userObject.getString("role"));

//                        if (userObject.getString("role").equals("DRIVER")) {
//                            JSONObject vehicle = userObject.getJSONObject("car");
//                            arr.add(vehicle.getString("make"));
//                            arr.add(vehicle.getString("model"));
//                            arr.add(vehicle.getString("year"));
//                            arr.add(vehicle.getString("color"));
//                            arr.add(vehicle.getString("licensePlate"));
//                        }



                        serverData.put(userObject.getString("userName"), arr);

//                        Log.i("user:", userObject.getString("userName"));
                    }

//                    Log.i("users size", String.valueOf(serverData.size()));

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            },
            error -> Log.i("VOLLEY", error.getMessage() + " 404-100"));

        // add request to volley thread
        vQueue.add(jsonObjectRequest);
    }


    /*
    {
        "name": "DIO",
        "date": "1234",
        "role": null,
        "time": "1234",
        "isBooked": null,
        "location": [
            {
                "id": 5,
                "latitude": 41.619549,
                "longitude": -93.598022
            },
            {
                "id": 6,
                "latitude": 42.034534,
                "longitude": -93.620369
            }
        ],
        "fullName": "John Doe",
        "requests": [],
        "bid": 13
    }
    */
    public static void serverGetBookingTables(RequestQueue vQueue, String url, ArrayList<Integer> bookingIDS, HashMap<Integer, ArrayList<String>> userBookings) {
        // create GET request
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
            response -> {
                Log.i("items", "" + response.length());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject tempObj = response.getJSONObject(i);

//                        Log.i("user", tempObj.getString("name"));
//                        Log.i("date", tempObj.getString("date"));
//                        Log.i("time", tempObj.getString("time"));
//                        Log.i("name", tempObj.getString("fullName"));
//                        Log.i("bid", tempObj.getString("bid"));

                        ArrayList<String> objData = new ArrayList<>();
                        objData.add(tempObj.getString("date"));
                        objData.add(tempObj.getString("time"));
                        JSONArray tempMaps = tempObj.getJSONArray("location");
                        JSONObject currLoc = tempMaps.getJSONObject(0);
                        JSONObject destLoc = tempMaps.getJSONObject(1);
                        objData.add(String.valueOf(currLoc.getDouble("latitude")));
                        objData.add(String.valueOf(currLoc.getDouble("longitude")));
                        objData.add(String.valueOf(destLoc.getDouble("latitude")));
                        objData.add(String.valueOf(destLoc.getDouble("longitude")));
                        objData.add(tempObj.getString("fullName"));
                        bookingIDS.add(tempObj.getInt("bid"));
                        userBookings.put(tempObj.getInt("bid"), objData);

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        continue;
                    }
                }
            },
            error -> Log.i("VOLLEY", error.getMessage() + " 404-200"));

        // add request to volley thread
        vQueue.add(jsonObjectRequest);
    }


    /**
     * Method for posting json arrays to server.
     *
     * @param vQueue for creating volley thread
     * @param url    where to send data to
     * @param data   json array - must be pre-validated
     */
    public static void serverPostArray(RequestQueue vQueue, String url, JSONArray data) {
        // create POST request
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url, data,
                response -> Log.i("JSON", String.valueOf(response)),
                error -> Log.i("VOLLEY", error.getMessage() + " 404-300"));
        // add request to volley thread
        vQueue.add(jsonObjectRequest);

    }


    /**
     * Method for posting json objects to server.
     *
     * @param vQueue for creating volley thread
     * @param url    where to send data to
     * @param data   json object - must be pre-validated
     */
    public static void serverPostObject(RequestQueue vQueue, String url, JSONObject data) {
        // create POST request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data,
                response -> Log.i("JSON", String.valueOf(response)),
                error -> Log.i("VOLLEY", error.getMessage() + " 404-400"));
        // add request to volley thread
        vQueue.add(jsonObjectRequest);

    }


    /**
     * Method for putting json objects to server.
     *
     * @param vQueue for creating volley thread
     * @param url    where to send data to
     * @param data   json object - must be pre-validated
     */
    public static void serverPutObject(RequestQueue vQueue, String url, JSONObject data) {
        // create POST request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, data,
                response -> Log.i("JSON", String.valueOf(response)),
                error -> Log.i("VOLLEY", error.getMessage() + " 404-500"));
        // add request to volley thread
        vQueue.add(jsonObjectRequest);

    }

    public static void serverPutArray(RequestQueue vQueue, String url, JSONArray data) {
        // create POST request
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.PUT, url, data,
                response -> Log.i("JSON", String.valueOf(response)),
                error -> Log.i("VOLLEY", error.getMessage() + " 404-600"));
        // add request to volley thread
        vQueue.add(jsonObjectRequest);

    }


}
