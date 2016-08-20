package com.demo.client;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.mediaplayer.SoundMeter;
import com.demo.tools.MyTools;
import com.example.socketdemo.R;

public class MyClient extends Activity implements OnClickListener {

	public static String ip, name;

	public static String TAG = "MyClient";

	private static ScrollView sv_show;
	private static TextView tv_content;
	private EditText et_message;
	private Button btn_send, btn_more;
	private LinearLayout ll_more;
	private ImageButton ib_voice, ib_photo, ib_video, ib_file;
	private Intent intent_service;

	private static MyTools tools;
	public static Context cxt;

	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x234:
				tv_content.append(tools.getSystemTime() + "\n"
						+ msg.obj.toString() + "\n");
				break;
			case 0x233:
				tv_content.append(tools.getSystemTime() + "\n" + "System : "
						+ msg.obj.toString() + "\n");
				break;
			default:
				break;
			}

			tools.scroll2Bottom(sv_show, tv_content);
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chat);

		initView();
		initData();
	}

	@SuppressLint("NewApi")
	public void initView() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		sv_show = (ScrollView) findViewById(R.id.sv_show);
		tv_content = (TextView) findViewById(R.id.tv_content);

		et_message = (EditText) findViewById(R.id.et_message);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_more = (Button) findViewById(R.id.btn_more);
		ll_more = (LinearLayout) findViewById(R.id.ll_more);
		ib_voice = (ImageButton) findViewById(R.id.ib_voice);
		ib_photo = (ImageButton) findViewById(R.id.ib_photo);
		ib_video = (ImageButton) findViewById(R.id.ib_video);
		ib_file = (ImageButton) findViewById(R.id.ib_file);

		btn_send.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		ib_file.setOnClickListener(this);
		ib_photo.setOnClickListener(this);
		ib_video.setOnClickListener(this);

		ib_voice.setOnTouchListener(new OnTouchListener() {

			SoundMeter mSensor = new SoundMeter();
			String VoiceName;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mSensor.start();
					VoiceName = mSensor.getVoiceName();
					break;
				case MotionEvent.ACTION_UP:
					mSensor.stop();
					ArrayList<String> fileNameArray = new ArrayList<String>();
					fileNameArray.add(VoiceName);

					ArrayList<String> filePathArray = new ArrayList<String>();
					String VoicePath = Environment
							.getExternalStorageDirectory().getPath()
							+ "/MicroChat/Record/" + VoiceName;
					filePathArray.add(VoicePath);
					new FileSendThread(fileNameArray, filePathArray, handler,
							ip).start();
					break;
				default:
					break;
				}
				return false;
			}
		});

		et_message.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onTextChanged");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				Log.d(TAG, "beforeTextChanged");
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.d(TAG, "afterTextChanged");
				if (et_message.getText().toString().trim().equals("")) {
					btn_send.setVisibility(View.GONE);
					btn_more.setVisibility(View.VISIBLE);
				} else {
					btn_send.setVisibility(View.VISIBLE);
					btn_more.setVisibility(View.GONE);
				}
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		ip = bundle.getString("ServerIp");
		name = bundle.getString("Name");
		tools = new MyTools();
		cxt = this;

		intent_service = new Intent();
		Bundle bundle_service = new Bundle();
		bundle_service.putString("ServerIp", ip);
		bundle_service.putString("UserName", name);
		intent_service.putExtras(bundle);
		intent_service.setAction("com.demo.client.RecieveDataService");
		startService(intent_service);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			String msgContent = et_message.getText().toString();
			new SendTextThread(msgContent).start();
			et_message.setText(null);
			break;
		case R.id.btn_more:
			if (ll_more.getVisibility() == 0) {
				ll_more.setVisibility(View.GONE);
			} else {
				ll_more.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ib_photo:
			Intent intent_photo = new Intent();
			// MyTools tools = new MyTools();
			// photoName = tools.createFileName();
			// Uri imageUri = Uri.fromFile(new File(photoPath, photoName));
			// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
			// intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			intent_photo.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent_photo, 1);
			break;
		case R.id.ib_video:
			Intent intent_video = new Intent();
			intent_video.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(intent_video, 2);
			break;
		case R.id.ib_file:
			Intent intent_file = new Intent();
			intent_file.setClass(this, FileSelection.class);
			startActivityForResult(intent_file, 0);
			ll_more.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选择了文件发送
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				final ArrayList<String> fileNameArray = data
						.getStringArrayListExtra("fileName");
				final ArrayList<String> filePathArray = data
						.getStringArrayListExtra("safeFileName");
				new FileSendThread(fileNameArray, filePathArray, handler, ip)
						.start();
			}
			break;
		case 1:
			if (resultCode == RESULT_OK) {
				// int SCALE = 2;
				MyTools tools = new MyTools();
				// Bitmap bitmap = BitmapFactory.decodeFile(photoPath
				// + "/photoName");
				// Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
				// bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// bitmap.recycle();
				Bitmap bm_camera = (Bitmap) data.getExtras().get("data");
				String photoName = tools.createFileName(".jpg");
				String photoPath = Environment.getExternalStorageDirectory()
						.getPath() + "/MicroChat/Photo/";
				tools.savePhotoToSDCard(photoPath, photoName, bm_camera);

				ArrayList<String> photoNameArray = new ArrayList<String>();
				ArrayList<String> photoPathArray = new ArrayList<String>();
				photoNameArray.add(photoName);
				photoPathArray.add(photoPath + photoName);
				new FileSendThread(photoNameArray, photoPathArray, handler, ip)
						.start();
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				// Uri uri = data.getData();
				// Cursor cursor = this.getContentResolver().query(uri, null,
				// null, null, null);

				// if (cursor.moveToFirst()) {
				// String videoPath = cursor.getString(cursor
				// .getColumnIndex("_data"));
				// vv_01.setVideoURI(Uri.parse(videoPath));
				// vv_01.setMediaController(new MediaController(this));
				// vv_01.start();
				// }
				MyTools tools = new MyTools();
				Context cxt = MyClient.this;
				String videoPath = Environment.getExternalStorageDirectory()
						.getPath() + "/MicroChat/Video/";
				String videoName = tools.createFileName(".3gp");
				tools.saveVideoToSDCard(cxt, videoPath, videoName, data);

				ArrayList<String> videoNameArray = new ArrayList<String>();
				ArrayList<String> videoPathArray = new ArrayList<String>();
				videoNameArray.add(videoName);
				videoPathArray.add(videoPath + videoName);
				new FileSendThread(videoNameArray, videoPathArray, handler, ip)
						.start();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		tv_content.setText(null);
		stopService(intent_service);
		// receiveTextDataThread.runThread(false);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");
		// stopService(intent_service);
		// receiveTextDataThread.runThread(false);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		super.onResume();
	}
}
