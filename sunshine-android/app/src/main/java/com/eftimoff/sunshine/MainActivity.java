package com.eftimoff.sunshine;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends Activity implements ForecastFragment.Callback {

    private boolean mTwoPane;
    private AdView mAdView;
    private ConnectivityChangeReceiver mConnectivityChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;

            getFragmentManager().
                    beginTransaction().
                    replace(R.id.weather_detail_container, new DetailFragment()).
                    commit();
        }

        ForecastFragment forecastFragment = ((ForecastFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_forecast));
        forecastFragment.setUseTodayLayout(!mTwoPane);

        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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


    @Override
    public void onItemSelected(final String date, final double latitude, final double longitude) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(DetailActivity.DATE_KEY, date);
            args.putDouble(DetailActivity.LATITUDE_KEY, latitude);
            args.putDouble(DetailActivity.LONGITUDE_KEY, longitude);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivity.DATE_KEY, date)
                    .putExtra(DetailActivity.LATITUDE_KEY, latitude)
                    .putExtra(DetailActivity.LONGITUDE_KEY, longitude);

            startActivity(intent);
        }
    }
}
