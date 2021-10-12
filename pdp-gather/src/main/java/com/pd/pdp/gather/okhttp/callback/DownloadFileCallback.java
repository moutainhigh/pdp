package com.pd.pdp.gather.okhttp.callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pd.pdp.gather.okhttp.Response;
import com.pd.pdp.gather.okhttp.util.FileUtil;
import okhttp3.Call;


public abstract class DownloadFileCallback extends Callback{
	//
	public static Logger logger = LoggerFactory.getLogger(DownloadFileCallback.class);
	//
	private String fileAbsolutePath;
	//
	public DownloadFileCallback(){
	}
	//
	public DownloadFileCallback(String fileAbsolutePath){
		this.fileAbsolutePath=fileAbsolutePath;
	}
	//
	@Override
	public void onResponse(Call call, Response response, String id) {
		try {
			if(fileAbsolutePath!=null&&fileAbsolutePath.length()>0){
				File file=new File(fileAbsolutePath);
				FileUtil.saveContent(response.body().bytes(),file);
				onSuccess(call,file, id);
			}else{
				onSuccess(call,response.body().byteStream(),id);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	//
	public void onSuccess(Call call,File file, String id) {
		
	}
	//
	public void onSuccess(Call call,InputStream fileStream, String id) {
		
	}
}
