package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class MyEventBoardFragment extends Fragment {

    private ListView eventBoardListView;
    protected static ArrayList<Event> eventBoardItems;
    protected static EventBoardAdapter ebAdapter;
    protected View rootView;

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

        Event fakeEvent0 = new Event(fakeEventInfo0, fakeOrg0, false, fakeTypes, fakeTags, fakeEventBk0);
        Event fakeEvent1 = new Event(fakeEventInfo1, fakeOrg2, true, fakeTypes2, fakeTags, fakeEventBk1);
        //Event fakeEvent2 = new Event(fakeEventInfo2, fakeOrg0, true, fakeTypes1, fakeTags, fakeEventBk2);
        Event fakeEvent3 = new Event(fakeEventInfo3, fakeOrg2, false, fakeTypes1, fakeTags, fakeEventBk3);
        Event fakeEvent4 = new Event(fakeEventInfo4, fakeOrg2, false, fakeTypes1, fakeTags, fakeEventBk4);
        //Event fakeEvent5 = new Event(fakeEventInfo5, fakeOrg1, true, fakeTypes2, fakeTags, fakeEventBk5);
        Event fakeEvent6 = new Event(fakeEventInfo6, fakeOrg1, false, fakeTypes3, fakeTags, fakeEventBk6);

        /*Event fakeEvent0 = new Event("Event_Example_0", "Example_0.org", "Baltimore", "Fun Event", true, imgId, fakeTypes, fakeTags);
        Event fakeEvent1 = new Event("Event_Example_1", "Example_1.org", "Charles Village", "Educational Event", false, imgId, fakeTypes, fakeTags);
        Event fakeEvent2 = new Event("Event_Example_2", "Example_2.org", "Hampden", "Interesting Event", true, imgId, fakeTypes, fakeTags);
        Event fakeEvent3 = new Event("Event_Example_3", "Example_3.org", "Downtown Baltimore", "Enlightening Event", false, imgId, fakeTypes, fakeTags);
        Event fakeEvent4 = new Event("Event_Example_4", "Example_4.org", "Johns Hopkins", "Kick-Off Event", true, imgId, fakeTypes, fakeTags);
        Event fakeEvent5 = new Event("Event_Example_5", "Example_5.org", "Felles Point", "Food Event", false, imgId, fakeTypes, fakeTags);
        Event fakeEvent6 = new Event("Event_Example_6", "Example_6.org", "Inner Harbor", "Dance Event", true, imgId, fakeTypes, fakeTags);*/

        Event[] fakeEvents= {fakeEvent0, fakeEvent1, fakeEvent3, fakeEvent4, fakeEvent6};

        //Event[] fakeEvents= new Event[4];
        //fakeEvents[0] = fakeEvent0;
        //fakeEvents[1] = fakeEvent1;
        //fakeEvents[X] = fakeEvent2;
        //fakeEvents[2] = fakeEvent3;
        //fakeEvents[3] = fakeEvent4;
        //fakeEvents[X] = fakeEvent5;
        //fakeEvents[4] = fakeEvent6;

        for(int i = 0; i < fakeEvents.length; i++) {
            Event temp = fakeEvents[i];
            fakeEventList.add(temp);
        }

        return fakeEventList;
    }

}
