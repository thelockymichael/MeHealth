package com.mehealth.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.MoodValue;
import com.mehealth.User.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class MoodActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "MoodActivity";
    private User mUser;
    private SharedPref mPref;
    private Date mDate;
    private Boolean mSettingsOpened;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        mPref = new SharedPref(getApplicationContext());
        mDate = Calendar.getInstance().getTime();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Mieliala");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        MainActivity.menuIconHighlight(bottomNavigationView, 4);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent home = new Intent(MoodActivity.this, MainActivity.class);
                        startActivity(home.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_weight_scale:
                        Intent weight = new Intent(MoodActivity.this, WeightActivity.class);
                        startActivity(weight.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(MoodActivity.this, WaterActivity.class);
                        startActivity(water.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(MoodActivity.this, ExerciseActivity.class);
                        startActivity(exercise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        break;
                }
                return false;
            }
        });

        //Declares the ImageView and sets the first image to be the neutral smiley corresponding to 5
        final ImageView imageMieliala = findViewById(R.id.imageMieliala);
        imageMieliala.setImageResource(R.drawable.smiley5);
        //This TextView contains the current progress of the seekbar
        final TextView textMieliala = findViewById(R.id.textMieliala);
        //The default value of the seekbar is 5 when opening the activity, hence the textview is set to 5 at start
        textMieliala.setText("5");

        //Declares the seekbar and sets the listener for it
        final SeekBar seekBarMood = findViewById(R.id.seekbarMieliala);
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Depending on the progress of the seekbar, the smiley image changes color
                //There are smileys ranging from smiley0 being red to smiley10 being green'
                //smiley5 is the neutral yellow
                textMieliala.setText(String.format(Locale.getDefault(), "%d", progress));
                switch (progress) {
                    case 0:
                        imageMieliala.setImageResource(R.drawable.smiley0);
                        break;
                    case 1:
                        imageMieliala.setImageResource(R.drawable.smiley1);
                        break;
                    case 2:
                        imageMieliala.setImageResource(R.drawable.smiley2);
                        break;
                    case 3:
                        imageMieliala.setImageResource(R.drawable.smiley3);
                        break;
                    case 4:
                        imageMieliala.setImageResource(R.drawable.smiley4);
                        break;
                    case 5:
                        imageMieliala.setImageResource(R.drawable.smiley5);
                        break;
                    case 6:
                        imageMieliala.setImageResource(R.drawable.smiley6);
                        break;
                    case 7:
                        imageMieliala.setImageResource(R.drawable.smiley7);
                        break;
                    case 8:
                        imageMieliala.setImageResource(R.drawable.smiley8);
                        break;
                    case 9:
                        imageMieliala.setImageResource(R.drawable.smiley9);
                        break;
                    case 10:
                        imageMieliala.setImageResource(R.drawable.smiley10);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //These methods are not used but need to be declared
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //These methods are not used but need to be declared
            }
        });

        //Declares the listener for the button to save a mood state
        findViewById(R.id.buttonAddMood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Depending on the seekbar's progress, saves a mood state from 0-10 to the user objects mood history
                int progress = ((SeekBar)findViewById(R.id.seekbarMieliala)).getProgress();
                mUser.mood.addMoodRecord(progress, mDate);
                updateGraph();
                updateDateText();
            }
        });

        findViewById(R.id.tvDateMood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = mPref.getUser();
        mSettingsOpened = false;
        updateGraph();
        updateDateText();
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

    private void updateDateText() {
        TextView tvDateMood = findViewById(R.id.tvDateMood);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = day + "/" + (month + 1) + "/" + year;
        tvDateMood.setText(date);
    }

    private void updateGraph() {
        //Declare needed variables
        final DateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
        final LineChart chart = findViewById(R.id.moodGraph);
        List<Entry> moodEntries = new ArrayList<>();
        ArrayList<MoodValue> moodHistory = mUser.mood.getMoodHistory();


        //Sort the mood list by date
        Collections.sort(moodHistory, new Comparator<MoodValue>() {
            @Override
            public int compare(MoodValue o1, MoodValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });


        //Set entries to entry list from mood history list
        for (int i = 0; i < moodHistory.size(); i++) {
            //Get the current weight weight value and intialize the current variables
            MoodValue moodValue = moodHistory.get(i);
            Date date = moodValue.getDate();
            float mood = (float) moodValue.getMood();

            //Add the entries to the entry list
            moodEntries.add(new Entry(date.getTime(), mood));
        }

        //Setup XAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(moodHistory.size(), false);
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
        final LineDataSet moodDataSet = new LineDataSet(moodEntries, "Mieliala");

        //Customize chart
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.setMaxHighlightDistance(20);

        moodDataSet.setValueTextSize(10);
        moodDataSet.setColor(Color.YELLOW);
        moodDataSet.setFillAlpha(10);
        moodDataSet.setLineWidth(3f);
        moodDataSet.setCircleRadius(4);

        moodDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        });

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoodActivity.this);
                builder.setTitle("Poista arvo")
                        .setMessage("Oletko varma?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moodDataSet.removeEntry(e);
                                chart.invalidate();

                                mUser.mood.removeMoodByDate(e.getX());
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
        LineData moodLineData = new LineData(moodDataSet);
        chart.setData(moodLineData);
        chart.invalidate();
    }
}
