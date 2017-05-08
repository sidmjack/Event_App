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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventSelectAdapter  extends ArrayAdapter<Event> {
    int res;
    int imgID;
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
        Event eventSelectedItem = getItem(position);

        if (convertView == null) {
            eventSelectListView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, eventSelectListView, true);
        } else {
            eventSelectListView = (LinearLayout) convertView;
        }

        imgID = Integer.valueOf(eventSelectedItem.getImgId());
        event_name = eventSelectedItem.getName();
        event_date_time = (eventSelectedItem.getLocation() + " at " + eventSelectedItem.getStart_time());

        //ImageView eventSelectOrganizationLogo = (ImageView) eventSelectListView.findViewById(R.id.selected_event_organization_logo);
        TextView eventSelect_name = (TextView) eventSelectListView.findViewById(R.id.selected_event_name);
        TextView eventSelect_desc = (TextView) eventSelectListView.findViewById(R.id.selected_event_description);

        //eventSelectOrganizationLogo.setImageResource(imgID);
        eventSelect_name.setText(event_name);
        eventSelect_desc.setText(event_date_time);


        // Handles "getting" user reference.
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserRef = database.getReference().child("users").child(currentUser.getUid());

        //currentUser.child("");

        final Button attendButton = (Button) eventSelectListView.findViewById(R.id.attending_button);

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

        //SharedPreferences myPrefs = getSharedPreferences("favorites", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = myPrefs.edit();
        //editor.putString("Event Key", xxx);
        //editor.commit();

        return eventSelectListView;
    }

    // "favs" is the name of the list of the users favorited events

    /*public boolean saveArray(ArrayList<String> favorites, String favs, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("favorite_events", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(favs+"_size", favorites.size()); // Set size of favorite event array
        for(int i=0;i<favorites.size();i++) // Iterate through all event keys saved in favorites array
            editor.putString(favs+ "_"+ i, favorites.get(i));
        return editor.commit();
    }

    public ArrayList<String> loadArray(String favs, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("favorite_events", 0);
        int size = prefs.getInt(favs+"_size", 0);
        ArrayList<String> favorites = new ArrayList<String>();
        for(int i=0;i<size;i++)
            favorites.add(prefs.getString(favs+ "_"+ i, null));
        return favorites;
    }

    public void addFavorite(String favs, Context mContext) {
        ArrayList<String> temp = getFavorites(favs, mContext);
        // TODO

    }

    public void removeFavorite(String favs, Context mContext) {
        ArrayList<String> temp = getFavorites(favs, mContext);
        // TODO
    }

    public ArrayList<String> getFavorites(String favs, Context mContext){
        ArrayList<String> favorites = new ArrayList<String>();
        favorites = loadArray(favs, mContext);
        return favorites;
    }*/

}
