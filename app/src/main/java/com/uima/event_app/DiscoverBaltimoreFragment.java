package com.uima.event_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DiscoverBaltimoreFragment extends Fragment {

    private ListView eventCategoryListView;
    protected static ArrayList<String> categoryItems;
    protected static EventCategoryAdapter ecAdapter;
    protected View rootView;
    String categoryName = "";

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
        eventCategoryListView.setAdapter(ecAdapter); // Layout File

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

                EventSelectFragment eventSelectFragment = new EventSelectFragment();
                categoryName = listItem.toString();

                SharedPreferences myPrefs = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("TYPE", categoryName);
                editor.commit();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, eventSelectFragment)
                        .addToBackStack(null)
                        .commit();


                getActivity().setTitle(categoryName);
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

    public class EventCategoryAdapter extends ArrayAdapter<String> {
        int res;

        public EventCategoryAdapter(Context ctx, int res, List<String> items)  {
            super(ctx, res, items);
            this.res = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout categoryView;
            String category = getItem(position);

            if (convertView == null) {
                categoryView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(res, categoryView, true);
            } else {
                categoryView = (LinearLayout) convertView;
            }

            TextView category_name = (TextView) categoryView.findViewById(R.id.category_name);
            TextView category_desc = (TextView) categoryView.findViewById(R.id.category_description);
            ImageView categoryTypeImage = (ImageView) categoryView.findViewById(R.id.category_image);


            if (category.equals("Local Culture")) {
                categoryTypeImage.setImageResource(R.drawable.local_culture);
                category_desc.setText(R.string.local_culture_desc);
            } else if (category.equals("Social Activism")) {
                categoryTypeImage.setImageResource(R.drawable.activism);
                category_desc.setText(R.string.social_activism_desc);
            } else if (category.equals("Popular Culture")) {
                categoryTypeImage.setImageResource(R.drawable.pop_culture);
                category_desc.setText(R.string.popular_culture_desc);
            } else if (category.equals("Community Outreach")) {
                categoryTypeImage.setImageResource(R.drawable.charity);
                category_desc.setText(R.string.community_outreach_desc);
            } else if (category.equals("Education & Learning")) {
                categoryTypeImage.setImageResource(R.drawable.education);
                category_desc.setText(R.string.education_and_learning_desc);
            } else if (category.equals("Shopping & Market Events")) {
                categoryTypeImage.setImageResource(R.drawable.shopping);
                category_desc.setText(R.string.shopping_and_markets_desc);
            } else if (category.equals("Miscellaneous")) {
                categoryTypeImage.setImageResource(R.drawable.miscellaneous);
                category_desc.setText(R.string.miscellaneous_desc);
            }

            category_name.setText(category);
            return categoryView;
        }
    }

}