package com.demo.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServerService extends Service {
	// private MyBinder mBinder = new MyBinder();
	private static final String TAG = "ServerService";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreate");
		initData();
		super.onCreate();
	}

	private void initData() {
		new AcceptConnectThread().start();
		new FileReceiveThread().start();
		Toast.makeText(this, "Server is running...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Server is stoped", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onBind");
		return null;// mBinder;
	}

	// @Override
	// public boolean onUnbind(Intent intent) {
	// // TODO Auto-generated method stub
	// Log.d(TAG, "onUnbind");
	// return super.onUnbind(intent);
	// }

	// public class MyBinder extends Binder {
	// ServerService getService() {
	// return ServerService.this;
	// }
	// }
}
