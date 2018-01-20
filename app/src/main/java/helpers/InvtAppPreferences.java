package helpers;

/**
 * Created by adarsh on 7/12/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import dataobjects.LocationState;

public class InvtAppPreferences {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String OWNER_ID = "ownerId";
    private static final String LOGGEDIN = "loggedIn";
    public static SharedPreferences pref;
    private static final String STATES_INFO = "statesInfo";


    public static SharedPreferences getPref() {
        return pref;
    }

    public static void setPref(Context context) {
        if (pref == null) {
            InvtAppPreferences.pref = context.getSharedPreferences("com.example.invtapp_android", Context.MODE_PRIVATE);
        }
    }

    public static void reset() {
        pref.edit().clear().commit();
    }

    public static void setLoggedIn(boolean loggedIn) {
        pref.edit().putBoolean(LOGGEDIN, loggedIn).commit();
    }

    public static boolean isLoggedIn() {
        return pref.getBoolean(LOGGEDIN, false);
    }
    public static void setZoomPosition(String accessToken) {
        pref.edit().putString(ACCESS_TOKEN, accessToken).commit();
    }

    public static String getZoomPosition() {
        return pref.getString(ACCESS_TOKEN, "");
    }
    public static  List<LocationState> getStatesList(){
        java.lang.reflect.Type type = new TypeToken<List<LocationState>>() {
        }.getType();
        List<LocationState> statesList = new Gson().fromJson(pref.getString(STATES_INFO, ""), type);
        if (statesList == null) {
            statesList = new ArrayList<LocationState>();
        }
        return statesList;

    }
    public  static  void setStatesIno(List<LocationState> statesList){
        String contactDetails = new Gson().toJson(statesList);
        pref.edit().putString(STATES_INFO, contactDetails).commit();

    }

}