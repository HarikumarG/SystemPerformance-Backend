package com.web.websocket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import com.web.statisticsmodel.StatisticsModel;

public class WebsocketDecoderStats implements Decoder.Text<StatisticsModel> {

	public void init(EndpointConfig config) {}

	public void destroy() {}

	public StatisticsModel decode(String s) throws DecodeException {
		return new Gson().fromJson(s,StatisticsModel.class);
	}

	public boolean willDecode(String s) {
		return (s != null);
	}
}