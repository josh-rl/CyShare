/*
 * @Author Tanay Parikh
 * @Author Josh Lawrinenko
 * @Author Bhuwan Joshi
 */

package com.example.cyshare_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.controller.backend_io;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class register_page extends AppCompatActivity {
    /**
     * Used for sending user to next page
     */
    private enum userTypes {
        DRIVER,
        PASSENGER
    }
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
    /**
     * Holds user type, determined by button click
     */
    public userTypes userType;

    private EditText userName;
    private EditText userPass1;
    private EditText userPass2;
    private EditText fullName;
    private TextView notificationMsg;


    /**
     * Initialize is all the buttons and text Fields
     *
     * @param savedInstanceState android state data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //region $init screen
        userName = findViewById(R.id.enterEmailReg);
        userPass1 = findViewById(R.id.enterPassReg);
        userPass2 = findViewById(R.id.confirmPassReg);
        fullName = findViewById(R.id.full_name);
        Button button_to_register = findViewById(R.id.register_button);
        ToggleButton userSelect = findViewById(R.id.toggleButton);
        notificationMsg = findViewById(R.id.notificationInfo);
        userType = userTypes.PASSENGER;
        //endregion

        // get server data
        usersInfo = new HashMap<>();
        volleyQueue = Volley.newRequestQueue(this);
        String url = "http://coms-309-031.cs.iastate.edu:8080/users/all";
        backend_io.serverGetUserTables(volleyQueue, url, usersInfo);

        // functionality
        userSelect.setOnCheckedChangeListener((buttonView,
            isChecked) -> {
                if (isChecked)  {
                    userType = userTypes.DRIVER;
                    userSelect.setText("Driver");
                }
                else {
                    userType = userTypes.PASSENGER;
                    userSelect.setText("Passenger");
                }
        });
        button_to_register.setOnClickListener(view -> {
            String email = userName.getText().toString();
            String password1 = userPass1.getText().toString();
            String password2 = userPass2.getText().toString();
            String fullname = fullName.getText().toString();
            if (!email.equals("") && !password1.equals("") && !password2.equals("") && !fullname.equals("")) {
                validate(volleyQueue, email, password1, password2, fullname);
            }
            else {
                String errorMsg = "Missing data, all fields must be filled out.";
                notificationMsg.setText(errorMsg);
            }
        });
    }

    /**
     * Helper method for separating validation steps.
     * Checks user supplied data against server data.
     * If data is good, send user to splash page.
     *
     * @param email from user
     * @param password1 from user
     * @param password2 from user for validation
     */
    public void validate(RequestQueue vQueue, String email, String password1, String password2, String name) {
        // if email already exists, error case
        if (usersInfo.containsKey(email)) {
            String errorMsg = "User Already Exists";
            notificationMsg.setText(errorMsg);
        }
        // if passwords match and new email is being added
        else if (password1.equals(password2)) {
            String userNotify = "Loading...";
            notificationMsg.setText(userNotify);
            // check where to send user
            if(userType == userTypes.DRIVER) {
                // creates json object and send to server
                //JSONArray to_server = create_post_object(email, password1, name, "DRIVER");
                String url = "https://coms-309-031.cs.iastate.edu:8080/users/add";
                backend_io.serverPostObject(vQueue, url, create_post_object(email, password1, name, "DRIVER"));
                to_driver_reg();
            }
            else{
                // creates json object and send to server
                // JSONArray to_server = create_post_object(email, password1, name, "PASSENGER");

                String url = "http://coms-309-031.cs.iastate.edu:8080/users/add";
                backend_io.serverPostObject(vQueue, url, create_post_object(email, password1, name, "PASSENGER"));
                to_passenger_home();
            }
        }
        else {
            String errorMsg = "Passwords must match";
            notificationMsg.setText(errorMsg);
        }

    }

    /**
     * Helper method for creating json object with user supplied
     * information.
     *
     * Activity specific.
     *
     * @param email email from user
     * @param pass password from user
     * @return JSONArray in server ready format
     */
    public JSONObject create_post_object(String email, String pass, String fullname, String role) {
        JSONObject temp = new JSONObject();

        try {
            temp.put("userName", email);
            temp.put("password", pass);
            temp.put("fullName", fullname);
            temp.put("role", role);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Helper method for taking the user to the splash page
     * Activity specific.
     */
    public void to_passenger_home() {
        Intent loadSplash = new Intent(this, passenger_home.class);

        Bundle extras = new Bundle();
        String userEmailStr = userName.getText().toString();
        extras.putString("USERNAME", userEmailStr);
        loadSplash.putExtras(extras);

        startActivity(loadSplash);
    }

    public void to_driver_reg(){
        Intent loadReg = new Intent(this, driver_registration.class);

        Bundle extras = new Bundle();
        String userEmailStr = userName.getText().toString();
        extras.putString("USERNAME", userEmailStr);
        loadReg.putExtras(extras);

        startActivity(loadReg);
    }
}
