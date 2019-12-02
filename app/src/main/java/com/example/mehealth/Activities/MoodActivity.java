package com.example.mehealth.Activities;

import android.annotation.SuppressLint;
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

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MoodActivity extends AppCompatActivity {
    //private static final String TAG = "MoodActivity";
    User user;
    Toolbar toolbar;
    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mieliala);
        pref = new SharedPref(getApplicationContext());

        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MeHealth");

        final ImageView imageMieliala = findViewById(R.id.imageMieliala);
        final TextView textMieliala = findViewById(R.id.textMieliala);
        textMieliala.setText("5");
        imageMieliala.setImageResource(R.drawable.smiley10);

        final SeekBar seekBarMood = findViewById(R.id.seekbarMieliala);
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMieliala.setText(Integer.toString(progress));
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
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.buttonAddMood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = ((SeekBar)findViewById(R.id.seekbarMieliala)).getProgress();
                user.mood.addMoodRecord(progress);
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
                    case R.id.ic_attach_money:
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        finish();
    }
}
