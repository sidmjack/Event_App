package com.uima.event_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyLiveEventsFragment extends ManageEventsFragment {
    private View rootView;

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
        editButton = (ImageButton) rootView.findViewById(R.id.edit_event_btn);

        updateArray();
        registerForContextMenu(eventListView);

        eventListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventPageFragment eventPageFragment = new EventPageFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.content_frame, eventPageFragment)
                        .replace(R.id.content_frame, eventPageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        /*editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditEventActivity.class);
                startActivity(intent);
            }
        });*/
        return rootView;
    }
}
