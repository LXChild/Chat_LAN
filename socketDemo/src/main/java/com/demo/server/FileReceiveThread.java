package com.demo.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

public class FileReceiveThread extends Thread {
	private static final String TAG = "ServerFileReceiveThread";
	private ServerSocket server;
	private int port = 8082;
	private ArrayList<String> fileNameArray;

	public FileReceiveThread() {
		// TODO Auto-generated constructor stub
		fileNameArray = null;
		fileNameArray = new ArrayList<String>();
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, e.toString());
			e.printStackTrace();
		}
	}

	public void CreateDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
				Log.d(TAG, "CreateFilePath");
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				// 接收文件名
				Socket name = server.accept();
				InputStream nameStream = name.getInputStream();
				InputStreamReader streamReader = new InputStreamReader(
						nameStream);
				BufferedReader br = new BufferedReader(streamReader);
				String fileName = br.readLine();
				fileNameArray.add(fileName);
				br.close();
				streamReader.close();
				nameStream.close();
				name.close();
				Log.d(TAG, "Receiving:" + fileName);
				// 接收文件数据
				Socket data = server.accept();
				InputStream dataStream = data.getInputStream();
				String savePath = Environment.getExternalStorageDirectory()
						.getPath() + "/MicroChat/ReceivedFile/";
				CreateDirectory(savePath);
				FileOutputStream file = new FileOutputStream(savePath
						+ fileName, false);
				byte[] buffer = new byte[1024];
				int size = -1;
				while ((size = dataStream.read(buffer)) != -1) {
					file.write(buffer, 0, size);
				}
				file.close();
				dataStream.close();
				data.close();
				Log.d(TAG, fileName + "ReceiveMissionComplete");
			} catch (Exception e) {
				Log.d(TAG, e.toString());
				Log.d(TAG, "ReceiveError:" + e.getMessage());
				break;
			}
			ArrayList<String> clientIpArray = TextTransmitThread
					.getClientIpArray();
			new FileTransmitThread(clientIpArray, fileNameArray).start();
		}

		closeSocket();
		super.run();
	}

	private void closeSocket() {
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
