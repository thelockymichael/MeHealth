package com.example.mehealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    User user;
    User emptyUser;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("com.example.mehealth_preferences", Activity.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();
        emptyUser = new User();
        Gson gson = new Gson();
        String defUserJson = gson.toJson(emptyUser);
        String userJson = sharedPref.getString("user", defUserJson);
        sharedPrefEditor.putString("user", userJson);
        sharedPrefEditor.commit();

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
                Intent settings = new Intent(this, AsetuksetActivity.class);
                startActivity(settings);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);

        //Asettaa nykyisen välilehden ikonin valituksi
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //Tarkistaa jos jotain alavalikon ikonia painetaan
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        //Nykyisen välilehden ikonin klikkaaminen ei tee mitään
                        break;
                    case R.id.ic_attach_money:
                        //Riippuen ikonista uusi intent ohjautuu oikeaan activityyn
                        Intent weight = new Intent(MainActivity.this, PainoActivity.class);
                        startActivity(weight.addFlags(weight.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent water = new Intent(MainActivity.this, VesiActivity.class);
                        startActivity(water.addFlags(water.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent exercise = new Intent(MainActivity.this, LiikuntaActivity.class);
                        startActivity(exercise.addFlags(exercise.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mood = new Intent(MainActivity.this, MielialaActivity.class);
                        startActivity(mood.addFlags(mood.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref = getSharedPreferences("com.example.mehealth_preferences", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("user", "");
        user = gson.fromJson(json, User.class);
        Log.d(TAG, "onResume: juotu " + user.getWaterDrankToday());

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("H");
        String formattedDate = df.format(date);

        int time = Integer.parseInt(formattedDate);
        String hello;
        if (time >= 21 || time <= 3) hello = "Öitä";
        else if (time <= 10) hello = "Huomenta";
        else if (time <= 17) hello = "Päivää";
        else hello = "Iltaa";

        String name = sharedPref.getString("name", "perkele");

        TextView textHello = findViewById(R.id.textHello);
        TextView textWaterDrankToday = findViewById(R.id.textWaterDrankToday);
        textHello.setText(hello + "\n" + name);
        textWaterDrankToday.setText("Tänään juotu " + user.getWaterDrankToday() + "dl");

        ((TextView)findViewById(R.id.textMoodNow)).setText("Viimeisin mielialasi oli\n" + user.getLatestMoodRecord());

    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPrefEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        sharedPrefEditor.putString("user", json);
        sharedPrefEditor.commit();
    }

}

