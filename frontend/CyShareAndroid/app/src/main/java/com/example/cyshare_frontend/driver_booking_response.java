package com.example.cyshare_frontend;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.controller.backend_io;
import com.example.cyshare_frontend.model.user_booking_data;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;

public class driver_booking_response extends AppCompatActivity {
    private ListView listView;
    private ListView listView1;
    private RequestQueue volleyQueue;
    private String userName;
    private int driverBID;
    private user_booking_data ubd;
    private user_booking_data ubd1;
    private List<String> addArray;
    private ProgressBar bar;
    private Handler phandler = new Handler();
    private int counter = 0;
    private TextView t;
    private TextView y;
    private websocket_override wsObject;
    private Button butt;
    private ArrayList<Integer> ids;
    private ArrayList<Integer> toSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_booking_response);
        // get bundle data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");
        driverBID = extras.getInt("PASSBID");
        ubd = (user_booking_data) intent.getSerializableExtra("USERDATA");
        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsObject = new websocket_override(wsURL);
        // volleyQueue = Volley.newRequestQueue(this);
        t = (TextView) findViewById(R.id.driver_booking_res_text);
        y = (TextView) findViewById(R.id.driver_booking_text2);
        bar = (ProgressBar) findViewById(R.id.driverResponseBar);
        butt = (Button) findViewById(R.id.button);
        butt.setVisibility(View.INVISIBLE);
        butt.setText("Home");
        toSend = new ArrayList<>();
        listView = (ListView) findViewById(R.id.et_listView_driver);
        listView1 = (ListView) findViewById(R.id.et_list_driver2);
        listView.setVisibility(View.INVISIBLE);
        listView1.setVisibility(View.INVISIBLE);
        y.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter < 100) {
                    counter++;
                    android.os.SystemClock.sleep(20);
                    phandler.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(counter);
                        }
                    });

                }
                phandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility(View.VISIBLE);
                        listView1.setVisibility(View.VISIBLE);
                        y.setVisibility(View.VISIBLE);
                        t.setText("Booked Rides");
                        bar.setVisibility(View.GONE);
                        butt.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
        ubd = (user_booking_data) intent.getSerializableExtra("USERDATA");
        ubd1 = (user_booking_data) intent.getSerializableExtra("USERDATA2");
        ids = ubd.getAllBIDS();
        Log.i("Array Ids", ubd.getAllBIDS().size() + "");
        ArrayList<String> view1 = ubd1.getAllBookingData(this);
        ArrayList<String> tempArr = ubd.getAllBookingData(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, view1);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tempArr);
        listView.setAdapter(arrayAdapter);
        listView1.setAdapter(arrayAdapter1);
        volleyQueue = Volley.newRequestQueue(this);
        Context con = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                arrayAdapter.notifyDataSetChanged();
            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

                if (view1.size() == 3) {
                    Toast.makeText(driver_booking_response.this, "Cannot Book More than Three Passengers", Toast.LENGTH_SHORT).show();
                    return;
                }
                toSend.add(ids.get(position));
                view1.add(tempArr.get(position));
                arrayAdapter1.remove(arrayAdapter1.getItem(position));
                ids.remove(position);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        butt.setOnClickListener(view -> {
            //Toast.makeText(this,toSend.get(0)+"",Toast.LENGTH_LONG).show();
            for (int id : toSend) {
                String url = "http://coms-309-031.cs.iastate.edu:8080/booking/add/"+driverBID+"/target/"+id;
                backend_io.serverPutObject(volleyQueue, url, new JSONObject());
            }
            to_home();
        }); }

    private void sendUpdateToast() {
        Toast.makeText(this, "Updates received. Returning home.", Toast.LENGTH_LONG).show();
    }

    private void to_home() {
        wsObject.wsClient.close();
        Intent loadBookingRes = new Intent(this, driver_home.class);
        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        loadBookingRes.putExtras(extras);
        startActivity(loadBookingRes);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void getNotified()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("Passeneger Notification","Passeneger Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Passeneger Notification");
        builder.setContentTitle("Yaaay you got a Passenger");
        builder.setContentText("You can accept someone in your request check status page");
        builder.setSmallIcon(R.drawable.icons8_car);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1,builder.build());
    }


    private class websocket_override {
        public WebSocketClient wsClient;
        public URI serverURI;

        public websocket_override(String uri) {
            try {
                serverURI = new URI(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }

            wsClient = new WebSocketClient(this.serverURI) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i("Websocket", "Opened");
                }

                @Override
                public void onMessage(String msg) {
                    sendUpdateToast();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    to_home();
                    Log.i("Websocket", "Message Received");
                }

                @Override
                public void onClose(int errorCode, String reason, boolean remote) {
                    Log.i("Websocket", "Closed " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.i("Websocket", "Error " + e.getMessage());
                }
            };

            wsClient.connect();
        }
    }
    
}




