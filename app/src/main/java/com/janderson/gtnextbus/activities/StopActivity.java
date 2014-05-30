package com.janderson.gtnextbus.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.StopAdapter;
import com.janderson.gtnextbus.background.ParseFeed;
import com.janderson.gtnextbus.items.StopItem;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;


public class StopActivity extends Activity {

    private String[] strings;
    private String title;
    private String route;
    private String stop;
    private String color;
    private String direction;
    private RelativeLayout stopLayout;
    private ListView stopList;
    private ArrayList<StopItem> stopItems;
    private StopAdapter adapter;
    private String url;
    private String time = "No Current Prediction";
    private String secondTime;
    private String thirdTime;
    private String[] times;
    private boolean savedStop = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;
    private Set<String> stringSet;
    private String favoriteKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        LinearLayout lay = (LinearLayout) findViewById(R.id.lay);
        preferences = getSharedPreferences("saved_favorites", MODE_PRIVATE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            lay.setPadding(0,72,0,10);
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        swipeRefreshLayout.setColorScheme(R.color.white,
                R.color.yellow,
                R.color.white,
                R.color.yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runTask();
            }
        });
        Intent intent = getIntent();
        strings = intent.getStringArrayExtra("extra");
        title = strings[0];
        route = strings[1];
        stop = strings[2];
        color = strings[3];
        stringSet = new LinkedHashSet<String>();
        stringSet.add("?".concat(title));
        stringSet.add("*".concat(route));
        stringSet.add("$".concat(stop));
        stringSet.add(color);
        favoriteKey = route.concat(stop);
        secondTime = "time";
        thirdTime = "time";
        times = new String[]{time, secondTime, thirdTime};
        swipeRefreshLayout.setRefreshing(true);
        runTask();
    }


    private void runTask() {
        RssFeedTask rssTask = new RssFeedTask(this);
        rssTask.execute();
    }

    private void parse() {
        ParseFeed parseFeed = new ParseFeed(createURL(route), stop, time, secondTime, thirdTime);
        time = parseFeed.time;
        secondTime = parseFeed.secondTime;
        thirdTime = parseFeed.thirdTime;
        if (time.equals("0 minutes")) {
            time = "Arriving Now";
        }
        if (secondTime.equals("0 minutes")) {
            secondTime = "Arriving Now";
        }
        if (thirdTime.equals("0 minutes")) {
            thirdTime = "Arriving Now";
        }
        if (time.equals("1 minutes")) {
            time = ("1 minute");
        }
        if (secondTime.equals("1 minutes")) {
            secondTime = ("1 minute");
        }
        if (thirdTime.equals("1 minutes")) {
            thirdTime = ("1 minute");
        }
    }

    private static String createURL(String route) {
        URL finalURL = null;
        final String s = "http://gtwiki.info/nextbus/nextbus.php?a=georgia-tech&command=predictionsForMultiStops&r=";
        String url = s.concat(route);
        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite, menu);
        if (preferences.contains(favoriteKey)) {
            menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_saved);
            menu.findItem(R.id.action_save).setTitle("Remove favorite");
            savedStop = true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if (savedStop) {
                    preferences.edit().remove(favoriteKey).commit();
                    Toast.makeText(this, "Removed stop from favorites.", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_action_save);
                    item.setTitle("Add favorite");
                    savedStop = false;
                } else {
                    preferences.edit().putStringSet(favoriteKey, stringSet).commit();
                    Toast.makeText(this, "Added stop to favorites.", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_action_saved);
                    item.setTitle("Remove favorite");
                    savedStop = true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class RssFeedTask extends AsyncTask<String, Void, String> {
        private String result = "";
        public StopActivity activity;

        public RssFeedTask (StopActivity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            activity.parse();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            stopLayout = (RelativeLayout) activity.findViewById(R.id.activity_stop);
            stopList = (ListView) activity.findViewById(R.id.stop_cards);
            stopItems = new ArrayList<StopItem>();
            stopItems.add(new StopItem(title));
            stopItems.add(new StopItem(time));
            if (!secondTime.equals("time")) {
                stopItems.add(new StopItem(secondTime));
            }
            if (!thirdTime.equals("time")) {
                stopItems.add(new StopItem(thirdTime));
            }
            adapter = new StopAdapter(activity.getApplicationContext(),
                    stopItems, color);
            SwingRightInAnimationAdapter swingRightInAnimationAdapter = new SwingRightInAnimationAdapter(adapter);
            swingRightInAnimationAdapter.setAbsListView(stopList);
            stopList.setAdapter(swingRightInAnimationAdapter);
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
                        getActionBar().show();
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            });
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
