package com.sunsun.framework.http;

import java.util.Hashtable;
import java.util.Set;

import com.sunsun.framework.component.base.BaseApplication;
import com.sunsun.framework.http.NetWorkUtil.NetworkType;

/**
 * 抽象请求包
 * 
 */
public abstract class AbstractRequestPackage implements RequestPackage {

	protected Hashtable<String, Object> mParams;

	@Override
	public String getGetRequestParams() {
		if (mParams != null && mParams.size() >= 0) {
			StringBuilder builder = new StringBuilder();
			builder.append("/");
			final Set<String> keys = mParams.keySet();
			for (String key : keys) {
				builder.append(key).append("=").append(mParams.get(key))
						.append("&");
			}
			builder.deleteCharAt(builder.length() - 1);
			return builder.toString();
		}
		return "";
	}

	public Hashtable<String, Object> getParams() {
		return mParams;
	}

	public void setParams(Hashtable<String, Object> mParams) {
		this.mParams = mParams;
	}

	@Override
	public Hashtable<String, Object> getSettings() {
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		String networkType = NetWorkUtil.getNetworkType(BaseApplication
				.getInstance());
		if (NetworkType.WIFI.equals(networkType)) {
			params.put("conn-timeout", 15 * 1000);
			params.put("socket-timeout", 15 * 1000);
		} else {
			params.put("conn-timeout", 20 * 1000);
			params.put("socket-timeout", 20 * 1000);
		}
		return params;
	}
}
