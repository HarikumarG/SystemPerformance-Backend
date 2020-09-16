package com.web.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.helpers.Singleton;
import com.web.service.AlertService;

@WebServlet("/storeStats")
public class CppstatsController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from CppstatsController "+e.getMessage());
		}
		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String data = jsonObject.get("data").getAsString();
		System.out.println(data);
		String[] stats = data.split("/",0);
		boolean esCheck = Singleton.getElasticSearchDao().storeData(stats) ? true : false;
		if(!esCheck) {
			System.out.println("Data Not stored in ES");
		}
		boolean check = Singleton.getStatisticsDao().storeData(stats) ? true : false;
		if(check) {
			AlertService mailservice = new AlertService(stats);
			mailservice.start();
		}
		if(!check)
			System.out.println("Data Not Stored");
		String jsonObj = new Gson().toJson("SUCCESS");
		response.getWriter().write(jsonObj);
	}
}