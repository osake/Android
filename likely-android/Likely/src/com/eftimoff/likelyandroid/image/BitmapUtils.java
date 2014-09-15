package com.eftimoff.likelyandroid.image;

import java.io.IOException;
import java.io.InputStream;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * Utility methods for managing bitmaps.
 */
class BitmapUtils {

	/**
	 * Decoding {@link InputStream} into a {@link Bitmap}.
	 * 
	 * @author Georgi Eftimov in Apr 11, 2014 11:19:23 AM
	 * 
	 * @param inputStream
	 *            - An {@link InputStream} from the bitmap file.
	 * 
	 * @param reqWidth
	 *            - the width you want the bitmap to be.
	 * @param reqHeight
	 *            - the height you want the bitmap to be.
	 * @return {@link Bitmap} completed.
	 */
	public static Bitmap decode(final InputStream inputStream, final int reqWidth,
			final int reqHeight) {
		// First decode only image dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, options);

		// Calculate inSampleSize scale factor
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Try to reset the InputStream to be able to use it again. This is
		// needed for API19 (KitKat).
		try {
			inputStream.reset();
		} catch (IOException e) {
		}

		// Decode bitmap image with the new calculated dimensions
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(inputStream, null, options);
	}

	/**
	 * Decoding {@link InputStream} into a {@link Bitmap}.
	 * 
	 * @author Georgi Eftimov in Apr 11, 2014 11:19:23 AM
	 * 
	 * @param inputStream
	 *            - An {@link InputStream} from the bitmap file.
	 * 
	 * @param reqWidth
	 *            - the width you want the bitmap to be.
	 * @param reqHeight
	 *            - the height you want the bitmap to be.
	 * @return {@link Bitmap} completed.
	 */
	public static Bitmap decode(final InputStream inputStream) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		return BitmapFactory.decodeStream(inputStream, null, options);
	}

	/**
	 * 
	 * Decode the {@link Bitmap} from the resources with given width and height
	 * 
	 * @author Georgi Eftimov in Apr 10, 2014 6:02:23 PM
	 * 
	 * @param res
	 *            - The {@link Resources} in your {@link Application}.
	 * @param resId
	 *            - the resource id of the file.
	 * @param reqWidth
	 *            - the width you want the bitmap to be.
	 * @param reqHeight
	 *            - the height you want the bitmap to be.
	 * @return {@link Bitmap} completed.
	 */
	public static Bitmap decode(final Resources res, final int resId, final int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 
	 * Decode the {@link Bitmap} from the resources with given width and height
	 * 
	 * @author Georgi Eftimov in Apr 10, 2014 6:02:23 PM
	 * 
	 * @param res
	 *            - The {@link Resources} in your {@link Application}.
	 * @param resId
	 *            - the resource id of the file.
	 * @param reqWidth
	 *            - the width you want the bitmap to be.
	 * @param reqHeight
	 *            - the height you want the bitmap to be.
	 * @return {@link Bitmap} completed.
	 */
	public static Bitmap decode(final Resources res, final int resId) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * Calculate the sample size of the image to be putted in memory. If the
	 * return sample size is 4 the bitmap will be 1/4 in size.
	 * 
	 * @author Georgi Eftimov in Apr 10, 2014 5:56:49 PM
	 * 
	 * @param options
	 *            - The {@link Bitmap}{@link Options} are get from the
	 *            {@link Bitmap} without loading it into memory.
	 * @param reqWidth
	 *            - the width you want the bitmap to be.
	 * @param reqHeight
	 *            - the height you want the bitmap to be.
	 * 
	 * @return int - sample size coresponding the times which the {@link Bitmap}
	 *         will small.
	 * 
	 *         Taken from
	 *         <b>http://developer.android.com/training/displaying-bitmaps
	 *         /load-bitmap.html</b>
	 */
	private static int calculateInSampleSize(final BitmapFactory.Options options,
			final int reqWidth, final int reqHeight) {

		// Raw height and width of image

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

}
