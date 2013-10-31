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


import java.util.List;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class CustomView extends RelativeLayout {

	private LayoutInflater layoutInflater;
	private CustomListViewGmailType listview;
	private LinearLayout loadingBars;
	private ProgressBar loadingBarLeft;
	private ProgressBar loadingBarRight;
	private ProgressBar loadingBarIndeterminate;
	private ActionBar actionBarView;
	protected static IRefreshListner refreshListnerInstance;
	protected static boolean isRefreshStarted = false; 


	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomView(Context context) {
		super(context);
		init(context);
	}

	public CustomView(Context context, ActionBar actionbar) {
		super(context);
		actionBarView = actionbar;
		CustomListViewGmailType.actionBarDisplayOptions = actionBarView.getDisplayOptions();
		init(context);
	}

	public CustomView(){
		super(null);
	}

	private void init(Context context){
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.main, this);
		loadingBars 			= (LinearLayout)findViewById(R.id.loadingBars);
		loadingBarLeft 			= (ProgressBar)findViewById(R.id.loadingBarLeft);
		loadingBarRight 		= (ProgressBar)findViewById(R.id.loadingBarRight);
		loadingBarIndeterminate = (ProgressBar)findViewById(R.id.loadingBarInDeterminate);
		listview 				= (CustomListViewGmailType)findViewById(R.id.listView);
		listview.setLoading(loadingBars, loadingBarLeft, loadingBarRight, loadingBarIndeterminate, actionBarView);
	}

	/**
	 * To set {@link IRefreshListner} instance.
	 * 
	 * @param refreshListnerInstance 
	 * 									{@link IRefreshListner} instance.
	 */
	public void setRefreshListner(IRefreshListner refreshListnerInstance) {
		if(listview != null){
			listview = (CustomListViewGmailType)findViewById(R.id.listView);
		}
		if(refreshListnerInstance != null){
			CustomView.refreshListnerInstance = refreshListnerInstance;
		}
	}

	/**
	 * To set {@link ActionBar} content after refresh.
	 * 
	 * 
	 * @param onActionBarReset
	 * 							{@link IActionBarReset} instance
	 * 
	 */
	public void setActionBar(IActionBarReset onActionBarReset){
		if(listview != null){
			listview = (CustomListViewGmailType)findViewById(R.id.listView);
		}
		if(onActionBarReset != null){
			listview.setOnActionBarReset(onActionBarReset);
		}
	}

	/**
	 * 
	 * @return {@link List} instance.
	 */
	public ListView getListView(){
		return listview;

	}

	/**
	 * To stop refreshing.
	 */
	public void stopLoading(){
		if(isRefreshStarted){
			if(refreshListnerInstance != null){
				refreshListnerInstance.postRefresh();
			}
			loadingBars.setVisibility(View.INVISIBLE);
			loadingBarIndeterminate.setVisibility(View.GONE);
			loadingBarIndeterminate.setIndeterminate(false);
			isRefreshStarted = false;
		}
	}

	/**O
	 * To start refreshing.
	 */
	public void startLoading(){
		if(! isRefreshStarted){
			if(refreshListnerInstance != null){
				refreshListnerInstance.preRefresh();
			}
			loadingBars.setVisibility(View.INVISIBLE);
			loadingBarIndeterminate.setVisibility(View.VISIBLE);
			loadingBarIndeterminate.setIndeterminate(true);
			isRefreshStarted = true;
		}
	}
}
