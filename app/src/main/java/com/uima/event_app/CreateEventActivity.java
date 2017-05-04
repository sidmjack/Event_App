package com.uima.event_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.id.message;

public class CreateEventActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDetails;
    private CheckBox needVolunteers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        database = FirebaseDatabase.getInstance();

        eventName = (EditText) findViewById(R.id.create_event_name);
        eventLocation = (EditText) findViewById(R.id.create_event_location);
        eventDetails = (EditText) findViewById(R.id.create_event_details);

        Button createButton = (Button) findViewById(R.id.create_event);
        Button cancelButton = (Button) findViewById(R.id.cancel_event);

        createButton.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                writeToEventDB();
                Toast.makeText(getBaseContext(), "Event Created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Event Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void writeToEventDB() {
        Event e = new Event(eventName.getText().toString(), eventLocation.getText().toString(), eventDetails.getText().toString());

        // Write a message to the database
        myRef = database.getReference().child("events").push();

        myRef.setValue(e);
        String myKey = myRef.getKey();
    }

    private void readFromDB(String message) {
        // Read from the database
        myRef = database.getReference("events");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
