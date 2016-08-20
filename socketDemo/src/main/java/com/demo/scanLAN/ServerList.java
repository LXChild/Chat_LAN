package com.demo.scanLAN;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.client.MyClient;
import com.example.socketdemo.R;

public class ServerList extends Activity implements OnItemClickListener {
	private static final String TAG = "ServerList";
	private static ListView lv_server;
	private ProgressBar pb_scanServer = null;
	private static ServerListAdapter mAdapter;
	private static String LocalIpAddress;
	private Context cxt;
	private String Name;
	private static String status;

	private static TextView tv_scanner;

	private Scanner_LAN scanner;

	private static LinearLayout ll_scanning;
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT);

	static ArrayList<HashMap<String, Object>> list_server = new ArrayList<HashMap<String, Object>>();

	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			switch (msg.what) {
			case 0x235:
				LocalIpAddress = msg.obj.toString();
				Log.d(TAG, "Received Local ip:" + LocalIpAddress);
				map.put("ServerIp", msg.obj.toString());
				map.put("IpTag", "LocaIpAdress");
				list_server.add(map);
				mAdapter.notifyDataSetChanged();
				break;
			case 0x236:
				Log.d(TAG, "Receive External ip:" + msg.obj.toString());
				map.put("ServerIp", msg.obj.toString());
				map.put("IpTag", "ExternalServer");
				list_server.add(map);
				mAdapter.notifyDataSetChanged();
				break;
			case 0x237:
				lv_server.removeFooterView(ll_scanning);
				Log.d(TAG, "Scan Over!");
			case 0x238:
				status = msg.obj.toString();
				tv_scanner.setText("System is pinging the:" + status);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	public ServerList() {
		// TODO Auto-generated constructor stub
		cxt = ServerList.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	private void initView() {
		setContentView(R.layout.layout_server_list);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		ll_scanning = new LinearLayout(this);
		ll_scanning.setOrientation(LinearLayout.HORIZONTAL);

		pb_scanServer = new ProgressBar(this);
		pb_scanServer.setPadding(0, 0, 15, 0);
		ll_scanning.addView(pb_scanServer, mLayoutParams);

		tv_scanner = new TextView(this);
		tv_scanner.setGravity(Gravity.CENTER_VERTICAL);
		ll_scanning.addView(tv_scanner, FFlayoutParams);

		ll_scanning.setGravity(Gravity.CENTER);

		lv_server = (ListView) findViewById(R.id.lv_server);
		lv_server.addFooterView(ll_scanning);
	}

	private void initData() {
		mAdapter = new ServerListAdapter(this);
		lv_server.setAdapter(mAdapter);
		lv_server.setOnItemClickListener(this);

		scanner = new Scanner_LAN(cxt, handler);
		scanner.start();

		Bundle bundle = this.getIntent().getExtras();
		Name = bundle.getString("Name");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Item" + position + "has been clicked");
		if (view == ll_scanning) {
			return;
		}
		scanner.stopThread(false);
		lv_server.removeFooterView(ll_scanning);
		String serverIp = getData().get(position).get("ServerIp").toString();
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MyClient.class);

		Bundle bundle = new Bundle();
		bundle.putString("ServerIp", serverIp);
		bundle.putString("Name", Name);

		intent.putExtras(bundle);
		startActivity(intent);
		ServerList.this.finish();
	}

	public static ArrayList<HashMap<String, Object>> getData() {
		return list_server;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return false;

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		list_server.removeAll(list_server);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");
		scanner.stopThread(false);
		super.onPause();
	}
}
