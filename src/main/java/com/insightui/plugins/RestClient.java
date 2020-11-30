package com.insightui.plugins;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import com.insightui.variables.CommonVariables.HttpMethods;

import net.minidev.json.JSONArray;

//import com.google.gson.JsonObject;
import com.insightui.logger.Logger;

public class RestClient {
	public String requestURL;
	public HttpMethods method;
	public String reqBody;
	public JSONArray jsonArray;
	public JSONObject jsonRequestBody;
	public Map<String, String> reqParam = new HashMap<String, String>();
	public Map<String, String> reqHeader = new HashMap<String, String>();
	public String contentType;

	public static HttpResponse response;
	public static DefaultHttpClient client;

	public RestClient(String requestURL, HttpMethods method, String reqBody, Map<String, String> reqParam,
			Map<String, String> reqHeader, String contentType) {
		this.requestURL = requestURL;
		this.method = method;
		this.reqBody = reqBody;
		this.reqParam = reqParam;
		this.reqHeader = reqHeader;
		this.contentType = contentType;
	}

	public RestClient(String requestURL, HttpMethods method, Map<String, String> reqParam,
			Map<String, String> reqHeader) {
		this.requestURL = requestURL;
		this.method = method;
		this.reqParam = reqParam;
		this.reqHeader = reqHeader;
	}

	public RestClient(String requestURL, HttpMethods method, JSONObject jsonRequestBody, String contentType) {
		this.requestURL = requestURL;
		this.method = method;
		this.jsonRequestBody = jsonRequestBody;
		this.contentType = contentType;
	}

	public RestClient(String requestURL, HttpMethods method, JSONObject jsonRequestBody, Map<String, String> reqParam) {
		this.requestURL = requestURL;
		this.method = method;
		this.jsonRequestBody = jsonRequestBody;
		this.reqParam = reqParam;
		this.reqHeader = reqHeader;
	}

	public RestClient(String requestURL, HttpMethods method, JSONArray jsonArray, String contentType) {
		this.requestURL = requestURL;
		this.method = method;
		this.jsonArray = jsonArray;
		this.contentType = contentType;
	}

	public HttpResponse executeRequest() {

		try {
			client = new DefaultHttpClient();

			if (method == HttpMethods.POST) {
				HttpPost postReq = new HttpPost(requestURL);
				StringEntity entity = new StringEntity(jsonRequestBody.toString());
				entity.setContentType(contentType);
				postReq.setEntity(entity);
				response = client.execute(postReq);
			}
		} catch (Exception ex) {
			Logger.logError("Unable to process the Post request to the resource, " + requestURL
					+ "\nFollowing exception occured.\n" + ex.getMessage());
		}
		return response;
	}

	public HttpResponse executeDynamicRequest() {

		try {
			client = new DefaultHttpClient();

			if (method == HttpMethods.POST) {
				HttpPost postReq = new HttpPost(requestURL);
				StringEntity entity = new StringEntity(jsonRequestBody.toString());
				entity.setContentType(contentType);
				postReq.setEntity(entity);
				response = client.execute(postReq);
			} else if (method == HttpMethods.PUT) {
				HttpPut putReq = new HttpPut(requestURL);
				StringEntity entity = new StringEntity(jsonArray.toString());
				entity.setContentType(contentType);
				putReq.setEntity(entity);
				response = client.execute(putReq);
			}
		} catch (Exception ex) {
			Logger.logError("Unable to process the Post request to the resource, " + requestURL
					+ "\nFollowing exception occured.\n" + ex.getMessage());
		}
		return response;
	}

}
