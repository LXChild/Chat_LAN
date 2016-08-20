package com.demo.appstart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import com.demo.server.MyServer;
import com.example.socketdemo.R;

public class SplashActivity extends Activity {

	private String PREFS_NAME = "startStatus";
	private boolean hasLogged = false;
	private final int GO_LOGIN = 0X234;
	private final int GO_HOME = 0X235;
	private int SPLASH_DELAY_MILLIS = 1000;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_LOGIN:
				goLogIn();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();
		setContentView(R.layout.layout_splash_activity);
		initData();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	private void initData() {
		SharedPreferences preference = getSharedPreferences(PREFS_NAME,
				MODE_PRIVATE);
		hasLogged = preference.getBoolean("hasLogged", false);

		if (!hasLogged) {
			handler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS);
		} else {
			handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}
	}

	private void goHome() {
		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, MyServer.class);
		startActivity(intent);
		finish();
	}

	private void goLogIn() {
		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, LogInActivity.class);
		startActivity(intent);
		finish();
	}
}
