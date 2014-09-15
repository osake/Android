package com.eftimoff.fonts.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.eftimoff.fonts.FontsApplication;
import com.eftimoff.fonts.contants.WebFontsContants;
import com.eftimoff.fonts.models.Font;
import com.eftimoff.fonts.models.Fonts;
import com.eftimoff.fonts.net.WebFontsService;
import com.eftimoff.fonts.utils.DownloadUtils;

import java.io.File;

public class StartService extends IntentService {

	/* Constants. */
	public static final String EXTRA_DONE = "extra_done";
	public static final String EXTRA_MAX = "extra_max";
	public static final String UPDATE_RESULT_ACTION = "com.eftimoff.fonts.services.StartService.UPDATE_RESULT";

	private static final String TAG = StartService.class.getSimpleName();
	private static final String INTENT_ACTION = "com.eftimoff.fonts.services.StartService.ACTION";
	private static final String ALPHA = "alpha";

	/* Private fields. */

	private WebFontsService webFontsService;

	/* Constructors. */

	public StartService() {
		super("StartService");
	}

	/* Public static methods. */

	public static Intent makeIntent() {
		return new Intent(INTENT_ACTION);
	}

	/* Private methods. */

	private void sendBroadcastDone() {
		final Intent intent = new Intent(UPDATE_RESULT_ACTION);
		intent.putExtra(EXTRA_DONE, true);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}


	private void sendUpdateBroadcast(final int max) {
		final Intent intent = new Intent(UPDATE_RESULT_ACTION);
		intent.putExtra(EXTRA_MAX, max);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	/* Public methods. */

	@Override
	public void onCreate() {
		super.onCreate();
		webFontsService = FontsApplication.getWebFontService();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "ARIVED");
		final Fonts fonts = webFontsService.listFonts(ALPHA, WebFontsContants.WEB_FONTS_KEY);
		Log.i(TAG, Integer.toString(fonts.getItems().size()));

		//fake broadcast
		for (int i = 0; i < fonts.getItems().size(); i++) {

			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendUpdateBroadcast(fonts.getItems().size());
		}
		sendBroadcastDone();

		for (int i = 0; i < fonts.getItems().size(); i++) {
			final Font font = fonts.getItems().get(i);

			final String firstFilename = font.getFiles().get(font.getVariants().get(0));
			if (firstFilename != null) {
				if (!new File(getApplicationContext().getCacheDir(), DownloadUtils.getFilename(firstFilename)).exists()) {
					DownloadUtils.downloadFont(getApplicationContext(), firstFilename);
				}
			}
		}
	}

	/* Inner classes. */


}
