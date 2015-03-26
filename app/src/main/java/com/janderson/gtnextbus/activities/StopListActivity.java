package com.janderson.gtnextbus.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.StopListAdapter;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;


public class StopListActivity extends ActionBarActivity {

    private String color;
    private String[] stops;
    private String routeTag;
    private String[] stopTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);
        android.support.v7.widget.Toolbar mToolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_stop_list);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Route Stops");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String[] strings = intent.getStringArrayExtra("extra");
        stops = intent.getStringArrayExtra("stops");
        stopTags = intent.getStringArrayExtra("stopTags");
        String title = strings[0];
        color = strings[1];
        routeTag = strings[2];
        ListView stopList = (ListView) this.findViewById(R.id.stop_list_cards);
        ArrayList<StopItem> stopItems = new ArrayList<>();
        stopItems.add(new StopItem(title));
        for (String stop : stops) {
            stopItems.add(new StopItem(stop));
        }
        StopListAdapter adapter = new StopListAdapter(this.getApplicationContext(),
                stopItems, color);
        stopList.setAdapter(adapter);
        stopList.setOnItemClickListener(new StopListClickListener());
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
        String[] strings;
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


