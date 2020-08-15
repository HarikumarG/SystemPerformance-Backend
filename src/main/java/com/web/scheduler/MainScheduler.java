package com.web.scheduler;

public class MainScheduler{
	
	SchedulerTask task;

	public MainScheduler() {
		task = new SchedulerTask();	
	}
	public void startScheduler() {
		task.start();
	}
	public void stopScheduler() {
		task.stop();	
	}
}