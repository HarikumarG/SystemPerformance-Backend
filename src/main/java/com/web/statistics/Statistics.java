package com.web.statistics;

import com.web.statisticsmodel.StatisticsModel;

public class Statistics {
	static {
		System.loadLibrary("stats");
	}

	private native StatisticsModel getStats();

	public static StatisticsModel getSystemData() {
		StatisticsModel model = new Statistics().getStats();
		return model;
	}
}