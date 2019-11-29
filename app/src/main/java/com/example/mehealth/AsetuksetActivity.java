package com.example.mehealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.gson.Gson;

public class AsetuksetActivity extends AppCompatActivity {
    private static final String TAG = "AsetuksetActivity";
    User user;
    Toolbar toolbar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MeHealth");

        sharedPref = getSharedPreferences("com.example.mehealth_preferences", Activity.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference.getKey().equals("buttonResetPainoHistoria")) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.mehealth_preferences", Activity.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPref.getString("user", "");
                User user = gson.fromJson(json, User.class);
                user.resetWeightHistory();

                SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
                json = gson.toJson(user);
                sharedPrefEditor.putString("user", json);
                sharedPrefEditor.commit();
                Toast.makeText(getContext().getApplicationContext(),"terve",Toast.LENGTH_SHORT).show();
            }
            return super.onPreferenceTreeClick(preference);
        }
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
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backArrow:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void addUserToSharedPref() {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        sharedPrefEditor.putString("user", json);
        sharedPrefEditor.commit();
    }


}