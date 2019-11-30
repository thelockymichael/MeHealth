package com.example.mehealth;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPref {
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor sharedPrefEditor;
    private Gson gson;
    private Context context;

    public SharedPref(Context context) {
        this.context = context;
        this.sharedPref = this.context.getSharedPreferences("com.example.mehealth_preferences", Activity.MODE_PRIVATE);
        this.sharedPrefEditor = this.sharedPref.edit();
        this.gson = new Gson();
    }

    public User getUser() {
        String json = sharedPref.getString("user", "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void saveUser(User user) {
        String json = gson.toJson(user);
        sharedPrefEditor.putString("user", json);
        sharedPrefEditor.commit();
    }

}