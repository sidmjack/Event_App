package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sidneyjackson on 4/18/17.
 */

public class EventPageFragment extends Fragment {

    protected View rootView;
    private String eventID;

    public EventPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eventID = getArguments().getString("eventID");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.eventID = getArguments().getString("eventID");
        getActivity().setTitle(getArguments().getString("eventTitle"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //rootView = inflater.inflate(R.layout.event_board_row, container, false);
        rootView = inflater.inflate(R.layout.fragment_event_page, container, false);
        getActivity().setTitle(getArguments().getString("eventTitle"));

        TopEventPageFragment upperPageFragment = new TopEventPageFragment();
        BottomEventPageFragment lowerPageFragment = new BottomEventPageFragment();

        Bundle data = new Bundle();
        data.putString("eventID", eventID);
        upperPageFragment.setArguments(data);
        lowerPageFragment.setArguments(data);

        FragmentTransaction transaction =
               getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.event_page_top_half, upperPageFragment);
        transaction.add(R.id.event_page_bottom_half, lowerPageFragment);

        transaction.commit();


        return rootView;
    }
}
