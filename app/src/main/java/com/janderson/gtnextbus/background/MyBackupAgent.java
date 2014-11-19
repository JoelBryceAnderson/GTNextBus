package com.janderson.gtnextbus.background;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class MyBackupAgent extends BackupAgentHelper {

    static final String favoritePreferences = "saved_favorites";
    static final String FAVORITE_BACKUP_KEY = "fav";

    public void onCreate() {
        SharedPreferencesBackupHelper favoriteHelper =
                new SharedPreferencesBackupHelper(this, favoritePreferences);
        addHelper(FAVORITE_BACKUP_KEY, favoriteHelper);
    }

}
