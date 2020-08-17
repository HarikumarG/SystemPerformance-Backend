package com.web.scheduler;

import com.web.helpers.Singleton;
public class MainScheduler{
	
	SchedulerTask task;

	public MainScheduler() {
		task = Singleton.getSchedulerTask();
	}
	public void startScheduler() {
		task.start();
	}
	public void stopScheduler() {
		task.stop();	
	}
	public String getStats(String name) {
		return task.getDataHttp(name);
	}
}