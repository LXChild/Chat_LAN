package com.demo.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socketdemo.R;

public class FileSelection extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView lv_file;
	private Button btn_confirm, btn_cancel;
	private SimpleAdapter fileListAdapter;
	private ArrayList<String> fileName, safeFileName;
	private ArrayList<HashMap<String, Object>> fileArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_filelist);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lv_file = (ListView) findViewById(R.id.lv_file);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);

		btn_confirm.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		fileName = new ArrayList<String>();
		safeFileName = new ArrayList<String>();
		fileArray = new ArrayList<HashMap<String, Object>>();

		this.FilesListView(Environment.getExternalStorageDirectory().getPath());

		String[] content = new String[] { "image", "name", "path", "type",
				"parent", "select" };
		int[] fileId = new int[] { R.id.image, R.id.file_name, R.id.file_path,
				R.id.file_type, R.id.file_parent, R.id.select };
		fileListAdapter = new SimpleAdapter(getApplicationContext(), fileArray,
				R.layout.item_view, content, fileId);

		lv_file.setAdapter(fileListAdapter);
		lv_file.setOnItemClickListener(this);
	}

	private void FilesListView(String selectedPath) {
		File selectedFile = new File(selectedPath);
		if (selectedFile.canRead()) {
			File[] file = selectedFile.listFiles();
			fileArray.clear();
			for (int i = 0; i < file.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("image", file[i].isDirectory() ? R.drawable.folder
						: R.drawable.file);
				map.put("name", file[i].getName());
				map.put("path", file[i].getPath());
				map.put("type", file[i].isDirectory());
				map.put("parent", file[i].getParent());
				map.put("select", file[i].isFile() ? R.drawable.unselected : "");
				fileArray.add(map);
			}
			// 判断有无父目录，增加返回上一级目录菜单
			if (selectedFile.getParent() != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", "返回上一级目录");
				map.put("path", selectedFile.getParent());
				map.put("type", true);
				map.put("parent", selectedFile.getParent());
				fileArray.add(0, map);
			}
		} else {
			Toast.makeText(getApplicationContext(), "该目录不能读取",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		TextView isDirectory = (TextView) view.findViewById(R.id.file_type);
		TextView path = (TextView) view.findViewById(R.id.file_path);
		TextView name = (TextView) view.findViewById(R.id.file_name);

		if (Boolean.parseBoolean(isDirectory.getText().toString())) {
			// 进入目录
			this.FilesListView(path.getText().toString());
			fileListAdapter.notifyDataSetChanged();
			fileName.clear();
			safeFileName.clear();
			SetSendButtonEnabled();
		} else {
			// 选择文件
			ImageView select = (ImageView) view.findViewById(R.id.select);
			if (Integer.parseInt(select.getTag().toString()) == 1) {
				select.setImageResource(R.drawable.unselected);
				select.setTag(0);
				fileName.remove(name.getText().toString());
				safeFileName.remove(path.getText().toString());
			} else {
				select.setImageResource(R.drawable.selected);
				select.setTag(1);
				fileName.add(name.getText().toString());
				safeFileName.add(path.getText().toString());
			}
			SetSendButtonEnabled();
		}
	}

	void SetSendButtonEnabled() {
		btn_confirm.setEnabled(false);
		if (fileName.size() > 0) {
			btn_confirm.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:
			Intent intent = new Intent();
			intent.putStringArrayListExtra("fileName", fileName);
			intent.putStringArrayListExtra("safeFileName", safeFileName);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.btn_cancel:
			finish();
			break;

		default:
			break;
		}
	}
}
