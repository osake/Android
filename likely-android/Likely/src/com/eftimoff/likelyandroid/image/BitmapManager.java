package com.eftimoff.likelyandroid.image;

import java.io.InputStream;

import android.graphics.Bitmap;

/**
 * Provides methods for easy, fast and efficient way to manage bitmaps.
 */
public interface BitmapManager {

	/**
	 * A callback listener for the bitmap loading flow.
	 */
	public interface Listener {

	}

	/**
	 * A listener that defines a callback that is invoked when the bitmap is
	 * loaded successfully.
	 */
	public interface OnLoadListener extends Listener {

		/**
		 * Called when the bitmap has been loaded successfully.
		 * 
		 * @param bitmap
		 *            The loaded bitmap object.
		 */
		void onLoad(Bitmap bitmap);
	}

	/**
	 * A listener that defines a callback that is invoked when something goes
	 * wrong during loading.
	 */
	public interface OnErrorListener extends Listener {

		/**
		 * Called when something goes really wrong.
		 * 
		 * @param ex
		 *            The exception that was thrown.
		 */
		void onError(Exception ex);
	}

	/**
	 * Loads a bitmap from the given {@link InputStream} object.
	 * 
	 * @param in
	 *            The {@link InputStream} to be read from.
	 * @param listeners
	 *            The loading flow listeners.
	 */
	void load(InputStream in, Listener... listeners);

	/**
	 * Loads a bitmap from the given {@link InputStream} object.
	 * 
	 * @param in
	 *            The {@link InputStream} to be read from.
	 * @param width
	 *            The required width of the bitmap.
	 * @param height
	 *            The required height of the bitmap.
	 * @param listeners
	 *            The loading flow listeners.
	 */
	void load(InputStream in, int width, int height, Listener... listeners);

	/**
	 * Loads a bitmap with the given filename from the assets.
	 * 
	 * @param filename
	 *            The name of the bitmap file.
	 * @param listeners
	 *            The loading flow listeners.
	 */
	void loadFromAssets(String filename, Listener... listeners);

	/**
	 * Loads a bitmap with the given filename from the assets.
	 * 
	 * @param filename
	 *            The name of the bitmap file.
	 * @param width
	 *            The required width of the bitmap.
	 * @param height
	 *            The required height of the bitmap.
	 * @param listeners
	 *            The loading flow listeners.
	 */
	void loadFromAssets(String filename, int width, int height, Listener... listeners);

}
