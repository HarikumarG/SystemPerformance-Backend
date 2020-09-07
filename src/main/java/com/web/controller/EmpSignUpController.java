package com.web.controller;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;

import com.web.helpers.Singleton;
import com.web.pojo.EmployeeSignUp;

@WebServlet("/empSignUp")
public class EmpSignUpController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from EmpSignUpController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		EmployeeSignUp details = new Gson().fromJson(jsonObject.toString(),EmployeeSignUp.class);

		boolean check = Singleton.getEmployeeDao().storeCredentials(details);

		String resp = (check == true) ? "SUCCESS" : "FAILURE";

		String jsonObj = new Gson().toJson(resp);
		response.getWriter().write(jsonObj);
	}
}

