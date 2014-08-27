package com.eftimoff.knowledge.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.eftimoff.knowledge.fragments.QuestionFragment;
import com.eftimoff.question.model.QuestionRecord;

import java.util.List;

public class QuestionsAdapter extends FragmentStatePagerAdapter {

	private List<QuestionRecord> list;

	public QuestionsAdapter(final FragmentManager fm, final List<QuestionRecord> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int position) {
		return QuestionFragment.getInstance(list.get(position));
	}

	@Override
	public int getCount() {
		return list.size();
	}
}