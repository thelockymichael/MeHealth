package com.mehealth.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Amin Karaoui
 */
public class WaterActivity extends AppCompatActivity {
    private User mUser;
    private SharedPref mPref;
    private Boolean mSettingsOpened;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        mPref = new SharedPref(getApplicationContext());

        //Sets up the top toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Vesi");
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNavBar();
        setupBtnListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingsOpened = false;
        mUser = mPref.getUser();
        mUser.water.checkWater(mPref);
        updateWater(mUser);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!mSettingsOpened) {
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
            mSettingsOpened = true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Setup bottom navigation bar
     */
    private void setupNavBar() {
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
                    case R.id.ic_weight_scale:
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
    }

    /**
     * Updates the textview containing the amount of water drank today.
     * @param user  User object in use.
     */
    private void updateWater(User user) {
        TextView juotuMaara = findViewById(R.id.tvWaterDrankToday);
        juotuMaara.setText(String.format(Locale.getDefault(), "%ddl", user.water.getWaterDrankToday(mPref)));
        mPref.saveUser(user);
    }

    private void setupBtnListeners() {
        //Set the onClickListeners for the imagebuttons to add water drank throughout the day
        findViewById(R.id.btn1dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.water.drinkWater(1);
                updateWater(mUser);
            }
        });
        findViewById(R.id.btn2dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.water.drinkWater(2);
                updateWater(mUser);
            }
        });
        findViewById(R.id.btn5dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.water.drinkWater(5);
                updateWater(mUser);
            }
        });
        findViewById(R.id.btn1l).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.water.drinkWater(10);
                updateWater(mUser);
            }
        });
    }

}
