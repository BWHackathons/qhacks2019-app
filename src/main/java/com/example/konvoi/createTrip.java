package com.example.konvoi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class createTrip extends AppCompatActivity {

    private Button button;
    private Context context = this;

    private static final String serverIp = "http://10.217.11.57";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        final String code = this.getIntent().getStringExtra("groupId");
        TextView code_tv = (TextView) findViewById(R.id.code);
        code_tv.setText(code);

        final EditText et = (EditText) findViewById(R.id.tripName);

        button = (Button) findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                RequestQueue rq = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, serverIp + "/groups/join/"+code,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(createTrip.this, mainPage.class);
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

                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, serverIp + "/groups/"+code+"/setName/"+et.getText().toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //nothing to do
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("webreq", "joining group failed 2");
                        error.printStackTrace();
                    }
                });

                // Add the request to the RequestQueue.
                rq.add(stringRequest2);
                rq.add(stringRequest);

            }
        });

    }
}
