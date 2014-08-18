package com.janderson.gtnextbus.navdrawerfragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.activities.StopActivity;
import com.janderson.gtnextbus.adapters.FavoriteAdapter;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by JoelAnderson on 5/27/14.
 */
public class FavoriteFragment extends Fragment {

    public FavoriteFragment(){}
    private RelativeLayout favoriteLayout;
    private ListView mRouteList;
    private String header;
    private SharedPreferences preferences;
    private ArrayList<StopItem> favoriteDestinationItems;
    private FavoriteAdapter adapter;
    private int pos = 0;
    private String[] names;
    private String[] routeTags;
    private String[] stopTags;
    private String[] colors;
    private String name;
    private String color;
    private String stop;
    private String route;
    private String routeStopCombo;
    private String[] greenTepStopTags;
    private String[] greenHubStopTags;
    private String[] greenFourteenthStopTags;
    private String[] trolleyMartaStopTags;
    private String[] trolleyHubStopTags;
    private String[] emoryEmoryStopTags;
    private String[] emoryGatechStopTags;
    private String[] nightFittenStopTags;
    private String[] nightCulcStopTags;
    private TextView noFavoritesText;
    private ImageView noFavoritesImage;
    private ColorDrawable headerColor;
    private boolean justRotated;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle("Favorite Stops");
        headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        getActivity().getActionBar().setBackgroundDrawable(headerColor);
        header = getResources().getString(R.string.favorite_destinations_header);
        favoriteLayout = (RelativeLayout) getView().findViewById(R.id.fragment_favorite);
        mRouteList = (ListView) getView().findViewById(R.id.favorite_cards);
        LinearLayout noFavoritesLayout = (LinearLayout)
                getView().findViewById(R.id.no_favorites_layout);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
            noFavoritesLayout.setLayoutAnimation(null);
            justRotated = savedInstanceState.getBoolean("justRotated");
        }
        noFavoritesText = (TextView) getView().findViewById(R.id.no_favorites_text);
        noFavoritesImage = (ImageView) getView().findViewById(R.id.no_favorites_image);
        favoriteDestinationItems = new ArrayList<StopItem>();
        preferences = getActivity().getSharedPreferences("saved_favorites", Context.MODE_PRIVATE);
        Map<String, ?> keys = preferences.getAll();
        if (!keys.isEmpty()) {
            favoriteDestinationItems.add(new StopItem(header, "#000000"));
            noFavoritesText.setVisibility(View.GONE);
            noFavoritesImage.setVisibility(View.GONE);
            mRouteList.setVisibility(View.VISIBLE);
        } else {
            noFavoritesText.setVisibility(View.VISIBLE);
            noFavoritesImage.setVisibility(View.VISIBLE);
            mRouteList.setVisibility(View.GONE);
            noFavoritesText.setText("You don't have any favorites yet!");
            noFavoritesImage.setImageResource(R.drawable.ic_favorites_empty);
        }
        names = new String[keys.size()];
        routeTags = new String[keys.size()];
        stopTags = new String[keys.size()];
        colors = new String[keys.size()];
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Set<String> item = (Set<String>) entry.getValue();
            Object[] itemArray = item.toArray();
            for (int i = 0; i < itemArray.length; i++) {
                if (itemArray[i].toString().startsWith("?")) {
                    name = itemArray[i].toString().substring(1);
                    names[pos] = name;
                } else if (itemArray[i].toString().startsWith("*")) {
                    route = itemArray[i].toString().substring(1);
                    routeTags[pos] = route;
                } else if (itemArray[i].toString().startsWith("$")) {
                    stop = itemArray[i].toString().substring(1);
                    stopTags[pos] = stop;
                } else if (itemArray[i].toString().startsWith("#")) {
                    color = itemArray[i].toString();
                    colors[pos] = color;
                }
            }
            routeStopCombo = route.concat(stop);
            addDestination();
            favoriteDestinationItems.add(new StopItem(name, color));
            pos++;
        }
        adapter = new FavoriteAdapter(getActivity().getApplicationContext(),
                favoriteDestinationItems);
        mRouteList.setAdapter(adapter);
        mRouteList.setOnItemClickListener(new StopListClickListener());
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("justRotated", true);
    }

    private class StopListClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pos = 0;
        header = getResources().getString(R.string.favorite_destinations_header);
        favoriteLayout = (RelativeLayout) getView().findViewById(R.id.fragment_favorite);
        mRouteList = (ListView) getView().findViewById(R.id.favorite_cards);
        noFavoritesText = (TextView) getView().findViewById(R.id.no_favorites_text);
        noFavoritesImage = (ImageView) getView().findViewById(R.id.no_favorites_image);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mRouteList.setPadding(0,72,0,10);
        }
        favoriteDestinationItems = new ArrayList<StopItem>();
        preferences = getActivity().getSharedPreferences("saved_favorites", Context.MODE_PRIVATE);
        Map<String, ?> keys = preferences.getAll();
        if (!keys.isEmpty()) {
            favoriteDestinationItems.add(new StopItem(header, "#000000"));
            noFavoritesText.setVisibility(View.GONE);
            noFavoritesImage.setVisibility(View.GONE);
            mRouteList.setVisibility(View.VISIBLE);
        } else {
            noFavoritesText.setVisibility(View.VISIBLE);
            noFavoritesImage.setVisibility(View.VISIBLE);
            mRouteList.setVisibility(View.GONE);
            noFavoritesText.setText("You don't have any favorites yet!");
            noFavoritesImage.setImageResource(R.drawable.ic_favorites_empty);
        }
        names = new String[keys.size()];
        routeTags = new String[keys.size()];
        stopTags = new String[keys.size()];
        colors = new String[keys.size()];
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Set<String> item = (Set<String>) entry.getValue();
            Object[] itemArray = item.toArray();
            for (int i = 0; i < itemArray.length; i++) {
                if (itemArray[i].toString().startsWith("?")) {
                    name = itemArray[i].toString().substring(1);
                    names[pos] = name;
                } else if (itemArray[i].toString().startsWith("*")) {
                    route = itemArray[i].toString().substring(1);
                    routeTags[pos] = route;
                } else if (itemArray[i].toString().startsWith("$")) {
                    stop = itemArray[i].toString().substring(1);
                    stopTags[pos] = stop;
                } else if (itemArray[i].toString().startsWith("#")) {
                    color = itemArray[i].toString();
                    colors[pos] = color;
                }
            }
            routeStopCombo = route.concat(stop);
            addDestination();
            favoriteDestinationItems.add(new StopItem(name, color));
            pos++;
        }
        adapter = new FavoriteAdapter(getActivity().getApplicationContext(),
                favoriteDestinationItems);
        mRouteList.setAdapter(adapter);
        mRouteList.setOnItemClickListener(new StopListClickListener());
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

    private void displayView(int position) {
        Intent intent = null;
        String[] strings = null;
        ArrayList<String> stringArrayList = null;
        switch (position) {
            case 0:
                break;
            default:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {names[position - 1],routeTags[position - 1], stopTags[position - 1], colors[position -1]};
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

    public boolean addDestination() {
        greenTepStopTags = getResources().getStringArray(R.array.green_tep_stop_titles);
        greenHubStopTags = getResources().getStringArray(R.array.green_hub_stop_titles);
        greenFourteenthStopTags = getResources().getStringArray(R.array.green_fourteenth_stop_titles);
        trolleyHubStopTags = getResources().getStringArray(R.array.trolley_hub_stop_titles);
        trolleyMartaStopTags = getResources().getStringArray(R.array.trolley_marta_stop_titles);
        emoryEmoryStopTags = getResources().getStringArray(R.array.emory_emory_stop_titles);
        emoryGatechStopTags = getResources().getStringArray(R.array.emory_gatech_stop_titles);
        nightFittenStopTags = getResources().getStringArray(R.array.night_fitten_stop_titles);
        nightCulcStopTags = getResources().getStringArray(R.array.night_culc_stop_titles);

        for (String tag : greenTepStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To TEP- \n" + name;
                return true;
            }
        }
        for (String tag : greenHubStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To Hub- \n" + name;
                return true;
            }
        }
        for (String tag : greenFourteenthStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To 14th Street- \n" + name;
                return true;
            }
        }
        for (String tag : trolleyHubStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To Hub- \n" + name;
                return true;
            }
        }
        for (String tag : trolleyMartaStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To MARTA- \n" + name;
                return true;
            }
        }
        for (String tag : emoryEmoryStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To Emory- \n" + name;
                return true;
            }
        }
        for (String tag : emoryGatechStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To Georgia Tech- \n" + name;
                return true;
            }
        }
        for (String tag : nightFittenStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To Fitten Hall- \n" + name;
                return true;
            }
        }
        for (String tag : nightCulcStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To CULC- \n" + name;
                return true;
            }
        }
        return false;
    }
}
