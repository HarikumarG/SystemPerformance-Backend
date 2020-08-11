package com.web.websocket;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.web.helpers.Singleton;

import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value = "/getStatsWebsocket",encoders = {WebsocketEncoder.class,WebsocketEncoderStats.class},decoders = {WebsocketDecoder.class,WebsocketDecoderStats.class})
public class Websocket {

	@OnOpen
	public void open(Session session) throws IOException,EncodeException {
		System.out.println("One connection is connected");
	}

	@OnMessage
	public void message(WebsocketModel model,Session session) throws IOException,EncodeException {
		Singleton.getWebsocketService().handleMessage(model,session);
	}

	@OnClose
	public void close(Session session) throws IOException,EncodeException {
		Singleton.getWebsocketService().handleClose(session);
	}

	@OnError
	public void error(Session session,Throwable throwable) {
		System.out.println("On error called by "+session.getUserProperties().get("name")+"-"+throwable.getMessage());
	}
}
