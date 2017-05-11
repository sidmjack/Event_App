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
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        eventName = (TextView) findViewById(R.id.event_name);
        eventDesc = (TextView) findViewById(R.id.event_desc);
        eventLoc = (TextView) findViewById(R.id.event_location);
        eventDate = (TextView) findViewById(R.id.event_date);
        eventStartTime = (TextView) findViewById(R.id.event_start_time);
        eventEndTime = (TextView) findViewById(R.id.event_end_time);
        eventTypes = (Spinner) findViewById(R.id.event_attributes);

        eventName.setText(currEvents.get(0).getName());
        eventDesc.setText(currEvents.get(0).getDetails());
        eventLoc.setText(currEvents.get(0).getLocation());
        eventDate.setText(currEvents.get(0).getDate());
        eventStartTime.setText(currEvents.get(0).getStart_time());
        eventEndTime.setText(currEvents.get(0).getEnd_time());
        //eventTypes.setText(currEvents.get(0).getDetails());
        HashMap<String, String> tags = currEvents.get(0).getTags();
        //ArrayList<String> eventTags = currEvents.get(0).getEventTags();
        //ArrayList<String> eventTags = getEventTags(tags);
       // ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventTags);
        // tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //eventTypes.setAdapter(tagAdapter);
    }

    /*public ArrayList<String> getEventTags(HashMap<String, String> tags) {
        ArrayList<String> eventTags = new ArrayList<>();
        for (String key : tags.keySet()) {
            eventTags.add(tags.get(key));
        }
        return eventTags;
    }*/
}
