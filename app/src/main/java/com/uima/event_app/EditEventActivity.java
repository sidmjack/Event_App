package com.uima.event_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

public class EditEventActivity extends CreateEventActivity {
    private String eventID, lat, log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Activity");

        Bundle extras = getIntent().getExtras();

        String eventNameStr = extras.getString("event name");
        String eventLocationStr = extras.getString("event location");
        String eventDetailsStr = extras.getString("event details");
        eventID = extras.getString("event id");
        lat = extras.getString("latitude");
        log = extras.getString("longitude");

        eventName.setText(eventNameStr);
        eventLocation.setText(eventLocationStr);
        eventDetails.setText(eventDetailsStr);

        Button orangeButton = (Button) findViewById(R.id.cancel_event);
        Button purpleButton = (Button) findViewById(R.id.create_event);

        orangeButton.setText("cancel");
        purpleButton.setText("update");

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEventDB();
                Toast.makeText(getBaseContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                // TODO: update the event in question
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle("Edit Activity");
    }

    private void updateEventDB() {
        String imgId = imgReference;

        final Calendar c = Calendar.getInstance();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventStartTime.getCurrentHour(), eventStartTime.getCurrentMinute());
        long start_time = c.getTimeInMillis();
        c.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth(), eventEndTime.getCurrentHour(), eventEndTime.getCurrentMinute());
        long end_time = c.getTimeInMillis();
        HashMap<String, String> tags = new HashMap<>();
        Event e = new Event(eventID, eventName.getText().toString(), user.getOrganizer(), eventLocation.getText().toString(), eventDetails.getText().toString(), needVolunteers.isChecked(), imgId, clickType, tags, start_time, end_time, lat, log);

        // Write a message to the database
        myRef = database.getReference().child("events").child(eventID);

        myRef.setValue(e);
        populateAttributeList(eventID);
    }
}
