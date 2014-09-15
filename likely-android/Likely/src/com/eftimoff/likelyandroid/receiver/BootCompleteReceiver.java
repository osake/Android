package com.eftimoff.likelyandroid.receiver;

import com.eftimoff.likelyandroid.application.LikelyApplication;
import com.eftimoff.likelyandroid.constants.SharedPreferencesConstats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		final boolean showJokesOfTheDay = sharedPreferences.getBoolean(
				SharedPreferencesConstats.SHOW_JOKE_OF_THE_DAY, true);
		final String notificationTime = sharedPreferences.getString(
				SharedPreferencesConstats.NOTIFICATION_TIME,
				SharedPreferencesConstats.DEFAULT_NOTIFICATION_TIME);
		if (showJokesOfTheDay) {
			LikelyApplication.getInstance().getLikelyAlarmManager()
					.setAlarm(Integer.parseInt(notificationTime));
		}
	}

}
