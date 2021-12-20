package com.example.cyshare_frontend.stubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cyshare_frontend.R;
import com.example.cyshare_frontend.model.user_booking_data;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class unit_tester extends AppCompatActivity {
    /**
     * Volley thread for network activity
     */
    public RequestQueue vq;
    public user_booking_data ubd;
    public websocket_override wsc;
    public TextView utNotification;
    public String testValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_tester);

        String userName = "joshthecoolguy";

        vq = Volley.newRequestQueue(this);
        ubd = new user_booking_data(vq, userName);

        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
//        wsc = new websocket_override(wsURL);

        utNotification = findViewById(R.id.utNotification);

        Button utBTN1 = findViewById(R.id.utTestBundle);
        utBTN1.setOnClickListener(view -> {
            Intent loadSplash = new Intent(this, unit_tester2.class);

            Bundle extras = new Bundle();
            extras.putSerializable("userData", ubd);
            loadSplash.putExtras(extras);

            startActivity(loadSplash);
        });

        Button utBTN2 = findViewById(R.id.utConnect);
        utBTN2.setOnClickListener(view -> {
            wsc.wsClient.connect();
        });

        Button utBTN3 = findViewById(R.id.utSendMessage);
        utBTN3.setOnClickListener(view -> {
            wsc.wsClient.send("1");
        });

        Button utBTN4 = findViewById(R.id.utCloseClient);
        utBTN4.setOnClickListener(view -> {
            wsc.wsClient.close();
        });
    }

    public void setupWebsocket() {
        String userName = "joshthecoolguy";
        String wsURL = "ws://coms-309-031.cs.iastate.edu:8080/websocket/" + userName;
        wsc = new websocket_override(wsURL);
    }

    public class websocket_override {
        public WebSocketClient wsClient;
        public URI serverURI;

        public websocket_override(String uri) {
            try {
                serverURI = new URI(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
//                utNotification.setText("URI error");
                return;
            }

            wsClient = new WebSocketClient(this.serverURI) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
//                    utNotification.setText("Connected");
                    Log.i("Websocket", "Opened");
                }

                @Override
                public void onMessage(String msg) {
//                    utNotification.setText("Message received: " + msg);
                    testValue = msg;
                    Log.i("Websocket", "Message Received");
                }

                @Override
                public void onClose(int errorCode, String reason, boolean remote) {
//                    utNotification.setText("Closed successfully\n" + reason);
                    Log.i("Websocket", "Closed " + reason);
                }

                @Override
                public void onError(Exception e) {
//                    utNotification.setText("Error occurred\n" + e.getMessage());
                    Log.i("Websocket", "Error " + e.getMessage());
                }
            };
        }

        public void connectWSC() {
            wsClient.connect();
        }

        public void sendOne() {
            wsClient.send("1");
        }

        public void sendZero() {
            wsClient.send("0");
        }

        public void closeWSC() {
            wsClient.close();
        }

    }
}
