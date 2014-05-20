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

public class RedFragment extends Fragment {

    private RelativeLayout redRouteLayout;
    private ListView mRouteList;
    private String[] routes;
    private ArrayList<RouteItem> redRouteItems;
    private DestinationAdapter adapter;

    public RedFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_red, container, false);
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        routes = getResources().getStringArray(R.array.red_routes);
        redRouteLayout = (RelativeLayout) getView().findViewById(R.id.fragment_red);
        mRouteList = (ListView) getView().findViewById(R.id.red_cards);
        redRouteItems = new ArrayList<RouteItem>();
        for (int i = 0; i < 18; i++) {
            redRouteItems.add(new RouteItem(routes[i]));
        }
        adapter = new DestinationAdapter(getActivity().getApplicationContext(),
                redRouteItems);
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
                strings = new String [] {"Fitten Hall","red","fitten", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 2:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"McMillian & 8th","red","mcm8th", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 3:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"8th & Hemphill","red","8thhemp", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 4:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Hemphill","red","fershemrt", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 5:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & State","red","fersstmrt", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 6:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Atlantic","red","fersatmrt", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 7:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Klaus","red","ferschmrt", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 8:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Ferst & Fowler","red","5thfowl", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 9:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & 5th","red","tech5th", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 10:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood and 4th","red","tech4th", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 11:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood and Bobby Dodd","red","techbob", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 12:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Techwood & North Ave","red","technorth", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 13:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Ave Apartments","red","nortavea_a", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 14:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"North Ave & President's Lot","red","nortpres", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 15:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Tech and Aerospace","red","techaero", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 16:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"Tech and State","red","techstat", "#CC0000"};
                intent.putExtra("extra", strings);
                break;
            case 17:
                intent = new Intent(getActivity(), StopActivity.class);
                strings = new String [] {"CRC","red","765femrt", "#CC0000"};
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
