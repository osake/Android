package com.eftimoff.fonts.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.eftimoff.fonts.FontsApplication;
import com.eftimoff.fonts.R;
import com.eftimoff.fonts.adapters.WebFontsAdapter;
import com.eftimoff.fonts.contants.WebFontsContants;
import com.eftimoff.fonts.models.Fonts;
import com.eftimoff.fonts.net.WebFontsService;

public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {

	public static final String TAG = MainFragment.class.getSimpleName();
	public static final int CACHE_MAX_SIZE = 1500000;

	private MenuItem refreshMenuItem;
	private WebFontsService webFontsService;
	private ArrayAdapter<CharSequence> sortsAdapter;
	private WebFontsAdapter webFontsAdapter;

	private GridView gridView;
	private EditText editText;

	public MainFragment() {
		setHasOptionsMenu(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		sortsAdapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.sorts));
		sortsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		webFontsService = FontsApplication.getWebFontService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		gridView = (GridView) rootView.findViewById(R.id.gridView);
		editText = (EditText) rootView.findViewById(R.id.editText);
		editText.addTextChangedListener(this);
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		getActivity().getMenuInflater().inflate(R.menu.menu_fragment_main, menu);
		final Spinner spinner = (Spinner) menu.findItem(R.id.action_spinner).getActionView();
		spinner.setAdapter(sortsAdapter);
		spinner.setOnItemSelectedListener(this);
		refreshMenuItem = menu.findItem(R.id.action_refresh);
		changeMenuItemVisibility(refreshMenuItem, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_refresh:
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void changeMenuItemVisibility(final MenuItem menuItem, final boolean visible) {
		if (!menuItem.isVisible() && visible) {
			menuItem.setVisible(true);
		} else {
			menuItem.setVisible(false);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		final WebFontsRequest webFontsRequest = new WebFontsRequest();
		webFontsRequest.execute((String) sortsAdapter.getItem(position));
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (webFontsAdapter != null) {
			webFontsAdapter.setText(s.toString());
		}
	}

	private class WebFontsRequest extends AsyncTask<String, Void, Fonts> {
		@Override
		protected Fonts doInBackground(String[] params) {
			return webFontsService.listFonts(params[0], WebFontsContants.WEB_FONTS_KEY);
		}

		@Override
		protected void onPostExecute(Fonts fonts) {
			super.onPostExecute(fonts);
			webFontsAdapter = new WebFontsAdapter(getActivity(), R.layout.view_list_item, fonts.getItems());
			webFontsAdapter.setText(editText.getText().toString());
			gridView.setAdapter(webFontsAdapter);
			changeMenuItemVisibility(refreshMenuItem, false);
		}
	}
}
