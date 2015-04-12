package com.example.anh.watchdawgs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by Anh on 4/11/2015.
 */
public class AppLocationService extends Service implements LocationListener {
    Location location;
    LocationManager locationManager;
    MainActivity.GeocoderHandler locHandler;
    public AppLocationService(Context context, MainActivity.GeocoderHandler handler) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locHandler = handler;
    }


//    public Location getLocation(String provider) {
//        if (locationManager.isProviderEnabled(provider)) {
//            locationManager.requestLocationUpdates(provider, 0, 0, this);
//            if (locationManager != null) {
//                location = locationManager.getLastKnownLocation(provider);
//                return location;
//            }
//        }
//        return null;
//    }

    public void onLocationChanged(Location loc) {
        // Called when a new location is found by the network location provider.
//        Double lat = loc.getLatitude();
//        Double lng = loc.getLongitude();
//        TextView tv = (TextView) findViewById(R.id.textView1);
//        tv.setText("Lat: " + lat + " Long: " + lng);


            Message message = Message.obtain();
            message.setTarget(locHandler);
            if (loc != null) {
                Bundle bundle = new Bundle();
                bundle.putString("Lat", Double.toString(loc.getLatitude()));
                bundle.putString("Long", Double.toString(loc.getLongitude()));
                message.setData(bundle);
            }
            message.sendToTarget();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Register the listener with the Location Manager to receive location updates
    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
}

