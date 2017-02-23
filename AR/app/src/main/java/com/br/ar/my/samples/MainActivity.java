package com.br.ar.my.samples;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.view.OnTouchBeyondarViewListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.br.ar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    /*
    *
    * Poli USP
    * -23.5570464,-46.7328786,15
    *
    * IME USP
    * -23.5595922,-46.7313849,15
    *
    * -23.5506027,-46.3775692
    *
    * */

    private Map<String, Double []> locations;

    private BeyondarFragmentSupport mBeyondarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.frag_ar);
        final Context context = this;
        mBeyondarFragment.setOnClickBeyondarObjectListener(new OnClickBeyondarObjectListener() {
            @Override
            public void onClickBeyondarObject(ArrayList<BeyondarObject> arrayList) {
                Toast.makeText(context
                        ,String.format("TOUCH %s", arrayList.size() > 0 ? arrayList.get(0).toString() : "Nenhum objeto")
                        ,Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mBeyondarFragment.setOnTouchBeyondarViewListener(new OnTouchBeyondarViewListener() {
            @Override
            public void onTouchBeyondarView(MotionEvent motionEvent, BeyondarGLSurfaceView beyondarGLSurfaceView) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                // ArrayList<BeyondarObject> beyondarObjects = new ArrayList<BeyondarObject>();
                // mBeyondarFragment.getBeyondarObjectsOnScreenCoordinates(x, y, beyondarObjects);

                // a documentacao nao recomenda executar esse metodo na UI Thread
                mBeyondarFragment.getBeyondarObjectsOnScreenCoordinates(x, y);

                String text = "";
                int action = motionEvent.getAction();
                switch ( action ) {
                    case MotionEvent.ACTION_DOWN:
                        text = "Event type ACTION_DOWN: ";
                        break;
                    case MotionEvent.ACTION_UP:
                        text = "Event type ACTION_UP: ";
                        break;
                    case MotionEvent.ACTION_MOVE:
                        text = "Event type ACTION_MOVE: ";
                        break;
                    default:
                        break;
                }
                Toast.makeText(context
                        ,String.format("%s %d", text, action)
                        ,Toast.LENGTH_SHORT)
                        .show();
            }
        });
        /*
        FragmentExampleAR fragAr = FragmentExampleAR.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.model, fragAr);
        ft.commit();
        */
        Location location = new Location("");
        location.setLatitude(-23.5506027);
        location.setLongitude(-46.3775692);
        String local      = "Casa";

        World world = createWorldsInstanceSync(this, location);
        world.addBeyondarObject(createGeoObject(location, local, R.drawable.googlelogo));

        location.setLatitude(-23.5595922);
        location.setLongitude(-46.7313849);
        local      = "IME";
        world.addBeyondarObject(createGeoObject(location, local, R.drawable.ccsl));

        location.setLatitude(-23.5570464);
        location.setLongitude(-46.7328786);
        local      = "POLI";
        world.addBeyondarObject(createGeoObject(location, local, R.drawable.poliusp));

        mBeyondarFragment.setWorld(world);
    }

    private World createWorldsInstanceSync(Context context, Location location) {
        World world = new World(context);
        world.setDefaultBitmap(R.drawable.logousp1, android.R.layout.simple_list_item_1);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        world.setGeoPosition(lat, lng);
        return world;
    }

    private GeoObject createGeoObject(Location location, String local, int drawable) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        GeoObject geoObject = new GeoObject(1l);
        geoObject.setGeoPosition(lat, lng);
        geoObject.setImageResource(drawable);
        geoObject.setName(local);
        return geoObject;
    }


    private World createWorldsInstanceASync(Context context, Location location, String local) {
        World world = new World(context);
        world.setDefaultBitmap(R.drawable.ccsl, android.R.layout.simple_list_item_1);

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        world.setGeoPosition(lat, lng);

        GeoObject geoObject = new GeoObject(2l);
        geoObject.setGeoPosition(lat, lng);
        // buscar imagem da URL
        geoObject.setImageUri("https://www.google.com.br/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png");
        // ou do sd card
        //geoObject.setImageUri("sdcard//");
        // pasta assets
        //geoObject.setImageUri("assets//");
        geoObject.setName(local);
        world.addBeyondarObject(geoObject);
        return world;
    }

    public static final int REQUEST_PERM_CODE = 0xff;

    private void requestPermissions() {
        List<String> permissionsDenied = new ArrayList<>();
        String permissions [] = {
                android.Manifest.permission.ACCESS_FINE_LOCATION
                ,android.Manifest.permission.ACCESS_COARSE_LOCATION
                ,android.Manifest.permission.ACCESS_WIFI_STATE
                ,android.Manifest.permission.CAMERA
        };

        for (String permission : permissions) {
            boolean denied = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;
            if(denied) {
                permissionsDenied.add(permission);
            }
        }

        String arrayPermissions [] = new String[permissionsDenied.size()];
        permissionsDenied.toArray(arrayPermissions);
        ActivityCompat.requestPermissions(this, arrayPermissions, REQUEST_PERM_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
