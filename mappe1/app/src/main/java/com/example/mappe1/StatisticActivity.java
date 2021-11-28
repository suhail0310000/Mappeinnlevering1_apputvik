package com.example.mappe1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class StatisticActivity extends AppCompatActivity {
    // Widgets
    ProgressBar percentBar;
    TextView percentTitle, percentDesc;

    // Variables
    int totalCorrect, totalWrong;
    private final String SHARED_PREFS = "SHARED_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbarStatistic);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        percentTitle = (TextView) findViewById(R.id.percentText);
        percentDesc = (TextView) findViewById(R.id.percentDesc);

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        totalCorrect = sharedPrefs.getInt(getString(R.string.statistic_correct_key), 0);
        totalWrong = sharedPrefs.getInt(getString(R.string.statistic_wrong_key), 0);

        double percent = (double)totalCorrect / ((double)totalCorrect + (double)totalWrong) * 100;
        String percentText;
        if (Double.isNaN(percent)) {
            percentText = "0%";
        } else {
            percentText = String.format("%.2f%%", percent);
        }

        String descText = getResources().getString(R.string.percentDesc, totalCorrect, totalWrong, totalCorrect+totalWrong);
        percentTitle.setText(percentText);
        percentDesc.setText(descText);

        percentBar = (ProgressBar) findViewById(R.id.percentBar);
        percentBar.setProgress((int) percent);
    }

    public void deleteStatistic (android.view.View view) {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.statistic_correct_key), 0);
        editor.putInt(getString(R.string.statistic_wrong_key), 0);
        editor.apply();

        percentTitle.setText("0%");
        String descText = getResources().getString(R.string.percentDesc, 0, 0, 0);
        percentDesc.setText(descText);
        percentBar.setProgress(0);
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
}
