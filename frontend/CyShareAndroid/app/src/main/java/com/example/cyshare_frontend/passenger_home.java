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

public class passenger_home extends AppCompatActivity {

    /**
     * Volley thread for network activity
     */
    public RequestQueue volleyQueue;
    private String userName;
    public user_booking_data ubd;
    public websocket_override wsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);

        // Get bundle info
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");
//        userName = "Josh";
        volleyQueue = Volley.newRequestQueue(this);
        String url = "http://coms-309-031.cs.iastate.edu:8080/PassengerBooking/" + userName;
        ubd = new user_booking_data(volleyQueue, url);

        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsObject = new websocket_override(wsURL);


        Button toRequestBooking = findViewById(R.id.toPassRequest);
        Button toViewStatus = findViewById(R.id.toPassViewStatus);
        Button toChat = findViewById(R.id.toChatPass);

        toRequestBooking.setOnClickListener(view ->
                to_request_booking()
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

    private void to_request_booking() {
        wsObject.wsClient.close();
        Intent loadDriverHome = new Intent(this, passenger_create_request.class);

        Bundle extras = new Bundle();
        extras.putString("USERNAME", userName);
        extras.putSerializable("USERDATA", ubd);
        loadDriverHome.putExtras(extras);

        startActivity(loadDriverHome);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getNotified() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Passeneger Notification", "Passeneger Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Passeneger Notification");
        builder.setContentTitle("Yaaay you got a Driver");
        builder.setContentText("A driver has accepted your request check status page");
        builder.setSmallIcon(R.drawable.icons8_car);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }


    private void to_view_status() {
        wsObject.wsClient.close();
        Intent loadDriverHome = new Intent(this, passenger_view_requests.class);

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
                    ubd = new user_booking_data(volleyQueue, userName);
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
