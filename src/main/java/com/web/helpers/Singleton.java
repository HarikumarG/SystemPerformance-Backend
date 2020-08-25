package com.web.helpers;

import com.web.statisticsdao.StatisticsDao;
import com.web.scheduler.SchedulerTask;
import com.web.alert.AlertMailer;

public class Singleton {

	private static SchedulerTask taskObj = new SchedulerTask();
	private static StatisticsDao daoObj = new StatisticsDao();
	private static AlertMailer mailerObj = new AlertMailer();

	public static StatisticsDao getStatisticsDao() {
		return daoObj;
	}
	public static SchedulerTask getSchedulerTask() {
		return taskObj;
	}
	public static AlertMailer getMailer() {
		return mailerObj;
	}
}