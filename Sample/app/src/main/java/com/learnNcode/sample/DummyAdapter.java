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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class DummyAdapter extends ArrayAdapter<Integer> {

	private Context context;
	private ArrayList<Integer> list;

	public DummyAdapter(Context context, int textViewResourceId,
			ArrayList<Integer> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		list = objects;
	}

	@Override
	public void add(Integer object) {
		super.add(object);
	}

	public void add(Integer object, int position) {
		list.add(position, object);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Integer getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(com.learnNcode.sample.R.layout.list_item, parent,false);
		}
		TextView textView = (TextView) convertView;
		textView.setText("" + list.get(position));

		return convertView;

	}
}
