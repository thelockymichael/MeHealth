package com.mehealth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MoodActivity extends AppCompatActivity {
    private static final String TAG = "MoodActivity";
    private User user;
    private SharedPref pref;
    private Boolean settingsOpened;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        pref = new SharedPref(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Mieliala");

        //Declares the ImageView and sets the first image to be the neutral smiley corresponding to 5
        final ImageView imageMieliala = findViewById(R.id.imageMieliala);
        imageMieliala.setImageResource(R.drawable.smiley5);
        //This TextView contains the current progress of the seekbar
        final TextView textMieliala = findViewById(R.id.textMieliala);
        //The default value of the seekbar is 5 when opening the activity, hence the textview is set to 5 at start
        textMieliala.setText("5");

        //Declares the seekbar and sets the listener for it
        final SeekBar seekBarMood = findViewById(R.id.seekbarMieliala);
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Depending on the progress of the seekbar, the smiley image changes color
                //There are smileys ranging from smiley0 being red to smiley10 being green'
                //smiley5 is the neutral yellow
                textMieliala.setText(String.format(Locale.getDefault(), "%d", progress));
                switch (progress) {
                    case 0:
                        imageMieliala.setImageResource(R.drawable.smiley0);
                        break;
                    case 1:
                        imageMieliala.setImageResource(R.drawable.smiley1);
                        break;
                    case 2:
                        imageMieliala.setImageResource(R.drawable.smiley2);
                        break;
                    case 3:
                        imageMieliala.setImageResource(R.drawable.smiley3);
                        break;
                    case 4:
                        imageMieliala.setImageResource(R.drawable.smiley4);
                        break;
                    case 5:
                        imageMieliala.setImageResource(R.drawable.smiley5);
                        break;
                    case 6:
                        imageMieliala.setImageResource(R.drawable.smiley6);
                        break;
                    case 7:
                        imageMieliala.setImageResource(R.drawable.smiley7);
                        break;
                    case 8:
                        imageMieliala.setImageResource(R.drawable.smiley8);
                        break;
                    case 9:
                        imageMieliala.setImageResource(R.drawable.smiley9);
                        break;
                    case 10:
                        imageMieliala.setImageResource(R.drawable.smiley10);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //These methods are not used but need to be declared
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //These methods are not used but need to be declared
            }
        });

        //Declares the listener for the button to save a mood state
        findViewById(R.id.buttonAddMood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Depending on the seekbar's progress, saves a mood state from 0-10 to the user objects mood history
                int progress = ((SeekBar)findViewById(R.id.seekbarMieliala)).getProgress();
                user.mood.addMoodRecord(progress);
                updateGraph(user);
            }
        });
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        MainActivity.menuIconHighlight(bottomNavigationView, 4);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(MoodActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_weight_scale:
                        Intent weight = new Intent(MoodActivity.this, WeightActivity.class);
                        startActivity(weight.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(MoodActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(MoodActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = pref.getUser();
        updateGraph(user);
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
     * Updates mood graph with newest information.
     * @param user  Gets mood history list from user.
     */
    protected void updateGraph(User user) {
        GraphView graph = findViewById(R.id.moodGraph);
        ArrayList moodHistory = user.mood.getMoodHistory();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < moodHistory.size(); i++) {
            DataPoint dataPoint = new DataPoint(i, (int) moodHistory.get(i));
            series.appendData(dataPoint, true, moodHistory.size());
        }

        graph.removeAllSeries();
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        graph.addSeries(series);
        graph.setTitle("Mieliala");
        graph.getViewport().setScalable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        if (moodHistory.size() < 2) {
            graph.getViewport().setMaxX(1);
        } else {
            graph.getViewport().setMaxX(moodHistory.size() - 1);
        }
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setMinY(0);
    }
}
