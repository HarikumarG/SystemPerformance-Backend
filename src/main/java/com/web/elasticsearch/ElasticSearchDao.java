package com.web.elasticsearch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;
import org.apache.http.HttpHost;

public class ElasticSearchDao {

	private RestHighLevelClient client;

	public ElasticSearchDao() {
		client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost",9200,"http")));
	}

	private String getRandomId() {
		Random rand = new Random();
		int val = rand.nextInt(10000);
		return String.valueOf(val);
	}
	public boolean storeData(String stats[]) {
		try {
			//Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stats[4]);
			HashMap<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("UUID",stats[0]);
			dataMap.put("TotalRAM",stats[1]);
			dataMap.put("UsedRAM",stats[2]);
			dataMap.put("CpuUsage",stats[3]);
			dataMap.put("TimeStamp",stats[4]);
			IndexRequest indexRequest = new IndexRequest("system_performance");
			indexRequest.id(getRandomId()); 
			indexRequest.source(dataMap);
			IndexResponse response = client.index(indexRequest,RequestOptions.DEFAULT);
			return true;
		} catch (ElasticsearchException e) {
			System.out.println("Exception in ElasticSearchDao storeData method");
			e.getDetailedMessage();
			return false;
		} catch (Exception e) {
			System.out.println("Exception in ElasticSearchDao storeData method");
			e.printStackTrace();
			return false;
		}
	}
}