/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
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
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomListViewGmailType extends ListView{

	private final int PROGRESS_THRESHOLD = 200;
	private IActionBarReset mOnActionBarReset;
	private boolean isProgessStart = false;
	private Context context;
	private int value;
	private GestureDetector gestureDetector;
	protected static int actionBarDisplayOptions = 0;

	private LinearLayout loadingBars;
	private ProgressBar loadingBarLeft;
	private ProgressBar loadingBarRight;
	private ProgressBar loadingBarIndeterminate;
	private ActionBar actionBarView;
	private boolean isScrollFromTop = true;


	protected void setLoading(LinearLayout loadingBars, ProgressBar loadingBarLeft, ProgressBar loadingBarRight, ProgressBar loadingBarIndeterminate, ActionBar actionBarView) {
		this.loadingBars = loadingBars;
		this.loadingBarLeft = loadingBarLeft;
		this.loadingBarRight = loadingBarRight;
		this.loadingBarIndeterminate = loadingBarIndeterminate;
		this.actionBarView = actionBarView;
	}



	public CustomListViewGmailType(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		gestureDetector = new GestureDetector(this.context, new GestureListener());

	}

	public CustomListViewGmailType(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		gestureDetector = new GestureDetector(this.context, new GestureListener());
	}

	public CustomListViewGmailType(Context context) {
		super(context);
		this.context = context;
		gestureDetector = new GestureDetector(this.context, new GestureListener());
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
			new AnimateBackProgressBars().execute(Math.abs(loadingBarLeft.getProgress()));

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
		TextView swipeToRefreshText 			  = (TextView)overlay.findViewById(R.id.swipeToRefreshText);

		Animation translate = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_in_from_top);
		translate.setDuration(100);
		translate.setFillAfter(true);

		swipeToRefreshText.setAnimation(translate);

		actionBarView.setCustomView(overlay, layout);
		actionBarView.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	private void showActionbar(){
		mOnActionBarReset.onActionBarReset();
		actionBarView.setCustomView(null);
		actionBarView.setDisplayOptions(actionBarDisplayOptions);
	}



	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {


		if(getFirstVisiblePosition() == 0 && isProgessStart){

			value  += deltaY;
			if(deltaY < 0){

				if(! CustomView.isRefreshStarted){

					handler.sendEmptyMessage(1);
					loadingBars.setVisibility(View.VISIBLE);
					loadingBarIndeterminate.setVisibility(View.GONE);

					loadingBarLeft.setProgress(Math.abs(value));
					loadingBarRight.setProgress(Math.abs(value));

					if (Math.abs(value) > PROGRESS_THRESHOLD) {
						if(! CustomView.isRefreshStarted){
							if(CustomView.refreshListnerInstance != null){
								CustomView.refreshListnerInstance.preRefresh();
							}
							loadingBars.setVisibility(View.INVISIBLE);
							loadingBarIndeterminate.setVisibility(View.VISIBLE);
							loadingBarIndeterminate.setIndeterminate(true);
							CustomView.isRefreshStarted = true;
						}
					}
				}
			}
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}



	private class GestureListener extends SimpleOnGestureListener{

		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2,float velocityX, float velocityY) {
			isProgessStart = false;
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE &&
					Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				//From Right to Left
				return true;
			}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
					Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				//From Left to Right
				return true;
			}

			if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE &&
					Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				//From Bottom to Top

				loadingBars.setVisibility(View.INVISIBLE);
				return true;
			}  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE &&
					Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				//From Top to Bottom
				loadingBars.setVisibility(View.INVISIBLE);
				return true;
			}
			return false;
		}

	}


	private class AnimateBackProgressBars extends AsyncTask<Integer, Integer, ProgressBar> {
		protected ProgressBar doInBackground(Integer...paths) {

			while (loadingBarRight.getProgress() > 0) {

				int val = loadingBarRight.getProgress();
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
				loadingBarRight.setProgress(--val);
				loadingBarLeft.setProgress(--val);
			}

			return null;
		}

		protected void onPostExecute(ProgressBar result) {
			loadingBars.setVisibility(View.INVISIBLE);
			value = 0;
		}

	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			if(msg.what == 1){
				if (isScrollFromTop) {
					isScrollFromTop = false;

					hideActionBar();
				}	
			}
		};
	};

}
