package com.theleafapps.pro.hydrationreminder.sync;

import android.content.Context;
import android.text.TextUtils;

import com.theleafapps.pro.hydrationreminder.utilities.NotificationUtils;
import com.theleafapps.pro.hydrationreminder.utilities.PreferenceUtilities;

public class ReminderTasks {

    public static String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static String ACTION_CHARGING_REMINDER = "charging-reminder";

    public static void executeTask(Context context, String action) {
        if (TextUtils.equals(ACTION_INCREMENT_WATER_COUNT, action)) {
            incrementWaterCount(context);
        } else if (TextUtils.equals(ACTION_DISMISS_NOTIFICATION, action)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (TextUtils.equals(ACTION_CHARGING_REMINDER, action)) {
            issueChargingReminder(context);
        }
    }

    private static void issueChargingReminder(Context context) {
        PreferenceUtilities.incrementChargingReminderCount(context);
        NotificationUtils.remindUserBecauseCharging(context);
    }

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
        NotificationUtils.clearAllNotifications(context);
    }
}