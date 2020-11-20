package com.example.internshipassignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class IntroPreference {

    private SharedPreferences preferences;
    private final SharedPreferences.Editor editor;
    private static final String PREF_NAME = "com.example.assignment";
    private static final String IS_FIRST_LAUNCH = "first_launch";

    @SuppressLint("CommitPrefEdits")
    public IntroPreference(Context context) {
        if(context != null) {
            preferences = context.getSharedPreferences(PREF_NAME, 0);
        }
        editor = preferences.edit();
    }

    public void setIsFirstTimeLaunch(boolean firstLaunch) {
        editor.putBoolean(IS_FIRST_LAUNCH, firstLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return preferences.getBoolean(IS_FIRST_LAUNCH, true);
    }
}