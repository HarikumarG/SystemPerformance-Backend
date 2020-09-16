package com.web.helpers;

import com.web.dao.StatisticsDao;
import com.web.dao.EmployeeDao;
import com.web.dao.MachineDao;
import com.web.service.AlertMailer;

import com.web.elasticsearch.ElasticSearchDao;

public class Singleton {

	private static StatisticsDao statsDaoObj = new StatisticsDao();
	private static EmployeeDao signInDaoObj = new EmployeeDao();
	private static MachineDao machineDaoObj = new MachineDao();
	private static AlertMailer mailerObj = new AlertMailer();

	private static ElasticSearchDao esDaoObj = new ElasticSearchDao();

	public static StatisticsDao getStatisticsDao() {
		return statsDaoObj;
	}
	public static EmployeeDao getEmployeeDao() {
		return signInDaoObj;
	}
	public static MachineDao getMachineDao() {
		return machineDaoObj;
	}
	public static AlertMailer getMailer() {
		return mailerObj;
	}

	public static ElasticSearchDao getElasticSearchDao() {
		return esDaoObj;
	}
}