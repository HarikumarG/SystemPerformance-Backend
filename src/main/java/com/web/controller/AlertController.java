package com.web.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.helpers.Singleton;

@WebServlet("/alertConfigUpdate")
public class AlertController extends HttpServlet{

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from AlertController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		String empid = jsonObject.get("EmpID").getAsString();
		String password = jsonObject.get("Password").getAsString();
		String systemName = jsonObject.get("SystemName").getAsString();
		String alertEnable = jsonObject.get("Notify").getAsString();
		String ramUsage = jsonObject.get("MaxRAMUsage").getAsString();
		String cpuUsage = jsonObject.get("MaxCPUUsage").getAsString();

		boolean userVerify = Singleton.getEmployeeDao().verifySettingsAccess(empid,password);
		String resp = "Alert Update Failed";
		if(userVerify) {
			boolean check = Singleton.getMachineDao().updateAlertConfig(systemName,ramUsage,cpuUsage,alertEnable);
			resp = (check == true) ? "Alert Updated" : "Alert Update Failed";
		}
		String jsonObj = new Gson().toJson(resp);
		response.getWriter().write(jsonObj);
	}
}