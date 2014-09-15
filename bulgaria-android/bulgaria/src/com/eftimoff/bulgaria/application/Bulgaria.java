package com.eftimoff.bulgaria.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.eftimoff.bulgaria.R;
import com.eftimoff.bulgaria.model.Category;

public class Bulgaria extends Application {

	private static Bulgaria instance;

	private List<Category> mCategories;

	public Bulgaria() {
	}

	public static Bulgaria getInstance() {
		if (instance == null) {
			instance = new Bulgaria();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mCategories = new ArrayList<Category>();
		initCategories(getResources().getStringArray(R.array.categories));
		instance = this;
	}

	public List<Category> getCategories() {
		return mCategories;
	}

	private void initCategories(final String[] categories) {
		for (String category : categories) {
			mCategories.add(new Category(category, true));
		}
	}
}
