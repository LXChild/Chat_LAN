package com.demo.settings;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.socketdemo.R;

public class Settings extends Activity implements OnItemClickListener {

	private ListView lv_settings;
	private SettingsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	private void initView() {
		setContentView(R.layout.layout_settings);

		ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		lv_settings = (ListView) findViewById(R.id.lv_settings);
	}

	private void initData() {
		adapter = new SettingsAdapter(this);
		lv_settings.setAdapter(adapter);
		lv_settings.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
		String Item = tv_item.getText().toString();
	}
}
