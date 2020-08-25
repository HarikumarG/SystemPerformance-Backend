package com.web.alert;

import com.web.helpers.Singleton;

public class AlertCheck {

	public void check(String[] stats) {
		if(AlertUtil.sendNotify == true) {
			String timestamp = stats[0];
			String ramUsage = stats[2];
			String cpuUsage = stats[3];
			if(Float.parseFloat(ramUsage) > AlertUtil.RAMUsage && Integer.parseInt(cpuUsage) > AlertUtil.CPUUsage)
				Singleton.getMailer().sendMail(timestamp,cpuUsage,ramUsage);
			else if(Float.parseFloat(ramUsage) > AlertUtil.RAMUsage)
				Singleton.getMailer().sendMail(timestamp,"",ramUsage);
			else if(Integer.parseInt(cpuUsage) > AlertUtil.CPUUsage)
				Singleton.getMailer().sendMail(timestamp,cpuUsage,"");
		}
	}
}