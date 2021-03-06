package com.ridesharing.ui.main;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.DialogInterface;
import android.util.DisplayMetrics;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ridesharing.Entity.User;
import com.ridesharing.Entity.Wish;
import com.ridesharing.Service.AuthenticationService;
import com.ridesharing.Service.LocationService;
import com.ridesharing.Service.SearchPlaceService;
import com.ridesharing.Service.UserService;
import com.ridesharing.R;
import com.ridesharing.Service.WishService;
import com.ridesharing.ui.Inject.InjectActionBarActivity;
import com.ridesharing.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends InjectActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DestinationFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //@FragmentById(R.id.navigation_drawer)
    protected NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Inject UserService userService;
    @Inject AuthenticationService authService;
    @Inject SearchPlaceService searchPlaceService;
    protected LocationService locationService;
    @Inject
    WishService wishService;



    GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if(userService.getUser() == null){
            Intent login = new Intent(getApplicationContext(), LoginActivity_.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
            Log.v("activity switched","switch to login activity");
        }
    }

    @AfterViews
    public void initial(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if(userService.getUser() != null){

            mTitle = getTitle();

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));

            // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                String regid = getRegistrationId(getApplicationContext());

                if (regid.isEmpty()) {
                    registerInBackground(regid);
                }
            } else {
                Log.i(TAG, "No valid Google Play Services APK found.");
            }
        }
    }

    public LocationService getLocationService() {
        return locationService;
    }

    @Override
    public void onResume(){
        super.onResume();
        authUser();
        checkPlayServices();
    }

    @Background
    public void authUser(){
        boolean isAuth = false;
        try {
            isAuth = authService.isAuthorized();
        }
        catch (Exception e){
            Log.e("com.ridesharing.mainActivity", e.toString());
        }
        if(!isAuth){
            Intent login = new Intent(getApplicationContext(), LoginActivity_.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();
        }
    }

    public void showError( String errorMessage){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setNegativeButton("OK", null)
                .show();
    }


    @Background
    public void registerInBackground(String regid){
        String msg = "";
        Context context = getApplicationContext();
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;

            // You should send the registration ID to your server over HTTP, so it
            // can use GCM/HTTP or CCS to send messages to your app.
            sendRegistrationIdToBackend(regid);

            // For this demo: we don't need to send it because the device will send
            // upstream messages to a server that echo back the message using the
            // 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(context, regid);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        Log.v(TAG, msg);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    @Background
    public void sendRegistrationIdToBackend(String regid) {
        User user =  userService.getUser();
        user.setDeviceId(regid);
        userService.updateDevice(user);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        hideKeyboard();
        if(position == 0){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DefaultFragment.newInstance(position + 1, getString(R.string.homePage)))
                    .commit();
        } else if(position == 1){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DestinationFragment.newInstance(position + 1, getString(R.string.destinationPage)))
                    .commit();
        }else if(position == 2){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, RequestFragment.newInstance(position + 1, getString(R.string.requestPage)))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void quickSearch(String address){
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment instanceof DefaultFragment){
            DefaultFragment defaultFragment = (DefaultFragment)fragment;
            defaultFragment.searchWishListByAddr(address);
            //defaultFragment.addMapMarker("Destination", new LatLng(addr.getLatitude(), addr.getLongitude()));
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MainActivity activity = this;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final Menu currentmenu = menu;
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    hideKeyboard();
                    final SearchView search = (SearchView) currentmenu.findItem(R.id.action_search).getActionView();
                    search.onActionViewCollapsed();
                    quickSearch(s);
                    Toast.makeText(activity, getString(R.string.filterFor) + s, Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (s.equals("")) {
                        final SearchView search = (SearchView) currentmenu.findItem(R.id.action_search).getActionView();
                        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "text"});
                        search.setSuggestionsAdapter(new SearchPlaceAdapter(activity, cursor, null, search));
                    } else {
                        new SetupPlaceAutoCompleteTask(s).execute(null, null);
                    }
                    return true;
                }

                class SetupPlaceAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {
                    private String input;

                    SetupPlaceAutoCompleteTask(String input) {
                        this.input = input;
                    }

                    @Override
                    protected List<String> doInBackground(Void... voids) {
                        return searchPlaceService.autocomplete(input);
                    }

                    @Override
                    protected void onPostExecute(List<String> items) {
                        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "text"});

                        for (int i = 0; i < items.size(); i++) {
                            cursor.addRow(new Object[]{(Object) i, items.get(i)});
                        }
                        // SearchView
                        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

                        final SearchView search = (SearchView) currentmenu.findItem(R.id.action_search).getActionView();
                        search.setSuggestionsAdapter(new SearchPlaceAdapter(activity, cursor, items, search));
                    }
                }
            });
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
           // case R.id.action_settings:
           //     return true;
            case R.id.action_changelanguage:
                /*
                new AlertDialog.Builder(this)
                        .setTitle("标题")
                        .setMessage(config.locale.toString())
                        .setPositiveButton("确定", null)
                        .show();
                new AlertDialog.Builder(this)
                        .setTitle("标题")
                        .setMessage(Locale.US.toString())
                        .setPositiveButton("确定", null)
                        .show();
                        */
                new AlertDialog.Builder(this)
                        .setTitle(R.string.Select_Language)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[] {getText(R.string.Lan_English).toString(), getText(R.string.Lan_Chinese).toString()}, 0,
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        Resources resources = getResources();//获得res资源对象

                                        Configuration config = resources.getConfiguration();//获得设置对象

                                        DisplayMetrics dm = resources .getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
                                        switch(which)
                                        {
                                            case 0://English
                                                config.locale = Locale.ENGLISH;
                                               break;
                                            case 1://Chinese
                                                config.locale = Locale.SIMPLIFIED_CHINESE;
                                                break;
                                            default:


                                        }
                                        onConfigurationChanged(config);
                                        //resources.updateConfiguration(config, dm);
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setNegativeButton(R.string.Cancel, null)
                        .show();
                /*
                if (config.locale == Locale.US)
                    config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
                else
                    config.locale = Locale.US; //English
                    */

/*
                FragmentManager fragmentManager = getSupportFragmentManager();
                DefaultFragment fragment = DefaultFragment.newInstance(1, getString(R.string.homePage));
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
*/

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        //return super.onOptionsItemSelected(item);
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        //setContentView(R.layout.activity_main);
        restartActivity();
        setTitle(R.string.app_name);

        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {
            Toast.makeText(this, getText(R.string.Lan_English), Toast.LENGTH_SHORT).show();
        } else if (newConfig.locale == Locale.SIMPLIFIED_CHINESE){
            Toast.makeText(this, getText(R.string.simplified_chinese), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Background
    @Override
    public void processAdvancedSearch(Wish wish){
        ArrayList<Wish> lists = wishService.searchAndAdd(wish);
        for(Wish otherWish: lists){
            if(!userService.getUserTables().containsKey(otherWish.getUid())){
                User user = userService.fetchOtherInfo(otherWish.getUid());
                userService.getUserTables().put(user.getId(), user);
            }
            User user = userService.getUserTables().get(otherWish.getUid());
            wishService.getWishHashtable().put(user.getUsername(), otherWish);
        }
        //back to home page
        backToHomePage(lists);
    }

    @UiThread
    public void backToHomePage(ArrayList<Wish> lists){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DefaultFragment fragment = DefaultFragment.newInstance(1, getString(R.string.homePage));
        fragment.setBackFromSearch(true);
        fragment.setSearchlists(lists);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}
