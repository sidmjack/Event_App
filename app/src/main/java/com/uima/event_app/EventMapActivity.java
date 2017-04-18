package com.uima.event_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_event_map);
    }

    /* Allows "back" navigation when back button is pressed.*/
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
