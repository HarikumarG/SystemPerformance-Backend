package com.web.scheduler;

import com.web.helpers.Singleton;
import com.web.statisticsmodel.StatisticsModel;
import java.net.*;
import java.io.*;

public class SchedulerTask extends Thread{

	private static Socket sock;
	private static BufferedReader r;
	public void run() {
		try {
			Thread.sleep(3000);
			System.out.println("SchedulerTask is called");
			ServerSocket serversocket = new ServerSocket(8081);
			Socket socket = serversocket.accept();
			System.out.println("Socket connection established");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.sock = socket;
			this.r = reader;
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
		long uptime = Long.parseLong(stats[0]);
		long totalram = Long.parseLong(stats[1]);
		long freeram = Long.parseLong(stats[2]);
		long usedram = Long.parseLong(stats[3]);
		long totalswap = Long.parseLong(stats[4]);
		long freeswap = Long.parseLong(stats[5]);
		long usedswap = Long.parseLong(stats[6]);
		float loadavgpast1 = Float.valueOf(stats[7]).floatValue();
		float loadavgpast5 = Float.valueOf(stats[8]).floatValue();
		float loadavgpast15 = Float.valueOf(stats[9]).floatValue();
		String type = stats[10];
		StatisticsModel model = new StatisticsModel(uptime,totalram,freeram,usedram,totalswap,freeswap,usedswap,loadavgpast1,loadavgpast5,loadavgpast15,type);
		Singleton.getWebsocketService().storeData(model);
		if(type.equals("socket")) {
			Singleton.getWebsocketService().sendDataToAllSubscribers(model);
		} else {
			Singleton.getWebsocketService().sendDataToSubscriber(model,type);
		}
	}

	public String getDataHttp(String name) {
		try {
			PrintWriter writer = new PrintWriter(sock.getOutputStream(),true);
			writer.println(name);
			return "SUCCESS";
		} catch(Exception e) {
			System.out.println("GetDataHTTP error "+e.getMessage());
		}
		return null;
	}
}