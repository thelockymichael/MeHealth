package com.example.mehealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PainoActivity extends AppCompatActivity {
    private static final String TAG = "PainoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paino);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        final User user = (User)i.getSerializableExtra("user");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent koti = new Intent(PainoActivity.this, MainActivity.class);
                        koti.putExtra("user", user);
                        startActivity(koti.addFlags(koti.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
                        break;

                    case R.id.ic_local_drink:
                        Intent vesi = new Intent(PainoActivity.this, VesiActivity.class);
                        vesi.putExtra("user", user);
                        startActivity(vesi.addFlags(vesi.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent liikunta = new Intent(PainoActivity.this, LiikuntaActivity.class);
                        liikunta.putExtra("user", user);
                        startActivity(liikunta.addFlags(liikunta.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mieliala = new Intent(PainoActivity.this, MielialaActivity.class);
                        mieliala.putExtra("user", user);
                        startActivity(mieliala.addFlags(mieliala.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });

        TextView painoText = (TextView) findViewById(R.id.textViewPaino);
        TextView aliPaineText = (TextView) findViewById(R.id.textViewAliPaine);
        TextView yliPaineText = (TextView) findViewById(R.id.textViewYliPaine);


        painoText.setText("paino\n" + user.getWeightNow());
        aliPaineText.setText("aliP\n" + user.getAliPaineNow());
        yliPaineText.setText("yliP\n" + user.getYliPaineNow());
    }
}
