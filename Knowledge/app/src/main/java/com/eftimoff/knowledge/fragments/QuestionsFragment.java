package com.eftimoff.knowledge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eftimoff.knowledge.R;
import com.eftimoff.knowledge.adapters.QuestionsAdapter;
import com.eftimoff.knowledge.callbacks.DownloadListener;
import com.eftimoff.knowledge.tasks.QuestionsTask;
import com.eftimoff.question.model.QuestionRecord;
import com.joanzapata.android.iconify.Iconify;
import com.tundem.actionitembadge.library.ActionItemBadge;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestionsFragment extends Fragment implements DownloadListener<List<QuestionRecord>> {

	private static final String TAG = QuestionsFragment.class.getSimpleName();

	/* Constants. */

	/* Private fields. */

	private AtomicInteger badgeCount = new AtomicInteger(0);
	private ViewPager pager;
	private QuestionsAdapter questionsAdapter;

	/* Constructors. */

	/* Public static methods. */

	/* Public methods. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment_main, menu);
//		if (badgeCount.get() > 0) {
		ActionItemBadge.update(getActivity(), menu.findItem(R.id.action_gift), Iconify.IconValue.fa_gift, ActionItemBadge.BadgeStyle.GREEN, badgeCount.get());
//		} else {
//			ActionItemBadge.hide(menu.findItem(R.id.action_gift));
//		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final QuestionsTask questionsTask = new QuestionsTask(this);
		questionsTask.execute(15);
	}

	/* Private methods. */

	private void updateGiftColor(final Menu menu, final ActionItemBadge.BadgeStyle style) {
		ActionItemBadge.update(getActivity(), menu.findItem(R.id.action_gift), Iconify.IconValue.fa_gift, style, badgeCount.get());
	}

	/* Inner classes. */


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_questions, container, false);
		pager = (ViewPager) rootView.findViewById(R.id.pager);
		return rootView;
	}


	@Override
	public void onSuccess(List<QuestionRecord> questionRecords) {
		questionsAdapter = new QuestionsAdapter(getFragmentManager(), questionRecords);
		pager.setAdapter(questionsAdapter);
	}

	@Override
	public void onFailure() {

	}
}