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
    private ArrayList<Event> localCulture;
    private ArrayList<Event> socailActivism;
    private ArrayList<Event> popularCulture;
    private ArrayList<Event> communityOutreach;
    private ArrayList<Event> educationLearning;
    private ArrayList<Event> shoppingMarket;
    private ArrayList<Event> Miscellaneous;

    public FirebaseAdapter(Context ctx) {
        context = ctx;
        database = FirebaseDatabase.getInstance();
        localEvents = new ArrayList<Event>();
        localCulture = new ArrayList<Event>();
        socailActivism = new ArrayList<Event>();
        popularCulture = new ArrayList<Event>();
        communityOutreach = new ArrayList<Event>();
        educationLearning = new ArrayList<Event>();
        shoppingMarket = new ArrayList<Event>();
        Miscellaneous = new ArrayList<Event>();
        getAllEvents();
        divideEvents();
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

    public void divideEvents() {
        for (int i = 0; i < localEvents.size(); i++) {
            String eventType = localEvents.get(i).getType();
            if (eventType.contains("Local Culture")) {
                localCulture.add(localEvents.get(i));
            } else if (eventType.contains("Social Activism")) {
                socailActivism.add(localEvents.get(i));
            } else if (eventType.contains("Popular")) {
                popularCulture.add(localEvents.get(i));
            } else if (eventType.contains("Community")) {
                communityOutreach.add(localEvents.get(i));
            } else if (eventType.contains("Education")) {
                educationLearning.add(localEvents.get(i));
            } else if (eventType.contains("Shopping")) {
                shoppingMarket.add(localEvents.get(i));
            } else {
                Miscellaneous.add(localEvents.get(i));
            }
        }
    }

    public ArrayList<Event> getLocalCulture() { return localCulture; }
    public ArrayList<Event> getPopularCulture() { return popularCulture; }
    public ArrayList<Event> getSocailActivism() { return socailActivism; }
    public ArrayList<Event> getCommunityOutreach() { return communityOutreach; }
    public ArrayList<Event> getEducationLearning() { return educationLearning; }
    public ArrayList<Event> getShoppingMarket() { return shoppingMarket; }
    public ArrayList<Event> getMiscellaneous() { return Miscellaneous; }

}
