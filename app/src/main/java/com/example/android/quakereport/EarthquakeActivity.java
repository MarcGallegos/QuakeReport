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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
            "https://earthquake.usgs.gov/fdsnws/event/1/query?" +
                    "format=geojson&orderby=time&minmag=5&limit=10";

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
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: Loader onCreate Called");
        //Create a new Loader for the given URL
        return new EarthquakeLoader(this, USGS_Request_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes){

        //Hide loading indicator as data has been loaded
        View progressBar=findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);
        //Set (currently-blank) empty state text to display "No Earthquakes Found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);
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


}
