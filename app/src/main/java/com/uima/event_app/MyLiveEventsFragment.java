package com.uima.event_app;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyLiveEventsFragment extends ManageEventsFragment {
    private View rootView;

    public static final int MENU_ITEM_DUPLICATE = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    public MyLiveEventsFragment() {
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

    private void updateArray() {
        // get dummy or actual list of events
        eventItems = new ArrayList<>();
        List<String> dummyTypes = new ArrayList<>();
        dummyTypes.add("Local Culture");
        dummyTypes.add("Social Activism");
        List<String> dummyTags = new ArrayList<>();
        dummyTags.add("free");
        dummyTags.add("parking");
        /*Event temp = new Event("An Event!", "An Org", "JHU", "oh look details", false, "", "FAKE", dummyTags);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        eventItems.add(temp);
        */

        adapter = new EventAdapter(getActivity(), R.layout.event_item, eventItems);
        eventListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        menu.setHeaderTitle("Select Option");

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
                // duplicate event
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

}
