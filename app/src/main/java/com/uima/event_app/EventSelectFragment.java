package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventSelectFragment extends Fragment {
    private ListView eventSelectListView;
    protected static ArrayList<Event> eventItems;
    protected static EventSelectAdapter esAdapter;
    protected View rootView;

    public EventSelectFragment() {
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

        eventItems = populateEventList();

        esAdapter = new EventSelectAdapter(getActivity(), R.layout.event_select_row, eventItems);
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

                EventPageFragment eventPageFragment = new EventPageFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
                String eventName = selectedEvent.getName();
                getActivity().setTitle(eventName);
            }
        });

        return rootView;
    }

    public ArrayList<Event> populateEventList() {

        // Fake Events
        ArrayList<Event> fakeEventList = new ArrayList<Event>();

        // Types of Fake Events
        ArrayList<String> fakeTypes = new ArrayList<String>();
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
