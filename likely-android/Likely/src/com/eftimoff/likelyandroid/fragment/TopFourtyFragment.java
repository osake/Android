package com.eftimoff.likelyandroid.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

public class TopFourtyFragment extends ListFragment implements OnItemLongClickListener,
		LoaderCallbacks<Cursor> {

	private static final String SQL_AND = " AND ";
	private static final String SQL_EQUALS = " = ? ";
	private static final DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd",
			Locale.getDefault());
	private static final String sTodayDate = sDateFormat.format(new Date());

	private SwipeListView mSwipelistview;
	private ItemAdapter mAdapter;
	private String mLimit;

	public static Fragment newInstance() {
		final Fragment fragment = new TopFourtyFragment();
		fragment.setRetainInstance(true);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_top_fourty, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSwipelistview = (SwipeListView) getListView();
		mLimit = getResources().getString(R.string.top_fourty_limit);

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

		final StringBuilder selectionBuilder = new StringBuilder();
		final List<String> selectionArgsList = new ArrayList<String>();
		selectionBuilder.//
				append(JokeEntry.COLUMN_SEEN).//
				append(SQL_EQUALS).//
				append(SQL_AND).//
				append(JokeEntry.COLUMN_DATE).//
				append(SQL_EQUALS);//
		selectionArgsList.add(Integer.toString(Joke.Seen.NOT_SEEN.getNumber()));
		selectionArgsList.add(sTodayDate);

		final Uri uri = LikelyProvider.CONTENT_URI.buildUpon()
				.appendQueryParameter(LikelyProvider.QUERY_PARAMETER_LIMIT, mLimit).build();
		final CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, null,
				selectionBuilder.toString(), selectionArgsList.toArray(new String[selectionArgsList
						.size()]), null);
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
		mSwipelistview.setEmptyView(getView().findViewById(
				R.id.fragment_top_fourty_invisible_text_view));
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.notifyDataSetChanged();
	}

}
