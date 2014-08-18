package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] navMenuTitles;
    private String[] navSubTitles;
    private TypedArray icons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private ColorDrawable headerColor;
    private float originalRatio = 1;
    private int selectedItemPosition;
    private int currentFragmentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        getActionBar().setBackgroundDrawable(headerColor);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            currentFragmentPosition = 2000;
        } else {
            setTitle(savedInstanceState.getCharSequence("title"));
            selectedItemPosition = savedInstanceState.getInt("selectedItemValue");
            currentFragmentPosition = savedInstanceState.getInt("currentFragmentPosition");
        }
        navMenuTitles = getResources().getStringArray(R.array.buses);
        navSubTitles = getResources().getStringArray(R.array.sub);
        icons = getResources()
                .obtainTypedArray(R.array.icons);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));
        navDrawerItems.add(new NavDrawerItem(navSubTitles[0], icons.getResourceId(0, -1), true));
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        adapter.setSelectedItem(selectedItemPosition);
        mDrawerList.setAdapter(adapter);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (!getActionBar().isShowing()) {
                    getActionBar().show();
                }
                super.onDrawerSlide(drawerView, slideOffset);
                final float ratio = Math.min(Math.max(slideOffset, originalRatio), 1) / 1;
                int alphaVal = (int) (ratio * 255);
                headerColor.setAlpha(alphaVal);
                getActionBar().setBackgroundDrawable(headerColor);
            }

            public void onDrawerStateChanged(int newState) {
                if (!mDrawerLayout.isDrawerVisible(mDrawerList)) {
                    int originalAlpha = headerColor.getAlpha();
                    originalRatio = (float) originalAlpha / 255;
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
        if (sharedPreferences.getBoolean("transparentNav", true)) {
            Window window = getWindow();
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                if(getResources().
                        getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top_translucent);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom_translucent);
                    mDrawerList.setPadding(0, topPadding, 0, bottomPadding);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom);
                    mDrawerList.setPadding(0, topPadding, 0 , bottomPadding);
                }
            }
        } else {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int topPadding = getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_top);
            int bottomPadding = getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_bottom);
            mDrawerList.setPadding(0, topPadding, 0 , bottomPadding);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
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
            case 0:
                fragment = new RedFragment();
                break;
            case 1:
                fragment = new BlueFragment();
                break;
            case 2:
                fragment = new GreenFragment();
                break;
            case 3:
                fragment = new EmoryFragment();
                break;
            case 4:
                fragment = new TrolleyFragment();
                break;
            case 5:
                fragment = new MidnightFragment();
                break;
            case 6:
                fragment = new FavoriteFragment();
                break;
            case 7:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        if (fragment != null) {
            mDrawerLayout.closeDrawer(mDrawerList);
            if (selectedItemPosition != currentFragmentPosition) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_fragment, R.anim.slide_out_left);
                transaction.replace(R.id.frame_container, fragment);
                transaction.commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                currentFragmentPosition = selectedItemPosition;
            }
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    public void setActionBarAlpha(int actionBarAlpha) {
        headerColor.setAlpha(actionBarAlpha);
        getActionBar().setBackgroundDrawable(headerColor);
    }

    public int getActionBarAlpha() {
        return headerColor.getAlpha();
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position < 7) {
                originalRatio = 1;
                adapter.setSelectedItem(position);
                adapter.notifyDataSetChanged();
            }
            displayView(position);
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
