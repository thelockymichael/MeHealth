package com.mehealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mehealth.R;
import com.mehealth.SharedPref;
import com.mehealth.User.BloodPressureValue;
import com.mehealth.User.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BPChartActivity extends AppCompatActivity {

    private static final String TAG = "BPChartActivity";
    private User mUser;
    private SharedPref mPref;
    private Boolean settingsOrDetailsOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpchart);

        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Verenpaine");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPref = new SharedPref(getApplicationContext());
        mUser = mPref.getUser();
        settingsOrDetailsOpened = false;
        updateChart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!settingsOrDetailsOpened ) {
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        settingsOrDetailsOpened = hasFocus;
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

    private void updateChart() {
        final DateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
        final LineChart chart = findViewById(R.id.bloodPressureChart);

        final ArrayList<BloodPressureValue> lowerBPHistory = mUser.bloodPressure.getLowerBPHistory();
        final ArrayList<BloodPressureValue> upperBPHistory = mUser.bloodPressure.getUpperBPHistory();
        List<Entry> lowerBPEntries = new ArrayList<>();
        List<Entry> upperBPEntries = new ArrayList<>();


        Collections.sort(lowerBPHistory, new Comparator<BloodPressureValue>() {
            @Override
            public int compare(BloodPressureValue o1, BloodPressureValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        } );

        Collections.sort(upperBPHistory, new Comparator<BloodPressureValue>() {
            @Override
            public int compare(BloodPressureValue o1, BloodPressureValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        } );

        //Set entries to blood pressure lists from
        for (int i = 0; i < lowerBPHistory.size(); i++) {
            BloodPressureValue lowerBPValue = lowerBPHistory.get(i);
            BloodPressureValue upperBPValue = upperBPHistory.get(i);
            Date lowerBPDate = lowerBPValue.getDate();
            Date upperBPDate = upperBPValue.getDate();

            float lowerBP = (float) lowerBPValue.getBloodPressure();
            float upperBP = (float) upperBPValue.getBloodPressure();

            lowerBPEntries.add(new Entry(lowerBPDate.getTime(), lowerBP));
            upperBPEntries.add(new Entry(upperBPDate.getTime(), upperBP));
        }

        //Setup XAxis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(lowerBPHistory.size(), false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return dateFormat.format(value);
            }
        });

        //Setups YAxis
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);

        YAxis leftYAxis = chart.getAxisLeft();


        //Create linedata from the entries list

        final LineDataSet upperBPDataSet = new LineDataSet(upperBPEntries, "Yläpaine");
        final LineDataSet lowerBPDataSet = new LineDataSet(lowerBPEntries, "Alapaine");

        //Customize chart
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.setMaxHighlightDistance(20);

        upperBPDataSet.setValueTextSize(10);
        upperBPDataSet.setColor(Color.RED);
        upperBPDataSet.setFillAlpha(10);
        upperBPDataSet.setLineWidth(3f);
        upperBPDataSet.setCircleRadius(4);

        lowerBPDataSet.setValueTextSize(10);
        lowerBPDataSet.setColor(Color.GREEN);
        lowerBPDataSet.setFillAlpha(10);
        lowerBPDataSet.setLineWidth(3f);
        lowerBPDataSet.setCircleRadius(4);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BPChartActivity.this);
                builder.setTitle("Poista tämän päivän arvot?")
                        .setMessage("Oletko varma?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float x = e.getX();
                                lowerBPDataSet.removeEntryByXValue(x);
                                upperBPDataSet.removeEntryByXValue(x);
                                chart.invalidate();

                                mUser.bloodPressure.removeBPByDate(x);
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
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lowerBPDataSet);
        dataSets.add(upperBPDataSet);
        LineData allData = new LineData(dataSets);

        //chart.setData(weightLineData);
        chart.setData(allData);
        chart.invalidate();
    }
}
