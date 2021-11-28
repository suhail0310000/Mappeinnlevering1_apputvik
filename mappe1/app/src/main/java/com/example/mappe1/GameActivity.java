package com.example.mappe1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;


public class GameActivity extends AppCompatActivity {
    // Widgets
    private TextView ansText;
    private TextView probText;
    private TextView counterText;
    private TextView counterRightWrongText;

    // Variables
    private String ans = "";
    private final String SHARED_PREFS = "SHARED_PREFS";
    private int preferences;
    private int count = 0;
    private int[][] mathproblems;
    private int correct = 0;
    private int wrong = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        loadPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbarGame);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        counterRightWrongText = (TextView) findViewById(R.id.rightWrong);
        ansText = (TextView) findViewById(R.id.ansText);
        probText = (TextView) findViewById(R.id.probText);
        counterText = (TextView) findViewById(R.id.counter);

        // Getting math problems from resources.
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        preferences = sharedPref.getInt(getString(R.string.preferences_amount_key), 5);

        String[] mathRes = getResources().getStringArray(R.array.mathproblems);
        mathproblems = new int[preferences][3];

        Integer[] indexes = new Integer[preferences];

        for (int i = 0; i < preferences; i++) {
            Random rn = new Random();
            int index = rn.nextInt(mathRes.length);

            // Checks if any questions are reaccuring, if so finds a new question.
            while (Arrays.asList(indexes).contains(index)) {
                Log.v("INDEX", index+" ");
                index = rn.nextInt(mathRes.length);
            }

            indexes[i] = index;
        }

        for (int i = 0; i < mathproblems.length; i++) {
            mathproblems[i][0] = Integer.parseInt(mathRes[indexes[i]].split(",")[0]);
            mathproblems[i][1] = Integer.parseInt(mathRes[indexes[i]].split(",")[1]);
            mathproblems[i][2] = Integer.parseInt(mathRes[indexes[i]].split(",")[2]);

            Log.v("TAG", mathproblems[i][0] +" "+ mathproblems[i][1] +" "+ mathproblems[i][2]);
        }

        // Displaying the first problem
        updateProblem();
    }

    public void updateProblem() {
        String prb = mathproblems[count][0] +" + "+ mathproblems[count][1] +" = ";
        probText.setText(prb);
        String cnt = (count+1) +"/"+preferences;
        counterText.setText(cnt);
    }

    public void saveScore() {
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int totalCorrect = sharedPref.getInt(getString(R.string.statistic_correct_key), 0) + correct;
        int totalWrong = sharedPref.getInt(getString(R.string.statistic_wrong_key), 0) + wrong;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.statistic_correct_key), totalCorrect);
        editor.putInt(getString(R.string.statistic_wrong_key), totalWrong);
        editor.apply();
    }

    public void gameInput(android.view.View view) {
        Button b = (Button) view;
        String buttonText = b.getText().toString();

        if (buttonText.equals("Angre") || buttonText.equals("Bereuen")) {
            ans = "";
        } else if (buttonText.equals("Svar") || buttonText.equals("Antwort")) {
            // Submit answer
            if (ans.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if (Integer.parseInt(ans) == mathproblems[count][2]) {
                correct++;
                Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT).show();

            } else {
                wrong++;
                String toastWrong = getResources().getString(R.string.wrong, mathproblems[count][2]);
                Toast.makeText(this, toastWrong, Toast.LENGTH_SHORT).show();
            }

            ans = "";
            counterRightWrongText.setText(getResources().getString(R.string.counterString, correct, wrong));

            // Gets next question or exits if user has answered all questions
            if (count == preferences - 1) {
                ResultDialog dialog = new ResultDialog();
                saveScore();
                dialog.show(getSupportFragmentManager(),"Result");
            } else {
                count++;
                updateProblem();
            }

        // Checks if answer is longer than 8 digits, in which case the answer is too long
        } else if (ans.length() > 8) {
            Toast.makeText(this, getString(R.string.tooLong), Toast.LENGTH_SHORT).show();
        } else {
            ans += buttonText;
        }
        ansText.setText(ans);
    }

    // Overrides that dialog fragment shows up instead of the default up navigation
    @Override
    public void onBackPressed() {
        ExitGameDialog dialog = new ExitGameDialog();
        dialog.show(getSupportFragmentManager(),"Avslutt");
    }

    // Overrides clicking on up navigation in the action bar to onBackPressed()
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ans", ans);
        outState.putInt("count", count);
        outState.putInt("correct", correct);
        outState.putInt("wrong", wrong);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ans = savedInstanceState.getString("ans");
        ansText.setText(ans);

        correct = savedInstanceState.getInt("correct");
        wrong = savedInstanceState.getInt("wrong");
        counterRightWrongText.setText(getResources().getString(R.string.counterString, correct, wrong));

        count = savedInstanceState.getInt("count");
        updateProblem();
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