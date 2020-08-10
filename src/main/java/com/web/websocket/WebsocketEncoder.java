package com.web.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class WebsocketEncoder implements Encoder.Text<WebsocketModel> {

	public void init(EndpointConfig config){}

	public void destroy() {}

	public String encode(WebsocketModel model) throws EncodeException {
		return new Gson().toJson(model);
	}
}