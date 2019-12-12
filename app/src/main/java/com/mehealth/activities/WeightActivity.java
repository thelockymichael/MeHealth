package com.mehealth.activities;

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
import com.mehealth.utilities.InputFilterMinMax;
import com.mehealth.R;
import com.mehealth.utilities.SharedPref;
import com.mehealth.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mehealth.user.values.WeightValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Activity to add weight and blood pressure values.
 * Also contains the chart for weight history.
 * @author Amin Karaoui
 */
public class WeightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private User mUser;
    private SharedPref mPref;
    private Boolean mSettingsOrBPChartOpened;
    private Date mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        mPref = new SharedPref(getApplicationContext());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 0,0,0);
        calendar.set(Calendar.MILLISECOND, 0);
        mDate = calendar.getTime();

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Get date from date picker
        Calendar calendar = new GregorianCalendar();
        //calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //Set the milliseconds to 0 because java.util.Date is not accurate on milliseconds
        mDate = calendar.getTime();
        updateDateText();
        updateText();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        //Get the current year, month and date from mDate into a calendar
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Set the max date and current date picked based on just created calendar
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.getDatePicker().init(year, month, day, datePickerDialog);
        datePickerDialog.show();
    }

    /**
     * Updates the text and chart with the latest values.
     */
    private void updateUI() {
        mUser.weight.sortListByDate();
        mUser.bloodPressure.sortListsByDate();
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

        mUser.weight.sortListByDate();
        mUser.bloodPressure.sortListsByDate();

        double weight = mUser.weight.getWeightByDate(mDate.getTime());
        int upperBP = mUser.bloodPressure.getUpperBPByDate(mDate.getTime());
        int lowerBP = mUser.bloodPressure.getLowerBPByDate(mDate.getTime());

        weightText.setText(String.format(Locale.getDefault(), "Paino\n%.1f", weight));
        lowerBPText.setText(String.format(Locale.getDefault(), "AlaP\n%d", lowerBP));
        upperBPText.setText(String.format(Locale.getDefault(), "YläP\n%d", upperBP));

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
        final LineChart chart = findViewById(R.id.chartWeight);
        List<Entry> weightEntries = new ArrayList<>();
        mUser.weight.sortListByDate();
        ArrayList<WeightValue> weightHistory = mUser.weight.getWeightHistory();

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



        //Shows the values as integers rather than floats
        weightDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.1f", value);
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
                                updateText();
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

        final String weight = editTextWeight.getText().toString();
        final String lowerBP = editTextLowerBP.getText().toString();
        final String upperBP = editTextUpperBP.getText().toString();

        /*if (!weight.isEmpty() && mUser.weight.isDateInList(mDate)) {
            mUser.weight.addWeightRecord(Integer.parseInt(weight), mDate);
        }
        if (!lowerBP.isEmpty() && !upperBP.isEmpty() && mUser.bloodPressure.isDateInBothLists(mDate)) {
            mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
            mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
        }*/

        /*
        Very sorry to anyone reading this
         */
        if (!weight.isEmpty() && !lowerBP.isEmpty() && !upperBP.isEmpty()) {
            if (mUser.weight.isDateInList(mDate) && mUser.bloodPressure.isDateInBothLists(mDate)) {
                //dialog to change both
                AlertDialog.Builder builder = new AlertDialog.Builder(WeightActivity.this);
                builder.setTitle("Tämän päivän paino ja verenpaine ovat asetettu.")
                        .setMessage("Korvaa arvot?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUser.weight.removeWeightByDate(mDate.getTime());
                                mUser.weight.addWeightRecord(Double.parseDouble(weight), mDate);

                                mUser.bloodPressure.removeBPByDate(mDate.getTime());
                                mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
                                mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
                                updateUI();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            } else if (mUser.weight.isDateInList(mDate)) {
                //List contains weight but not bp, so bp is added
                mUser.bloodPressure.removeBPByDate(mDate.getTime());
                mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
                mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);

                //dialog to change weight
                AlertDialog.Builder builder = new AlertDialog.Builder(WeightActivity.this);
                builder.setTitle("Tämän päivän paino on asetettu.")
                        .setMessage("Korvaa arvo?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUser.weight.removeWeightByDate(mDate.getTime());
                                mUser.weight.addWeightRecord(Double.parseDouble(weight), mDate);
                                updateUI();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (mUser.bloodPressure.isDateInBothLists(mDate)) {
                //List contains bp but not weight, so weight is added
                mUser.weight.removeWeightByDate(mDate.getTime());
                mUser.weight.addWeightRecord(Double.parseDouble(weight), mDate);

                //dialog to change bp
                AlertDialog.Builder builder = new AlertDialog.Builder(WeightActivity.this);
                builder.setTitle("Tämän päivän verenpaine on asetettu.")
                        .setMessage("Korvaa arvo?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUser.bloodPressure.removeBPByDate(mDate.getTime());
                                mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
                                mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
                                updateUI();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //add both
                mUser.weight.addWeightRecord(Double.parseDouble(weight), mDate);
                mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
                mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
            }
        } else if (!weight.isEmpty()) {
            if (mUser.weight.isDateInList(mDate)) {
                //dialog to change weight
                AlertDialog.Builder builder = new AlertDialog.Builder(WeightActivity.this);
                builder.setTitle("Tämän päivän paino on asetettu.")
                        .setMessage("Korvaa arvo?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUser.weight.removeWeightByDate(mDate.getTime());
                                mUser.weight.addWeightRecord(Double.parseDouble(weight), mDate);
                                updateUI();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //add weight
                mUser.weight.addWeightRecord(Double.parseDouble(weight), mDate);
            }
        } else if (!lowerBP.isEmpty() && !upperBP.isEmpty()) {
            if (mUser.bloodPressure.isDateInBothLists(mDate)) {
                //dialog to change bp
                AlertDialog.Builder builder = new AlertDialog.Builder(WeightActivity.this);
                builder.setTitle("Tämän päivän verenpaine on asetettu.")
                        .setMessage("Korvaa arvo?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mUser.bloodPressure.removeBPByDate(mDate.getTime());
                                mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
                                mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
                                updateUI();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //add bp
                mUser.bloodPressure.addLowerBPRecord(Integer.parseInt(lowerBP), mDate);
                mUser.bloodPressure.addUpperBPRecord(Integer.parseInt(upperBP), mDate);
            }
        }
        updateUI();
    }

    /**
     * Set input filters for edit text input and when typing, clickcing outside the keyboard closes the keyboard.
     */
    private void setupEditTexts() {
        EditText paino = findViewById(R.id.etWeight);
        EditText alaPaine = findViewById(R.id.etLowerBP);
        EditText ylaPaine = findViewById(R.id.etUpperBP);

        //Set the input filters
        paino.setFilters(new InputFilter[] { new InputFilterMinMax(0f, 999f)});
        alaPaine.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});
        ylaPaine.setFilters(new InputFilter[] { new InputFilterMinMax(1, 999)});

        //Set listeners for the edit texts on when the user clicks something else on the screen while typing
        //When clicked outside, the keyboard closes
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

    private void setupBtnListeners() {
        //Button to add value updates ui and closes keyboard
        findViewById(R.id.btnAddValue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddValues();
                updateUI();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });

        //Button to open blood pressure chart sets opens blood pressure chart activity
        findViewById(R.id.btnOpenBPChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bpChart = new Intent(WeightActivity.this, BPChartActivity.class);
                startActivity(bpChart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                mSettingsOrBPChartOpened = true;
            }
        });

        //Clicking date button opens date picker and closes keyboard
        findViewById(R.id.tvDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
                MainActivity.hideKeyboard(getApplicationContext(), v);
            }
        });
    }
}




