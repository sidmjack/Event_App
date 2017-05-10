package com.uima.event_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

        orangeButton.setText("update");
        purpleButton.setText("cancel");

        orangeButton.setOnClickListener(new View.OnClickListener() {
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
        String start_time = eventStartTime.getCurrentHour() + ":" + eventStartTime.getCurrentMinute();
        String end_time = eventEndTime.getCurrentHour() + ":" + eventEndTime.getCurrentMinute();
        String imgId = "22"; //eventImage.getId() + "";
        String event_date = eventDate.getMonth() + "/" + eventDate.getDayOfMonth() + "/" + eventDate.getYear();
        Event e = new Event(eventID, eventName.getText().toString(), user.getOrganizer(), eventLocation.getText().toString(), eventDetails.getText().toString(), needVolunteers.isChecked(), imgId, clickType, attributeItems, start_time, end_time, event_date, lat, log);

        // Write a message to the database
        myRef = database.getReference().child("events").child(eventID);

        myRef.setValue(e);
    }
}
