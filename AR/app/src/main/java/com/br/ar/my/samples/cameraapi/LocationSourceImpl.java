package com.br.ar.my.samples.cameraapi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.br.ar.R;
import com.google.android.gms.maps.LocationSource;

import java.util.List;


/**
 * Created by C.Lucas on 26/02/2017.
 */

public class LocationSourceImpl implements LocationSource, ActivityCompat.OnRequestPermissionsResultCallback {

    private OnLocationChangedListener onLocationChangedListener;

    public static class MyLocationListener implements LocationListener {

        private Context context;

        public MyLocationListener() {}
        public MyLocationListener(Context context) {
        }

        @Override
        public void onLocationChanged(final Location location) {
            if(context != null) {
                final Activity activity = ((Activity) context);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String fmt = String.format("Altitude: %f\nLat: %f, Lon: %f\nBearing: %f"
                                ,location.getAltitude()
                                ,location.getLatitude()
                                ,location.getLongitude()
                                ,location.getBearing()
                        );
                        TextView textLocation = (TextView) activity.findViewById(R.id.text_location);
                        textLocation.setText(fmt);
                    }
                });
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }

    private static final String GPS_PROVIDER = LocationManager.GPS_PROVIDER;
    private static final String NET_PROVIDER = LocationManager.NETWORK_PROVIDER;

    public static final int REQUEST_PERMISSION_LOCATION = 0xf1;
    public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private static LocationListener locationListener;
    private static LocationManager locationManager;

    public static Location find(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        if (locationManager != null) {
            boolean isEnabledGPS = locationManager.isProviderEnabled(GPS_PROVIDER);
            boolean isEnabledNET = locationManager.isProviderEnabled(NET_PROVIDER);
            String enabledProvider = "";
            if (isEnabledGPS) {
                enabledProvider = GPS_PROVIDER;
            }
            else if(isEnabledNET) {
                enabledProvider = NET_PROVIDER;
            }

            else {
                return location;
            }

            List<String> providers = locationManager.getProviders(true);
            if (providers != null && providers.size() > 0) {}

            long timer = 5;
            float distance = 10.0f;
            locationListener = new MyLocationListener();

            if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                         (Activity) context
                        ,new String [] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}
                        ,REQUEST_PERMISSION_LOCATION
                );
            }
            else {
                locationManager.requestLocationUpdates(enabledProvider, timer, distance, locationListener);

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

                boolean onlyTurnOn = true;
                String bestProvider = locationManager.getBestProvider(criteria, onlyTurnOn );

                location = locationManager.getLastKnownLocation(enabledProvider);

                if(location != null) {
                    Log.i("FIND_LOCATION", String.format("%f %f\n%f %f", location.getLatitude(), location.getLongitude()
                            ,location.getAltitude(), location.getBearing()));
                }
            }
        }

        return location;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_LOCATION && permissions != null && permissions.length > 0) {
            if(grantResults[0] == PERMISSION_GRANTED) {
                String permission = permissions[0];
            }
        }
    }
}
