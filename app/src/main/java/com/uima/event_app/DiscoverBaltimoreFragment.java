package com.uima.event_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DiscoverBaltimoreFragment extends Fragment {

    private ListView eventCategoryListView;
    protected static ArrayList<String> categoryItems;
    protected static EventCategoryAdapter ecAdapter;
    protected View rootView;
    String categoryName = " ";

    public DiscoverBaltimoreFragment() {
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

        categoryItems = populateCategoryList();

        ecAdapter = new EventCategoryAdapter(getActivity(), R.layout.event_category_row, categoryItems);
        eventCategoryListView.setAdapter(ecAdapter);

        getActivity().setTitle("Discover Baltimore");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_discover_baltimore, container, false);

        eventCategoryListView = (ListView) rootView.findViewById(R.id.event_category_list_view);

        eventCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = eventCategoryListView.getItemAtPosition(position);

                //EventListFragment eventListFragment = new EventListFragment();
                EventSelectFragment eventSelectFragment = new EventSelectFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        //.replace(R.id.content_frame, eventListFragment)
                        .replace(R.id.content_frame, eventSelectFragment)
                        .addToBackStack(null)
                        .commit();
                categoryName = listItem.toString();
                getActivity().setTitle(categoryName + " Events");
            }
        });

        return rootView;
    }

    public ArrayList<String> populateCategoryList() {
        ArrayList<String> category = new ArrayList<String>();

        String[] category_name = new String[7];
        category_name[0] = getString(R.string.local_culture);
        category_name[1] = getString(R.string.social_activism);
        category_name[2] = getString(R.string.popular_culture);
        category_name[3] = getString(R.string.community_outreach);
        category_name[4] = getString(R.string.education_and_learning);
        category_name[5] = getString(R.string.shopping_and_markets);
        category_name[6] = getString(R.string.miscellaneous);

        for(int i = 0; i < category_name.length; i++) {
            String temp = category_name[i];
            category.add(temp);
        }

        return category;
    }

}