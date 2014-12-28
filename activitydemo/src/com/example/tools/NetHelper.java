package com.example.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

public class NetHelper {

	public static final String url =  "http://14.17.77.81:820";
	public static boolean debug = true;
	private static final int timeoutConnection = 20000;   
	private static final int timeoutSocket = 20000;
	private static HttpClient httpclient;
	
	public void requestFDailyUrl(Context myContext, final IRequestListener listener) {
		// TODO Auto-generated method stub
		final String registerString ="http://14.17.77.81:820/Comminicate/GetInfo.ashx";
		new Thread(){
			public void run() {
                try {
                	String result = request(registerString);//通过网络下载数据
                	listener.onComplete(result);
                } catch (Exception e) {
                	listener.onError(e);
                }
            }
			
		}.start();
	}	
	
	private static String encodeUrl(Parameters parameters) {
		if (parameters == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int loc = 0; loc < parameters.size(); loc++) {
			if (first)
				first = false;
			else
				sb.append("&");
			sb.append((parameters.getKey(loc)) + "="
					+ (parameters.getValue(loc)));
		}
		return sb.toString();
	}
	public String request(Context context, String url, Parameters params,
			String method) {
		String result = "";
		Log.d("ylg", "url = " + url);
		InputStream is = null;

		if (method.equals("GET")) {

		}
		try {

			StringBuffer finalURL = new StringBuffer(url);
			finalURL.append(encodeUrl(params));
			if (debug) {
				System.out.println(finalURL.toString());
			}
			HttpGet httpRequest = new HttpGet(finalURL.toString());

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			httpclient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpclient.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				is = httpResponse.getEntity().getContent();
		 
			} 

			if (is != null) {
				ByteArrayOutputStream content = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int bytes;
				while ((bytes = is.read(buffer)) != -1) {
					content.write(buffer, 0, bytes);
				}
				result = new String(content.toByteArray());
			}

			return result;
		} catch (Exception e) {
			Log.d("ylg", " e = " + e);
			e.printStackTrace();
			return result;
		}

	}
	
	public static interface IRequestListener {

        public void onComplete(String response);

        public void onError(Exception e);
    }

	
	private static String request(String dailyUrl) {
		// TODO Auto-generated method stub
		 
		// HttpGet对象
		HttpGet httpRequest = new HttpGet(dailyUrl);
		String strResult = "";
		try {
			// HttpClient对象
			HttpClient httpClient = createHttpClient();
			// 获得HttpResponse对象
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//网络返回结果正确
				// 取得返回的数据
				strResult = EntityUtils.toString(httpResponse.getEntity());
			 
			} 

		} catch (ClientProtocolException e) {
			Log.e("Info", "protocol error");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Info","IO error");
			e.printStackTrace();
		}
		catch (Exception e) {
			Log.e("Info","IO error");
			e.printStackTrace();
		}
		return strResult;
	}
	
	// 创建 HttpParams 以用来设置 HTTP 参数 
		public static HttpClient createHttpClient() {

			
			BasicHttpParams httpParams = new BasicHttpParams();
			// http版本设置
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(httpParams, true);

			// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
			HttpConnectionParams
					.setConnectionTimeout(httpParams, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			// HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

			// 设置重定向，缺省为 true

			HttpClientParams.setRedirecting(httpParams, true);

			// 设置 user agent

			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
			HttpProtocolParams.setUserAgent(httpParams, userAgent);

			// 创建一个 HttpClient 实例
			// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
			// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

			// 设置HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			// schReg.register(new
			// Scheme("https",SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					httpParams, schReg);
			// HttpClient httpClient = new DefaultHttpClient(conMgr, httpParams);

			return new DefaultHttpClient(conMgr, httpParams);
		}
		
		
		// 创建 HttpParams 以用来设置 HTTP 参数 
			public static HttpClient createHttpClientShort() {

				
				BasicHttpParams httpParams = new BasicHttpParams();
				// http版本设置
				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
				HttpProtocolParams.setUseExpectContinue(httpParams, true);

				// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
				HttpConnectionParams
						.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 3000);
				// HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

				// 设置重定向，缺省为 true

				HttpClientParams.setRedirecting(httpParams, true);

				// 设置 user agent

				String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
				HttpProtocolParams.setUserAgent(httpParams, userAgent);

				// 创建一个 HttpClient 实例
				// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
				// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

				// 设置HttpClient支持HTTP和HTTPS两种模式
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				// schReg.register(new
				// Scheme("https",SSLSocketFactory.getSocketFactory(), 443));
				ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
						httpParams, schReg);
				// HttpClient httpClient = new DefaultHttpClient(conMgr, httpParams);

				return new DefaultHttpClient(conMgr, httpParams);
			}
			
}
