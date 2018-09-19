package com.example.android.quakereport;

import android.util.Log;
import org.json.JSONArray;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with helper methods to help perform HTTP request and parse the JSON response.
 */
public final class QueryUtils {

    /** Tag for log messages*/
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /** Query USGS dataset and return list of {@link Earthquake} Objects */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        //sleep thread for 1 second to show progress indicator
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    //Create URL Object
        URL url = createUrl(requestUrl);

    //Perform HTTP request to the URL and receive JSON response back
    String jsonResponse = null;
    try{
    jsonResponse = makeHttpRequest(url);
    Log.i(LOG_TAG,"TEST: fetchEarthquakeData Called");
    }catch (IOException e) {
        Log.e(LOG_TAG, "Problem making HTTP request.", e);
    }

    //Extract relevant fields from JSON response and create list of {@link Earthquake}s
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

    //Return list of earthquakes
        return earthquakes;
    }

    /** Returns new URL object from given String URL */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL", e);
        }
        return url;
    }

    /** Makes an HTTP request to given URL and returns jsonResponse String */
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse ="";

        // If URL is null, return early
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try{
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If connection is successful (response code 200) Read input stream and parse response
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code:" + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG,"Problem retrieving earthquake JSON results.", e);
        }finally{
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        if(inputStream != null){
            //Closing inputStream could throw an IOException, so makeHttpRequest(URL url) specifies
            // an IOException could be thrown
            inputStream.close();
            }
        }
            return jsonResponse;
    }

        /**Converts the {@link InputStream} into a String, named output, that contains entire JSON
         * response from the server */
        private static String readFromStream (InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                        Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line!=null){
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing features from JSON response.
     */
    private static List<Earthquake> extractFeatureFromJson (String earthquakeJSON) {

        //If JSON string is empty or null, return early
        if (TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build a JSONObject of Earthquake features with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            //Extract JSONArray with key named "features" (an earthquake features list)
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // For each earthquake in earthquakeArray, create an {@link Earthquake} object.
            //while index i is less than array length increment i..
            for (int i =0; i < earthquakeArray.length(); i++) {

                //extract single earthquake @ index position (i) from list of earthquakes.
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                //For a given event, extract JSONObject with key named "properties"
                // which represents a list of properties for that earthquake
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                //extract double value for key "mag"
                double magnitude=properties.getDouble("mag");

                //extract string for key named "place"
                String location = properties.getString("place");

                //extract long value for key named "time"
                long time = properties.getLong("time");

                //extract string for key named "url"
                String url = properties.getString("url");

                //Create a new {@link Earthquake} object with magnitude, location, time, and url
                //from the JSON Response
                Earthquake earthquake = new Earthquake (magnitude, location, time, url);

                //add the new {@link Earthquake} to list of earthquakes
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
