package com.pd.pdp.gather.okhttp.callback;

import com.pd.pdp.gather.okhttp.Response;
import okhttp3.Call;


public abstract class Callback{
	//
	public abstract void onFailure(Call call,Exception e,String id);
	//
	public abstract void onResponse(Call call, Response response, String id);
}