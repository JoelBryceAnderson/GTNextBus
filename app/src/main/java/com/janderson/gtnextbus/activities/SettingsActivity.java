package com.janderson.gtnextbus.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.janderson.gtnextbus.R;

/**
 * Created by JoelAnderson on 5/31/14.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            addPreferencesFromResource(R.xml.preferences);
            Preference aboutCreator = (Preference) findPreference("openAbout");
            aboutCreator.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), About.class);
                    startActivity(intent);
                    return true;
                }
            });
            Preference activeAlerts = (Preference) findPreference("activeAlerts");
            activeAlerts.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), AlertsActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}
