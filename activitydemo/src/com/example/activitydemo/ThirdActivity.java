package com.example.activitydemo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.example.tools.NetHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.activitydemo.secondActivity.DetailRequestListener;
import com.example.domain.CommunicationShareData;
import com.example.tools.CommunicationShareAdapter;
import com.example.tools.NetHelper.IRequestListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tools.Parameters;

public class ThirdActivity extends Activity {

	private ArrayList<CommunicationShareData> communicationShareData;// ��ͨ����-��ͨ��������
	public final static short MISSION_COMMUNICATION_SHARE = 11;
	private CommunicationShareAdapter mCommunicationShareAdapter;
	private ListView listview;
	private Context myContext;
	
	private SimpleDateFormat sDateFormat;
	private String name = "";
	private String time;
	private String content;
	private EditText communicationShareEditText;
	private StringBuffer urlBuffer = new StringBuffer();
	private Parameters parameters = new Parameters();
	private ImageView communicationShareBotton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		
		listview = (ListView)findViewById(R.id.communication_share_list);	
		communicationShareEditText = (EditText) findViewById(R.id.communication_share_edittext);
		
		communicationShareBotton = (ImageView) findViewById(R.id.communication_share_button);
		communicationShareBotton.setOnTouchListener(lis);
		
		BindData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.third, menu);
		return true;
	}
	
	
	private void BindData()
	{		
		try {
			
			NetHelper helper = new  NetHelper();
			
			helper.requestFDailyUrl(myContext,new DetailRequestListener());
			
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
		mCommunicationShareAdapter = new CommunicationShareAdapter(ThirdActivity.this, communicationShareData);
		listview.setAdapter(mCommunicationShareAdapter);
		
	}
	
	View.OnTouchListener lis = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.communication_share_button:
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					//communicationShareBotton.setImageResource(R.drawable.communication_share_button_click);
					break;
				case MotionEvent.ACTION_UP:
					//communicationShareBotton.setImageResource(R.drawable.communication_share_button);
					sDateFormat = new SimpleDateFormat("yyyy-MM-dd mm:ss");
					time = sDateFormat.format(new java.util.Date()); 
					content = communicationShareEditText.getText().toString().trim();
					if(content.equals("")){
						Toast.makeText(ThirdActivity.this, "����Ϊ�գ��������ٷ���", 2000).show();
					} else {
						PostData(myContext,"1001","Admin",content,new DetailRequestListener());
					}
							
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			return true;
		}

		
	};
	

	private void PostData(final Context context,
			String fUnitId, String fuser, String fcontent,
			final IRequestListener listener) {
		
		urlBuffer.delete(0, urlBuffer.length());
		urlBuffer.append(NetHelper.url + "/comminicate/PostInfo.ashx?");
		urlBuffer.append("funitid=");
		urlBuffer.append(fUnitId);
		urlBuffer.append("&fuser=");
		urlBuffer.append(fuser);
		urlBuffer.append("&fcontent=");
		urlBuffer.append(fcontent);
		final String registerString = urlBuffer.toString();

		parameters.clear();
		new Thread() {
			@Override
			public void run() {
				try {
					NetHelper helper = new  NetHelper();
					
					String resp = helper.request(context, registerString, parameters,
							"GET");
					listener.onComplete(resp);
				} catch (Exception e) {
					listener.onError(e);
				}
			}
		}.start();
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
	
}