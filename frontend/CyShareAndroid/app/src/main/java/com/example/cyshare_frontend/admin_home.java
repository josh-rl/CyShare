package com.example.cyshare_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class admin_home<s1> extends AppCompatActivity {
private ListView listView;
private SearchView SearchView;
private ProgressBar prog;
private int counter =0;
private Handler handler= new Handler();


public ArrayList<String> arrey;
ArrayAdapter<String> arrayAdapter;
//SearchView view = (SearchView) findViewById(R.id.admin_search);

    String s2 =  "Josh";
    String s3 = "bhuwan";
    String s4 = "Tanay";
    String s5 = "Hugo";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        prog= (ProgressBar) findViewById(R.id.adminHomeProg);
        SearchView =(SearchView) findViewById(R.id.admin_search);
        listView = (ListView) findViewById(R.id.list_admin);

        listView.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(counter <100)
                {
                    counter ++;
                    android.os.SystemClock.sleep(20);
                    handler.post((new Runnable() {
                        @Override
                        public void run() {
                            prog.setProgress(counter);
                        }
                    }));

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setVisibility(View.VISIBLE);
                        prog.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
        //tried to get data but the app is very fast vs the volloy , so could not get the data

        Intent intent = getIntent();
        //HashMap<String, ArrayList<String>> usersInfo = (HashMap<String, ArrayList<String>>) intent.getSerializableExtra("adminData");
        //Log.i("Data Test",usersInfo.size()+"");
        //arrey = new ArrayList<>(usersInfo.keySet());
        arrey = new ArrayList<>();
        arrey.add(s2);
       arrey.add(s3);
        arrey.add(s4);
        arrey.add(s5);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrey);
        listView.setAdapter(arrayAdapter);
//        arrey.add(s2);
//        arrey.add(s3);
//        arrey.add(s4);
//        arrey.add(s5);
      listView.setOnItemClickListener((adapterView, view, position, id)
              -> to_afterAdmin());

             // Toast.makeText(admin_home.this,"b"+adapterView.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show());
      SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
          public boolean onQueryTextSubmit(String query)
          {
              admin_home.this.arrayAdapter.getFilter().filter(query);
             return false;
          }
          @Override
          public boolean onQueryTextChange(String newText) {
            admin_home.this.arrayAdapter.getFilter().filter(newText);
            return false;
          }
      });
    }
    private void to_afterAdmin() {
       Intent loadafterAdmin = new Intent(this, after_admin.class);
        startActivity(loadafterAdmin);
        //will have bundels

    }


}
