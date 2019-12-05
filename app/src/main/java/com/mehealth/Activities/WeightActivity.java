package com.mehealth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mehealth.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class WeightActivity extends AppCompatActivity {
    private static final String TAG = "WeightActivity";
    User user;
    Toolbar toolbar;
    SharedPref pref;
    Boolean settingsOpened;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        pref = new SharedPref(getApplicationContext());
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MeHealth");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            settingsOpened = true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        MainActivity.menuIconHighlight(bottomNavigationView, 1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(WeightActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_weight_scale:
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(WeightActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(WeightActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(WeightActivity.this, MoodActivity.class);
                        startActivity(mood.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.buttonLisaaArvo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddValues(v);
                updateUI(user);
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });

        findViewById(R.id.buttonDeleteLastWeightRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.weight.deleteRecord(user.weight.getWeightHistoryList().size() - 1);
                updateUI(user);
            }
        });

        setupEditTexts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = pref.getUser();
        updateUI(user);
        settingsOpened = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        if (!settingsOpened) {
            finish();
        }
    }

    /**
     * Updates the TextViews with the latest values from weight and blood pressures.
     * @param user
     */
    private void updateText(User user) {
        TextView weightText = findViewById(R.id.textViewWeight);
        TextView lowerBPText = findViewById(R.id.textViewLowerBP);
        TextView upperBPText = findViewById(R.id.textViewUpperBP);

        weightText.setText(String.format(Locale.getDefault(), "Paino\n%d", user.weight.getLatestWeight()));
        lowerBPText.setText(String.format(Locale.getDefault(), "AlaP\n%d", user.bloodPressure.getLatestLowerBP()));
        upperBPText.setText(String.format(Locale.getDefault(), "YläP\n%d", user.bloodPressure.getLatestUpperBP()));

        ((EditText)findViewById(R.id.editTextPaino)).setText("");
        ((EditText)findViewById(R.id.editTextAlaPaine)).setText("");
        ((EditText)findViewById(R.id.editTextYlaPaine)).setText("");
    }

    /**
     * Gets the values from user input and saves them into the user's history.
     * @param view
     */
    public void buttonAddValues(View view) {
        EditText editTextWeight = findViewById(R.id.editTextPaino);
        EditText editTextLowerBP = findViewById(R.id.editTextAlaPaine);
        EditText editTextUpperBP = findViewById(R.id.editTextYlaPaine);

        String weight = editTextWeight.getText().toString();
        String lowerBP = editTextLowerBP.getText().toString();
        String upperBP = editTextUpperBP.getText().toString();
        if (!weight.isEmpty()) user.weight.addWeightRecord(Integer.parseInt(weight));
        if (!lowerBP.isEmpty()) user.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP));
        if (!upperBP.isEmpty()) user.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP));
    }

    /**
     * Updates the weight graph with the newest values
     * @param user
     */
    protected void updateGraph(User user) {
        GraphView graph = findViewById(R.id.weightGraph);
        ArrayList weightHistory = user.weight.getWeightHistoryList();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < weightHistory.size(); i++) {
            DataPoint dataPoint = new DataPoint(i, (int) weightHistory.get(i));
            series.appendData(dataPoint, true, weightHistory.size());
        }

        graph.removeAllSeries();
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        graph.addSeries(series);
        graph.setTitle("Paino");
        graph.getViewport().setScalable(true);
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setXAxisBoundsManual(true);
        if (weightHistory.size() < 2) {
            graph.getViewport().setMaxX(1);
        } else {
            graph.getViewport().setMaxX(weightHistory.size() - 1);
        }
    }



    /**
     * Updates the text and graph with the latest values.
     * @param user
     */
    protected void updateUI(User user) {
        updateText(user);
        updateGraph(user);
    }

    protected void setupEditTexts() {
        EditText paino = findViewById(R.id.editTextPaino);
        EditText alaPaine = findViewById(R.id.editTextAlaPaine);
        EditText ylaPaine = findViewById(R.id.editTextYlaPaine);

        paino.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});
        alaPaine.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});
        ylaPaine.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});

        paino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
        alaPaine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
        ylaPaine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
    }



}
