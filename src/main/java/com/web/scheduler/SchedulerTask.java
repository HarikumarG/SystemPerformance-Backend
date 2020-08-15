package com.web.scheduler;

import com.web.helpers.Singleton;
import com.web.statisticsmodel.StatisticsModel;
import java.net.*;
import java.io.*;

class SchedulerTask extends Thread{

	public void run() {
		try {
			Thread.sleep(3000);
			System.out.println("SchedulerTask is called");
			Socket socket = new Socket("localhost",8081);
			socket.setKeepAlive(true);
			System.out.println("Socket connection established");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
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
			socket.close();
			System.out.println("Socket is closed");
		} catch(SocketException e) {
			System.out.println("Oops! Socket is closed");
		} catch(Exception e) {
			System.out.println("Exception in Telnet class "+e.getMessage());
		}
	}

	private void parseData(String line) {

		String[] stats = line.split("/",0);
		long totalram = Long.parseLong(stats[0]);
		long freeram = Long.parseLong(stats[1]);
		long usedram = Long.parseLong(stats[2]);
		long totalswap = Long.parseLong(stats[3]);
		long freeswap = Long.parseLong(stats[4]);
		long usedswap = Long.parseLong(stats[5]);
		float loadavgpast1 = Float.valueOf(stats[6]).floatValue();
		float loadavgpast5 = Float.valueOf(stats[7]).floatValue();
		float loadavgpast15 = Float.valueOf(stats[8]).floatValue();
		StatisticsModel model = new StatisticsModel(totalram,freeram,usedram,totalswap,freeswap,usedswap,loadavgpast1,loadavgpast5,loadavgpast15);
		Singleton.getWebsocketService().sendDataToAllSubscribers(model);
	}
}