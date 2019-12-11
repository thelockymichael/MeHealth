package com.mehealth.activities;

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
import com.mehealth.utilities.SharedPref;
import com.mehealth.user.values.BloodValues;
import com.mehealth.user.User;
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
 * @author Amin Karaoui
 */
public class MainActivity extends AppCompatActivity {

    private User mUser;
    private SharedPref mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPref = new SharedPref(getApplicationContext());

        //Sets up the top toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MeHealth");
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNavBar();
        setupLayoutClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Always gets the latest user from shared preferences when activity resumes
        mUser = mPref.getUser();
        mUser.exercisedToday.checkCalories(mPref);
        mUser.water.checkWater(mPref);

        updateTextViews();
        updateArrow();
        updateBPWarning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Saves the user into shared preferences whenever the activity is paused
        mPref.saveUser(mUser);
    }

    /**
     * Creates the toolbar menu according to toolbar_menu file in menu folder
     * @param menu menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    /**
     * Listener for the settings icon in the top toolbar. Opens the settings activity if clicked
     * @param item item clicked
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
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
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Decides the greeting based on time of day taken from the phone.
     * @return  A greeting string
     */
    private String greeting() {
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
    private void layoutOnClicks(ConstraintLayout layout, final Class<?> newActivityClass) {
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
    protected void updateTextViews() {
        //Takes the user's name that is set in settings from shared preferences, default is empty
        String name = mPref.getString("name", "");

        //Initialize the TextViews
        TextView tvMoodNow = findViewById(R.id.tvMoodNow);
        TextView tvMoodNowDescription = findViewById(R.id.tvMoodNowDescription);
        TextView tvWeightNumber = findViewById(R.id.tvWeightNumber);
        TextView tvLowerBPNumber = findViewById(R.id.tvLowerBPNumber);
        TextView tvUpperBPNumber = findViewById(R.id.tvUpperBPNumber);
        TextView tvWaterLeftToDrink = findViewById(R.id.tvWaterLeftToDrink);
        TextView tvCaloriesBurnedToday = findViewById(R.id.tvCaloriesBurnedToday);
        TextView tvCaloriesBurnedTodayDescription = findViewById(R.id.tvCaloriesBurnedTodayDescription);

        //Sets the greeting TextView
        TextView textHello = findViewById(R.id.tvGreeting);
        textHello.setText(String.format("%s %s", greeting(), name));

        //Sets the mood TextViews
        tvMoodNow.setText(String.format(Locale.getDefault(), "%d", mUser.mood.getLatestMood()));
        tvMoodNowDescription.setText(R.string.activity_main_lastMood);

        //Sets the weight and blood pressure TextViews
        tvWeightNumber.setText(String.format(Locale.getDefault(), "%d", mUser.weight.getLatestWeight()));
        tvLowerBPNumber.setText(String.format(Locale.getDefault(), "%d", mUser.bloodPressure.getLatestLowerBP()));
        tvUpperBPNumber.setText(String.format(Locale.getDefault(), "%d", mUser.bloodPressure.getLatestUpperBP()));

        //Sets the water left to drink today TextView
        tvWaterLeftToDrink.setText(String.format(Locale.getDefault(), "%ddl", mUser.water.howMuchWaterToDrink()));

        //Sets the calories burned today number TextView
        tvCaloriesBurnedToday.setText(String.format(Locale.getDefault(), "%d", mUser.exercisedToday.getCaloriesBurnedToday()));

        /*
        Sets the calories burned today description TextView
        If 1 calorie has been burned the text is "Kalori poltettu tänään" / "Calorie burned today"
        Else it is "Kaloria poltettu tänään" / "Calories burned today"
         */
        if (mUser.exercisedToday.getCaloriesBurnedToday() == 1) {
            tvCaloriesBurnedTodayDescription.setText(R.string.activity_main_calorieBurnedToday);
        } else {
            tvCaloriesBurnedTodayDescription.setText(R.string.activity_main_caloriesBurnedToday);
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
        ImageView imgArrowWeight = findViewById(R.id.imgArrowWeight);
        if (mUser.weight.isLatestWeightLower()) {
            imgArrowWeight.setImageResource(R.drawable.ic_arrow_downward_grey);
        } else {
            imgArrowWeight.setImageResource(R.drawable.ic_arrow_upward_gray);
        }
        if (mUser.weight.isWeightNotChanged()) {
            imgArrowWeight.setVisibility(View.INVISIBLE);
        } else {
            imgArrowWeight.setVisibility(View.VISIBLE);
        }

        //Arrow for lower blood pressure
        ImageView imgArrowLowerBP = findViewById(R.id.imgArrowLowerBP);
        if (mUser.bloodPressure.isLatestBPLower("lower")) {
            imgArrowLowerBP.setImageResource(R.drawable.ic_arrow_downward_grey);
        } else {
            imgArrowLowerBP.setImageResource(R.drawable.ic_arrow_upward_gray);
        }
        if (mUser.bloodPressure.isBPNotChanged("lower")) {
            imgArrowLowerBP.setVisibility(View.INVISIBLE);
        } else {
            imgArrowLowerBP.setVisibility(View.VISIBLE);
        }

        //Arrow for upper blood pressure
        ImageView imgArrowUpperBP = findViewById(R.id.imgArrowUpperBP);
        if (mUser.bloodPressure.isLatestBPLower("upper")) {
            imgArrowUpperBP.setImageResource(R.drawable.ic_arrow_downward_grey);
        } else {
            imgArrowUpperBP.setImageResource(R.drawable.ic_arrow_upward_gray);
        }
        if (mUser.bloodPressure.isBPNotChanged("upper")) {
            imgArrowUpperBP.setVisibility(View.INVISIBLE);
        } else {
            imgArrowUpperBP.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Setup bottom navigation bar
     */
    private void setupNavBar() {
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
    }

    /**
     * Checks users age and latest bloodpressure values. If bloodpressure is too high, a warning appears on main page.
     */
    private void updateBPWarning() {
        int normalDiastolic;
        int normalSystolic;

        int userAge = Integer.parseInt(mPref.getString("ika", "0"));

        if (userAge >  18) {
            normalDiastolic = BloodValues.getInstance().getBloodvalues().get(0).getNormalDiastolic();
            normalSystolic = BloodValues.getInstance().getBloodvalues().get(0).getNormalSystolic();
        } else {
            normalDiastolic = BloodValues.getInstance().getBloodvalues().get(userAge).getNormalDiastolic();
            normalSystolic = BloodValues.getInstance().getBloodvalues().get(userAge).getNormalSystolic();
        }

        if (mUser.bloodPressure.getLatestLowerBP() > normalDiastolic) {
            //High lower BP
            ((TextView)findViewById(R.id.warningHighDBP)).setText(R.string.activity_main_highBloodPressure);
        } else ((TextView)findViewById(R.id.warningHighDBP)).setText("");

        if (mUser.bloodPressure.getLatestUpperBP() > normalSystolic) {
            //High upper BP
            ((TextView)findViewById(R.id.warningHighSBP)).setText(R.string.activity_main_highBloodPressure);
        } else ((TextView)findViewById(R.id.warningHighSBP)).setText("");
    }

    private void setupLayoutClicks() {
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutWeight), WeightActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutWater), WaterActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutUpperBP), WeightActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutCaloriesBurnedToday), ExerciseActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutLowerBP), WeightActivity.class);
        layoutOnClicks((ConstraintLayout)findViewById(R.id.constraintLayoutLatestMood), MoodActivity.class);
    }

}

