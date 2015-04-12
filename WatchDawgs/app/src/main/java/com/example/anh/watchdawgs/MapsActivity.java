//package com.example.anh.watchdawgs;
//
//import android.app.Activity;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Message;
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
//import android.widget.TextView;
//
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.HashMap;
//import java.util.List;
//
//import android.os.Handler;
//
//public class MapsActivity extends FragmentActivity {
//
//    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
//    private AppLocationService applicationService;
//    private GeocoderHandler handler;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        //setUpMapIfNeeded();
//        //setContentView(R.layout.firstpage);
//        handler = new GeocoderHandler(MapsActivity.this);
//        applicationService = new AppLocationService(MapsActivity.this, handler);
//
//
//
//        // Define a listener that responds to location updates
//        //LocationListener locationListener = new LocationListener() {
//    }
////    @Override
////    public void onResume() {
////        super.onResume();
////        //setUpMapIfNeeded();
////    }
//
//    /**
//     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
//     * installed) and the map has not already been instantiated.. This will ensure that we only ever
//     * call {@link #setUpMap()} once when {@link #mMap} is not null.
//     * <p/>
//     * If it isn't installed {@link SupportMapFragment} (and
//     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
//     * install/update the Google Play services APK on their device.
//     * <p/>
//     * A user can return to this FragmentActivity after following the prompt and correctly
//     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
//     * have been completely destroyed during this process (it is likely that it would only be
//     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
//     * method in {@link #onResume()} to guarantee that it will be called.
//     */
////    private void setUpMapIfNeeded() {
////        // Do a null check to confirm that we have not already instantiated the map.
////        if (mMap == null) {
////            // Try to obtain the map from the SupportMapFragment.
////            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
////                    .getMap();
////            // Check if we were successful in obtaining the map.
////            if (mMap != null) {
////                setUpMap();
////            }
////        }
////    }
//
//    /**
//     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
//     * just add a marker near Africa.
//     * <p/>
//     * This should only be called once and when we are sure that {@link #mMap} is not null.
//     */
////    private void setUpMap() {
////        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
////        mMap.setMyLocationEnabled(true);
////    }
//
//    public class GeocoderHandler extends Handler {
//        HashMap<String, Integer> localityToSketchMap = new HashMap<>();
//        Context context;
//        boolean sketch = false;
//        public GeocoderHandler(Context context) {
//            buildMap();
//            this.context=context;
//        }
//
//        @Override
//        public void handleMessage(Message message) {
//            Bundle bundle = message.getData();
//            Double latitude = Double.parseDouble(bundle.getString("Lat"));
//            Double longitude = Double.parseDouble(bundle.getString("Long"));
//            Geocoder geocoder = new Geocoder(getApplicationContext());
//            List<Address> matches = null;
//            try {
//                matches = geocoder.getFromLocation(latitude, longitude, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
//            if (sketch == false && localityToSketchMap.containsKey(bestMatch.getLocality())
//                    && localityToSketchMap.get(bestMatch.getLocality()) > 0) {
//                TextView tv = (TextView) findViewById(R.id.textView1);
//                tv.setText(bestMatch.getLocality());
//                String text = localityToSketchMap.get(
//                        bestMatch.getLocality()) == 2 ? "You are in an EXTREMELY sketchy area." : "This area is kinda sketchy.";
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.drawable.six).setContentTitle("WARNING:").setContentText(text);
//                mBuilder.setVibrate(new long[] {1000, 1000, 1000});
////                int color = localityToSketchMap.get(
////                        bestMatch.getLocality()) == 2 ? Color.RED : Color.YELLOW;
//                mBuilder.setLights(Color.RED, 3000, 3000);
//                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                int notificationID = 100;
//
//                Intent resultIntent = new Intent(context, MapsActivity.class);
//                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                mBuilder.setContentIntent(resultPendingIntent);
//                mBuilder.setAutoCancel(true);
//                mNotificationManager.notify(notificationID, mBuilder.build());
//                sketch = true;
//            }
//            else if (sketch == true && localityToSketchMap.containsKey(bestMatch.getLocality())
//                    && localityToSketchMap.get(bestMatch.getLocality()) == 0) {
//                sketch = false;
//           }
//
//        }
//
//        public void buildMap() {
//            //build the locality to crime map
//            BufferedReader reader = null;
//            try {
//                reader = new BufferedReader(
//                        new InputStreamReader(getAssets().open("DC_CRIME_CLUSTER_INFO.txt")));
//
//                // do reading, usually loop until end of file reading
//                String mLine = reader.readLine();
//                while (mLine != null) {
////                    //process line
//                    mLine = reader.readLine();
//                    if (mLine != null) {
//                        String[] s = mLine.split(",");
//                        double weight = Double.parseDouble(s[1]);
//                        int rank = 0;
//                        if (weight >=3900 && weight <= 120000) {
//                            rank = 1;
//                        } else if (weight >= 120000) {
//                            rank = 2;
//                        }
//
//                        localityToSketchMap.put(s[0],rank);
//                    }
//                }
//            } catch (IOException e) {
//                //log the exception
//            } finally {
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        //log the exception
//                    }
//                }
//            }
//        }
//    }
//}
