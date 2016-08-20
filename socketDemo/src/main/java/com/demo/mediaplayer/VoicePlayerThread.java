package com.demo.mediaplayer;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class VoicePlayerThread extends Thread {

	private MediaPlayer mp = null;
	private String VoicePath;
	private static final String TAG = "VoicePlayerThread";

	public VoicePlayerThread(String VoicePath) {
		// TODO Auto-generated constructor stub
		mp = new MediaPlayer();
		this.VoicePath = VoicePath;
	}

	public void run() {
		try {
			mp.setDataSource(VoicePath);
			mp.prepare();
			mp.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.stop();
				mp.release();
			}
		});

		mp.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				try {
					mp.release();
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
				return false;
			}
		});
	};
}
