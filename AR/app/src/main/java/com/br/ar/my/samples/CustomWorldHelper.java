package com.br.ar.my.samples;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.br.ar.R;

import java.util.Random;

/**
 * Created by C.Lucas on 25/02/2017.
 */
public class CustomWorldHelper {

    public static final int LIST_TYPE_EXAMPLE_1 = 1;

    public static World sharedWorld;

    private static Random random = new Random();

    public static World generateObjects(Context context, Location location) {
        if (sharedWorld != null) {
            return sharedWorld;
        }
        sharedWorld = new World(context);
        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        sharedWorld.setDefaultImage(R.drawable.beyondar_default_unknow_icon);
        if(location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            //double alt = location.getAltitude();
            double offSetLat = .00015d;
            double offSetLng = .00015d;
            Log.i("CENTER_RADAR", String.format("%f %f", lat + (-offSetLat), lng + (-offSetLng)));
            sharedWorld.setGeoPosition(lat + (-offSetLat), lng + (-offSetLng) );

            GeoObject go4 = new GeoObject(4l);
            go4.setGeoPosition(lat, lng);
            go4.setImageResource(R.drawable.google_favicon_vector);
            go4.setName("Google Favicon Vector");
            sharedWorld.addBeyondarObject(go4);

            GeoObject go5 = new GeoObject(5l);
            go5.setGeoPosition(lat , lng);
            go5.setImageResource(R.drawable.logousp1);
            go5.setName("USP");
            sharedWorld.addBeyondarObject(go5);

            GeoObject go2 = new GeoObject(2l);
            go2.setGeoPosition(-23.5595922, -46.7313849);
            go2.setImageResource(R.drawable.ccsl);
            go2.setName("IME");

            GeoObject go3 = new GeoObject(3l);
            go3.setGeoPosition(-23.5570464, -46.7328786);
            go3.setImageResource(R.drawable.poliusp);
            go3.setName("POLI");

            GeoObject go1 = new GeoObject(3l);
            go1.setGeoPosition(-23.5506027, -46.3775692);
            go1.setImageResource(R.drawable.creature_7);
            go1.setName("Minha Cada");

            sharedWorld.addBeyondarObject(go1);
            sharedWorld.addBeyondarObject(go2);
            sharedWorld.addBeyondarObject(go3);

            //BeyondarLocationManager.addGeoObjectLocationUpdate(go4);
            //BeyondarLocationManager.addGeoObjectLocationUpdate(go5);

            int images [] = {
                 R.drawable.google_favicon_vector
                ,R.drawable.google_logo
                ,R.drawable.creature_4
                ,R.drawable.creature_6
                ,R.drawable.poliusp
                ,R.drawable.logousp1
                ,R.drawable.ccsl
            };
            double accLat = -offSetLat, accLng = -offSetLng, newLat = lat, newLng = lng;
            for(int i=0; i<7; i++) {
                GeoObject go = new GeoObject(i + 6L);
                go.setImageResource(images[ i % images.length ]);
                newLat = randomDouble(lat, lat + (-.00010d)); /*-.000010d;*/ //
                newLng = randomDouble(lng, lng + (-.00015d)); /*-.000015d;*/ //
                go.setGeoPosition(newLat, newLng);
                String name = String.format("Image %d %f %f", go.getId(), newLat, newLng);
                Log.i("NAME_OBJ", name);
                go.setName(name);
                sharedWorld.addBeyondarObject(go);
            }
        }
        else {
            sharedWorld.setGeoPosition(41.90533734214473, 2.565848038959814);
        /*
            // Create an object with an image in the app resources.
            // go1.setImageUri("http://beyondar.com/sites/default/files/logo_reduced.png");
            // go1.setImageUri("/sdcard/someImageInYourSDcard.jpeg")
        */
            // Is it also possible to load the image asynchronously form internet
            GeoObject go2 = new GeoObject(2l);
            go2.setGeoPosition(41.90518966360719d, 2.56582424468222d);
            go2.setImageUri("http://beyondar.github.io/beyondar/images/logo_512.png");
            go2.setName("Online image");

            // Also possible to get images from the SDcard
            GeoObject go3 = new GeoObject(3l);
            go3.setGeoPosition(41.90550959641445d, 2.565873388087619d);
            //go3.setImageUri("/sdcard/someImageInYourSDcard.jpeg");
            go3.setImageResource(R.drawable.logousp1);
            go3.setName("IronMan from sdcard");

            // And the same goes for the app assets
            GeoObject go4 = new GeoObject(4l);
            go4.setGeoPosition(41.90553066234138d, 2.565777906882577d);
            go4.setImageResource(R.drawable.creature_4);
            go4.setName("Image from assets");
        /*
            GeoObject go5 = new GeoObject(5l);
            go5.setGeoPosition(41.90553066234138d, 2.565777906882577d);
            go5.setImageResource(R.drawable.ccsl);
            go5.setName("Creature 5");

            GeoObject go6 = new GeoObject(6l);
            go6.setGeoPosition(41.90596218466268d, 2.565250806050688d);
            go6.setImageResource(R.drawable.poliusp);
            go6.setName("Creature 6");

            GeoObject go7 = new GeoObject(7l);
            go7.setGeoPosition(41.90581776104766d, 2.565932313852319d);
            go7.setImageResource(R.drawable.creature_2);
            go7.setName("Creature 2");

            GeoObject go8 = new GeoObject(8l);
            go8.setGeoPosition(41.90534261025682d, 2.566164369775198d);
            go8.setImageResource(R.drawable.rectangle);
            go8.setName("Object 8");

            GeoObject go9 = new GeoObject(9l);
            go9.setGeoPosition(41.90530734214473d, 2.565808038959814d);
            go9.setImageResource(R.drawable.creature_4);
            go9.setName("Creature 4");

            GeoObject go10 = new GeoObject(10l);
            go10.setGeoPosition(42.006667d, 2.705d);
            go10.setImageResource(R.drawable.object_stuff);
            go10.setName("Far away");
        */
            sharedWorld.addBeyondarObject(go2);
            sharedWorld.addBeyondarObject(go3);
            sharedWorld.addBeyondarObject(go4);
        /*
            sharedWorld.addBeyondarObject(go5);
            sharedWorld.addBeyondarObject(go6);
            sharedWorld.addBeyondarObject(go7);
            sharedWorld.addBeyondarObject(go8);
            sharedWorld.addBeyondarObject(go9);
            sharedWorld.addBeyondarObject(go10);
        */
        }
        return sharedWorld;
    }


    public static int randomInt(int max, int min) {
        int n = random.nextInt((max - min)) + min;
        return n;
    }

    public static double randomDouble(double max, double min) {
        double rdn = random.nextDouble( );
        double ans = ((max - min)) * rdn + min;
        return ans;
    }
}
