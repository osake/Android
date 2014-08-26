package com.eftimoff.knowledge.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eftimoff.knowledge.R;
import com.eftimoff.knowledge.tasks.QuestionListTask;

public class MainFragment extends Fragment {

	private Button getListButton;

	public MainFragment() {
	}


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