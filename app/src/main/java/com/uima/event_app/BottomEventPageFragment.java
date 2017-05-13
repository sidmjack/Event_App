package com.uima.event_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sidneyjackson on 4/21/17.
 */

public class BottomEventPageFragment extends Fragment implements ViewPager.OnPageChangeListener {

    protected View rootView;
    ViewPager pager;
    RadioGroup mRadioGroup;
    RadioButton rbOne;
    RadioButton rbTwo;
    private String eventID;

    //private FirebaseDatabase database;
    //private DatabaseReference myRef;

    public BottomEventPageFragment() {
        // Required empty public constructor
    }

    public static BottomEventPageFragment newInstance() {
        return new BottomEventPageFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.eventID = getArguments().getString("eventID");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.eventID = getArguments().getString("eventID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_bottom_event_page, container, false);

        // Handles setting up page viewer.
        pager = (ViewPager) rootView.findViewById(R.id.event_page_view_pager);
        pager.setAdapter(new EventPageAdapter(getChildFragmentManager()));
        pager.addOnPageChangeListener(this);

        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.mRadioGroup);
        rbOne = (RadioButton) rootView.findViewById(R.id.rb_frag_1);
        rbTwo = (RadioButton) rootView.findViewById(R.id.rb_frag_2);

        // On Click Listeners to handle radio button switches.

        rbOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });

        rbTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });

        return rootView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mRadioGroup.check(mRadioGroup.getChildAt(position).getId());
    }

    @Override
    public void onPageSelected(int position) {
        //Do Nothing ...
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do Nothing...
    }

    // Event Page Adapter Here...

    private class EventPageAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments = new ArrayList<>();

        public EventPageAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            EventInfo1Fragment e1 = new EventInfo1Fragment();
            EventInfo2Fragment e2 = new EventInfo2Fragment();
            Bundle data = new Bundle();
            data.putString("eventID", eventID);
            e1.setArguments(data);
            e2.setArguments(data);

            fragments.add(e1);
            fragments.add(e2);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return " ";
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

    }

}
