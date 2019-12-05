package com.mehealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
                    case R.id.ic_weight_scale:
                        //Depending on the icon clicked, starts the corresponding activity
                        startSpecificActivity(WeightActivity.class);
                        break;

                    case R.id.ic_local_drink:
                        startSpecificActivity(WaterActivity.class);
                        break;

                    case R.id.ic_directions_run:
                        startSpecificActivity(ExerciseActivity.class);
                        break;

                    case R.id.ic_insert_emoticon:
                        startSpecificActivity(MoodActivity.class);
                        break;
                }
                return false;
            }
        });
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutWeight), WeightActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutWater), WaterActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutUpperBP), WeightActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutCaloriesBurnedToday), ExerciseActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutLowerBP), WeightActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutLatestMood), MoodActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Always gets the latest user from shared preferences when activity resumes
        user = pref.getUser();
        user.exercisedToday.checkCalories(pref);
        user.water.checkWater(pref);

        setupTextViews();
        updateArrow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Saves the user into shared preferences whenever the activity is paused
        pref.saveUser(user);
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

    /**
     * Hides keyboard.
     * @param c     Context of application
     * @param view  Current view
     */
    public static void hideKeyboard(Context c, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)c.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
     * Starts activity when layout is clicked.
     * @param layout            Layout to click
     * @param newActivityClass  Activity to start, e.g. ActivityName.class
     */
    protected void layoutOnClicks(ConstraintLayout layout, final Class<?> newActivityClass) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpecificActivity(newActivityClass);
            }
        });
    }

    /**
     * Starts specific activity with flag for no animation.
     * @param newActivityClass  Activity to start, e.g. ActivityName.class
     */
    public void startSpecificActivity(Class<?> newActivityClass) {
        Intent intent = new Intent(getApplicationContext(), newActivityClass);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    /**
     * Sets up the textViews for the home screen.
     */
    protected void setupTextViews() {
        //Takes the user's name that is set in settings from shared preferences, default is "tuntematon"
        String name = pref.getString("name", "tuntematon");

        //Sets the greeting TextView
        TextView textHello = findViewById(R.id.textHello);
        textHello.setText(String.format("%s %s", greeting(), name));

        //Sets the mood TextViews
        ((TextView)findViewById(R.id.textMoodNow)).setText(String.format(Locale.getDefault(), "%d", user.mood.getLatestMoodRecord()));
        ((TextView)findViewById(R.id.textMoodNowDescription)).setText("Viimeisin mielialasi oli");

        //Sets the weight and blood pressure TextViews
        ((TextView)findViewById(R.id.textWeightNumber)).setText(String.format(Locale.getDefault(), "%d", user.weight.getLatestWeight()));
        ((TextView)findViewById(R.id.textLowerBPNumber)).setText(String.format(Locale.getDefault(), "%d", user.bloodPressure.getLatestLowerBP()));
        ((TextView)findViewById(R.id.textUpperBPNumber)).setText(String.format(Locale.getDefault(), "%d", user.bloodPressure.getLatestUpperBP()));

        //Sets the water left to drink today TextView
        ((TextView)findViewById(R.id.textWaterLeftToDrink)).setText(String.format(Locale.getDefault(), "%ddl", user.water.howMuchWaterToDrink()));

        //Sets the calories burned today number TextView
        ((TextView)findViewById(R.id.textCaloriesBurnedToday)).setText(String.format(Locale.getDefault(), "%d", user.exercisedToday.getCaloriesBurnedToday()));

        /*
        Sets the calories burned today description TextView
        If 1 calorie has been burned the text is "Kalori poltettu tänään" / "Calorie burned today"
        Else it is "Kaloria poltettu tänään" / "Calories burned today"
         */
        TextView textCaloriesBurnedTodayDescription = findViewById(R.id.textCaloriesBurnedTodayDescription);
        if (user.exercisedToday.getCaloriesBurnedToday() == 1) {
            textCaloriesBurnedTodayDescription.setText("Kalori poltettu tänään");
        } else {
            textCaloriesBurnedTodayDescription.setText("Kaloria poltettu tänään");
        }
    }

    /**
     * Updates the ImageView arrows.
     * If there was no change then arrow is invisible.
     * If new value is higher than value before then image is arrow pointing up.
     * If new value is lower than value before then image is arrow pointing down.
     */
    protected void updateArrow() {
        //Arrow for weight
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

        //Arrow for lower blood pressure
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

        //Arrow for upper blood pressure
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

