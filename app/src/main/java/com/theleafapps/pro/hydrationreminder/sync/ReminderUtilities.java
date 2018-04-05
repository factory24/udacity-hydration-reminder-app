package com.theleafapps.pro.hydrationreminder.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by aviator on 16/03/18.
 */

public class ReminderUtilities {

    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";
    private static FirebaseJobDispatcher dispatcher;

    private static boolean sInitialized;
    private static Driver driver;

    private static int interval_time;

    synchronized public static void scheduleChargingReminder(Context context) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String reminder_interval_time = sharedPrefs.getString("reminder_interval_time", "15");

        Log.d(TAG, "value: " + interval_time);

        if (interval_time != 0) {
            if (sInitialized && Integer.parseInt(reminder_interval_time) == interval_time)
                return;
            else {
                dispatcher.cancel(REMINDER_JOB_TAG);
            }
        }

        driver = new GooglePlayDriver(context);
        dispatcher = new FirebaseJobDispatcher(driver);

        int reminder_interval_minutes = Integer.parseInt(reminder_interval_time);
        int reminder_interval_seconds = (int) (TimeUnit.MINUTES.toSeconds(reminder_interval_minutes));
        int sync_flextime_seconds = reminder_interval_seconds;
        interval_time = reminder_interval_minutes;

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(WaterReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        reminder_interval_seconds,
                        reminder_interval_seconds + sync_flextime_seconds))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }

    synchronized public static void cancelHydrationReminder() {
        driver.cancelAll();
    }

    public static boolean getDriver() {
        return driver.isAvailable();
    }
}
