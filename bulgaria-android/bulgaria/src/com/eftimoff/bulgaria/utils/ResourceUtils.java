package com.eftimoff.bulgaria.utils;

import android.annotation.SuppressLint;
import android.content.Context;

public class ResourceUtils {

	private ResourceUtils() {
	}

	public enum ResourceType {
		STRING, DRAWABLE
	}

	@SuppressLint("DefaultLocale")
	public static int getResourceIdByName(final Context context, final String resource,
			final ResourceType resourceType) {
		return context.getResources().getIdentifier(resource,
				resourceType.toString().toLowerCase(), context.getPackageName());
	}

}
