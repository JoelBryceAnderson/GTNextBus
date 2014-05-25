package com.janderson.gtnextbus.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.janderson.gtnextbus.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int actionBarColor = Color.parseColor("#FFBB33");
            tintManager.setStatusBarTintColor(actionBarColor);
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
}
