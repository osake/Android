package com.eftimoff.knowledge.callbacks;

public interface DownloadListener<T> {

	void onSuccess(final T t);

	void onFailure();
}
