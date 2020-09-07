package com.web.alert;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;
import java.util.*;

import com.web.helpers.Singleton;

@WebServlet("/getAllConfig")
public class GetSettingController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from GetSettingController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		String systemName = jsonObject.get("SystemName").getAsString();
		System.out.println("Setting - SystemName "+systemName);

		HashMap<String,HashMap<String,String>> config = Singleton.getMachineDao().getAllConfig(systemName);

		String jsonObj = new Gson().toJson(config);
		response.getWriter().write(jsonObj);
	}
}