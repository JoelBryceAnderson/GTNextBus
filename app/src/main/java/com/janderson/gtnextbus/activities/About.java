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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.gtnextbus.R;

public class About extends Activity {

    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getActionBar().setTitle("About");
        LinearLayout aboutLayout = (LinearLayout) findViewById(R.id.about_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView nameCard = (TextView) findViewById(R.id.joel_name_card);
        nameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (clickCount == 5) {
                    if (nameCard.getText().equals("Beta Pi 1500")) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    nameCard.setText("By Joel Anderson");
                                    nameCard.setTranslationX(-2000);
                                    nameCard.animate().translationX(0).setDuration(350).
                                            setListener(null);
                                }
                            });
                        } else {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    nameCard.setText("By Joel Anderson");
                                    nameCard.setTranslationX(-2000);
                                    nameCard.animate().translationX(0).setDuration(350);
                                }
                            });
                        }
                        clickCount = 0;
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            nameCard.setText("Beta Pi 1500");
                                            nameCard.setTranslationX(-2000);
                                            nameCard.animate().translationX(0).setDuration(350)
                                            .setListener(null);
                                        }
                                    });
                        } else {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            nameCard.setText("Beta Pi 1500");
                                            nameCard.setTranslationX(-2000);
                                            nameCard.animate().translationX(0).setDuration(350);
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
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "JAnderson97@mail.gatech.edu", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "GT NextBus Feedback");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
