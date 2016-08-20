package com.demo.appstart;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.server.MyServer;
import com.demo.userdb.DataBase;
import com.example.socketdemo.R;

public class LogInActivity extends Activity implements OnClickListener {

	private String TAG = "LogInActivity";
	private EditText et_username, et_password;
    private String PREFS_NAME = "UserData";
	private String username, password;
	private SharedPreferences preferences;
	private DataBase userdata;
	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_login);
		initView();
		initData();
		playMusic();
	}

	public void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_register = (Button) findViewById(R.id.btn_register);

		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			readData();
			saveUserData();
			break;
		case R.id.btn_register:
			writeData();
			break;
		default:
			break;
		}
	}

	private void initData() {
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		et_username.setText(getUserName());
		et_password.setText(getPassword());
		mp = MediaPlayer.create(this, R.raw.background_music);
	}

	private void saveUserData() {
		SharedPreferences.Editor editor = preferences.edit();
		username = et_username.getText().toString();
		password = et_password.getText().toString();
		editor.putString("username", username);
		editor.putString("password", password);
		editor.apply();
	}

	private String getUserName() {
        return preferences.getString("username", "");
	}

	private String getPassword() {
        return preferences.getString("password", "");
	}

	private void readData() {
		username = et_username.getText().toString();
		password = et_password.getText().toString();
		if (judge()) {
			userdata = new DataBase(this);
			SQLiteDatabase dbRead = userdata.getReadableDatabase();
			Cursor c = dbRead.query("UserData", null, null, null, null, null,
					null);
			while (c.moveToNext()) {
				String db_username = c.getString(c.getColumnIndex("username"));
				String db_password = c.getString(c.getColumnIndex("password"));
				if (username.equals(db_username)
						&& password.equals(db_password)) {
					Intent intent = new Intent();
					intent.setClass(this, MyServer.class);
					Bundle bundle = new Bundle();
					bundle.putString("username", username);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
					return;
				}
			}
			Toast.makeText(this, "UserName or password is wrong!",
					Toast.LENGTH_SHORT).show();
        }
	}

	private void writeData() {
		username = et_username.getText().toString();
		password = et_password.getText().toString();

		if (judge()) {
			userdata = new DataBase(this);

			SQLiteDatabase dbwrite = userdata.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("username", username);
			cv.put("password", password);
			dbwrite.insert("UserData", null, cv);
			dbwrite.close();

			Toast.makeText(this, "Register successfully!", Toast.LENGTH_SHORT)
					.show();
		}
    }

	private boolean judge() {
		username = et_username.getText().toString();
		password = et_password.getText().toString();
		if (username.trim().equals("")) {
			et_username.setError("username can't be null");
			return false;
		} else if (password.trim().equals("")) {
			et_password.setError("password can't be null");
			return false;
		} else {
			return true;
		}
	}

	private void playMusic() {
		mp.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		try {

			if (mp.isPlaying()) {
				mp.stop();
				mp.release();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(TAG, e.toString());
		}
		super.onPause();
	}
}
