package com.web.alert;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;
import java.util.*;

@WebServlet("/getAlertSetting")
public class GetSetting extends HttpServlet {

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

		String name = jsonObject.get("name").getAsString();
		System.out.println("Setting - UserName "+name);

		HashMap<String,String> setting = new HashMap<String,String>();
		setting.put("sendNotify",String.valueOf(AlertUtil.sendNotify));
		setting.put("cpuUsage",String.valueOf((AlertUtil.CPUUsage < 0) ? "" : AlertUtil.CPUUsage));
		setting.put("ramUsage",String.valueOf((AlertUtil.RAMUsage < 0) ? "" : AlertUtil.RAMUsage));

		String jsonObj = new Gson().toJson(setting);
		response.getWriter().write(jsonObj);
		
	}
}