package com.eftimoff.bulgaria.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.eftimoff.bulgaria.R;
import com.eftimoff.bulgaria.model.Category;

public class DrawerAdapter extends ArrayAdapter<Category> {

	/* Constants */

	private static final String DRAWABLE = "drawable";

	/* Fields */

	private List<Category> categories;

	private String mPackageName;

	/* Constructors */

	public DrawerAdapter(Context context, int resource, List<Category> categories) {
		super(context, resource, categories);
		this.categories = categories;

		// get the package name
		mPackageName = context.getPackageName();
	}

	/* Public methods */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.view_drawer_item, parent, false);

			holder = new ViewHolder();
			holder.label = (TextView) convertView.findViewById(R.id.viewDrawerItemLabel);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.viewDrawerItemCheckBox);
			holder.checkbox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					final CheckBox checkBox = (CheckBox) v;
					final Category category = (Category) checkBox.getTag();
					if (category.isChecked()) {
						category.setChecked(!category.isChecked());
						checkBox.setChecked(category.isChecked());
					}
				}
			});
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Category category = categories.get(position);

		holder.label.setText(category.getName());
		holder.label.setCompoundDrawablesWithIntrinsicBounds(getResourceByName(category.getName()),
				0, 0, 0);
		holder.checkbox.setTag(category);
		holder.checkbox.setChecked(category.isChecked());

		return convertView;

	}

	/* Private methods */

	private int getResourceByName(final String resource) {
		return getContext().getResources().getIdentifier(resource + ".png", DRAWABLE, mPackageName);
	}

	/* Private classes */

	private static class ViewHolder {
		public TextView label;
		public CheckBox checkbox;
	}

}
