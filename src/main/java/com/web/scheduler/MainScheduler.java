package com.web.scheduler;

import java.util.Timer;
import java.util.TimerTask;

public class MainScheduler {
	
	TimerTask task;
	Timer timer;

	public MainScheduler() {
		task = new SchedulerTask();
		timer = new Timer();
	}
	public void startScheduler() {

		long delay = 2000;
		long period = 10000;

		timer.scheduleAtFixedRate(task,delay,period);
	}
	public void stopScheduler() {
		task.cancel();
		timer.cancel();
	}
}