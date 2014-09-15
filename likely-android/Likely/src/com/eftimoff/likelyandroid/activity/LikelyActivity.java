package com.eftimoff.likelyandroid.activity;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.eftimoff.likelyandroid.R;
import com.eftimoff.likelyandroid.application.LikelyApplication;
import com.eftimoff.likelyandroid.constants.SharedPreferencesConstats;
import com.eftimoff.likelyandroid.fragment.FavoritesFragment;
import com.eftimoff.likelyandroid.fragment.TopFourtyFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LikelyActivity extends ActionBarActivity implements ActionBar.TabListener {

	private static final int SETTINGS_REQUEST_CODE = 21;

	private ActionBar actionBar;
	private AdView mAdView;
	private ConnectivityChangeReceiver mConnectivityChangeReceiver;
	private SearchView mSearchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_likely);
		final FrameLayout frameLayout2 = (FrameLayout) findViewById(R.id.frameContainer2);

		if (frameLayout2 == null) {
			final String[] tabs = getResources().getStringArray(R.array.tabs);

			setPrefferedOrientation();

			actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			for (String tab_name : tabs) {
				actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
			}
		} else {
			final FragmentManager fragmentManager = getFragmentManager();
			final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
			fragmentTransaction.replace(R.id.frameContainer, TopFourtyFragment.newInstance());
			fragmentTransaction.commit();
			final FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
			fragmentTransaction2.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
			fragmentTransaction2.replace(R.id.frameContainer2, FavoritesFragment.newInstance());
			fragmentTransaction2.commit();
		}

		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.setVisibility(View.GONE);

		setAlarmFromPreferences();
	}

	private void setAlarmFromPreferences() {
		final SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		final boolean showJokesOfTheDay = sharedPreferences.getBoolean(
				SharedPreferencesConstats.SHOW_JOKE_OF_THE_DAY, true);
		final String alreadySetAlarm = sharedPreferences.getString(
				SharedPreferencesConstats.ALREADY_SET_ALARM, "0");
		final String notificationTime = sharedPreferences.getString(
				SharedPreferencesConstats.NOTIFICATION_TIME,
				SharedPreferencesConstats.DEFAULT_NOTIFICATION_TIME);
		if (showJokesOfTheDay) {
			if (!notificationTime.equals(alreadySetAlarm)) {
				LikelyApplication.getInstance().getLikelyAlarmManager()
						.setAlarm(Integer.parseInt(notificationTime));
				final Editor editor = sharedPreferences.edit();
				editor.putString(SharedPreferencesConstats.ALREADY_SET_ALARM, notificationTime);
				editor.commit();
			}
		} else {
			LikelyApplication.getInstance().getLikelyAlarmManager().cancelAlarm();
		}
	}

	private void setPrefferedOrientation() {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		checkIntent(intent);
	}

	private void checkIntent(Intent intent) {
		String intentAction = intent.getAction();
		if (Intent.ACTION_VIEW.equals(intentAction)) {
			final Intent detailsIntent = new Intent(this, FullScreenActivity.class);
			detailsIntent.putExtra(FullScreenActivity.EXTRA_DATA, intent.getData().toString());
			startActivity(detailsIntent);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:
			return true;
		case R.id.action_settings:
			final Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, SETTINGS_REQUEST_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mConnectivityChangeReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		getMenuInflater().inflate(R.menu.swipe, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		// Assumes current activity is the searchable activity
		mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setLayoutTransition(new LayoutTransition());
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {
		switch (tab.getPosition()) {
		case 0:
			fragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out)
					.replace(R.id.frameContainer, TopFourtyFragment.newInstance());
			break;
		case 1:
			fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
					.replace(R.id.frameContainer, FavoritesFragment.newInstance());
			break;
		default:
			break;
		}

	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SETTINGS_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				setAlarmFromPreferences();
			}
			break;
		default:
			break;
		}
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
