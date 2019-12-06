package com.mehealth.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mehealth.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mehealth.User.WeightValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class WeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "WeightActivity";
    private User mUser;
    private Toolbar mToolbar;
    private SharedPref mPref;
    private Boolean mSettingsOpened;
    private Date mDate;
    private TextView mTVDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        mPref = new SharedPref(getApplicationContext());
        mToolbar = findViewById(R.id.toolbarTop);
        mDate = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH).getTime();
        mTVDate = findViewById(R.id.tvDate);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Paino");

        findViewById(R.id.tvDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        //datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        mTVDate.setText(date);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, dayOfMonth);
        mDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        Log.d(TAG, "onDateSet: " + mDate.toString());
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        MainActivity.menuIconHighlight(bottomNavigationView, 1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(WeightActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_weight_scale:
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(WeightActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(WeightActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(WeightActivity.this, MoodActivity.class);
                        startActivity(mood.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.buttonLisaaArvo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddValues(v);
                updateUI();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });

        findViewById(R.id.buttonDeleteLastWeightRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.weight.deleteRecord(mUser.weight.getWeightHistory().size() - 1);
                updateUI();
            }
        });
        setupEditTexts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = mPref.getUser();
        updateUI();
        mSettingsOpened = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!mSettingsOpened) {
            finish();
        }
    }

    /**
     * Updates the TextViews with the latest values from weight and blood pressures.
     */
    protected void updateText() {
        TextView weightText = findViewById(R.id.textViewWeight);
        TextView lowerBPText = findViewById(R.id.textViewLowerBP);
        TextView upperBPText = findViewById(R.id.textViewUpperBP);

        weightText.setText(String.format(Locale.getDefault(), "Paino\n%d", mUser.weight.getLatestWeight()));
        lowerBPText.setText(String.format(Locale.getDefault(), "AlaP\n%d", mUser.bloodPressure.getLatestLowerBP()));
        upperBPText.setText(String.format(Locale.getDefault(), "YläP\n%d", mUser.bloodPressure.getLatestUpperBP()));

        ((EditText)findViewById(R.id.editTextPaino)).setText("");
        ((EditText)findViewById(R.id.editTextAlaPaine)).setText("");
        ((EditText)findViewById(R.id.editTextYlaPaine)).setText("");
    }

    /**
     * Gets the values from mUser input and saves them into the mUser's history.
     * @param view  Current view
     */
    public void buttonAddValues(View view) {
        EditText editTextWeight = findViewById(R.id.editTextPaino);
        EditText editTextLowerBP = findViewById(R.id.editTextAlaPaine);
        EditText editTextUpperBP = findViewById(R.id.editTextYlaPaine);

        String weight = editTextWeight.getText().toString();
        String lowerBP = editTextLowerBP.getText().toString();
        String upperBP = editTextUpperBP.getText().toString();
        if (!weight.isEmpty()) mUser.weight.addWeightRecord(Integer.parseInt(weight), mDate);
        if (!lowerBP.isEmpty()) mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP));
        if (!upperBP.isEmpty()) mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP));
    }

    /**
     * Updates the weight graph with the newest values
     */
    protected void updateGraph() {
        GraphView weightGraph = findViewById(R.id.weightGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        ArrayList<WeightValue> weightHistory = mUser.weight.getWeightHistory();

        Collections.sort(weightHistory, new Comparator<WeightValue>() {
            @Override
            public int compare(WeightValue o1, WeightValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        long firstDate = 0;
        long lastDate = 1;
        if (weightHistory.size() > 1) {
            firstDate = weightHistory.get(0).getDate().getTime();
            lastDate = weightHistory.get(weightHistory.size() - 1).getDate().getTime();
        }

        weightGraph.removeAllSeries();
        weightGraph.addSeries(series);

        if (weightHistory.size() > 0) {
            for (int i = 0; i < weightHistory.size(); i++) {
                WeightValue weightValue = weightHistory.get(i);
                Date date = weightValue.getDate();
                Double weight = (double) weightValue.getWeight();
                Log.d(TAG, "updateGraph: " + date + "  paino: " + weight);

                DataPoint dataPoint = new DataPoint(date, weight);
                series.appendData(dataPoint, true, weightHistory.size());
            }
        }
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        weightGraph.setTitle("Paino");

        //weightGraph.getGridLabelRenderer().setNumHorizontalLabels(3);
        weightGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(
                this,
                new SimpleDateFormat("dd-MM")
        ));

        weightGraph.getViewport().setMinX(firstDate);
        weightGraph.getViewport().setMaxX(lastDate);
        weightGraph.getViewport().setXAxisBoundsManual(true);
        weightGraph.getViewport().setScalable(true);
        weightGraph.getGridLabelRenderer().setHumanRounding(false, true);
    }



    /**
     * Updates the text and graph with the latest values.
     */
    protected void updateUI() {
        updateText();
        updateGraph();
    }

    protected void setupEditTexts() {
        EditText paino = findViewById(R.id.editTextPaino);
        EditText alaPaine = findViewById(R.id.editTextAlaPaine);
        EditText ylaPaine = findViewById(R.id.editTextYlaPaine);

        paino.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});
        alaPaine.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});
        ylaPaine.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});

        paino.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
        alaPaine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
        ylaPaine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
    }
}
