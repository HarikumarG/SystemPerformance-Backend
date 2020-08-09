package com.web.statisticscontroller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.statisticsmodel.StatisticsModel;
import com.web.statistics.Statistics;

@WebServlet("/getSystemStats")
public class StatisticsController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		String name = jsonObject.get("name").getAsString();
		System.out.println(name);

		StatisticsModel model = Statistics.getSystemData();
		
		String jsonObj = new Gson().toJson(model);
		response.getWriter().write(jsonObj);
	}
}