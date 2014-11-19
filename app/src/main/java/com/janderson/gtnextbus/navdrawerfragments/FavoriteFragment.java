package com.janderson.gtnextbus.navdrawerfragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.activities.StopActivity;
import com.janderson.gtnextbus.adapters.FavoriteAdapter;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment(){}

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
    private TextView noFavoritesText;
    private ImageView noFavoritesImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Favorite Stops");
        ColorDrawable headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        actionBar.setBackgroundDrawable(headerColor);
        header = getResources().getString(R.string.favorite_destinations_header);
        mRouteList = (ListView) getView().findViewById(R.id.favorite_cards);
        LinearLayout noFavoritesLayout = (LinearLayout)
                getView().findViewById(R.id.no_favorites_layout);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
            noFavoritesLayout.setLayoutAnimation(null);
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
            for (Object anItemArray : itemArray) {
                if (anItemArray.toString().startsWith("?")) {
                    name = anItemArray.toString().substring(1);
                    names[pos] = name;
                } else if (anItemArray.toString().startsWith("*")) {
                    route = anItemArray.toString().substring(1);
                    routeTags[pos] = route;
                } else if (anItemArray.toString().startsWith("$")) {
                    stop = anItemArray.toString().substring(1);
                    stopTags[pos] = stop;
                } else if (anItemArray.toString().startsWith("#")) {
                    color = anItemArray.toString();
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
        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        pos = 0;
        header = getResources().getString(R.string.favorite_destinations_header);
        mRouteList = (ListView) getView().findViewById(R.id.favorite_cards);
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
                    actionBar.hide();
                }
                else if (currentFirstVisibleItem < mLastFirstVisibleItem)
                {
                    actionBar.show();
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
    }

    private void displayView(int position) {
        Intent intent = null;
        String[] strings;
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
        String[] greenTepStopTags = getResources().getStringArray(R.array.green_tep_stop_titles);
        String[] greenFourteenthStopTags = getResources().getStringArray(R.array.green_fourteenth_stop_titles);
        String[] trolleyHubStopTags = getResources().getStringArray(R.array.trolley_hub_stop_titles);
        String[] trolleyMartaStopTags = getResources().getStringArray(R.array.trolley_marta_stop_titles);
        String[] emoryEmoryStopTags = getResources().getStringArray(R.array.emory_emory_stop_titles);
        String[] emoryGatechStopTags = getResources().getStringArray(R.array.emory_gatech_stop_titles);
        String[] nightFittenStopTags = getResources().getStringArray(R.array.night_fitten_stop_titles);
        String[] nightCulcStopTags = getResources().getStringArray(R.array.night_culc_stop_titles);

        for (String tag : greenTepStopTags) {
            if (tag.contains(routeStopCombo)) {
                name = "-To TEP- \n" + name;
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
