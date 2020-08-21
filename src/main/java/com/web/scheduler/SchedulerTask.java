package com.web.scheduler;

import com.web.helpers.Singleton;
import java.net.*;
import java.io.*;

public class SchedulerTask extends Thread{
	public void run() {
		try {
			Thread.sleep(3000);
			System.out.println("SchedulerTask is called");
			ServerSocket serversocket = new ServerSocket(8081);
			Socket socket = serversocket.accept();
			System.out.println("Socket connection established");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			int c = 0;
			String line = "";
			while((c = reader.read()) != -1) {
				if((char)c == '\n') {
					parseData(line);
					line = "";
				} else {
					line = line + (char)c;
				}
			}
			serversocket.close();
			System.out.println("Socket is closed");
		} catch(SocketException e) {
			System.out.println("Oops! Socket is closed");
		} catch(Exception e) {
			System.out.println("Exception in Telnet class "+e.getMessage());
			e.printStackTrace();
		}
	}

	private void parseData(String line) {
		String[] stats = line.split("/",0);
		boolean check = Singleton.getStatisticsDao().storeData(stats) ? true : false;
		if(!check) {
			System.out.println("Data Not Stored");
		}
	}

	// public String getDataHttp(String name) {
	// 	try {
	// 		PrintWriter writer = new PrintWriter(sock.getOutputStream(),true);
	// 		writer.println(name);
	// 		return "SUCCESS";
	// 	} catch(Exception e) {
	// 		System.out.println("GetDataHTTP error "+e.getMessage());
	// 	}
	// 	return null;
	// }
}