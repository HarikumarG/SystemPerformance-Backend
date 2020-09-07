package com.web.service;


public class ConfigService {
	public static String updateConfig(String conf,String notify,int id) {
		String updated = "";
		int config = Integer.parseInt(conf);
		int pow = (int) Math.pow(2,id);
		if(notify.equals("TRUE")) {
			int and = config & pow;
			if(and == 0) {
				int val = config | pow;
				updated = Integer.toString(val);
			} else if(and == 1) {
				updated = conf;
			}
		} else if(notify.equals("FALSE")){
			int and = config & pow;
			if(and == 1) {
				int val = config ^ pow;
				updated = Integer.toString(val);
			} else if(and == 0){
				updated = conf;
			}
		}
		return updated;
	}

	public static boolean getToggleValue(String conf,int id) {
		int config = Integer.parseInt(conf);
		int pow = (int) Math.pow(2,id);
		int and = config & pow;
		if(and == 1) 
			return true;
		return false;
	}
}