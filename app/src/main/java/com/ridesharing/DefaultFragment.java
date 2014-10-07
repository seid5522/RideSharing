package com.ridesharing;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesharing.Service.LocationService;

public class DefaultFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "position";
    private static final String NAME = "name";

    private int position;
    private String name;
    private Location location;
    private SupportMapFragment mapFragment;
    private Activity MainActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @param name Parameter 2.
     * @return A new instance of fragment DefaultFragment.
     */
    public static DefaultFragment newInstance(int position, String name) {
        DefaultFragment fragment = new DefaultFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }
    public DefaultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
            name = getArguments().getString(NAME);
        }
        mapFragment = new com.google.android.gms.maps.SupportMapFragment(){
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                GoogleMap map = mapFragment.getMap();
                if (map != null) {
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(
                            new MarkerOptions()
                                    .position(latlng)
                                    .title("Current Location")
                    ).showInfoWindow();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Toast.makeText(MainActivity, "Click Info Window", Toast.LENGTH_SHORT).show();
                        }
                    } );
                }
            }
        };
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.Main_Map, mapFragment)
                .commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setUpMapIfNeeded();
            }
        }, 2000);
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity = activity;
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(POSITION));
        location =  LocationService.getCurrentLocation(activity);
        Log.v("latitude", Double.toString(location.getLatitude()));
        Log.v("longitude", Double.toString(location.getLongitude()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call { #setUpMap()} once when { #mMap} is not null.
     * <p>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    /*
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = mapFragment
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Current Location"));
            }
        }
    }
    */

}
