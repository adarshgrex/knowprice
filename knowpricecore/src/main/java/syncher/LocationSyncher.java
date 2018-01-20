package syncher;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dataobjects.MyLatLogn;
import dataobjects.Travel;
import dataobjects.TravelLocation;
import utils.HTTPUtils;


public class LocationSyncher extends BaseSyncher {

    private static final String LOCATION = "location";
    private static final String GEOMETRY = "geometry";
    private static final String STATUS = "status";
    private static final String OK = "OK";
    private static final String RESULTS = "results";
    private static final String KEY_DESCRIPTION = "description";
    private static final String PREDICTIONS = "predictions";
    private static final String API_KEY = "AIzaSyAr8xCnXF4DmGqHV1JGibUlw57mkNrAPLs";
    String URL_START = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
    String URL_END = "&regions=(administrative_area_level_1)&sensor=true&key=" + API_KEY;
    String LAT_LON_URL_START = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    String LAT_LON_URL_END = "&key=" + API_KEY;
    String URL = "";

    public List<String> getLocations(String searchLocation) {
        List<String> list = new ArrayList<String>();
        try {
            URL = URL_START + URLEncoder.encode(searchLocation, "utf-8") + URL_END;
            String data = HTTPUtils.getDataFromServer(URL, "GET", false);
            System.out.println(data);
            JSONObject json = HTTPUtils.getJSONFromUrl(URL);
            if (json != null) {
                JSONArray contacts;
                try {
                    contacts = json.getJSONArray(PREDICTIONS);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String description = c.getString(KEY_DESCRIPTION);
                        // Log.d(KEY_DESCRIPTION, description);
                        list.add(description);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return list;
    }

    public Travel getTravelInformation(String startLocation, String endLocation) {
        Travel travel = null;
        try {
            String completeUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + URLEncoder.encode(startLocation, "utf-8") + "&destination=" + URLEncoder.encode(endLocation, "utf-8") +"&sensor=false";
            String response = HTTPUtils.getDataFromServer(completeUrl, "GET", false);
            JSONObject json = new JSONObject(response);
            if (json != null) {
                String status = json.getString("status");
                if (status.compareTo("OK") == 0) {
                    JSONArray routes = json.getJSONArray("routes");
                    for(int i = 0;i<routes.length();i++){
                        JSONObject route = routes.getJSONObject(i);
                        JSONArray legsArray = route.getJSONArray("legs");
                        for (int j =0 ;j<legsArray.length();j++){
                            travel = new Travel();
;                            JSONObject leg = legsArray.getJSONObject(j);
                            travel.setStartAddress(leg.getString("start_address"));
                            travel.setEndAddress(leg.getString("end_address"));
                            JSONObject distanceInfo = leg.getJSONObject("distance");
                            JSONObject fromLocationInfo = leg.getJSONObject("start_location");
                            JSONObject toLocationInfo = leg.getJSONObject("end_location");
                            MyLatLogn start = new MyLatLogn();
                            start.setLattitude(fromLocationInfo.getDouble("lat"));
                            start.setLogitude(fromLocationInfo.getDouble("lng"));
                            MyLatLogn end = new MyLatLogn();
                            end.setLattitude(fromLocationInfo.getDouble("lat"));
                            end.setLogitude(fromLocationInfo.getDouble("lng"));
                            travel.setDistance(distanceInfo.getString("text"));
                            travel.setSourceLocation(start);
                            travel.setDestinatonLocation(end);
                            return  travel;
                        }

                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return travel;
    }

    public TravelLocation getLocationDetailsByName(String locationName) {
        TravelLocation details = null;
        try {
            String completeUrl = LAT_LON_URL_START + URLEncoder.encode(locationName, "utf-8") + LAT_LON_URL_END;
            String response = HTTPUtils.getDataFromServer(completeUrl, "GET", false);
            JSONObject json = new JSONObject(response);
            if (json != null) {
                JSONArray contacts;
                try {
                    if (json.getString(STATUS).equals(OK)) {
                        details = new TravelLocation();
                        contacts = json.getJSONArray(RESULTS);
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject myLocation = contacts.getJSONObject(i);
                            JSONArray addressConponents = (JSONArray) myLocation.get("address_components");
                            if (addressConponents.length() >= 2) {
                                JSONObject stateInfo = addressConponents.getJSONObject(addressConponents.length() - 2);
                                details.setState(stateInfo.getString("long_name"));
                            }
                            if (myLocation.has(GEOMETRY)) {
                                JSONObject gemetryInfo = myLocation.getJSONObject(GEOMETRY);
                                JSONObject latLong = gemetryInfo.getJSONObject(LOCATION);
                                details.setLatitude(latLong.getDouble("lat"));
                                details.setLongitude(latLong.getDouble("lng"));
                                details.setAddress(locationName);//myLocation.getString("formatted_address")
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return details;
    }
}