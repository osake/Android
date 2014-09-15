package com.eftimoff.bulgaria.provider.sight;

import android.net.Uri;
import android.provider.BaseColumns;

import com.eftimoff.bulgaria.provider.SightProvider;

/**
 * Columns for the {@code sight} table.
 */
public class SightColumns implements BaseColumns {

	private SightColumns() {
	}

	public static final String TABLE_NAME = "sight";
	public static final Uri CONTENT_URI = Uri.parse(SightProvider.CONTENT_URI_BASE + "/"
			+ TABLE_NAME);

	public static final String _ID = BaseColumns._ID;
	public static final String LABEL = "label";
	public static final String DESCRIPTION = "description";
	public static final String CATEGORY = "category";
	public static final String REGION = "region";
	public static final String SHOWNOTIFICATIONS = "shownotifications";
	public static final String LIKE = "like";
	public static final String THUMBNAIL = "thumbnail";
	public static final String IMG1 = "img1";
	public static final String IMG2 = "img2";
	public static final String IMG3 = "img3";
	public static final String IMG4 = "img4";
	public static final String IMG5 = "img5";
	public static final String COORDINATEX = "coordinatex";
	public static final String COORDINATEY = "coordinatey";

	public static final String DEFAULT_ORDER = _ID;

	// @formatter:off
	public static final String[] FULL_PROJECTION = new String[] { _ID, LABEL, DESCRIPTION,
			CATEGORY, REGION, SHOWNOTIFICATIONS, LIKE, THUMBNAIL, IMG1, IMG2, IMG3, IMG4, IMG5,
			COORDINATEX, COORDINATEY };
	// @formatter:on
}