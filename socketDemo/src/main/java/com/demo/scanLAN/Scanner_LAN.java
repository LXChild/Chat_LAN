package com.demo.scanLAN;

import java.io.IOException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.demo.networkinfo.MyNetworkInfo;

public class Scanner_LAN extends Thread {

	private Handler handler;
	private static final String TAG = "Scanner_LAN";
	private String locIpPrefix;// 存储本机ip，例：本地ip ：192.168.1.
	private Runtime run = Runtime.getRuntime();// 获取当前运行环境，来执行ping，相当于windows的cmd
	private Process proc = null;
	private String ping = "ping -c 1 -w 0.5 ";// 其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
	private boolean flag = true;
	private int i;// 存放ip最后一位地址 0-255
	private Context ctx;// 上下文

	private MyNetworkInfo networkInfo;
	private String localIp;

	public Scanner_LAN(Context ctx, Handler handler) {
		networkInfo = new MyNetworkInfo();
		localIp = networkInfo.getLocalHostIp();

		this.ctx = ctx;
		this.handler = handler;
	}

	public void SendMessage(int what, String content) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = content;
		handler.sendMessage(msg);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		locIpPrefix = getLocalIpPrefix();// 获取本地ip前缀

		if (locIpPrefix.equals("")) {
			Toast.makeText(ctx, "GetLocalIp failed!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String p, current_ip;
		int result;

		while (flag) {
			p = Scanner_LAN.this.ping + locIpPrefix + Scanner_LAN.this.i;
			current_ip = locIpPrefix + Scanner_LAN.this.i;
			Log.d(TAG, "System is scanning the ip:" + current_ip);

			SendMessage(0x238, current_ip);

			try {
				proc = run.exec(p);
				result = proc.waitFor();

				if (result == 0) {
					if (current_ip.trim().equals(localIp)) {
						Log.d(TAG, "LocalIp has been found:" + current_ip);
						SendMessage(0x235, current_ip);

					} else {
						Log.d(TAG, "Found external server:" + current_ip);
						SendMessage(0x236, current_ip);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			} finally {
				proc.destroy();
			}
			i++;
			if (i == 256) {
				SendMessage(0x237, null);
				break;
			}
			super.run();
		}
	}

	// 获取IP前缀
	public String getLocalIpPrefix() {

		String str = localIp;

		if (!str.equals("")) {
			return str.substring(0, str.lastIndexOf(".") + 1);
		}

		return null;
	}

	public void stopThread(boolean flag) {
		this.flag = flag;
		i = 0;
	}
	// // 获取本机设备名称
	// public String getLocDeviceName() {
	//
	// return android.os.Build.MODEL;
	//
	// }
}