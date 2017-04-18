package com.uima.event_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by edmundConnor on 4/18/17.
 */

public class SettingsActivity extends AppCompatActivity {

    Spinner notificationTimes;
    Spinner appThemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationTimes = (Spinner) findViewById(R.id.settings_times);
        appThemes = (Spinner) findViewById(R.id.settings_themes);

        final ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.notification_times, R.layout.spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(this,
                R.array.app_themes, R.layout.spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        notificationTimes.setAdapter(timeAdapter);
        appThemes.setAdapter(themeAdapter);
    }
}
