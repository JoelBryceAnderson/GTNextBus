package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.janderson.gtnextbus.items.NavDrawerItem;
import com.janderson.gtnextbus.adapters.NavDrawerListAdapter;
import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.navdrawerfragments.BlueFragment;
import com.janderson.gtnextbus.navdrawerfragments.EmoryFragment;
import com.janderson.gtnextbus.navdrawerfragments.GreenFragment;
import com.janderson.gtnextbus.navdrawerfragments.MidnightFragment;
import com.janderson.gtnextbus.navdrawerfragments.RedFragment;
import com.janderson.gtnextbus.navdrawerfragments.TrolleyFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.app.FragmentManager;
import android.util.Log;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.support.v4.app.ActionBarDrawerToggle;
import java.util.ArrayList;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.content.res.Configuration;
import android.widget.TextView;
import android.graphics.Typeface;

public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private String[] navSubTitles;
    private TypedArray icons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.buses);
        navSubTitles = getResources().getStringArray(R.array.sub);
        icons = getResources()
                .obtainTypedArray(R.array.icons);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int actionBarColor = Color.parseColor("#FFBB33");
            tintManager.setStatusBarTintColor(actionBarColor);
        } else {
            mDrawerList.setPadding(0,0,0,0);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
        navDrawerItems.add(new NavDrawerItem(navSubTitles[0], icons.getResourceId(0, -1), true));
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.app_name,
                R.string.app_name
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
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
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
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
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
44
    public void setNavDrawerItemNormal() {
        for (int i = 0; i < mDrawerList.getChildCount(); i++) {
            View view = mDrawerList.getChildAt(i);
            TextView TV = ((TextView) view.findViewById(R.id.title));
            TV.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position < 6) {
                setNavDrawerItemNormal();
                TextView TV = (TextView) view.findViewById(R.id.title);
                TV.setTypeface(null, Typeface.BOLD);
            }
            displayView(position);
        }
    }

}
