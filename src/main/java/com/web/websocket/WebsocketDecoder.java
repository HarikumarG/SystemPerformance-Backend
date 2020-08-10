package com.web.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

public class WebsocketDecoder implements Decoder.Text<WebsocketModel> {

	public void init(EndpointConfig config) {}

	public void destroy() {}

	public WebsocketModel decode(String s) throws DecodeException {
		return new Gson().fromJson(s,WebsocketModel.class);
	}

	public boolean willDecode(String s) {
		return (s != null);
	}
}