package com.demo.server;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.demo.about.AboutActivity;
import com.demo.appstart.LogInActivity;
import com.demo.client.MyClient;
import com.demo.networkinfo.MyNetworkInfo;
import com.demo.scanLAN.ServerList;
import com.demo.settings.Settings;
import com.example.socketdemo.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.Timer;
import java.util.TimerTask;

public class MyServer extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private SlidingMenu slidingMenu;
	public static String TAG = "MyServer";
	private String PREFS_NAME = "startStatus";
	private SharedPreferences startStatus;

	private TextView tv_server;
	private String username, serverip;

	private ToggleButton togbtn_create;
    private TextView tv_username;
	private Button btn_scan, btn_enter, btn_connect, btn_settings, btn_about, btn_exit;

    private boolean isExit = false;

	final Intent intent_service = new Intent();

	// private ServerService mServerService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_server);

		initView();
		initData();
	}

	@SuppressLint("NewApi")
	public void initView() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingMenu_offset);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu);

		tv_server = (TextView) findViewById(R.id.tv_server);

		tv_username = (TextView) findViewById(R.id.tv_sm_username);
		togbtn_create = (ToggleButton) findViewById(R.id.togbtn_create);
		btn_scan = (Button) findViewById(R.id.btn_scanLAN);
		btn_enter = (Button) findViewById(R.id.btn_enter);
		btn_connect = (Button) findViewById(R.id.btn_connect);
		btn_settings = (Button) findViewById(R.id.btn_settings);
		btn_about = (Button) findViewById(R.id.btn_about);
		btn_exit = (Button) findViewById(R.id.btn_exit);
	}

	public void initData() {
		startStatus = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		boolean hasLogged = startStatus.getBoolean("hasLogged", false);
		if (hasLogged) {
			SharedPreferences preferences = getSharedPreferences("UserData",
					Context.MODE_PRIVATE);
			username = preferences.getString("username", "");

		} else {
			Bundle bundle = this.getIntent().getExtras();
			username = bundle.getString("username");

			SharedPreferences.Editor editor = startStatus.edit();
			editor.putBoolean("hasLogged", true);
			editor.commit();
		}
		tv_server.setText("Welcom to use the MicroChat : " + username + "\n" + "Try to slide to the right");

		togbtn_create.setOnCheckedChangeListener(this);
		if (isServiceRunning()) {
			togbtn_create.setChecked(true);
		}
		tv_username.setText(username);
		btn_scan.setOnClickListener(this);
		btn_enter.setOnClickListener(this);
		btn_connect.setOnClickListener(this);
		btn_settings.setOnClickListener(this);
		btn_about.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.togbtn_create:
			if (isChecked) {
				intent_service.setAction("com.demo.socket.ServerService");
				// bindService(intent_service, mServiceConnection,
				// BIND_AUTO_CREATE);
				startService(intent_service);
			} else {
				stopService(intent_service);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_scanLAN:
			MyNetworkInfo networkInfo = new MyNetworkInfo();
			Log.d(TAG, networkInfo.isWifiActive(this) + "");
			if (networkInfo.isWifiActive(this)) {
				intent.setClass(this, ServerList.class);
				intent.putExtra("Name", username + " :");
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Please connect to the LAN", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_enter:
			if (isServiceRunning()) {
				Bundle bundle = new Bundle();
				bundle.putString("ServerIp", "127.0.0.1");
				bundle.putString("Name", username + " : ");
				intent.setClass(getApplicationContext(), MyClient.class);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						"System can't locate the Server!", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.btn_connect:
			showConnectDialog();
			break;
		case R.id.btn_settings:
			intent.setClass(this, Settings.class);
			startActivity(intent);
			break;
		case R.id.btn_about:
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_exit:
			showExitTypeDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			slidingMenu.toggle(true);
			break;
		case KeyEvent.KEYCODE_BACK:
            exitBy2Click();
			break;
		default:
			break;
		}
        return false;
	}

    private void exitBy2Click() {

        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "Double click to exit", Toast.LENGTH_SHORT).show();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }

    }

    // private ServiceConnection mServiceConnection = new ServiceConnection() {
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// // TODO Auto-generated method stub
	// mServerService = ((ServerService.MyBinder) service).getService();
	// Log.d(TAG, "onServiceConnected");
	// }
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// mServerService = null;
	// Log.d(TAG, "onServiceDisconnected");
	// };
	// };

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.demo.server.ServerService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			slidingMenu.toggle(true);
			return true;
		case R.id.item_01:
			Toast.makeText(this, "item_01", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.item_02:
			Toast.makeText(this, "item_02", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void showConnectDialog() {
		AlertDialog.Builder connectDialog = new AlertDialog.Builder(this);
		connectDialog.setTitle("Tip");
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogContent = inflater.inflate(R.layout.layout_connectdialog,
				null);
		final EditText et_ip = (EditText) dialogContent
				.findViewById(R.id.et_serverIp);
		connectDialog.setView(dialogContent);
		connectDialog.setPositiveButton("Connect",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						serverip = et_ip.getText().toString();
						connectToAssignServer();
					}
				});
		connectDialog.setNegativeButton(R.string.dialog_exit_cancel, null);
		connectDialog.show();
	}

	private void showExitTypeDialog() {
		AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
		exitDialog.setTitle("ExitType");
		exitDialog.setItems(R.array.items_exitdialog_type,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							SharedPreferences.Editor editor = startStatus
									.edit();
							editor.putBoolean("hasLogged", false);
							editor.commit();
							MyServer.this.finish();
							Intent intent = new Intent();
							intent.setClass(MyServer.this, LogInActivity.class);
							startActivity(intent);
							finish();
							break;
						case 1:
							stopService(intent_service);
							// unbindService(mServiceConnection);
							finish();
                            System.exit(0);
							break;
						default:
							break;
						}

					}
				});
		exitDialog.show();
	}

	private void connectToAssignServer() {
		if (serverip != null && !serverip.trim().equals("")) {
			Bundle bundle = new Bundle();
			bundle.putString("ServerIp", serverip);
			bundle.putString("Name", username + " : ");

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyClient.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), "ServerIp is null!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		stopService(intent_service);
		// unbindService(mServiceConnection);
		super.onDestroy();
	}
}
