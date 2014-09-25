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

	public static final String url =  "http://www.rntech.com.cn:820";
	public static boolean debug = true;
	private static final int timeoutConnection = 20000;   
	private static final int timeoutSocket = 20000;
	private static HttpClient httpclient;
	
	public void requestFDailyUrl(Context myContext, final IRequestListener listener) {
		// TODO Auto-generated method stub
		final String registerString ="http://www.rntech.com.cn:820/Comminicate/GetInfo.ashx";
		new Thread(){
			public void run() {
                try {
                	String result = request(registerString);//ͨ��������������
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
		 
		// HttpGet����
		HttpGet httpRequest = new HttpGet(dailyUrl);
		String strResult = "";
		try {
			// HttpClient����
			HttpClient httpClient = createHttpClient();
			// ���HttpResponse����
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//���緵�ؽ����ȷ
				// ȡ�÷��ص�����
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
	
	// ���� HttpParams ���������� HTTP ���� 
		public static HttpClient createHttpClient() {

			
			BasicHttpParams httpParams = new BasicHttpParams();
			// http�汾����
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(httpParams, true);

			// �������ӳ�ʱ�� Socket ��ʱ���Լ� Socket �����С
			HttpConnectionParams
					.setConnectionTimeout(httpParams, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			// HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

			// �����ض���ȱʡΪ true

			HttpClientParams.setRedirecting(httpParams, true);

			// ���� user agent

			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
			HttpProtocolParams.setUserAgent(httpParams, userAgent);

			// ����һ�� HttpClient ʵ��
			// ע�� HttpClient httpClient = new HttpClient(); ��Commons HttpClient
			// �е��÷����� Android 1.5 ��������Ҫʹ�� Apache ��ȱʡʵ�� DefaultHttpClient

			// ����HttpClient֧��HTTP��HTTPS����ģʽ
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
		
		
		// ���� HttpParams ���������� HTTP ���� 
			public static HttpClient createHttpClientShort() {

				
				BasicHttpParams httpParams = new BasicHttpParams();
				// http�汾����
				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
				HttpProtocolParams.setUseExpectContinue(httpParams, true);

				// �������ӳ�ʱ�� Socket ��ʱ���Լ� Socket �����С
				HttpConnectionParams
						.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 3000);
				// HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

				// �����ض���ȱʡΪ true

				HttpClientParams.setRedirecting(httpParams, true);

				// ���� user agent

				String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
				HttpProtocolParams.setUserAgent(httpParams, userAgent);

				// ����һ�� HttpClient ʵ��
				// ע�� HttpClient httpClient = new HttpClient(); ��Commons HttpClient
				// �е��÷����� Android 1.5 ��������Ҫʹ�� Apache ��ȱʡʵ�� DefaultHttpClient

				// ����HttpClient֧��HTTP��HTTPS����ģʽ
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