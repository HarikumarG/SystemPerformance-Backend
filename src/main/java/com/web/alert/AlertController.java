package com.web.alert;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

@WebServlet("/alertUpdate")
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

		String notify = jsonObject.get("AlertNotify").getAsString();
		String ramUsage = jsonObject.get("RAMUsage").getAsString();
		String cpuUsage = jsonObject.get("CPUUsage").getAsString();

		AlertUtil.RAMUsage = Float.parseFloat(ramUsage);
		AlertUtil.CPUUsage = Integer.parseInt(cpuUsage);
		AlertUtil.sendNotify = notify.equals("true") ? true : false;

		String jsonObj = new Gson().toJson("Alert Updated");
		response.getWriter().write(jsonObj);
	}
}