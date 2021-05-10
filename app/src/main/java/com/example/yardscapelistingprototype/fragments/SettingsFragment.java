package com.example.yardscapelistingprototype.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.yardscapelistingprototype.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragmentCompat {
    SwitchPreference dark_mode;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        dark_mode = findPreference("dark_mode");
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", true);

        assert dark_mode != null;

        dark_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(dark_mode.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);

                }
                editor.apply();
                getActivity().recreate();
                return true;
            }
        });
/*        dark_mode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(isDarkModeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                    return true;
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                    return false;
                }
            }
        });*/

    }


}