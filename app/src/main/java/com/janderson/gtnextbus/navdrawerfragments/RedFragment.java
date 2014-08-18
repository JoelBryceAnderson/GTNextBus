package com.janderson.gtnextbus.navdrawerfragments;

/**
 * Created by JoelAnderson on 5/15/14.
 */

import android.app.Activity;
import android.app.ActivityOptions;
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
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.activities.MainActivity;
import com.janderson.gtnextbus.activities.StopActivity;
import com.janderson.gtnextbus.adapters.DestinationAdapter;
import com.janderson.gtnextbus.items.RouteItem;

import java.util.ArrayList;
import java.util.List;

public class RedFragment extends Fragment {

    private RelativeLayout redRouteLayout;
    private ListView mRouteList;
    private String[] routes;
    private ArrayList<RouteItem> redRouteItems;
    private DestinationAdapter adapter;
    private ColorDrawable headerColor;
    private boolean justRotated;

    public RedFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_red, container, false);
        return rootView;
    }

    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle("Red Route");
        headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        getActivity().getActionBar().setBackgroundDrawable(headerColor);
        routes = getResources().getStringArray(R.array.red_routes);
        redRouteLayout = (RelativeLayout) getView().findViewById(R.id.fragment_red);
        mRouteList = (ListView) getView().findViewById(R.id.red_cards);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
            justRotated = savedInstanceState.getBoolean("justRotated");
        }
        redRouteItems = new ArrayList<RouteItem>();
        redRouteItems.add(new RouteItem(routes[0], R.drawable.stinger, true));
        for (int i = 1; i < 18; i++) {
            redRouteItems.add(new RouteItem(routes[i]));
        }
        adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                redRouteItems, true);
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
                    if (justRotated) {
                        getActivity().getActionBar().show();
                        justRotated = false;
                    } else {
                        getActivity().getActionBar().hide();
                    }
                }
                else if (currentFirstVisibleItem < mLastFirstVisibleItem)
                {
                    if (!getActivity().getActionBar().isShowing() ||
                            ((MainActivity)getActivity()).getActionBarAlpha() != 255) {
                        final float ratio = (float) Math.min(Math.max(i, 0), i3) / i3;
                        final float finalRatio = (float) (1 - ratio);
                        int alphaVal = (int) (finalRatio * 255);
                        ((MainActivity)getActivity()).setActionBarAlpha(alphaVal);
                        ((MainActivity)getActivity()).getActionBar().show();
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
                if(getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_PORTRAIT) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
                int topPadding = getActivity().getApplicationContext().
                        getResources().getDimensionPixelSize(R.dimen.padding_top_translucent);
                int bottomPadding = getActivity().getApplicationContext().
                        getResources().getDimensionPixelSize(R.dimen.padding_bottom_translucent);
                mRouteList.setPadding(0, topPadding, 0, bottomPadding);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("justRotated", true);
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
        ArrayList<String> stringArrayList = null;
        switch (position) {
            case 0:
                break;
            case 1:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Fitten Hall","red","fitten", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 2:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"McMillian & 8th","red","mcm8th", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 3:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"8th & Hemphill","red","8thhemp", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 4:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Hemphill","red","fershemrt", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 5:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & State","red","fersstmrt", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 6:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Atlantic","red","fersatmrt", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 7:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Klaus","red","ferschmrt", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 8:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Fowler","red","5thfowl", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 9:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 5th","red","tech5th", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 10:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood and 4th","red","tech4th", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 11:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood and Bobby Dodd","red","techbob", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 12:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & North Ave","red","technorth", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 13:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Ave Apartments","red","nortavea_a", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 14:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Ave & President's Lot","red","nortpres", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 15:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Tech and Aerospace","red","techaero", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 16:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Tech and State","red","techstat", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 17:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"CRC","red","765femrt", "#CC0000"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
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
