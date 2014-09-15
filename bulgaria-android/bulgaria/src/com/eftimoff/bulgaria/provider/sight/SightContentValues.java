package com.eftimoff.bulgaria.provider.sight;

import android.content.ContentResolver;
import android.net.Uri;

import com.eftimoff.bulgaria.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code sight} table.
 */
public class SightContentValues extends AbstractContentValues {
	@Override
	public Uri uri() {
		return SightColumns.CONTENT_URI;
	}

	/**
	 * Update row(s) using the values stored by this object and the given
	 * selection.
	 * 
	 * @param contentResolver
	 *            The content resolver to use.
	 * @param where
	 *            The selection to use (can be {@code null}).
	 */
	public int update(ContentResolver contentResolver, SightSelection where) {
		return contentResolver.update(uri(), values(), where == null ? null : where.sel(),
				where == null ? null : where.args());
	}

	public SightContentValues putLabel(String value) {
		mContentValues.put(SightColumns.LABEL, value);
		return this;
	}

	public SightContentValues putLabelNull() {
		mContentValues.putNull(SightColumns.LABEL);
		return this;
	}

	public SightContentValues putDescription(String value) {
		mContentValues.put(SightColumns.DESCRIPTION, value);
		return this;
	}

	public SightContentValues putDescriptionNull() {
		mContentValues.putNull(SightColumns.DESCRIPTION);
		return this;
	}

	public SightContentValues putCategory(String value) {
		mContentValues.put(SightColumns.CATEGORY, value);
		return this;
	}

	public SightContentValues putCategoryNull() {
		mContentValues.putNull(SightColumns.CATEGORY);
		return this;
	}

	public SightContentValues putRegion(String value) {
		mContentValues.put(SightColumns.REGION, value);
		return this;
	}

	public SightContentValues putRegionNull() {
		mContentValues.putNull(SightColumns.REGION);
		return this;
	}

	public SightContentValues putShownotifications(Boolean value) {
		mContentValues.put(SightColumns.SHOWNOTIFICATIONS, value);
		return this;
	}

	public SightContentValues putShownotificationsNull() {
		mContentValues.putNull(SightColumns.SHOWNOTIFICATIONS);
		return this;
	}

	public SightContentValues putLike(Boolean value) {
		mContentValues.put(SightColumns.LIKE, value);
		return this;
	}

	public SightContentValues putLikeNull() {
		mContentValues.putNull(SightColumns.LIKE);
		return this;
	}

	public SightContentValues putThumbnail(String value) {
		mContentValues.put(SightColumns.THUMBNAIL, value);
		return this;
	}

	public SightContentValues putThumbnailNull() {
		mContentValues.putNull(SightColumns.THUMBNAIL);
		return this;
	}

	public SightContentValues putImg1(String value) {
		mContentValues.put(SightColumns.IMG1, value);
		return this;
	}

	public SightContentValues putImg1Null() {
		mContentValues.putNull(SightColumns.IMG1);
		return this;
	}

	public SightContentValues putImg2(String value) {
		mContentValues.put(SightColumns.IMG2, value);
		return this;
	}

	public SightContentValues putImg2Null() {
		mContentValues.putNull(SightColumns.IMG2);
		return this;
	}

	public SightContentValues putImg3(String value) {
		mContentValues.put(SightColumns.IMG3, value);
		return this;
	}

	public SightContentValues putImg3Null() {
		mContentValues.putNull(SightColumns.IMG3);
		return this;
	}

	public SightContentValues putImg4(String value) {
		mContentValues.put(SightColumns.IMG4, value);
		return this;
	}

	public SightContentValues putImg4Null() {
		mContentValues.putNull(SightColumns.IMG4);
		return this;
	}

	public SightContentValues putImg5(String value) {
		mContentValues.put(SightColumns.IMG5, value);
		return this;
	}

	public SightContentValues putImg5Null() {
		mContentValues.putNull(SightColumns.IMG5);
		return this;
	}

	public SightContentValues putCoordinatex(Float value) {
		mContentValues.put(SightColumns.COORDINATEX, value);
		return this;
	}

	public SightContentValues putCoordinatexNull() {
		mContentValues.putNull(SightColumns.COORDINATEX);
		return this;
	}

	public SightContentValues putCoordinatey(Float value) {
		mContentValues.put(SightColumns.COORDINATEY, value);
		return this;
	}

	public SightContentValues putCoordinateyNull() {
		mContentValues.putNull(SightColumns.COORDINATEY);
		return this;
	}

}
