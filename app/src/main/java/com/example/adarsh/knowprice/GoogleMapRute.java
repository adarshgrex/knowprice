package com.example.adarsh.knowprice;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import dataobjects.LocationDetails;
import dataobjects.TravelLocation;
import helpers.GPSTracker;
import helpers.InvtAppAsyncTask;
import helpers.KnowPriceTextWatcher;
import helpers.ToastHelper;
import syncher.LocationSyncher;
import utils.GMapV2Direction;

/**
 * Created by adarsh on 7/24/16.
 */
public class GoogleMapRute extends AppCompatActivity implements OnFocusChangeListener, OnClickListener {
    GoogleMap googleMap;
    Document doc;
    GMapV2Direction googleMapV2Direction;
    String fullAddress, city, latitude, longitude, state, country, postalCode,
            totalAddress, completeAddress;
    ProgressDialog progressDialog;
    public AutoCompleteTextView fromLocation, toLocation;
    HashMap<Integer, Marker> visibleMarkers = new HashMap<Integer, Marker>();
    LatLng sourcePosition, destPosition;
    Polyline polylin;
    List<String> locations = new ArrayList<String>();
    GPSTracker gpsTracker;
    boolean isManualSelection;
    LinearLayout swapLayout;
    ImageView swapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_a_location_layout);
        fromLocation = (AutoCompleteTextView) findViewById(R.id.pickFromLocation);
        toLocation = (AutoCompleteTextView) findViewById(R.id.pickToLocation);
        swapLayout = (LinearLayout) findViewById(R.id.swapLayout);
        swapImage = (ImageView) findViewById(R.id.swapIcon);
        swapLayout.setOnClickListener(this);
        swapImage.setOnClickListener(this);
        googleMapV2Direction = new GMapV2Direction();
        googleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (fromLocation.hasFocus() || toLocation.hasFocus()) {
                    isManualSelection = true;
                    List<Address> addresses;
                    Geocoder geocoder1 = new Geocoder(getBaseContext(), Locale
                            .getDefault());
                    try {
                        addresses = geocoder1.getFromLocation(point.latitude, point.longitude, 1);
                        fullAddress = addresses.get(0).getAddressLine(0); // I
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        postalCode = addresses.get(0).getPostalCode();
                        longitude = point.longitude + "";
                        latitude = point.latitude + "";
                        totalAddress = getValidString(city)
                                + getValidString(state)
                                + getValidString(country)
                                + getValidString(postalCode);
                        city = getValidStringWithBoolenFlag(city, false);
                        state = getValidStringWithBoolenFlag(state, false);
                        country = getValidStringWithBoolenFlag(country, false);
                        postalCode = getValidStringWithBoolenFlag(postalCode,
                                false);
                        completeAddress = getValidString(fullAddress)
                                + getValidString(city) + getValidString(state)
                                + getValidString(country)
                                + getValidString(postalCode)
                                + getValidString(point.latitude + "")
                                + getValidString(point.longitude + "");
                        totalAddress = totalAddress.endsWith(",") ? totalAddress
                                .substring(0, totalAddress.length() - 1)
                                : totalAddress;
                        Toast.makeText(getBaseContext(),
                                "Address " + completeAddress,
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (fromLocation.hasFocus()) {
                        removeMarker(1);
                        fromLocation.setText(totalAddress);
                        sourcePosition = point;
                        drawMarker(point, false);
                    } else {
                        removeMarker(2);
                        destPosition = point;
                        toLocation.setText(totalAddress);
                        drawMarker(point, true);
                        drawPath();
                    }
                    isManualSelection = false;
                }
            }
        });
        addTextChangedListener();
        findViewById(R.id.back_button_layout).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
    }

    public void removeMarker(int key) {
        if (visibleMarkers.size() > 0) {
            Marker marker = visibleMarkers.get(key);
            try {
                if (marker != null)
                    marker.remove();
            } catch (Exception e) {
                ToastHelper.redToast(getApplicationContext(),
                        "Failed to remove marker");
            }
        }
    }

    private void addTextChangedListener() {
        fromLocation.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                if (!isManualSelection)
                    new InvtAppAsyncTask(GoogleMapRute.this) {

                        LocationDetails locationDetailsByName = null;

                        @Override
                        public void process() {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            TravelLocation location = locationSyncher.getLocationDetailsByName(fromLocation.getText().toString());
                            if (location != null) {
                                locationDetailsByName = new LocationDetails();
                                locationDetailsByName.setAddress(location.getAddress());
                                locationDetailsByName.setLattitude(location.getLatitude());
                                locationDetailsByName.setLongitude(location.getLongitude());
                            }
                        }

                        @Override
                        public void afterPostExecute() {
                            if (locationDetailsByName != null) {
                                removeMarker(1);
                                LatLng point = new LatLng(locationDetailsByName.getLattitude(), locationDetailsByName.getLongitude());
                                sourcePosition = point;
                                drawMarker(point, false);
                            } else {
                                ToastHelper.redToast(getApplicationContext(),
                                        "Location details not found");
                            }
                        }
                    }.execute();
            }
        });
        toLocation.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                if (!isManualSelection)
                    new InvtAppAsyncTask(GoogleMapRute.this) {

                        LocationDetails locationDetailsByName = null;

                        @Override
                        public void process() {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            TravelLocation location = locationSyncher.getLocationDetailsByName(toLocation.getText().toString());
                            if (location != null) {
                                locationDetailsByName = new LocationDetails();
                                locationDetailsByName.setAddress(location.getAddress());
                                locationDetailsByName.setLattitude(location.getLatitude());
                                locationDetailsByName.setLongitude(location.getLongitude());
                            }
                        }

                        @Override
                        public void afterPostExecute() {
                            if (locationDetailsByName != null) {
                                removeMarker(2);
                                LatLng point = new LatLng(locationDetailsByName.getLattitude(), locationDetailsByName.getLongitude());
                                destPosition = point;
                                drawMarker(point, true);
                                drawPath();
                            } else {
                                ToastHelper.redToast(getApplicationContext(),"Location details not found");
                            }
                        }
                    }.execute();
            }
        });
        fromLocation.addTextChangedListener(new KnowPriceTextWatcher() {


            @Override
            public void afterTextChanged(Editable s) {
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected void onPostExecute(String result) {
                        String[] countries = locations
                                .toArray(new String[locations.size()]);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.package_spinner_item, countries);
                        fromLocation.setAdapter(arrayAdapter);
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        locations.clear();
                        String keyInfo = getFromText(fromLocation);
                        if (keyInfo.length() >= 3) {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            locations = locationSyncher.getLocations(keyInfo);
                        }
                        return null;
                    }

                    ;
                }.execute();
            }
        });
        toLocation.addTextChangedListener(new KnowPriceTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                new AsyncTask<String, Void, String>() {

                    @Override
                    protected void onPostExecute(String result) {
                        String[] countries = locations
                                .toArray(new String[locations.size()]);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.package_spinner_item, countries);
                        toLocation.setAdapter(arrayAdapter);
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        locations.clear();
                        String key = getFromText(toLocation);
                        if (key.length() >= 3) {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            locations = locationSyncher.getLocations(key);
                        }
                        return null;
                    }

                    ;
                }.execute();
            }
        });
    }

    public String getFromText(TextView textUi){
        return  textUi.getText().toString();
    }

    private void drawMarker(LatLng point, boolean isDestination) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        // Adding marker on the Google Map
        int key = 1;
        BitmapDescriptor fromResource = BitmapDescriptorFactory
                .fromResource(R.drawable.pick_up_location_from_icon);
        if (isDestination) {
            key = 2;
            fromResource = BitmapDescriptorFactory
                    .fromResource(R.drawable.pick_location_to_icon);
        }
        Marker addMarker = googleMap
                .addMarker(markerOptions.icon(fromResource));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 10));
        visibleMarkers.put(key, addMarker);
    }

    public static String getValidString(String data) {
        return getValidStringWithBoolenFlag(data, true);
    }

    public static String getValidStringWithBoolenFlag(String data, boolean value) {
        String comma = value ? "," : "";
        return (data != null && data.length() > 0) ? data + comma : "";
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        removeMarker();
    }

    public void removeMarker() {
        String text = "";
        if (fromLocation.hasFocus()) {
            text = fromLocation.getText().toString();
            if (text.isEmpty()) {
                // Need to remove from location marker
            }
            fromLocation.requestLayout();
        } else {
            text = toLocation.getText().toString();
            if (text.isEmpty()) {
                // Need to remove to location marker
            }
            toLocation.requestLayout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pickFromLocation:
                fromLocation.setFocusable(true);
                break;
            case R.id.pickToLocation:
                toLocation.setFocusable(true);
                break;
            case R.id.title:
                break;
            case R.id.done:
//                if (sourcePosition != null && destPosition != null) {
//                    FromAndToLocation courierLocation = new FromAndToLocation();
//                    courierLocation.setDestinationAddress(toLocation.getText()
//                            .toString());
//                    courierLocation.setSourceAddress(fromLocation.getText()
//                            .toString());
//                    courierLocation.setSourcePosition(new CeroneLatLong(
//                            sourcePosition.latitude, sourcePosition.longitude));
//                    courierLocation.setDestPosition(new CeroneLatLong(
//                            destPosition.latitude, destPosition.longitude));
//                    // courierLocation.setPolylin(polylin);
//                    OneDelPreferences.setFromAndToLocation(courierLocation);
//                    setResult(RESULT_OK);
//                    finish();
//                } else {
//                    ToastHelper.redToast(getApplicationContext(),
//                            "Please select from and to locations.");
//                }
                break;
            case R.id.back_button_layout:
            case R.id.back_button:
                finish();
                break;
            case R.id.swapIcon:
            case R.id.swapLayout:
                if (sourcePosition != null && destPosition != null) {
                    removeMarker(1);
                    removeMarker(2);
                    isManualSelection = true;
                    String textRef = toLocation.getText().toString();
                    toLocation.setText(fromLocation.getText().toString());
                    fromLocation.setText(textRef);
                    LatLng referencePosition = new LatLng(sourcePosition.latitude,
                            sourcePosition.longitude);
                    sourcePosition = destPosition;
                    destPosition = referencePosition;
                    drawMarker(sourcePosition, false);
                    drawMarker(destPosition, true);
                    drawPath();
                    isManualSelection = false;
                } else {
                    ToastHelper.redToast(getApplicationContext(),
                            "Please select from and to locations.");
                }
                break;
            default:
                break;
        }
    }

    private void drawPath() {
        if (sourcePosition != null && destPosition != null) {
            new AsyncTask<String, Void, String>() {

                @Override
                protected void onPreExecute() {
                    progressDialog = ProgressDialog.show(GoogleMapRute.this,
                            "", "Loading....");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                }

                ;

                @Override
                protected String doInBackground(String... params) {
                    doc = googleMapV2Direction.getDocument(sourcePosition,
                            destPosition, GMapV2Direction.MODE_DRIVING);
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (polylin != null) {
                        polylin.remove();
                    }
                    ArrayList<LatLng> directionPoint = googleMapV2Direction
                            .getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(3)
                            .color(Color.RED);
                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    polylin = googleMap.addPolyline(rectLine);
                    polylin.setColor(Color.parseColor("#2a94eb"));
                    polylin.setWidth(10f);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(sourcePosition);
                    builder.include(destPosition);
                    LatLngBounds bounds = builder.build();
                    int padding = 70;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.moveCamera(cu);
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sourcePosition,5));
                    progressDialog.dismiss();
                }

                ;
            }.execute();
        } else {
            ToastHelper.redToast(getApplicationContext(),
                    "Please select from and to locations.");
        }
    }
}
