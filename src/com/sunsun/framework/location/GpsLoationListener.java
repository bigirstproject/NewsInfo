package com.sunsun.framework.location;

import com.baidu.location.BDLocation;

/**
 * 描述: gps信息监听接口
 * 
 */
public interface GpsLoationListener {
	/**
	 * 监听百度定位地理位置信息
	 * 
	 * @param location
	 *            地理位置信息实体
	 */
	public void onReceiveLocation(GpsLocation location);

	/**
	 * 获取详细数据信息
	 * 
	 * @param location
	 */
	public void onReceiveLocation(BDLocation location);
}
