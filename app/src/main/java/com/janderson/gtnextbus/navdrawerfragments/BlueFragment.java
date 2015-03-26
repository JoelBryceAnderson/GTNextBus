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

public class BlueFragment extends Fragment {

    public BlueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blue, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Blue Route");
        String[] routes = getResources().getStringArray(R.array.blue_destinations);
        ListView mRouteList = (ListView) getView().findViewById(R.id.blue_cards);
        if (savedInstanceState != null) {
            mRouteList.setLayoutAnimation(null);
        }
        ArrayList<RouteItem> blueRouteItems = new ArrayList<RouteItem>();
        blueRouteItems.add(new RouteItem(routes[0], R.drawable.stinger_two, true));
        for (int i = 1; i < 18; i++) {
            blueRouteItems.add(new RouteItem(routes[i]));
        }
        DestinationAdapter adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                blueRouteItems, true);
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
        String color = "#2196F3";
        intent = new Intent(getActivity(), StopActivity.class);
        switch (position) {
            case 0:
                break;
            case 1:
                strings = new String [] {"Fitten Hall","blue","fitthall_a", color};
                break;
            case 2:
                strings = new String [] {"McMillian & 8th","blue","mcmil8th", color};
                break;
            case 3:
                strings = new String [] {"8th & Hemphill","blue","8thhemp", color};
                break;
            case 4:
                strings = new String [] {"Ferst & Hemphill","blue","fershemp_ob", color};
                break;
            case 5:
                strings = new String [] {"CRC","blue","reccent", color};
                break;
            case 6:
                strings = new String [] {"Student Center","blue","studcentr", color};
                break;
            case 7:
                strings = new String [] {"Transit Hub","blue","fershub", color};
                break;
            case 8:
                strings = new String [] {"Tech Tower","blue","cherfers", color};
                break;
            case 9:
                strings = new String [] {"North Avenue Apartments","blue","naveapts_a", color};
                break;
            case 10:
                strings = new String [] {"Techwood & North Ave","blue","technorth", color};
                break;
            case 11:
                strings = new String [] {"Techwood & 3rd","blue","3rdtech", color};
                break;
            case 12:
                strings = new String [] {"Techwood & 4th","blue","4thtech", color};
                break;
            case 13:
                strings = new String [] {"Techwood and 5th","blue","5thtech", color};
                break;
            case 14:
                strings = new String [] {"Ferst and Fowler","blue","fersfowl", color};
                break;
            case 15:
                strings = new String [] {"Klaus","blue","fersklau", color};
                break;
            case 16:
                strings = new String [] {"Ferst & Atlantic","blue","fersatla", color};
                break;
            case 17:
                strings = new String [] {"Ferst & State","blue","fersstat", color};
                break;
            default:
                break;
        }

        if (intent != null) {
            intent.putExtra("extra", strings);
            intent.putExtra("started_from", "other");
            startActivity(intent);
        } else {
            Log.e("MainActivity", "Error in creating activity");
        }
    }

}
