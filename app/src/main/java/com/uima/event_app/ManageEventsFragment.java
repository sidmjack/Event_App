package com.uima.event_app;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.host;
import static android.R.attr.type;
import static android.R.attr.value;
import static android.media.CamcorderProfile.get;
import static com.uima.event_app.EventSelectFragment.eventItems;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventsFragment extends Fragment {
    private final List<Event> eventItems  = new ArrayList<>();
    private static EventAdapter adapter;
    private  ListView eventListView;

    public static final int MENU_ITEM_DUPLICATE = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;
    public static final int MENU_ITEM_EDIT = Menu.FIRST + 2;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public ManageEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        SharedPreferences myPrefs = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        final String hostOrg = myPrefs.getString("organization", "");
        System.out.println("*****HOSTORG " + hostOrg);

        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);
                    System.out.println(value.getHostOrg());
                    if (value.getHostOrg().equalsIgnoreCase(hostOrg)) {
                        eventItems.add(value);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_events, container, false);
        eventListView = (ListView) rootView.findViewById(R.id.event_list_view);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventPageFragment eventPageFragment = new EventPageFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.content_frame, eventPageFragment)
                        .replace(R.id.content_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        updateArray();
        registerForContextMenu(eventListView);

        return rootView;
    }

    protected void updateArray() {
        // get dummy or actual list of events
        adapter = new EventAdapter(getActivity(), R.layout.event_item, eventItems);
        eventListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)

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

        switch (item.getItemId()) {
            case MENU_ITEM_DUPLICATE: {
                Event event = eventItems.get(index);

                Intent intent = new Intent(getActivity(), CreateEventActivity.class);

                intent.putExtra("event name", event.getName());
                intent.putExtra("event location", event.getLocation());
                intent.putExtra("event details", event.getDetails());
                intent.putExtra("duplicate", true);

                startActivity(intent);

                return true;
            }
            case MENU_ITEM_DELETE: {
                Toast.makeText(getContext(), "event " + index + " deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            case MENU_ITEM_EDIT: {
                Event event = eventItems.get(index);

                Intent intent = new Intent(getActivity(), EditEventActivity.class);

                intent.putExtra("event name", event.getName());
                intent.putExtra("event location", event.getLocation());
                intent.putExtra("event details", event.getDetails());
                intent.putExtra("duplicate", false);

                startActivity(intent);

                return true;
            }
        }
        return false;
    }
}
