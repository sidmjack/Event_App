package com.uima.event_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventsFragment extends ListFragment {
    protected static List<Event> eventItems;
    protected static ManageEventsAdapter adapter;
    protected  ListView eventListView;
    private FragmentTabHost host;
    private FirebaseDatabase database;
    private FirebaseAuth myAuth;
    private DatabaseReference myRef;
    protected View rootView;

    private UserProfile user;
    private ListView myEventsView;

    public ManageEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("events");

        initializeUser();

        final List<Event> myEvents = new ArrayList<Event>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);
                    if (value.getHostOrg().equals(user.getOrganizer())) {
                        myEvents.add(value);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ManageEventsAdapter(getActivity(), R.layout.event_select_row, myEvents);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_select, container, false);

        myEventsView = (ListView) rootView.findViewById(R.id.list);

        myEventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) myEventsView.getItemAtPosition(position);

                //EventPageFragment eventPageFragment = new EventPageFragment();
                ManageEventsFragment manageEventFragment = new ManageEventsFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.content_frame, eventPageFragment)
                        .replace(R.id.content_frame, manageEventFragment)
                        .addToBackStack(null)
                        .commit();
                String eventName = selectedEvent.getName();
                getActivity().setTitle(eventName);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        host = null;
    }

    public void onEditClick(View view) {
        Intent intent = new Intent(getActivity(), CreateEventActivity.class);
        startActivity(intent);
    }

    /**
     * Initialize user from Firebase.  We do this for later to get the user host organization.
     */
    private void initializeUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference currentUserRef = database.getReference().child("users").child(currentUser.getUid());
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);

    }
}
