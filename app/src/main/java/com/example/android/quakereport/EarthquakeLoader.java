package com.example.android.quakereport;
import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.quakereport.Earthquake;
import com.example.android.quakereport.QueryUtils;
import java.util.List;

//  Loads list of events by using an AsyncTask to perform network request to given URL.
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    // Tag for log messages
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    // Query URL
    private String mUrl;

    /** Constructs new {@link EarthquakeLoader}
     * @param context of the activity
     *
     * @param url to load data from
     */
    public EarthquakeLoader(Context context, String url){
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    /**This is to be done on a background thread*/
    @Override
    public List<Earthquake> loadInBackground(){
        if (mUrl==null){
            return null;
        }

        //Perform network request, parse the response, and extract list of earthquakes
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }

}
