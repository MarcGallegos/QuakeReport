package com.example.android.quakereport;

public class Earthquake {

    //Location Details to display to list segment
    private String mLocation;
    //Magnitude Details to display to list segment
    private double mMagnitude;
    //Date of Event to display to list segment
    private String mDate;
    //Time of event to display to list segment
    private long mTimeInMilliseconds;

    /**
     * Create new Earthquake List Object
     *
     * @param location  is the event location for this list segment
     * @param timeInMilliseconds is the date of event for this list segment
     * @param magnitude is the event magnitude for this list segment
     */
    public Earthquake(double magnitude, String location, Long timeInMilliseconds) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
    }

    /**
     * Get and Return Event Location data from Earthquake Object
     */
    public String getLocation() {
        return mLocation;
    }
    /**
     * Get and Return Magnitude data from Earthquake Object
     */
    public double getMagnitude() {
        return mMagnitude;
    }
    /**
     * get and return Event Time from Earthquake JSON Object
     */
    public Long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }



}
