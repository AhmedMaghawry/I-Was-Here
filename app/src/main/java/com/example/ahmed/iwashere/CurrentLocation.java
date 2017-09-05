package com.example.ahmed.iwashere;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

public class CurrentLocation extends Service {
    public CurrentLocation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 500, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                sendLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        return START_NOT_STICKY;
    }

    private void sendLocation(Location location) {
        Intent intent = new Intent("current.Location");
        intent.putExtra("location_latitude", location.getLatitude());
        intent.putExtra("location_longitude", location.getLongitude());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
