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
import com.web.pojo.MachineDetails;

@WebServlet("/getAllMachine")
public class GetMachineController extends HttpServlet {

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		StringBuffer jb = new StringBuffer();
		String line = null;

		try {
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch(Exception e) {
			System.out.println("Error in reading request body from GetMachineController "+e.getMessage());
		}

		JsonElement jsonElement = new JsonParser().parse(jb.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		String empid = jsonObject.get("EmpID").getAsString();
		String empName = jsonObject.get("EmpName").getAsString();

		ArrayList<MachineDetails> machineList = Singleton.getMachineDao().getAllMachineDetails();

		String jsonObj = new Gson().toJson(machineList);
		response.getWriter().write(jsonObj);
	}
}
