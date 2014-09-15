package com.eftimoff.likelyandroid.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eftimoff.likelyandroid.R;
import com.eftimoff.likelyandroid.database.LikelyContract.JokeEntry;
import com.eftimoff.likelyandroid.models.Joke;
import com.eftimoff.likelyandroid.provider.LikelyProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FullScreenActivity extends ActionBarActivity {

	/* Constants */

	private static final String DRAWABLE = "drawable";
	public static final String EXTRA_DATA = "extra_data";
	public static final String EXTRA_ID = "extra_id";

	/* Fields */

	private AdView mAdView;
	private ConnectivityChangeReceiver mConnectivityChangeReceiver;
	private ShareActionProvider mShareActionProvider;
	private Joke mJoke;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen);

		final Cursor cursor = getCursor();
		mJoke = toJoke(cursor);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.replace(R.id.container, PlaceholderFragment.newInstance(mJoke)).commit();
		}

		mAdView = (AdView) findViewById(R.id.fullScreeActiviyAdView);
		mAdView.setVisibility(View.GONE);
	}

	private Joke toJoke(final Cursor data) {
		final Joke joke = new Joke();
		if (data.moveToFirst()) {
			joke.setCategory(data.getString(data.getColumnIndexOrThrow(JokeEntry.COLUMN_CATEGORY)));
			joke.setId(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_ID)));
			joke.setLikes(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_LIKES)));
			joke.setRating(data.getFloat(data.getColumnIndexOrThrow(JokeEntry.COLUMN_RATING)));
			joke.setText(data.getString(data.getColumnIndexOrThrow(JokeEntry.COLUMN_TEXT)));
			joke.setVotes(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_VOTES)));
			joke.setBackgroundColor(data.getInt(data
					.getColumnIndexOrThrow(JokeEntry.COLUMN_BACKGROUND_COLOR)));
			joke.setSeen(data.getInt(data.getColumnIndexOrThrow(JokeEntry.COLUMN_SEEN)));
			joke.setDate(data.getString(data.getColumnIndexOrThrow(JokeEntry.COLUMN_DATE)));
		}
		return joke;
	}

	private Cursor getCursor() {
		Cursor cursor = null;
		final String data = getIntent().getExtras().getString(EXTRA_DATA);
		if (data != null) {
			final String jokeId = data.substring(data.lastIndexOf("/") + 1);
			cursor = getContentResolver().query(LikelyProvider.CONTENT_URI, null,
					JokeEntry._ID + " = ?", new String[] { jokeId }, null);
		}
		if (getIntent().getExtras().getInt(EXTRA_ID, -1) != -1) {
			final String jokeProductId = Integer.toString(getIntent().getExtras().getInt(EXTRA_ID));
			cursor = getContentResolver().query(LikelyProvider.CONTENT_URI, null,
					JokeEntry.COLUMN_ID + " = ?", new String[] { jokeProductId }, null);
		}
		return cursor;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.full_screen, menu);

		// Set up ShareActionProvider's default share intent
		MenuItem shareItem = menu.findItem(R.id.action_share);
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
		mShareActionProvider.setShareIntent(getDefaultIntent());

		return super.onCreateOptionsMenu(menu);
	}

	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, mJoke.getCategory());
		intent.putExtra(Intent.EXTRA_TEXT, mJoke.getText());
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_share:
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private TextView mTextView;
		private Joke joke;

		public static Fragment newInstance(final Joke joke) {
			final PlaceholderFragment fragment = new PlaceholderFragment();
			fragment.setRetainInstance(true);
			fragment.setJoke(joke);
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_full_screen, container, false);
		}

		@SuppressLint("NewApi")
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			final Joke joke = getJoke();
			mTextView = (TextView) getView().findViewById(R.id.fragment_full_screen_text);

			getActivity().setTitle(joke.getCategory());

			final int identifier = contructId(joke.getCategory());
			if (identifier != 0) {
				getActivity().getActionBar().setIcon(identifier);
			}

			mTextView.setText(joke.getText());
			mTextView.setMovementMethod(new ScrollingMovementMethod());
		}

		private int contructId(final String category) {
			return getResources().getIdentifier(
					"ic_" + category.toLowerCase(Locale.getDefault()).replace(" ", "_"), DRAWABLE,
					getActivity().getPackageName());
		}

		public Joke getJoke() {
			return joke;
		}

		public void setJoke(Joke joke) {
			this.joke = joke;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mConnectivityChangeReceiver = new ConnectivityChangeReceiver();
		registerReceiver(mConnectivityChangeReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mConnectivityChangeReceiver);
	}

	private class ConnectivityChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final Bundle extras = intent.getExtras();
			if (extras != null) {
				if (extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
					mAdView.setVisibility(View.GONE);
				} else {
					mAdView.setVisibility(View.VISIBLE);
					final AdRequest adRequest = new AdRequest.Builder().build();
					mAdView.loadAd(adRequest);
				}
			}
		}

	}

}
