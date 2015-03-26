package com.janderson.gtnextbus.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.StopAdapter;
import com.janderson.gtnextbus.background.NotificationService;
import com.janderson.gtnextbus.background.ParseFeed;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;



public class StopActivity extends ActionBarActivity {

    private String[] strings;
    private String title;
    private String route;
    private String stop;
    private String color;
    private ListView stopList;
    private String time = "No Current Prediction";
    private String secondTime;
    private String thirdTime;
    private boolean savedStop = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;
    private Set<String> stringSet;
    private String favoriteKey;
    private String routeName;
    private String minutes;
    private SharedPreferences alertPref;
    private String posString;
    private String startedFrom;
    private TextView slideTitle;
    private SeekBar seek;
    private int slideVal;
    private int roundVal;
    private boolean isLessThanTen;
    private Dialog dialog;
    private BackupManager backupManager;
    public ImageButton floatingButton;
    public Animation floatingAnimation;
    public boolean animateFloatingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backupManager = new BackupManager(this);
        setContentView(R.layout.activity_stop);
        android.support.v7.widget.Toolbar mToolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_stop);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Stop Times");
        preferences = getSharedPreferences("saved_favorites", MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
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
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor(color));
        stringSet = new LinkedHashSet<String>();
        stringSet.add("?".concat(title));
        stringSet.add("*".concat(route));
        stringSet.add("$".concat(stop));
        stringSet.add(color);
        favoriteKey = route.concat(stop);
        floatingButton = (ImageButton) findViewById(R.id.floating_star_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable rippleBackground = getDrawable(R.drawable.circle_button_ripple_selector);
            floatingButton.setBackground(rippleBackground);
        }
        floatingAnimation =  AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.grow_out);
        animateFloatingButton = true;
        floatingButton.setVisibility(View.GONE);
        floatingButton.setAnimation(floatingAnimation);
        if (preferences.contains(favoriteKey)) {
            floatingButton.setImageResource(R.drawable.ic_action_saved);
            savedStop = true;
        }
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedStop) {
                    preferences.edit().remove(favoriteKey).apply();
                    floatingButton.setImageResource(R.drawable.ic_action_save);
                    savedStop = false;
                } else {
                    preferences.edit().putStringSet(favoriteKey, stringSet).apply();
                    floatingButton.setImageResource(R.drawable.ic_action_saved);
                    savedStop = true;
                }
                backupManager.dataChanged();
            }
        });
        secondTime = "time";
        thirdTime = "time";
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
        return s.concat(route);
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
            stopList = (ListView) activity.findViewById(R.id.stop_cards);
            ArrayList<StopItem> stopItems = new ArrayList<StopItem>();
            stopItems.add(new StopItem(title));
            stopItems.add(new StopItem(time));
            if (!secondTime.equals("time")) {
                stopItems.add(new StopItem(secondTime));
            }
            if (!thirdTime.equals("time")) {
                stopItems.add(new StopItem(thirdTime));
            }
            StopAdapter adapter = new StopAdapter(activity.getApplicationContext(),
                    stopItems, color);
            stopList.setAdapter(adapter);
            stopList.setOnScrollListener(new AbsListView.OnScrollListener() {

                int mLastFirstVisibleItem = 0;

                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                    final int currentFirstVisibleItem = stopList.getFirstVisiblePosition();
                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        getSupportActionBar().hide();
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        getSupportActionBar().show();
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            });
            stopList.setOnItemClickListener(new TimeClickListener());
            if (animateFloatingButton) {
                floatingButton.setVisibility(View.VISIBLE);
                floatingButton.startAnimation(floatingAnimation);
            }
            animateFloatingButton = false;
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

    public static boolean isNumeric(String str) {
        return str.matches("\\d");
    }

    private void createReminder(int position) {
        switch (position) {
            default:
                String onClickText = ((StopItem) stopList.getItemAtPosition(position))
                        .getTitle().toString();
                alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
                posString = Integer.toString(position);
                String stringKey = routeName + title + stop + posString;
                if (alertPref.contains(stringKey)) {
                    Toast.makeText(this, "You've already added this alert",
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
                        LayoutInflater layoutInflater = LayoutInflater.from(
                                getApplicationContext());
                        View dialogView = layoutInflater.inflate(R.layout.alert_dialog_card, null);
                        seek = (SeekBar) dialogView.findViewById(R.id.seekbar);
                        int maxVal;
                        if (Integer.parseInt(minutes) > 10) {
                            maxVal = Integer.parseInt(minutes) - 1;
                            isLessThanTen = false;
                        } else {
                            maxVal = (Integer.parseInt(minutes) - 1) * 10;
                            isLessThanTen =  true;
                        }
                        seek.setMax(maxVal);
                        slideTitle = (TextView) dialogView.findViewById(R.id.dialog_text);
                        slideVal = seek.getProgress();
                        if (!minutes.equals("1")) {
                            slideTitle.setText("Set alert for when the bus arrives.");
                        } else {
                            slideTitle.setText("Set alert for 1 minute from now.");
                            seek.setVisibility(View.GONE);
                        }
                        Button dialogButton = (Button)
                                dialogView.findViewById(R.id.dialog_button);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notifyManager();
                                dialog.dismiss();
                            }
                        });
                        Button dialogButtonCancel = (Button)
                                dialogView.findViewById(R.id.dialog_button_cancel);
                        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                if (isLessThanTen) {
                                    if (i % 10 == 0) {
                                        if (i / 10 == 1) {
                                            slideTitle.setText(
                                                    "Set alert for " + String.valueOf(i / 10)
                                                    + " minute before the bus arrives.");
                                        } else if (i / 10 == 0) {
                                            slideTitle.setText(
                                                    "Set alert for when the bus arrives.");
                                        } else {
                                            slideTitle.setText(
                                                    "Set alert for " + String.valueOf(i / 10)
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
                                    } else {
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
                                        double rounded = 10 * Math.round(((double) roundVal) / 10);
                                        int roundInt = (int) rounded;
                                        seek.setProgress(roundInt);
                                    }
                                }
                            }
                        });
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setView(dialogView);
                        dialog = builder.create();
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.getWindow().getAttributes().windowAnimations =
                                R.style.dialog_animation;
                        dialog.show();
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
        Toast.makeText(this, "Alert set", Toast.LENGTH_SHORT).show();
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
