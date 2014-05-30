package com.janderson.gtnextbus.navdrawerfragments;

/**
 * Created by JoelAnderson on 5/15/14.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.janderson.gtnextbus.activities.StopListActivity;
import com.janderson.gtnextbus.adapters.DestinationAdapter;
import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.items.RouteItem;
import com.janderson.gtnextbus.activities.StopActivity;

import java.util.ArrayList;

public class EmoryFragment extends Fragment {

    public EmoryFragment(){}
    private RelativeLayout emoryDestinationLayout;
    private ListView mRouteList;
    private String[] destinations;
    private ArrayList<RouteItem> emoryDestinationItems;
    private DestinationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_emory, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        destinations = getResources().getStringArray(R.array.emory_destinations);
        emoryDestinationLayout = (RelativeLayout) getView().findViewById(R.id.fragment_emory);
        mRouteList = (ListView) getView().findViewById(R.id.emory_cards);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mRouteList.setPadding(0,72,0,10);
        }
        emoryDestinationItems = new ArrayList<RouteItem>();
        emoryDestinationItems.add(new RouteItem(destinations[0]));
        emoryDestinationItems.add(new RouteItem(destinations[1]));
        emoryDestinationItems.add(new RouteItem(destinations[2]));
        adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                emoryDestinationItems);
        mRouteList.setAdapter(adapter);
        mRouteList.setOnItemClickListener(new StopClickListener());
        mRouteList.setOnScrollListener(new AbsListView.OnScrollListener() {

            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                final int currentFirstVisibleItem = mRouteList.getFirstVisiblePosition();
                if (currentFirstVisibleItem > mLastFirstVisibleItem)
                {
                    getActivity().getActionBar().hide();
                }
                else if (currentFirstVisibleItem < mLastFirstVisibleItem)
                {
                    getActivity().getActionBar().show();
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
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
        String[] strings =  null;
        String[] stops = null;
        String[] stopTags = null;
        switch (position) {
            case 0:
                break;
            case 1:
                intent = new Intent(getActivity(), StopListActivity.class);
                strings = new String [] {"To Emory", "#000080", "emory"};
                stops = getResources().getStringArray(R.array.emory_emory_stops);
                stopTags = getResources().getStringArray(R.array.emory_emory_stop_tags);
                intent.putExtra("stopTags", stopTags);
                intent.putExtra("stops", stops);
                intent.putExtra("extra", strings);
                break;
            case 2:
                intent = new Intent(getActivity(), StopListActivity.class);
                strings = new String [] {"To Georgia Tech", "#000080", "emory"};
                stops = getResources().getStringArray(R.array.emory_gatech_stops);
                stopTags = getResources().getStringArray(R.array.emory_gatech_stop_tags);
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