package com.example.mappe1;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class PreferanseFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener{

    private final String SHARED_PREFS = "SHARED_PREFS";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        // Listpreference for changing language
        final android.support.v7.preference.Preference prefListLang = findPreference(getResources().getString(R.string.changeLanguageKey));
        prefListLang.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object value) {
                String key1 = value.toString();
                SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if(key1.equals("1")){
                    editor.putString(getString(R.string.preferences_language_key), "no");
                    editor.apply();
                    settland("no");

                    // Refreshing the activity to update language
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                    return true;

                }else if(key1.equals("2")){
                    editor.putString(getString(R.string.preferences_language_key), "de");
                    editor.apply();
                    settland("de");

                    // Refreshing the activity to update language
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Listpreference for choosing amount of questions
        final android.support.v7.preference.Preference prefListSpm = findPreference(getResources().getString(R.string.chooseAmountKey));
        prefListSpm.setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object value) {
                String key1 = value.toString();
                SharedPreferences sharedPref = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                if(key1.equals("5")){
                    editor.putInt(getString(R.string.preferences_amount_key), 5);
                    editor.apply();
                    return true;

                } else if(key1.equals("10")){
                    editor.putInt(getString(R.string.preferences_amount_key), 10);
                    editor.apply();
                    return true;

                } else if(key1.equals("15")){
;                   editor.putInt(getString(R.string.preferences_amount_key), 15);
                    editor.apply();
                    return true;
                }

                return false;
            }
        });
    }

    public void settland(String landskode){
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration cf=res.getConfiguration();
        cf.setLocale(new Locale(landskode));
        res.updateConfiguration(cf,dm);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}