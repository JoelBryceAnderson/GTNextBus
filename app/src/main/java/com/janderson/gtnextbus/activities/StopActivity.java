package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.RelativeLayout;

import uk.co.senab.actionbarpulltorefresh.library.DefaultHeaderTransformer;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.StopAdapter;
import com.janderson.gtnextbus.background.ParseFeed;
import com.janderson.gtnextbus.items.StopItem;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.net.URL;
import java.util.ArrayList;



public class StopActivity extends Activity {

    private PullToRefreshLayout mPullToRefreshLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        int actionBarColor = Color.parseColor("#FFBB33");
        tintManager.setStatusBarTintColor(actionBarColor);


        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        runTask();
                    }
                })
                .setup(mPullToRefreshLayout);
        DefaultHeaderTransformer transformer = (DefaultHeaderTransformer) mPullToRefreshLayout
                .getHeaderTransformer();
        transformer.setProgressBarColor(Color.parseColor("#FFFFFF"));


        Intent intent = getIntent();
        strings = intent.getStringArrayExtra("extra");
        title = strings[0];
        route = strings[1];
        stop = strings[2];
        color = strings[3];
        secondTime = "time";
        thirdTime = "time";
        times = new String[]{time, secondTime, thirdTime};

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

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
            mPullToRefreshLayout.setRefreshComplete();

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
        }
    }
}
