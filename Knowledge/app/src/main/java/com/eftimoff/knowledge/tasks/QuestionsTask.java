package com.eftimoff.knowledge.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.eftimoff.knowledge.KnowledgeApplication;
import com.eftimoff.knowledge.callbacks.DownloadListener;
import com.eftimoff.question.model.QuestionRecord;
import com.eftimoff.question.model.QuestionRecordCollection;

import java.io.IOException;
import java.util.List;

public class QuestionsTask extends AsyncTask<Integer, Void, List<QuestionRecord>> {

	private DownloadListener<List<QuestionRecord>> downloadListener;

	public QuestionsTask(final DownloadListener<List<QuestionRecord>> downloadListener) {
		this.downloadListener = downloadListener;
	}

	private static final String TAG = QuestionsTask.class.getSimpleName();

	@Override
	protected List<QuestionRecord> doInBackground(Integer... params) {
		if (params.length <= 0) {
			throw new IllegalArgumentException("You must provide params for the count of the questions.");
		}
		try {
			final QuestionRecordCollection execute = KnowledgeApplication.getQuestionApi(null).listQuestions(params[0]).execute();
			return execute.getItems();
		} catch (IOException e) {
			Log.e(TAG, "Request for questions failed.");
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<QuestionRecord> questionRecords) {
		if (downloadListener != null) {
			if (questionRecords == null) {
				downloadListener.onFailure();
			} else {
				downloadListener.onSuccess(questionRecords);
			}
		}
	}
}
