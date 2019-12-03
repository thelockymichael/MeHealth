package com.example.mehealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Home screen of the app.
 * Welcomes the user and contains recent information about the user.
 * Always has one task of the activity open so every backclick redirects to the main activity
 */
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
        //Always gets the latest user from shared preferences when activity resumes
        user = pref.getUser();

        //Takes the user's name that is set in settings from shared preferences
        String name = pref.getString("name", "tuntematon");

        //Sets the textviews
        TextView textHello = findViewById(R.id.textHello);
        textHello.setText(String.format("%s %s", greeting(), name));

        ((TextView)findViewById(R.id.textMoodNow)).setText(String.format(Locale.getDefault(), "Viimeisin mielialasi oli\n%d", user.mood.getLatestMoodRecord()));
        ((TextView)findViewById(R.id.textWeightNumber)).setText(String.format(Locale.getDefault(), "%d", user.weight.getLatestWeight()));

        ((TextView)findViewById(R.id.textWaterLeftToDrink)).setText(String.format(Locale.getDefault(), "%ddl", user.water.howMuchWaterToDrink()));
        ((TextView)findViewById(R.id.textLowerBPNumber)).setText(String.format(Locale.getDefault(), "%d", user.bloodPressure.getLatestLowerBP()));
        ((TextView)findViewById(R.id.textUpperBPNumber)).setText(String.format(Locale.getDefault(), "%d", user.bloodPressure.getLatestUpperBP()));

        updateArrow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Saves the user into shared preferences whenever the activity is paused
        pref.saveUser(user);
    }

    /**
     * Decides the greeting based on time of day taken from the phone.
     * @return  A greeting string
     */
    public String greeting() {
        //Gets the date and time from android
        Date date = Calendar.getInstance().getTime();
        //Sets the wanted format to only contain the current hour
        SimpleDateFormat dateFormat = new SimpleDateFormat("H", Locale.getDefault());
        //Formats the string to only contain the current hour
        String formattedDate = dateFormat.format(date);

        //Converts the hour string into an integer
        int time = Integer.parseInt(formattedDate);
        String greeting;
        //Depending on the time of day the greeting string changes
        if (time >= 21 || time <= 3) greeting = "Öitä";
        else if (time <= 10) greeting = "Huomenta";
        else if (time <= 17) greeting = "Päivää";
        else greeting = "Iltaa";
        return greeting;
    }

    /**
     * A method that lights up the bottom navigation menu item depending on which activity is open.
     * Called from every activity that has the bottom navigation toolbar.
     * @param bottomNavigationView  The bottom navigation toolbar
     * @param item                  The number of the activity in the menu list.
     */
    public static void menuIconHighlight(BottomNavigationView bottomNavigationView, int item) {
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(item);
        menuItem.setChecked(true);
    }

    public void updateArrow() {
        ImageView imageArrowWeight = findViewById(R.id.imageArrowWeight);
        if (user.weight.latestWeightLower()) {
            imageArrowWeight.setImageResource(R.drawable.ic_arrow_downward_grey);
        } else {
            imageArrowWeight.setImageResource(R.drawable.ic_arrow_upward_gray);
        }
        if (user.weight.weightNotChanged()) {
            imageArrowWeight.setVisibility(View.INVISIBLE);
        } else {
            imageArrowWeight.setVisibility(View.VISIBLE);
        }

        ImageView imageArrowLowerBP = findViewById(R.id.imageArrowLowerBP);
        if (user.bloodPressure.latestBPLower("lower")) {
            imageArrowLowerBP.setImageResource(R.drawable.ic_arrow_downward_grey);
        } else {
            imageArrowLowerBP.setImageResource(R.drawable.ic_arrow_upward_gray);
        }
        if (user.bloodPressure.bpNotChanged("lower")) {
            imageArrowLowerBP.setVisibility(View.INVISIBLE);
        } else {
            imageArrowLowerBP.setVisibility(View.VISIBLE);
        }

        ImageView imageArrowUpperBP = findViewById(R.id.imageArrowUpperBP);
        if (user.bloodPressure.latestBPLower("upper")) {
            imageArrowUpperBP.setImageResource(R.drawable.ic_arrow_downward_grey);
        } else {
            imageArrowUpperBP.setImageResource(R.drawable.ic_arrow_upward_gray);
        }
        if (user.bloodPressure.bpNotChanged("upper")) {
            imageArrowUpperBP.setVisibility(View.INVISIBLE);
        } else {
            imageArrowUpperBP.setVisibility(View.VISIBLE);
        }

    }

}

