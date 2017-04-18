package com.uima.event_app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static android.R.attr.host;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CharSequence mTitle;

    private static DiscoverBaltimoreFragment discoverBaltimoreFragment = new DiscoverBaltimoreFragment();
    private static MyEventBoardFragment myEventBoardFragment = new MyEventBoardFragment();
    private static MyLiveEventsFragment myLiveEventsFragment = new MyLiveEventsFragment();

    private static int currentTitle = R.string.discover_baltimore;
    private static Fragment currentFragment = discoverBaltimoreFragment;
    private FragmentTabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // host keeps turning up null...
        host = (FragmentTabHost) findViewById(android.R.id.tabhost);
        host.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        host.addTab(host.newTabSpec("live").setIndicator("live"), MyLiveEventsFragment.class, null);
        host.addTab(host.newTabSpec("history").setIndicator("history"), MyPastEventsFragment.class, null);
        host.addTab(host.newTabSpec("drafts").setIndicator("drafts"), MyDraftEventsFragment.class, null);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragment).commit();
        setTitle(currentTitle);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getFragmentManager();

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case (R.id.nav_EventMap):
                Intent eventMapIntent = new Intent(MainActivity.this, EventMapActivity.class);
                MainActivity.this.startActivity(eventMapIntent);
                currentTitle = R.string.event_map;
                break;
            case (R.id.nav_DiscoverBaltimore):
                currentFragment = discoverBaltimoreFragment;
                currentTitle = R.string.discover_baltimore;
                break;
            case (R.id.nav_MyEventBoard):
                currentFragment = myEventBoardFragment;
                currentTitle = R.string.my_event_board;
                break;
            case (R.id.nav_ManageMyEvents):
                currentFragment = myLiveEventsFragment;
                currentTitle = R.string.manage_events;
                break;
            default:
                currentFragment = discoverBaltimoreFragment;
                currentTitle = R.string.discover_baltimore;
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
