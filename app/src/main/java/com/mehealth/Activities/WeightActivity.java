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
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mehealth.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mehealth.User.WeightValue;

import org.w3c.dom.Text;

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
    private SharedPref mPref;
    private Boolean mSettingsOpened;
    private Date mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        mPref = new SharedPref(getApplicationContext());
        mDate = Calendar.getInstance().getTime();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Paino");

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

        findViewById(R.id.btnAddValue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddValues();
                updateUI();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });

        findViewById(R.id.btnDelLastWeightRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.weight.deleteRecord(mUser.weight.getWeightHistory().size() - 1);
                updateUI();
            }
        });

        findViewById(R.id.tvDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
        setupEditTexts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = mPref.getUser();
        mSettingsOpened = false;
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!mSettingsOpened) {
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
        if (item.getItemId() == R.id.settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            mSettingsOpened = true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, dayOfMonth);
        mDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
        updateDateText();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.getDatePicker().init(year, month, day, datePickerDialog);
        datePickerDialog.show();
    }

    /**
     * Updates the text and graph with the latest values.
     */
    private void updateUI() {
        updateText();
        updateGraph();
    }

    /**
     * Updates the TextViews with the latest values from weight and blood pressures.
     */
    private void updateText() {
        updateDateText();

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

    private void updateDateText() {
        TextView tvDate = findViewById(R.id.tvDate);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "/" + (month + 1) + "/" + year;
        tvDate.setText(date);
    }

    /**
     * Updates the weight graph with the newest values
     */
    private void updateGraph() {
        GraphView weightGraph = findViewById(R.id.weightGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        ArrayList<WeightValue> weightHistory = mUser.weight.getWeightHistory();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());

        Collections.sort(weightHistory, new Comparator<WeightValue>() {
            @Override
            public int compare(WeightValue o1, WeightValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        long firstDate = mDate.getTime();
        long lastDate = mDate.getTime();
        lastDate = lastDate + 86400000;
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

                DataPoint dataPoint = new DataPoint(date, weight);
                series.appendData(dataPoint, true, weightHistory.size());
            }
        }
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        weightGraph.setTitle("Paino");

        weightGraph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                return ""+((int) value);
            }

            @Override
            public void setViewport(Viewport viewport) {

            }
        });

        weightGraph.getGridLabelRenderer().setNumHorizontalLabels(3);
        weightGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(
                this,
                new SimpleDateFormat("dd-MM")
        ));

        if (weightHistory.size() > 1) weightGraph.getViewport().setScalable(true);

        weightGraph.getViewport().setXAxisBoundsManual(true);
        weightGraph.getViewport().setMinX(firstDate);
        weightGraph.getViewport().setMaxX(lastDate);

        weightGraph.getGridLabelRenderer().setHumanRounding(false, true);
    }

    /**
     * Gets the values from mUser input and saves them into the mUser's history.
     */
    private void buttonAddValues() {
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

    private void setupEditTexts() {
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
