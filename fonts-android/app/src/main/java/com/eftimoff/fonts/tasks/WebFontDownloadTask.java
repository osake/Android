package com.eftimoff.fonts.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.eftimoff.fonts.utils.DownloadUtils;

public class WebFontDownloadTask extends AsyncTask<String, Void, Void> {

	public static final String TAG = WebFontDownloadTask.class.getSimpleName();

	private Context context;

	public WebFontDownloadTask(final Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(String... params) {
		if (params[0] == null) {
			return null;
		}
		DownloadUtils.downloadFont(context, params[0]);
		return null;
	}
}
