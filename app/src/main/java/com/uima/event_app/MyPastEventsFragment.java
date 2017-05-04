package com.uima.event_app;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.uima.event_app.MyLiveEventsFragment.MENU_ITEM_DELETE;
import static com.uima.event_app.MyLiveEventsFragment.MENU_ITEM_DUPLICATE;

public class MyPastEventsFragment extends ManageEventsFragment {
    private View rootView;

    public MyPastEventsFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_my_live_events, container, false);
        eventListView = (ListView) rootView.findViewById(R.id.event_list_view);

        updateArray();
        registerForContextMenu(eventListView);
        return rootView;
    }

//    private void updateArray() {
//        // get dummy or actual list of events
//        eventItems = new ArrayList<>();
//        List<String> dummyTypes = new ArrayList<>();
//        dummyTypes.add("Local Culture");
//        dummyTypes.add("Social Activism");
//        List<String> dummyTags = new ArrayList<>();
//        dummyTags.add("free");
//        dummyTags.add("parking");
//        Event temp = new Event("An Event!", "An Org", "JHU", "oh look past details", false, "", dummyTypes, dummyTags);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//        eventItems.add(temp);
//
//        adapter = new EventSelectAdapter(getActivity(), R.layout.event_select_row, eventItems);
//        eventListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }
}
