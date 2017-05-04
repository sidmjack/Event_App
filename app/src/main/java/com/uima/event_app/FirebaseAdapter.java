package com.uima.event_app;

import android.content.Context;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.support.design.R.id.snap;

/**
 * Created by econno14 on 5/3/17.
 */

public class FirebaseAdapter {
    private final Context context;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String orgID;
    private ArrayList<Event> localEvents;

    public FirebaseAdapter(Context ctx) {
        context = ctx;
        database = FirebaseDatabase.getInstance();
        localEvents = new ArrayList<Event>();
    }

    public void addEventToDB(Event event) {
        String name = event.getName();
        String addr = event.getAddress();
        String date = event.getDate();
        String start_time = event.getStart_time();
        String end_time = event.getEnd_time();
        String location = event.getLocation(); //MAKE SURE ALL THESE AREN"T NULL
        orgID = event.getHostOrg();
        // Write a message to the database
        myRef = database.getReference().child("events").push();
        myRef.setValue(event);
        String myKey = myRef.getKey();

    }

    public void getOrganizationEvents() {
        myRef = database.getReference("events");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                myRef.equalTo("organization", orgID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        myRef.addValueEventListener(postListener);
    }

    public void getAllEvents() {
        myRef = database.getReference("events");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event event = dataSnapshot.getValue(Event.class);
                localEvents.add(event);
                Log.i(TAG,"add event name = " + event.getName());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });
    }
}
