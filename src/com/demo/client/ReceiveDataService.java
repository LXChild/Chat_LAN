package com.demo.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ReceiveDataService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		new ReceiveTextThread().start();
		new FileReceiveThread().start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("ReceiveDataService", "onDestroy");
		super.onDestroy();
	}

}
