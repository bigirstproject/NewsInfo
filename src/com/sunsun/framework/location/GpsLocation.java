package com.sunsun.framework.location;

/**
 * 描述:地理位置信息类，包括：经纬度，地理信息
 * 
 */
public class GpsLocation {
	// 纬度
	private double latitude;

	// 经度
	private double longitude;

	// 地址
	private String address;

	// 所在城市信息
	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
