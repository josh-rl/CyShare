package com.example.cyshare_frontend;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.model.user_booking_data;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class passenger_view_requests extends AppCompatActivity {

    private ListView listView;
    private RequestQueue volleyQueue;
    private List<String> addArray;
    private ProgressBar bar;
    private Handler han = new Handler();
    private int counter = 0;
    private TextView t;

    private String userName;
    public user_booking_data ubd;
    private websocket_override wsObject;
    private ArrayList<user_booking_data> driverData;
    private ArrayList<Integer> ids;
    private ArrayList<String> toDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_view_requests);
        toDisplay = new ArrayList<>();
        driverData = new ArrayList<>();
        ids = new ArrayList<>();

        // get bundle data


        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsObject = new websocket_override(wsURL);

        bar = (ProgressBar) findViewById(R.id.passView_bar);
        // TextView t =(TextView) findViewById(R.id.et_passBooking_list);
        TextView t = (TextView) findViewById(R.id.passBooking_text);
        listView = (ListView) findViewById(R.id.bookingIds);
        listView.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter < 100) {
                    counter++;
                    android.os.SystemClock.sleep(50);
                    han.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(counter);
                        }
                    });
                }
                han.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility((View.VISIBLE));
                        t.setText("Your Booking Status");
                        bar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");
        ubd = (user_booking_data) intent.getSerializableExtra("USERDATA");
        ids = ubd.getAllBIDS();
        volleyQueue = Volley.newRequestQueue(this);
        toDisplay = ubd.getAllBookingData(this);
        for(int i:ids)
        {
           String url = " http://coms-309-031.cs.iastate.edu:8080/Booking/Driver/"+i;
           user_booking_data usr = new user_booking_data(volleyQueue,url);
           driverData.add(usr);
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, toDisplay);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {

    // getNotified();
               // to_request_status(position);


                // arrayAdapter.remove(arrayAdapter.getItem(position));
                //arraylist.clear();

                // backend_io.serverPut( Volley.newRequestQueue(this)," ",null);

            }
        });


    }

    private void sendUpdateToast() {
        Toast.makeText(this, "Updates received. Returning home.", Toast.LENGTH_LONG).show();
    }

    private void to_request_status(int pos) {
        wsObject.wsClient.close();
        Intent loadBookingRes = new Intent(this, passenger_request_status.class);

        Bundle extras = new Bundle();

        extras.putString("USERNAME", userName);
        extras.putSerializable("USERDATA", driverData.get(pos));

        loadBookingRes.putExtras(extras);
        startActivity(loadBookingRes);
    }

    private void to_home() {

        wsObject.wsClient.close();
        Intent loadBookingRes = new Intent(this, passenger_home.class);
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
        builder.setContentTitle("Yaaay you got a Driver");
        builder.setContentText("A driver has accepted your request check status page");
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

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onMessage(String msg) {
                    sendUpdateToast();
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    to_home();
                    getNotified();
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
