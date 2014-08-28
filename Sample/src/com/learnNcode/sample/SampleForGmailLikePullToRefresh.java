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


package com.learnNcode.sample;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.learnNcode.gmaillikeloading.CustomView;
import com.learnNcode.gmaillikeloading.IActionBarReset;
import com.learnNcode.gmaillikeloading.IRefreshListner;

public class SampleForGmailLikePullToRefresh extends Activity implements IRefreshListner, IActionBarReset{

	private CustomView view;
	private ActionBar actionBar;
	private DummyAdapter adapter;
	private ArrayList<Integer> list;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("WOW");


		view = new CustomView(getApplicationContext(), actionBar);
		setContentView(view);
		view.setRefreshListner(SampleForGmailLikePullToRefresh.this);
		view.setActionBar(SampleForGmailLikePullToRefresh.this);


		list = new ArrayList<Integer>();
		adapter = new DummyAdapter(SampleForGmailLikePullToRefresh.this, 0, list);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.sample_for_gmail_like_pull_to_refresh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId())
		{
		case R.id.show_loading:
			view.startLoading();
			return true;

		case R.id.stop_loading:
			view.stopLoading();
			return true;

		case R.id.add_data_refresh:
			addTempDummyData();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActionBarReset() {
		actionBar = getActionBar();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}


	@Override
	public void preRefresh() {
		Toast.makeText(SampleForGmailLikePullToRefresh.this, getString(R.string.pre_refresh_method_called_text), Toast.LENGTH_SHORT).show();

		for(int i = 0; i < 10; i++){
			adapter.add(i, i);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(view.getListView().getAdapter() == null){
			view.getListView().setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		handler.postDelayed(runnable, 1500);
	}


	@Override
	public void postRefresh() {
		Toast.makeText(SampleForGmailLikePullToRefresh.this, getString(R.string.post_refresh_method_called_text), Toast.LENGTH_SHORT).show();
	}


	public void addTempDummyData() {

		view.startLoading();

		for(int i = 0; i < 10; i++){
			adapter.add(i, i);

			adapter.notifyDataSetChanged();
		}

		handler.postDelayed(runnable, 5000);

	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			view.getListView().invalidate();
			view.getListView().requestLayout();
			view.stopLoading();	

		}
	};

}