package com.example.adarsh.knowprice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dataobjects.LocationDetails;
import dataobjects.LocationState;
import dataobjects.Travel;
import dataobjects.TravelLocation;
import helpers.InvtAppAsyncTask;
import helpers.InvtAppPreferences;
import helpers.KnowPriceTextWatcher;
import helpers.ToastHelper;
import syncher.LocationSyncher;
import syncher.StatesSyncher;
import utils.Calculator;
import utils.GMapV2Direction;
import utils.HTTPUtils;
import utils.StringUtils;

import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LocationSyncher locationSyncher = new LocationSyncher();
    AutoCompleteTextView fromLocation;
    AutoCompleteTextView toLocation;
    TravelLocation fromLocationInfo;
    TravelLocation toLocationInfo;
    TextView detailsView;
    EditText milage;
    Button calculate;
    Calculator calculator = new Calculator();
    List<String> locations = new ArrayList<String>();
    //Google maps
    GoogleMap googleMap;
    Document doc;
    GMapV2Direction googleMapV2Direction;
    LatLng sourcePosition, destPosition;
    Polyline polylin;
    HashMap<Integer, Marker> visibleMarkers = new HashMap<Integer, Marker>();
    StatesSyncher statesSyncher = new StatesSyncher();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InvtAppPreferences.setPref(this);
        //
        fromLocation = (AutoCompleteTextView) findViewById(R.id.fromLocaton);
        toLocation = (AutoCompleteTextView) findViewById(R.id.toLocation);
        milage = (EditText) findViewById(R.id.milage);
        calculate = (Button) findViewById(R.id.calculate);
        detailsView = (TextView) findViewById(R.id.detailsView);
        calculate.setOnClickListener(this);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setOnTextChangeListener();
        //Google map
        googleMapV2Direction = new GMapV2Direction();
        googleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Intent locationIntent = new Intent(getApplicationContext(), GoogleMapRute.class);
                startActivity(locationIntent);
            }
        });
        Log.d("StatesInfo","StatesInfo 0");
       // loadStates();
    }


    private void loadStates() {
        Log.d("StatesInfo", "Berore 1");

        new InvtAppAsyncTask(MainActivity.this) {
            List<LocationState> statesList = new ArrayList<LocationState>();

            @Override
            public void process() {
                Log.d("StatesInfo","Berore");
                statesList = statesSyncher.getStatesList();
            }

            @Override
            public void afterPostExecute() {
                InvtAppPreferences.setStatesIno(statesList);
                ToastHelper.blueToast(getApplicationContext(), "Count " + statesList.size());
                detailsView.setText("Distance :" + statesList.size());

            }

        }.execute();
    }

    private void setOnTextChangeListener() {
        fromLocation.addTextChangedListener(new KnowPriceTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        locations.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String key = fromLocation.getText().toString();
                                if (key.length() >= 3) {
                                    locations = locationSyncher.getLocations(key);
                                }
                            }
                        });


                        return null;
                    }


                    @Override
                    protected void onPostExecute(String result) {
                        String[] countries = locations.toArray(new String[locations.size()]);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, countries);
                        fromLocation.setAdapter(arrayAdapter);
                    }
                }.execute();
            }
        });
        fromLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                new InvtAppAsyncTask(MainActivity.this) {

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
        ///
        toLocation.addTextChangedListener(new KnowPriceTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        locations.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String key = toLocation.getText().toString();
                                if (key.length() >= 3) {
                                    locations = locationSyncher.getLocations(key);
                                }
                            }
                        });


                        return null;
                    }

                    ;

                    @Override
                    protected void onPostExecute(String result) {
                        String[] countries = locations.toArray(new String[locations.size()]);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, countries);
                        toLocation.setAdapter(arrayAdapter);
                    }
                }.execute();
            }
        });
        toLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                new InvtAppAsyncTask(MainActivity.this) {
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
                            ToastHelper.redToast(getApplicationContext(), "Location details not found");
                        }
                    }
                }.execute();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculate:
                testAdarsh();
//                if (!fromLocation.getText().toString().isEmpty() && !toLocation.getText().toString().isEmpty()) {
//                    if (!milage.getText().toString().isEmpty()) {
//                        new InvtAppAsyncTask(MainActivity.this) {
//                            Travel travel = null;
//
//                            @Override
//                            public void process() {
//                                travel = locationSyncher.getTravelInformation(fromLocation.getText().toString(), toLocation.getText().toString());
//                            }
//
//                            @Override
//                            public void afterPostExecute() {
//                                if (travel != null) {
//                                    String stringDistance = travel.getDistance().replaceAll(" km", "").trim();
//                                    if (!stringDistance.contains(".")) {
//                                        stringDistance = stringDistance + ".00";
//                                    }
//                                    Log.d("Distance", "" + travel.getDistance() + "String distance" + stringDistance);
//                                    double distance = Double.valueOf(stringDistance);
//                                    double millageInfo = Double.parseDouble(milage.getText().toString());
//                                    double price = calculator.getPrice(distance, millageInfo, 60.00);
//                                    detailsView.setText("Distance : " + distance + " Km \n price : Rs " + price);
//                                } else {
//                                    ToastHelper.redToast(getApplicationContext(), "Failed to calculate price try again.");
//                                }
//                            }
//                        }.execute();
////
//                    } else {
//                        ToastHelper.redToast(getApplicationContext(), "Please choose milage.");
//                    }
//                } else {
//                    ToastHelper.redToast(getApplicationContext(), "Please enter valid from and to locations.");
//                }
                break;
        }

    }
    public  void testAdarsh(){
        Log.d("Test","Sai Ram");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Goole map
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

    private void drawPath() {
        if (sourcePosition != null && destPosition != null) {
            new AsyncTask<String, Void, String>() {

                @Override
                protected void onPreExecute() {
                    progressDialog = ProgressDialog.show(MainActivity.this,
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
