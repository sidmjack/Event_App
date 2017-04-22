package com.uima.event_app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sidneyjackson on 4/21/17.
 */

public class BottomEventPageFragment extends Fragment {

    protected View rootView;
    TabLayout tabLayout;

    public BottomEventPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_bottom_event_page, container, false);

        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.event_page_view_pager);
        mViewPager.setAdapter(new EventPageAdapter(getChildFragmentManager()));

        tabLayout = (TabLayout) rootView.findViewById(R.id.event_page_tab_view);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private class EventPageAdapter extends FragmentPagerAdapter {
        private String fragments[] = {"Event Attributes", "General Information"};

        public EventPageAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new EventInfo1Fragment();
                case 1:
                    return new EventInfo2Fragment();
                default:
                    return new EventInfo1Fragment();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();

            super.destroyItem(container, position, object);
        }

    }
}
