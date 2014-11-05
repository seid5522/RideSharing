package com.ridesharing.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesharing.R;
import com.ridesharing.Service.LocationService;
import com.ridesharing.Service.LocationServiceImpl_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

@EFragment(R.layout.fragment_default)
public class DefaultFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "position";
    private static final String NAME = "name";

    private int position;
    private String name;
    private SupportMapFragment mapFragment;
    private MainActivity mainActivity;
    private GoogleMap map;

    @ViewById(R.id.maploading_progress)
    ProgressBar progressBar;
    @ViewById(R.id.Main_Map)
    FrameLayout mainMap;
    @ViewById(R.id.infoCard)
    CardView cardView;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection;
    private Location location;
    protected LocationService locationService;
    protected boolean mlocationServiceBound;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @param name Parameter 2.
     * @return A new instance of fragment DefaultFragment.
     */
    public static DefaultFragment newInstance(int position, String name) {
        DefaultFragment_ fragment = new DefaultFragment_();
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

        mConnection= new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                LocationServiceImpl_.LocationServiceBinder binder = (LocationServiceImpl_.LocationServiceBinder) service;
                locationService = binder.getService();
                mlocationServiceBound = true;
                showMainMap();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mlocationServiceBound = false;
            }
        };
    }

    @AfterViews
    void initView() {
        showProgress(true);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mapFragment == null)
            startLocationService();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationService();
    }

    public void startLocationService(){
        Intent intent = new Intent(mainActivity, LocationServiceImpl_.class);
        mainActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopLocationService(){
        // Unbind from the service
        if (mlocationServiceBound) {
            mainActivity.unbindService(mConnection);
            mlocationServiceBound = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainMap.setVisibility(show ? View.GONE : View.VISIBLE);
            mainMap.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainMap.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }else{
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mainMap.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @UiThread
    protected void showMainMap(){
        mapFragment = new com.google.android.gms.maps.SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                map = mapFragment.getMap();
                if (map != null) {
                    if(locationService.getLastBestLocation() != null){
                        location = locationService.getLastBestLocation();
                    }else if(locationService.getLastLocation() != null){
                        location = locationService.getLastLocation();
                    }else{
                        location = locationService.getLastKnowLocation();
                    }
                    if(location != null){
                        addMapMarker("Current Location", new LatLng(location.getLatitude(), location.getLongitude()));
                        stopLocationService();
                        Address addr = LocationServiceImpl_.getLocationFromAddress(getActivity(), "5600 City Ave, Philadelphia, PA");
                        addMapMarker("School", new LatLng(addr.getLatitude(), addr.getLongitude()));
                    }
                }
            }
        };
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.Main_Map, mapFragment)
                .commit();
        showProgress(false);
    }

    @UiThread
    protected void addMapMarker(String name, LatLng latlng){
        map.addMarker(
                new MarkerOptions()
                        .position(latlng)
                        .title(name)
        ).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(com.google.android.gms.maps.model.LatLng latLng){
                cardView.setVisibility(View.GONE);
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Projection projection = map.getProjection();
                LatLng markerLocation = marker.getPosition();
                Point screenPosition = projection.toScreenLocation(markerLocation);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                int maxWidth = mainMap.getWidth();
                int maxHeight = mainMap.getHeight();

                int width = 400;
                int height = 300;
                int left = 0, top = 0, right = 0, bottom = 0;
                if(screenPosition.x - width < 0){//left
                    left = screenPosition.x + 50;
                }
                if( screenPosition.y + height> maxHeight){//bottom
                    top = maxHeight - height - 50;
                }
                if(screenPosition.y - height < 0 ){//top
                    top = screenPosition.y + 50;
                }
                if(screenPosition.x + width > maxWidth){//right
                    left = maxWidth - width - 50;
                }

                if(left == 0) {
                    left = screenPosition.x - 200;
                }
                if(top == 0) {
                    top = screenPosition.y - 300;
                }

                right = maxWidth - left - width;
                bottom = maxHeight - top - height;

                params.setMargins(left, top, right, bottom);
                //Create a Card
                Card card = new Card(getActivity());

                //Create a CardHeader
                CardHeader header = new CardHeader(getActivity());
                card.setTitle("My Title");
                //Add Header to card
                card.addCardHeader(header);
                cardView.setCard(card);
                cardView.setLayoutParams(params);
                cardView.setVisibility(View.VISIBLE);
                //setting animation
                Animation animation =  AnimationUtils.loadAnimation(getActivity(), R.anim.card_alpha);
                cardView.setAnimation(animation);
            }
        } );
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
        mainActivity.onSectionAttached(
                getArguments().getInt(POSITION));


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }


}