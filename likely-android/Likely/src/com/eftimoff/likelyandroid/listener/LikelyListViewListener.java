package com.eftimoff.likelyandroid.listener;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.eftimoff.likelyandroid.R;
import com.eftimoff.likelyandroid.adapter.ItemAdapter;
import com.eftimoff.likelyandroid.database.LikelyContract.JokeEntry;
import com.eftimoff.likelyandroid.models.Joke;
import com.eftimoff.likelyandroid.provider.LikelyProvider;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;

public class LikelyListViewListener extends BaseSwipeListViewListener {

	private int mGreenColor;
	private int mRedColor;
	private Activity mContext;
	private ItemAdapter mAdapter;

	public LikelyListViewListener(final Activity context, final ItemAdapter adapter) {
		mContext = context;
		mAdapter = adapter;
		getDisplaySizes();

		mGreenColor = mContext.getResources().getColor(R.color.green_color);
		mRedColor = mContext.getResources().getColor(R.color.red_color);
	}

	private void getDisplaySizes() {
		final DisplayMetrics displaymetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	}

	@Override
	public void onMove(int position, float x) {
		final int colorPosition = (int) x;
		int color;
		if (colorPosition < 0) {
			color = mRedColor;
		} else {
			color = mGreenColor;
		}
		final Joke joke = mAdapter.getItem(position);
		if (joke != null && joke.getBackgroundColor() != color) {
			joke.setBackgroundColor(color);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDismiss(int[] reverseSortedPositions) {
		for (int position : reverseSortedPositions) {
			final Joke joke = mAdapter.getItem(position);
			final UpdateJokeAsynkTask update = new UpdateJokeAsynkTask();
			update.execute(joke);
			mAdapter.remove(joke);
		}
	}

	private class UpdateJokeAsynkTask extends AsyncTask<Joke, Void, Void> {

		@Override
		protected Void doInBackground(Joke... params) {

			final Joke joke = params[0];

			// New value for one column
			final ContentValues values = new ContentValues();
			values.put(JokeEntry.COLUMN_SEEN, Joke.Seen.SEEN.getNumber());
			values.put(JokeEntry.COLUMN_BACKGROUND_COLOR, joke.getBackgroundColor());
			final int greenColor = mContext.getResources().getColor(R.color.green_color);
			if (joke.getBackgroundColor() == greenColor) {
				if (joke.getLikes() != Joke.Likes.LIKES.getNumber()) {
					values.put(JokeEntry.COLUMN_VOTES, joke.getVotes() + 1);
					values.put(JokeEntry.COLUMN_LIKES, Joke.Likes.LIKES.getNumber());
				}
			} else {
				values.put(JokeEntry.COLUMN_LIKES, Joke.Likes.DONT_LIKE.getNumber());
			}

			// Which row to update, based on the ID
			String selection = JokeEntry.COLUMN_ID + " = ?";
			String[] selectionArgs = { String.valueOf(joke.getId()) };

			mContext.getContentResolver().update(LikelyProvider.CONTENT_URI, values, selection,
					selectionArgs);
			return null;
		}

	}

}
