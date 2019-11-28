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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class PainoActivity extends AppCompatActivity {
    private static final String TAG = "PainoActivity";
    User user;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paino);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");

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
                        startActivity(koti.addFlags(koti.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_attach_money:
                        break;

                    case R.id.ic_local_drink:
                        Intent vesi = new Intent(PainoActivity.this, VesiActivity.class);
                        startActivity(vesi.addFlags(vesi.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_directions_run:
                        Intent liikunta = new Intent(PainoActivity.this, LiikuntaActivity.class);
                        startActivity(liikunta.addFlags(liikunta.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.ic_insert_emoticon:
                        Intent mieliala = new Intent(PainoActivity.this, MielialaActivity.class);
                        startActivity(mieliala.addFlags(mieliala.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }
                return false;
            }
        });
        Button buttonLisaaArvo = findViewById(R.id.buttonLisaaArvo);
        buttonLisaaArvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLisaaArvoPaino(v);
                paivitaTextit(user);
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
        paivitaTextit(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        addUserToSharedPref();
        finish();
    }

    private void paivitaTextit(User user) {
        TextView painoText = findViewById(R.id.textViewPaino);
        TextView alaPaineText = findViewById(R.id.textViewAlaPaine);
        TextView ylaPaineText = findViewById(R.id.textViewYlaPaine);

        painoText.setText("paino\n" + user.getWeightNow());
        alaPaineText.setText("alaP\n" + user.getAlaPaineNow());
        ylaPaineText.setText("ylaP\n" + user.getYlaPaineNow());
    }
    public void buttonLisaaArvoPaino(View view) {
        EditText painoEditText = findViewById(R.id.editTextPaino);
        EditText alaPaineEditText = findViewById(R.id.editTextAlaPaine);
        EditText ylaPaineEditText = findViewById(R.id.editTextYlaPaine);

        String painoText = painoEditText.getText().toString();
        String alaPaineText = alaPaineEditText.getText().toString();
        String ylaPaineText = ylaPaineEditText.getText().toString();

        user.addWeightRecord(Integer.parseInt(painoText));
        user.addAlapaineRecord(Integer.parseInt(alaPaineText));
        user.addYlaPaineRecord(Integer.parseInt(ylaPaineText));
    }

    protected void addUserToSharedPref() {
        editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.commit();
    }
}
