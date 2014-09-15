package com.eftimoff.fonts.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.io.File;
import java.util.Hashtable;

public class TypeFaces {

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface getTypeFace(Context context, String path) {
		synchronized (cache) {
			if (!cache.containsKey(path)) {
				try {
					final Typeface typeFace = Typeface.createFromFile(new File(context.getCacheDir(), path));
					cache.put(path, typeFace);
				} catch (Exception e) {
					Log.e("TypeFaces", "Typeface not loaded.");
					return null;
				}
			}
			return cache.get(path);
		}
	}
}
