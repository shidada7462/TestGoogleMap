package iac.com.testgooglemap;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private Boolean mLocationPermissionGranted = false;
    private LatLng mLocation;

    private double mVParam = 0.0000258;
    private double mV1Param = -0.0000326;

    private double mV = 24.9602229;
    private double mV1 = 121.1666207;

    private Handler mMoveLoactionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(mMoveLoactionHandler == null) {
            mMoveLoactionHandler = new Handler();
        }
        if (!checkPermission()) {
            Toast.makeText(this, "Require permissions.", Toast.LENGTH_SHORT).show();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.basicGoogleMap_actvity_map);
            mapFragment.getMapAsync(onMapReadyCallback);
        }
    }

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mLocation = new LatLng(mV, mV1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 18f)); // 1.0~21.0

            if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMoveLoactionHandler.postDelayed(moveLactionEmulator, 100);
        }
    };

    public Boolean checkPermission() {
        String[] pm = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
        List<String> list = new ArrayList<>();

        for (int i = 0; i < pm.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, pm[i]) != PackageManager.PERMISSION_GRANTED) {
                list.add(pm[i]);
            }
        }
        if (list.size() > 0) {
            ActivityCompat.requestPermissions(this, list.toArray(new String[list.size()]), 1);
            return false;
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    int i;
                    for (i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            break;
                        }
                    }
                }
                break;
        }
    }

    private Runnable moveLactionEmulator = new Runnable() {
        public void run() {

            mLocation = new LatLng(mV, mV1);
            MarkerOptions carMarker = new MarkerOptions();
            carMarker.position(mLocation);
            carMarker.title("car");
            carMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.twotone_directions_car_black_24dp));

            mMap.clear();
            mMap.addMarker(carMarker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 17.5f)); // 1.0~21.0
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 18f));

            mV = mV + mVParam;
            mV1 = mV1 + mV1Param;
            mMoveLoactionHandler.postDelayed(moveLactionEmulator, 100);
        }
    };
}
