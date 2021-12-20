package com.example.cyshare_frontend.stubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cyshare_frontend.R;
import com.example.cyshare_frontend.model.user_booking_data;

import java.util.ArrayList;

public class unit_tester2 extends AppCompatActivity {
    private user_booking_data ubd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_tester2);

        Intent intent = getIntent();
        ubd = (user_booking_data) intent.getSerializableExtra("userData");

        TextView utTXT = findViewById(R.id.unitTestTXT);
        ArrayList<String> tempArr = ubd.getAllBookingData(this);
        utTXT.setText(tempArr.get(0));

    }
}