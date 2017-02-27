package com.br.ar.my.samples;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.view.OnTouchBeyondarViewListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.br.ar.R;
import com.br.ar.my.samples.cameraapi.LocationSourceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private Location location;

    private World world;

    public static final int REQUEST_PERMISSION_LOCATION = 0xf1;
    public static final int REQUEST_LIST_PERMISSION = 0xff;
    public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        // We create the world and fill it ...
        //mWorld = CustomWorldHelper.generateObjects(this);
        // ... and send it to the fragment
        //mBeyondarFragment.setWorld(mWorld);
        //mBeyondarFragment.showFPS(true);

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
            }
        });


        mBeyondarFragment.setOnClickBeyondarObjectListener(new OnClickBeyondarObjectListener() {
            @Override
            public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
                Toast.makeText(context
                        ,String.format("TOUCH %s", beyondarObjects.size() > 0 ? beyondarObjects.get(0).toString() : "Nenhum objeto")
                        ,Toast.LENGTH_SHORT)
                        .show();

                for(BeyondarObject beyondarObject : beyondarObjects) {
                    com.beyondar.android.util.math.geom.Point3 point3 = beyondarObject.getAngle();
                }
            }
        });


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
            // We also can see the Frames per seconds
            World world = CustomWorldHelper.generateObjects(this); //init(this);
            mBeyondarFragment.setWorld(world);
        }
        requestPermissions();
    }

    private World init(Context context) {
        if(world != null)
            return world;

        world = new World(context);
        LocationSourceImpl.find(context);
// The user can set the default bitmap. This is useful if you are
// loading images form Internet and the connection get lost
        world.setDefaultBitmap(R.drawable.beyondar_default_unknow_icon, 1);

// User position (you can change it using the GPS listeners form Android
// API)
        if(location != null)
            world.setGeoPosition(location.getLatitude(), location.getLongitude());

// Create an object with an image in the app resources.
        GeoObject go1 = new GeoObject(1l);
        go1.setGeoPosition(-23.550602d, -46.3775692d);
        go1.setImageResource(R.drawable.creature_1);
        go1.setName("Creature 1");
        //go1.setImageUri("http://beyondar.com/sites/default/files/logo_reduced.png");
        //go1.setImageUri("/sdcard/someImageInYourSDcard.jpeg");


// And the same goes for the app assets
        GeoObject go2 = new GeoObject(2l);
        go2.setGeoPosition(-23.5595922d, -46.7313849d);
        go1.setImageResource(R.drawable.googlelogo);
        go2.setName("Image from assets");

        GeoObject go3 = new GeoObject(4l);
        go3.setGeoPosition(41.90518862002349d, 2.565662767707665d);
        go3.setImageUri("assets://creature_7.png");
        go3.setName("Image from assets");

// We add this GeoObjects to the world
        world.addBeyondarObject(go1);
        world.addBeyondarObject(go2);
        world.addBeyondarObject(go3);
        return world;
    }



    private void requestPermissions() {
        List<String> permissionsDenied = new ArrayList<>();
        String permissions [] = {
             android.Manifest.permission.ACCESS_FINE_LOCATION
            ,android.Manifest.permission.ACCESS_COARSE_LOCATION
            ,android.Manifest.permission.ACCESS_WIFI_STATE
            ,android.Manifest.permission.CAMERA
            ,Manifest.permission.INTERNET
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
                    init(this);
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
