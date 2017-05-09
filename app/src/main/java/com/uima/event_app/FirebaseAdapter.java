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
import java.util.List;

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
    protected List<Event> localEvents;
    private List<Event> localCulture;
    private List<Event> socailActivism;
    private List<Event> popularCulture;
    private List<Event> communityOutreach;
    private List<Event> educationLearning;
    private List<Event> shoppingMarket;
    private List<Event> Miscellaneous;
    private UserProfile user;

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

    }

    public void getUserProfile() {
        
    }

    public void addEventToDB(Event event) {
        String name = event.getName();
        String date = event.getDate();
        String start_time = event.getStart_time();
        String end_time = event.getEnd_time();
        String location = event.getLocation(); //MAKE SURE ALL THESE AREN"T NULL
        String latitude = event.getLatutude();
        String longitude = event.getLongitude();
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


        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children =  dataSnapshot.getChildren();

                for (DataSnapshot child:children) {
                    Event value = child.getValue(Event.class);
                    String lat = "39";
                    String log = "-76";
                    if (value.getLatutude() != null) {
                        lat = value.getLatutude();
                    }
                    if (value.getLongitude() != null) {
                        lat = value.getLongitude();
                    }
                    Event temp = new Event(value.getId(), value.getName(), value.getHostOrg(), value.getLocation(), value.getDetails(), value.getNeedVolunteers(), value.getImgId(), value.getType(), value.getTags(), value.getStart_time(), value.getEnd_time(), value.getDate(), lat, log);

                    //System.out.println(value.getName());
                    localEvents.add(temp);
                    System.out.println("Size " + localEvents.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<Event> getLocalEvents() {
        return localEvents;
    }

    public void divideEvents() {
        getAllEvents();
        System.out.println("**** " + localEvents.size());
        for (int i = 0; i < localEvents.size(); i++) {
            String eventType = localEvents.get(i).getType();
            System.out.println(localEvents.get(i).getName());
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
            System.out.println(localEvents.get(i).getName());
        }
    }

    public List<Event> getLocalCulture() { return localCulture; }
    public List<Event> getPopularCulture() { return popularCulture; }
    public List<Event> getSocailActivism() { return socailActivism; }
    public List<Event> getCommunityOutreach() { return communityOutreach; }
    public List<Event> getEducationLearning() { return educationLearning; }
    public List<Event> getShoppingMarket() { return shoppingMarket; }
    public List<Event> getMiscellaneous() { return Miscellaneous; }

}
