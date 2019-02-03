package com.example.konvoi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class mainPage extends AppCompatActivity implements OnMapReadyCallback {
    private Button button;
    private GoogleMap mMap;
    private MapView mapView;
    GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionGranted = false;
    private RequestQueue rq;
    private Handler handler;
    private JSONObject responseJson = new JSONObject();


    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "mainPage";
    private static final int PERM_REQ_CODE = 43298;
    private static final String serverIp = "http://10.217.11.57";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        try {
            responseJson = new JSONObject(this.getIntent().getStringExtra("responseJson"));
            Log.d("fuckemup", responseJson.toString());
            TextView tripName = (TextView) findViewById(R.id.tripName);
            tripName.setText(responseJson.getJSONObject("group").getString("tripName"));
        }catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Error in server response!", Toast.LENGTH_LONG).show();
            finish();
        }

        handler = new Handler();

        rq = Volley.newRequestQueue(this);

        mapView = findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if(!mLocationPermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERM_REQ_CODE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapView.getMapAsync(this);

        final Button btnSlogin = (Button) findViewById(R.id.btn_slogin);
        final Button btnPlay = (Button) findViewById(R.id.btn_play);
        final Button btnPause = (Button) findViewById(R.id.btn_pause);
        try {
            final String gCode = responseJson.getJSONObject("group").getString("groupId");
            final int uId = responseJson.getJSONObject("user").getInt("userId");
            final int uType = responseJson.getJSONObject("user").getInt("type");
            btnSlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = serverIp + "/users/" + uId + "/spotify/login";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    btnSlogin.setVisibility(View.INVISIBLE);
                    if(uType == 1) {
                        btnPause.setVisibility(View.VISIBLE);
                        btnPlay.setVisibility(View.VISIBLE);
                    }
                }
            });
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, serverIp + "/groups/" + gCode + "/music/play",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("webreq", response);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("webreq", "spotify play failed");
                            error.printStackTrace();
                        }
                    });

                    rq.add(stringRequest);
                }
            });

            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, serverIp + "/groups/" + gCode + "/music/pause",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("webreq", response);
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("webreq", "spotify pause failed");
                            error.printStackTrace();
                        }
                    });

                    rq.add(stringRequest);
                }
            });
        }catch(JSONException e) {
            e.printStackTrace();
        }

        String mBitmoji = PreferenceManager.getDefaultSharedPreferences(this).getString("bitmoji", "");
        if(mBitmoji.length() > 0) {
            try {
                JSONObject body = new JSONObject();
                body.put("bitmojiUrl", mBitmoji);
                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, serverIp + "/users/" + responseJson.getJSONObject("user").getInt("userId")+"/setBitmoji", body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                    }
                });
                rq.add(jsonReq);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }


    }

    private void getDeviceLocation(final boolean move) {
        Log.d(TAG, "GetDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            if(move)
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);

                            try {
                                JSONObject body = new JSONObject();
                                body.put("lat", currentLocation.getLatitude());
                                body.put("lng", currentLocation.getLongitude());
                                JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST, serverIp + "/users/" + responseJson.getJSONObject("user").getInt("userId")+"/location", body, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString());
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        error.printStackTrace();

                                    }
                                });
                                rq.add(jsonReq);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }


                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(mainPage.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMarkers();

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                addMarkers();
                getDeviceLocation(false);
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
//        // Add a marker in Sydney, Australia, and move the camera.
//        final LatLng sydney = new LatLng(-34, 151);
//        LatLng toronto = new LatLng(-34, 140);
//        LatLng london = new LatLng(-40, 130);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bmp = getBitmapFromURL("https://sdk.bitmoji.com/render/panel/3a275541-c977-4a9c-8fd2-1f445bb222e2-AUdudEZxV7EC49Y2~Ki7Rmbf_i4qeg-v1.png?transparent=1&palette=1");
//                final BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bmp);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(bd));
//                    }
//                });
//
//            }
//        }).start();
//
//
//        mMap.addMarker(new MarkerOptions().position(toronto).title("Marker in Sydney"));
//        mMap.addMarker(new MarkerOptions().position(london).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (mLocationPermissionGranted) {
            getDeviceLocation(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);

        }
    }

    private void addMarkers() {
        try{
            mMap.clear();
            JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET, serverIp + "/groups/" + responseJson.getJSONObject("group").getString("groupId")+"/locations", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                     for(int i=0; i<response.length(); i++) {
                        try{
                            final LatLng pos = new LatLng(((JSONObject) response.get(i)).getDouble("lat"), ((JSONObject) response.get(i)).getDouble("lng"));
                            final String burl = ((JSONObject) response.get(i)).getString("bitmojiUrl");
                            if(burl != null && !burl.equals("")){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap bmp = getBitmapFromURL(burl);
                                        final BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bmp);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMap.addMarker(new MarkerOptions().position(pos).icon(bd));
                                            }
                                        });

                                    }
                                }).start();
                            }else{
                                mMap.addMarker(new MarkerOptions().position(pos).title("Some anon person"));
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    error.printStackTrace();

                }
            });
            rq.add(jsonReq);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERM_REQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation(true);
                    mMap.setMyLocationEnabled(true);
                } else {
                    mLocationPermissionGranted = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    @Override
    public void onStart(){
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle random) {
        super.onSaveInstanceState(random);
        mapView.onSaveInstanceState(random);
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }



    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



//     if (mGoogleApiClient == null) {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//if (mGoogleApiClient != null) {
//        mGoogleApiClient.connect();
//    }







}