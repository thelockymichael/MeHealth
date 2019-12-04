package com.mehealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mehealth.Exercise;
import com.mehealth.Exercises;
import com.mehealth.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;

import java.util.Objects;

public class ExerciseDetailsActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    SharedPref pref;
    Boolean settingsOrDetailsOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        pref = new SharedPref(getApplicationContext());
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("MeHealth");

        final Bundle received = getIntent().getExtras();
        int position = received.getInt(ExerciseActivity.EXTRA_MESSAGE, 0);
        final Exercise selectedExercise = Exercises.getInstance().getExercise(position);

        ((TextView)findViewById(R.id.textExerciseName))
                .setText(selectedExercise.getNimi());

        final EditText editHowManyMinutesExercised = findViewById(R.id.editHowManyMinutesExercised);
        setupEditText(editHowManyMinutesExercised);

        ((Button)findViewById(R.id.buttonAddExercise)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringMinutes = editHowManyMinutesExercised.getText().toString();
                if (!stringMinutes.isEmpty()) {
                    int iMinutes = Integer.parseInt(stringMinutes);
                    user.exercisedToday.addExercise(selectedExercise, iMinutes, user);
                    editHowManyMinutesExercised.setText("");
                }
                MainActivity.hideKeyboard(getApplicationContext(), v);
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


}