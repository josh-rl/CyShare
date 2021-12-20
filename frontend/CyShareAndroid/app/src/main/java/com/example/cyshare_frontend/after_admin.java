package com.example.cyshare_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class after_admin extends AppCompatActivity {
private Button Bdelete;
ListView listView;
private Button Udelete;
private ProgressBar bar;
private Handler handler = new Handler();
private int counter =0 ;
private TextView t;
    public ArrayList<String> arrey;
    ArrayAdapter<String> arrayAdapter;
    @Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_admin);
        listView = (ListView) findViewById(R.id.list_afterAdmin);
        //Bdelete = findViewById(R.id.del_button);
        //Udelete = findViewById(R.id.after_admin2);
        bar = (ProgressBar)findViewById(R.id.afteradminprog);
        t = (TextView) findViewById(R.id.afteradmin_text);
        listView.setVisibility(View.INVISIBLE);
       new Thread(new Runnable() {
           @Override
           public void run() {
               while(counter <100)
               {
                   counter ++;
                   android.os.SystemClock.sleep(20);
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           bar.setProgress(counter);
                       }
                   });
               }
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       t.setText("User Booking");
                       listView.setVisibility(View.VISIBLE);
                       bar.setVisibility(View.GONE);
                   }
               });

           }
       }).start();


        listView.setOnItemClickListener((adapterView, view, position, id) ->
                arrayAdapter.remove(arrayAdapter.getItem(position)));

        arrey = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrey);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, position, id) ->
                arrayAdapter.remove(arrayAdapter.getItem(position)));

        arrey.add("Name: Tanay" + "\n" + "Date: 09/01/2021 " + "\n" +"Role: Passenger" + "\n" + "time: 07:45" + "\n" + "isBooked: Booked" + "\n"+ "Location: 4019 Juneberry Rd, Naperville, Il" );
        arrey.add("Name: Tanay" + "\n" + "Date: 12/09/2021" + "\n" +"Role: Passenger" + "\n" + "time: 09:40" + "\n" + "isBooked: null" + "\n"+ "Location: 2300 lincoln way, Ames, IA" );
        //Bdelete.setOnClickListener(view -> {
        // change to delete things in the array
        //Bdelete.setVisibility(View.INVISIBLE);

   // });
     //   Udelete.setOnClickListener(v ->

            //should delete the user
        // Udelete.setOnClickListener(V.INVISIBLE);
      //  });

    }

}
