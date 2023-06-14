package com.java.chaseweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.java.chaseweather.ChaseWeatherApplication;

public class SharedPref {
    private static final String PREF_NAME = "ChaseWeatherPreference";
    private static final String KEY_CITY_NAME = "cityName";

    public static void saveCityName(String cityName) {
        SharedPreferences sharedPreferences = ChaseWeatherApplication.application.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CITY_NAME, cityName);
        editor.apply();
    }

    public static String getCityName() {
        SharedPreferences sharedPreferences = ChaseWeatherApplication.application.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CITY_NAME, "");
    }
}
