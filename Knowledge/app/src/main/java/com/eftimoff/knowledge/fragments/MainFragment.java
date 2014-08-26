package com.eftimoff.knowledge.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eftimoff.knowledge.R;
import com.eftimoff.knowledge.tasks.QuestionListTask;
import com.joanzapata.android.iconify.Iconify;
import com.tundem.actionitembadge.library.ActionItemBadge;

import java.util.concurrent.atomic.AtomicInteger;

public class MainFragment extends Fragment {

	private Button getListButton;

	/* Constants. */

	private static final String TAG = MainFragment.class.getSimpleName();

	/* Private fields. */

	private AtomicInteger badgeCount = new AtomicInteger(0);
	private TextView textView;

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
		//you can add some logic (hide it if the count == 0)
//		if (badgeCount.get() > 0) {
			ActionItemBadge.update(getActivity(), menu.findItem(R.id.action_gift), Iconify.IconValue.fa_gift, ActionItemBadge.BadgeStyle.GREEN, badgeCount.get());
//		} else {
//			ActionItemBadge.hide(menu.findItem(R.id.action_gift));
//		}
	}

	/* Private methods. */

	/* Inner classes. */


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		this.getListButton = (Button) rootView.findViewById(R.id.getList);
		getListButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new QuestionListTask().execute();
			}
		});
		return rootView;
	}
}