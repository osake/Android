package com.eftimoff.bulgaria.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eftimoff.bulgaria.R;

public class SightView extends RelativeLayout {

	/* Constants */

	private static final String STRING = "string";

	/* Fields */

	private ImageView mImage;
	private TextView mLabel;
	private TextView mDescription;
	private TextView mDirection;
	private Button mRegion;
	private Button mCategory;
	private Button mLike;

	private String mPackageName;

	/* Constructors */

	public SightView(Context context) {
		super(context, null);
	}

	public SightView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Inflate root view
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_sight, this, true);

		// init the views
		mImage = (ImageView) findViewById(R.id.viewSightImageView);
		mLabel = (TextView) findViewById(R.id.viewSightLabel);
		mDescription = (TextView) findViewById(R.id.viewSightDescription);
		mDirection = (TextView) findViewById(R.id.viewSightDirection);
//		mRegion = (Button) findViewById(R.id.viewSightRegion);
//		mCategory = (Button) findViewById(R.id.viewSightCategory);
		mLike = (Button) findViewById(R.id.viewSightLike);

		// get the package name
		mPackageName = context.getPackageName();

	}

	/* Public methods */

	public void setImage(final String imageId) {
		final int resourceId = getResourceByName(imageId);
		mImage.setBackgroundResource(resourceId);
	}

	public void setRegionBackground(final String region) {
		final int resourceId = getResourceByName(region);
		mRegion.setBackgroundResource(resourceId);
	}

	public void setCategoryBackground(final String category) {
		final int resourceId = getResourceByName(category);
		mCategory.setBackgroundResource(resourceId);
	}

	public void setLabel(final String label) {
		mLabel.setText(label);
	}

	public void setDescription(final String description) {
		mDescription.setText(description);
	}

	public void setDirection(final String direction) {
		mDirection.setText(direction);
	}

	public void setLikeBackground(final String like) {
		final int resourceId = getResourceByName(like);
		mLike.setBackgroundResource(resourceId);
	}

	/* Private methods */

	private int getResourceByName(final String resource) {
		return getResources().getIdentifier(resource, STRING, mPackageName);
	}

}
