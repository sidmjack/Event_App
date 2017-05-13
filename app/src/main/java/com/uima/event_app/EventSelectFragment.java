package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventSelectFragment extends Fragment {
    private ListView eventSelectListView;
    protected static List<Event> eventItems;
    protected static EventSelectAdapter esAdapter;
    protected View rootView;
    protected String type;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> keys = new ArrayList<String>();

    private FirebaseAuth mAuth;

    public EventSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        SharedPreferences myPrefs = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        type = myPrefs.getString("TYPE", "Miscellaneous");

        this.populateList();

        /*mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();*/

        // Uncertain...
        DatabaseReference userRef = myRef.child("users");
        //ArrayList<String> favoritedEvents = new ArrayList<String>();
        //userRef.child(userID).child("favorites").setValue(favoritedEvents);


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.populateList();
        getActivity().setTitle(type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_event_select, container, false);
        getActivity().setTitle(type);

        eventSelectListView = (ListView) rootView.findViewById(R.id.listEventSelect);

        eventSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectEvent = (Event) eventSelectListView.getItemAtPosition(position);
                Bundle data = new Bundle();
                data.putString("eventID", selectEvent.getId());
                data.putString("eventTitle", selectEvent.getName());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment currentFragment = new EventPageFragment();
                currentFragment.setArguments(data);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, currentFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

    private void populateList() {
        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> localEvents = new ArrayList<Event>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);
                    System.out.println(value.getType() + " " + type);
                    if (value.getType().equalsIgnoreCase(type)) {
                        keys.add(child.getKey());
                        localEvents.add(value);
                    }
                }

                esAdapter = new EventSelectAdapter(getActivity(), R.layout.event_select_row, localEvents);
                eventSelectListView.setAdapter(esAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
