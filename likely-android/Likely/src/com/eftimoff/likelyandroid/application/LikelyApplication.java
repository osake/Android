package com.eftimoff.likelyandroid.application;

import android.app.Application;

import com.eftimoff.likelyandroid.alarm.LikelyAlarmManager;

public class LikelyApplication extends Application {

	/* Constants */

	private static LikelyApplication INSTANCE;

	/* Fields */

	private LikelyAlarmManager mLikelyAlarmManager;

	/* Constructors */

	public LikelyApplication() {
		INSTANCE = this;
	}

	/* Public methods */

	@Override
	public void onCreate() {
		super.onCreate();
		mLikelyAlarmManager = LikelyAlarmManager.getInstance(getApplicationContext());
	}

	public static synchronized LikelyApplication getInstance() {
		return INSTANCE;
	}

	/* Getters & Setters */

	public LikelyAlarmManager getLikelyAlarmManager() {
		return mLikelyAlarmManager;
	}
}
