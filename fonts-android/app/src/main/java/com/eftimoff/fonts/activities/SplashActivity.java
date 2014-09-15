package com.eftimoff.fonts.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.eftimoff.fonts.R;
import com.eftimoff.fonts.dialogs.AlertDialogFragment;
import com.eftimoff.fonts.services.StartService;

public class SplashActivity extends Activity {

	/* Constants. */

	private static final String TAG = SplashActivity.class.getSimpleName();
	private static final String DIALOG_FRAGMENT_TAG = "dialog_tag";

	/* Private fields. */

	private AlertDialogFragment alertDialogFragment;
	private TextView splashFragmentTextView;
	private NumberProgressBar splashFragmentProgressBar;
	private ConnectivityChangeReceiver mConnectivityChangeReceiver;
	private BroadcastReceiver updateBroadcastReceiver;

	/* Public methods. */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		splashFragmentProgressBar = (NumberProgressBar) findViewById(R.id.splashFragmentProgressBar);
		splashFragmentTextView = (TextView) findViewById(R.id.splashFragmentTextView);

		mConnectivityChangeReceiver = new ConnectivityChangeReceiver();
		alertDialogFragment = new AlertDialogFragment.Builder().
				message(getString(R.string.no_internet)).
				build();
		updateBroadcastReceiver = new UpdateBroadcastReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(updateBroadcastReceiver
				, new IntentFilter(StartService.UPDATE_RESULT_ACTION));

	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mConnectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mConnectivityChangeReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(updateBroadcastReceiver);
	}

	/* Private methods. */

	/* Inner classes. */

	private class ConnectivityChangeReceiver extends android.content.BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Bundle extras = intent.getExtras();
			if (extras != null) {
				if (extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
					alertDialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
				} else {
					if (alertDialogFragment != null && alertDialogFragment.isAdded()) {
						alertDialogFragment.dismiss();
					}
					startService(StartService.makeIntent());
				}
			}
		}
	}

	private class UpdateBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			final int max = intent.getIntExtra(StartService.EXTRA_MAX, 0);
			splashFragmentProgressBar.incrementProgressBy(1);
			if (max != splashFragmentProgressBar.getMax()) {
				splashFragmentProgressBar.setMax(max);
			}

			if (intent.hasExtra(StartService.EXTRA_DONE) && intent.getBooleanExtra(StartService.EXTRA_DONE, false)) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
			}
		}
	}
}
