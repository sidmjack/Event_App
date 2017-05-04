package com.uima.event_app;


import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventsFragment extends Fragment {
    protected static List<Event> eventItems;
    protected static EventAdapter adapter;
    protected  ListView eventListView;
    private FragmentTabHost host;
    protected ImageButton editButton;

    public static final int MENU_ITEM_DUPLICATE = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    public ManageEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // host keeps turning up null...
        host = new FragmentTabHost(getActivity());
        host.setup(getActivity(), getChildFragmentManager(), R.layout.fragment_manage_events);

        host.addTab(host.newTabSpec("live").setIndicator("live"), MyLiveEventsFragment.class, null);
        host.addTab(host.newTabSpec("history").setIndicator("history"), MyPastEventsFragment.class, null);
        host.addTab(host.newTabSpec("drafts").setIndicator("drafts"), MyDraftEventsFragment.class, null);

        return host;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)

        // Add menu items
        menu.add(0, MENU_ITEM_DUPLICATE, 0, R.string.menu_duplicate);
        menu.add(0, MENU_ITEM_DELETE, 0, R.string.menu_delete);
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

                Intent intent = new Intent(getActivity(), EditEventActivity.class);

                intent.putExtra("event name", event.getName());
                intent.putExtra("event location", event.getLocation());
                intent.putExtra("event details", event.getDetails());

                startActivity(intent);

                return false;
            }
            case MENU_ITEM_DELETE: {
                Toast.makeText(getContext(), "event " + index + " deleted",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    protected void updateArray() {
        // get dummy or actual list of events
        eventItems = populateEventList();
        adapter = new EventAdapter(getActivity(), R.layout.event_item, eventItems);
        eventListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<Event> populateEventList() {

        // Fake Events
        List<Event> fakeEventList = new ArrayList<Event>();

        // Types of Fake Events
        List<String> fakeTypes = new ArrayList<String>();
        fakeTypes.add("Popular Culture");

        // Image Id for fake events.
        String imgId = Integer.toString(R.drawable.question);

        // Types of Fake Events
        ArrayList<String> fakeTags = new ArrayList<String>();
        fakeTypes.add("Free Food");
        fakeTypes.add("Music");
        fakeTypes.add("Parking");

        // Create ArrayList of fake events here!

        Event fakeEvent0 = new Event("Event_Example_0", "Example_0.org", "Baltimore", "Fun Event", true, imgId, fakeTypes, fakeTags);
        Event fakeEvent1 = new Event("Event_Example_1", "Example_1.org", "Charles Village", "Educational Event", false, imgId, fakeTypes, fakeTags);
        Event fakeEvent2 = new Event("Event_Example_2", "Example_2.org", "Hampden", "Interesting Event", true, imgId, fakeTypes, fakeTags);
        Event fakeEvent3 = new Event("Event_Example_3", "Example_3.org", "Downtown Baltimore", "Enlightening Event", false, imgId, fakeTypes, fakeTags);
        Event fakeEvent4 = new Event("Event_Example_4", "Example_4.org", "Johns Hopkins", "Kick-Off Event", true, imgId, fakeTypes, fakeTags);
        Event fakeEvent5 = new Event("Event_Example_5", "Example_5.org", "Felles Point", "Food Event", false, imgId, fakeTypes, fakeTags);
        Event fakeEvent6 = new Event("Event_Example_6", "Example_6.org", "Inner Harbor", "Dance Event", true, imgId, fakeTypes, fakeTags);

        Event[] fakeEvents= new Event[7];
        fakeEvents[0] = fakeEvent0;
        fakeEvents[1] = fakeEvent1;
        fakeEvents[2] = fakeEvent2;
        fakeEvents[3] = fakeEvent3;
        fakeEvents[4] = fakeEvent4;
        fakeEvents[5] = fakeEvent5;
        fakeEvents[6] = fakeEvent6;

        for(int i = 0; i < fakeEvents.length; i++) {
            Event temp = fakeEvents[i];
            fakeEventList.add(temp);
        }

        return fakeEventList;
    }

}
