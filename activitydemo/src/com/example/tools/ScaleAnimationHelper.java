package com.example.tools;

import com.example.tools.DisplayNextView;
import com.example.activitydemo.AnimationActivity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class ScaleAnimationHelper {
	Context con;
	int order;

	public ScaleAnimationHelper(Context con, int order) {
		this.con = con;
		this.order = order;
	}

	DisplayNextView listener;
	ScaleAnimation myAnimation_Scale;
        //�Ŵ����,����Ҫ���ü�����
	public void ScaleOutAnimation(View view) {
		myAnimation_Scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		myAnimation_Scale.setInterpolator(new AccelerateInterpolator());
		AnimationSet aa = new AnimationSet(true);
		aa.addAnimation(myAnimation_Scale);
		aa.setDuration(500);

		view.startAnimation(aa);
	}

	public void ScaleInAnimation(View view) {
		listener = new DisplayNextView((AnimationActivity) con, order);
		myAnimation_Scale = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		myAnimation_Scale.setInterpolator(new AccelerateInterpolator());
               //��СLayout����,�ڶ���������Ҫ�Ӹ�View�Ƴ���
		myAnimation_Scale.setAnimationListener(listener);
		AnimationSet aa = new AnimationSet(true);
		aa.addAnimation(myAnimation_Scale);
		aa.setDuration(500);

		view.startAnimation(aa);
	}
}
