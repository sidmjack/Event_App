package com.uima.event_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventSelectFragment extends ListFragment {
    private ListView eventSelectListView;
    protected static List<Event> eventItems;
    protected static EventSelectAdapter esAdapter;
    protected View rootView;
    protected String type;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> keys = new ArrayList<String>();


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

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.populateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_event_select, container, false);

        eventSelectListView = (ListView) rootView.findViewById(R.id.list);

        eventSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EventPageActivity.class);
                Event selectEvent = (Event) eventSelectListView.getItemAtPosition(position);
                String currKey = keys.get(position);
                intent.putExtra("key", currKey);
                Toast.makeText(getActivity(), "I'm Clickable!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
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
                setListAdapter(esAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
