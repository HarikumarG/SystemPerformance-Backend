package com.web.pojo;

public class Statistics {

	private String systemname;
	private String timestamp;
	private float totalram;
	private float usedram;
	private int cpuusage;

	public void setSystemname(String systemname) {
		this.systemname = systemname;
	}
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

	public String getSystemname() {
		return systemname;
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