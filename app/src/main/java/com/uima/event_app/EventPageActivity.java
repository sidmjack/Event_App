package com.uima.event_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by edmundConnor on 5/5/17.
 */

public class EventPageActivity extends Activity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView eventName;
    private TextView eventDesc;
    private TextView eventLoc;
    private TextView eventDate;
    private TextView eventStartTime;
    private TextView eventEndTime;
    private Spinner eventTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        Intent intent = getIntent();
        final List<Event> currEvents = new ArrayList<Event>();
        final String eventkey = intent.getStringExtra("key");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(eventkey)) {
                        Event temp = child.getValue(Event.class);
                        currEvents.add(temp);
                        eventName = (TextView) findViewById(R.id.event_name);
                        eventDesc = (TextView) findViewById(R.id.event_desc);
                        eventLoc = (TextView) findViewById(R.id.event_location);
                        eventDate = (TextView) findViewById(R.id.event_date);
                        eventStartTime = (TextView) findViewById(R.id.event_start_time);
                        eventEndTime = (TextView) findViewById(R.id.event_end_time);
                        eventTypes = (Spinner) findViewById(R.id.event_attributes);

                        eventName.setText(temp.getName());
                        eventDesc.setText(temp.getDetails());
                        eventLoc.setText(temp.getLocation());
                        eventDate.setText(temp.getDate());
                        eventStartTime.setText(temp.getStart_time());
                        eventEndTime.setText(temp.getEnd_time());
                        //eventTypes.setText(currEvents.get(0).getDetails());
                        //List<String> tags = temp.getTags();
                        HashMap<String, String> tags = temp.getTags();
                        //ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tags);
                        //tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //eventTypes.setAdapter(tagAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
