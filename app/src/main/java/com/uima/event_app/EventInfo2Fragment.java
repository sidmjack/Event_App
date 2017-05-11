package com.uima.event_app;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventInfo2Fragment extends Fragment {
    View rootView;
    private String eventID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    TextView hostOrg;
    TextView details;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        eventID = getArguments().getString("eventID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_info2, container, false);
        hostOrg = (TextView) rootView.findViewById(R.id.bottom_event_host_org);
        details = (TextView) rootView.findViewById(R.id.bottom_event_details);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        populateData();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }

    public EventInfo2Fragment() {
        // Required empty public constructor
    }

    public static EventInfo2Fragment newInstance() {
        EventInfo2Fragment f = new EventInfo2Fragment();
        return f;
    }

    /**
     *  Populate data for this fragment.
     *
     */
    private void populateData() {
        myRef.child("events").child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event thisEvent = dataSnapshot.getValue(Event.class);
                details.setText(thisEvent.getDetails());
                myRef.child("users").child(thisEvent.getHostOrg()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserProfile thisUser = dataSnapshot.getValue(UserProfile.class);
                        hostOrg.setText(thisUser.getOrganizer());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
