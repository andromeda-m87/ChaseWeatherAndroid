package com.java.chaseweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DecimalFormat;

public class Utils {

    public String roundTo(Double temp) {
        double rounded = Math.round(temp);
        String formatted = new DecimalFormat("#").format(rounded);
        return formatted;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public double getWindSpeedInMph(double metersPerSecond) {
        //1 m/s = 2.2369 mph
        double mph = (metersPerSecond * 2.2369);
        DecimalFormat df = new DecimalFormat("#.00");
        String formatted = df.format(mph);
        return Double.parseDouble(formatted);
    }
}
