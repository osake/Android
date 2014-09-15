package com.eftimoff.bulgaria.provider.sight;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.eftimoff.bulgaria.provider.base.AbstractSelection;

/**
 * Selection for the {@code sight} table.
 */
public class SightSelection extends AbstractSelection<SightSelection> {
    @Override
    public Uri uri() {
        return SightColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code SightCursor} object, which is positioned before the first entry, or null.
     */
    public SightCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new SightCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public SightCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public SightCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public SightSelection id(long... value) {
        addEquals(SightColumns._ID, toObjectArray(value));
        return this;
    }


    public SightSelection label(String... value) {
        addEquals(SightColumns.LABEL, value);
        return this;
    }

    public SightSelection labelNot(String... value) {
        addNotEquals(SightColumns.LABEL, value);
        return this;
    }

    public SightSelection labelLike(String... value) {
        addLike(SightColumns.LABEL, value);
        return this;
    }

    public SightSelection description(String... value) {
        addEquals(SightColumns.DESCRIPTION, value);
        return this;
    }

    public SightSelection descriptionNot(String... value) {
        addNotEquals(SightColumns.DESCRIPTION, value);
        return this;
    }

    public SightSelection descriptionLike(String... value) {
        addLike(SightColumns.DESCRIPTION, value);
        return this;
    }

    public SightSelection category(String... value) {
        addEquals(SightColumns.CATEGORY, value);
        return this;
    }

    public SightSelection categoryNot(String... value) {
        addNotEquals(SightColumns.CATEGORY, value);
        return this;
    }

    public SightSelection categoryLike(String... value) {
        addLike(SightColumns.CATEGORY, value);
        return this;
    }

    public SightSelection region(String... value) {
        addEquals(SightColumns.REGION, value);
        return this;
    }

    public SightSelection regionNot(String... value) {
        addNotEquals(SightColumns.REGION, value);
        return this;
    }

    public SightSelection regionLike(String... value) {
        addLike(SightColumns.REGION, value);
        return this;
    }

    public SightSelection shownotifications(Boolean value) {
        addEquals(SightColumns.SHOWNOTIFICATIONS, toObjectArray(value));
        return this;
    }

    public SightSelection like(Boolean value) {
        addEquals(SightColumns.LIKE, toObjectArray(value));
        return this;
    }

    public SightSelection thumbnail(String... value) {
        addEquals(SightColumns.THUMBNAIL, value);
        return this;
    }

    public SightSelection thumbnailNot(String... value) {
        addNotEquals(SightColumns.THUMBNAIL, value);
        return this;
    }

    public SightSelection thumbnailLike(String... value) {
        addLike(SightColumns.THUMBNAIL, value);
        return this;
    }

    public SightSelection img1(String... value) {
        addEquals(SightColumns.IMG1, value);
        return this;
    }

    public SightSelection img1Not(String... value) {
        addNotEquals(SightColumns.IMG1, value);
        return this;
    }

    public SightSelection img1Like(String... value) {
        addLike(SightColumns.IMG1, value);
        return this;
    }

    public SightSelection img2(String... value) {
        addEquals(SightColumns.IMG2, value);
        return this;
    }

    public SightSelection img2Not(String... value) {
        addNotEquals(SightColumns.IMG2, value);
        return this;
    }

    public SightSelection img2Like(String... value) {
        addLike(SightColumns.IMG2, value);
        return this;
    }

    public SightSelection img3(String... value) {
        addEquals(SightColumns.IMG3, value);
        return this;
    }

    public SightSelection img3Not(String... value) {
        addNotEquals(SightColumns.IMG3, value);
        return this;
    }

    public SightSelection img3Like(String... value) {
        addLike(SightColumns.IMG3, value);
        return this;
    }

    public SightSelection img4(String... value) {
        addEquals(SightColumns.IMG4, value);
        return this;
    }

    public SightSelection img4Not(String... value) {
        addNotEquals(SightColumns.IMG4, value);
        return this;
    }

    public SightSelection img4Like(String... value) {
        addLike(SightColumns.IMG4, value);
        return this;
    }

    public SightSelection img5(String... value) {
        addEquals(SightColumns.IMG5, value);
        return this;
    }

    public SightSelection img5Not(String... value) {
        addNotEquals(SightColumns.IMG5, value);
        return this;
    }

    public SightSelection img5Like(String... value) {
        addLike(SightColumns.IMG5, value);
        return this;
    }

    public SightSelection coordinatex(Float... value) {
        addEquals(SightColumns.COORDINATEX, value);
        return this;
    }

    public SightSelection coordinatexNot(Float... value) {
        addNotEquals(SightColumns.COORDINATEX, value);
        return this;
    }

    public SightSelection coordinatexGt(float value) {
        addGreaterThan(SightColumns.COORDINATEX, value);
        return this;
    }

    public SightSelection coordinatexGtEq(float value) {
        addGreaterThanOrEquals(SightColumns.COORDINATEX, value);
        return this;
    }

    public SightSelection coordinatexLt(float value) {
        addLessThan(SightColumns.COORDINATEX, value);
        return this;
    }

    public SightSelection coordinatexLtEq(float value) {
        addLessThanOrEquals(SightColumns.COORDINATEX, value);
        return this;
    }

    public SightSelection coordinatey(Float... value) {
        addEquals(SightColumns.COORDINATEY, value);
        return this;
    }

    public SightSelection coordinateyNot(Float... value) {
        addNotEquals(SightColumns.COORDINATEY, value);
        return this;
    }

    public SightSelection coordinateyGt(float value) {
        addGreaterThan(SightColumns.COORDINATEY, value);
        return this;
    }

    public SightSelection coordinateyGtEq(float value) {
        addGreaterThanOrEquals(SightColumns.COORDINATEY, value);
        return this;
    }

    public SightSelection coordinateyLt(float value) {
        addLessThan(SightColumns.COORDINATEY, value);
        return this;
    }

    public SightSelection coordinateyLtEq(float value) {
        addLessThanOrEquals(SightColumns.COORDINATEY, value);
        return this;
    }
}
