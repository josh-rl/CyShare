package com.example.cyshare_frontend;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

public class passenger_request_status extends AppCompatActivity {

    private ListView listView1;
    private RequestQueue volleyQueue;
    private List<String> addArray;
    private ProgressBar bar;
    private Handler mhandler = new Handler();
    private int counter = 0;
    private TextView t;

    private String userName;
    public user_booking_data ubd;
    private websocket_override wsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_request_status);

        // get bundle data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");
        ubd = (user_booking_data) intent.getSerializableExtra("USERDATA");

        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsObject = new websocket_override(wsURL);

        volleyQueue = Volley.newRequestQueue(this);
        //  ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android
        listView1 = (ListView) findViewById(R.id.et_ListView);
        t = (TextView) findViewById(R.id.pass_request_status_text);
        addArray = ubd.getAllBookingData(this);
        bar = (ProgressBar) findViewById(R.id.passStatus_bar);
        listView1.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (counter < 100) {
                    counter++;
                    android.os.SystemClock.sleep(50);
                    mhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(counter);
                        }
                    });
                }
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView1.setVisibility(View.VISIBLE);
                        t.setText("Booking List");
                        bar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        // ask bhuwan what type of data is going here

       // addArray.add("test");
        //backend_io.serverPutObject( Volley.newRequestQueue(this)," ",null);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, addArray);
        listView1.setAdapter(arrayAdapter);


    }

    private void sendUpdateToast() {

        Toast.makeText(this, "Updates received. Returning home.", Toast.LENGTH_LONG).show();
    }

    private void to_home() {
        wsObject.wsClient.close();
        Intent loadBookingRes = new Intent(this,passenger_home.class);
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