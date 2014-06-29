package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.StopListAdapter;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;


public class StopListActivity extends Activity {

    private String[] strings;
    private String title;
    private String color;
    private RelativeLayout stopLayout;
    private ListView stopList;
    private ArrayList<StopItem> stopItems;
    private StopListAdapter adapter;
    private String[] stops;
    private String routeTag;
    private String[] stopTags;
    private ColorDrawable headerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Route Stops");
        headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        getActionBar().setBackgroundDrawable(headerColor);
        setContentView(R.layout.activity_stop_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        strings = intent.getStringArrayExtra("extra");
        stops = intent.getStringArrayExtra("stops");
        stopTags = intent.getStringArrayExtra("stopTags");
        title = strings[0];
        color = strings[1];
        routeTag = strings[2];
        stopLayout = (RelativeLayout) this.findViewById(R.id.activity_stop_list);
        stopList = (ListView) this.findViewById(R.id.stop_list_cards);
        stopItems = new ArrayList<StopItem>();
        stopItems.add(new StopItem(title));
        for (int i = 0; i < stops.length; i++) {
            stopItems.add(new StopItem(stops[i]));
        }
        adapter = new StopListAdapter(this.getApplicationContext(),
                stopItems, color);
        stopList.setAdapter(adapter);
        stopList.setOnItemClickListener(new StopListClickListener());
        stopList.setOnScrollListener(new AbsListView.OnScrollListener() {

            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                final int currentFirstVisibleItem = stopList.getFirstVisiblePosition();
                if (currentFirstVisibleItem > mLastFirstVisibleItem)
                {
                    getActionBar().hide();
                }
                else if (currentFirstVisibleItem < mLastFirstVisibleItem)
                {
                    if (!getActionBar().isShowing() ||
                            headerColor.getAlpha() != 255) {
                        final float ratio = (float) Math.min(Math.max(i, 0), (i3)) / (i3);
                        final float finalRatio = (float) (1 - ratio);
                        int alphaVal = (int) (finalRatio * 255);
                        Log.v("alphaVal", Float.toString(ratio));
                        headerColor.setAlpha(alphaVal);
                        getActionBar().show();
                    }
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
        if (sharedPreferences.getBoolean("transparentNav", true)) {
            Window window = getWindow();
            if (android.os.Build.VERSION.SDK_INT>=19) {
                if(getResources().
                        getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top_translucent);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom_translucent);
                    stopList.setPadding(0, topPadding, 0, bottomPadding);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom);
                    stopList.setPadding(0, topPadding, 0 , bottomPadding);
                }
            }
        } else {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int topPadding = getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_top);
            int bottomPadding = getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_bottom);
            stopList.setPadding(0, topPadding, 0 , bottomPadding);
        }
    }
    private class StopListClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {
        Intent intent = null;
        String[] strings = null;
        ArrayList<String> stringArrayList = null;
        switch (position) {
            case 0:
                break;
            default:
                intent = new Intent(this, StopActivity.class);
                strings = new String [] {stops[position - 1],routeTag, stopTags[position - 1], color};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
        }
        if (intent != null) {
            startActivity(intent);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating activity");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


