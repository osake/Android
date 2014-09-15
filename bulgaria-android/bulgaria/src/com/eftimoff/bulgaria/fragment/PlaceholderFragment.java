package com.eftimoff.bulgaria.fragment;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.eftimoff.bulgaria.R;
import com.eftimoff.bulgaria.adapter.SightCursorAdapter;
import com.eftimoff.bulgaria.provider.sight.SightColumns;
import com.eftimoff.bulgaria.provider.sight.SightContentValues;

public class PlaceholderFragment extends Fragment implements LoaderCallbacks<Cursor>,
		OnItemSelectedListener, LocationListener {

	private ListView mList;
	private Button mButton;
	private Button mButton1;
	private SightCursorAdapter mAdapter;
	private LocationManager locationManager;
	private String provider;

	public static Fragment newInstance() {
		final Fragment fragment = new PlaceholderFragment();
		fragment.setRetainInstance(true);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the location manager
		locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		mList = (ListView) rootView.findViewById(R.id.list);
		mButton = (Button) rootView.findViewById(R.id.button);
		mButton1 = (Button) rootView.findViewById(R.id.button1);
		@SuppressLint("InflateParams")
		final View view = inflater.inflate(R.layout.view_listview_footer, null, false);
		mList.addFooterView(view);
		mList.addHeaderView(view);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// // Initialize the location fields
		// if (location != null) {
		// onLocationChanged(location);
		// }

		getLoaderManager().initLoader(0, null, this);
		mAdapter = new SightCursorAdapter(getActivity(), null, 0, location);
		mList.setAdapter(mAdapter);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SightContentValues values = new SightContentValues();
				Random random = new Random();
				values.putLabel("Krushunskite vodopadi")
						.putDescription(
								"Krushunskite vodopadi sa mnogo blizo do lovech i razpolagat s masivna vulna.")
						.putThumbnail("vodopad")//
						.putCoordinatex((float) random.nextInt(180 - 90))//
						.putCoordinatey((float) random.nextInt(180 - 90));
				getActivity().getContentResolver()
						.insert(SightColumns.CONTENT_URI, values.values());
			}
		});
		mButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getContentResolver().delete(SightColumns.CONTENT_URI, null, null);
			}
		});
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		final MenuItem item = menu.add(Menu.NONE, Menu.NONE, 10, R.string.action_meters);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		item.setActionView(R.layout.action_meters);
		final Spinner spinner = (Spinner) item.getActionView();
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.meters, R.layout.view_spinner);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.view_spinner_dropdown);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final CursorLoader cursorLoader = new CursorLoader(getActivity(), SightColumns.CONTENT_URI,
				null, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Toast.makeText(parent.getContext(), "The position is " + pos, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	/* Request updates at startup */
	@Override
	public void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 1000, 10, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	public void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		mAdapter.changeLocation(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getActivity(), "Enabled new provider " + provider, Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getActivity(), "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
	}

}