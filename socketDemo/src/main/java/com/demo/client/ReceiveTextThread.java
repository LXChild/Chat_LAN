package com.demo.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.demo.networkinfo.MyNetworkInfo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReceiveTextThread extends Thread {
	private static final String TAG = "ReceiveTextThread";
	private Handler handler;
	private String serverIp, clientIp, name;
	private int port = 8080;
	private String chatMsg;
	private BufferedWriter bw;
	private BufferedReader br;
	private MyNetworkInfo networkInfo;
	public static Socket socket;

	public boolean flag;

	public ReceiveTextThread() {
		// TODO Auto-generated constructor stub
		this.handler = MyClient.handler;
		this.serverIp = MyClient.ip;
		this.name = MyClient.name;
		flag = true;

		networkInfo = new MyNetworkInfo();
		clientIp = networkInfo.getLocalHostIp();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket(serverIp, port);
			sendConnectMsg();
			receiveMsg();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG + "1", e.toString());
			e.printStackTrace();
		}
		super.run();
	}

	public void runThread(boolean flag) {
		this.flag = flag;
	}

	private void sendConnectMsg() {
		try {
			if (socket.isConnected()) {
				chatMsg = "I've connected to Server.";
				bw = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				bw.write(name + chatMsg + "\n");
				bw.flush();
				if (clientIp != null) {
					chatMsg = clientIp;
					bw = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					bw.write(chatMsg + "\n");
					bw.flush();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG + "2", e.toString());
		}
	}

	private void receiveMsg() {
		try {
			while (flag == true && !(chatMsg = readFromServer()).equals(null)) {
				Message message = new Message();
				message.what = 0x234;
				message.obj = chatMsg;
				handler.sendMessage(message);
				Log.d(TAG, "1");
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG + "4", e.toString());
		} finally {
			try {
				bw.close();
				br.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG + "5", e.toString());
			}
		}
	}

	private String readFromServer() {
		try {
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			return br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		return null;
	}
}
