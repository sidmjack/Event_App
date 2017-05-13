package com.uima.event_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventSelectAdapter  extends ArrayAdapter<Event> {
    int res;
    String imgUrl;
    String event_name;
    String event_date_time;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference currentUserRef;


    public EventSelectAdapter(Context ctx, int res, List<Event> items)  {
        super(ctx, res, items);
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout eventSelectListView;
        final Event eventSelectedItem = getItem(position);

        if (convertView == null) {
            eventSelectListView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, eventSelectListView, true);
        } else {
            eventSelectListView = (LinearLayout) convertView;
        }

        imgUrl = eventSelectedItem.getImgId();

        event_name = eventSelectedItem.getName();
        event_date_time = (eventSelectedItem.getLocation()+ " @ " + eventSelectedItem.getStartTimeString()); // Just Changed.

        //ImageView eventSelectOrganizationLogo = (ImageView) eventSelectListView.findViewById(R.id.selected_event_organization_logo);
        TextView eventSelect_name = (TextView) eventSelectListView.findViewById(R.id.selected_event_name);
        TextView eventSelect_desc = (TextView) eventSelectListView.findViewById(R.id.selected_event_description);

        //eventSelectOrganizationLogo.setImageResource(imgID);
        eventSelect_name.setText(event_name);
        eventSelect_desc.setText(event_date_time);

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();

        final Button attendButton = (Button) eventSelectListView.findViewById(R.id.attending_button);

        // Set Previously Pressed Favorites Here:
        setPressedButtons(eventSelectedItem, attendButton);

        attendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (attendButton.isPressed() && event.getAction() == MotionEvent.ACTION_DOWN) {
                    attendButton.setPressed(false);
                    DatabaseReference myRef = database.getReference();
                    final DatabaseReference favEventRef = myRef.child("users").child(userID).child("favorites");
                    favEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                String eventKey = child.getValue(String.class);
                                if (eventKey.equals(eventSelectedItem.getId())) {
                                    favEventRef.child(child.getKey()).removeValue();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Do Nothing
                        }
                    });
                    return true;
                } else if (!(attendButton.isPressed()) && event.getAction() == MotionEvent.ACTION_DOWN){
                    attendButton.setPressed(true);
                    DatabaseReference myRef = database.getReference();
                    DatabaseReference userRef = myRef.child("users");
                    userRef.child(userID).child("favorites").push().setValue(eventSelectedItem.getId());
                    System.out.println("why she always so mad");
                    return true;
                }
                return true;
            }
        });

        return eventSelectListView;
    }

    public static void setPressedButtons(Event selectedEvent, final Button attendButton) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        final Event sEvent = selectedEvent;

        DatabaseReference myRef = database.getReference();
        final DatabaseReference favEventRef = myRef.child("users").child(userID).child("favorites");
        favEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                attendButton.setPressed(false);
                for (DataSnapshot child : children) {
                    String eventKey = child.getValue(String.class);
                    if (eventKey.equals(sEvent.getId())) {
                        attendButton.setPressed(true);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do Nothing
            }
        });

    }

}
