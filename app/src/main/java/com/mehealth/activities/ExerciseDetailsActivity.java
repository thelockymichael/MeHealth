package com.mehealth.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mehealth.user.values.Exercise;
import com.mehealth.user.values.Exercises;
import com.mehealth.utilities.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.utilities.SharedPref;
import com.mehealth.user.User;

import java.util.Locale;
import java.util.Objects;

/**
 * Exercise detail activity where user can add an exercise done.
 * @author Amin Karaoui
 */
public class ExerciseDetailsActivity extends AppCompatActivity {
    private User mUser;
    private SharedPref mPref;
    private Boolean mSettingsOrDetailsOpened;
    private double mCurrentIntensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        mPref = new SharedPref(getApplicationContext());
        mUser = mPref.getUser();
        //Sets the default intensity to 1 since default radio button checked is normal intensity.
        mCurrentIntensity = 1;

        //Sets up toolbar with back button on the left of the title
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lisää liikunta");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Gets the info from Exercise activity
        Bundle received = getIntent().getExtras();
        int position = Objects.requireNonNull(received).getInt(ExerciseActivity.EXTRA_MESSAGE, 0);
        final Exercise selectedExercise = Exercises.getInstance().getExercise(position);

        //Sets the textview as the name of the exercise
        ((TextView)findViewById(R.id.tvExerciseName))
                .setText(selectedExercise.getName());

        //Sets up rest of editTexts and TextViews
        final EditText etHowManyMinutesExercised = findViewById(R.id.etMinutesExercised);
        setupEditText(etHowManyMinutesExercised);
        setupTextViews(selectedExercise);

        //Sets up the listeners for the intensity radio buttons
        RadioGroup intensityGroup = findViewById(R.id.intensityGroup);
        intensityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.intensityRelaxed:
                        mCurrentIntensity = 0.75;
                        setupTextViews(selectedExercise);
                        break;

                    case R.id.intensityNormal:
                        mCurrentIntensity = 1;
                        setupTextViews(selectedExercise);
                        break;

                    case R.id.intensityHeavy:
                        mCurrentIntensity = 1.25;
                        setupTextViews(selectedExercise);
                        break;
                }
            }
        });

        //Sets the listener for the button to add an exercise
        findViewById(R.id.btnAddExercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringMinutes = etHowManyMinutesExercised.getText().toString();
                if (!stringMinutes.isEmpty()) {
                    int iMinutes = Integer.parseInt(stringMinutes);
                    mUser.exercisedToday.addExercise(selectedExercise, iMinutes, mUser, mCurrentIntensity);
                    etHowManyMinutesExercised.setText("");
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = mPref.getUser();
        mSettingsOrDetailsOpened = false;
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
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                mSettingsOrDetailsOpened = true;
                break;

            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the filter for editText and makes it so unfocusing it will hide they keyboard.
     * @param editText EditText to modify.
     */
    private void setupEditText(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MainActivity.hideKeyboard(getApplicationContext(), v);
                }
            }
        });
        editText.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});
    }

    /**
     * Sets up the textView for telling the user how calories they burn per minute.
     * @param exercise The exercise to calculate from.
     */
    private void setupTextViews(Exercise exercise) {
        TextView weightNow= findViewById(R.id.tvWeightNow);

        weightNow.setText(String.format(Locale.getDefault(), "Poltat %d kaloria tunnissa painollasi",
                Math.round((1.0 * exercise.getCaloriesInAnHourPerKilo(mUser)) * mCurrentIntensity)));
    }


}
