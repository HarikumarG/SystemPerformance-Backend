package com.web.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import com.web.pojo.Statistics;
import java.util.*;

public class StatisticsDao {

	private Connection conn;
	
	public StatisticsDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/system_performance";
			conn = (Connection) DriverManager.getConnection(url,"root","1234");
		} catch(Exception e) {
			System.out.println("Exception in StatisticsDao constructor "+e.getMessage());
		}
	}

	public boolean storeData(String[] stats) {

		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "insert into Performance(UUID,TotalRAM,UsedRAM,CpuUsage,TimeStamp)values('"+stats[0]+"','"+stats[1]+"','"+stats[2]+"','"+stats[3]+"','"+stats[4]+"')";
			int row = stmt.executeUpdate(url);
			boolean check = row > 0 ? true : false;
			return check;
		} catch(Exception e) {
			System.out.println("Exception in StatisticsDao storeData method "+e.getMessage());
			return false;
		}
	}
	public ArrayList<Statistics> getData(String systemName,String fromTimestamp,String toTimestamp) {
		ArrayList<Statistics> datalist = new ArrayList<Statistics>();
		try {
			Statement stmt = (Statement) conn.createStatement();
			//String url = "select * from Performance where TimeStamp >= '"+fromTimestamp+"' and TimeStamp <= '"+toTimestamp+"'";
			String url = "select * from Performance where TimeStamp >= '"+fromTimestamp+"' and TimeStamp <= '"+toTimestamp+"' and BINARY UUID in (select UUID from Machine where BINARY SystemName='"+systemName+"')";
			ResultSet rs = stmt.executeQuery(url);
			while(rs.next() != false) {
				Statistics model = new Statistics();
				model.setSystemname(systemName);
				model.setTotalram(Float.valueOf(rs.getString("TotalRAM")).floatValue());
				model.setUsedram(Float.valueOf(rs.getString("UsedRAM")).floatValue());
				model.setCpuusage(Integer.parseInt(rs.getString("CpuUsage")));
				String datetime = rs.getString("TimeStamp");
				model.setTimestamp(datetime.substring(0,datetime.length()-2));
				datalist.add(model);
			}
			return datalist;
		} catch(Exception e) {
			System.out.println("Exception in StatisticsDao getData method "+e.getMessage());
			return datalist;
		}
	}
}