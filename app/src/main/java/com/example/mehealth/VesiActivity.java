package com.example.mehealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VesiActivity extends AppCompatActivity {
    private static final String TAG = "VesiActivity";

    User user;
    Toolbar toolbar;
    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vesi);

        pref = new SharedPref(getApplicationContext());
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MeHealth");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent asetukset = new Intent(this, AsetuksetActivity.class);
                startActivity(asetukset);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Initialize the bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent koti = new Intent(VesiActivity.this, MainActivity.class);
                        startActivity(koti.addFlags(koti.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
                        Intent paino = new Intent(VesiActivity.this, PainoActivity.class);
                        startActivity(paino.addFlags(paino.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        break;

                    case R.id.ic_directions_run:
                        Intent liikunta = new Intent(VesiActivity.this, LiikuntaActivity.class);
                        startActivity(liikunta.addFlags(liikunta.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mieliala = new Intent(VesiActivity.this, MielialaActivity.class);
                        startActivity(mieliala.addFlags(mieliala.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });

        //Set the onClickListeners for the imagebuttons to add water drank throughout the day
        ImageButton button1dl = findViewById(R.id.button1dl);
        ImageButton button2dl = findViewById(R.id.button2dl);
        ImageButton button5dl = findViewById(R.id.button5dl);
        ImageButton button1l = findViewById(R.id.button1l);
        button1dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.drinkWater(1);
                paivitaVesi(user);
            }
        });
        button2dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.drinkWater(2);
                paivitaVesi(user);
            }
        });
        button5dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.drinkWater(5);
                paivitaVesi(user);
            }
        });
        button1l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.drinkWater(10);
                paivitaVesi(user);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //User shared preferences
        user = pref.getUser();

        //Get the current date
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        //Get the previous date from shared preferences
        String oldFormattedDate = pref.getString("oldDate");

        //Logcat to check the date in memory vs the current date
        Log.d(TAG, "old date: " + oldFormattedDate);
        Log.d(TAG, "date now: " + formattedDate);

        if (!formattedDate.equals(oldFormattedDate)) {
            Log.d(TAG, "date: reset vesi yo");
            user.waterDrankTodayReset();
        }
        oldFormattedDate = formattedDate;
        pref.putString("oldDate", oldFormattedDate);
        paivitaVesi(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref.saveUser(user);
        finish();
    }

    protected void paivitaVesi(User user) {
        TextView juotuMaara = findViewById(R.id.juotuMaara);
        juotuMaara.setText(user.getWaterDrankToday() + "dl");
        Log.d(TAG, "paivitaVesi: juotu" + user.getWaterDrankToday());
        pref.saveUser(user);
    }

}
