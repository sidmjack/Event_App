package com.uima.event_app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventInfo2Fragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_info2, container, false);
        return rootView;
    }

    public EventInfo2Fragment() {
        // Required empty public constructor
    }

    public static EventInfo2Fragment newInstance() {
        EventInfo2Fragment f = new EventInfo2Fragment();
        return f;
    }

}
