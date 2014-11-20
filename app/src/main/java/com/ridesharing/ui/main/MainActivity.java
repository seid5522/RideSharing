package com.ridesharing.ui.main;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.ridesharing.Service.AuthenticationService;
import com.ridesharing.Service.SearchPlaceService;
import com.ridesharing.Service.UserService;
import com.ridesharing.R;
import com.ridesharing.ui.Inject.InjectActionBarActivity;
import com.ridesharing.ui.login.LoginActivity_;
import com.ridesharing.ui.user.destinationFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends InjectActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, destinationFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    @FragmentById(R.id.navigation_drawer)
    protected NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    @Inject UserService userService;
    @Inject AuthenticationService authService;
    @Inject SearchPlaceService searchPlaceService;


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
        if(userService.getUser() != null){

            mTitle = getTitle();

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        authUser();
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(position == 0){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DefaultFragment.newInstance(position + 1, "Home Page"))
                    .commit();
        } else if(position == 1){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, destinationFragment.newInstance(position + 1, "Destination"))
                    .commit();
        }else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
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
                    Toast.makeText(activity, "filter for: " + s, Toast.LENGTH_LONG).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(s.equals("")){
                        final SearchView search = (SearchView) currentmenu.findItem(R.id.action_search).getActionView();
                        MatrixCursor cursor = new MatrixCursor(new String[] { "_id", "text" });
                        search.setSuggestionsAdapter(new SearchPlaceAdapter(activity, cursor, null, search));
                    }else {
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
                        MatrixCursor cursor = new MatrixCursor(new String[] { "_id", "text" });

                         for (int i = 0; i < items.size(); i++) {
                              cursor.addRow(new Object[]{ (Object)i, items.get(i)});
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
