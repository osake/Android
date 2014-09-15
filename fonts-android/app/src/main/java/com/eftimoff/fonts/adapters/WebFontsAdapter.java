package com.eftimoff.fonts.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eftimoff.fonts.R;
import com.eftimoff.fonts.models.Font;
import com.eftimoff.fonts.tasks.WebFontDownloadTask;
import com.eftimoff.fonts.utils.DownloadUtils;
import com.eftimoff.fonts.utils.TypeFaces;

import java.io.File;
import java.util.List;
import java.util.Map;

public class WebFontsAdapter extends ArrayAdapter<Font> {

	public static final String TAG = WebFontsAdapter.class.getSimpleName();

	private LayoutInflater inflater;
	private int resource;
	private String text = "";

	public WebFontsAdapter(Context context, int resource, List<Font> fonts) {
		super(context, resource, fonts);
		inflater = ((Activity) context).getLayoutInflater();
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			row = inflater.inflate(resource, parent, false);

			holder = new ViewHolder();
			holder.nameTextView = (TextView) row.findViewById(R.id.nameTextView);
			holder.textTextView = (TextView) row.findViewById(R.id.textTextView);
			holder.categoryButton = (Button) row.findViewById(R.id.categoryButton);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}


		final Font font = getItem(position);
		final Map<String, String> files = font.getFiles();
		holder.categoryButton.setText(font.getCategory());
		holder.nameTextView.setText(font.getFamily());
		holder.textTextView.setText(text);

		final String firstFilename = files.get(font.getVariants().get(0));
		final String filename = DownloadUtils.getFilename(firstFilename);
		if (new File(getContext().getCacheDir(), filename).exists()) {
			final Typeface myTypeface = TypeFaces.getTypeFace(getContext(), filename);
			holder.textTextView.setTypeface(myTypeface);
		} else {
			final WebFontDownloadTask webFontDownloadTask = new WebFontDownloadTask(getContext().getApplicationContext());
			webFontDownloadTask.execute(firstFilename);
		}

		return row;
	}

	public void setText(final String text) {
		this.text = text;
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		public Button categoryButton;
		public TextView nameTextView;
		public TextView textTextView;
	}
}
