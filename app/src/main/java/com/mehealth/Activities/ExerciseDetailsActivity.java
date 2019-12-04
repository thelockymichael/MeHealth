package com.mehealth.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.mehealth.Exercises;
import com.mehealth.R;

public class ExerciseDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        Bundle received = getIntent().getExtras();
        int position = received.getInt(ExerciseActivity.EXTRA_MESSAGE, 0);

        ((TextView)findViewById(R.id.exerciseName))
                .setText(Exercises.getInstance().getExercise(position).getNimi());
    }
}
