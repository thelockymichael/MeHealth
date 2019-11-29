package com.example.mehealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class MielialaActivity extends AppCompatActivity {
    private static final String TAG = "MielialaActivity";
    User user;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mieliala);
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MeHealth");

        final ImageView imageMieliala = findViewById(R.id.imageMieliala);
        final TextView textMieliala = findViewById(R.id.textMieliala);
        textMieliala.setText("5");
        imageMieliala.setImageResource(R.drawable.smiley10);

        ((SeekBar)findViewById(R.id.seekbarMieliala)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMieliala.setText(Integer.toString(progress));
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
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"terve",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String msg=" ";
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent koti = new Intent(MielialaActivity.this, MainActivity.class);
                        startActivity(koti.addFlags(koti.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
                        Intent paino = new Intent(MielialaActivity.this, PainoActivity.class);
                        startActivity(paino.addFlags(paino.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent vesi = new Intent(MielialaActivity.this, VesiActivity.class);
                        startActivity(vesi.addFlags(vesi.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent liikunta = new Intent(MielialaActivity.this, LiikuntaActivity.class);
                        startActivity(liikunta.addFlags(liikunta.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
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
}
