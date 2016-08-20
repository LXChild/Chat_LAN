package com.demo.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;

public class AcceptConnectThread extends Thread {

	private static final String TAG = "ServerAcceptConnectThread";
	private ServerSocket mServerSocket;
	public static Socket mSocket;
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	int port = 8080;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			mServerSocket = new ServerSocket(port);
			mServerSocket.setReuseAddress(true);
			while (!mServerSocket.isClosed()) {
				mSocket = mServerSocket.accept();
				socketList.add(mSocket);
				Log.d(TAG, "There is a Client connected to the Server");
				new TextTransmitThread(socketList).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		} finally {
			closeSocket();
		}
		super.run();
	}

	public void closeSocket() {
		try {

			if (!mServerSocket.isClosed()) {
				mServerSocket.close();
				Log.d(TAG, "ServerSocket is closed");
			}
			for (Socket socket : socketList) {
				if (!socket.isClosed()) {
					socket.close();
					Log.d(TAG, "Socket is closed");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
}
