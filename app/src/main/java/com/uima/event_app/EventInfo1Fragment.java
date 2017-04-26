package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventInfo1Fragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_info1, container, false);
        return rootView;
    }

    public EventInfo1Fragment() {
        // Required empty public constructor
    }

    public static EventInfo1Fragment newInstance() {
        EventInfo1Fragment f = new EventInfo1Fragment();
        return f;
    }
}
