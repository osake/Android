package com.eftimoff.fonts.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUtils {

	public static final String TAG = DownloadUtils.class.getSimpleName();

	private DownloadUtils() {
	}

	public static String getFilename(final String uri) {
		if (uri == null) {
			return null;
		}
		return uri.substring(uri.lastIndexOf("/") + 1);
	}

	public static boolean downloadFont(final Context context, final String uri) {
		HttpURLConnection urlConnection = null;
		FileOutputStream fileOutput = null;
		InputStream inputStream = null;
		try {
			//set the download URL, a url that points to a file on the internet
			//this is the file to be downloaded
			final URL url = new URL(uri);

			//create the new connection
			urlConnection = (HttpURLConnection) url.openConnection();

			//and connect!
			urlConnection.connect();

			//create a new file, specifying the path, and the filename
			//which we want to save the file as.
			final File file = new File(context.getCacheDir(), getFilename(uri));
			//this will be used to write the downloaded data into the file we created
			fileOutput = new FileOutputStream(file);

			//this will be used in reading the data from the internet
			inputStream = urlConnection.getInputStream();


			//create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength; //used to store a temporary size of the buffer

			//now, read through the input buffer and write the contents to the file
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				//add the data in the buffer to the file in the file output stream (the file on the sd card
				fileOutput.write(buffer, 0, bufferLength);
			}
			//close the output stream when done
			return true;
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutput != null) {
				try {
					fileOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return false;
	}

}
