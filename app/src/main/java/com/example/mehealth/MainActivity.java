package com.example.mehealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    User user;
    Toolbar toolbar;
    SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = new SharedPref(getApplicationContext());

        //Sets up the top toolbar
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MeHealth");
    }

    //Creates the toolbar menu according to toolbar_menu file in menu folder
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    //Listener for the settings icon in the top toolbar. Opens the settings activity if clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);

        //Highlight the icon for the current tab
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //Checks if a bottom navigation icon is pressed
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        //Clicking the current activitys icon does nothing
                        break;
                    case R.id.ic_attach_money:
                        //Depending on the icon clicked, starts the corresponding activity
                        Intent weight = new Intent(MainActivity.this, WeightActivity.class);
                        startActivity(weight.addFlags(weight.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(MainActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(water.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(MainActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(exercise.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(MainActivity.this, MoodActivity.class);
                        startActivity(mood.addFlags(mood.FLAG_ACTIVITY_NO_ANIMATION));
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

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("H");
        String formattedDate = dateFormat.format(date);

        //Based on the hour, the welcome string changes appropriately
        int time = Integer.parseInt(formattedDate);
        String hello;
        if (time >= 21 || time <= 3) hello = "Öitä";
        else if (time <= 10) hello = "Huomenta";
        else if (time <= 17) hello = "Päivää";
        else hello = "Iltaa";

        //Takes the name set in settings from shared preferences
        String name = pref.getString("name", "nimi");

        //Sets the textviews
        TextView textHello = findViewById(R.id.textHello);
        TextView textWaterDrankToday = findViewById(R.id.textWaterDrankToday);
        textHello.setText(hello + "\n" + name);
        textWaterDrankToday.setText("Tänään juotu " + user.water.getWaterDrankToday() + "dl");

        ((TextView)findViewById(R.id.textMoodNow)).setText("Viimeisin mielialasi oli\n" + user.mood.getLatestMoodRecord());

    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
    }

}

