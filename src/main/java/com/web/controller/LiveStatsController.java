package com.web.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.helpers.Singleton;
import com.web.pojo.Statistics;
import java.util.*;
import com.web.service.AlertService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/getLiveStats")
public class LiveStatsController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from LiveStatsController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		String systemName = jsonObject.get("SystemName").getAsString();

		String uuid = Singleton.getMachineDao().getUuid(systemName);

		ArrayList<Statistics> list = new ArrayList<Statistics>();

		if(uuid != null)
		{
			try {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest req = HttpRequest.newBuilder()
            		.uri(URI.create("http://localhost:9000/"+uuid))
                	.build();
                HttpResponse<String> resp = client.send(req,HttpResponse.BodyHandlers.ofString());
				JsonElement jsonEle = new JsonParser().parse(resp.body());
				JsonObject jsonObj = jsonEle.getAsJsonObject();
				String data = jsonObj.get("data").getAsString();
				System.out.println(data);
				String[] stats = data.split("/",0);
				Statistics model = new Statistics();
				model.setSystemname(systemName);
				model.setTimestamp(stats[4]);
				model.setTotalram(Float.valueOf(stats[1]));
				model.setUsedram(Float.valueOf(stats[2]));
				model.setCpuusage(Integer.valueOf(stats[3]));
				list.add(model);
				AlertService mailservice = new AlertService(stats);
				mailservice.start();
			} catch(Exception e) {
				System.out.println("Error in sending request to cpp service from LiveStatsController "+e.getMessage());
			}
		}
		String jsonObj = new Gson().toJson(list);
		response.getWriter().write(jsonObj);
	}
}
