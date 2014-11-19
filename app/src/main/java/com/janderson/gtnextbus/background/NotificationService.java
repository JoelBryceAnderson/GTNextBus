package com.janderson.gtnextbus.background;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.janderson.gtnextbus.R;
import com.janderson.gtnextbus.activities.StopActivity;

public class NotificationService extends IntentService {

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
        long[] vibrationPattern = {0, 200, 200, 200};
        String notifText;
        String tickerText;
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
                        .extend(new NotificationCompat.WearableExtender()
                                .setBackground(BitmapFactory.decodeResource(getResources(),
                                        R.drawable.wearable_background)))
                        .setContentText(notifText)
                        .setTicker(tickerText)
                        .setColor(getResources().getColor(R.color.yellow))
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