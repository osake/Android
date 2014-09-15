package com.eftimoff.fonts;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.eftimoff.fonts.contants.WebFontsContants;
import com.eftimoff.fonts.net.WebFontsService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class FontsApplication extends Application {

	/* Constants. */

	private static final int CACHE_MAX_SIZE = 1500000;
	private static final String TAG = FontsApplication.class.getSimpleName();

	/* Private fields. */

	private static Context context;
	private static WebFontsService webFontsService;

	/* Constructors. */

	/* Public static methods. */

	/* Public methods. */

	public static WebFontsService getWebFontService() {
		if (webFontsService == null) {
			synchronized (TAG) {
				if (webFontsService == null) {
					final OkHttpClient okHttpClient = new OkHttpClient();
					final Cache cache;
					try {
						cache = new Cache(context.getCacheDir(), CACHE_MAX_SIZE);
						okHttpClient.setCache(cache);
						final RestAdapter restAdapter = new RestAdapter.Builder()
								.setClient(new OkClient(okHttpClient))
								.setLogLevel(RestAdapter.LogLevel.BASIC)
								.setEndpoint(WebFontsContants.WEB_FONTS_ENDPOINT)
								.build();
						webFontsService = restAdapter.create(WebFontsService.class);
					} catch (IOException e) {
						Log.e(TAG, "The cache dir : " + context.getCacheDir().getAbsolutePath() + " is not available.");
					}
				}
			}
		}
		return webFontsService;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.context = getApplicationContext();
	}

	/* Private methods. */

	/* Inner classes. */

}
