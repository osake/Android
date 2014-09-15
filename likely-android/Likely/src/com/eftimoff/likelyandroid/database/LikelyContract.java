package com.eftimoff.likelyandroid.database;

import android.provider.BaseColumns;

public final class LikelyContract {

	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.
	public LikelyContract() {
	}

	/* Inner class that defines the table contents */
	public static abstract class JokeEntry implements BaseColumns {
		public static final String TABLE_NAME = "jokes";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_TEXT = "text";
		public static final String COLUMN_CATEGORY = "category";
		public static final String COLUMN_BACKGROUND_COLOR = "backgroundColor";
		public static final String COLUMN_RATING = "rating";
		public static final String COLUMN_LIKES = "likes";
		public static final String COLUMN_VOTES = "votes";
		public static final String COLUMN_SEEN = "seen";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_NULLABLE = "null";
	}
}
