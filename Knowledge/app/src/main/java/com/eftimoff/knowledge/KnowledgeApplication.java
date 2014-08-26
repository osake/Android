package com.eftimoff.knowledge;

import android.app.Application;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class KnowledgeApplication extends Application {

	/**
	 * Class instance of the JSON factory.
	 */
	public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

	/**
	 * Class instance of the HTTP transport.
	 */
	public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();


//	public static Registration getApiServiceHandle(@Nullable GoogleAccountCredential credential) {
//		// Use a builder to help formulate the API request.
//		Registration.Builder messaging = new Registration.Builder(HTTP_TRANSPORT,
//				JSON_FACTORY, credential);
//
//		// If running the Cloud Endpoint API locally then point the API stub there by un-commenting the
//		// next line.
//		// helloWorld.setRootUrl("http://192.168.1.100:8080/_ah/api/");
//
//		return messaging.build();
//	}
}
