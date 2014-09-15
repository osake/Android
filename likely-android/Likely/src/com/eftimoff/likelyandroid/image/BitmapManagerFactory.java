package com.eftimoff.likelyandroid.image;

import android.content.Context;

public class BitmapManagerFactory {

	public static BitmapManager create(final Context context) {
		return new BitmapManagerImpl(context);
	}
}
