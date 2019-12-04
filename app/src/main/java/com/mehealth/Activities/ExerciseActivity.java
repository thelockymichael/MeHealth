package com.mehealth.Activities;

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

import com.mehealth.Exercise;
import com.mehealth.Exercises;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class ExerciseActivity extends AppCompatActivity {
    private static final String TAG = "ExerciseActivity";
    public static final String EXTRA_MESSAGE = "com.mehealth.MESSAGE";
    User user;
    Toolbar toolbar;
    SharedPref pref;
    Boolean settingsOrDetailsOpened;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        pref = new SharedPref(getApplicationContext());
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MeHealth");

        ListView exercises = findViewById(R.id.exerciseListView);
        exercises.setAdapter(new ArrayAdapter<Exercise>(
           this,
                android.R.layout.simple_list_item_1,
                Exercises.getInstance().getExercises()
        ));
        exercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent exercise = new Intent(ExerciseActivity.this, ExerciseDetailsActivity.class);
                exercise.putExtra(EXTRA_MESSAGE, position);
                startActivity(exercise);
                settingsOrDetailsOpened = true;
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
            settingsOrDetailsOpened = true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                    case R.id.ic_attach_money:
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

    @Override
    protected void onResume() {
        super.onResume();
        user = pref.getUser();
        settingsOrDetailsOpened = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        if (!settingsOrDetailsOpened) {
            finish();
        }
    }
}
