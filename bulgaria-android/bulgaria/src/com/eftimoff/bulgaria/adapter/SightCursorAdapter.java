package com.eftimoff.bulgaria.adapter;

import java.text.DecimalFormat;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eftimoff.bulgaria.R;
import com.eftimoff.bulgaria.provider.sight.SightCursor;
import com.eftimoff.bulgaria.utils.DistanceUtils;
import com.eftimoff.bulgaria.utils.ResourceUtils;
import com.eftimoff.bulgaria.utils.ResourceUtils.ResourceType;
import com.squareup.picasso.Picasso;

public class SightCursorAdapter extends CursorAdapter {

	private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.##");

	private LayoutInflater mInflater;
	private Picasso mPicasso;
	private Location mLocation;

	public SightCursorAdapter(Context context, Cursor c, int flags, final Location location) {
		super(context, c, flags);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPicasso = Picasso.with(context);
		mLocation = location;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		final SightCursor sightCursor = new SightCursor(cursor);
		final int imageId = ResourceUtils.getResourceIdByName(context, sightCursor.getThumbnail(),
				ResourceType.DRAWABLE);
		mPicasso.load(imageId).into(viewHolder.image);
		viewHolder.label.setText(sightCursor.getLabel());
		viewHolder.description.setText(sightCursor.getDescription());
		final double distance = DistanceUtils.distance(mLocation.getLatitude(),
				mLocation.getLongitude(), sightCursor.getCoordinatex(),
				sightCursor.getCoordinatey());

		viewHolder.direction.setText(DECIMAL_FORMATTER.format(distance) + " км");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View rowView = mInflater.inflate(R.layout.view_sight, parent, false);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.image = (ImageView) rowView.findViewById(R.id.viewSightImageView);
		viewHolder.label = (TextView) rowView.findViewById(R.id.viewSightLabel);
		viewHolder.description = (TextView) rowView.findViewById(R.id.viewSightDescription);
		viewHolder.direction = (TextView) rowView.findViewById(R.id.viewSightDirection);
		// viewHolder.likeButton = (Button)
		// rowView.findViewById(R.id.viewSightLike);
		rowView.setTag(viewHolder);
		return rowView;
	}

	public void changeLocation(final Location location) {
		mLocation = location;
		this.notifyDataSetChanged();
	}

	private static class ViewHolder {
		public ImageView image;
		public TextView label;
		public TextView description;
		public TextView direction;
		public Button likeButton;
	}

}