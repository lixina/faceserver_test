/**
 * @(#)com.xmcares.scims.util.HttpClients.java Copyright (c) 2014-2018 厦门民航凯亚有限公司
 */
package com.xmcares.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * http工具类
 */
public class SynAuthorizationHttpClientsTest {
	private static Logger logger = LoggerFactory.getLogger(SynAuthorizationHttpClientsTest.class);
	
	// HttpClient
    public static CloseableHttpAsyncClient closeableHttpClient = HttpAsyncClients.createDefault();
    public static String doPost(String url, String inbound) throws Exception {
    	closeableHttpClient.start();
    	String result = "";
        HttpResponse httpResponse = null;
        HttpEntity entity = null;
    	final HttpPost postMethod = new HttpPost(url); 
    	if(inbound!=null){
    		postMethod.setHeader("Content-Type", "application/json;charset=UTF-8");
    		HttpEntity inputEntity = new ByteArrayEntity(inbound.getBytes("utf-8"));
    		postMethod.setEntity(inputEntity);
    	}
    	
    	try {
            closeableHttpClient.execute(postMethod,new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse response) {
                	logger.info(" callback thread id is : " + Thread.currentThread().getId());
                	logger.info(postMethod.getRequestLine() + "->" + response.getStatusLine());
                    try {
                        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                        //logger.info(" response content is : " );
                        logger.info(" response content is : " + content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                  @Override
                public void failed(Exception ex) {
                	  logger.info(postMethod.getRequestLine() + "->" + ex);
                	  logger.info(" callback thread id is : " + Thread.currentThread().getId());
                }
                @Override
                public void cancelled() {
                	logger.info(postMethod.getRequestLine() + " cancelled");
                	logger.info(" callback thread id is : " + Thread.currentThread().getId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
