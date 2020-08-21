package com.web.statisticsmodel;

public class StatisticsModel {

	private String timestamp;
	private float totalram;
	private float usedram;
	private int cpuusage;

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public void setTotalram(float totalram) {
		this.totalram = totalram;
	}
	public void setUsedram(float usedram) {
		this.usedram = usedram;
	}
	public void setCpuusage(int cpuusage) {
		this.cpuusage = cpuusage;
	}

	public String getTimestamp() {
		return timestamp;
	}
	public float getTotalram() {
		return totalram;
	}
	public float getUsedram() {
		return usedram;
	}
	public int getCpuusage() {
		return cpuusage;
	}

}