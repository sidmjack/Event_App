package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class EventSelectFragment extends Fragment {
    private ListView eventSelectListView;
    protected static List<Event> eventItems;
    protected static EventSelectAdapter esAdapter;
    protected View rootView;
    protected String type;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    protected List<Event> localEvents;

    public EventSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        type = getActivity().getTitle().toString();
        localEvents = new ArrayList<Event>();
        getAllEvents();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        esAdapter = new EventSelectAdapter(getActivity(), R.layout.event_select_row, localEvents);
        eventSelectListView.setAdapter(esAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_event_select, container, false);

        eventSelectListView = (ListView) rootView.findViewById(R.id.event_select_list_view);

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

    public void getAllEvents() {
        myRef = database.getReference("events");


        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children =  dataSnapshot.getChildren();

                for (DataSnapshot child:children) {
                    Event value = child.getValue(Event.class);
                    if (value.getType().equalsIgnoreCase(type)) {
                        Event temp = new Event(value.getId(), value.getName(), value.getHostOrg(), value.getLocation(), value.getDetails(), value.getNeedVolunteers(), value.getImgId(), value.getType(), value.getTags(), value.getStart_time(), value.getEnd_time(), value.getDate());
                        localEvents.add(temp);
                    }
                    //System.out.println(value.getName());
                    System.out.println("Size " + localEvents.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println(localEvents.size());
    }


}
