package com.example.cyshare_frontend;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class chat_page extends AppCompatActivity {

    Button b1, b2;
    EditText e1, e2;
    TextView t1, t2;
    String userName;
    private WebSocketClient cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        userName = extras.getString("USERNAME");
        //userName = "Hugo";
        setContentView(R.layout.activity_chat_page);
        b1 = (Button) findViewById(R.id.CloseSession);
        b2 = (Button) findViewById(R.id.bt2);
        e2 = (EditText) findViewById(R.id.et2);
        t1 = (TextView) findViewById(R.id.tx1);
        t2 = (TextView) findViewById(R.id.ServerStatus);


        /**
         * If running this on an android device, make sure it is on the same network as your
         * computer, and change the ip address to that of your computer.
         * If running on the emulator, you can use localhost.
         */
        String w = "ws://coms-309-031.cs.iastate.edu:8080/chat/" + userName;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w)) {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    String s = t1.getText().toString() + "\n";
                    t1.setText(s + message);
                    getNotified(message);
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                    t2.setText("Server: " + userName + " has joined the chat");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        } catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();

        b2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try
                {
                    cc.send(e2.getText().toString());
                    e2.setText("");
                    closeKeyboard();
                    //getNotified();
                }
                catch (Exception e)
                {
                    Log.d("ExceptionSendMessage: ", e.getMessage().toString());
                    e.printStackTrace();
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cc.close();
                    finish();
                }
                catch (Exception e)
                {
                    Log.d("ExceptionCloseSocket: ", e.getMessage().toString());
                    e.printStackTrace();
                }
            }
        });
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getNotified(String message)
    {   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
    {
        NotificationChannel channel = new NotificationChannel("Notification","Notification", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Notification");
        builder.setContentTitle("New message in chat!");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1,builder.build());
    }

}