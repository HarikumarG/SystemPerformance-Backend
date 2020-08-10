package com.web.scheduler;

import java.util.TimerTask;
import com.web.helpers.Singleton;
import com.web.statistics.Statistics;
import com.web.statisticsmodel.StatisticsModel;

class SchedulerTask extends TimerTask {

	public void run() {
		System.out.println("SchedulerTask is called");
		StatisticsModel model = Statistics.getSystemData();
		Singleton.getWebsocketService().sendDataToAllSubscribers(model);
	}
}