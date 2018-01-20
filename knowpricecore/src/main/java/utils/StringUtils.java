package utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StringUtils {

    public static final SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    public static final SimpleDateFormat eventTimeFormat = new SimpleDateFormat("HH:mm a");
    public static final SimpleDateFormat newEventDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat newEventTimeFormat = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat eventInfoFormat = new SimpleDateFormat("E, yyyy MMM dd - HH:mm a");

    public static Date StringToDate(String date) {
        Date dateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = simpleDateFormat.parse(date);
        } catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
        return dateTime;
    }
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (simpleDateFormat.format(new Date()));
    }

    public static boolean inOrder(String startDateString, String endDateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null, endtDate = null;
        try {
            startDate = simpleDateFormat.parse(startDateString);
            endtDate = simpleDateFormat.parse(endDateString);
            return startDate.equals(endtDate) || startDate.before(endtDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long DiffBetweenTwoDates() {
        Calendar startDate = Calendar.getInstance(), endDate = Calendar.getInstance();
        startDate.setTime(StringToDate("2015-12-12 11:00:12"));
        endDate.setTime(StringToDate("2015-12-12 13:00:12"));
        long diff = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / (60 * 1000);
        System.out.println(diff);
        return diff;
    }

    public static boolean isGivenDateGreaterThanOrEqualToCurrentDate(String dateTimeInfo) {
        boolean status = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(dateTimeInfo);
            Date date2 = sdf.parse(getCurrentDate());
            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                status = true;
            }
            if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
            }
            if (date1.equals(date2)) {
                status = true;
                System.out.println("Date1 is equal Date2");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static String getValidString(String data) {
        return (data != null && data.length() > 0) ? data + "\n" : "";
    }

    public static String getNewDate(String oldDate, int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = oldDate;
        Date stringToDate = StringToDate(oldDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(stringToDate);
        cal.add(Calendar.MINUTE, minutes);
        newDate = sdf.format(cal.getTime());
        return newDate;
    }

    public static String getFormatedDateFromServerFormatedDate(String serverFormattedDate) {
        if (serverFormattedDate != null && serverFormattedDate.contains("+")) {
            serverFormattedDate = serverFormattedDate.substring(0, serverFormattedDate.indexOf('+'));
        }
        return serverFormattedDate.replace('T', ' ').replace('Z', ' ').trim();
    }

    public static String getContactsString(List<String> contacts) {
        String result = "";
        for (int i = 0; i < contacts.size() - 1; i++) {
            result += contacts.get(i) + ", ";
        }
        result = result.endsWith(",") ? (result.substring(0, result.length() - 1)) : result;
        return result;
    }

    public static String getTrimmeDistance(String distance) {
        String result = "Not found";
        DecimalFormat df = new DecimalFormat("#.0");
        df.setMinimumIntegerDigits(1);
        if (distance != null && !distance.equals("Not found")) {
            double doubleInfo = Double.parseDouble(distance);
            result = df.format(doubleInfo) + " km";
        }
        return result;
    }

    public static String formatDateAndTime(String string, int index) {
        String info = "Not found";
        Date dateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = simpleDateFormat.parse(string);
            switch (index) {
                case 1 :
                    info = eventDateFormat.format(dateTime);
                    break;
                case 2 :
                    info = eventTimeFormat.format(dateTime);
                    break;
                case 3 :
                    info = newEventDateFormat.format(dateTime);
                    break;
                case 4 :
                    info = newEventTimeFormat.format(dateTime);
                    break;
                case 5 :
                    info = eventInfoFormat.format(dateTime);
                    break;
                default :
                    break;
            }
        } catch (ParseException ex) {
            System.out.println("Exception " + ex);
        }
        System.out.println("Formated data " + info);
        return info;
    }
    public static double getRoundDoubleValue(double actualData){
    	DecimalFormat df=new DecimalFormat("0.00");
    	String formate = df.format(actualData); 
    	double finalValue = actualData;
		try {
			finalValue = (Double)df.parse(formate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return  finalValue;
    }
}