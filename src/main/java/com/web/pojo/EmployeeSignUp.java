package com.web.pojo;

public class EmployeeSignUp {

	private String empid;
	private String password;
	private String name;
	private String designation;
	private String place;

	public void setEmpid(String empid) {
		this.empid = empid;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	public String getEmpid() {
		return empid;
	}
	public String getPassword() {
		return password;
	}
	public String getName() {
		return name;
	}
	public String getDesignation() {
		return designation;
	}
	public String getPlace() {
		return place;
	}
}