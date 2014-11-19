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
        ColorDrawable headerColor = new ColorDrawable(
                Color.parseColor("#ffca28"));
        actionBar.setBackgroundDrawable(headerColor);
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
        String[] strings;
        switch (position) {
            case 0:
                break;
            case 1:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Fitten Hall","blue","fitthall_a", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 2:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"McMillian & 8th","blue","mcmil8th", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 3:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"8th & Hemphill","blue","8thhemp", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 4:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Hemphill","blue","fershemp_ob", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 5:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"CRC","blue","reccent", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 6:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Student Center","blue","studcentr", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 7:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Transit Hub","blue","fershub", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 8:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Tech Tower","blue","cherfers", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 9:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Avenue Apartments","blue","naveapts_a", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 10:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & North Ave","blue","technorth", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 11:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 3rd","blue","3rdtech", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 12:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 4th","blue","4thtech", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 13:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood and 5th","blue","5thtech", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 14:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst and Fowler","blue","fersfowl", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 15:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Klaus","blue","fersklau", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 16:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Atlantic","blue","fersatla", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            case 17:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & State","blue","fersstat", "#0000FF"};
                intent.putExtra("extra", strings);
                intent.putExtra("started_from", "other");
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating activity");
        }
    }

}
