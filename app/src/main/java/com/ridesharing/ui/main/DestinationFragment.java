package com.ridesharing.ui.main;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.ridesharing.Entity.Wish;
import com.ridesharing.Entity.WishType;
import com.ridesharing.R;
import com.ridesharing.Service.LocationService;
import com.ridesharing.Service.LocationServiceImpl;
import com.ridesharing.Utility.DataProcess;
import com.ridesharing.ui.main.DestinationFragment_;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;


@EFragment(R.layout.fragment_destination)
public class DestinationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView fromDest;
    private AutoCompleteTextView toDest;
    private OnFragmentInteractionListener mListener;
    private String currLoc;
    private String destLoc;
    private MainActivity activity;

    @ViewById(R.id.beginTimedatePicker)
    DatePicker beginTimedatePicker;
    @ViewById(R.id.beginTimePicker)
    TimePicker beginTimePicker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment destinationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DestinationFragment newInstance(Integer param1, String param2) {
        DestinationFragment fragment = new DestinationFragment_();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public DestinationFragment() {
        // Required empty public constructor
    }


    public void onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_destination, container, false);
        Context context = super.getActivity();


        final Spinner spinner = (Spinner) view.findViewById(R.id.options_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.options_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(this);

        fromDest = (AutoCompleteTextView) view.findViewById(R.id.fromDest);
        toDest = (AutoCompleteTextView) view.findViewById(R.id.toDest);
        //final EditText date = (EditText) view.findViewById(R.id.dateDest);
        //final EditText time = (EditText) view.findViewById(R.id.timeDest);
        //final Switch amToggle = (Switch) view.findViewById(R.id.amToggle);
        final EditText numPassengers = (EditText) view.findViewById(R.id.passengersDest);

        fromDest.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.fragment_destination));

        fromDest.setOnClickListener(this);
        fromDest.requestFocus();

        fromDest.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.list_item_autocomplete));

        toDest.setOnClickListener(this);
        toDest.requestFocus();
        toDest.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.list_item_autocomplete));

        Button sbm = (Button) view.findViewById(R.id.submitDest);
        sbm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Wish wish = new Wish();
                WishType type = WishType.Request;
                type = WishType.Offer;
                if(spinner.getSelectedItemPosition() == 0){
                    type = WishType.Request;
                }
                //set type
                wish.setType(type);
                //set time
                Date time =  DataProcess.getDateFromDatePickerAndTimePicer(beginTimedatePicker, beginTimePicker);
                wish.setStartTime(time);

                String fromDestAddr = fromDest.getText().toString();
                if(fromDestAddr.equals(getText(R.string.currentLocation))){
                    //decide location
                    LocationService locationService = activity.getLocationService();
                    Location location = null;
                    if(activity.getLocationService().getLastBestLocation() != null){
                        location = locationService.getLastBestLocation();
                    }else if(locationService.getLastLocation() != null){
                        location = locationService.getLastLocation();
                    }else{
                        location = locationService.getLastKnowLocation();
                    }
                    Address address =  LocationServiceImpl.getLocationFromLatLng(activity, location.getLatitude(),location.getLongitude());
                    wish.setFromlng(location.getLongitude());
                    wish.setFromlat(location.getLatitude());
                    wish.setFromAddr(address.getAddressLine(0));
                }else{
                    Address address = LocationServiceImpl.getLocationFromAddress(activity, fromDestAddr);
                    wish.setFromAddr(address.getAddressLine(0));
                    wish.setFromlat(address.getLatitude());
                    wish.setFromlng(address.getLongitude());
                }

                String toDestAddr = toDest.getText().toString();
                Address address = LocationServiceImpl.getLocationFromAddress(activity, toDestAddr);
                wish.setToAddr(toDest.getText().toString());
                wish.setTolat(address.getLatitude());
                wish.setTolng(address.getLongitude());
                wish.setNumOfPassenger(Integer.parseInt(numPassengers.getText().toString()));
                mListener.processAdvancedSearch(wish);
                /*
                boolean toggle = amToggle.isChecked();
                String toggleText;
                if (toggle==true){
                    toggleText = amToggle.getTextOn().toString();
                }
                else{
                    toggleText = amToggle.getTextOff().toString();
                }*/
                //******  HERE's the PROBLEM  ********
                //Toast.makeText(getActivity(),spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(),toDest.getText(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(),fromDest.getText(), Toast.LENGTH_LONG).show();
               // Toast.makeText(getActivity(),date.getText(), Toast.LENGTH_LONG).show();
               // Toast.makeText(getActivity(),time.getText(), Toast.LENGTH_LONG).show();
               // Toast.makeText(getActivity(),toggleText, Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(),numPassengers.getText(), Toast.LENGTH_LONG).show();

            }
        });



    return view;

    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

    }




    private static final String LOG_TAG = "FlockApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDDdXLw695vmtduvgnkP8EDBJ_nOs0CYkg";

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }





    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }






    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.activity = (MainActivity)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }






    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void processAdvancedSearch(Wish wish);
    }

}
