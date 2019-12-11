package com.mehealth.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
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

/**
 * @author Amin Karaoui
 */
public class BPChartActivity extends AppCompatActivity {

    private static final String TAG = "BPChartActivity";
    private User mUser;
    private SharedPref mPref;
    private Boolean mSettingsOpenedOrOrientationChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpchart);
        mPref = new SharedPref(getApplicationContext());

        //Sets up the top toolbar
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Verenpaine");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Set setting and orientation checker to false and get latest user info
        mSettingsOpenedOrOrientationChanged = false;
        mUser = mPref.getUser();
        updateChart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPref.saveUser(mUser);
        if (!mSettingsOpenedOrOrientationChanged) {
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
            mSettingsOpenedOrOrientationChanged = true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Runs when orientation changes
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Activity would finish when rotation changes if this boolean wasn't set here
        mSettingsOpenedOrOrientationChanged = hasFocus;
    }

    private void updateChart() {
        //Declare the date formatter
        final DateFormat dateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());

        //Get the blood pressure lists from user object
        final ArrayList<BloodPressureValue> lowerBPHistory = mUser.bloodPressure.getLowerBPHistory();
        final ArrayList<BloodPressureValue> upperBPHistory = mUser.bloodPressure.getUpperBPHistory();

        //Declare the chart and the entry lists
        final LineChart chart = findViewById(R.id.chartBloodPressure);
        List<Entry> lowerBPEntries = new ArrayList<>();
        List<Entry> upperBPEntries = new ArrayList<>();


        //Sort the lower BP list by date
        Collections.sort(lowerBPHistory, new Comparator<BloodPressureValue>() {
            @Override
            public int compare(BloodPressureValue o1, BloodPressureValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        } );

        //Sort the upper BP list by date
        Collections.sort(upperBPHistory, new Comparator<BloodPressureValue>() {
            @Override
            public int compare(BloodPressureValue o1, BloodPressureValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        } );

        //Convert the blood pressure lists into entry lists
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

        //Setup the X Axis
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

        //Deletes the right Y axis so only the left Y axis is left
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);


        //Create linedata from the entries list
        final LineDataSet upperBPDataSet = new LineDataSet(upperBPEntries, "Yläpaine");
        final LineDataSet lowerBPDataSet = new LineDataSet(lowerBPEntries, "Alapaine");

        //Customize chart
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        chart.setMaxHighlightDistance(20);

        //Customize the upper BP dataset
        upperBPDataSet.setValueTextSize(10);
        upperBPDataSet.setColor(Color.RED);
        upperBPDataSet.setFillAlpha(10);
        upperBPDataSet.setLineWidth(3f);
        upperBPDataSet.setCircleRadius(4);

        //Customize the lower BP dataset
        lowerBPDataSet.setValueTextSize(10);
        lowerBPDataSet.setColor(Color.GREEN);
        lowerBPDataSet.setFillAlpha(10);
        lowerBPDataSet.setLineWidth(3f);
        lowerBPDataSet.setCircleRadius(4);

        //Shows the values as integers rather than floats
        upperBPDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        });


        //Shows the values as integers rather than floats
        lowerBPDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        });

        //Opens dialog to delete the value when user clicks a value on the chart
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, Highlight h) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BPChartActivity.this);
                builder.setTitle("Poista tämän päivän arvot")
                        .setMessage("Oletko varma?")
                        .setNegativeButton("Peruuta", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Gets x value of the entry clicked
                                float x = e.getX();

                                //Removes the entry from both datasets based on the date
                                lowerBPDataSet.removeEntryByXValue(x);
                                upperBPDataSet.removeEntryByXValue(x);

                                //Refreshes chart
                                chart.invalidate();

                                //Deletes the value from the user object
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

        //Make one linedata from the blood pressure values
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lowerBPDataSet);
        dataSets.add(upperBPDataSet);
        LineData allData = new LineData(dataSets);

        //Set the linedate to the chart and update it
        chart.setData(allData);
        chart.invalidate();
    }
}
