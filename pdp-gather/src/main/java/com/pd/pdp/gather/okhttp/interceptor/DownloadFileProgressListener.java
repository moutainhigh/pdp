package com.pd.pdp.gather.okhttp.interceptor;


public interface DownloadFileProgressListener {
	void updateProgress(long downloadLenth, long totalLength, boolean done);
}