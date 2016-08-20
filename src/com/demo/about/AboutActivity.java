package com.demo.about;

import com.demo.networkinfo.MyNetworkInfo;
import com.example.socketdemo.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private TextView tv_about;
	private MyNetworkInfo networkInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	public void initView() {
		setContentView(R.layout.layout_about);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		tv_about = (TextView) findViewById(R.id.tv_about);
	}

	private void initData() {
		String networkStatus = "";
		String networkAddress = "";
		networkInfo = new MyNetworkInfo();
		if (networkInfo.isWifiActive(this)) {
			networkStatus = "Wifi";
			networkAddress = networkInfo.getWifiAddress(this);
		} else if (networkInfo.is3rd(this)) {
			networkStatus = "Mobile";
			networkAddress = networkInfo.getLocalHostIp();
		} else {
			networkStatus = "Not available";
			networkAddress = "127.0.0.1";
		}

		String Author = "Author: UIT_2013Team";
		String FeedbackGroup = "FeedbackGroup: 49388448";
		String NetworkStatus = "Network status: " + networkStatus;
		String NetworkAddress = "NetworkAddress: " + networkAddress;
		tv_about.setText(Author + "\n" + FeedbackGroup + "\n" + NetworkStatus
				+ "\n" + NetworkAddress);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		return super.onOptionsItemSelected(item);
	}
}
