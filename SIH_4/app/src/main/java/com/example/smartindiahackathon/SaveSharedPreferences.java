package com.example.smartindiahackathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {
    static final String USER_NAME = "username", DEVICE_TOKEN = "myToken", THUBMBNAIL_URL = "myThumbnailUrl", AIRPORT_NAME = "myAirport";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String user_Name)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_NAME, user_Name);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_NAME, "");
    }

    //////

    public static void setDeviceToken(Context ctx, String my_token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(DEVICE_TOKEN, my_token);
        editor.commit();
    }

    public static String getDeviceToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(DEVICE_TOKEN, "");
    }

    //////

    public static void setThubmbnailUrl(Context ctx, String my_thumbnailUrl)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(THUBMBNAIL_URL, my_thumbnailUrl);
        editor.commit();
    }

    public static String getThubmbnailUrl(Context ctx)
    {
        return getSharedPreferences(ctx).getString(THUBMBNAIL_URL, "");
    }

    //////

    public static void setAirportName(Context ctx, String my_Airport)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(AIRPORT_NAME, my_Airport);
        editor.commit();
    }

    public static String getAirportName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(AIRPORT_NAME, "");
    }

}