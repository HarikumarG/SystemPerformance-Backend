package com.web.service;

import java.util.*;

import com.web.helpers.Singleton;

public class AlertService extends Thread {

	private String[] stats;

	public AlertService(String[] stats) {
		this.stats = stats;
	}
	public void run() {
		String uuid = stats[0];
		HashMap<String,String> alertData = Singleton.getMachineDao().getAlertConfig(uuid);
		if(alertData.size() != 0) {
			int maxCpu = Integer.parseInt(alertData.get("MaxCpuUsage"));
			float maxRam = Float.parseFloat(alertData.get("MaxRAMUsage"));
			String systemName = alertData.get("SystemName");
			float usedRam = Float.parseFloat(stats[2]);
			int usedCpu = Integer.parseInt(stats[3]);
			String timestamp = stats[4];

			if(usedCpu > maxCpu && usedRam > maxRam)
				Singleton.getMailer().sendMail(systemName,timestamp,String.valueOf(maxCpu),String.valueOf(usedCpu),String.valueOf(maxRam),String.valueOf(usedRam));
			else if(usedRam > maxRam)
				Singleton.getMailer().sendMail(systemName,timestamp,"","",String.valueOf(maxRam),String.valueOf(usedRam));
			else if(usedCpu > maxCpu)
				Singleton.getMailer().sendMail(systemName,timestamp,String.valueOf(maxCpu),String.valueOf(usedCpu),"","");
			System.out.println("Mail sent to all recipients successfully");			
		}
	}

}