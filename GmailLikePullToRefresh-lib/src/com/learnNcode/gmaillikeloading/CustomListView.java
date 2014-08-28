/*
 * Copyright 2014 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */




package com.learnNcode.gmaillikeloading;

import android.app.ActionBar;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

class CustomListView extends ListView implements INotifyClass{

	private final int PROGRESS_THRESHOLD = 200;
	private IActionBarReset mOnActionBarReset;
	private boolean isProgessStart = false;
	private Context context;
	private int value;
	private GestureDetector gestureDetector;
	private boolean isScrollFromTop = true;
	private CustomComponent component;

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		gestureDetector = new GestureDetector(this.context, new GestureListener(CustomListView.this));
		component = CustomComponent.CustomComponentInstance();
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		gestureDetector = new GestureDetector(this.context, new GestureListener(CustomListView.this));
		component = CustomComponent.CustomComponentInstance();
	}

	private CustomListView(Context context) {
		super(context);
		this.context = context;
		gestureDetector = new GestureDetector(this.context, new GestureListener(CustomListView.this));
		component = CustomComponent.CustomComponentInstance();
	}


	protected void setOnActionBarReset(IActionBarReset mOnActionBarReset){
		this.mOnActionBarReset = mOnActionBarReset;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN: {
			isScrollFromTop = true;
		}

		case MotionEvent.ACTION_UP: {
			isScrollFromTop = true;
			showActionbar();

			new AnimateBackProgressBars().execute(Math.abs(component.loadingBarLeft.getProgress()));
			value = 0;
		}
		case MotionEvent.ACTION_MOVE: {
			isProgessStart = true;
			gestureDetector.onTouchEvent(event);
		}
		}
		return super.onTouchEvent(event);
	}


	private void hideActionBar(){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View overlay 			= inflater.inflate(R.layout.overlay_actionbar, null);
		android.app.ActionBar.LayoutParams layout = new android.app.ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		TextView swipeToRefreshText 			  = (TextView)overlay.findViewById(R.id.swipeToRefreshText_gmaillikepulltorefresh);

		Animation translate = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_in_from_top);
		translate.setDuration(100);
		translate.setFillAfter(true);

		swipeToRefreshText.setAnimation(translate);

		component.actionBarView.setCustomView(overlay, layout);
		component.actionBarView.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	private void showActionbar(){
		mOnActionBarReset.onActionBarReset();
		component.actionBarView.setCustomView(null);
		component.setActionBarDisplayOptions();
	}

	@Override
	public boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		if(getFirstVisiblePosition() == 0 && isProgessStart){

			value  += deltaY;
			if(deltaY < 0){

				if(! component.isRefreshStarted){

					if (Math.abs(value) > PROGRESS_THRESHOLD) {
						startLoading();
					}else{
						handler.sendEmptyMessage(1);
					}
				}
			}
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}

	protected void startLoading() {
		component.loadingBars.setVisibility(View.GONE);
		component.loadingBarIndeterminate.setVisibility(View.VISIBLE);
		component.loadingBarIndeterminate.setIndeterminate(true);
		component.isRefreshStarted = true;

		if(component.refreshListnerInstance != null){
			component.refreshListnerInstance.preRefresh();
		}
	}

	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if(msg.what == 1){

				component.loadingBarLeft.setProgress(Math.abs(value));
				component.loadingBarRight.setProgress(Math.abs(value));
				component.loadingBars.setVisibility(View.VISIBLE);
				component.loadingBarIndeterminate.setVisibility(View.GONE);

				if (isScrollFromTop) {
					isScrollFromTop = false;

					if(component.actionBarView != null){
						hideActionBar();
					}
				}

			}
			return false;
		}
	});


	@Override
	public void isProcessStart(boolean value) {
		isProgessStart = value;
	}


	@Override
	public void isLoadingBarVisible(boolean value) {
		if(! value){
			component.loadingBars.setVisibility(View.GONE);
		}
	}


	@Override
	public void getY(int y) {}


	@Override
	public void showtLoading() {}

}
