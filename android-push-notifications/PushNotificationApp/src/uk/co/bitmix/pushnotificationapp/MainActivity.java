package uk.co.bitmix.pushnotificationapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pushnotificationapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {

	/* Constants */

	private static final String TAG = MainActivity.class.getSimpleName();
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/* URL Constants */

	private static final String URL_ADDRESS = "http://api.merlin.bitmix.co.uk/notifications/subscribe/Android/%s/%s/-/";
	private static final String TOKEN = "token";
	private static final String DEVID = "devid";

	/* Fields */

	private String senderId = null;

	private TextView mRegitserId;
	private GoogleCloudMessaging gcm;
	private String regid;
	private EditText mTenantEditText;
	private EditText mAppHostIdEditText;
	private EditText mDeviceIdEditText;
	private Button mRegisterButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mRegitserId = (TextView) findViewById(R.id.regitserId);
		mTenantEditText = (EditText) findViewById(R.id.tenant);
		mAppHostIdEditText = (EditText) findViewById(R.id.apphostid);
		mDeviceIdEditText = (EditText) findViewById(R.id.deviceId);
		mRegisterButton = (Button) findViewById(R.id.buttonRegister);
		mRegisterButton.setOnClickListener(new RegisterButtonClickListener());
		senderId = getResources().getString(R.string.project_number);

	}

	// You need to do the Play Services APK check here too.
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		final String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
					}
					if (regid.length() == 0) {
						regid = gcm.register(senderId);
					}

					final int status = sendRegistrationIdToBackend();
					msg = "Registration id send to server.\nResponce status code : " + status;

					storeRegistrationId(getApplicationContext(), regid);
				} catch (IOException ex) {
					msg = "Error :" + ex;
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mRegitserId.setText("Registration ID = " + regid);
				Log.i(TAG, regid);
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}
		}.execute(null, null, null);
	}

	/**
	 * Sends the registration ID to the server over HTTP.
	 */
	private int sendRegistrationIdToBackend() {
		int status = 0;
		if (chechInputFields() != null) {
			Log.e(TAG, "The input in" + chechInputFields() + " must be valid.\nPlease try again...");
			return status;
		}
		final HttpClient client = new DefaultHttpClient();

		final String address = String.format(URL_ADDRESS, mTenantEditText.getText(),
				mAppHostIdEditText.getText());
		final HttpPost post = new HttpPost(address);

		try {

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(TOKEN, regid));
			pairs.add(new BasicNameValuePair(DEVID, mDeviceIdEditText.getText().toString()));
			post.setEntity(new UrlEncodedFormEntity(pairs));
			final HttpResponse response = client.execute(post);
			status = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			Log.e(TAG, "Error while executing the request.");
		}
		return status;
	}

	private String chechInputFields() {
		if (mTenantEditText.getText().toString() == null
				|| mTenantEditText.getText().toString().equals("")) {
			return "tenant";
		}
		if (mAppHostIdEditText.getText().toString() == null
				|| mAppHostIdEditText.getText().toString().equals("")) {
			return "apphostid";
		}
		if (mDeviceIdEditText.getText().toString() == null
				|| mDeviceIdEditText.getText().toString().equals("")) {
			return "deviceid";
		}
		return null;
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private class RegisterButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// Check device for Play Services APK. If check succeeds, proceed
			// with
			// GCM registration.
			if (checkPlayServices()) {
				gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
				regid = getRegistrationId(getApplicationContext());
				registerInBackground();
				mRegitserId.setText("Registration ID = " + regid);
				Toast.makeText(getApplicationContext(),
						"Generating Registration ID ...\nPlease view the label.",
						Toast.LENGTH_SHORT).show();
			} else {
				Log.i(TAG, "No valid Google Play Services APK found.");
			}
		}

	}
}
