package com.mehealth.Activities;

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

import com.mehealth.Exercise;
import com.mehealth.Exercises;
import com.mehealth.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;

import java.util.Locale;
import java.util.Objects;

public class ExerciseDetailsActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    SharedPref pref;
    Boolean settingsOrDetailsOpened;
    double currentIntensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        pref = new SharedPref(getApplicationContext());
        user = pref.getUser();
        currentIntensity = 1;
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lisää liikunta");

        final Bundle received = getIntent().getExtras();
        final int position = Objects.requireNonNull(received).getInt(ExerciseActivity.EXTRA_MESSAGE, 0);
        final Exercise selectedExercise = Exercises.getInstance().getExercise(position);

        ((TextView)findViewById(R.id.textExerciseName))
                .setText(selectedExercise.getNimi());

        final EditText editHowManyMinutesExercised = findViewById(R.id.editHowManyMinutesExercised);
        setupEditText(editHowManyMinutesExercised);
        setupTextViews(selectedExercise);

        RadioGroup intensityGroup = findViewById(R.id.intensityGroup);
        intensityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.intensityRelaxed:
                        currentIntensity = 0.75;
                        setupTextViews(selectedExercise);
                        break;

                    case R.id.intensityNormal:
                        currentIntensity = 1;
                        setupTextViews(selectedExercise);
                        break;

                    case R.id.intensityHeavy:
                        currentIntensity = 1.25;
                        setupTextViews(selectedExercise);
                        break;
                }
            }
        });


        findViewById(R.id.buttonAddExercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringMinutes = editHowManyMinutesExercised.getText().toString();
                if (!stringMinutes.isEmpty()) {
                    int iMinutes = Integer.parseInt(stringMinutes);
                    user.exercisedToday.addExercise(selectedExercise, iMinutes, user, currentIntensity);
                    editHowManyMinutesExercised.setText("");
                }
                finish();
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

    protected void setupEditText(EditText editText) {
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

    protected void setupTextViews(Exercise exercise) {
        TextView weightNow= findViewById(R.id.weightNow);
        //TextView caloriesBurnedPerHour = findViewById(R.id.caloriesBurnedPerHour);

        weightNow.setText(String.format(Locale.getDefault(), "Poltat %d kaloria tunnissa painollasi",
                Math.round((1.0 * exercise.getKaloritTunnissa(user)) * currentIntensity)));
    }


}
