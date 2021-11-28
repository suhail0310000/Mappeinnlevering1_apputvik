package com.example.mappe1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String SHARED_PREFS = "SHARED_PREFS";
    private boolean languageChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Starts game activity
    public void startGameActivity(android.view.View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    // Starts game activity
    public void startStatisticActivity(android.view.View view) {
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
    }

    //Start preference fragment
    public void startPreferanseActivity(android.view.View view) {
        languageChange = true;
        Intent intent = new Intent(this, SetPreferenceFragment.class);
        startActivity(intent);

    }

    public void loadPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String language = sharedPref.getString(getString(R.string.preferences_language_key), "no");

        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration cf=res.getConfiguration();
        cf.setLocale(new Locale(language));
        res.updateConfiguration(cf,dm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (languageChange) recreate();
    }
}