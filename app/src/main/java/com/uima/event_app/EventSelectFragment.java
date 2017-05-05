package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


    public EventSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        type = getActivity().getTitle().toString();
        final List<Event> localEvents = new ArrayList<Event>();

        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);
                    localEvents.add(value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<Event> eventAdapter = new ArrayAdapter<Event>(getActivity(), android.R.layout.simple_list_item_1, localEvents);
        //esAdapter = new EventSelectAdapter(getActivity(), R.layout.event_select_row, localEvents);
        setListAdapter(eventAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
/*
        esAdapter = new EventSelectAdapter(getActivity(), R.layout.event_select_row, eventItems);
        eventSelectListView.setAdapter(esAdapter);
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_event_select, container, false);

        eventSelectListView = (ListView) rootView.findViewById(R.id.list);

        eventSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) eventSelectListView.getItemAtPosition(position);

                //EventPageFragment eventPageFragment = new EventPageFragment();
                EventPageFragment eventPageFragment = new EventPageFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.content_frame, eventPageFragment)
                        .replace(R.id.content_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
                String eventName = selectedEvent.getName();
                getActivity().setTitle(eventName);
            }
        });

        return rootView;
    }

}
