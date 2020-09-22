package com.web.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import com.web.pojo.EmployeeSignIn;
import com.web.pojo.EmployeeSignUp;

public class EmployeeDao {

	private Connection conn;

	public EmployeeDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/system_performance";
			conn = (Connection) DriverManager.getConnection(url,"root","1234");
		} catch(Exception e) {
			System.out.println("Exception in EmployeeDao constructor "+e.getMessage());
		}
	}

	public String verifyCredentials(EmployeeSignIn details) {
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select * from Employees where BINARY EmpID='"+details.getEmpid()+"'and BINARY Password='"+details.getPassword()+"'";
			ResultSet rs = stmt.executeQuery(url);
			if(rs.next()) {
				return rs.getString("Name");
			}
			return null;
		} catch(Exception e) {
			System.out.println("Exception in EmployeeDao verifyCredentials method "+e.getMessage());
			return null;
		}
	}

	public boolean storeCredentials(EmployeeSignUp details) {
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "insert into Employees(EmpID,Password,Name,Designation,Place)values('"+details.getEmpid()+"','"+details.getPassword()+"','"+details.getName()+"','"+details.getDesignation()+"','"+details.getPlace()+"')";
			int rowNum = stmt.executeUpdate(url);
			if(rowNum > 0) {
				return true;
			}
			return false;
		} catch(Exception e) {
			System.out.println("Exception in EmployeeDao storeCredentials method "+e.getMessage());
			return false;
		}
	}

	public boolean verifySettingsAccess(String empId,String password) {
		try {
			Statement stmt = (Statement) conn.createStatement();
			String url = "select * from Employees where BINARY EmpID='"+empId+"'and BINARY Password='"+password+"' and BINARY Designation='Team Head'";
			ResultSet rs = stmt.executeQuery(url);
			if(rs.next()) {
				return true;
			}
			return false;
		} catch(Exception e) {
			System.out.println("Exception in EmployeeDao verifySettingsAccess method "+e.getMessage());
			return false;
		}
	}
}
