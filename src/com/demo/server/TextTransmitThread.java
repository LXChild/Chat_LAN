package com.demo.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.demo.networkinfo.MyNetworkInfo;

import android.util.Log;

public class TextTransmitThread extends Thread {

	private String msg = null;
	private BufferedReader br;
	private BufferedWriter bw;
	private Socket socket = null;
	public static String TAG = "ServerTransmitTextThread";
	public ArrayList<Socket> socketList;
	private static ArrayList<String> clientIpArray;
	private MyNetworkInfo networkInfo;

	public TextTransmitThread(ArrayList<Socket> socketList) {
		this.socket = AcceptConnectThread.mSocket;
		this.socketList = new ArrayList<Socket>();
		this.socketList = socketList;
		networkInfo = new MyNetworkInfo();
		clientIpArray = new ArrayList<String>();
	}

	public static ArrayList<String> getClientIpArray() {
		return clientIpArray;
	}

	@Override
	public void run() {
		try {

			while (!(msg = readFromClient()).equals(null)) {
				// int i = msg.indexOf(":");
				// String userName = msg.substring(0, i);
				Log.d(TAG, msg);
				if (!msg.contains(":")) {
					getClientIp();
				} else {
					transmitMessage();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		closeSocket();
		super.run();
	}

	private void getClientIp() {
		if (!msg.equals(networkInfo.getLocalHostIp())) {
			clientIpArray.add(msg);
			Log.d(TAG, "ReceivedClientIp:" + msg);
		}
	}

	private void transmitMessage() {
		for (Socket s : socketList) {
			if (s != null && !s.isClosed()) {
				try {
					Log.d(TAG, s.toString());
					bw = new BufferedWriter(new OutputStreamWriter(
							s.getOutputStream()));

					bw.write(msg + "\n");
					bw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d(TAG, e.toString());
				}
			}
		}
	}

	private String readFromClient() {
		try {
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			return br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			socketList.remove(socket);
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
		return null;
	}

	private void closeSocket() {
		try {
			if (!socket.isClosed()) {
				br.close();
				bw.close();
				socket.close();
				socketList.remove(socket);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
	}
}
