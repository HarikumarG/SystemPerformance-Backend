package com.web.statisticsdao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import com.web.statisticsmodel.StatisticsModel;
import java.util.*;

public class StatisticsDao {

	private static Connection conn;

	public void closeConnection() {
		try {
			conn.close();
		} catch(Exception e) {
			System.out.println("Exception in closing DB connection "+e.getMessage());
		}
	}
	public StatisticsDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/systemperformance";
			conn = (Connection) DriverManager.getConnection(url,"root","");
		} catch(Exception e) {
			System.out.println("Exception in StatisticsDao constructor "+e.getMessage());
		}
	}

	public boolean storeData(String[] stats) {

		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "insert into performance(timestamp,totalRAM,usedRAM,CpuUsage)values('"+stats[0]+"','"+stats[1]+"','"+stats[2]+"','"+stats[3]+"')";
			int row = stmt.executeUpdate(url);
			boolean check = row > 0 ? true : false;
			return check;
		} catch(Exception e) {
			System.out.println("Exception in StatisticsDao storeData method "+e.getMessage());
			return false;
		}
	}

	public ArrayList<StatisticsModel> getData(String fromTimestamp,String toTimestamp) {
		ArrayList<StatisticsModel> datalist = new ArrayList<StatisticsModel>();
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select * from performance where timestamp >= '"+fromTimestamp+"' and timestamp <= '"+toTimestamp+"'";
			ResultSet rs = stmt.executeQuery(url);
			while(rs.next() != false) {
				StatisticsModel model = new StatisticsModel();
				String datetime = rs.getString("timestamp");
				model.setTimestamp(datetime.substring(0,datetime.length()-2));
				model.setTotalram(Float.valueOf(rs.getString("totalRAM")).floatValue());
				model.setUsedram(Float.valueOf(rs.getString("usedRAM")).floatValue());
				model.setCpuusage(Integer.parseInt(rs.getString("CpuUsage")));
				datalist.add(model);
			}
			return datalist;
		} catch(Exception e) {
			System.out.println("Exception in StatisticsDao getData method "+e.getMessage());
			return datalist;
		}
	}
}