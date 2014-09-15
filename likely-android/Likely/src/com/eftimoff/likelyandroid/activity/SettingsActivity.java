package com.eftimoff.likelyandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.eftimoff.likelyandroid.R;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment())
				.commit();
	}

	public static class PrefsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}

	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}
}
