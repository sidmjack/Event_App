package com.uima.event_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sidneyjackson on 4/17/17.
 */

public class EventMapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private View mView;
    private GoogleMap mGoogleMap;
    FirebaseAdapter fb;

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
        fb = new FirebaseAdapter(getContext());
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
        System.out.print("Pins being set  ");
        fb.getAllEvents();
        LatLng baltimore = new LatLng(39.2904, -76.6122);
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(baltimore).title("@string/baltimore"));
        CameraPosition Baltimore = CameraPosition.builder().target(baltimore).zoom(12).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Baltimore));
        List<Event> live_events = fb.getLocalEvents();
        System.out.println(live_events.size());
        List<LatLng> map_pins = new ArrayList<LatLng>();
        List<String> pin_names = new ArrayList<String>();
        for (Event curr : live_events) {
            Integer lat = new Integer(curr.getLatutude());
            Integer log = new Integer(curr.getLongitude());
            LatLng temp = new LatLng(lat, log);
            map_pins.add(temp);
            pin_names.add(curr.getName());
        }
        for (int i = 0; i < pin_names.size(); i++) {
            mGoogleMap.addMarker(new MarkerOptions().position(map_pins.get(i)).title(pin_names.get(i)));
        }
    }

}
