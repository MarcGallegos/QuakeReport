package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{

            @Override
            public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_main);

                //Get minMagnitude preference Object. To help bind the value
                // in sharedPreferences to what will show up in preference summary.
                Preference minMagnitude = findPreference(getString
                        (R.string.settings_min_magnitude_key));
                bindPreferenceSummaryToValue(minMagnitude);

                //Get orderBy preference Object to help bind the value
                //in sharedPreferences to what will show in preference summary.
                Preference orderBy = findPreference(getString
                        (R.string.settings_order_by_key));
                bindPreferenceSummaryToValue(orderBy);
            }
            @Override
            public boolean onPreferenceChange (Preference preference, Object value){
                //The code in this method takes care of updating the displayed preference summary
                //after it has been changed
                String stringValue = value.toString();
                if (preference instanceof ListPreference){
                    ListPreference listPreference = (ListPreference) preference;
                    int prefIndex=listPreference.findIndexOfValue((stringValue));
                    if (prefIndex >=0){
                        CharSequence[] labels = listPreference.getEntries();
                        preference.setSummary(labels[prefIndex]);
                    }
                }else {
                    preference.setSummary(stringValue);
                }
                return true;
            }

        private void bindPreferenceSummaryToValue(Preference preference){
            //define method,taking a Preference as parameter, and use setOnPreferenceChange to set
            //the current EarthquakePreferenceFragment instance to listen for preference
            //changes passed in
            preference.setOnPreferenceChangeListener(this);
            //read current value of preference stored in sharedPreferences on device, and display in
            //preference summary(so user can see the current preference value)
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(),"");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
