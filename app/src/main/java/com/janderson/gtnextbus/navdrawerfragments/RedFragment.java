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
import com.janderson.gtnextbus.activities.StopActivity;
import com.janderson.gtnextbus.adapters.DestinationAdapter;
import com.janderson.gtnextbus.items.RouteItem;

import java.util.ArrayList;

public class RedFragment extends Fragment {

    public RedFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_red, container, false);
    }

    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Red Route");
        String[] routes = getResources().getStringArray(R.array.red_routes);
        ListView mRouteList = (ListView) getView().findViewById(R.id.red_cards);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
        }
        ArrayList<RouteItem> redRouteItems = new ArrayList<RouteItem>();
        redRouteItems.add(new RouteItem(routes[0], R.drawable.stinger, true));
        for (int i = 1; i < 19; i++) {
            redRouteItems.add(new RouteItem(routes[i]));
        }
        DestinationAdapter adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                redRouteItems, true);
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
        String[] strings = null;
        intent = new Intent(getActivity(), StopActivity.class);
        intent.putExtra("started_from", "other");
        switch (position) {
            case 0:
                break;
            case 1:
                strings = new String [] {"Fitten Hall","red","fitthall", "#CC0000"};
                break;
            case 2:
                strings = new String [] {"McMillian & 8th","red","mcmil8th", "#CC0000"};
                break;
            case 3:
                strings = new String [] {"8th & Hemphill","red","8thhemp", "#CC0000"};
                break;
            case 4:
                strings = new String [] {"Ferst & Hemphill","red","fershemp", "#CC0000"};
                break;
            case 5:
                strings = new String [] {"Ferst & State","red","fersstat", "#CC0000"};
                break;
            case 6:
                strings = new String [] {"Ferst & Atlantic","red","fersatla", "#CC0000"};
                break;
            case 7:
                strings = new String [] {"Klaus","red","klaubldg", "#CC0000"};
                break;
            case 8:
                strings = new String [] {"Ferst & Fowler","red","fersfowl", "#CC0000"};
                break;
            case 9:
                strings = new String [] {"Techwood & 5th","red","tech5th", "#CC0000"};
                break;
            case 10:
                strings = new String [] {"Techwood and 4th","red","tech4th", "#CC0000"};
                break;
            case 11:
                strings = new String [] {"Techwood and Bobby Dodd","red","techbob", "#CC0000"};
                break;
            case 12:
                strings = new String [] {"Techwood & North Ave","red","technorth", "#CC0000"};
                break;
            case 13:
                strings = new String [] {"North Ave Apartments","red","naveapts_a", "#CC0000"};
                break;
            case 14:
                strings = new String [] {"North Ave & President's Lot","red","nortpres", "#CC0000"};
                break;
            case 15:
                strings = new String [] {"Tech Tower","red","ferstcher", "#CC0000"};
                break;
            case 16:
                strings = new String [] {"Transit Hub","red","hubfers", "#CC0000"};
                break;
            case 17:
                strings = new String [] {"Student Center","red","centrstud", "#CC0000"};
                break;

            case 18:
                strings = new String [] {"CRC","red","creccent", "#CC0000"};
                break;
            default:
                break;
        }

        if (intent != null) {
            intent.putExtra("extra", strings);
            startActivity(intent);
        } else {
            Log.e("MainActivity", "Error in creating activity");
        }
    }

}
