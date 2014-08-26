package com.eftimoff.knowledge.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eftimoff.knowledge.KnowledgeApplication;
import com.eftimoff.question.model.QuestionRecordCollection;

import java.io.IOException;

public class QuestionListTask extends AsyncTask<Void, Void, Void> {
	@Override
	protected Void doInBackground(Void... params) {
		try {
			final QuestionRecordCollection execute = KnowledgeApplication.getQuestionApi(null).listQuestions(15).execute();
			Log.i("tag", Integer.toString(execute.getItems().size()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
