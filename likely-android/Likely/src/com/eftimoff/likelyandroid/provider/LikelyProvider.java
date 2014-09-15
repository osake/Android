package com.eftimoff.likelyandroid.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.eftimoff.likelyandroid.database.LikelyDbHelper;
import com.eftimoff.likelyandroid.database.LikelyContract.JokeEntry;

public class LikelyProvider extends ContentProvider {

	/* Constants */

	private static final String AUTHORITY = "com.eftimoff.likelyandroid.jokes.contentprovider";
	public static final String QUERY_PARAMETER_LIMIT = "limit";

	// used for the UriMacher
	private static final int JOKES = 1;
	private static final int JOKE_ID = 2;
	private static final int SEARCH = 3;

	private static final String BASE_PATH = "jokes";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
			+ BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/joke";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, JOKES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", JOKE_ID);
		sURIMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
	}

	private static final HashMap<String, String> SEARCH_SUGGEST_PROJECTION_MAP;
	static {
		SEARCH_SUGGEST_PROJECTION_MAP = new HashMap<String, String>();
		SEARCH_SUGGEST_PROJECTION_MAP.put(JokeEntry._ID, JokeEntry._ID);
		SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1,
				JokeEntry.COLUMN_TEXT + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
		SEARCH_SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
				JokeEntry._ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
	}

	/* Fields */

	private LikelyDbHelper mSwipeDbHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase sqlDB = mSwipeDbHelper.getWritableDatabase();

		int rowsDeleted = 0;
		switch (uriType) {
		case JOKES:
			rowsDeleted = sqlDB.delete(JokeEntry.TABLE_NAME, selection, selectionArgs);
			break;
		case JOKE_ID:
			final String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(JokeEntry.TABLE_NAME, JokeEntry.COLUMN_ID + "=" + id,
						null);
			} else {
				rowsDeleted = sqlDB.delete(JokeEntry.TABLE_NAME, JokeEntry.COLUMN_ID + "=" + id
						+ " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase sqlDB = mSwipeDbHelper.getWritableDatabase();

		long id = 0;
		switch (uriType) {
		case JOKES:
			id = sqlDB.insert(JokeEntry.TABLE_NAME, JokeEntry.COLUMN_NULLABLE, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		mSwipeDbHelper = new LikelyDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
		checkColumns(projection);

		final String limit = uri.getQueryParameter(QUERY_PARAMETER_LIMIT);

		// Set the table
		queryBuilder.setTables(JokeEntry.TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case JOKES:
			break;
		case JOKE_ID:
			// adding the ID to the original query
			queryBuilder.appendWhere(JokeEntry.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		case SEARCH:
			final SQLiteDatabase db = mSwipeDbHelper.getReadableDatabase();
			selectionArgs = new String[] { "%" + selectionArgs[0].toLowerCase(Locale.getDefault())
					+ "%" };
			queryBuilder.setProjectionMap(SEARCH_SUGGEST_PROJECTION_MAP);
			final Cursor cursor = queryBuilder.query(db, null, selection, selectionArgs, null,
					null, null, null);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		final SQLiteDatabase db = mSwipeDbHelper.getWritableDatabase();
		final Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null,
				null, sortOrder, limit);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase sqlDB = mSwipeDbHelper.getWritableDatabase();

		int rowsUpdated = 0;
		switch (uriType) {
		case JOKES:
			rowsUpdated = sqlDB.update(JokeEntry.TABLE_NAME, values, selection, selectionArgs);
			break;
		case JOKE_ID:
			final String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(JokeEntry.TABLE_NAME, values, JokeEntry.COLUMN_ID + "="
						+ id, null);
			} else {
				rowsUpdated = sqlDB.update(JokeEntry.TABLE_NAME, values, JokeEntry.COLUMN_ID + "="
						+ id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { JokeEntry._ID, JokeEntry.COLUMN_BACKGROUND_COLOR,
				JokeEntry.COLUMN_CATEGORY, JokeEntry.COLUMN_ID, JokeEntry.COLUMN_LIKES,
				JokeEntry.COLUMN_RATING, JokeEntry.COLUMN_TEXT, JokeEntry.COLUMN_SEEN,
				JokeEntry.COLUMN_VOTES, JokeEntry.COLUMN_DATE };
		if (projection != null) {
			final HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			final HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}
