package com.eftimoff.likelyandroid.adapter;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eftimoff.likelyandroid.R;
import com.eftimoff.likelyandroid.models.Joke;
import com.squareup.picasso.Picasso;

@SuppressLint("DefaultLocale")
public class ItemAdapter extends ArrayAdapter<Joke> {

	/* Constants */

	private static final String DRAWABLE = "drawable";

	/* Fields */

	private List<Joke> mData;
	private Context mContext;
	private int mLayoutResID;
	private LayoutInflater mInflater;

	public ItemAdapter(Context context, int layoutResourceId, List<Joke> data) {
		super(context, layoutResourceId, data);
		this.mData = data;
		this.mContext = context;
		this.mLayoutResID = layoutResourceId;
		this.mInflater = ((Activity) context).getLayoutInflater();
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Joke joke = mData.get(position);

		ItemHolder holder = null;
		View row = convertView;

		if (row == null) {
			row = mInflater.inflate(mLayoutResID, parent, false);
			holder = new ItemHolder();
			holder.text = (TextView) row.findViewById(R.id.text);
			holder.text.setOnClickListener(new TextClickListener());
			holder.category = (TextView) row.findViewById(R.id.categoryTextView);
			holder.image = (ImageView) row.findViewById(R.id.imageView);
			holder.date = (TextView) row.findViewById(R.id.dateTextView);
			holder.share = (Button) row.findViewById(R.id.shareButton);
			holder.height = row.getLayoutParams().height;
			row.setTag(holder);
		} else {
			holder = (ItemHolder) row.getTag();
		}

		if (holder.height != 0 && row.getLayoutParams().height != holder.height) {
			row.getLayoutParams().height = holder.height;
		}

		if (!holder.text.getText().equals(joke.getText())) {
			holder.text.setText(joke.getText());
		}
		if (!holder.category.getText().equals(joke.getCategory())) {
			holder.category.setText(joke.getCategory());
		}
		holder.text.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
		holder.date.setText(joke.getDate());
		holder.share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, joke.getCategory());
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, joke.getText());
				mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources()
						.getString(R.string.share_chooser_text)));
			}
		});

		int identifier = contructId(joke.getCategory());
		if (identifier != 0) {
			Picasso.with(mContext).load(identifier).into(holder.image);
		}
		final Drawable drawable = row.getBackground();
		if (drawable instanceof GradientDrawable) {
			((GradientDrawable) drawable).setColor(joke.getBackgroundColor());
		}

		return row;

	}

	private class TextClickListener implements OnClickListener {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			final TextView textView = (TextView) v;
			if (textView.getMaxLines() == Integer.MAX_VALUE) {
				textView.setMaxLines(5);
			} else {
				textView.setMaxLines(Integer.MAX_VALUE);
			}
		}

	}

	private int contructId(final String category) {
		return mContext.getResources().getIdentifier(
				"ic_" + category.toLowerCase(Locale.getDefault()).replace(" ", "_"), DRAWABLE,
				mContext.getPackageName());
	}

	private static class ItemHolder {
		private ImageView image;
		private TextView category;
		private TextView text;
		private TextView date;
		private Button share;
		private int height;
	}
}
