/*
 * @Author Josh Lawrinenko
 */

package com.example.cyshare_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.controller.backend_io;
import com.example.cyshare_frontend.model.user_booking_data;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class driver_create_booking extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * Volley thread for network activity
     */
    public RequestQueue volleyQueue;
    private String userName;
    private String API_KEY;
    private AutocompleteSupportFragment driverDestination;
    private AutocompleteSupportFragment driverLocation;
    private SupportMapFragment mapFragment;
    private Marker destMarker;
    private Marker currMarker;
    private GoogleMap gMap;
    private LatLng currLocation;
    private LatLng destLocation;
    private Button toHome;
    private EditText departureTime;
    private EditText departureDate;
    private EditText passengerCount;
    private String departureTimeStr;
    private String departureDateStr;
    private String passCountStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_create_booking);

        //region $init
        // Get bundle info
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");


        // init fields
        departureTime = findViewById(R.id.etDepTime);
        departureDate = findViewById(R.id.etDepDate);
        passengerCount = findViewById(R.id.etPassCount);
        toHome = findViewById(R.id.button_to_lobby);

        // Initialize the SDK
        API_KEY = getString(R.string.api_key);
        Places.initialize(getApplicationContext(), API_KEY);
        // get server data
        volleyQueue = Volley.newRequestQueue(this);

        // Initialize the AutocompleteSupportFragment.
        driverDestination = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_destination);
        driverLocation = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_location);

        // Initialize Google MapFragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        destMarker = null;
        currMarker = null;

        // Specify the types of place data to return.
        driverLocation.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        driverDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        //endregion

        // Set up a PlaceSelectionListener to handle the response
        driverLocation.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                currLocation = place.getLatLng();
//                Log.i("PlacesApi", "Place: " + currLocation.latitude + "\n" + currLocation.longitude);
                if (gMap != null) {
                    if (currMarker != null) {
                        currMarker.remove();
                    }
                    currMarker = gMap.addMarker(new MarkerOptions().position(currLocation).title("Departure"));

                    if (destMarker == null || destLocation == null) {
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 12));
                    } else {
                        double centerLat = (currLocation.latitude + destLocation.latitude) / 2.0;
                        double centerLng = (currLocation.longitude + destLocation.longitude) / 2.0;
                        LatLng center = new LatLng(centerLat, centerLng);
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12));
                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("PlacesError", "An error occurred: " + status);
            }
        });
        driverDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destLocation = place.getLatLng();
//                Log.i("PlacesApi", "Place: " + destLocation.latitude + "\n" + destLocation.longitude);
                if (gMap != null) {
                    if (destMarker != null) {
                        destMarker.remove();
                    }
                    destMarker = gMap.addMarker(new MarkerOptions().position(destLocation).title("Destination"));

                    if (currMarker == null || currLocation == null) {
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destLocation, 12));
                    } else {
                        double centerLat = (currLocation.latitude + destLocation.latitude) / 2.0;
                        double centerLng = (currLocation.longitude + destLocation.longitude) / 2.0;
                        LatLng center = new LatLng(centerLat, centerLng);
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12));
                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("PlacesError", "An error occurred: " + status);
            }
        });
        toHome.setOnClickListener(view -> {
            update_fields();
            try {
                if (validate()) to_home();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initializes map fragment with activity specific UI
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(41.8780, 93.0977)).title("Marker"));
        gMap = googleMap;
        gMap.getUiSettings().setAllGesturesEnabled(false);
        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.0279039, -93.6241462), 12));
    }

    /**
     * Activity specific. Helper method for creating json object
     * with user supplied information.
     */
    public JSONObject create_post_object() {
        JSONObject temp = new JSONObject();
        try {
//            temp.put("name", "DIO");
            temp.put("name", userName);
            temp.put("date", departureDateStr);
            temp.put("time", departureTimeStr);

            String role = " ";

            role = "DRIVER";
//            else
//                role = "PASSENGER";
//
            temp.put("role", role);
            JSONArray arr = new JSONArray();
            JSONObject temp1 = new JSONObject();
            temp1.put("latitude", currLocation.latitude);
            temp1.put("longitude", currLocation.longitude);
            JSONObject temp2 = new JSONObject();
            temp2.put("latitude", destLocation.latitude);
            temp2.put("longitude", destLocation.longitude);
            arr.put(temp1);
            arr.put(temp2);
            temp.put("location", arr);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.i("JSON", e.getMessage() + "316");
        }
        return temp;

    }

    /**
     * Bundles server io operations into one method.
     * Creates post object and sends to server.
     *
     * @return True is post was successful
     */
    private boolean validate() throws JSONException {
        if (destLocation != null && currLocation != null && departureTimeStr != "" && departureDateStr != "" && passCountStr != "") {
            // TODO - Figure out link
            JSONObject toServer = create_post_object();
            //String serverEndpoint = "http://coms-309-031.cs.iastate.edu:8080/booking/add/maps/"+"DIO"+"/"+departureDateStr+departureTimeStr;
            backend_io.serverPostObject(volleyQueue, "http://coms-309-031.cs.iastate.edu:8080/booking/add/", toServer);
            // backend_io.serverPostArray(volleyQueue,serverEndpoint,create_post_array());
            return true;
        } else {
            return false;
        }
    }

    private void update_fields() {
        departureDateStr = departureDate.getText().toString();
        //Toast.makeText(this,departureDateStr, Toast.LENGTH_LONG).show();
        departureTimeStr = departureTime.getText().toString();
        passCountStr = passengerCount.getText().toString();
    }

    //adding commment
    private void to_home() {
        Intent loadPage = new Intent(this, driver_home.class);

        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        loadPage.putExtras(extras);

        startActivity(loadPage);
    }

}
