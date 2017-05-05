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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyEventBoardFragment extends Fragment {

    private ListView eventBoardListView;
    protected static ArrayList<Event> eventBoardItems;
    protected static EventBoardAdapter ebAdapter;
    protected View rootView;
    private static LruCache<String, Bitmap> mMemoryCache;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public MyEventBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemorySize / 10;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        eventBoardItems = populateEventBoardList();

        ebAdapter = new EventBoardAdapter(getActivity(), R.layout.event_board_row, eventBoardItems);
        eventBoardListView.setAdapter(ebAdapter);

        getActivity().setTitle("My Event Board");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_event_board, container, false);

        eventBoardListView = (ListView) rootView.findViewById(R.id.event_board_list_view);

        return rootView;
    }

    public static Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void setBitMapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public ArrayList<Event> populateEventBoardList() {

        // Fake Events
        ArrayList<Event> fakeEventList = new ArrayList<Event>();

        // Fake Organizations
        ArrayList<Organization> fakeOrganizationList = new ArrayList<Organization>();

        // Types of Fake Events
        ArrayList<String> fakeTypes = new ArrayList<String>();
        fakeTypes.add("Popular Culture");

        // More Fake Types
        ArrayList<String> fakeTypes1 = new ArrayList<String>();
        fakeTypes1.add("Popular Culture");
        fakeTypes1.add("Local Culture");
        ArrayList<String> fakeTypes2 = new ArrayList<String>();
        fakeTypes2.add("Community Outreach");
        ArrayList<String> fakeTypes3 = new ArrayList<String>();
        fakeTypes3.add("Miscellaneous");

        // Image Id for fake events.
        String imgId = Integer.toString(R.drawable.question);

        // Types of Fake Events
        ArrayList<String> fakeTags = new ArrayList<String>();
        fakeTypes.add("Free Food");
        fakeTypes.add("Music");
        fakeTypes.add("Parking");

        // Create ArrayList of fake events here!

        String[] fakeEventInfo0 = {"0","Beach Party","Johns Hopkins University","3500 N. Charles St.","Party on the Beach!", "2:00 PM", "5:00 PM", "April 18, 2017"};
        String[] fakeEventInfo1 = {"0","Harbor Clean Up","Inner Harbor","1200 Riddle St.","We should probably clean this up...", "10:00 AM", "2:00 PM", "April 20, 2017"};
        //String[] fakeEventInfo2 = {"0","Spring Fair","Johns Hopkins","3500 N. Charles St.","Spring is finally here! Celebrate with music, food, fun, and games", "10:00 AM", "8:00 PM", "April 23, 2017"};
        String[] fakeEventInfo3 = {"0","Baltimore Crab Festival","Felles Point","240 Thames St.","Crab Cakes...as far as the eye can see...", "11:00 AM", "4:30 PM", "April 25, 2017"};
        String[] fakeEventInfo4 = {"0","Orioles Baseball Game","Campden Yards","1224 Campden Rd.","Cheer on the O's and get a free t-shirt", "12:00 PM", "3:00 PM", "April 27, 2017"};
        String[] fakeEventInfo5 = {"0","Charmery Ice Cream Party","Hampden","2994 West University St.","FREE ICE CREAM! Need we say more?", "6:00 PM", "8:30 PM", "April 27, 2017"};
        String[] fakeEventInfo6 = {"0","Federal Hill Sledding","Federal Hill","1776 Washington St.","Ride the Hill! Dodge the cars...!", "3:00 PM", "5:00 PM", "April 29, 2017"};

        // Fake Organization Logos
        String fakeLogo0 = Integer.toString(R.drawable.crab);
        String fakeLogo1 = Integer.toString(R.drawable.dancer);
        String fakeLogo2 = Integer.toString(R.drawable.jhu_logo);

        // Fake Event Backgrounds
        //String fakeEventBk = Integer.toString(R.drawable.event_board3);
        String fakeEventBk0 = Integer.toString(R.drawable.event_board3);
        String fakeEventBk1 = Integer.toString(R.drawable.event_board1);
        //String fakeEventBk2 = Integer.toString(R.drawable.event_board7);
        String fakeEventBk3 = Integer.toString(R.drawable.event_board8);
        String fakeEventBk4 = Integer.toString(R.drawable.event_board6);
        //String fakeEventBk5 = Integer.toString(R.drawable.event_board5);
        String fakeEventBk6 = Integer.toString(R.drawable.event_board4);

        // Create FakeOrganizations Here!
        String[] fakeOrgInfo0 = {"0", "JHU", "3500 N. Charles", "University", fakeLogo2, "jhu@jhu.edu", "410-516-0000", "jhu.edu", "jhu.fb", "jhu.instagram", "jhu.twitter"};
        String[] fakeOrgInfo1 = {"0", "Party City", "123 Party Rd.", "We Party.", fakeLogo1, "party@party.edu", "410-123-3210", "party.com", "party.fb", "party.instagram", "party.twitter"};
        String[] fakeOrgInfo2 = {"0", "Balitmore Parks & Rec", "584 Baltimore St.", "We love Baltimore!", fakeLogo0, "balti@md.org", "410-121-1211", "baltimore.org", "balit.fb", "balit.instagram", "balti.twitter"};

        Organization fakeOrg0 = new Organization(fakeOrgInfo0); // JHU
        Organization fakeOrg1 = new Organization(fakeOrgInfo1); // Party
        Organization fakeOrg2 = new Organization(fakeOrgInfo2); // Baltimore

        Event fake_event_1 = new Event();
        Event fake_event_2 = new Event();
        Event fake_event_3 = new Event();

        Event[] fakeEvents= {fake_event_1, fake_event_2, fake_event_3};

        for(int i = 0; i < fakeEvents.length; i++) {
            Event temp = fakeEvents[i];
            fakeEventList.add(temp);
        }

        return fakeEventList;
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
            //int eventImage = Integer.parseInt(ebEvent.getImgId());
            //String date = ("Date: " + ebEvent.getDate());
            //String time = ("Time: " + ebEvent.getStart_time() + " - "+ ebEvent.getEnd_time());
            //String desc = ebEvent.getDetails();

            // Simplified Event Board Row Filling:
            String event_time = ebEvent.getDate() + " @ " + ebEvent.getStart_time();
            String event_time_notification = "Event: " + "Upcoming!";


            TextView eventBoardName = (TextView) eventBoardView.findViewById(R.id.event_title);
            //ImageView eventBoardPicture = (ImageView) eventBoardView.findViewById(R.id.event_board_header);
            //TextView eventDate = (TextView) eventBoardView.findViewById(R.id.event_board_event_date);
            //TextView eventTime = (TextView) eventBoardView.findViewById(R.id.event_board_event_time);
            //TextView eventDesc = (TextView) eventBoardView.findViewById(R.id.selected_event_desc);
            final Button attendButton = (Button) eventBoardView.findViewById(R.id.attend_button);

            /*attendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (attendButton.isPressed()) {
                        attendButton.setPressed(false);
                    } else {
                        attendButton.setPressed(true);
                    }
                }
            });*/

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
            //eventBoardPicture.setImageResource(eventImage);
            //eventDate.setText(date);
            //eventTime.setText(time);
            //eventDesc.setText(desc);

            return eventBoardView;
        }
    }

}
