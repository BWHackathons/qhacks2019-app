package com.example.konvoi;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.Menu;
import android.app.Activity;


public class MainActivity extends AppCompatActivity {


    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//link to createTrip Activity
        button = findViewById(R.id.createTrip);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, createTrip.class);
                startActivity(intent);

            }
        });

//link to joinTrip Activity
        button = (Button) findViewById(R.id.join);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, joinTrip.class);
                startActivity(intent);

            }


        });


    }


//        button = (Button) findViewById(R.id.createTrip);
//        button = (Button) findViewById(R.id.createTrip);

//
//    RequestQueue mRequestQueue;
//
//    // Instantiate the cache
//    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
//
//    // Set up the network to use HttpURLConnection as the HTTP client.
//    Network network = new BasicNetwork(new HurlStack());
//
//// Instantiate the RequestQueue with the cache and network.
//    mRequestQueue = new RequestQueue(cache, network);
//
//// Start the queue
//mRequestQueue.start();
//
//    String url ="http://10.217.11.57";
//
//    // Formulate the request and handle the response.
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//            new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    // Do something with the response
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // Handle error
//                }
//            });
//
//// Add the request to the RequestQueue.
//mRequestQueue.add(stringRequest);
//
//






}

