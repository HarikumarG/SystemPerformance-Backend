package com.web.statisticsmodel;

public class StatisticsModel {

	private long uptime;
	private long totalram;
	private long freeram;
	private long usedram;
	private long totalswap;
	private long freeswap;
	private long usedswap;
	private float loadavgpast1;
	private float loadavgpast5;
	private float loadavgpast15;
	private String type;

	public StatisticsModel(long uptime,long totalram,long freeram,long usedram,long totalswap,long freeswap,long usedswap,float loadavgpast1,float loadavgpast5,float loadavgpast15,String type) {

		this.uptime = uptime;
		this.totalram = totalram;
		this.freeram = freeram;
		this.usedram = usedram;
		this.totalswap = totalswap;
		this.freeswap = freeswap;
		this.usedswap = usedswap;
		this.loadavgpast1 = loadavgpast1;
		this.loadavgpast5 = loadavgpast5;
		this.loadavgpast15 = loadavgpast15;
		this.type = type;
	}
}