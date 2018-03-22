package com.example.android.background.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.background.MainActivity;
import com.example.android.background.R;
import com.example.android.background.sync.ReminderTasks;
import com.example.android.background.sync.WaterReminderIntentService;

/**
 * Created by aviator on 13/03/18.
 */

public class NotificationUtils {

    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3000;

    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 3001;

    public static NotificationManager notificationManager;

    // This method will create the pending intent which will trigger when
    // the notification is pressed. This pending intent should open up the MainActivity
    private static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // This method is necessary to decode a bitmap needed for the notification.
    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
    }

    // This method will create a notification for charging.
    public static void remindUserBecauseCharging(Context context) {

        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                        .setColorized(true)
                        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .setSmallIcon(R.drawable.ic_drink_notification)
                        .setLargeIcon(largeIcon(context))
                        .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                        .setContentText(context.getString(R.string.charging_reminder_notification_body))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(
                                context.getString(R.string.charging_reminder_notification_body)))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true)
                        .addAction(drinkWaterAction(context))
                        .addAction(ignoreReminderAction(context));

        // If the build version is greater than JELLY_BEAN and lower than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        //Trigger the notification by calling notify on the NotificationManager.
        notificationManager.notify(WATER_REMINDER_PENDING_INTENT_ID, notificationBuilder.build());
    }


    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static NotificationCompat.Action ignoreReminderAction(Context context) {

        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action ignoreReminderAction =
                new NotificationCompat.Action(
                        R.drawable.ic_cancel_black_24px,
                        "No Thanks !",
                        ignoreReminderPendingIntent);

        return ignoreReminderAction;
    }

    private static NotificationCompat.Action drinkWaterAction(Context context) {
        Intent incrementWaterCounterIntent = new Intent(context, WaterReminderIntentService.class);
        incrementWaterCounterIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        PendingIntent incrementWaterCounterPendingIntent = PendingIntent.getService(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                incrementWaterCounterIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action incrementWaterCounterAction =
                new NotificationCompat.Action(
                        R.drawable.ic_drink_notification,
                        "Done !",
                        incrementWaterCounterPendingIntent);

        return incrementWaterCounterAction;
    }
}