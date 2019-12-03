package com.example.mehealth.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.mehealth.R;
import com.example.mehealth.SharedPref;
import com.example.mehealth.User.User;

/**
 * Settings activity containing the preferences fragment.
 * User can set basic values such as their name and reset the values collected by the app thus far.
 */
public class SettingsActivity extends AppCompatActivity {
    //private static final String TAG = "SettingsActivity";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Default settings for creating a settings activity
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

        //Sets the toolbar for the activity
        toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MeHealth");
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        /**
         * Listens for clicks on the preferences.
         * If a preference given preference based on their id is clicked, executes the code below.
         * @param preference    Name of the preference clicked
         * @return
         */
        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference.getKey().equals("buttonResetWeight")) {
                //Gets user class from shared preferences and calls the method to reset the weight history.
                SharedPref pref = new SharedPref(getContext());
                User user = pref.getUser();
                user.weight.clear();
                pref.saveUser(user);
            } else if (preference.getKey().equals("buttonResetEverything")) {
                //Gets the user class from shared preferences and calls the method to reset everything.
                SharedPref pref = new SharedPref(getContext());
                User user = pref.getUser();
                user.resetEverything();
                pref.saveUser(user);
            }
            return super.onPreferenceTreeClick(preference);
        }
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
        //Replaces the normal settings icon in the toolbar with a backarrow that opens the main activity
        if (item.getItemId() == R.id.backArrow) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }
}