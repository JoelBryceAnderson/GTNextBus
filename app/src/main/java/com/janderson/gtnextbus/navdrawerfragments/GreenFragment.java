package com.janderson.gtnextbus.navdrawerfragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.activities.StopListActivity;
import com.janderson.gtnextbus.adapters.DestinationAdapter;
import com.janderson.gtnextbus.items.RouteItem;

import java.util.ArrayList;

public class GreenFragment extends Fragment {

    public GreenFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_green, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Green Route");
        String[] destinations = getResources().getStringArray(R.array.green_destinations);
        ListView mRouteList = (ListView) getView().findViewById(R.id.green_cards);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
        }
        ArrayList<RouteItem> greenDestinationItems = new ArrayList<RouteItem>();
        greenDestinationItems.add(new RouteItem(destinations[1], R.drawable.tep, true));
        greenDestinationItems.add(new RouteItem(destinations[2],
                R.drawable.fourteenthstreet, true));
        DestinationAdapter adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                greenDestinationItems, false);
        mRouteList.setAdapter(adapter);
        mRouteList.setOnItemClickListener(new StopClickListener());
    }

    private class StopClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {
        Intent intent = null;
        String[] strings;
        String[] stops;
        String[] stopTags;
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), StopListActivity.class);
                strings = new String [] {"To TEP", "#669900", "green"};
                stops = getResources().getStringArray(R.array.green_tep_stops);
                stopTags = getResources().getStringArray(R.array.green_tep_stop_tags);
                intent.putExtra("stopTags", stopTags);
                intent.putExtra("extra", strings);
                intent.putExtra("stops", stops);
                break;
            case 1:
                intent = new Intent(getActivity(), StopListActivity.class);
                strings = new String [] {"To 14th Street", "#669900", "green"};
                stops = getResources().getStringArray(R.array.green_fourteenth_stops);
                stopTags = getResources().getStringArray(R.array.green_fourteenth_stop_tags);
                intent.putExtra("stopTags", stopTags);
                intent.putExtra("stops", stops);
                intent.putExtra("extra", strings);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        } else {
            Log.e("MainActivity", "Error in creating activity");
        }
    }

}