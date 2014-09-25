/**
 * 
 */
package com.example.activitydemo;

import com.example.domain.CommunicationShareData;
import com.example.objserial.CommunicationSerial;
import com.example.tools.CommunicationShareAdapter;

import java.io.IOException;
import java.util.ArrayList;

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
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class secondActivity extends Activity{
	public static final int RESULT_CODE=1;

	private TextView tv=null;

    private Button btnSecondButton;
    private Button btnQuery = null;
	private static final int timeoutConnection = 20000;   
	private static final int timeoutSocket = 20000;
	private Context myContext;
	private UICallback curCallBack;
	private ArrayList<CommunicationShareData> communicationShareData;// ��ͨ����-��ͨ��������
	/** ����ͨ����-�������� */
	public final static short MISSION_COMMUNICATION_SHARE = 11;
	private ListView listview;
	private CommunicationShareAdapter mCommunicationShareAdapter;
	
	public void init(Context _Context, UICallback _UICallback) {
		myContext = _Context;
		curCallBack = _UICallback;

	}
	
	// �ص��ӿ�
	public interface UICallback {

		public void call(short MISSION);

	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

	   super.onCreate(savedInstanceState);
	   
	   

	   setContentView(R.layout.activity_second);

	   listview = (ListView)findViewById(R.id.listView1);
	   //�ӵ�һ��activity���ղ���

	   Intent intent = getIntent();

	   Bundle bundle = intent.getExtras();

	   String str = bundle.getString("str");

	   tv=(TextView)findViewById(R.id.secondTextview);

	   tv.setText(str);//��ʾ���յ�����

	   //���ʱ���ز�������һ��activity

	   btnSecondButton = (Button)findViewById(R.id.secondButton);

	   btnSecondButton.setOnClickListener(listener);
	   
	   btnQuery =  (Button)findViewById(R.id.btnQuery);
	   btnQuery.setOnClickListener(listener); 

	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

//			//TODO Auto-generated method stub
//			Intent intent = new Intent();
//			intent.putExtra("back", "come from second activiy");
//			setResult(RESULT_CODE, intent);
//			finish();
			
			switch(v.getId()){

			case R.id.secondButton:{
				//TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("back", "come from second activiy");
				setResult(RESULT_CODE, intent);
				finish();
			
			}
			break;
			case R.id.btnQuery:{
				BindData();
	
				
//		        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,  
//	                    new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});  
//		        listview.setAdapter(adapter);  
	        
				//String str = request("http://www.rntech.com.cn:820/Comminicate/GetInfo.ashx");
				//Log.i("Info",str);
			
			}
			break;
			default:
				break;
			}			
		}

//		@Override
//		public void onClick(DialogInterface dialog, int which) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent();
//
//			intent.putExtra("back", "come from second activiy");
//
//			setResult(RESULT_CODE, intent);
//
//			finish();
//			
//		}
	};

	private void BindData()
	{		
		try {
			requestFDailyUrl(myContext,new DetailRequestListener());
			
			//Thread.sleep(10000);

		} catch (Exception e) {
			Log.e("Info", e.getMessage());
			e.printStackTrace();
		 
		}finally{
			
		}
	}
	/**
	 * �ص����ݵ�UI�߳�
	 */
	private Handler myHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MISSION_COMMUNICATION_SHARE:
				call(MISSION_COMMUNICATION_SHARE);
				break;
			default:
				break;
			}
		}
	};
	
	
	public void call(short MISSION) {
		mCommunicationShareAdapter = new CommunicationShareAdapter(secondActivity.this, communicationShareData);
		listview.setAdapter(mCommunicationShareAdapter);
		
	}
	class DetailRequestListener implements IRequestListener {
		@Override
		public void onComplete(String response) {
			try {
				//m_commucationObj =  new CommunicationShareData();
				
				communicationShareData = new ArrayList<CommunicationShareData>();
				communicationShareData.addAll(readCommunicationShare(response));
				
				Message myMessage = myHandler.obtainMessage();//new Message();
				myMessage.what = MISSION_COMMUNICATION_SHARE;
				myHandler.sendMessage(myMessage);
				

				
				Log.i("Info",response);
				
			} catch (Exception e) {
				//e.printStackTrace();
			 
			}finally{
				
			}
			
		}
		@Override
		public void onError(Exception e) {
			//Toast.makeText(myContext, "�����¼����ʧ��", 2000).show();
		}
	}
	
	
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
	
	public static ArrayList<CommunicationShareData> readCommunicationShare(String resp)throws IOException, JSONException {
		ArrayList<CommunicationShareData> mCommunicationShareDataList = new ArrayList<CommunicationShareData>();
		JSONObject jsonObject = new JSONObject(resp).getJSONObject("AllInfo");
		JSONArray json = jsonObject.getJSONArray("InfoList");
		for(int i = 0; i < json.length(); i++){
			JSONObject jsonObject2 = (JSONObject)json.opt(i);
			CommunicationShareData mCommunicationShareData = new CommunicationShareData();
			mCommunicationShareData.setContent(jsonObject2.getString("FContent"));
			mCommunicationShareData.setDate(jsonObject2.getString("FTime"));
			mCommunicationShareData.setName(jsonObject2.getString("FUser"));
			mCommunicationShareDataList.add(mCommunicationShareData);
		}
		
		return mCommunicationShareDataList;
		
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
