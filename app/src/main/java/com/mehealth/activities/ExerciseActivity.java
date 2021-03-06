package com.mehealth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mehealth.user.values.Exercise;
import com.mehealth.user.values.Exercises;
import com.mehealth.R;
import com.mehealth.utilities.SharedPref;
import com.mehealth.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

/**
 * Activity that contains list of exercises the user can click to open the exercise adding window.
 * @author Amin Karaoui
 */
public class ExerciseActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.mehealth.MESSAGE";
    private User mUser;
    private SharedPref mPref;
    private Boolean mSettingsOrDetailsOpened;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        mPref = new SharedPref(getApplicationContext());

        //Sets up the top toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Liikunta");
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNavBar();
        setupExerciseListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingsOrDetailsOpened = false;
        mUser = mPref.getUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!mSettingsOrDetailsOpened) {
            finish();
        }
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
            mSettingsOrDetailsOpened = true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup bottom navigation bar.
     */
    private void setupNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        MainActivity.menuIconHighlight(bottomNavigationView, 3);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(ExerciseActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_weight_scale:
                        Intent weight = new Intent(ExerciseActivity.this, WeightActivity.class);
                        startActivity(weight.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(ExerciseActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(ExerciseActivity.this, MoodActivity.class);
                        startActivity(mood.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * Sets up the actual list view containing the exercises from "Exercises" singleton.
     */
    private void setupExerciseListView() {
        ListView exercises = findViewById(R.id.exerciseListView);
        exercises.setAdapter(new ArrayAdapter<Exercise>(
                this,
                R.layout.exercise_item_layout,
                Exercises.getInstance().getExercises()
        ));
        exercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent exercise = new Intent(ExerciseActivity.this, ExerciseDetailsActivity.class);
                exercise.putExtra(EXTRA_MESSAGE, position);
                startActivity(exercise);
                mSettingsOrDetailsOpened = true;
            }
        });
    }
}


