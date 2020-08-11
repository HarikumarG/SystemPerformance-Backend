package com.web.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import com.web.statisticsmodel.StatisticsModel;

public class WebsocketEncoderStats implements Encoder.Text<StatisticsModel>{

	public void init(EndpointConfig config){}

	public void destroy() {}

	public String encode(StatisticsModel model) throws EncodeException {
		return new Gson().toJson(model);
	}
}