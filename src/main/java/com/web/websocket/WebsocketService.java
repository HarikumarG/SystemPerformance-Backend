package com.web.websocket;

import java.util.*;
import javax.websocket.Session;
import javax.websocket.EncodeException;
import java.io.IOException;
import com.google.gson.Gson;

import com.web.statisticsmodel.StatisticsModel;

public class WebsocketService {

	private final HashMap<String,Session> client; 
	private final ArrayList<StatisticsModel> dataStore = new ArrayList<StatisticsModel>();
	public WebsocketService() {
		client = new HashMap<String,Session>(); 
	}

	public void handleMessage(WebsocketModel model,Session session) {
		handler(model,session);
	}

	public void handleClose(Session session) {
		String clientname = (String) session.getUserProperties().get("name");
		if(client.containsKey(clientname)) {
			client.remove(clientname);
		} else {
			System.out.println("No such client "+clientname);
		}
	}

	private void handler(WebsocketModel model,Session session) {
		switch(model.getType()) {
			case "login": {
				handleLogin(model,session);
				sendStoredData(session);
				break;
			}
			default : {
				System.out.println("No such command exist");
				break;
			}
		}
	}

	private void handleLogin(WebsocketModel model,Session session) {
		if(model.getName().equals("null") || client.containsKey(model.getName())) {
			System.out.println("This name already exist: "+model.getName());
		} else {
			session.getUserProperties().put("name",model.getName());
			client.put(model.getName(),session);
			System.out.println("This name is subscribed: "+model.getName());
		}
	}

	private void sendStoredData(Session session) {
		try {
			for(int i = 0; i < dataStore.size(); i++) {
				session.getBasicRemote().sendObject(dataStore.get(i));
			}
			System.out.println("Data Store is sent to "+session.getUserProperties().get("name"));
		} catch(IOException e) {
			System.out.println("Send stored data "+e.getMessage());
		} catch(EncodeException e) {
			System.out.println("Send stored data "+e.getMessage());
		}
	}

	public void storeData(StatisticsModel data) {
		dataStore.add(data);
	}
	public void sendDataToAllSubscribers(StatisticsModel data){
		try {
			String jsonObj = new Gson().toJson(data);
			System.out.println(jsonObj);

			if(client.size() == 0) {
				System.out.println("No client exist");
			} else {
				for(Map.Entry<String,Session> eachclient: client.entrySet()) {
					String clientname = eachclient.getKey();
					Session clientsession = eachclient.getValue();
					clientsession.getBasicRemote().sendObject(data);
					System.out.println("Data is sent to "+clientname);
				}
			}
		} catch(IOException e) {
			System.out.println("Websocket service IOException "+e.getMessage());
		} catch(EncodeException e) {
			System.out.println("Websocket service EncodeException "+e.getMessage());
		}
	}
}