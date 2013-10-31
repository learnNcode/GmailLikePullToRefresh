package com.learnNcode.sample;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.learnNcode.gmaillikeloading.CustomView;
import com.learnNcode.gmaillikeloading.IActionBarReset;
import com.learnNcode.gmaillikeloading.IRefreshListner;

public class SampleForGmailLIkePullToRefresh extends Activity implements IRefreshListner, IActionBarReset{

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
		view.setRefreshListner(SampleForGmailLIkePullToRefresh.this);
		view.setActionBar(SampleForGmailLIkePullToRefresh.this);


		list = new ArrayList<Integer>();
		for(int i = 10; i < 51; i++){
			list.add(i);	
		}
		adapter = new DummyAdapter(SampleForGmailLIkePullToRefresh.this, 0, list);
		view.getListView().setAdapter(adapter);
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
		for(int i = 0; i < 10; i++){
			adapter.add(i, i);
			adapter.notifyDataSetChanged();
		}
		handler.postDelayed(runnable, 5000);
	}


	@Override
	public void postRefresh() {

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