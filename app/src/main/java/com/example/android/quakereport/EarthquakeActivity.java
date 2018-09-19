/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
            implements LoaderCallbacks<List<Earthquake>> {

    /**
     * TextView to display if no earthquakes are present (List Empty State)
     */
    private TextView mEmptyStateTextView;

    /**
     * Tag for Logging the methods of the earthquake app
     */
    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * URL (variable) for earthquake data from USGS dataset
     */
    private static final String USGS_Request_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /**
     * Constant value for earthquake loader ID. Can be any integer as is meant for >1 loader
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * Adapter for list of Earthquakes
     */
    private EarthquakeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //Find and Display TextView for Empty List State if no earthquakes are found
        mEmptyStateTextView=(TextView)findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

    //Set onItemClickListener on ListView, which sends an intent to a web browser
    //to view more details about the selected event on the USGS website
    earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //Find most currently selected earthquake
        Earthquake currentEarthquake = mAdapter.getItem(position);
        //Convert string URL into URI object (to pass to Intent constructor)
        Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
        //Create new intent to view earthquake URI
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
        //Send Intent to launch new browser activity
        startActivity(websiteIntent);
        }
    });

        //Prepare EarthquakeLoader. Either via reconnecting to an existing one or start a new one.
        getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        Log.i(LOG_TAG,"TEST: Loader Initialized");
    }

    @Override
    //onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: Loader onCreateLoader Called");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //getString retrieves a String value from the preferences. The second parameter is the
        // default value for this preference.
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        //take user's preference stored in orderBy variable to use as "orderby" parameter
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        //parse breaks apart the URI string that's passed into it's parameter
        Uri baseUri = Uri.parse(USGS_Request_URL);

        //buildUpon prepares the baseUri we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Append query parameter and its value. For example, the 'format=geojson'
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag","minMagnitude");
        uriBuilder.appendQueryParameter("orderby",orderBy);

        //Create a new Loader and return the completed Uri:
        // "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes){

        //Hide loading indicator as data has been loaded
        View progressBar=findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);

        //Check for internet connection
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =cm.getActiveNetworkInfo();
        if (networkInfo==null) {
            //state there is no internet connection
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }else if (networkInfo!=null && networkInfo.isConnected()){
            //There IS internet but list is still empty
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        }
        //Clear adapter of previous earthquake data
        mAdapter.clear();
        //If a valid list of {@link Earthquake}s exists add them to the adapter's dataset.
        //This will trigger ListView to update
        if (earthquakes != null && !earthquakes.isEmpty()) {
            //Add Earthquake Items to List
            mAdapter.addAll(earthquakes);

            Log.i(LOG_TAG,"TEST: onLoadFinished Called");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>>loader){
        //Loader reset to purge existing data
        mAdapter.clear();
        Log.i(LOG_TAG,"TEST: onLoaderReset Called");
    }

    @Override
    //This method initializes the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the Options menu specified in XML
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    //Pass MenuItem that is selected
    public boolean onOptionsItemSelected(MenuItem item){
        //returns unique id for menu item defined by android:id in menu resource
        // determine which item was selected and what action to take
        int id=item.getItemId();
        //menu has one item, @id/action-settings,
        //match id against known menu items to perform appropriate action
        if(id==R.id.action_settings){
            //in this case, open SettingsActivity via an intent.
            Intent settingsIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            //Return boolean "true"
            return true;
        }
        //Return item selected
        return super.onOptionsItemSelected(item);
    }

}
