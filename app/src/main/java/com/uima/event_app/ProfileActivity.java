package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences myPrefs;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Context context = getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void logOutTapped(View view) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putBoolean("logged_in", false);
        editor.apply();


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }

}

/*  Intent intent = new Intent(this, HomeActivity.class);
    intent.putExtra("finish", true);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
    startActivity(intent);
    finish();*/