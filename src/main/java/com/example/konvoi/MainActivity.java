package com.example.konvoi;

import android.content.Intent;
import android.support.annotation.Nullable;
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

        Bitmoji.fetchAvatarUrl(context, new FetchAvatarUrlCallback()) {
            @Override
            public void onSuccess(@Nullable String avatarUrl) {
                //fuck
            }

            @Override
            public void onFailure(boolean isNetworkError, int statusCode){
                //fuck 2
            }
        }};
    }


//        button = (Button) findViewById(R.id.createTrip);
//        button = (Button) findViewById(R.id.createTrip);

}

