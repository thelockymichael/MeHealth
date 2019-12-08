package com.mehealth.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mehealth.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mehealth.User.WeightValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "WeightActivity";
    private User mUser;
    private SharedPref mPref;
    private Boolean mSettingsOrBPChartOpened;
    private Date mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        mPref = new SharedPref(getApplicationContext());
        mDate = Calendar.getInstance().getTime();

        //Sets up the top toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Paino");
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupNavBar();
        setupBtnListeners();
        setupEditTexts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSettingsOrBPChartOpened = false;
        mUser = mPref.getUser();
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!mSettingsOrBPChartOpened) {
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
            mSettingsOrBPChartOpened = true;
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
     * Updates the text and chart with the latest values.
     */
    private void updateUI() {
        updateText();
        updateChart();
    }

    /**
     * Updates the TextViews with the latest values from weight and blood pressures.
     */
    private void updateText() {
        updateDateText();

        TextView weightText = findViewById(R.id.tvWeight);
        TextView lowerBPText = findViewById(R.id.tvLowerBP);
        TextView upperBPText = findViewById(R.id.tvUpperBP);

        weightText.setText(String.format(Locale.getDefault(), "Paino\n%d", mUser.weight.getLatestWeight()));
        lowerBPText.setText(String.format(Locale.getDefault(), "AlaP\n%d", mUser.bloodPressure.getLatestLowerBP()));
        upperBPText.setText(String.format(Locale.getDefault(), "YläP\n%d", mUser.bloodPressure.getLatestUpperBP()));

        ((EditText)findViewById(R.id.etWeight)).setText("");
        ((EditText)findViewById(R.id.etLowerBP)).setText("");
        ((EditText)findViewById(R.id.etUpperBP)).setText("");
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
     * Updates the weight chart with the newest values
     */
    private void updateChart() {
        //Declare needed variables
        final DateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
        final LineChart chart = findViewById(R.id.weightChart);
        List<Entry> weightEntries = new ArrayList<>();
        ArrayList<WeightValue> weightHistory = mUser.weight.getWeightHistory();


        //Sort the weight list by date
        Collections.sort(weightHistory, new Comparator<WeightValue>() {
            @Override
            public int compare(WeightValue o1, WeightValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });


        //Set entries to entry list from weight history list
        for (int i = 0; i < weightHistory.size(); i++) {
            //Get the current weight weight value and intialize the current variables
            WeightValue weightValue = weightHistory.get(i);
            Date date = weightValue.getDate();
            float weight = (float) weightValue.getWeight();

            //Add the entries to the entries list
            weightEntries.add(new Entry(date.getTime(), weight));
        }

        //Setup XAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(weightHistory.size(), false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dateFormat.format(value);
            }
        });

        //Setups YAxis
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);

        //Create linedata from the entries list
        final LineDataSet weightDataSet = new LineDataSet(weightEntries, "Paino");

        //Customize chart
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.setMaxHighlightDistance(20);

        weightDataSet.setValueTextSize(10);
        weightDataSet.setColor(Color.BLUE);
        weightDataSet.setFillAlpha(10);
        weightDataSet.setLineWidth(3f);
        weightDataSet.setCircleRadius(4);

        weightDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        });

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WeightActivity.this);
                builder.setTitle("Poista arvo")
                        .setMessage("Oletko varma?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                weightDataSet.removeEntry(e);
                                chart.invalidate();

                                mUser.weight.removeWeightByDate(e.getX());
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //Add the values to the chart and intialize it
        LineData weightLineData = new LineData(weightDataSet);
        chart.setData(weightLineData);
        chart.invalidate();
    }

    /**
     * Gets the values from mUser input and saves them into the mUser's history.
     */
    private void buttonAddValues() {
        EditText editTextWeight = findViewById(R.id.etWeight);
        EditText editTextLowerBP = findViewById(R.id.etLowerBP);
        EditText editTextUpperBP = findViewById(R.id.etUpperBP);

        String weight = editTextWeight.getText().toString();
        String lowerBP = editTextLowerBP.getText().toString();
        String upperBP = editTextUpperBP.getText().toString();

        if (!weight.isEmpty()) mUser.weight.addWeightRecord(Integer.parseInt(weight), mDate);
        if (!lowerBP.isEmpty() && !upperBP.isEmpty()) {
            mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
            mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
        }
    }

    private void setupEditTexts() {
        EditText paino = findViewById(R.id.etWeight);
        EditText alaPaine = findViewById(R.id.etLowerBP);
        EditText ylaPaine = findViewById(R.id.etUpperBP);

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

    /**
     * Setup bottom navigation bar
     */
    private void setupNavBar() {
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
    }

    private void setupBtnListeners() {
        findViewById(R.id.btnAddValue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddValues();
                updateUI();
                MainActivity.hideKeyboard(getApplicationContext(), v);

            }
        });

        findViewById(R.id.btnOpenBPChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bpChart = new Intent(WeightActivity.this, BPChartActivity.class);
                startActivity(bpChart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                mSettingsOrBPChartOpened = true;
            }
        });

        findViewById(R.id.tvDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
    }
}




