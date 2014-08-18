package com.janderson.gtnextbus.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.adapters.FavoriteAdapter;
import com.janderson.gtnextbus.items.StopItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    private boolean mSwiping;
    private static final int MOVE_DURATION = 150;
    private static final int SWIPE_DURATION = 250;
    private int removedPosition;
    private ColorDrawable headerColor;
    private HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_activity, R.anim.stay_put_activity);
        setContentView(R.layout.activity_alerts);
        getActionBar().setTitle("Active Alerts");
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
        alertItems.add(new StopItem("Active Alerts", "#000000"));
        if (!keys.isEmpty()) {
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
        adapter = new FavoriteAdapter(this.getApplicationContext(), alertItems, mTouchListener);
        alertList.setAdapter(adapter);
        alertList.setOnScrollListener(new AbsListView.OnScrollListener() {

            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                final int currentFirstVisibleItem = alertList.getFirstVisiblePosition();
                if (currentFirstVisibleItem > mLastFirstVisibleItem)
                {
                    getActionBar().hide();
                }
                else if (currentFirstVisibleItem < mLastFirstVisibleItem)
                {
                    if (!getActionBar().isShowing()) {
                        getActionBar().show();
                    }
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;
            }
        });
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
        if (sharedPreferences.getBoolean("transparentNav", true)) {
            Window window = getWindow();
            if (android.os.Build.VERSION.SDK_INT>=19) {
                if(getResources().
                        getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top_translucent);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom_translucent);
                    alertList.setPadding(0, topPadding, 0, bottomPadding);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom);
                    alertList.setPadding(0, topPadding, 0 , bottomPadding);
                }
            }
        } else {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int topPadding = getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_top);
            alertList.setPadding(0, topPadding, 0 , 0);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.stay_put_activity, R.anim.slide_out_activity);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_put_activity, R.anim.slide_out_activity);
    }

    private void removeItem(int position) {
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

        SharedPreferences alertPref = getSharedPreferences("alerts", MODE_PRIVATE);
        alertPref.edit().remove(stringKey).apply();

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
        Toast.makeText(this, "Alert cancelled", Toast.LENGTH_SHORT).show();
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;
        boolean mItemPressed = false;

        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(AlertsActivity.this).getScaledTouchSlop();
            }
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = motionEvent.getX() + view.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if(!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                           mSwiping = true;
                            alertList.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (mSwiping) {
                        view.setTranslationX((x - mDownX));
                        view.setAlpha(1 - deltaXAbs / view.getWidth());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mSwiping) {
                        x = motionEvent.getX() + view.getTranslationX();
                        deltaX = x - mDownX;
                        deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > view.getWidth() / 4) {
                            fractionCovered = deltaXAbs / view.getWidth();
                            endX = deltaX < 0 ? - view.getWidth() : view.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            fractionCovered = 1 - (deltaXAbs / view.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        alertList.setEnabled(false);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            view.animate().setDuration(duration).
                                    alpha(endAlpha).translationX(endX)
                                    .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    view.setAlpha(1);
                                    view.setTranslationX(0);
                                    if (remove) {
                                        animateRemoval(alertList, view);
                                    } else {
                                        mSwiping = false;
                                        alertList.setEnabled(true);
                                    }
                                }
                            });
                        } else {
                            view.animate().setDuration(duration).
                                    alpha(endAlpha).translationX(endX).
                                    withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.setAlpha(1);
                                            view.setTranslationX(0);
                                            if (remove) {
                                                animateRemoval(alertList, view);
                                            } else {
                                                mSwiping = false;
                                                alertList.setEnabled(true);
                                            }
                                        }
                                    });
                        }
                    }
                    mItemPressed = false;
                    alertList.requestDisallowInterceptTouchEvent(false);
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (listview.getChildCount() == 2) {
                View header = listview.getChildAt(0);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    header.animate().setDuration(MOVE_DURATION).alpha(0)
                            .translationX(1200).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            noAlertsImage.setVisibility(View.VISIBLE);
                            noAlertsText.setVisibility(View.VISIBLE);
                            alertList.setVisibility(View.GONE);
                            noAlertsText.setText("You don't have any active alerts!");
                            noAlertsImage.setImageResource(R.drawable.ic_empty_alerts);
                        }
                    });
                } else {
                    header.animate().setDuration(MOVE_DURATION).alpha(0)
                            .translationX(1200).withEndAction(
                            new Runnable() {
                                @Override
                                public void run() {
                                    noAlertsImage.setVisibility(View.VISIBLE);
                                    noAlertsText.setVisibility(View.VISIBLE);
                                    alertList.setVisibility(View.GONE);
                                    noAlertsText.setText("You don't have any active alerts!");
                                    noAlertsImage.setImageResource(R.drawable.ic_empty_alerts);
                                }
                            }
                    );
                }
            }
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = adapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        int position = listview.getPositionForView(viewToRemove);
        removedPosition = position;
        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = removedPosition; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    child.animate().setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mSwiping = false;
                                            listview.setEnabled(true);
                                        }
                                    });
                                } else {
                                    child.animate().withEndAction(new Runnable() {
                                        public void run() {
                                            mSwiping = false;
                                            listview.setEnabled(true);
                                        }
                                    });
                                    firstAnimation = false;
                                }
                            }
                        }
                    } else {
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                child.animate().setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mSwiping = false;
                                        listview.setEnabled(true);
                                    }
                                });
                            } else {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mSwiping = false;
                                        listview.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    }
                }
                return true;
            }
        });
        alertItems.remove(position);
        mItemIdTopMap.clear();
        adapter.notifyDataSetChanged();
        removeItem(position);
    }
}
