package com.ridesharing.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesharing.Entity.ELocation;
import com.ridesharing.Entity.StatusType;
import com.ridesharing.Entity.User;
import com.ridesharing.Entity.Wish;
import com.ridesharing.Entity.WishType;
import com.ridesharing.R;
import com.ridesharing.Service.LocationService;
import com.ridesharing.Service.LocationServiceImpl;
import com.ridesharing.Service.LocationServiceImpl_;
import com.ridesharing.Service.UserService;
import com.ridesharing.Service.WishService;
import com.ridesharing.ui.Inject.InjectFragment;
import com.ridesharing.ui.cards.CustomerDetailCard;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

@EFragment(R.layout.fragment_default)
public class DefaultFragment extends InjectFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "position";
    private static final String NAME = "name";

    private int position;
    private String name;
    protected CustomGoogleMap mapFragment;
    private MainActivity mainActivity;

    @ViewById(R.id.maploading_progress)
    ProgressBar progressBar;
    @ViewById(R.id.Main_Map)
    FrameLayout mainMap;
    @ViewById(R.id.infoCard)
    CardView cardView;

    PopupWindow mPopupWindow;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection;
    protected Location location;
    protected LocationService locationService;
    protected boolean mlocationServiceBound;
    protected boolean backFromSearch;
    protected ArrayList<Wish> searchlists;

    @Inject
    UserService userService;
    @Inject
    WishService wishService;

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
        fragment.backFromSearch = false;
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

        View popupView = getActivity().getLayoutInflater().inflate(R.layout.layout_marker_popup_window, null);
        mPopupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        mConnection= new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                LocationServiceImpl_.LocationServiceBinder binder = (LocationServiceImpl_.LocationServiceBinder) service;
                locationService = binder.getService();
                //bind with location service at main activity
                mainActivity.locationService = locationService;
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

    public void setBackFromSearch(boolean backFromSearch) {
        this.backFromSearch = backFromSearch;
    }

    public void setSearchlists(ArrayList<Wish> searchlists) {
        this.searchlists = searchlists;
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
        mapFragment = CustomGoogleMap.newInstance(this);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.Main_Map, mapFragment)
                .commit();
        showProgress(false);
        stopLocationService();
    }

    @Background
    public void searchWishListByAddr(String toAddress){
        Address from = LocationServiceImpl_.getLocationFromLatLng(getActivity(), location.getLatitude(), location.getLongitude());
        Address to = LocationServiceImpl_.getLocationFromAddress(getActivity(), toAddress);
        int uid = userService.getUser().getId();
        WishType type = userService.isDriver()? WishType.Offer: WishType.Request;
        Wish wish = new Wish(uid, from.getAddressLine(0), from.getAddressLine(1), from.getPostalCode(), from.getLatitude(), from.getLongitude(),
                to.getAddressLine(0), to.getAddressLine(1), to.getPostalCode(), to.getLatitude(), to.getLongitude(), new Date(System.currentTimeMillis()), 0, type, 0, StatusType.INITIAL);
        ArrayList<Wish> lists = wishService.search(wish);
        for(Wish otherWish: lists){
            if(!userService.getUserTables().containsKey(otherWish.getUid())){
                User user = userService.fetchOtherInfo(otherWish.getUid());
                userService.getUserTables().put(user.getId(), user);
            }
            User user = userService.getUserTables().get(otherWish.getUid());
            wishService.getWishHashtable().put(user.getUsername(), otherWish);
        }
        showMarkerOnMap(lists);
    }

    @UiThread
    public void showMarkerOnMap(ArrayList<Wish> lists){
        final GoogleMap map = mapFragment.getMap();
        if(map == null){
            return;
        }
        map.clear();

        LatLng clatlng = new LatLng(location.getLatitude(), location.getLongitude());

        Marker currentMarker = map.addMarker(
                new MarkerOptions()
                        .position(clatlng)
                        .title(getText(R.string.currentLocation).toString())
        );
        currentMarker.showInfoWindow();

        if(lists.size() > 0){
            Wish w = lists.get(0);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(clatlng, 14));
        }
        for(Wish otherWish: lists){
            LatLng latLng = new LatLng(otherWish.getFromlat(), otherWish.getFromlng());
            String name = userService.getUserTables().get(otherWish.getUid()).getUsername();
            float hue = BitmapDescriptorFactory.HUE_BLUE;//rider
            if(otherWish.getType() == WishType.Offer){
                hue = BitmapDescriptorFactory.HUE_GREEN;//driver
            }
            map.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .title(name)
                            .icon(BitmapDescriptorFactory.defaultMarker(hue))
            );
        }
        map.setInfoWindowAdapter(new MarkerWindowAdapter(wishService, userService,this));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String title = marker.getTitle();
                if (title.equals(getText(R.string.currentLocation))) {
                    return;
                }
                Wish wish = wishService.getWishHashtable().get(title);
                if (wish != null) {
                    User user = userService.getUserTables().get(wish.getUid());
                    //Create a Card
                    CustomerDetailCard card = new CustomerDetailCard(getActivity(), wish, user, userService.getUser(), userService.isDriver(), mPopupWindow);
                    CardView detailcard = (CardView) mPopupWindow.getContentView().findViewById(R.id.detailCard);
                    if (detailcard.getCard() == null) {
                        detailcard.setCard(card);
                    } else {
                        detailcard.replaceCard(card);
                    }
                    //setting animation
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.card_alpha);
                    detailcard.setAnimation(animation);
                    mPopupWindow.showAtLocation(mainMap, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                }
            }
        });
        Toast.makeText(getActivity(), String.format("find %d record(s).", lists.size()), Toast.LENGTH_LONG).show();
    }

    @Background
    public void loadWishList(){
        ArrayList<Wish> lists = null;
        if(backFromSearch){
            lists = this.searchlists;
        }else{
            Wish wish = new Wish();
            wish.setFromlat(location.getLatitude());
            wish.setFromlng(location.getLongitude());
            wish.setType(WishType.Request);
            lists = wishService.fetchAll(wish, 10);
        }
        if(lists == null){
            return;
        }
        for(Wish otherWish: lists){
            if(!userService.getUserTables().containsKey(otherWish.getUid())){
                User user = userService.fetchOtherInfo(otherWish.getUid());
                userService.getUserTables().put(user.getId(), user);
            }
            User user = userService.getUserTables().get(otherWish.getUid());
            wishService.getWishHashtable().put(user.getUsername(), otherWish);
        }
        showMarkerOnMap(lists);
    }

    @UiThread
    protected void addCurrentLocationToMap(){
        //decide location
        if(locationService.getLastBestLocation() != null){
            location = locationService.getLastBestLocation();
        }else if(locationService.getLastLocation() != null){
            location = locationService.getLastLocation();
        }else{
            location = locationService.getLastKnowLocation();
        }

        LatLng clatlng = new LatLng(location.getLatitude(), location.getLongitude());
        GoogleMap map = mapFragment.getMap();
        /*
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color,
        // stroke width, size
        Paint color = new Paint();
        color.setTextSize(10);
        color.setColor(Color.RED);
        color.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        //modify canvas
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.passenger);
        bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
        bitmap = getCroppedBitmap(bitmap, 80);
        canvas1.drawBitmap(bitmap, 0, 0, null);
        canvas1.drawText("Michael", 20, 70, color);
        */
/*
 from internet (You also must download the image from an background thread (you could use AsyncTask for that)
 URL url = new URL(user_image_url);
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setDoInput(true);
conn.connect();
InputStream is = conn.getInputStream();
bmImg = BitmapFactory.decodeStream(is); */
        Marker currentMarker = map.addMarker(
                new MarkerOptions()
                        .position(clatlng)
                        .title(getText(R.string.currentLocation).toString())
        );
       // currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
        currentMarker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(clatlng, 15));
        loadWishList();
        /*
        if(mainActivity.userService.isDriver()) {
            Address addr = LocationServiceImpl_.getLocationFromAddress(getActivity(), "5600 City Ave, Philadelphia, PA");
            addMapMarker("School", new LatLng(addr.getLatitude(), addr.getLongitude()));
        }
        */
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);


        return output;
    }

    @UiThread
    protected void addMapMarker(String name, LatLng latlng) {
        final GoogleMap map = mapFragment.getMap();
        map.addMarker(
                new MarkerOptions()
                        .position(latlng)
                        .title(name)
        );
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(com.google.android.gms.maps.model.LatLng latLng) {
                cardView.setVisibility(View.GONE);
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Projection projection = map.getProjection();
                LatLng markerLocation = marker.getPosition();

                //Create a Card
                Card card = new Card(getActivity());

                //Create a CardHeader
                CardHeader header = new CardHeader(getActivity());
                card.setTitle("Loading...");

                //Add Header to card
                card.addCardHeader(header);
                cardView.setCard(card);
                cardView.setLayoutParams(calcBestPosition(projection, markerLocation));
                cardView.setVisibility(View.VISIBLE);
                //setting animation
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.card_alpha);
                cardView.setAnimation(animation);
            }
        });
    }

    private RelativeLayout.LayoutParams calcBestPosition(Projection projection, LatLng markerLocation){
        Point screenPosition = projection.toScreenLocation(markerLocation);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int maxWidth = mapFragment.getView().getWidth();
        int maxHeight = mapFragment.getView().getHeight();

        int width = 400;
        int height = 300;
        int left = 0, top = 0, right = 0, bottom = 0;
        if (screenPosition.x - width < 0) {//left
            left = screenPosition.x + 50;
        }
        if (screenPosition.y + height > maxHeight) {//bottom
            top = maxHeight - height - 50;
        }
        if (screenPosition.y - height < 0) {//top
            top = screenPosition.y + 50;
        }
        if (screenPosition.x + width > maxWidth) {//right
            left = maxWidth - width - 50;
        }

        if (left == 0) {
            left = screenPosition.x - 200;
        }
        if (top == 0) {
            top = screenPosition.y - 300;
        }

        right = maxWidth - left - width;
        bottom = maxHeight - top - height;

        params.setMargins(left, top, right, bottom);
        return params;
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
