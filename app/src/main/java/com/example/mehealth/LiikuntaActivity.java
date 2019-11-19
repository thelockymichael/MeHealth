package com.example.mehealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LiikuntaActivity extends AppCompatActivity {
    private static final String TAG = "LiikuntaActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liikunta);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent koti = new Intent(LiikuntaActivity.this, MainActivity.class);
                        startActivity(koti.addFlags(koti.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
                        Intent paino = new Intent(LiikuntaActivity.this, PainoActivity.class);
                        startActivity(paino.addFlags(paino.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_local_drink:
                        Intent vesi = new Intent(LiikuntaActivity.this, VesiActivity.class);
                        startActivity(vesi.addFlags(vesi.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mieliala = new Intent(LiikuntaActivity.this, MielialaActivity.class);
                        startActivity(mieliala.addFlags(mieliala.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
    }
}
