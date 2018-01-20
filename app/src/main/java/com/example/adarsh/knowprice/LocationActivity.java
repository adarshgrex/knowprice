package com.example.adarsh.knowprice;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by adarsh on 7/24/16.
 */
public class LocationActivity extends AppCompatActivity {
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
        }
    }
}
