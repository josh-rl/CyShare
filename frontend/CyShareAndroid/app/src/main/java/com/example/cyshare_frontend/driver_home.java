/*
 * @Author Josh Lawrinenko
 */

package com.example.cyshare_frontend;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

public class driver_home extends AppCompatActivity {

    public RequestQueue vq;
    public user_booking_data ubd;
    private String userName;
    public websocket_override wsObject;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        // Get bundle info
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");
        vq = Volley.newRequestQueue(this);
        ubd = new user_booking_data(vq, "http://coms-309-031.cs.iastate.edu:8080/booking/" + userName);

        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsObject = new websocket_override(wsURL);

        Button toCreateBooking = findViewById(R.id.toCreateBooking);
        Button toViewStatus = findViewById(R.id.toDriverViewStatus);
        Button toChat = findViewById(R.id.toChatDriver);

        toCreateBooking.setOnClickListener(view ->
                to_create_booking()
        );
        toViewStatus.setOnClickListener(view ->
                to_view_status()
        );
        toChat.setOnClickListener(view ->
                to_chat()
        );
    }

    private void sendUpdateToast() {
        Toast.makeText(this, "Updates received.", Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getNotified() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Passeneger Notification", "Passeneger Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Passeneger Notification");
        builder.setContentTitle("Yaaay  you got a Passenger");
        builder.setContentText("You can accept someone in your request check status page");
        builder.setSmallIcon(R.drawable.icons8_car_50);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }

    private void to_create_booking() {
        wsObject.wsClient.close();
        Intent loadDriverHome = new Intent(this, driver_create_booking.class);
        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        extras.putSerializable("USERDATA", ubd);
        loadDriverHome.putExtras(extras);
        startActivity(loadDriverHome);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void to_view_status() {
        getNotified();
        wsObject.wsClient.close();
        Intent loadDriverHome = new Intent(this, driver_view_bookings.class);
        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        extras.putSerializable("USERDATA", ubd);
        loadDriverHome.putExtras(extras);
        startActivity(loadDriverHome);
    }

    private void to_chat() {
        wsObject.wsClient.close();
        Intent loadDriverHome = new Intent(this, chat_page.class);
        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        loadDriverHome.putExtras(extras);
        startActivity(loadDriverHome);
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
                    ubd = new user_booking_data(vq, userName);
                    Log.i("Websocket", "Message Received");
                }

                @Override
                public void onClose(int errorCode, String reason, boolean remote) {
                    Log.i("Websocket", "Closed " + reason);
                    wsClient.close();
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