package com.uima.event_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventsFragment extends Fragment {
    protected static List<Event> eventItems;
    protected static EventAdapter adapter;
    protected  ListView eventListView;
    private FragmentTabHost host;



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
}
