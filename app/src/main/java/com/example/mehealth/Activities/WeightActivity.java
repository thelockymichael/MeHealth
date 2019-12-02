package com.example.mehealth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paino);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(WeightActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
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
        Button buttonLisaaArvo = findViewById(R.id.buttonLisaaArvo);
        buttonLisaaArvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddValues(v);
                updateUI(user);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = pref.getUser();
        updateUI(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        finish();
    }

    private void paivitaTextit(User user) {
        TextView painoText = findViewById(R.id.textViewPaino);
        TextView alaPaineText = findViewById(R.id.textViewAlaPaine);
        TextView ylaPaineText = findViewById(R.id.textViewYlaPaine);

        painoText.setText(String.format(Locale.getDefault(), "paino\n%d", user.weight.getLatestWeight()));
        alaPaineText.setText(String.format(Locale.getDefault(), "alaP\n%d", user.bloodPressure.getLatestLowerBloodPressure()));
        ylaPaineText.setText(String.format(Locale.getDefault(), "ylaP\n%d", user.bloodPressure.getLatestUpperBloodPressure()));
    }
    public void buttonAddValues(View view) {
        EditText painoEditText = findViewById(R.id.editTextPaino);
        EditText alaPaineEditText = findViewById(R.id.editTextAlaPaine);
        EditText ylaPaineEditText = findViewById(R.id.editTextYlaPaine);

        String painoText = painoEditText.getText().toString();
        String alaPaineText = alaPaineEditText.getText().toString();
        String ylaPaineText = ylaPaineEditText.getText().toString();
        if (!painoText.isEmpty()) user.weight.addWeightRecord(Integer.parseInt(painoText));
        if (!alaPaineText.isEmpty()) user.bloodPressure.addLowerBloodPressureRecord(Integer.parseInt(alaPaineText));
        if (!ylaPaineText.isEmpty()) user.bloodPressure.addUpperBloodPressureRecord(Integer.parseInt(ylaPaineText));
    }

    protected void updateGraph(User user) {
        ArrayList<Integer> weightHistory = user.weight.getWeightHistoryList();
        DataPoint[] data = new DataPoint[weightHistory.size()];

        for (int i = 0; i < weightHistory.size(); i++) {
            data[i] = new DataPoint(i , weightHistory.get(i));
        }

        GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        graph.addSeries(series);
        graph.setTitle("Paino");
        graph.getViewport().setMaxX(50);
        graph.getViewport().setScalable(true);
    }

    protected void updateUI(User user) {
        paivitaTextit(user);
        updateGraph(user);
    }
}
