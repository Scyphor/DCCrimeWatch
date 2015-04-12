package com.example.anh.watchdawgs;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private AppLocationService applicationService;
    private GeocoderHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
        //setUpMapIfNeeded();
        setContentView(R.layout.firstpage);
        handler = new GeocoderHandler(MainActivity.this, MainActivity.this);
        applicationService = new AppLocationService(MainActivity.this, handler);

        Button btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(this);

        // Define a listener that responds to location updates
        //LocationListener locationListener = new LocationListener() {
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, MainPageIntent.class);
        startActivity(i);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        //setUpMapIfNeeded();
//    }


    public class GeocoderHandler extends Handler {
        HashMap<String, Integer> localityToSketchMap = new HashMap<>();
        Context context;
        View.OnClickListener ocl;
        boolean sketch = false;
        public GeocoderHandler(Context context, View.OnClickListener ocl) {
            buildMap();
            this.context=context;
            this.ocl=ocl;
        }

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            Double latitude = Double.parseDouble(bundle.getString("Lat"));
            Double longitude = Double.parseDouble(bundle.getString("Long"));
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> matches = null;
            try {
                matches = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            if (sketch == false && localityToSketchMap.containsKey(bestMatch.getLocality())
                    && localityToSketchMap.get(bestMatch.getLocality()) > 0) {
                String text = localityToSketchMap.get(
                        bestMatch.getLocality()) == 2 ? "You are in an EXTREMELY sketchy area." : "This area is kinda sketchy.";
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.six).setContentTitle("WARNING:").setContentText(text);
                mBuilder.setVibrate(new long[] {1000, 1000, 1000});
//                int color = localityToSketchMap.get(
//                        bestMatch.getLocality()) == 2 ? Color.RED : Color.YELLOW;
                mBuilder.setLights(Color.RED, 3000, 3000);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int notificationID = 100;

                Intent resultIntent = new Intent(context, MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setAutoCancel(true);
                mNotificationManager.notify(notificationID, mBuilder.build());
                int view = localityToSketchMap.get(bestMatch.getLocality()) == 2 ?  R.layout.redpage : R.layout.yellowpage;
                setContentView(view);
                TextView tv = (TextView) findViewById(R.id.textView1);
                tv.setText(bestMatch.getLocality());
                String warnText = localityToSketchMap.get(bestMatch.getLocality()) == 2 ?  "High Crime Area" : "Medium Crime Area";
                TextView tv2 = (TextView) findViewById(R.id.editText);
                tv2.setText(warnText);
                Button btn1 = (Button) findViewById(R.id.button);
                btn1.setOnClickListener(ocl);
                sketch = true;
            }
            else if (sketch == true && localityToSketchMap.containsKey(bestMatch.getLocality())
                    && localityToSketchMap.get(bestMatch.getLocality()) == 0) {
                setContentView(R.layout.firstpage);
                TextView tv = (TextView) findViewById(R.id.textView1);
                tv.setText(bestMatch.getLocality());
                TextView tv2 = (TextView) findViewById(R.id.editText);
                tv2.setText("Low Crime Area");
                Button btn1 = (Button) findViewById(R.id.button);
                btn1.setOnClickListener(ocl);
                sketch = false;
            }

        }

        public void buildMap() {
            //build the locality to crime map
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("DC_CRIME_CLUSTER_INFO.txt")));

                // do reading, usually loop until end of file reading
                String mLine = reader.readLine();
                while (mLine != null) {
//                    //process line
                    mLine = reader.readLine();
                    if (mLine != null) {
                        String[] s = mLine.split(",");
                        double weight = Double.parseDouble(s[1]);
                        int rank = 0;
                        if (weight >=3900 && weight <= 120000) {
                            rank = 1;
                        } else if (weight >= 120000) {
                            rank = 2;
                        }

                        localityToSketchMap.put(s[0],rank);
                    }
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
        }
    }

}
