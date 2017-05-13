package com.uima.event_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyEventBoardFragment extends Fragment{

    private ListView eventBoardListView;
    protected static EventBoardAdapter ebAdapter;
    protected View rootView;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    int count = 0;
    private ArrayList<String> keys = new ArrayList<>();
    private ValueEventListener valueEventListener;

    private FirebaseUser currentUser;
    private DatabaseReference currentUserRef;

    private StorageReference storageRef;  //mStorageRef was previously used to transfer data.

    private static final int MENU_ITEM_DELETE = Menu.FIRST;

    public MyEventBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //populateEventBoardList();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Event Board");
        //populateEventBoardList();
    }

    @Override
    public void onPause() {
        super.onPause();
        //currentUserRef.child("favorites").removeEventListener(valueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentUserRef.child("favorites").removeEventListener(valueEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my_event_board, container, false);
        eventBoardListView = (ListView) rootView.findViewById(R.id.event_board_list_view);
        populateEventBoardList();

        return rootView;
    }

    public void populateEventBoardList() {
        final ArrayList<Event> favoritesList = new ArrayList<>(); // List of favorited events.
        final ArrayList<String> favoritedEvents = new ArrayList<>(); // List of eventIds
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserRef = database.getReference().child("users").child(currentUser.getUid());

        /*Start of Event Listener for Users Favorited Events*/

        valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final List<String> eventIdsToRemove = new ArrayList<>();
                favoritesList.clear();
                favoritedEvents.clear();

                System.out.println("Fresh favorites list: " + favoritesList.size());
                System.out.println("Fresh favorited events list: " + favoritedEvents.size());

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int size = 0;
                for (DataSnapshot child : children) {
                    String eventId = child.getValue(String.class);
                    favoritedEvents.add(eventId);
                    size++;
                    if (size == dataSnapshot.getChildrenCount()) {
                        favEventCounterReset();
                        System.out.println("(Inside) Size of Favorited Events List: " + favoritedEvents.size());

                        for(String eventKey: favoritedEvents) {
                            System.out.println("Event: " + eventKey);
                        }

                        final String eventIdKey = child.getKey();

                        for (String eventID : favoritedEvents) {
                            DatabaseReference eventRef= database.getReference().child("events").child(eventID);

                            eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Event favedEvent = dataSnapshot.getValue(Event.class);
                                    //if (favedEvent == null) {
                                        //eventIdsToRemove.add(eventIdKey);

                                        /*DatabaseReference delRef = database.getReference()
                                                .child("users")
                                                .child(currentUser.getUid())
                                                .child("favorites");*/

                                        //System.out.println("Remove list size: " + eventIdsToRemove.size());
                                        //delRef.child(eventIdKey).removeValue();
                                   // } else {
                                        favoritesList.add(favedEvent);
                                        favEventCounter();
                                        System.out.println("Iteration Count: " + count);
                                        System.out.println("Favorited Event Count: " + favoritedEvents.size());
                                        if (count == favoritedEvents.size()) {
                                            System.out.println("Size of Favorites List: " + favoritesList.size());
                                            ebAdapter.notifyDataSetChanged();
                                        }
                                   // }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Do Nothing.
                                }

                            });
                        }
                    }
                }

                Collections.sort(favoritesList, new Comparator<Event>() {
                    @Override
                    public int compare(Event e1, Event e2) {
                        if (e1.getStart_time() < e2.getStart_time())
                            return 1;
                        if (e1.getStart_time() > e2.getStart_time())
                            return -1;
                        return 0;
                    }
                });
                ebAdapter = new EventBoardAdapter(getActivity(), R.layout.event_board_row, favoritesList);
                eventBoardListView.setAdapter(ebAdapter);
                eventBoardListView.setItemsCanFocus(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        currentUserRef.child("favorites").addValueEventListener(valueEventListener);
    }

    public void favEventCounterReset() {
        count = 0;
    }

    public void favEventCounter() {
        count++;
    }

    /* EVENT BOARD ADAPTER HERE ... */
    /* EVENT BOARD ADAPTER HERE ... */
    /* EVENT BOARD ADAPTER HERE ... */
    /* EVENT BOARD ADAPTER HERE ... */
    /* EVENT BOARD ADAPTER HERE ... */


    public class EventBoardAdapter extends ArrayAdapter<Event> {
        int res;

        public EventBoardAdapter(Context ctx, int res, List<Event> items)  {
            super(ctx, res, items);
            this.res = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout eventBoardView;
            final Event ebEvent = getItem(position);

            if (convertView == null) {
                eventBoardView = new LinearLayout(getActivity());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(inflater);
                vi.inflate(res, eventBoardView, true);
            } else {
                eventBoardView = (LinearLayout) convertView;
            }

            eventBoardView.setClickable(true);
            eventBoardView.setFocusable(true);
            eventBoardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle data = new Bundle();
                    data.putString("eventID", ebEvent.getId());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Fragment currentFragment = new EventPageFragment();
                    currentFragment.setArguments(data);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, currentFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            eventBoardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventId", ebEvent.getId());
                    DialogFragment dialog = new ConfirmDialogFragment();
                    dialog.setArguments(bundle);
                    dialog.show(getFragmentManager(), "hooplah!");
                    return true;
                }
            });


            String eventName = ebEvent.getName();

            // Simplified Event Board Row Filling:
            String event_time = ebEvent.getDateString() + " @ " + ebEvent.getStartTimeString();
            String event_time_notification = "Event: " + "Upcoming!";
            String event_description = ebEvent.getDetails();

            TextView eventBoardName = (TextView) eventBoardView.findViewById(R.id.event_title);
            TextView eventTime = (TextView) eventBoardView.findViewById(R.id.event_time);
            TextView eventNotification = (TextView) eventBoardView.findViewById(R.id.event_time_notification);
            TextView eventDescription = (TextView) eventBoardView.findViewById(R.id.event_board_description);
            final ImageView eventBoardImageView = (ImageView) eventBoardView.findViewById(R.id.event_board_image);
            final ImageView eventBoardIconView = (ImageView) eventBoardView.findViewById(R.id.event_board_icon);

            eventBoardName.setText(eventName);
            eventTime.setText(event_time);
            eventNotification.setText(event_time_notification);
            eventDescription.setText(event_description);

            // Reference to an image file in Firebase Storage
            if (ebEvent.getImgId() != null && !ebEvent.getImgId().equals("")) {
                storageRef.child(ebEvent.getImgId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getContext()).load(uri).fit().centerCrop().into(eventBoardImageView);
                    }
                });
            }

            // Stuff

            String hostOrgId = ebEvent.getHostOrg();
            DatabaseReference myRef = database.getReference();
            DatabaseReference hostOrg = myRef.child("users").child(hostOrgId);

            if (hostOrg.child("imagePath")!= null) {
                hostOrg.child("imagePath").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String fp = snapshot.getValue(String.class);
                        storageRef.child(fp).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).fit().centerCrop().into(eventBoardIconView );
                            }
                        });

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Do Nothing
                    }
                });
            } else {

                storageRef.child("/EventPhotos/default.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getContext()).load(uri).fit().centerCrop().into(eventBoardIconView );
                    }
                });

            }



            return eventBoardView;
        }
    }
}
