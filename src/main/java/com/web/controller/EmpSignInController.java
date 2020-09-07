package com.web.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

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

		boolean check = Singleton.getEmployeeDao().verifyCredentials(details);

		String resp = (check == true) ? "SUCCESS" : "FAILURE";

		String jsonObj = new Gson().toJson(resp);
		response.getWriter().write(jsonObj);
	}
}
