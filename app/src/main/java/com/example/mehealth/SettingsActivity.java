package com.example.mehealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    Toolbar toolbar;

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
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference.getKey().equals("buttonResetWeight")) {
                SharedPref pref = new SharedPref(getContext());
                User user = pref.getUser();
                user.resetWeightHistory();
                pref.saveUser(user);
            } else if (preference.getKey().equals("buttonResetEverything")) {
                SharedPref pref = new SharedPref(getContext());
                User user = pref.getUser();
                user.resetEverything();
                pref.saveUser(user);
            }
            return super.onPreferenceTreeClick(preference);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}