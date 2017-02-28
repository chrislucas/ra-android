package com.br.ar.my.samples;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.plugin.radar.RadarView;
import com.beyondar.android.plugin.radar.RadarWorldPlugin;
import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.util.math.geom.Point3;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.BeyondarViewAdapter;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.view.OnTouchBeyondarViewListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.BeyondarObjectList;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.br.ar.R;
import com.br.ar.my.samples.cameraapi.LocationSourceImpl;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private Location location;

    private RadarView radarView;
    private RadarWorldPlugin radarWorldPlugin;
    private SeekBar mSeekBar;

    private TextView distance;
    private Handler handler;

    private World world;

    public static final int REQUEST_PERMISSION_LOCATION = 0xf1;
    public static final int REQUEST_LIST_PERMISSION = 0xff;
    public static final int MAX_DISTANCE = 300;
    public static final int MIN_DISTANCE = 150;
    public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;


    private TextView textLocation;
    private Vibrator vibrator;

    public static class ARViewAdapter extends BeyondarViewAdapter {

        LayoutInflater layoutInflater;
        public ARViewAdapter(Context context) {
            super(context);
            layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(BeyondarObject beyondarObject, View view, ViewGroup viewGroup) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        requestPermissions();

        BeyondarLocationManager.setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondar_fragment);

        radarView       = (RadarView) findViewById(R.id.radarView);
        textLocation    = (TextView) findViewById(R.id.text_location);
        distance        = (TextView) findViewById(R.id.text_distance);
        mSeekBar        = (SeekBar) findViewById(R.id.seekBarMaxDistance);

        // We create the world and fill it ...
        //mWorld = CustomWorldHelper.generateObjects(this);
        // ... and send it to the fragment
        //mBeyondarFragment.setWorld(mWorld);
        //mBeyondarFragment.showFPS(true);

        handler = new Handler();

        radarWorldPlugin = new RadarWorldPlugin(this);
        radarWorldPlugin.setRadarView(radarView);
        radarWorldPlugin.setMaxDistance(MIN_DISTANCE);
        radarWorldPlugin.setListColor(CustomWorldHelper.LIST_TYPE_EXAMPLE_1, Color.RED);
        radarWorldPlugin.setListDotRadius(CustomWorldHelper.LIST_TYPE_EXAMPLE_1, 6);
        mBeyondarFragment.setSensorDelay(SensorManager.SENSOR_DELAY_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this
                    ,new String [] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}
                    , REQUEST_PERMISSION_LOCATION
            );
        }

        else {
            location = LocationSourceImpl.find(this);
            updateTextLocation(location);
            // We also can see the Frames per seconds
            World world = CustomWorldHelper.generateObjects(this, location); //init(this);

            // BeyondarLocationManager.addWorldLocationUpdate(world);
            LocationSourceImpl.MyLocationListener listener = new LocationSourceImpl.MyLocationListener(this);
            BeyondarLocationManager.addLocationListener(listener);

            BeyondarObjectList objects = world.getBeyondarObjectList(World.LIST_TYPE_DEFAULT);
            if(objects != null) {
                Iterator<BeyondarObject> it = objects.iterator();
                while(it.hasNext()) {
                    BeyondarObject beyondarObject = it.next();
                    GeoObject geoObject = (GeoObject) beyondarObject;
                    Log.i("DISTANCE_OBJECT", String.format("%f", geoObject.getDistanceFromUser()));
                    // BeyondarLocationManager.addGeoObjectLocationUpdate(beyondarObject);
                }
            }

            //radarWorldPlugin.setup(world);
            world.addPlugin(radarWorldPlugin);
            mBeyondarFragment.setWorld(world);
        }



        final Context context = this;
        mBeyondarFragment.setOnTouchBeyondarViewListener(new OnTouchBeyondarViewListener() {
            @Override
            public void onTouchBeyondarView(MotionEvent motionEvent, BeyondarGLSurfaceView beyondarGLSurfaceView) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                if(beyondarGLSurfaceView != null) {
                    beyondarGLSurfaceView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "GLSurface", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                ArrayList<BeyondarObject> beyondarObjects = new ArrayList<BeyondarObject>();
                beyondarGLSurfaceView.getBeyondarObjectsOnScreenCoordinates(x, y, beyondarObjects);
                for(BeyondarObject beyondarObject : beyondarObjects) {
                    Log.i("BEYOND_OBJECTS", beyondarObject.getName());
                }
            }
        });


        mBeyondarFragment.setOnClickBeyondarObjectListener(new OnClickBeyondarObjectListener() {
            @Override
            public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
                for(BeyondarObject beyondarObject : beyondarObjects) {
                    Point3 point3 = beyondarObject.getAngle();
                    float [] coord = point3.getCoordinatesArray();
                    if(coord != null)
                        Toast.makeText(context
                                ,String.format("%.2f, %.2f, %.2f", coord[0], coord[1], coord[2])
                                ,Toast.LENGTH_LONG).show();;
                }
            }
        });


        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if(radarWorldPlugin == null)
                    return;
                if(seekBar == mSeekBar) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            distance.setText(String.valueOf(progress));
                        }
                    });

                    radarWorldPlugin.setMaxDistance(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSeekBar.setMax(MAX_DISTANCE);
        mSeekBar.setProgress(10);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }


    private void updateTextLocation(final Location location) {
        if(location != null) {
            /*
            if(vibrator != null)
                vibrator.vibrate(new long[] {1000, 100, 1000, 100}, 3000);
            */

            handler.post(new Runnable() {
                @Override
                public void run() {
                    String fmt = String.format("Altitude: %f\nLat: %f, Lon: %f\nBearing: %f"
                            ,location.getAltitude()
                            ,location.getLatitude()
                            ,location.getLongitude()
                            ,location.getBearing()
                    );
                    textLocation.setText(fmt);
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BeyondarLocationManager.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BeyondarLocationManager.disable();
    }

    private void requestPermissions() {
        List<String> permissionsDenied = new ArrayList<>();
        String permissions [] = {
             android.Manifest.permission.ACCESS_FINE_LOCATION
            ,android.Manifest.permission.ACCESS_COARSE_LOCATION
            ,android.Manifest.permission.ACCESS_WIFI_STATE
            ,android.Manifest.permission.CAMERA
            ,Manifest.permission.INTERNET
            ,Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        for (String permission : permissions) {
            boolean denied = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;
            if(denied) {
                permissionsDenied.add(permission);
            }
        }

        String arrayPermissions [] = new String[permissionsDenied.size()];
        permissionsDenied.toArray(arrayPermissions);
        if(arrayPermissions.length > 0)
            ActivityCompat.requestPermissions(this, arrayPermissions, REQUEST_LIST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LIST_PERMISSION:
                break;
            case REQUEST_PERMISSION_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location = LocationSourceImpl.find(this);
                    updateTextLocation(location);
                    World world = CustomWorldHelper.generateObjects(this, location);
                    mBeyondarFragment.setWorld(world);
                }
                break;

        }
        if(permissions != null && permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            for (String permission : permissions) {
                Log.i("PERMISSION_ACTIVITY_2", permission);
            }
        }
    }
}
