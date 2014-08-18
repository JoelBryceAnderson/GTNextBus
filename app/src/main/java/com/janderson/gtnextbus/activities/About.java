package com.janderson.gtnextbus.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.gtnextbus.R;

public class About extends Activity {

    private int clickCount = 0;
    private boolean mSwiping;
    private LinearLayout aboutLayout;
    private WindowManager.LayoutParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_activity, R.anim.stay_put_activity);
        setContentView(R.layout.activity_about);
        getActionBar().setTitle("About");
        aboutLayout = (LinearLayout) findViewById(R.id.about_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView nameCard = (TextView) findViewById(R.id.joel_name_card);
        final LinearLayout aboutDetailsCard = (LinearLayout) findViewById(R.id.about_details_card);
        nameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (clickCount == 5) {
                    if (nameCard.getText().equals("Beta Pi 1500")) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            aboutDetailsCard.animate().translationX(2000).setDuration(350)
                                    .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    nameCard.setText("By Joel Anderson");
                                    aboutDetailsCard.setTranslationX(-2000);
                                    aboutDetailsCard.animate().translationX(0).setDuration(350).
                                            setListener(null);
                                }
                            });
                        } else {
                            aboutDetailsCard.animate().translationX(2000).setDuration(350)
                                    .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    nameCard.setText("By Joel Anderson");
                                    aboutDetailsCard.setTranslationX(-2000);
                                    aboutDetailsCard.animate().translationX(0).setDuration(350);
                                }
                            });
                        }
                        clickCount = 0;
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            aboutDetailsCard.animate().translationX(2000).setDuration(350)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            nameCard.setText("Beta Pi 1500");
                                            aboutDetailsCard.setTranslationX(-2000);
                                            aboutDetailsCard.animate().translationX(0).setDuration(350)
                                            .setListener(null);
                                        }
                                    });
                        } else {
                            aboutDetailsCard.animate().translationX(2000).setDuration(350)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            nameCard.setText("Beta Pi 1500");
                                            aboutDetailsCard.setTranslationX(-2000);
                                            aboutDetailsCard.animate().translationX(0).setDuration(350);
                                        }
                                    });
                        }
                        clickCount = 0;
                    }
                }
            }
        });
        nameCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (nameCard.getText().equals("Beta Pi 1500")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Suck it other fraternities!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        final TextView emailCard = (TextView) findViewById(R.id.email_card);
        emailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
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
                    aboutLayout.setPadding(0, topPadding, 0, bottomPadding);
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    int topPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_top);
                    int bottomPadding = getApplicationContext().
                            getResources().getDimensionPixelSize(R.dimen.padding_bottom);
                    aboutLayout.setPadding(0, topPadding, 0 , bottomPadding);
                }
            }
        } else {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int topPadding = getApplicationContext().
                    getResources().getDimensionPixelSize(R.dimen.padding_top);
            aboutLayout.setPadding(0, topPadding, 0 , 0);
        }
        aboutLayout.setOnTouchListener(mTouchListener);
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

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "JAnderson97@mail.gatech.edu", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "GT NextBus Feedback");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;
        boolean mItemPressed = false;

        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(About.this).getScaledTouchSlop();
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
                            aboutLayout.requestDisallowInterceptTouchEvent(true);
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
                        long duration = (int) ((1 - fractionCovered) * 200);
                        aboutLayout.setEnabled(false);
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
                                                finish();
                                                overridePendingTransition(R.anim.stay_put_activity,
                                                        R.anim.slide_out_activity);
                                            } else {
                                                mSwiping = false;
                                                aboutLayout.setEnabled(true);
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
                                                finish();
                                                overridePendingTransition(R.anim.stay_put_activity,
                                                        R.anim.slide_out_activity);
                                            } else {
                                                mSwiping = false;
                                                aboutLayout.setEnabled(true);
                                            }
                                        }
                                    });
                        }
                    }
                    mItemPressed = false;
                    aboutLayout.requestDisallowInterceptTouchEvent(false);
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

}
