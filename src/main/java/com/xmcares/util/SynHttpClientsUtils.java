/**
 * @(#)com.xmcares.scims.util.HttpClients.java Copyright (c) 2014-2018 厦门民航凯亚有限公司
 */
package com.xmcares.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * http工具类
 *
 * @Author zengd
 * @Version 1.0  2016-9-1
 * @Modified zengd  2016-9-1  <创建>
 */
public class SynHttpClientsUtils {
	private static Logger logger = LoggerFactory.getLogger(SynHttpClientsUtils.class);
	public SynHttpClientsUtils() throws Exception {}
	// HttpClient
    public static CloseableHttpAsyncClient  closeableHttpClient = null;
    public static CloseableHttpClient httpClient = null;
    public static void httpsClientInit(String IP, String user, String Password){
        // 设置BasicAuth
        CredentialsProvider provider = new BasicCredentialsProvider();
        // Create the authentication scope
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        // Create credential pair，在此处填写用户名和密码
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, Password);
        // Inject the credentials
        provider.setCredentials(scope, credentials);
        // HttpClient
        closeableHttpClient = HttpAsyncClients.custom().setDefaultCredentialsProvider(provider).build();
	}
	// 同步
    public static void httpsClientInitSyn(String IP, String user, String Password){
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置BasicAuth
        CredentialsProvider provider = new BasicCredentialsProvider();
        // Create the authentication scope
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        // Create credential pair，在此处填写用户名和密码
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, Password);
        // Inject the credentials
        provider.setCredentials(scope, credentials);
        // Set the default credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(provider);
        // HttpClient
        httpClient = httpClientBuilder.build();
    }
    private static String result = "";
    public static String doPost(String url, String inbound, String charset) throws Exception {
    	closeableHttpClient.start();
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
                        result = content;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                  @Override
                public void failed(Exception ex) {
                	  logger.info(postMethod.getRequestLine() + "->" + ex);
                	  logger.info(" callback thread id is : " + Thread.currentThread().getId());
                	  result = ex.toString();
                }
                @Override
                public void cancelled() {
                	logger.info(postMethod.getRequestLine() + " cancelled");
                	logger.info(" callback thread id is : " + Thread.currentThread().getId());
                	result = "cancelled";
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
// 同步
    public static String doPostSyn(String url, String inbound, String charset) throws Exception {

        String result = "";
        HttpResponse httpResponse = null;
        HttpEntity entity = null;
        HttpPost postMethod = new HttpPost(url);
        if(inbound!=null){
            postMethod.setHeader("Content-Type", "application/json;charset=UTF-8");
            HttpEntity inputEntity = new ByteArrayEntity(inbound.getBytes("utf-8"));
            postMethod.setEntity(inputEntity);
        }
        try {
            httpResponse = httpClient.execute(postMethod);
            entity = httpResponse.getEntity();
            if( entity != null ){
                result = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        System.out.println(result);
        return result;
    }
}
