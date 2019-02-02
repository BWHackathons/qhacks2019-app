package com.example.konvoi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.Menu;
import android.app.Activity;



import com.snapchat.kit.sdk.Bitmoji;
import com.snapchat.kit.sdk.SnapLogin;
import com.snapchat.kit.sdk.bitmoji.networking.FetchAvatarUrlCallback;
import com.snapchat.kit.sdk.core.controller.LoginStateController;


public class MainActivity extends AppCompatActivity implements LoginStateController.OnLoginStateChangedListener {


    private Button button;
    final Context context = this;

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
        SnapLogin.getButton(this, (ViewGroup) findViewById(R.id.login_button));
        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(this);


    }

    @Override
    public void onLoginSucceeded() {
        findViewById(R.id.login_button).setVisibility(View.GONE);
        Bitmoji.fetchAvatarUrl(this, new FetchAvatarUrlCallback() {
            @Override
            public void onSuccess(@Nullable String avatarUrl) {
                Log.d("bitmoji", avatarUrl);
            }

            @Override
            public void onFailure(boolean isNetworkError, int statusCode) {
                Log.d("bitmoji","FUCK ME");
            }
        });
    }

    @Override
    public void onLoginFailed() {
        failedLogin();
    }


    public void failedLogin() {
        AlertDialog.Builder loginError = new AlertDialog.Builder(context);
        loginError.setTitle("THERE WAS A LOGIN ERROR \n PLEASE TRY AGAIN");

        loginError.setMessage("this is the message")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

        loginError.show();


    }

    @Override
    public void onLogout() {

    }

//        button = (Button) findViewById(R.id.createTrip);
//        button = (Button) findViewById(R.id.createTrip);

}

