package com.eftimoff.likelyandroid.service;

import java.util.Random;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;

import com.eftimoff.likelyandroid.R;
import com.eftimoff.likelyandroid.activity.FullScreenActivity;
import com.eftimoff.likelyandroid.activity.LikelyActivity;
import com.eftimoff.likelyandroid.database.LikelyContract;
import com.eftimoff.likelyandroid.provider.LikelyProvider;

public class NotificationService extends IntentService {

	public NotificationService() {
		super("NotificationService");
	}

	private NotificationManager mManager;

	@Override
	protected void onHandleIntent(Intent intent) {
		mManager = (NotificationManager) this.getApplicationContext().getSystemService(
				Context.NOTIFICATION_SERVICE);

		Cursor cursor = getContentResolver().query(LikelyProvider.CONTENT_URI, null, null, null,
				null);
		final Random random = new Random();
		final int randomNumberFromDB = random.nextInt(cursor.getCount());
		cursor = getContentResolver().query(LikelyProvider.CONTENT_URI, null, "id = ?",
				new String[] { Integer.toString(randomNumberFromDB) }, null);

		if (cursor.moveToFirst()) {
			final String text = cursor.getString(cursor
					.getColumnIndexOrThrow(LikelyContract.JokeEntry.COLUMN_TEXT));

			// Create the style object with BigTextStyle subclass.
			NotificationCompat.BigTextStyle notiStyle = new NotificationCompat.BigTextStyle();
			notiStyle.setBigContentTitle(getResources().getString(R.string.title_notification));
			notiStyle.bigText(text);

			// Creates an explicit intent for an ResultActivity to receive.
			final Intent resultIntent = new Intent(this, LikelyActivity.class);
			resultIntent.putExtra(FullScreenActivity.EXTRA_ID, randomNumberFromDB);

			final PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
					resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			final Notification notification = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
					.setContentIntent(resultPendingIntent)
					.setContentTitle(getResources().getString(R.string.title_notification))
					.setContentText(text).setStyle(notiStyle).build();
			mManager.notify(0, notification);
		}

	}
}