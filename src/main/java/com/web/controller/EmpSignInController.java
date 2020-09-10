package com.web.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;
import java.util.*;

import com.web.helpers.Singleton;
import com.web.pojo.EmployeeSignIn;

@WebServlet("/empSignIn")
public class EmpSignInController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from EmpSignInController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		EmployeeSignIn details = new Gson().fromJson(jsonObject.toString(),EmployeeSignIn.class);

		String empName = Singleton.getEmployeeDao().verifyCredentials(details);

		String resp = (empName != null) ? "SUCCESS" : "FAILURE";

		HashMap<String,String> responseData = new HashMap<String,String>();
		responseData.put("Status",resp);
		responseData.put("EmpName",empName);

		String jsonObj = new Gson().toJson(responseData);
		response.getWriter().write(jsonObj);
	}
}
