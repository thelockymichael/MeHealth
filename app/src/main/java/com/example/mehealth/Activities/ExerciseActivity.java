package com.example.mehealth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class ExerciseActivity extends AppCompatActivity {
    private static final String TAG = "ExerciseActivity";
    User user;
    Toolbar toolbar;
    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liikunta);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        finish();
    }
}
