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
    protected FirebaseAdapter fb;
    protected String type;

    public EventSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fb = new FirebaseAdapter(getContext());
        type = getActivity().getTitle().toString();
        eventItems = new ArrayList<Event>();
        getEventItems();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        getEventItems();

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

                //EventPageFragment eventPageFragment = new EventPageFragment();
                EventPageFragment eventPageFragment = new EventPageFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.content_frame, eventPageFragment)
                        .replace(R.id.content_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
                String eventName = selectedEvent.getName();
                getActivity().setTitle(eventName);
            }
        });

        return rootView;
    }

    public void getEventItems() {
        if (type.contains("Local Culture")) {
            eventItems = fb.getLocalCulture();
        } else if (type.contains("Social Activism")) {
            eventItems = fb.getSocailActivism();
        } else if (type.contains("Popular")) {
            eventItems = fb.getPopularCulture();
        } else if (type.contains("Community")) {
            eventItems = fb.getCommunityOutreach();
        } else if (type.contains("Education")) {
            eventItems = fb.getEducationLearning();
        } else if (type.contains("Shopping")) {
            eventItems = fb.getShoppingMarket();
        } else {
            eventItems = fb.getMiscellaneous();
        }
    }


}
