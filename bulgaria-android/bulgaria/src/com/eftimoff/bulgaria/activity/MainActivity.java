package com.eftimoff.bulgaria.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.eftimoff.bulgaria.R;
import com.eftimoff.bulgaria.adapter.DrawerAdapter;
import com.eftimoff.bulgaria.application.Bulgaria;
import com.eftimoff.bulgaria.fragment.PlaceholderFragment;
import com.eftimoff.bulgaria.model.Category;
import com.squareup.picasso.Picasso;

public class MainActivity extends Activity implements TabListener {

	// Within which the entire activity is enclosed
	private DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	private ListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
	// action bar
	private ActionBarDrawerToggle mDrawerToggle;

	private CheckBox mCheckBoxAll;
	private CheckBox mCheckBoxNone;

	private DrawerAdapter mDrawerAdapter;

	private ImageView mImageDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageDrawer = (ImageView) findViewById(R.id.ImgDrawer);

		Picasso.with(this).load(R.drawable.bulgaria).into(mImageDrawer);

		// ShowcaseView build = new ShowcaseView.Builder(this, true)
		// .setTarget(new ActionViewTarget(this, ActionViewTarget.Type.TITLE))
		// .setContentTitle("ShowcaseView")
		// .setContentText("This is highlighting the Home button").build();

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mCheckBoxAll = (CheckBox) findViewById(R.id.checkBoxAll);
		mCheckBoxNone = (CheckBox) findViewById(R.id.checkBoxNone);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		String[] tabs = getResources().getStringArray(R.array.tabs);
		for (String tabString : tabs) {
			final Tab tab = getActionBar().newTab();
			tab.setText(tabString).setTabListener(this);
			getActionBar().addTab(tab);
		}

		mDrawerAdapter = new DrawerAdapter(this, R.layout.view_drawer_item, Bulgaria.getInstance()
				.getCategories());
		mDrawerList.setAdapter(mDrawerAdapter);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
				getActionBar().setTitle(R.string.app_name);
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
				getActionBar().setTitle(R.string.drawer_inner_text);
			}

		};

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mCheckBoxAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Category category : Bulgaria.getInstance().getCategories()) {
					category.setChecked(true);
				}
				mDrawerAdapter.notifyDataSetInvalidated();
				mCheckBoxNone.setChecked(false);
			}
		});

		mCheckBoxNone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Category category : Bulgaria.getInstance().getCategories()) {
					category.setChecked(false);
				}
				mDrawerAdapter.notifyDataSetInvalidated();
				mCheckBoxAll.setChecked(false);
			}
		});

		if (savedInstanceState == null) {

			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
					.commit();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

	}
}
