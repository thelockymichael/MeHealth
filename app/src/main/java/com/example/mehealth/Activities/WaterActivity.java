package com.example.mehealth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Objects;

public class WaterActivity extends AppCompatActivity {
    private static final String TAG = "WaterActivity";

    User user;
    Toolbar toolbar;
    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vesi);

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
        MainActivity.menuIconHighlight(bottomNavigationView, 2);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(WaterActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
                        Intent weight = new Intent(WaterActivity.this, WeightActivity.class);
                        startActivity(weight.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(WaterActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(WaterActivity.this, MoodActivity.class);
                        startActivity(mood.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });

        //Set the onClickListeners for the imagebuttons to add water drank throughout the day
        findViewById(R.id.button1dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.water.drinkWater(1);
                paivitaVesi(user);
            }
        });
        findViewById(R.id.button2dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.water.drinkWater(2);
                paivitaVesi(user);
            }
        });
        findViewById(R.id.button5dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.water.drinkWater(5);
                paivitaVesi(user);
            }
        });
        findViewById(R.id.button1l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.water.drinkWater(10);
                paivitaVesi(user);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        user = pref.getUser();
        user.water.checkWater(user, pref);
        paivitaVesi(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        finish();
    }

    /**
     * Updates the textview containing the amount of water drank today.
     * @param user  User object in use.
     */
    protected void paivitaVesi(User user) {
        TextView juotuMaara = findViewById(R.id.juotuMaara);
        juotuMaara.setText(String.format(Locale.getDefault(), "%ddl", user.water.getWaterDrankToday(user, pref)));
        pref.saveUser(user);
    }

}
