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

import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter (Context context, List<Earthquake> earthquake){
        super(context,0,earthquake);
    }
@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

    //store convertView as variable of View type called listEventView
    View listEventView=convertView;

    //check if existing view is in use else inflate view
    if (listEventView==null){
        listEventView= LayoutInflater.from(getContext()).inflate(R.layout.list_segment,parent,false ); }

    //get earthquake data at this position in index
    Earthquake currentEarthquake=getItem(position);

    //Locate Magnitude XML TextView from list segment by R.id
    TextView magnitudeView=(TextView)listEventView.findViewById(R.id.magnitude);
    //Get View from Adapter and set view with earthquake data
    magnitudeView.setText(currentEarthquake.getMagnitude());

    //Locate Location XML TextView from list segment by R.id
    TextView locationView=(TextView)listEventView.findViewById(R.id.location);
    //Get View from Adapter and set view with earthquake data
    locationView.setText(currentEarthquake.getLocation());

    //Locate Date XML TextView from list segment by R.id
    TextView dateView=(TextView)listEventView.findViewById(R.id.date);
    //Get View from Adapter and set view with earthquake data
    dateView.setText(currentEarthquake.getDate());

    //Return tri-element TextView list layout for display in ListView R.id.list_segment
    return listEventView;

    }
}
