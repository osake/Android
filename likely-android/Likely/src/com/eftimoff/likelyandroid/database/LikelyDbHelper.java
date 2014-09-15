package com.eftimoff.likelyandroid.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eftimoff.likelyandroid.database.LikelyContract.JokeEntry;

public class LikelyDbHelper extends SQLiteOpenHelper {

	private enum Type {
		TEXT(" TEXT"), BOOLEAN(" BOOLEAN"), INTEGER(" INTEGER"), FLOAT(" FLOAT");

		private String text;

		private Type(String text) {
			this.setText(text);
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}
	}

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Jokes.db";
	public static final String PRIMARY_KEY = " PRIMARY KEY";

	private static final String COMMA_SEP = ",";
	private static final String CREATE_JOKES_TABLE = "CREATE TABLE " + JokeEntry.TABLE_NAME + " ("
			+ JokeEntry._ID + Type.INTEGER.getText() + PRIMARY_KEY + COMMA_SEP
			+ JokeEntry.COLUMN_ID + Type.INTEGER.getText() + COMMA_SEP
			+ JokeEntry.COLUMN_BACKGROUND_COLOR + Type.TEXT.getText() + COMMA_SEP
			+ JokeEntry.COLUMN_CATEGORY + Type.TEXT.getText() + COMMA_SEP + JokeEntry.COLUMN_LIKES
			+ Type.INTEGER.getText() + COMMA_SEP + JokeEntry.COLUMN_RATING + Type.FLOAT.getText()
			+ COMMA_SEP + JokeEntry.COLUMN_SEEN + Type.INTEGER.getText() + COMMA_SEP
			+ JokeEntry.COLUMN_VOTES + Type.INTEGER.getText() + COMMA_SEP + JokeEntry.COLUMN_DATE
			+ Type.TEXT.getText() + COMMA_SEP + JokeEntry.COLUMN_TEXT + Type.TEXT.getText() + ")";

	private static final String DELETE_JOKES_TABLE = "DROP TABLE IF EXISTS " + JokeEntry.TABLE_NAME;
	private static final DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",
			Locale.getDefault());

	private Context mContext;

	public LikelyDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_JOKES_TABLE);
		final List<String> list = readFromFile();
		int counter = 0;
		final Calendar cal = Calendar.getInstance();
		Date now = new Date();
		String todayDate = sDateFormat.format(now);
		for (String statement : list) {
			if (counter == 40) {
				cal.setTime(now);
				cal.add(Calendar.DAY_OF_YEAR, 1);
				now = cal.getTime();
				todayDate = sDateFormat.format(now);
				counter = 0;
			}
			db.execSQL(String.format(statement, todayDate).replaceAll("NEW_LINE", "\n"));
			counter++;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is to simply to discard the data and start over
		db.execSQL(DELETE_JOKES_TABLE);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	private List<String> readFromFile() {
		final List<String> list = new ArrayList<String>();
		// Read text from file
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(
					DATABASE_NAME)));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}
		Collections.shuffle(list, new Random());
		return list;
	}
}