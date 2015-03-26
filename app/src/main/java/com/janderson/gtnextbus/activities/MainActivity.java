package com.janderson.gtnextbus.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.NavDrawerListAdapter;
import com.janderson.gtnextbus.items.NavDrawerItem;
import com.janderson.gtnextbus.navdrawerfragments.BlueFragment;
import com.janderson.gtnextbus.navdrawerfragments.EmoryFragment;
import com.janderson.gtnextbus.navdrawerfragments.FavoriteFragment;
import com.janderson.gtnextbus.navdrawerfragments.GreenFragment;
import com.janderson.gtnextbus.navdrawerfragments.MidnightFragment;
import com.janderson.gtnextbus.navdrawerfragments.RedFragment;
import com.janderson.gtnextbus.navdrawerfragments.TrolleyFragment;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private String[] navMenuTitles;
    private NavDrawerListAdapter adapter;
    private int selectedItemPosition;
    private int currentFragmentPosition;
    private boolean changeFragment;
    private int positionToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final android.support.v7.widget.Toolbar mToolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (savedInstanceState == null) {
            currentFragmentPosition = 2000;
            selectedItemPosition = 1;
        } else {
            setTitle(savedInstanceState.getCharSequence("title"));
            selectedItemPosition = savedInstanceState.getInt("selectedItemValue");
            currentFragmentPosition = savedInstanceState.getInt("currentFragmentPosition");
        }
        navMenuTitles = getResources().getStringArray(R.array.buses);
        TypedArray icons = getResources()
                .obtainTypedArray(R.array.icons);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new NavDrawerItem("image"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], icons.getResourceId(5, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], icons.getResourceId(6, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], icons.getResourceId(1, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], icons.getResourceId(7, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], icons.getResourceId(4, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], icons.getResourceId(2, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], icons.getResourceId(3, -1), true));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], icons.getResourceId(0, -1), true));
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        adapter.setSelectedItem(selectedItemPosition);
        mDrawerList.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar,
                R.string.app_name,
                R.string.app_name
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (changeFragment) {
                    displayView(positionToDisplay);
                }
            }
            @Override
            public void onDrawerOpened(View drawerView) {
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            displayView(1);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_map:
                openMapView();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openMapView() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
        adapter.setSelectedItem(selectedItemPosition);
        adapter.notifyDataSetChanged();
    }

    private void displayView(int position) {
        selectedItemPosition = position;
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new RedFragment();
                break;
            case 2:
                fragment = new BlueFragment();
                break;
            case 3:
                fragment = new GreenFragment();
                break;
            case 4:
                fragment = new EmoryFragment();
                break;
            case 5:
                fragment = new TrolleyFragment();
                break;
            case 6:
                fragment = new MidnightFragment();
                break;
            case 7:
                fragment = new FavoriteFragment();
                break;
            case 8:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        if (fragment != null) {
            if (selectedItemPosition != currentFragmentPosition) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_fragment, R.anim.slide_out_left);
                transaction.replace(R.id.frame_container, fragment);
                transaction.commit();
                mDrawerList.setItemChecked(position, true);
                setTitle(navMenuTitles[position - 1]);
                invalidateOptionsMenu();
                currentFragmentPosition = selectedItemPosition;
            }
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position < 8 && position > 0) {
                adapter.setSelectedItem(position);
                adapter.notifyDataSetChanged();
                changeFragment = true;
                positionToDisplay = position;
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                displayView(position);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("title", getTitle());
        outState.putBoolean("isDrawerOpen",
                mDrawerLayout.isDrawerOpen(mDrawerList));
        outState.putInt("selectedItemValue", selectedItemPosition);
        outState.putInt("currentFragmentPosition", currentFragmentPosition);
    }

}
