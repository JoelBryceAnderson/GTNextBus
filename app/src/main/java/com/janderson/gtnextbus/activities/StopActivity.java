package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.StopAdapter;
import com.janderson.gtnextbus.background.ParseFeed;
import com.janderson.gtnextbus.items.StopItem;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

import java.util.ArrayList;;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;


public class StopActivity extends Activity {

    private String[] strings;
    private String title;
    private String route;
    private String stop;
    private String color;
    private RelativeLayout stopLayout;
    private ListView stopList;
    private ArrayList<StopItem> stopItems;
    private StopAdapter adapter;
    private String time = "No Current Prediction";
    private String secondTime;
    private String thirdTime;
    private String[] times;
    private boolean savedStop = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;
    private Set<String> stringSet;
    private String favoriteKey;
    private String onClickText;
    private String routeName;
    private String minutes;
    private SharedPreferences alertPref;
    private String posString;
    private String startedFrom;
    private TextView slideTitle;
    private SeekBar seek;
    private int slideVal;
    private int maxVal;
    private int roundVal;
    private boolean isLessThanTen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        LinearLayout lay = (LinearLayout) findViewById(R.id.lay);
        preferences = getSharedPreferences("saved_favorites", MODE_PRIVATE);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        swipeRefreshLayout.setColorScheme(R.color.white,
                R.color.yellow,
                R.color.white,
                R.color.yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runTask();
            }
        });
        Intent intent = getIntent();
        strings = intent.getStringArrayExtra("extra");
        startedFrom = intent.getStringExtra("started_from");
        title = strings[0];
        route = strings[1];
        stop = strings[2];
        color = strings[3];
        stringSet = new LinkedHashSet<String>();
        stringSet.add("?".concat(title));
        stringSet.add("*".concat(route));
        stringSet.add("$".concat(stop));
        stringSet.add(color);
        favoriteKey = route.concat(stop);
        secondTime = "time";
        thirdTime = "time";
        times = new String[]{time, secondTime, thirdTime};
        createRouteName();
        swipeRefreshLayout.setRefreshing(true);
        runTask();
    }


    private void runTask() {
        RssFeedTask rssTask = new RssFeedTask(this);
        rssTask.execute();
    }

    private void parse() {
        ParseFeed parseFeed = new ParseFeed(createURL(route), stop, time, secondTime, thirdTime);
        time = parseFeed.time;
        secondTime = parseFeed.secondTime;
        thirdTime = parseFeed.thirdTime;
        if (time.equals("0 minutes")) {
            time = "Arriving Now";
        }
        if (secondTime.equals("0 minutes")) {
            secondTime = "Arriving Now";
        }
        if (thirdTime.equals("0 minutes")) {
            thirdTime = "Arriving Now";
        }
        if (time.equals("1 minutes")) {
            time = ("1 minute");
        }
        if (secondTime.equals("1 minutes")) {
            secondTime = ("1 minute");
        }
        if (thirdTime.equals("1 minutes")) {
            thirdTime = ("1 minute");
        }
    }

    private static String createURL(String route) {
        final String s = "http://gtwiki.info/nextbus/nextbus.php?a=georgia-tech&command=predictionsForMultiStops&r=";
        String url = s.concat(route);
        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite, menu);
        if (preferences.contains(favoriteKey)) {
            menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_saved);
            menu.findItem(R.id.action_save).setTitle("Remove favorite");
            savedStop = true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (startedFrom.equals("notification")) {
                    Intent returnToMain = new Intent(this, MainActivity.class);
                    startActivity(returnToMain);
                }
                finish();
                return true;
            case R.id.action_save:
                if (savedStop) {
                    preferences.edit().remove(favoriteKey).commit();
                    Toast.makeText(this, "Removed stop from favorites.", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_action_save);
                    item.setTitle("Add favorite");
                    savedStop = false;

                } else {
                    preferences.edit().putStringSet(favoriteKey, stringSet).commit();
                    Toast.makeText(this, "Added stop to favorites.", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_action_saved);
                    item.setTitle("Remove favorite");
                    savedStop = true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (startedFrom.equals("notification")) {
            Intent returnToMain = new Intent(this, MainActivity.class);
            startActivity(returnToMain);
        }
    }


    private class RssFeedTask extends AsyncTask<String, Void, String> {
        private String result = "";
        public StopActivity activity;

        public RssFeedTask (StopActivity activity) {
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            activity.parse();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            stopLayout = (RelativeLayout) activity.findViewById(R.id.activity_stop);
            stopList = (ListView) activity.findViewById(R.id.stop_cards);
            stopItems = new ArrayList<StopItem>();
            stopItems.add(new StopItem(title));
            stopItems.add(new StopItem(time));
            if (!secondTime.equals("time")) {
                stopItems.add(new StopItem(secondTime));
            }
            if (!thirdTime.equals("time")) {
                stopItems.add(new StopItem(thirdTime));
            }
            adapter = new StopAdapter(activity.getApplicationContext(),
                    stopItems, color);
            SwingRightInAnimationAdapter swingRightInAnimationAdapter = new SwingRightInAnimationAdapter(adapter);
            swingRightInAnimationAdapter.setAbsListView(stopList);
            stopList.setAdapter(swingRightInAnimationAdapter);
            stopList.setOnScrollListener(new AbsListView.OnScrollListener() {

                int mLastFirstVisibleItem = 0;

                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                    final int currentFirstVisibleItem = stopList.getFirstVisiblePosition();
                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        getActionBar().hide();
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        getActionBar().show();
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            });
            stopList.setOnItemClickListener(new TimeClickListener());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class TimeClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            createReminder(position);
        }
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }


    private void createReminder(int position) {
        switch (position) {
            default:
                onClickText = ((StopItem) stopList.getItemAtPosition(position)).getTitle().toString();
                alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
                posString = Integer.toString(position);
                String stringKey = routeName + title + stop + posString;
                if (alertPref.contains(stringKey)) {
                    Toast.makeText(this, "You've already added this alert!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (isNumeric(Character.toString(onClickText.charAt(0)))) {
                        minutes = (Character.toString(onClickText.charAt(0)));
                        if (isNumeric(Character.toString(onClickText.charAt(1)))) {
                            minutes = minutes.concat(Character.toString(onClickText.charAt(1)));
                        }
                        if (isNumeric(Character.toString(onClickText.charAt(2)))) {
                            minutes = minutes.concat(Character.toString(onClickText.charAt(2)));
                        }
                        LinearLayout linear=new LinearLayout(this);
                        linear.setOrientation(LinearLayout.VERTICAL);
                        seek=new SeekBar(this);
                        if (Integer.parseInt(minutes) > 10) {
                            maxVal = Integer.parseInt(minutes) - 1;
                            isLessThanTen = false;
                        } else {
                            maxVal = (Integer.parseInt(minutes) - 1) * 10;
                            isLessThanTen =  true;
                        }
                        seek.setMax(maxVal);
                        slideTitle = new TextView(this);
                        slideTitle.setText("Set alert for 1 minute from now.");
                        slideTitle.setPadding(40, 40, 40, 40);
                        slideTitle.setTextSize(20);
                        linear.addView(slideTitle);
                        slideVal = seek.getProgress();
                        if (!minutes.equals("1")) {
                            linear.addView(seek);
                            slideTitle.setText("Set alert for when the bus arrives.");
                        }
                        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                if (isLessThanTen) {
                                    if (i % 10 == 0) {
                                        if (i / 10 == 1) {
                                            slideTitle.setText("Set alert for " + String.valueOf(i / 10)
                                                    + " minute before the bus arrives.");
                                        } else if (i / 10 == 0) {
                                            slideTitle.setText("Set alert for when the bus arrives.");
                                        }else {
                                            slideTitle.setText("Set alert for " + String.valueOf(i / 10)
                                                    + " minutes before the bus arrives.");
                                        }
                                        slideVal = i / 10;
                                    }
                                    roundVal = i;
                                } else {
                                    if (i == 1) {
                                        slideTitle.setText("Set alert for " + String.valueOf(i)
                                                + " minute before the bus arrives.");
                                    } else if (i == 0) {
                                        slideTitle.setText("Set alert for when the bus arrives.");
                                    }else {
                                        slideTitle.setText("Set alert for " + String.valueOf(i)
                                                + " minutes before the bus arrives.");
                                    }
                                    slideVal = i;
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                if (isLessThanTen) {
                                    if (roundVal % 10 != 0) {
                                        double rounded = 10 * Math.round(((double)roundVal) / 10);
                                        int roundInt = (int) rounded;
                                        seek.setProgress(roundInt);
                                    }
                                }
                            }
                        });
                        new AlertDialog.Builder(this)
                                .setTitle("Set alert")
                                .setView(linear)
                                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        notifyManager();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }
                }
        }
    }

    private void notifyManager() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notifIntent = new Intent(this, NotificationService.class);
        notifIntent.putExtra("extra", strings);
        notifIntent.putExtra("routeName", routeName);
        notifIntent.putExtra("title", title);
        notifIntent.putExtra("stop", stop);
        notifIntent.putExtra("posString", posString);
        notifIntent.putExtra("slideVal", String.valueOf(slideVal));
        Random generator = new Random();
        int randomNumber = generator.nextInt();
        PendingIntent pendingIntent = PendingIntent.getService(this,
                randomNumber, notifIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                Integer.parseInt(minutes) * 60000 - slideVal * 60000, pendingIntent);
        Toast.makeText(this, "Alert set.", Toast.LENGTH_SHORT).show();
        String alertRoute = "&".concat(routeName);
        String alertTitle = "/".concat(title);
        String alertNumber = "%".concat(Integer.toString(randomNumber));
        String alertStop = "@".concat(stop);
        String alertPosString = "*".concat(posString);
        LinkedHashSet<String> alertStringSet = new LinkedHashSet<String>();
        alertStringSet.add(alertRoute);
        alertStringSet.add(alertTitle);
        alertStringSet.add(alertNumber);
        alertStringSet.add(alertStop);
        alertStringSet.add(alertPosString);
        alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
        String stringKey = routeName + title + stop + posString;
        alertPref.edit().putStringSet(stringKey, alertStringSet).apply();
    }

    public static class NotificationService extends IntentService {

        private String notifText;
        private String tickerText;

        public NotificationService() {
            super("Notification Service");
        }

        @Override
        protected void onHandleIntent(Intent intent){
            String[] strings = intent.getStringArrayExtra("extra");
            String routeName = intent.getStringExtra("routeName");
            String title = intent.getStringExtra("title");
            String posString = intent.getStringExtra("posString");
            String stop = intent.getStringExtra("stop");
            String slideVal = intent.getStringExtra("slideVal");
            long[] vibrationPattern = {0, 200, 0, 200};
            if (Integer.parseInt(slideVal) > 1) {
                tickerText = "The " + routeName + " is arriving at " + title + " in " +
                        slideVal + " minutes.";
                notifText = "Arriving at " + title + " in " +
                        slideVal + " minutes.";
            } else {
                tickerText = "The " + routeName + " is arriving at " + title + ".";
                notifText = "Arriving at " + title + ".";
            }
            intent = new Intent(this, StopActivity.class);
            intent.putExtra("extra", strings);
            intent.putExtra("started_from", "notification");
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notifyID = 1;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_stat_bus)
                            .setContentTitle(routeName)
                            .setContentText(notifText)
                            .setTicker(tickerText)
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (sharedPreferences.getBoolean("soundNotification", true)) {
                mBuilder.setSound(uri);
            }
            if (sharedPreferences.getBoolean("vibrateNotification", true)) {
                mBuilder.setVibrate(vibrationPattern);
            }
            SharedPreferences alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
            String stringKey = routeName + title + stop + posString;
            alertPref.edit().remove(stringKey).apply();
            mNotificationManager.notify(notifyID, mBuilder.build());
        }
    }

    private void createRouteName() {
        if (route.equals("red")) {
            routeName = "Red Bus";
        }
        if (route.equals("blue")) {
            routeName = "Blue Bus";
        }
        if (route.equals("trolley")) {
            routeName = "Trolley";
        }
        if (route.equals("emory")) {
            routeName = "Emory Shuttle";
        }
        if (route.equals("green")) {
            routeName = "Green Bus";
        }
        if (route.equals("night")) {
            routeName = "Midnight Rambler";
        }
    }
}
