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
import com.web.pojo.Statistics;

import com.web.helpers.DateTimeConversion;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import com.fasterxml.jackson.databind.ObjectMapper;


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
	public boolean storeData(String stats[],String systemName) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stats[4]);
			HashMap<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("systemname",systemName);
			dataMap.put("totalram",stats[1]);
			dataMap.put("usedram",stats[2]);
			dataMap.put("cpuusage",stats[3]);
			dataMap.put("timestamp",date);
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

	public ArrayList<Statistics> getData(String systemName,String fromTimestamp,String toTimestamp) {
		ArrayList<Statistics> datalist = new ArrayList<Statistics>();
		try {
			SearchRequest searchRequest = new SearchRequest("system_performance");
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

			String from = DateTimeConversion.toUTC(fromTimestamp);
			String to = DateTimeConversion.toUTC(toTimestamp);
			boolQueryBuilder.must(QueryBuilders.rangeQuery("timestamp").gte(from).lte(to));
			boolQueryBuilder.must(QueryBuilders.termQuery("systemname.keyword",systemName));

			sourceBuilder.query(boolQueryBuilder);
			sourceBuilder.from(0); 
			sourceBuilder.size(1000);
			searchRequest.source(sourceBuilder);
			
			SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			for(SearchHit hit : searchResponse.getHits().getHits()) {
				String source = hit.getSourceAsString();
				Statistics stat = objectMapper.readValue(source,Statistics.class);
				String utc = stat.getTimestamp();
				String local = DateTimeConversion.toLocal(utc);
				stat.setTimestamp(local);
				datalist.add(stat);
			}
			return datalist;
		} catch (Exception e) {
			System.out.println("Exception in ElasticSearchDao getData method");	
	 		e.printStackTrace();
	 		return datalist;
		}
	}
}