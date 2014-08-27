package com.eftimoff.knowledge.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

	/* Constants. */

	private static final String TAG = DateUtils.class.getSimpleName();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String HOURS = "h";
	private static final String MINUTES = "m";
	private static final String SECONDS = "s";

	private DateUtils() {
	}

	public static long getDifferenceFromNow(final String stringDate) {
		try {
			return getDifferenceFromNow(DATE_FORMAT.parse(stringDate));
		} catch (ParseException e) {
			Log.e(TAG, "Exception while parsing the date.");
			return -1;
		}
	}

	public static long getDifferenceFromNow(final Date date) {
		Log.i(TAG, date.toString() + "    " + new Date().toString());
		return date.getTime() - new Date().getTime();
	}

	public static String formatMilliseconds(final long milliseconds) {
		final long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
		if (hours > 0) {
		}
		final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
		if (minutes > 0) {
		}
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
		if (seconds > 0) {
		}
		return String.format("%02d" + HOURS + ":%02d" + MINUTES + ":%02d" + SECONDS, hours, minutes, seconds);
	}
}
