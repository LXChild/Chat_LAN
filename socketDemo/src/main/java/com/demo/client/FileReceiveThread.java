package com.demo.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.demo.mediaplayer.VoicePlayerThread;
import com.demo.tools.MyTools;

public class FileReceiveThread extends Thread {
	private static final String TAG = "CLientFileReceiveThread";
	private Handler handler;
	private MyTools tools;
	private ServerSocket server;
	private int port = 8083;

	public FileReceiveThread() {
		// TODO Auto-generated constructor stub
		this.handler = MyClient.handler;
		tools = new MyTools();
	}

	public void CreateDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
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
				// �����ļ���
				if (server == null) {
					server = new ServerSocket();
					server.setReuseAddress(true);
					server.bind(new InetSocketAddress(port));
				}
				Socket name = server.accept();
				InputStream nameStream = name.getInputStream();
				InputStreamReader streamReader = new InputStreamReader(
						nameStream);
				BufferedReader br = new BufferedReader(streamReader);
				String fileName = br.readLine();
				br.close();
				streamReader.close();
				nameStream.close();
				name.close();
				Log.d(TAG, "Receiving:" + fileName);
				tools.SendMessage(handler, 0x233, "Receiving...\n" + fileName);
				// �����ļ����
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
				tools.SendMessage(handler, 0x233, "ReceiveMissionComplete\n"
						+ fileName);

				if (fileName.contains(".amr")) {
					new VoicePlayerThread(savePath + fileName).start();
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
				tools.SendMessage(handler, 0x233,
						"ReceiveError:\n" + e.getMessage());
				break;
			}
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
