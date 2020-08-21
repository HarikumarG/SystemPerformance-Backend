package com.web.helpers;

import com.web.statisticsdao.StatisticsDao;
import com.web.scheduler.SchedulerTask;

public class Singleton {

	private static SchedulerTask taskObj = new SchedulerTask();
	private static StatisticsDao daoObj = new StatisticsDao();

	public static StatisticsDao getStatisticsDao() {
		return daoObj;
	}
	public static SchedulerTask getSchedulerTask() {
		return taskObj;
	}
}