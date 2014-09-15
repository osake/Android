package com.eftimoff.likelyandroid.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.eftimoff.likelyandroid.R;
import com.eftimoff.likelyandroid.activity.FullScreenActivity;
import com.eftimoff.likelyandroid.adapter.ItemAdapter;
import com.eftimoff.likelyandroid.database.LikelyContract.JokeEntry;
import com.eftimoff.likelyandroid.listener.LikelyListViewListener;
import com.eftimoff.likelyandroid.models.Joke;
import com.eftimoff.likelyandroid.provider.LikelyProvider;
import com.fortysevendeg.swipelistview.SwipeListView;

public class FavoritesFragment extends ListFragment implements OnItemLongClickListener,
		LoaderCallbacks<Cursor> {

	private SwipeListView mSwipelistview;
	private ItemAdapter mAdapter;
	private String mLimit;

	public static Fragment newInstance() {
		final Fragment fragment = new FavoritesFragment();
		fragment.setRetainInstance(true);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_favorites, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSwipelistview = (SwipeListView) getListView();
		mLimit = getResources().getString(R.string.favorites_limit);

		mAdapter = new ItemAdapter(getActivity(), R.layout.custom_row, new ArrayList<Joke>());
		mSwipelistview
				.setSwipeListViewListener(new LikelyListViewListener(getActivity(), mAdapter));

		mSwipelistview.setAdapter(mAdapter);
		mSwipelistview.setOnItemLongClickListener((OnItemLongClickListener) this);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		final Intent intent = new Intent(getActivity(), FullScreenActivity.class);
		intent.putExtra(FullScreenActivity.EXTRA_ID, mAdapter.getItem(position).getId());
		startActivity(intent);
		return true;
	}

	// creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		final String sortOrder = JokeEntry.COLUMN_DATE + " DESC";

		final String selection = JokeEntry.COLUMN_LIKES + " = ?";
		final String[] selectionArgs = { Integer.toString(Joke.Likes.LIKES.getNumber()) };
		final Uri uri = LikelyProvider.CONTENT_URI.buildUpon()
				.appendQueryParameter(LikelyProvider.QUERY_PARAMETER_LIMIT, mLimit).build();
		final CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, null, selection,
				selectionArgs, sortOrder);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		while (data.moveToNext()) {
			final Joke joke = new Joke();
			joke.setBackgroundColor(data.getInt(data
					.getColumnIndexOrThrow(JokeEntry.COLUMN_BACKGROUND_COLOR)));
			joke.setCategory(data.getString(data.getColumnIndexOrThrow(JokeEntry.COLUMN_CATEGORY)));
			joke.setId(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_ID)));
			joke.setLikes(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_LIKES)));
			joke.setRating(data.getFloat(data.getColumnIndexOrThrow(JokeEntry.COLUMN_RATING)));
			joke.setText(data.getString(data.getColumnIndexOrThrow(JokeEntry.COLUMN_TEXT)));
			joke.setVotes(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_VOTES)));
			joke.setSeen(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_SEEN)));
			joke.setDate(data.getString(data.getColumnIndexOrThrow(JokeEntry.COLUMN_DATE)));
			if (mAdapter.getPosition(joke) == -1) {
				mAdapter.add(joke);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.notifyDataSetInvalidated();
	}
}
