package com.uima.event_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventsFragment extends Fragment {
    protected static ManageEventsAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    protected View rootView;

    private UserProfile user;
    private ListView myEventsView;

    private final List<Event> myEvents = new ArrayList<>();

    public static final int MENU_ITEM_DUPLICATE = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;
    public static final int MENU_ITEM_EDIT = Menu.FIRST + 2;

    public ManageEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("events");

        initializeUser();
        this.populateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_manage_events, container, false);
        myEventsView = (ListView) rootView.findViewById(R.id.list);

        myEventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity().getBaseContext(), "Event Created", Toast.LENGTH_SHORT).show();
                Event selectedEvent = (Event) myEventsView.getItemAtPosition(position);
                EventPageFragment eventPageFragment = new EventPageFragment();

                String eventName = selectedEvent.getName();
                getActivity().setTitle(eventName);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        registerForContextMenu(myEventsView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.populateList();
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

    private void populateList() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                myEvents.clear();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);
                    if (value.getHostOrg().equalsIgnoreCase(user.getOrganizer())) {
                        myEvents.add(value);
                    }
                }
                adapter = new ManageEventsAdapter(getActivity(), R.layout.event_item, myEvents);
                myEventsView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Add menu items
        menu.add(0, MENU_ITEM_DUPLICATE, 0, R.string.menu_duplicate);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
        menu.add(0, MENU_ITEM_EDIT, 0, R.string.menu_edit);
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position; // position in array adapter
        Event event = myEvents.get(index);

        switch (item.getItemId()) {
            case MENU_ITEM_DUPLICATE: {
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);

                intent.putExtra("event name", event.getName());
                intent.putExtra("event location", event.getLocation());
                intent.putExtra("event details", event.getDetails());
                intent.putExtra("event id", event.getId());
                intent.putExtra("duplicate", true);

                startActivity(intent);

                return true;
            }
            case MENU_ITEM_DELETE: {
                myRef.child(event.getId()).removeValue();
                return true;
            }
            case MENU_ITEM_EDIT: {
                Intent intent = new Intent(getActivity(), EditEventActivity.class);

                intent.putExtra("event name", event.getName());
                intent.putExtra("event location", event.getLocation());
                intent.putExtra("event details", event.getDetails());
                intent.putExtra("event id", event.getId());
                intent.putExtra("duplicate", false);

                startActivity(intent);

                return true;
            }
        }
        return false;
    }


}