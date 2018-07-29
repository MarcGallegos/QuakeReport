package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = "of";
    String primaryLocation;
    String locationOffset;

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //store convertView as variable of View type called listEventView
        View listEventView = convertView;

        //check if existing view is in use else inflate view
        if (listEventView == null) {
            listEventView = LayoutInflater.from(getContext()).inflate(R.layout.list_segment, parent, false);
        }

        //get earthquake data at this position in index
        Earthquake currentEarthquake = getItem(position);

        //get string original location and store as variable
        String originalLocation = currentEarthquake.getLocation();

        //Check if LOCATION_SEPARATOR exists, if not treat as location_offset, and use text "Near.."
        // Split string at word "of" (LOCATION_SEPARATOR)
        // Locate primaryLocation and locationOffset XML TextViews
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }else{
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation=originalLocation;
        }

        TextView primaryLocationView = (TextView) listEventView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        TextView locationOffsetView = (TextView) listEventView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);
//        //Locate Location XML TextView from list segment by R.id
//        TextView locationView = (TextView) listEventView.findViewById(R.id.location);
//        //Get View from Adapter and set view with earthquake data
//        locationView.setText(currentEarthquake.getLocation());

        //Create new date object from time in milliseconds for this earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        //Locate Date XML TextView from list segment by R.id
        TextView dateView = (TextView) listEventView.findViewById(R.id.date);
        //Format the Date string i.e. "Mar 3, 1984"
        String formattedDate = formatDate(dateObject);
        //Get View from Adapter and set view with earthquake data
        dateView.setText(formattedDate);

        //Locate Time XML TextView from list segment by R.id
        TextView timeView = (TextView) listEventView.findViewById(R.id.time);
        //Format the Date string i.e. "Mar 3, 1984"
        String formattedTime = formatTime(dateObject);
        //Get View from Adapter and set view with earthquake data
        timeView.setText(formattedTime);

        //Locate Magnitude XML TextView from list segment by R.id
        TextView magnitudeView = (TextView) listEventView.findViewById(R.id.magnitude);
        //Format magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        //Get View from Adapter and set view with earthquake data
        magnitudeView.setText(formattedMagnitude());


        //Return tri-element TextView list layout for display in ListView R.id.list_segment
        return listEventView;

    }
    //Return the formatted Date String i.e. "Mar 3, 1984" from a date object
    public String formatDate (Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    public String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return formatted magnitude string showing 1 decimal place (i.e. "4.5")
     * from a decimal magnitude value
     */
    private String formatMagnitude(double magnitude){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
}
