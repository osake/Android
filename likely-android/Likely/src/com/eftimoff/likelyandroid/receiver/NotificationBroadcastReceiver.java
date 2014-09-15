package com.eftimoff.likelyandroid.receiver;

import com.eftimoff.likelyandroid.service.NotificationService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class NotificationBroadcastReceiver extends WakefulBroadcastReceiver {

	public static final String INTENT_ACTION = "com.eftimoff.likelyandroid.receiver.NotificationBroadcastReceiver.NEW_NOTIFICATION";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				NotificationService.class.getName());
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}
}