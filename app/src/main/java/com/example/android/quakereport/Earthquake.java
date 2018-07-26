package com.example.android.quakereport;

public class Earthquake {

    //Location Details to display to list segment
    private String mLocation;
    //Magnitude Details to display to list segment
    private String mMagnitude;
    //Date of Event to display to list segment
    private String mDate;

    /**
     * Create new Earthquake List Object
     *
     * @param location  is the event location for this list segment
     * @param date      is the date of event for this list segment
     * @param magnitude is the event magnitude for this list segment
     */
    public Earthquake(String magnitude, String location, String date) {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
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
    public String getMagnitude() {
        return mMagnitude;
    }
    /**
     * Get and Return Event Date data from Earthquake Object
     */
    public String getDate() {
        return mDate;
    }


//    @Override
//    public String toString() {
//        return "Earthquake{" +
//                ", mMagnitude='" + mMagnitude + '\'' +
//                ", mLocation='" + mLocation + '\'' +
//                ", mDate='" + mDate + '\'' +
//                '}';
//    }

}
