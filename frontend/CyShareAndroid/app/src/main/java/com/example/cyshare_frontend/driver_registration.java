/*
 * @Author Josh Lawrinenko
 * @Author Tanay Parikh
 */

package com.example.cyshare_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.controller.backend_io;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class driver_registration extends AppCompatActivity {
    /**
     * Hash Map Holds the all user data from the server,
     * speeds up future operations.
     *
     * Needs to be updated frequently.
     */
    public HashMap<String, ArrayList<String>> usersInfo;
    /**
     * Volley thread for network activity
     */
    public RequestQueue volleyQueue;

    private String userName;

    private EditText carMake;
    private String carMakeStr;
    private EditText carModel;
    private String carModelStr;
    private EditText carYear;
    private String carYearStr;
    private EditText carLicensePlate;
    private String carLicensePlateStr;
    private EditText carColor;
    private String carColorStr;
    private EditText driverInsurance;
    private String driverInsuranceStr;
    private EditText driverLicense;
    private String driverLicenseStr;
    private Button driverHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        /**
         * Get bundle info
         */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");

        /**
         * Gets info from from the textview
         */
        carMake = findViewById(R.id.et_carMake);
        carModel = findViewById(R.id.et_carModel);
        carYear = findViewById(R.id.et_carYear);
        carLicensePlate = findViewById(R.id.et_carLic);
        carColor = findViewById(R.id.et_carCol);
        driverInsurance = findViewById(R.id.et_carInsure);
        driverLicense = findViewById(R.id.et_driverLic);
        //endregion

        /**
         * gets userinfo
         */
        usersInfo = new HashMap<>();
        volleyQueue = Volley.newRequestQueue(this);
        String userUrl = "http://coms-309-031.cs.iastate.edu:8080/users/all";
        backend_io.serverGetUserTables(volleyQueue, userUrl, usersInfo);

        // init button
        driverHome = findViewById(R.id.button_to_home);

        /**
         * sends info on click
         */
        driverHome.setOnClickListener(view -> {
            update_field_data();
            JSONArray to_back = create_post_object(carMakeStr, carModelStr, carYearStr, carLicensePlateStr, carColorStr);
            // TODO - Figure out link
//            String driverUrl = "http://coms-309-031.cs.iastate.edu:8080/cars/all";
//            backend_io.serverPost(volleyQueue, driverUrl, to_back);
            to_driver_home();
        });

    }

    /**
     * takes data from text view and converts into string
     */
    private void update_field_data(){
        carMakeStr = carMake.getText().toString();
        carModelStr = carModel.getText().toString();
        carYearStr = carYear.getText().toString();
        carLicensePlateStr = carLicensePlate.getText().toString();
        carColorStr = carColor.getText().toString();
        driverInsuranceStr = driverInsurance.getText().toString();
        driverLicenseStr = driverLicense.getText().toString();
    }

    /**
     * creates a json post object
     * @param make
     * @param model
     * @param year
     * @param licensePlate
     * @param color
     * @return
     */
    private JSONArray create_post_object(String make, String model, String year, String licensePlate, String color) {
        JSONObject jason = new JSONObject();
        JSONArray arr1 = new JSONArray();
        try {
            jason.put("make ", make);
            jason.put("model", model);
            jason.put("year", year);
            jason.put("color",color);
            jason.put("license_plate", licensePlate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arr1.put(jason);
        return arr1;
    }

    /**
     * takes you to the next page
     */
    private void to_driver_home() {
        Intent loadDriverHome = new Intent(this, driver_home.class);
        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        loadDriverHome.putExtras(extras);
        startActivity(loadDriverHome);
    }


}
