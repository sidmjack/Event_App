package com.uima.event_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyEventBoardFragment extends Fragment {

    private ListView eventBoardListView;
    protected static EventBoardAdapter ebAdapter;
    protected View rootView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    int count = 0;

    public MyEventBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Event Board");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_event_board, container, false);
        eventBoardListView = (ListView) rootView.findViewById(R.id.event_board_list_view);
        populateEventBoardList();
        return rootView;
    }

    public void populateEventBoardList() {
        final ArrayList<Event> favoritesList = new ArrayList<>(); // List of favorited events.
        final ArrayList<String> favoritedEvents = new ArrayList<>(); // List of eventIds

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference currentUserRef = database.getReference().child("users").child(currentUser.getUid());

        /*Start of Event Listener for Users Favorited Events*/

        currentUserRef.child("favorites").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                favoritesList.clear();
                favoritedEvents.clear();

                System.out.println("Fresh favorites list: " + favoritesList.size());
                System.out.println("Fresh favorited events list: " + favoritedEvents.size());

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int size = 0;
                for (DataSnapshot child : children) {
                    String eventId = child.getValue(String.class);
                    favoritedEvents.add(eventId);
                    size++;
                    if (size == dataSnapshot.getChildrenCount()) {
                        favEventCounterReset();
                        System.out.println("(Inside) Size of Favorited Events List: " + favoritedEvents.size());

                        for(String eventKey: favoritedEvents) {
                            System.out.println("Event: " + eventKey);
                        }

                        for (String eventID : favoritedEvents) {
                            DatabaseReference eventRef= database.getReference().child("events").child(eventID);
                            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Event favedEvent = dataSnapshot.getValue(Event.class);
                                    favoritesList.add(favedEvent);
                                    favEventCounter();
                                    System.out.println("Iteration Count: " + count);
                                    System.out.println("Favorited Event Count: " + favoritedEvents.size());
                                    if (count == favoritedEvents.size()) {
                                        System.out.println("Size of Favorites List: " + favoritesList.size());
                                        ebAdapter.notifyDataSetChanged();
                                        //ebAdapter = new EventBoardAdapter(getActivity(), R.layout.event_board_row, favoritesList);
                                        //eventBoardListView.setAdapter(ebAdapter);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Do Nothing.
                                }

                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ebAdapter = new EventBoardAdapter(getActivity(), R.layout.event_board_row, favoritesList);
        eventBoardListView.setAdapter(ebAdapter);
    }

    public void favEventCounterReset() {
        count = 0;
    }

    public void favEventCounter() {
        count++;
    }

    public class EventBoardAdapter extends ArrayAdapter<Event> {
        int res;

        public EventBoardAdapter(Context ctx, int res, List<Event> items)  {
            super(ctx, res, items);
            this.res = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout eventBoardView;
            Event ebEvent = getItem(position);

            if (convertView == null) {
                eventBoardView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(res, eventBoardView, true);
            } else {
                eventBoardView = (LinearLayout) convertView;
            }

            String eventName = ebEvent.getName();

            // Simplified Event Board Row Filling:
            String event_time = ebEvent.getDate() + " @ " + ebEvent.getStart_time();
            String event_time_notification = "Event: " + "Upcoming!";

            TextView eventBoardName = (TextView) eventBoardView.findViewById(R.id.event_title);
            final Button attendButton = (Button) eventBoardView.findViewById(R.id.attend_button);


            attendButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (attendButton.isPressed()) {
                        attendButton.setPressed(false);
                        return true;
                    } else {
                        attendButton.setPressed(true);
                        return true;
                    }
                }
            });
            TextView eventTime = (TextView) eventBoardView.findViewById(R.id.event_time);
            TextView eventNotification = (TextView) eventBoardView.findViewById(R.id.event_time_notification);

            eventBoardName.setText(eventName);
            eventTime.setText(event_time);
            eventNotification.setText(event_time_notification);

            return eventBoardView;
        }
    }

}
