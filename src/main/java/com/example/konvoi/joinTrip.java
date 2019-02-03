package com.example.konvoi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class joinTrip extends AppCompatActivity {

    private Button button;
    private Context context = this;

    private static final String serverIp = "http://10.217.11.57";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_trip);

        final EditText codeInput = (EditText) findViewById(R.id.givenCode);

        button = (Button) findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                RequestQueue rq = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, serverIp + "/groups/join/"+codeInput.getText().toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(joinTrip.this, mainPage.class);
                                intent.putExtra("responseJson", response);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("webreq", "joining group failed");
                        error.printStackTrace();
                    }
                });

                // Add the request to the RequestQueue.
                rq.add(stringRequest);

            }
        });

    }
}

