package com.uima.event_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.support.v7.appcompat.R.styleable.View;

public class CreateEventActivity extends AppCompatActivity {

    Button orangeButton;
    Button purpleButton;

    EditText eventName;
    EditText eventLocation;
    EditText eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setTitle("Create Activity");

        orangeButton = (Button) findViewById(R.id.create_event);
        purpleButton = (Button) findViewById(R.id.cancel_event);
        eventName = (EditText) findViewById(R.id.create_event_name);
        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventDetails = (EditText) findViewById(R.id.create_event_details);

        Bundle extras = getIntent().getExtras();

        if (extras.getBoolean("duplicate", false)) {
            String eventNameStr = extras.getString("event name");
            String eventLocationStr = extras.getString("event location");
            String eventDetailsStr = extras.getString("event details");

            eventName.setText(eventNameStr);
            eventLocation.setText(eventLocationStr);
            eventDetails.setText(eventDetailsStr);
        }

        orangeButton.setText("create");
        purpleButton.setText("cancel");

        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Event Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Create Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle("Create Activity");
    }




}
