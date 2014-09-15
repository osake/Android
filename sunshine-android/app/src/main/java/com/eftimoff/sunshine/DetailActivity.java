package com.eftimoff.sunshine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends Activity {

    public static final String DATE_KEY = "forecast_date";
    public static final String LATITUDE_KEY = "forecast_latitude";
    public static final String LONGITUDE_KEY = "forecast_longitude";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            String date = getIntent().getStringExtra(DATE_KEY);
            final double latitude = getIntent().getDoubleExtra(DetailActivity.LATITUDE_KEY, 0);
            final double longitude = getIntent().getDoubleExtra(DetailActivity.LONGITUDE_KEY, 0);

            Bundle arguments = new Bundle();
            arguments.putString(DetailActivity.DATE_KEY, date);
            arguments.putDouble(DetailActivity.LATITUDE_KEY, latitude);
            arguments.putDouble(DetailActivity.LONGITUDE_KEY, longitude);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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
}
