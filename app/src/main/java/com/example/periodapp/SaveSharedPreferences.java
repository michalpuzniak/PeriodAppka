package com.example.periodapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {
    static final String SH_PREF_USERNAME = "username";
    static final String SH_PREF_NICK = "nick";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SH_PREF_USERNAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(SH_PREF_USERNAME, "");
    }
    public static String getNick(Context ctx){
        return getSharedPreferences(ctx).getString(SH_PREF_NICK, "");
    }
    public static void clearPrefs(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
    public static void setNick(Context ctx, String nick){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SH_PREF_NICK, nick);
        editor.commit();
    }
}

