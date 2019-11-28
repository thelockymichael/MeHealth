package com.example.mehealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VesiActivity extends AppCompatActivity {
    private static final String TAG = "VesiActivity";
    User user;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vesi);
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        ImageButton button1dl = findViewById(R.id.button1dl);
        ImageButton button2dl = findViewById(R.id.button2dl);
        ImageButton button5dl = findViewById(R.id.button5dl);
        ImageButton button1l = findViewById(R.id.button1l);
        button1dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.juoVetta(1);
                paivitaVesi(user);
            }
        });
        button2dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.juoVetta(2);
            }
        });
        button5dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.juoVetta(5);
            }
        });
        button1l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.juoVetta(10);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref = getSharedPreferences("user", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("user", "");
        user = gson.fromJson(json, User.class);

        sharedPref = getSharedPreferences("oldDate", Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        String oldFormattedDate = sharedPref.getString("oldDate", "");

        Log.d(TAG, "old date: " + oldFormattedDate);
        Log.d(TAG, "date now: " + formattedDate);

        if (!formattedDate.equals(oldFormattedDate)) {
            Log.d(TAG, "date: reset vesi yo");
            user.vettaJuotuReset();
        }
        oldFormattedDate = formattedDate;
        editor.putString("oldDate", oldFormattedDate);
        editor.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.commit();
        finish();
    }

    public void paivitaVesi(User user) {
        TextView juotuMaara = findViewById(R.id.juotuMaara);
        juotuMaara.setText(user.getJuotuVesi() + "dl");
    }

}
