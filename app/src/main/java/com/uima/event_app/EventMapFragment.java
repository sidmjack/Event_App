package com.uima.event_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidneyjackson on 4/17/17.
 */

public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private View mView;
    private FirebaseDatabase database;
    protected static ManageEventsAdapter adapter;
    private DatabaseReference myRef;
    final List<LatLng> map_pins = new ArrayList<LatLng>();
    final List<String> pin_names = new ArrayList<String >();

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
        googleMap.addMarker(new MarkerOptions().position(baltimore).title("@string/baltimore"));
        CameraPosition Baltimore = CameraPosition.builder().target(baltimore).zoom(12).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Baltimore));
        myRef = database.getReference().child("events");
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final String title = marker.getTitle();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            Event value = child.getValue(Event.class);
                            if (value.getName().equalsIgnoreCase(title)) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                Fragment currentFragment = new EventPageFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, currentFragment)
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return false;
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
                    mGoogleMap.addMarker(new MarkerOptions().position(itemp).title(value.getName()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
