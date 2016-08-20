package com.demo.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.util.Log;

public class SendTextThread extends Thread {
	private static final String TAG = "SendTextThread";
	private String ip, name, chatMsg, msgContent;
	private int port;
	private Socket socket;

	public SendTextThread(String msgContent) {
		// TODO Auto-generated constructor stub
		this.ip = MyClient.ip;
		this.name = MyClient.name;
		this.msgContent = msgContent;
		this.port = 8080;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket(ip, port);
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			chatMsg = msgContent;
			if (chatMsg.trim().equals("")) {
				return;
			} else {
				bw.write(name + chatMsg + "\n");
				bw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.toString());
			}
		}
		super.run();
	}
}
