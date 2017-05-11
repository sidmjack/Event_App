package com.uima.event_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * EventMapFragment
 * Displays markers using Google Maps.  Each marker is linked to an event taken from
 * Firebase which it references with a HashMap that stores a markerID as a key and
 * its corresponding event.
 *
 * If marker is clicked, see event page details.
 * If a marker is not clicked, then don't see event page, but create an event (if part of
 * organization).
 */
public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private View mView;
    private FirebaseDatabase database;
    protected static ManageEventsAdapter adapter;
    private DatabaseReference myRef;
    final List<LatLng> map_pins = new ArrayList<LatLng>();
    final List<String> pin_names = new ArrayList<String >();
    private HashMap<String, Event> markerEventMap = new HashMap<>();

    UserProfile user;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference currentUserRef;

    public EventMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("events");
        myRef = database.getReference().child("events");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserRef = database.getReference().child("users").child(currentUser.getUid());
    }

    @Override
    public void onStart() {
        super.onStart();
        getUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_event_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //System.out.print("Pins being set  ");
        LatLng baltimore = new LatLng(39.2904, -76.6122);
        MapsInitializer.initialize(getContext());
        final GoogleMap mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition Baltimore = CameraPosition.builder().target(baltimore).zoom(12).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Baltimore));

        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String lat = latLng.latitude + "";
                String log = latLng.longitude + "";
                System.out.println(lat + " " + log);
                TextView latText = (TextView) getActivity().findViewById(R.id.event_latitude);
                TextView logText = (TextView) getActivity().findViewById(R.id.event_longitude);
                Intent intent = new Intent(getActivity(), CreateEventActivity.class);
                intent.putExtra("duplicate", false);
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", log);
                startActivity(intent);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Event value = child.getValue(Event.class);
                    System.out.println(value.getName());
                    String ilat = value.getLatutude();
                    String ilog = value.getLongitude();
                    Double dlat, dlog;
                    if (ilat.equals("")) {
                        dlat = 39.0;
                    } else {
                        dlat = Double.valueOf(ilat);
                    }
                    if (ilog.equals("")) {
                        dlog = -76.0;
                    } else {
                        dlog = Double.valueOf(ilog);
                    }
                    LatLng itemp = new LatLng(dlat, dlog);
                    MarkerOptions newMarkerOptions = new MarkerOptions();
                    newMarkerOptions.position(itemp);
                    newMarkerOptions.title(value.getName());
                    Marker marker = mGoogleMap.addMarker(newMarkerOptions);
                    markerEventMap.put(marker.getId(), value);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event event = markerEventMap.get(marker.getId());
                Bundle data = new Bundle();
                data.putString("eventID", event.getId());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment currentFragment = new EventPageFragment();
                currentFragment.setArguments(data);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, currentFragment)
                        .commit();
                return true;
            }
        });
    }

    private void getUser() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        currentUserRef.addValueEventListener(userListener);
    }



}
