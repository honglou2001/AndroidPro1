package com.example.activitydemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu; 
import android.graphics.drawable.Drawable;   
import android.widget.Button;  
import android.widget.ImageView;  
import android.widget.LinearLayout;  
import android.widget.RelativeLayout;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.View.OnClickListener;
import  com.example.tools.Constant;
import  com.example.tools.DisplayNextView;
import  com.example.tools.ScaleAnimationHelper;

public class AnimationActivity extends Activity  implements OnClickListener {  
	 
	   public RelativeLayout layout_parent;  
	     
	   Button scale_btn;  
	 
	   /** Called when the activity is first created. */  
	   @Override  
	   public void onCreate(Bundle savedInstanceState) {  
	       super.onCreate(savedInstanceState);  
	       setContentView(R.layout.activity_animation);  
	 
	       scale_btn = (Button) findViewById(R.id.scale_btn);  
	       scale_btn.setOnClickListener(this);  
	 
	       layout_parent = (RelativeLayout) findViewById(R.id.layout_parent);  
	   }  
	 
	   @Override  
	   public void onClick(View v) {  
	       // TODO Auto-generated method stub  
	       switch (v.getId()) {  
	       case R.id.scale_btn:  
	           displayPage();  
	           v.setEnabled(false);  
	           break;  
	       case R.id.dismiss_btn:  
	           dismissPage();  
	           break;  
	       }  
	 
	   }  
	 
	   View layout;  
	   ImageView jobShadow;  
	 
	   @SuppressWarnings("deprecation")
	public void displayPage() {  
	       LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  
	       layout = inflater.inflate(R.layout.animation2, null);  
	       layout.setId(Constant.KEY_LAYOUT_ID);  
	       jobShadow = (ImageView) layout.findViewById(R.id.jobShadow);  
	 
	       Drawable ico = getResources().getDrawable(R.drawable.title_bg);  
	       jobShadow.setBackgroundDrawable(ico);  
	       ico.mutate().setAlpha(200);  
	 
	       LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  
	               LinearLayout.LayoutParams.FILL_PARENT,  
	               LinearLayout.LayoutParams.FILL_PARENT);  
	       layout_parent.addView(layout, layoutParams);  
	 
	       findDialogView();  
	 
	       // 显示layout进行缩放动画效果  
	       ScaleAnimationHelper scaleHelper = new ScaleAnimationHelper(this,  
	               Constant.KEY_FIRSTPAGE);  
	       scaleHelper.ScaleOutAnimation(layout);  
	   }  
	 
	   public void removeLayout() {  
	 
	       layout_parent.removeView(layout_parent  
	               .findViewById(Constant.KEY_LAYOUT_ID));  
	   }  
	 
	   Button dismiss_btn;  
	 
	   public void findDialogView() {  
	       dismiss_btn = (Button) findViewById(R.id.dismiss_btn);  
	       dismiss_btn.setOnClickListener(this);  
	   }  
	 
	   public void dismissPage() {  
	       ScaleAnimationHelper scaleHelper = new ScaleAnimationHelper(this,  
	               Constant.KEY_SECONDPAGE);  
	       scaleHelper.ScaleInAnimation(layout);  
	       scale_btn.setEnabled(true);  
	   }  
	  
}


