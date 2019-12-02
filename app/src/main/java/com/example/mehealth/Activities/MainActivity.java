package com.example.mehealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
        Objects.requireNonNull(getSupportActionBar()).setTitle("MeHealth");
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

        //Highlight the icon for the current tab
        menuIconHighlight(bottomNavigationView, 0);

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
                        startActivity(weight.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(MainActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(MainActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(MainActivity.this, MoodActivity.class);
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

        //Takes the name set in settings from shared preferences
        String name = pref.getString("name", "tuntematon");

        //Sets the textviews
        TextView textHello = findViewById(R.id.textHello);
        TextView textWaterDrankToday = findViewById(R.id.textWaterDrankToday);
        textHello.setText(String.format("%s\n%s", greeting(), name));
        textWaterDrankToday.setText(String.format(Locale.getDefault(), "Tänään juotu %ddl", user.water.getWaterDrankToday(user, pref)));

        ((TextView)findViewById(R.id.textMoodNow)).setText(String.format(Locale.getDefault(), "Viimeisin mielialasi oli\n%d", user.mood.getLatestMoodRecord()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
    }

    public String greeting() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("H", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        int time = Integer.parseInt(formattedDate);
        String greeting;
        if (time >= 21 || time <= 3) greeting = "Öitä";
        else if (time <= 10) greeting = "Huomenta";
        else if (time <= 17) greeting = "Päivää";
        else greeting = "Iltaa";
        return greeting;
    }

    public static void menuIconHighlight(BottomNavigationView bottomNavigationView, int item) {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(item);
        menuItem.setChecked(true);
    }

}

