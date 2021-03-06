package com.example.activitydemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;

import android.net.Uri;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.Toast;
import com.example.dbmanager.DBHelper;
import com.example.dbmanager.PersonDAL;
import com.example.domain.Person;

public class MainActivity extends Activity {

	 private static final String TAG = "ActivityDemo";     
	 private EditText mEditText; 	 
	 private Button callButton = null;
	 private Button btnAnimation = null;
	 private static final int REQUEST_CODE=1;
	 private Button btnCreateDB = null;
	 private Button btnCreateTable = null;
	 private Button btnInsertRec = null;
	 private PersonDAL perDAL;  
	 private Dialog alertDialog = null;
	 private ListView listView = null;
	 private Button btnQueryMain = null;
	 private int cur_pos = 0;// 当前显示的一行  
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);         
        setContentView(R.layout.activity_main);         
        mEditText = (EditText)findViewById(R.id.editText123);        
        callButton = (Button)findViewById(R.id.callButton);
        callButton.setOnClickListener(listener);     
        
        btnAnimation = (Button)findViewById(R.id.btnAnimation);
        btnAnimation.setOnClickListener(listener);  
        
        btnInsertRec =  (Button)findViewById(R.id.btnInsertRec);
        listView  =  (ListView)findViewById(R.id.listView);
        btnInsertRec.setOnClickListener(listener); 
        
        btnQueryMain =  (Button)findViewById(R.id.btnQueryMain);
        btnQueryMain.setOnClickListener(listener); 
        
        perDAL = new PersonDAL(this);  
        
        
        listView.setOnItemClickListener(new OnItemClickListener()
        {  
            @Override   
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)    
            {        
                    //这是测试log,应该是在另一个activity里面打开的    
                    Log.e(TAG, "null");    //怀疑数组是空所以没有log添加的
                    Log.e(TAG, "((TextView)view).getText()");                      
     
                   
//                    TextView tv = (TextView)listView.getChildAt(0);
//                    Log.e(TAG, ((TextView)view).getText().toString()); 
                    Log.e(TAG, Integer.toString(position));  
                    
                  //获取下一行的index
//                    int index = listView.getSelectedItemPosition() + 1;
//                    //获取下一行的TextView
//                    TextView tv = (TextView)listView.getChildAt(index);
//                    //获取一下行的值
//                    String value = tv.getText().toString();
//                    Log.e(TAG, value);  
                    
                    HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(position); 
                    String title=map.get("name"); 
                    String content=map.get("info"); 
                    Toast.makeText(getApplicationContext(),  
                            "你选择了第"+position+"个Item，itemTitle的值是："+title+"itemContent的值是:"+content, 
                            Toast.LENGTH_SHORT).show(); 
             }   
		 });
        
		alertDialog = new AlertDialog.Builder(this). 
                setTitle("对话框的标题"). 
                setMessage("已成功增加一记录"). 
                setIcon(R.drawable.ic_launcher). 
                create();        
        Log.e(TAG, "start onCreate~~~"); 
    }
//  //生成SimpleAdapter适配器对象 
//    private SimpleAdapter mySimpleAdapter=new SimpleAdapter(this, 
//            myArrayList,//数据源 
//            R.layout.list_items,//ListView内部数据展示形式的布局文件listitem.xml 
//            new String[]{"itemTitle","itemContent"},//HashMap中的两个key值 itemTitle和itemContent 
//            new int[]{R.id.itemTitle,R.id.itemContent}); 
//     
//    listView.setAdapter(mySimpleAdapter); 
//    //添加点击事件 
//    myListView.setOnItemClickListener(new OnItemClickListener(){ 
//        @Override 
//        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
//            //获得选中项的HashMap对象 
//            HashMap<String,String> map=(HashMap<String,String>)myListView.getItemAtPosition(arg2); 
//            String title=map.get("itemTitle"); 
//            String content=map.get("itemContent"); 
//            Toast.makeText(getApplicationContext(),  
//                    "你选择了第"+arg2+"个Item，itemTitle的值是："+title+"itemContent的值是:"+content, 
//                    Toast.LENGTH_SHORT).show(); 
//        } 
//         
//    }); 
//  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE)
			{
			  if(resultCode==secondActivity.RESULT_CODE)
				{
				  Bundle bundle = data.getExtras();
				  String str = bundle.getString("back");
				  Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();				  
				}
			}
	}

    @Override
    protected void onStart(){
    	super.onStart();
    	Log.e(TAG, "start onStart~~~");    	
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    	Log.e(TAG, "start onRestart~~~");    	
    }
    
    
    @Override
    protected void onPause(){
    	super.onPause();
    	Log.e(TAG, "start onPause~~~");    	
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	Log.e(TAG, "start onPause~~~");    	
    }
 
    @Override
    protected void onStop(){
    	super.onStop();
    	Log.e(TAG, "start onPause~~~");    	
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB  
    	perDAL.closeDB();  
    	Log.e(TAG, "start onPause~~~");    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    private OnClickListener listener = new OnClickListener()
    {   	    
    	@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.callButton:{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, secondActivity.class);
				intent.putExtra("str", "come from first activity");
				//startActivity(intent);//无返回值的调用,启动一个明确的activity
				startActivityForResult(intent, REQUEST_CODE);
				/*调用打电话的intent,启动一个未指明的activity，由android去寻找
				* Intent intent = new Intent();
		   	intent.setAction(Intent.ACTION_CALL);
		   	intent.setData(Uri.parse("tel:10086"));
		   	startActivity(intent);
		   	Log.i("aa", "bb");*/							
			}
			break;
			case R.id.btnAnimation:{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AnimationActivity.class);
				intent.putExtra("str", "come from first activity");
				//startActivity(intent);//无返回值的调用,启动一个明确的activity
				startActivityForResult(intent, REQUEST_CODE);
									
			}
			break;
			case R.id.btnInsertRec:{
				add(v);
			
		        alertDialog.show(); 
			
			}
			break;
			case R.id.btnQueryMain:{
				query(v);
			
		        alertDialog.show(); 
			
			}
			break;
			default:
				break;
			}			
		}
	};

	
    private void add(View view) {  
        ArrayList<Person> persons = new ArrayList<Person>();  
          
        Person person1 = new Person("Ella", 22, "lively girl");  
        Person person2 = new Person("Jenny", 22, "beautiful girl");  
        Person person3 = new Person("Jessica", 23, "sexy girl");  
        Person person4 = new Person("Kelly", 23, "hot baby");  
        Person person5 = new Person("Jane", 25, "a pretty woman");  
          
        persons.add(person1);  
        persons.add(person2);  
        persons.add(person3);  
        persons.add(person4);  
        persons.add(person5);           
        this.perDAL.add(persons);  
    }  
     
    
    public void query(View view) {  
        List<Person> persons = perDAL.query();  
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();  
        for (Person person : persons) {  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("name", person.name);  
            map.put("info", person.age + " years old, " + person.info);  
            list.add(map);  
        }  
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,  
                    new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});  
        listView.setAdapter(adapter);  
    }  
      
    public void queryTheCursor(View view) {  
        Cursor c = perDAL.queryTheCursor();  
        startManagingCursor(c); //托付给activity根据自己的生命周期去管理Cursor的生命周期  
        CursorWrapper cursorWrapper = new CursorWrapper(c) {  
            @Override  
            public String getString(int columnIndex) {  
                //将简介前加上年龄  
                if (getColumnName(columnIndex).equals("info")) {  
                    int age = getInt(getColumnIndex("age"));  
                    return age + " years old, " + super.getString(columnIndex);  
                }  
                return super.getString(columnIndex);  
            }  
        };  
        //确保查询结果中有"_id"列  
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,   
                cursorWrapper, new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});  
        ListView listView = (ListView) findViewById(R.id.listView);  
        listView.setAdapter(adapter);  
    }   
    
}
