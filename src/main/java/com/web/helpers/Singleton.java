package com.web.helpers;

import com.web.websocket.WebsocketService;
import com.web.scheduler.SchedulerTask;
public class Singleton {

	private static WebsocketService instance = new WebsocketService();
	private static SchedulerTask task = new SchedulerTask();

	public static WebsocketService getWebsocketService() {
		return instance;
	}

	public static SchedulerTask getSchedulerTask() {
		return task;
	}
}