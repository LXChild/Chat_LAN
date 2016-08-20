package com.demo.client;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Handler;

import com.demo.tools.MyTools;

public class FileSendThread extends Thread {

	private ArrayList<String> fileNameArray, filePathArray;
	private Handler handler;
	private MyTools tools;
	private String ip;
	private int port = 8082;

	public FileSendThread(ArrayList<String> fileNameArray,
			ArrayList<String> filePathArray, Handler handler, String ip) {
		// TODO Auto-generated constructor stub
		this.fileNameArray = fileNameArray;
		this.filePathArray = filePathArray;
		this.handler = handler;
		this.ip = ip;
		tools = new MyTools();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
				tools.SendMessage(handler, 0x233, "Sending...\n"
						+ fileNameArray.get(i));

				Socket data = new Socket(ip, port);
				OutputStream outputData = data.getOutputStream();
				FileInputStream fileInput = new FileInputStream(
						filePathArray.get(i));
				int size = -1;
				byte[] buffer = new byte[1024];
				while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
					outputData.write(buffer, 0, size);
				}
				outputData.close();
				fileInput.close();
				data.close();
				tools.SendMessage(handler, 0x233, "SendComplete.\n"
						+ fileNameArray.get(i));
			}
			tools.SendMessage(handler, 0x233, "SendMissionComplete");
		} catch (Exception e) {
			tools.SendMessage(handler, 0x233, "SendError:\n" + e.getMessage());
		}
		super.run();
	}
}
