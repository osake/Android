package com.eftimoff.sunshine;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eftimoff.sunshine.data.WeatherContract;
import com.eftimoff.sunshine.service.SunshineService;

import java.util.Date;

public class ForecastFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {

    public ForecastFragment() {
        setRetainInstance(true);
    }


    private static final int FORECAST_LOADER = 0;

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATETEXT,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    private int mPosition = ListView.INVALID_POSITION;

    //    private String mLocation;
    private ForecastAdapter mForecastAdapter;
    private ListView mListView;
    private boolean mUseTodayLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private LocationManager mLocationManager;
    private String mProvider;
    private LocalBroadcastReceiver localBroadcastReceiver;
    private MenuItem mLocationMenuItem;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(final String date, final double latitude, final double longitude);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void saveCoordinates(final double latitude, final double longitude) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        editor.putString(getString(R.string.preference_latitude_key), Double.toString(latitude));
        editor.putString(getString(R.string.preference_longitude_key), Double.toString(longitude));
        editor.commit();
    }

    private double getLatitude() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String string = sharedPreferences.getString(getString(R.string.preference_latitude_key), getString(R.string.preference_latitude_default));
        return Double.parseDouble(string);
    }

    private double getLongitude() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String string = sharedPreferences.getString(getString(R.string.preference_longitude_key), getString(R.string.preference_longitude_default));
        return Double.parseDouble(string);
    }

    @Override
    public void onRefresh() {
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, true);
        Location location = mLocationManager.getLastKnownLocation(mProvider);
        if (location != null) {
            saveCoordinates(location.getLatitude(), location.getLongitude());
        }
        updateForecast(getLatitude(), getLongitude());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mForecastAdapter = new ForecastAdapter(getActivity());
        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);
        mListView.setOnItemClickListener(this);

        // Get the location manager
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin aprovider -> use
        // default
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, true);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (location != null) {
            saveCoordinates(location.getLatitude(), location.getLongitude());
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
        mLocationMenuItem = menu.findItem(R.id.action_location);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String storedTitle = sharedPreferences.getString(getString(R.string.action_location_key), getString(R.string.action_location_default));
        mLocationMenuItem.setTitle(storedTitle);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter(SunshineService.ACTION_SERVICE_DONE);
        localBroadcastReceiver = new LocalBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mForecastAdapter.getCount() == 0) {
            updateForecast(getLatitude(), getLongitude());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(localBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                openPreferredLocationInMap(getLatitude(), getLongitude(), mLocationMenuItem.getTitle().toString());
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap(final double latitude, final double longitude, final String label) {
        final String location = Double.toString(latitude) + "," + Double.toString(longitude) + "(" + label + ")";
        final Uri uri = Uri.parse("geo:" + Double.toString(latitude) + "," + Double.toString(longitude) + "?").buildUpon().appendQueryParameter("q", location).build();
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cursor cursor = mForecastAdapter.getCursor();
        if (cursor != null && cursor.moveToPosition(position)) {
            ((Callback) getActivity())
                    .onItemSelected(cursor.getString(COL_WEATHER_DATE), getLatitude() * 10000, getLongitude() * 1000);
        }
        mPosition = position;
    }

    /**
     * It hide the SwipeRefreshLayout progress
     */
    public void hideSwipeProgress() {
        mRefreshLayout.setRefreshing(false);
    }


    private void updateForecast(final double latitude, final double longitude) {
        final Intent intent = SunshineService.makeIntent(getActivity(), Double.toString(latitude), Double.toString(longitude));
        getActivity().startService(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, get the String representation for today,
        // and filter the query to return weather only for dates after or including today.
        // Only return data after today.
        String startDate = WeatherContract.getDbDateString(new Date());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATETEXT + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                getLatitude(), getLongitude(), startDate);

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);

        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SunshineService.ACTION_SERVICE_DONE)) {
                hideSwipeProgress();
                final String title = intent.getStringExtra(SunshineService.SERVICE_EXTRA_TITLE);

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final String storedTitle = sharedPreferences.getString(getString(R.string.action_location_key), getString(R.string.action_location_default));
                if (!storedTitle.equals(title)) {
                    mLocationMenuItem.setTitle(title);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.action_location_key), title);
                    editor.commit();
                }
                getActivity().sendBroadcast(new Intent(NotificationBroadcastReceiver.ACTION_SET_REPEATING_ALARMS));
            }
        }
    }
}
