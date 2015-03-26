package com.janderson.gtnextbus.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.gtnextbus.R;

public class About extends ActionBarActivity {

    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        android.support.v7.widget.Toolbar mToolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView nameCard = (TextView) findViewById(R.id.joel_name_card);
        final CardView aboutDetailsCard = (CardView) findViewById(R.id.about_details_card);
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
