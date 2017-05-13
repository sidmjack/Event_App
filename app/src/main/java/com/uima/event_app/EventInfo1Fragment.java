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

import java.util.ArrayList;
import java.util.List;

public class EventInfo1Fragment extends Fragment {
    View rootView;
    private String eventID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    TextView date;
    TextView time;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        eventID = getArguments().getString("eventID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_info1, container, false);
        date = (TextView) rootView.findViewById(R.id.bottom_event_board_event_date);
        time = (TextView) rootView.findViewById(R.id.bottom_event_board_event_time);
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

    public EventInfo1Fragment() {
        // Required empty public constructor
    }

    public static EventInfo1Fragment newInstance() {
        EventInfo1Fragment f = new EventInfo1Fragment();
        return f;
    }

    private void populateData() {
        myRef.child("events").child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event thisEvent = dataSnapshot.getValue(Event.class);
                date.setText("Date: " + thisEvent.getDateString());
                time.setText("Time: " + thisEvent.getStartTimeString() + " - " + thisEvent.getEndTimeString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
