package com.eftimoff.sunshine;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;

import com.eftimoff.sunshine.data.WeatherContract;

import java.util.Calendar;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_SET_REPEATING_ALARMS = "com.eftimoff.sunshine.action.SET_REPEATING_ALARM";
    public static final String ACTION_NEW_NOTIFICATION = "com.eftimoff.sunshine.action.NEW_NOTIFICATION";


    private static final int WEATHER_NOTIFICATION_ID = 8915;

    public static final String NOTIFICATION_EXTRA_TITLE = "extra_title";
    public static final String NOTIFICATION_EXTRA_CONDITITION = "extra_codition";
    public static final String NOTIFICATION_EXTRA_HIGH = "extra_high";
    public static final String NOTIFICATION_EXTRA_LOW = "extra_low";
    public static final String NOTIFICATION_EXTRA_ICON = "extra_icon_man_what_is_going_on";


    private static final String[] NOTIFY_WEATHER_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC

    };

    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;
    private static final int INDEX_SHORT_DESC = 3;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT_COMPLETED) || intent.getAction().equals(ACTION_SET_REPEATING_ALARMS)) {

            final Intent mainIntent = makeIntentForNotification(context);
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, mainIntent, 0);
            final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            final String notificationKey = context.getString(R.string.preference_notifications_key);
            final String notificationDefault = context.getString(R.string.preference_notifications_default);
            final boolean showNotifications = sharedPreferences.getBoolean(notificationKey, Boolean.parseBoolean(notificationDefault));
            if (showNotifications) {
                final Calendar firingCal = Calendar.getInstance();
                final Calendar currentCal = Calendar.getInstance();

                firingCal.set(Calendar.HOUR, 8); // At the hour you wanna fire
                firingCal.set(Calendar.MINUTE, 0); // Particular minute
                firingCal.set(Calendar.SECOND, 0); // particular second

                long intendedTime = firingCal.getTimeInMillis();
                long currentTime = currentCal.getTimeInMillis();

                if (intendedTime < currentTime) // you can add buffer time too here to ignore some small differences in milliseconds
                {
                    //set from next day
                    firingCal.add(Calendar.DAY_OF_MONTH, 1);
                    intendedTime = firingCal.getTimeInMillis();
                }

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        intendedTime, AlarmManager.INTERVAL_DAY,
                        pendingIntent);
            } else {
                alarmManager.cancel(pendingIntent);
            }
        }
        if (intent.getAction().equals(ACTION_NEW_NOTIFICATION)) {
            final Intent mainIntent = new Intent(context, MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            final String text = String.format(
                    context.getString(R.string.format_notification),
                    intent.getStringExtra(NOTIFICATION_EXTRA_CONDITITION),
                    Utility.formatTemperature(context, intent.getDoubleExtra(NOTIFICATION_EXTRA_HIGH, 0), true),
                    Utility.formatTemperature(context, intent.getDoubleExtra(NOTIFICATION_EXTRA_LOW, 0), true));


            final Notification notification = new Notification.Builder(context).
                    setSmallIcon(R.mipmap.ic_launcher).
                    setAutoCancel(true).
                    setContentTitle(intent.getStringExtra(NOTIFICATION_EXTRA_TITLE)).
                    setContentText(text).
                    setContentIntent(pendingIntent).
                    build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notification);
        }

    }


    private Intent makeIntentForNotification(final Context context) {
        final Intent intent = new Intent();
        final String applicationName = context.getString(R.string.app_name);

        final Cursor cursor = context.getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI, NOTIFY_WEATHER_PROJECTION, null, null, null);
        if (cursor.moveToFirst()) {
            intent.putExtra(NOTIFICATION_EXTRA_TITLE, applicationName);
            intent.putExtra(NOTIFICATION_EXTRA_CONDITITION, cursor.getString(INDEX_SHORT_DESC));
            intent.putExtra(NOTIFICATION_EXTRA_HIGH, cursor.getDouble(INDEX_MAX_TEMP));
            intent.putExtra(NOTIFICATION_EXTRA_LOW, cursor.getDouble(INDEX_MIN_TEMP));
            intent.putExtra(NOTIFICATION_EXTRA_ICON, Integer.toString(cursor.getInt(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID))));
            intent.setAction(ACTION_NEW_NOTIFICATION);
        }
        cursor.close();
        return intent;
    }
}
