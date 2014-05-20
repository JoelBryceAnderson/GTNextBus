package com.janderson.gtnextbus.navdrawerfragments;

/**
 * Created by JoelAnderson on 5/15/14.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.janderson.gtnextbus.adapters.DestinationAdapter;
import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.items.RouteItem;
import com.janderson.gtnextbus.activities.StopActivity;

import java.util.ArrayList;

public class BlueFragment extends Fragment {

    private RelativeLayout blueRouteLayout;
    private ListView mRouteList;
    private String[] routes;
    private ArrayList<RouteItem> blueRouteItems;
    private DestinationAdapter adapter;

    public BlueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_blue, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        routes = getResources().getStringArray(R.array.blue_destinations);
        blueRouteLayout = (RelativeLayout) getView().findViewById(R.id.fragment_blue);
        mRouteList = (ListView) getView().findViewById(R.id.blue_cards);
        blueRouteItems = new ArrayList<RouteItem>();
        for (int i = 0; i < 15; i++) {
            blueRouteItems.add(new RouteItem(routes[i]));
        }
        adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                blueRouteItems);
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
        // update the main content by replacing fragments
        Intent intent = null;
        String[] strings =  null;
        ArrayList<String> stringArrayList = null;
        switch (position) {
            case 0:
                break;
            case 1:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Fitten Hall","blue","fitten_a", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 2:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"McMillian & 8th","blue","mcm8th", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 3:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"8th & Hemphill","blue","8thhemp", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 4:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"CRC","blue","recctr", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 5:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Tech & State","blue","techstat", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 6:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Ave & Burge Deck","blue","nortburg", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 7:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Avenue Apartments","blue","nortavea_a", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 8:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & North Ave","blue","technorth", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 9:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 3rd","blue","3rdtech", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 10:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 4th","blue","4thtech", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 11:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 5th","blue","5thtech", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 12:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Fowler","blue","fersforec", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 13:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Klaus","blue","ferschrec", "#0000FF"};
                intent.putExtra("extra", strings);
                break;
            case 14:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Atlantic","blue","fersatrec", "#0000FF"};
                intent.putExtra("extra", strings);
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
