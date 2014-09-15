package com.eftimoff.likelyandroid.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.eftimoff.likelyandroid.receiver.BootCompleteReceiver;
import com.eftimoff.likelyandroid.receiver.NotificationBroadcastReceiver;
import com.eftimoff.likelyandroid.service.NotificationService;

public class LikelyAlarmManager {

	private static LikelyAlarmManager sInstance = null;

	private Context mContext;
	private AlarmManager mAlarmManager;

	protected LikelyAlarmManager(final Context context) {
		mContext = context;
		mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		// Exists only to defeat instantiation.
	}

	public static LikelyAlarmManager getInstance(final Context context) {
		if (sInstance == null) {
			sInstance = new LikelyAlarmManager(context);
		}
		return sInstance;
	}

	public void setAlarm(final int hour) {
		final Intent myIntent = new Intent(NotificationBroadcastReceiver.INTENT_ACTION);
		final PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, myIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		long alarm = 0;
		final Calendar now = Calendar.getInstance();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		if (calendar.getTimeInMillis() <= now.getTimeInMillis())
			alarm = calendar.getTimeInMillis() + (AlarmManager.INTERVAL_DAY + 1);
		else
			alarm = calendar.getTimeInMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm,
				AlarmManager.INTERVAL_DAY, pendingIntent);
		enableBootCompleteReceiver();
	}

	public void cancelAlarm() {
		final Intent myIntent = new Intent(mContext, NotificationService.class);
		final PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, myIntent, 0);
		mAlarmManager.cancel(pendingIntent);
		disableBootCompleteReceiver();
	}

	private void enableBootCompleteReceiver() {
		ComponentName receiver = new ComponentName(mContext, BootCompleteReceiver.class);
		PackageManager pm = mContext.getPackageManager();

		pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	private void disableBootCompleteReceiver() {
		ComponentName receiver = new ComponentName(mContext, BootCompleteReceiver.class);
		PackageManager pm = mContext.getPackageManager();

		pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

}
