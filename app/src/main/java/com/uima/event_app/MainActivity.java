package com.uima.event_app;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference currentUserRef;

    private boolean splashStarted = false;

    private CharSequence mTitle;

    private static EventMapFragment eventMapFragment = new EventMapFragment();
    private static DiscoverBaltimoreFragment discoverBaltimoreFragment = new DiscoverBaltimoreFragment();
    private static MyEventBoardFragment myEventBoardFragment = new MyEventBoardFragment();
    private static ManageEventsFragment manageEventsFragment = new ManageEventsFragment();


    private static int currentTitle = R.string.event_map;
    private static Fragment currentFragment = eventMapFragment;

    TextView navNameTextView;
    TextView navEmailTextView;
    MenuItem manageEventsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserRef = database.getReference().child("users").child(currentUser.getUid());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View headerView = navigationView.getHeaderView(0);
        navNameTextView = (TextView) headerView.findViewById(R.id.nav_head_name);
        navEmailTextView = (TextView) headerView.findViewById(R.id.nav_head_email);

        Menu drawerMenuView = navigationView.getMenu();
        manageEventsItem = drawerMenuView.findItem(R.id.nav_ManageMyEvents);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                navNameTextView.setText(user.getFirstname());
                navEmailTextView.setText(user.getEmail());

                if (!user.getIsOrganizer()) {
                    manageEventsItem.setVisible(false);
                } else {
                    manageEventsItem.setVisible(true);
                }

                try {
                    String imgId = user.getImagePath();
                    if (imgId != null) {
                        final ImageView navHeader = (ImageView) headerView.findViewById(R.id.header_imageView);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference sRef = storage.getReference();
                        sRef.child(user.getImagePath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(MainActivity.this).load(uri).fit().centerCrop().into(navHeader);
                            }
                        });
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Please select and Image in from a profile");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragment).commit();
        setTitle(currentTitle);
    }

    @Override
    public void onResume(){
        super.onResume();}

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } */
        if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case (R.id.nav_EventMap):
                currentFragment = eventMapFragment;
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
                currentFragment = manageEventsFragment;
                currentTitle = R.string.manage_events;
                break;
            default:
                currentFragment = eventMapFragment;
                currentTitle = R.string.event_map;
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .addToBackStack(null)
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
