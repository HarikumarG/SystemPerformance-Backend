package com.web.statisticscontroller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.helpers.Singleton;
import com.web.statisticsmodel.StatisticsModel;
import java.util.*;

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
			System.out.println("Error in reading request body from StatisticsController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		String fromTimestamp = jsonObject.get("fromTimestamp").getAsString();
		String toTimestamp = jsonObject.get("toTimestamp").getAsString();
		
		ArrayList<StatisticsModel> list = Singleton.getStatisticsDao().getData(fromTimestamp,toTimestamp);
				
		String jsonObj = new Gson().toJson(list);
		System.out.println(jsonObj);
		response.getWriter().write(jsonObj);
	}
}