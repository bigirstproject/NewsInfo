package com.sunsun.framework.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 描述: 引用百度定位sdk封装单例类
 * 
 */
public class GpsLocSdk implements BDLocationListener {
	// 实体单例
	private static GpsLocSdk gpsLocSdk;

	private LocationClient mLocationClient = null;

	// 定位地理信息监听
	private GpsLoationListener mLoationListener;

	/**
	 * 改成私有访问，为了实现单例模式
	 * 
	 * @param context
	 *            程序上下文
	 */
	private GpsLocSdk(Context context) {
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(this);
		initLocSeting();
	}

	/**
	 * @param context
	 *            程序上下问(备注：推荐用getApplicationConext()，以便保证context全程有效)
	 * @return
	 */
	public static GpsLocSdk getInstance(Context context) {
		if (gpsLocSdk == null) {
			gpsLocSdk = new GpsLocSdk(context);
		}
		return gpsLocSdk;
	}

	/**
	 * 请求当前位置信息
	 * 
	 * @param loationListener
	 *            定位地理信息监听
	 */
	public void requestLocation(GpsLoationListener loationListener) {
		this.mLoationListener = loationListener;
		if (mLocationClient != null) {
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
	}

	/**
	 * 设置百度定位基本参数
	 */
	private void initLocSeting() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setTimeOut(3000);// 2秒超时
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		// option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		// option.setPoiNumber(5); // 最多返回POI个数
		// option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		if (mLoationListener != null) {
			GpsLocation gpsLocation = new GpsLocation();
			gpsLocation.setLatitude(location.getLatitude());
			gpsLocation.setLongitude(location.getLongitude());
			gpsLocation.setAddress(location.getAddrStr());
			gpsLocation.setCity(location.getCity());
			mLoationListener.onReceiveLocation(gpsLocation);
			mLoationListener.onReceiveLocation(location);
		}
		mLocationClient.stop();
	}

	@Override
	public void onReceivePoi(BDLocation location) {
		// TODO Auto-generated method stub

	}
}
