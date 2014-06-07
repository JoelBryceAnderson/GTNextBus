package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.FavoriteAdapter;
import com.janderson.gtnextbus.items.StopItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertsActivity extends Activity {

    private String[] alertRoutes;
    private String[] alertTitles;
    private String alertRoute;
    private String alertTitle;
    private int pos = 0;
    private ArrayList<StopItem> alertItems;
    private String title;
    private FavoriteAdapter adapter;
    private TextView noAlertsText;
    private ImageView noAlertsImage;
    private ListView alertList;
    private Set<String> item;
    private int alertNumber;
    private Integer[] alertNumbers;
    private String alertPosString;
    private String alertStop;
    private String[] alertPosStrings;
    private String[] alertStops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        alertList = (ListView) findViewById(R.id.alert_cards);
        noAlertsText = (TextView) findViewById(R.id.no_alerts_text);
        noAlertsImage = (ImageView) findViewById(R.id.no_alerts_image);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        alertItems = new ArrayList<StopItem>();
        SharedPreferences alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
        Map<String, ?> keys = alertPref.getAll();
        alertRoutes = new String[keys.size()];
        alertTitles = new String[keys.size()];
        alertPosStrings = new String[keys.size()];
        alertStops = new String[keys.size()];
        alertNumbers = new Integer[keys.size()];
        if (!keys.isEmpty()) {
            alertItems.add(new StopItem("Active Alerts", "#000000"));
            noAlertsImage.setVisibility(View.GONE);
            noAlertsText.setVisibility(View.GONE);
            alertList.setVisibility(View.VISIBLE);
        } else {
            noAlertsImage.setVisibility(View.VISIBLE);
            noAlertsText.setVisibility(View.VISIBLE);
            alertList.setVisibility(View.GONE);
            noAlertsText.setText("You don't have any active alerts!");
            noAlertsImage.setImageResource(R.drawable.ic_empty_alerts);
        }
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            item = (Set<String>) entry.getValue();
            Object[] itemArray = item.toArray();
            for (int i = 0; i < itemArray.length; i++) {
                if (itemArray[i].toString().startsWith("&")) {
                    alertRoute = itemArray[i].toString().substring(1);
                    alertRoutes[pos] = alertRoute;
                } else if (itemArray[i].toString().startsWith("/")) {
                    alertTitle = itemArray[i].toString().substring(1);
                    alertTitles[pos] = alertTitle;
                } else if (itemArray[i].toString().startsWith("%")) {
                    alertNumber = Integer.parseInt(itemArray[i].toString().substring(1));
                    alertNumbers[pos] = alertNumber;
                } else if (itemArray[i].toString().startsWith("*")) {
                    alertPosString = itemArray[i].toString().substring(1);
                    alertPosStrings[pos] = alertPosString;
                } else if (itemArray[i].toString().startsWith("@")) {
                    alertStop = itemArray[i].toString().substring(1);
                    alertStops[pos] = alertStop;
                }
            }
            title = alertRoute + " to " + alertTitle;
            alertItems.add(new StopItem(title, "#000000"));
            pos++;
        }
        adapter = new FavoriteAdapter(this.getApplicationContext(),
                alertItems);
        alertList.setAdapter(adapter);
        alertList.setOnItemClickListener(new AlertsListClickListener());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class AlertsListClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            removeAlert(position);
        }
    }

    private void removeAlert(final int position) {
        switch (position) {
            case 0:
                break;
            default:
                new AlertDialog.Builder(this)
                        .setTitle("Cancel alert")
                        .setMessage("Cancel this alert?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertItems.remove(position);
                                int arrayPosition = (position - 1);
                                Intent notifIntent = new Intent(getApplicationContext(),
                                        StopActivity.NotificationService.class);
                                PendingIntent pendingIntent = PendingIntent.getService
                                        (getApplicationContext(),
                                                alertNumbers[position - 1], notifIntent, 0);
                                AlarmManager alarmManager = (AlarmManager)
                                        getSystemService(ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);
                                String stringKey = alertRoutes[arrayPosition] +
                                        alertTitles[arrayPosition] + alertStops[arrayPosition] +
                                        alertPosStrings[arrayPosition];


                                List<String> alertRoutesTemp = new LinkedList<String>(
                                        Arrays.asList(alertRoutes));
                                alertRoutesTemp.remove(arrayPosition);
                                alertRoutes = alertRoutesTemp.toArray(new
                                        String[alertRoutesTemp.size()]);


                                List<String> alertTitlesTemp = new LinkedList<String>(
                                        Arrays.asList(alertTitles));
                                alertTitlesTemp.remove(arrayPosition);
                                alertTitles = alertTitlesTemp.toArray(new
                                        String[alertTitlesTemp.size()]);

                                List<String> alertPosStringsTemp = new LinkedList<String>(Arrays.asList(
                                        alertPosStrings));
                                alertPosStringsTemp.remove(arrayPosition);
                                alertPosStrings = alertPosStringsTemp.toArray(new
                                        String[alertPosStringsTemp.size()]);

                                List<String> alertStopsTemp = new LinkedList<String>(
                                        Arrays.asList(alertStops));
                                alertStopsTemp.remove(arrayPosition);
                                alertStops = alertStopsTemp.toArray(new
                                        String[alertStopsTemp.size()]);

                                List<Integer> alertNumbersTemp = new LinkedList<Integer>(
                                        Arrays.asList(alertNumbers));
                                alertNumbersTemp.remove(arrayPosition);
                                alertNumbers = alertNumbersTemp.toArray(new
                                        Integer[alertNumbersTemp.size()]);


                                int lastPosition = alertList.getLastVisiblePosition();
                                if (lastPosition == position && position == 1) {
                                    alertItems.remove(arrayPosition);
                                    noAlertsImage.setVisibility(View.VISIBLE);
                                    noAlertsText.setVisibility(View.VISIBLE);
                                    alertList.setVisibility(View.GONE);
                                    noAlertsText.setText("You don't have any active alerts!");
                                    noAlertsImage.setImageResource(R.drawable.ic_empty_alerts);
                                }
                                SharedPreferences alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
                                alertPref.edit().remove(stringKey).apply();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
        }
    }

}
