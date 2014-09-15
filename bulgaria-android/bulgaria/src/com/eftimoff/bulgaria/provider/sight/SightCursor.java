package com.eftimoff.bulgaria.provider.sight;

import java.util.Date;

import android.database.Cursor;

import com.eftimoff.bulgaria.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code sight} table.
 */
public class SightCursor extends AbstractCursor {
    public SightCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code label} value.
     * Can be {@code null}.
     */
    public String getLabel() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.LABEL);
        return getString(index);
    }

    /**
     * Get the {@code description} value.
     * Can be {@code null}.
     */
    public String getDescription() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.DESCRIPTION);
        return getString(index);
    }

    /**
     * Get the {@code category} value.
     * Can be {@code null}.
     */
    public String getCategory() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.CATEGORY);
        return getString(index);
    }

    /**
     * Get the {@code region} value.
     * Can be {@code null}.
     */
    public String getRegion() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.REGION);
        return getString(index);
    }

    /**
     * Get the {@code shownotifications} value.
     * Can be {@code null}.
     */
    public Boolean getShownotifications() {
        return getBoolean(SightColumns.SHOWNOTIFICATIONS);
    }

    /**
     * Get the {@code like} value.
     * Can be {@code null}.
     */
    public Boolean getLike() {
        return getBoolean(SightColumns.LIKE);
    }

    /**
     * Get the {@code thumbnail} value.
     * Can be {@code null}.
     */
    public String getThumbnail() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.THUMBNAIL);
        return getString(index);
    }

    /**
     * Get the {@code img1} value.
     * Can be {@code null}.
     */
    public String getImg1() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.IMG1);
        return getString(index);
    }

    /**
     * Get the {@code img2} value.
     * Can be {@code null}.
     */
    public String getImg2() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.IMG2);
        return getString(index);
    }

    /**
     * Get the {@code img3} value.
     * Can be {@code null}.
     */
    public String getImg3() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.IMG3);
        return getString(index);
    }

    /**
     * Get the {@code img4} value.
     * Can be {@code null}.
     */
    public String getImg4() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.IMG4);
        return getString(index);
    }

    /**
     * Get the {@code img5} value.
     * Can be {@code null}.
     */
    public String getImg5() {
        Integer index = getCachedColumnIndexOrThrow(SightColumns.IMG5);
        return getString(index);
    }

    /**
     * Get the {@code coordinatex} value.
     * Can be {@code null}.
     */
    public Float getCoordinatex() {
        return getFloatOrNull(SightColumns.COORDINATEX);
    }

    /**
     * Get the {@code coordinatey} value.
     * Can be {@code null}.
     */
    public Float getCoordinatey() {
        return getFloatOrNull(SightColumns.COORDINATEY);
    }
}
