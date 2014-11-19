package com.janderson.gtnextbus.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.janderson.gtnextbus.R;

public class SettingsActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_activity, R.anim.stay_put_activity);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        setContentView(R.layout.activity_settings);
        android.support.v7.widget.Toolbar mToolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().add(R.id.settings_layout,
                new MyPreferenceFragment()).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.stay_put_activity, R.anim.slide_out_activity);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_put_activity, R.anim.slide_out_activity);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            Preference aboutCreator = findPreference("openAbout");
            aboutCreator.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), About.class);
                    startActivity(intent);
                    return true;
                }
            });
            Preference activeAlerts = findPreference("activeAlerts");
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
