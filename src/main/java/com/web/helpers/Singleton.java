package com.web.helpers;

import com.web.websocket.WebsocketService;

public class Singleton {

	private static WebsocketService instance = new WebsocketService();

	public static WebsocketService getWebsocketService() {
		return instance;
	}
}