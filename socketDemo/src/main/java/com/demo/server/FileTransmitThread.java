package com.demo.server;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

public class FileTransmitThread extends Thread {
	private static final String TAG = "SeverFileTransmitThread";
	private ArrayList<String> fileNameArray;
	private String path;
	private int port = 8083;
	private ArrayList<String> ipArray;

	public FileTransmitThread(ArrayList<String> ipArray,
			ArrayList<String> fileNameArray) {
		// TODO Auto-generated constructor stub
		this.ipArray = ipArray;
		this.fileNameArray = fileNameArray;
		path = Environment.getExternalStorageDirectory().getPath()
				+ "/MicroChat/ReceivedFile/";
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (ipArray == null) {
			return;
		}
		for (String ip : ipArray) {
			Log.d(TAG, ip);
			try {
				for (int i = 0; i < fileNameArray.size(); i++) {
					Socket name = new Socket(ip, port);
					OutputStream outputName = name.getOutputStream();
					OutputStreamWriter outputWriter = new OutputStreamWriter(
							outputName);
					BufferedWriter bwName = new BufferedWriter(outputWriter);
					bwName.write(fileNameArray.get(i));
					bwName.close();
					outputWriter.close();
					outputName.close();
					name.close();
					Log.d(TAG, "Sending" + fileNameArray.get(i) + " to : " + ip);

					Socket data = new Socket(ip, port);
					OutputStream outputData = data.getOutputStream();
					FileInputStream fileInput = new FileInputStream(path
							+ fileNameArray.get(i));
					int size = -1;
					byte[] buffer = new byte[1024];
					while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
						outputData.write(buffer, 0, size);
					}
					outputData.close();
					fileInput.close();
					data.close();
					Log.d(TAG, fileNameArray.get(i) + "SendComplete");
				}
				Log.d(TAG, "SendMissionComplete");
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			}
			super.run();
		}
	}
}
