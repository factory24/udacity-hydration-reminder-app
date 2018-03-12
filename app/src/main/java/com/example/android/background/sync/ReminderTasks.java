package com.example.android.background.sync;

import android.content.Context;
import android.text.TextUtils;

import com.example.android.background.utilities.PreferenceUtilities;

public class ReminderTasks {

    public static String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    public static void executeTask(Context context, String action) {
        if (TextUtils.equals(ACTION_INCREMENT_WATER_COUNT, action)) {
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
    }

}