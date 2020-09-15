package com.web.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.util.*;

import com.web.pojo.EmployeeSignIn;
import com.web.pojo.EmployeeSignUp;
import com.web.pojo.MachineDetails;

import com.web.service.ConfigService;

public class MachineDao {

	private Connection conn;

	public MachineDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/system_performance";
			conn = (Connection) DriverManager.getConnection(url,"hari","1234");
		} catch(Exception e) {
			System.out.println("Exception in MachineDao constructor "+e.getMessage());
		}
	}

	public ArrayList<MachineDetails> getAllMachineDetails() {
		ArrayList<MachineDetails> allDetails = new ArrayList<MachineDetails>();
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select * from Machine";
			ResultSet rs = stmt.executeQuery(url);
			while(rs.next()) {
				MachineDetails details = new MachineDetails(); 
				details.setSystemname(rs.getString("SystemName"));
				details.setUuid(rs.getString("UUID"));
				allDetails.add(details);
			}
			return allDetails;
		} catch (Exception e) {
			System.out.println("Exception in MachineDao getAllMachineDetails method "+e.getMessage());
			return allDetails;
		}
	}

	public boolean storeMachineCredentials(String systemName,String uuid) {
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "insert into Machine(SystemName,UUID,Config)values('"+systemName+"','"+uuid+"','0')";
			int rowNum = stmt.executeUpdate(url);
			if(rowNum > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Exception in MachineDao storeMachineCredentials method "+e.getMessage());
			return false;
		}
	}

	public boolean updateAlertConfig(String systemName,String ramUsage,String cpuUsage,String notify) {
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select Config from Machine where BINARY SystemName='"+systemName+"'";
			ResultSet rs = stmt.executeQuery(url);
			if(rs.next()) {
				String config = ConfigService.updateConfig(rs.getString("Config"),notify,0);
				String u = "update Machine set Config='"+config+"',MaxCpuUsage='"+cpuUsage+"',MaxUsedRAM='"+ramUsage+"' where BINARY SystemName='"+systemName+"'";
				int rowNum = stmt.executeUpdate(u);
				if(rowNum > 0)
					return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Exception in MachineDao updateAlertConfig method "+e.getMessage());
			return false;
		}
	}

	public HashMap<String,HashMap<String,String>> getAllConfig(String systemName) {
		HashMap<String,HashMap<String,String>> config = new HashMap<String,HashMap<String,String>>();
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select * from Machine where BINARY SystemName='"+systemName+"'";
			ResultSet rs = stmt.executeQuery(url);
			if(rs.next()) {
				config.put("Alert",new HashMap<String,String>());
				String notify = String.valueOf(ConfigService.getToggleValue(rs.getString("Config"),0));
				config.get("Alert").put("ToggleNotify",notify);
				config.get("Alert").put("MaxCpuUsage",rs.getString("MaxCpuUsage"));
				config.get("Alert").put("MaxRAMUsage",rs.getString("MaxUsedRAM"));
			}
			return config;
		} catch (Exception e) {
			System.out.println("Exception in MachineDao getAllConfig method "+e.getMessage());
			return config;
		}
	}

	public HashMap<String,String> getAlertConfig(String uuid) {
		HashMap<String,String> alertData = new HashMap<String,String>();
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select * from Machine where BINARY UUID='"+uuid+"'";
			ResultSet rs = stmt.executeQuery(url);
			if(rs.next()) {
				boolean check = ConfigService.getToggleValue(rs.getString("Config"),0);
				if(check) {
					alertData.put("ToggleNotify",String.valueOf(check));
					alertData.put("MaxCpuUsage",rs.getString("MaxCpuUsage"));
					alertData.put("MaxRAMUsage",rs.getString("MaxUsedRAM"));
					alertData.put("SystemName",rs.getString("SystemName"));
				}
			}
			return alertData;
		} catch (Exception e) {
			System.out.println("Exception in MachineDao alertConfig method "+e.getMessage());
			return alertData;
		}
	}
}