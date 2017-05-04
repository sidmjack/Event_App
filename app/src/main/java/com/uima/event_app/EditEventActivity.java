package com.uima.event_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditEventActivity extends CreateEventActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_event);
        setTitle("Edit Activity");

        Bundle extras = getIntent().getExtras();

        String eventNameStr = extras.getString("event name");
        String eventLocationStr = extras.getString("event location");
        String eventDetailsStr = extras.getString("event details");

        eventName.setText(eventNameStr);
        eventLocation.setText(eventLocationStr);
        eventDetails.setText(eventDetailsStr);

        orangeButton.setText("update");
        purpleButton.setText("cancel");

        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Edit Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle("Edit Activity");
    }
}
