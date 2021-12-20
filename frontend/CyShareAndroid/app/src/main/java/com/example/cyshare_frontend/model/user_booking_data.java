package com.example.cyshare_frontend.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.cyshare_frontend.controller.backend_io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class user_booking_data implements Serializable {
    private String userName;
    private ArrayList<Integer> bookingIDS;
    private HashMap<Integer, ArrayList<String>> userBookings;

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
    public user_booking_data(RequestQueue vQueue, String url, String userName) {

        bookingIDS = new ArrayList<>();
        userBookings = new HashMap<>();
        backend_io.serverGetBookingTables(vQueue, url, bookingIDS, userBookings);

//        Log.i("tempArr Size", String.valueOf(tempArr.size()));
    }

    public user_booking_data(RequestQueue vQueue, String url) {

        bookingIDS = new ArrayList<>();
        userBookings = new HashMap<>();


        backend_io.serverGetBookingTables(vQueue, url, bookingIDS, userBookings);

//        Log.i("tempArr Size", String.valueOf(tempArr.size()));
    }


    public ArrayList<Integer> getAllBIDS() {
        return bookingIDS;
    }


    /*
    0 = date
    1 = time
    2 = source lat
    3 = source lng
    4 = destination lat
    5 = destination lng
    6 = fullName
     */
    public ArrayList<String> getAllBookingData(Context context) {
        Log.i("from getAllBookingData", "method called, bids size: " + bookingIDS.size() + ", userBookings size: " + userBookings.size());
        ArrayList<String> toReturn = new ArrayList<>();
        for (int elem : bookingIDS) {
            String temp = "";
            ArrayList<String> tempArr = userBookings.get(elem);
            temp += "Given name: " + tempArr.get(6) + "\n";
            temp += "Date: " + tempArr.get(0) + "\n";
            temp += "Time: " + tempArr.get(1) + "\n";
            temp += "Source Loc: " + getAddress(Double.valueOf(tempArr.get(2)), Double.valueOf(tempArr.get(3)), context);
            temp += "Dest Loc: " + getAddress(Double.valueOf(tempArr.get(4)), Double.valueOf(tempArr.get(5)), context);
            toReturn.add(temp);
        }
        return toReturn;
    }


    private String getAddress(double lat, double lng, Context context) {
//        Log.i("Calling getAddress", "method called");
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//        Log.i("Check", "is present: " + geocoder.isPresent());
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);
            // use your lat, long value here
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);

//                Log.i("Check", "address values: " + address.toString());
//                Log.i("Check", "address values: " + address.getPostalCode());
//                Log.i("Check", "address values: " + address.getFeatureName());
//                Log.i("Check", "address values: " + address.getPremises());
//                Log.i("Check", "address values: " + address.getSubThoroughfare());
//                Log.i("Check", "address values: " + address.getThoroughfare());
//                Log.i("Check", "address values: " + address.getSubAdminArea());
//                Log.i("Check", "address values: " + address.getAdminArea());
//                Log.i("Check", "address values: " + address.getSubLocality());
//                Log.i("Check", "address values: " + address.getLocality());

                StringBuilder sb = new StringBuilder();
                String str;
                if (address.getPremises() != null) {
//                    sb.append(address.getPremises() + " ");
                    sb.append(address.getSubAdminArea() + " ");
                    sb.append(address.getLocality() + " ");
                    sb.append(address.getAdminArea() + " ");
                }
                Log.i("Passed getAddress", "Address: " + sb.toString());
                if (sb.toString().equals("")) return "Null address\n";
                return sb.toString();
            }
            Log.i("Outside getAddress", "failed to find address");
            return "Not Found\n";

        } catch (Exception e) {
            Log.i("Failed getAddress", "failed with exceptions");
            return "Failed with Exception\n";
        }
    }

}
