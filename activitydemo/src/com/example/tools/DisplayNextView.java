package com.example.tools;


import com.example.activitydemo.AnimationActivity;

import android.app.Activity;
import android.view.animation.Animation;

public class DisplayNextView implements Animation.AnimationListener {

	Object obj;

	// �����������Ĺ��캯��
	Activity ac;
	int order;

	public DisplayNextView(Activity ac, int order) {
		this.ac = ac;
		this.order = order;
	}

	public void onAnimationStart(Animation animation) {
	}

	public void onAnimationEnd(Animation animation) {
		doSomethingOnEnd(order);
	}

	public void onAnimationRepeat(Animation animation) {
	}

	private final class SwapViews implements Runnable {
		public void run() {
			switch (order) {
			case Constant.KEY_SECONDPAGE:
				((AnimationActivity) ac).removeLayout();
				break;
			}
		}
	}

	public void doSomethingOnEnd(int _order) {
		switch (_order) {
		
		case Constant.KEY_SECONDPAGE:
			((AnimationActivity) ac).layout_parent.post(new SwapViews());
			break;
		}
	}
}
