package com.janderson.gtnextbus.navdrawerfragments;

/**
 * Created by JoelAnderson on 5/15/14.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.activities.StopListActivity;
import com.janderson.gtnextbus.adapters.DestinationAdapter;
import com.janderson.gtnextbus.items.RouteItem;

import java.util.ArrayList;

public class GreenFragment extends Fragment {

    public GreenFragment(){}
    private RelativeLayout greenDestinationLayout;
    private ListView mRouteList;
    private String[] destinations;
    private ArrayList<RouteItem> greenDestinationItems;
    private DestinationAdapter adapter;
    private ColorDrawable headerColor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_green, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle("Green Route");
        destinations = getResources().getStringArray(R.array.green_destinations);
        headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        getActivity().getActionBar().setBackgroundDrawable(headerColor);
        greenDestinationLayout = (RelativeLayout) getView().findViewById(R.id.fragment_green);
        mRouteList = (ListView) getView().findViewById(R.id.green_cards);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
        }
        greenDestinationItems = new ArrayList<RouteItem>();
        greenDestinationItems.add(new RouteItem(destinations[0]));
        greenDestinationItems.add(new RouteItem(destinations[1]));
        greenDestinationItems.add(new RouteItem(destinations[2]));
        greenDestinationItems.add(new RouteItem(destinations[3]));
        adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                greenDestinationItems);
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
                    if (!getActivity().getActionBar().isShowing() ||
                            headerColor.getAlpha() != 255) {
                        final float ratio = (float) Math.min(Math.max(i, 0), i3) / i3;
                        final float finalRatio = (float) (1 - ratio);
                        int alphaVal = (int) (finalRatio * 255);
                        Log.v("alphaVal", Float.toString(ratio));
                        headerColor.setAlpha(alphaVal);
                        getActivity().getActionBar().show();
                    }
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(
                        getActivity().getApplicationContext());
        if (sharedPreferences.getBoolean("transparentNav", true)) {
            Window window = getActivity().getWindow();
            if (android.os.Build.VERSION.SDK_INT>=19) {
                if(getResources().
                        getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getActivity().getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top_translucent);
                    int bottomPadding = getActivity().getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom_translucent);
                    mRouteList.setPadding(0, topPadding, 0, bottomPadding);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getActivity().getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top);
                    int bottomPadding = getActivity().getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom);
                    mRouteList.setPadding(0, topPadding, 0 , bottomPadding);
                }
            }
        } else {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int topPadding = getActivity().getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_top);
            int bottomPadding = getActivity().getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_bottom);
            mRouteList.setPadding(0, topPadding, 0 , bottomPadding);
        }
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
                strings = new String [] {"To TEP", "#669900", "green"};
                stops = getResources().getStringArray(R.array.green_tep_stops);
                stopTags = getResources().getStringArray(R.array.green_tep_stop_tags);
                intent.putExtra("stopTags", stopTags);
                intent.putExtra("extra", strings);
                intent.putExtra("stops", stops);
                break;
            case 2:
                intent = new Intent(getActivity(), StopListActivity.class);
                strings = new String [] {"To Transit Hub", "#669900", "green"};
                stops = getResources().getStringArray(R.array.green_hub_stops);
                stopTags = getResources().getStringArray(R.array.green_hub_stop_tags);
                intent.putExtra("stopTags", stopTags);
                intent.putExtra("stops", stops);
                intent.putExtra("extra", strings);
                break;
            case 3:
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