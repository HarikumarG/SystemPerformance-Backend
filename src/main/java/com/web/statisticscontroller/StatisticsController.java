package com.web.statisticscontroller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.statistics.Statistics;
import com.web.scheduler.MainScheduler;

@WebServlet("/getStatsHttp")
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

		//StatisticsModel model = Statistics.getSystemData();

		MainScheduler scheduler = new MainScheduler();
		String result = scheduler.getStats(name);
		
		String jsonObj = new Gson().toJson(result);
		System.out.println(jsonObj);
		response.getWriter().write(jsonObj);
	}
}