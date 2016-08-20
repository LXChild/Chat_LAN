package com.demo.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class MyTools {

	public String getSystemTime() {
		String time = null;
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance();
		time = dateFormat.format(date);
		return time;
	}

	public void scroll2Bottom(final ScrollView scroll, final View inner) {
		Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (scroll == null || inner == null) {
					return;
				}
				// 内层高度超过外层
				int offset = inner.getMeasuredHeight()
						- scroll.getMeasuredHeight();
				if (offset < 0) {
					offset = 0;
				}
				scroll.scrollTo(0, offset);
			}
		});
	}

	public void SendMessage(Handler handler, int what, Object obj) {
		if (handler != null) {
			Message.obtain(handler, what, obj).sendToTarget();
		}
	}

	@SuppressLint("SimpleDateFormat")
	public String createFileName(String fileType) {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
		fileName = dateFormat.format(date) + fileType;
		return fileName;
	}

	public void savePhotoToSDCard(String path, String photoName,
			Bitmap photoBitmap) {
		if (!android.os.Environment.getExternalStorageDirectory().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File photoFile = new File(path, photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				photoFile.delete();
				Log.e("MyTools", e.toString());
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void saveVideoToSDCard(Context cxt, String videoPath,
			String videoName, Intent intent) {
		try {
			AssetFileDescriptor videoAsset = cxt.getContentResolver()
					.openAssetFileDescriptor(intent.getData(), "r");
			FileInputStream fis = videoAsset.createInputStream();
			File dir = new File(videoPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File tmpFile = new File(videoPath, videoName);
			FileOutputStream fos = new FileOutputStream(tmpFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) > 0) {
				fos.write(buf, 0, len);
			}
			fis.close();
			fos.close();
		} catch (IOException io_e) {
			// TODO: handle error
			Log.d("saveVideoToSDCard", io_e.toString());
		}
	}
}
