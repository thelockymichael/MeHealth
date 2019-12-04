package com.mehealth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.mehealth.User.User;
import com.google.gson.Gson;

/**
 * Class to manage the shared preferences.
 * Makes transferring the user class around activities easy.
 */
public class SharedPref {
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor sharedPrefEditor;
    private Gson gson;

    public SharedPref(Context context) {
        sharedPref = context.getSharedPreferences("com.mehealth_preferences", Activity.MODE_PRIVATE);
        sharedPrefEditor = this.sharedPref.edit();
        sharedPrefEditor.apply();
        gson = new Gson();
    }

    /**
     * Gets user from shared preferences.
     * Returns a default value user if there is no user in shared preferences.
     * @return
     */
    public User getUser() {
        User defaultUser = new User();
        String defaultUserJson = gson.toJson(defaultUser);

        String userJson = sharedPref.getString("user", defaultUserJson);
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    /**
     * Saves user into shared preferences.
     * @param user  User class in use
     */
    public void saveUser(User user) {
        String json = gson.toJson(user);
        sharedPrefEditor.putString("user", json);
        sharedPrefEditor.commit();
    }

    /**
     * Gets a string from shared preferences with default value set empty.
     * @param key   Key for shared preferences
     * @return      Returns the string corresponding to the key
     */
    public String getString(String key) {
        String string = sharedPref.getString(key, "");
        return string;
    }

    /**
     * Variation of getString where user decides the default value.
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        String string = sharedPref.getString(key, defaultValue);
        return string;
    }

    /**
     * Puts string into shared preferences.
     * @param key   Key for the text
     * @param text  Text to save
     */
    public void putString(String key, String text) {
        sharedPrefEditor.putString(key, text);
        sharedPrefEditor.commit();
    }

}