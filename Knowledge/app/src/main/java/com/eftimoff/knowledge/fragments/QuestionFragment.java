package com.eftimoff.knowledge.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eftimoff.knowledge.R;
import com.eftimoff.question.model.QuestionRecord;

public class QuestionFragment extends Fragment {

	private static final String TAG = QuestionFragment.class.getSimpleName();

	private QuestionRecord questionRecord;

	public static Fragment getInstance(QuestionRecord questionRecord) {
		final QuestionFragment fragment = new QuestionFragment();
		fragment.setQuestionRecord(questionRecord);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_question, container, false);
		final TextView text = (TextView) rootView.findViewById(R.id.text);
		text.setText(questionRecord.getQuestion());
		return rootView;
	}

	public QuestionRecord getQuestionRecord() {
		return questionRecord;
	}

	public void setQuestionRecord(QuestionRecord questionRecord) {
		this.questionRecord = questionRecord;
	}
}