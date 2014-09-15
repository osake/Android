package com.eftimoff.likelyandroid.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;

/**
 * Implementation of the {@link BitmapManager} interface that uses caching and
 * asynchronous loading.
 */
class BitmapManagerImpl implements BitmapManager {

	/**
	 * Implementation of the {@link AsyncTask} class that loads asynchronous a
	 * bitmap image.
	 */
	private abstract class BitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {

		private List<OnLoadListener> mOnLoadListeners;
		private List<OnErrorListener> mOnErrorListeners;
		private String mKey;

		protected int mWidth;
		protected int mHeight;

		public BitmapAsyncTask(final String key) {
			mKey = key;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap result = null;

			try {
				result = loadBitmap();
			} catch (Exception ex) {
				// If something goes wrong, call the listeners.
				if (mOnErrorListeners != null && !mOnErrorListeners.isEmpty()) {
					for (final OnErrorListener listener : mOnErrorListeners) {
						listener.onError(ex);
					}
				}

				cancel(true);
			}

			return result;

		}

		@Override
		protected void onPostExecute(final Bitmap result) {
			// Call the listeners if any.
			addBitmapToMemoryCache(mKey, result);
			if (mOnLoadListeners != null && !mOnLoadListeners.isEmpty()) {
				for (final OnLoadListener listener : mOnLoadListeners) {
					listener.onLoad(result);
				}
			}
		}

		/**
		 * Loads the bitmap.
		 * 
		 * @return The bitmap loaded.
		 */
		protected abstract Bitmap loadBitmap() throws Exception;
	}

	/**
	 * Implementation of the {@link BitmapAsyncTask} class that uses an
	 * {@link InputStream} for a source.
	 */
	private class BitmapStreamAsyncTask extends BitmapAsyncTask {

		private InputStream mInputStream;

		public BitmapStreamAsyncTask(final String key, final InputStream in) {
			super(key);
			mInputStream = in;
		}

		@Override
		protected Bitmap loadBitmap() {
			Bitmap result = null;

			if (mWidth != 0 && mHeight != 0) {
				result = BitmapUtils.decode(mInputStream, mWidth, mHeight);
			} else {
				result = BitmapUtils.decode(mInputStream);
			}

			return result;
		}
	}

	/**
	 * Implementation of the {@link BitmapAsyncTask} class that uses the assets
	 * folder for a source.
	 */
	private class BitmapAssetsAsyncTask extends BitmapAsyncTask {

		private String mFilename;

		public BitmapAssetsAsyncTask(final String key, final String filename) {
			super(key);
			mFilename = filename;
		}

		@Override
		protected Bitmap loadBitmap() throws IOException {
			Bitmap result = null;
			final AssetManager assets = mContext.getAssets();

			InputStream inputStream = null;
			try {
				inputStream = assets.open(mFilename);
				if (mWidth != 0 && mHeight != 0) {
					result = BitmapUtils.decode(inputStream, mWidth, mHeight);
				} else {
					result = BitmapUtils.decode(inputStream);
				}
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// Cannot close the InputStream
					}
				}
			}

			return result;
		}

	}

	/**
	 * Implementation of the Builder pattern that builds {@link BitmapAsyncTask}
	 * s.
	 * 
	 * @author kaleksandrov
	 * 
	 */
	private class BitmapAsyncTaskBuilder {

		/* Private members */

		private List<OnLoadListener> mOnLoadListeners = new ArrayList<OnLoadListener>();
		private List<OnErrorListener> mOnErrorListeners = new ArrayList<OnErrorListener>();

		private int mWidth;
		private int mHeight;

		private InputStream mInputStream;
		private String mFilename;
		private String mKey;

		/* Constructors */

		public BitmapAsyncTaskBuilder(final String key, final InputStream in) {
			mKey = key;
			mInputStream = in;
		}

		public BitmapAsyncTaskBuilder(final String key, final String filename) {
			mKey = key;
			mFilename = filename;
		}

		/* Public methods */

		/**
		 * Builds a {@link BitmapAsyncTask}.
		 * 
		 * @return new {@link BitmapAsyncTask} object.
		 */
		public BitmapAsyncTask build() {
			// Create the AsyncTask
			BitmapAsyncTask task = null;
			if (mInputStream != null) {
				task = new BitmapStreamAsyncTask(mKey, mInputStream);
			} else if (mFilename != null) {
				task = new BitmapAssetsAsyncTask(mKey, mFilename);
			}

			// Set the width & height if defined
			if (mWidth != 0 && mHeight != 0) {
				task.mWidth = mWidth;
				task.mHeight = mHeight;
			}

			// Attach listeners
			if (!mOnLoadListeners.isEmpty()) {
				task.mOnLoadListeners = mOnLoadListeners;
			}
			if (!mOnErrorListeners.isEmpty()) {
				task.mOnErrorListeners = mOnErrorListeners;
			}

			return task;
		}

		/**
		 * Set the bitmap dimensions. These dimensions are used during decoding
		 * time so lower dimensions will lead to better image processing
		 * performance.
		 * 
		 * @param width
		 *            The width of the bitmap.
		 * @param height
		 *            The height of the bitmap.
		 * @return The current instance of the {@link BitmapAsyncTaskBuilder}
		 */
		public BitmapAsyncTaskBuilder dimens(final int width, final int height) {
			mWidth = width;
			mHeight = height;

			return this;
		}

		/**
		 * Attaches an {@link OnLoadListener}.
		 * 
		 * @param listener
		 *            The listener to be attached.
		 * @return The current instance of the {@link BitmapAsyncTaskBuilder}
		 */
		public BitmapAsyncTaskBuilder addOnLoadListener(final OnLoadListener listener) {
			mOnLoadListeners.add(listener);

			return this;
		}

		/**
		 * Attaches an {@link OnErrorListener}.
		 * 
		 * @param listener
		 *            The listener to attached.
		 * @return The current instance of the {@link BitmapAsyncTaskBuilder}
		 */
		public BitmapAsyncTaskBuilder addOnErrorListener(final OnErrorListener listener) {
			mOnErrorListeners.add(listener);

			return this;
		}
	}

	/* Private members */

	private Context mContext;

	private LruCache<String, Bitmap> mMemoryCache;

	/* Constructors */

	public BitmapManagerImpl(final Context context) {
		mContext = context;
		initCache();
	}

	/* Public methods */

	@Override
	public void load(final InputStream in, final Listener... listeners) {
		final String key = generateKey(in);
		final Bitmap bitmap = getBitmapFromMemCache(key);
		if (bitmap != null) {
			for (Listener listener : listeners) {
				if (listener instanceof OnLoadListener) {
					((OnLoadListener) listener).onLoad(bitmap);
				}
			}
		} else {
			final BitmapAsyncTaskBuilder builder = new BitmapAsyncTaskBuilder(key, in);
			applyListeners(builder, listeners);
			builder.build().execute();
		}
	}

	@Override
	public void load(InputStream in, int width, int height, final Listener... listeners) {
		final String key = generateKey(in, width, height);
		final Bitmap bitmap = getBitmapFromMemCache(key);
		if (bitmap != null) {
			for (Listener listener : listeners) {
				if (listener instanceof OnLoadListener) {
					((OnLoadListener) listener).onLoad(bitmap);
				}
			}
		} else {
			final BitmapAsyncTaskBuilder builder = new BitmapAsyncTaskBuilder(key, in);
			applyListeners(builder, listeners);
			builder.dimens(width, height).build().execute();
		}
	}

	@Override
	public void loadFromAssets(String filename, final Listener... listeners) {
		final String key = generateKey(filename);
		final Bitmap bitmap = getBitmapFromMemCache(key);
		if (bitmap != null) {
			for (Listener listener : listeners) {
				if (listener instanceof OnLoadListener) {
					((OnLoadListener) listener).onLoad(bitmap);
				}
			}
		} else {
			final BitmapAsyncTaskBuilder builder = new BitmapAsyncTaskBuilder(key, filename);
			applyListeners(builder, listeners);
			builder.build().execute();
		}
	}

	@Override
	public void loadFromAssets(String filename, int width, int height, final Listener... listeners) {
		final String key = generateKey(filename, width, height);
		final Bitmap bitmap = getBitmapFromMemCache(key);
		if (bitmap != null) {
			for (Listener listener : listeners) {
				if (listener instanceof OnLoadListener) {
					((OnLoadListener) listener).onLoad(bitmap);
				}
			}
		} else {
			final BitmapAsyncTaskBuilder builder = new BitmapAsyncTaskBuilder(key, filename);
			applyListeners(builder, listeners);
			builder.dimens(width, height).build().execute();
		}
	}

	/* Private methods */

	/**
	 * Attaches the given listeners to the {@link BitmapAsyncTaskBuilder}.
	 * 
	 * @param builder
	 *            The builder the listeners to be attached to.
	 * @param listeners
	 *            The listeners to be attached.
	 */
	private void applyListeners(final BitmapAsyncTaskBuilder builder, final Listener... listeners) {
		for (final Listener listener : listeners) {
			if (listener instanceof OnLoadListener) {
				builder.addOnLoadListener((OnLoadListener) listener);
			} else if (listener instanceof OnErrorListener) {
				builder.addOnErrorListener((OnErrorListener) listener);
			}
		}
	}

	/**
	 * Initializes the cache.
	 */
	private void initCache() {
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	private static final String CACHE_KEY_ITEM_SEPARATOR = "_";

	/**
	 * Generates a key for the cache.
	 * 
	 * @param name
	 *            The filename from of the bitmap image file.
	 * @param width
	 *            The width of the bitmap.
	 * @param height
	 *            The height of the bitmap.
	 * @return The key for the cache.
	 */
	private String generateKey(final String name, final int width, final int height) {
		final StringBuilder builder = new StringBuilder();

		builder.append(name);
		builder.append(CACHE_KEY_ITEM_SEPARATOR);
		builder.append(width);
		builder.append(CACHE_KEY_ITEM_SEPARATOR);
		builder.append(height);

		return builder.toString();
	}

	/**
	 * Generates a key for the cache.
	 * 
	 * @param name
	 *            The filename from of the bitmap image file.
	 * @return The key for the cache.
	 */
	private String generateKey(final String name) {
		return name;
	}

	/**
	 * Generates a key for the cache.
	 * 
	 * @param in
	 *            The {@link InputStream} to the bitmap.
	 * @param width
	 *            The width of the bitmap.
	 * @param height
	 *            The height of the bitmap.
	 * @return The key for the cache.
	 */
	private String generateKey(final InputStream in, final int width, final int height) {
		final StringBuilder builder = new StringBuilder();

		builder.append(in.hashCode());
		builder.append(CACHE_KEY_ITEM_SEPARATOR);
		builder.append(width);
		builder.append(CACHE_KEY_ITEM_SEPARATOR);
		builder.append(height);

		return builder.toString();
	}

	/**
	 * Generates a key for the cache.
	 * 
	 * @param in
	 *            The {@link InputStream} to the bitmap.
	 * @return The key for the cache.
	 */
	private String generateKey(final InputStream in) {
		return Integer.toString(in.hashCode());
	}

	/**
	 * Adds a new bitmap to the cache. If there is already a value for this key,
	 * it is overwritten.
	 * 
	 * @param key
	 *            The key for the cache.
	 * @param bitmap
	 *            The bitmap to be cached.
	 */
	private void addBitmapToMemoryCache(final String key, final Bitmap bitmap) {
		mMemoryCache.put(key, bitmap);
	}

	/**
	 * Tries to load a bitmap from the cache.
	 * 
	 * @param key
	 *            The key for the bitmap in the cache.
	 * @return The cached bitmap or null if nothing found.
	 */
	private Bitmap getBitmapFromMemCache(final String key) {
		return mMemoryCache.get(key);
	}
}
