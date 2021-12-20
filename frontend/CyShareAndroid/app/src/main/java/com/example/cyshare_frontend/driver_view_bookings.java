package com.example.cyshare_frontend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.model.user_booking_data;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class driver_view_bookings extends AppCompatActivity {
    private RequestQueue volleyQueue;
    private String userName;
    private List<String> addArray;
    private ArrayList<String> ids;
    private ListView listView;
    private String bid;
    private ProgressBar pb;
    private Handler mhandler = new Handler();
    private int counter = 0;
    private user_booking_data ubd;
    private websocket_override wsObject;
    private ArrayList<user_booking_data> userData;
    private ArrayList<user_booking_data> passengerData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_view_bookings);

        // get bundle data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");

       ubd = (user_booking_data) intent.getSerializableExtra("USERDATA");
       userData = new ArrayList<>();
       passengerData = new ArrayList<>();



        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsObject = new websocket_override(wsURL);


        pb = (ProgressBar) findViewById(R.id.progressBar);
        ids = new ArrayList<>();

        listView = (ListView) findViewById(R.id.bookingIds);
        TextView t = (TextView) findViewById(R.id.driver_booking_res_text);
        listView.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter < 100) {
                    counter++;
                    android.os.SystemClock.sleep(20);
                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(counter);
                        }
                    });

                }
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility(View.VISIBLE);
                        t.setText("Bookings");
                        pb.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

      ArrayList<String> tempArr = ubd.getAllBookingData(this);

        //  ArrayList<String> tempArr = ubd.getAllBookingData(this);

        addArray = new ArrayList<>();
        addArray.add("Tanay");
       volleyQueue  = Volley.newRequestQueue(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tempArr);
        ArrayList<Integer> arrey = ubd.getAllBIDS();
        for(int ids:arrey)
        {
            String url = "http://coms-309-031.cs.iastate.edu:8080/booking/travellers/"+ids;
            Log.i("Id",ids+"");
            user_booking_data usr = new user_booking_data(volleyQueue,url);
           userData.add(usr);
        }
        for(int ids:arrey)
        {
            String url = "http://coms-309-031.cs.iastate.edu:8080/Booking/Passenger/"+ids;
            Log.i("Id",ids+"");
            user_booking_data usr = new user_booking_data(volleyQueue,url);
            passengerData.add(usr);

        }
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                to_booking_res(arrey.get(position),position);
            }
        });
    }

    private void sendUpdateToast() {
        Toast.makeText(this, "Updates received. Returning home.", Toast.LENGTH_LONG).show();
    }


    private void to_booking_res(int id, int pos) {
        wsObject.wsClient.close();
        Intent loadBookingRes = new Intent(this, driver_booking_response.class);
        Bundle extras = new Bundle();

        extras.putString("USERNAME", userName);
        extras.putInt("PASSBID", id);


        extras.putSerializable("USERDATA", userData.get(pos));
        extras.putSerializable("USERDATA2",passengerData.get(pos));
        Log.i("Taggs",pos+"");

        // extras.putSerializable("USERDATA", userData.get(pos));

        loadBookingRes.putExtras(extras);
        startActivity(loadBookingRes);
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
    {   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
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
