package helpers;

/**
 * Created by adarsh on 7/12/16.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class ToastHelper {

    public static boolean delayedToasting = false;
    public static Context lastContext = null;
    public static String lastMessage = null;
    public static String toastType = null;

    public static void blueToast(Context context, String message) {
        if (delayedToasting) {
            lastContext = context;
            lastMessage = message;
            toastType = "blue";
        } else {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.getView().setBackgroundColor(Color.BLUE);
            toast.show();
        }
    }

    public static void redToast(Context context, String message) {
        if (delayedToasting) {
            lastContext = context;
            lastMessage = message;
            toastType = "red";
        } else {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.getView().setBackgroundColor(Color.parseColor("#80FF4D4D"));
            toast.show();
        }
    }

    public static void yellowToast(Context context, String message) {
        if (delayedToasting) {
            lastContext = context;
            lastMessage = message;
            toastType = "yellow";
        } else {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(Color.BLACK);
            toast.getView().setBackgroundColor(Color.parseColor("#80FFA51B"));
            toast.show();
        }
    }

    public static void processPendingToast() {
        delayedToasting = false;
        if (lastContext != null && lastMessage != null && toastType != null) {
            if (toastType.equals("blue")) {
                blueToast(lastContext, lastMessage);
            }
            if (toastType.equals("red")) {
                redToast(lastContext, lastMessage);
            }
            if (toastType.equals("yellow")) {
                yellowToast(lastContext, lastMessage);
            }
        }
        lastContext = null;
        lastMessage = null;
        toastType = null;
    }

    public static void delayToasting() {
        delayedToasting = true;
        lastContext = null;
        lastMessage = null;
        toastType = null;
    }
}
