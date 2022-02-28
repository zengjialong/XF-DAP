package com.chs.mt.xf_dap.util;

import java.net.*;

public interface ILoader {
	public void onFail(URLConnection urlConnection, String error);
	public void onTimeout(URLConnection urlConnection);
	public void onReceiveData(URLConnection urlConnection, byte[] data);
	public void onFinishLoading(URLConnection urlConnection);
}
