package com.demo.networkinfo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class MyNetworkInfo {

	private static final String TAG = "NetWorkInfo";

	public boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public String getWifiAddress(Context context) {
		String strRet = "";
		WifiManager myWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
		int myIp = myWifiInfo.getIpAddress();

		int intMyIp3 = myIp / 0x1000000;
		int intMyIp3mod = myIp % 0x1000000;

		int intMyIp2 = intMyIp3mod / 0x10000;
		int intMyIp2mod = intMyIp3mod % 0x10000;

		int intMyIp1 = intMyIp2mod / 0x100;
		int intMyIp0 = intMyIp2mod % 0x100;
		strRet = String.valueOf(intMyIp0) + "." + String.valueOf(intMyIp1)
				+ "." + String.valueOf(intMyIp2) + "."
				+ String.valueOf(intMyIp3);

		return strRet;
	}

	public String getLocalHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG + "getIp", e.toString());
			e.printStackTrace();
		}
		return null;
	}
}
