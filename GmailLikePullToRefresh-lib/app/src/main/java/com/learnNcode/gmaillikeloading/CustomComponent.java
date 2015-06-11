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
import android.graphics.ColorFilter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class CustomComponent {

	protected ActionBar actionBarView = null;
	protected LinearLayout loadingBars;
	protected ProgressBar loadingBarLeft;
	protected ProgressBar loadingBarRight;
	protected ProgressBar loadingBarIndeterminate;
	protected int actionBarDisplayOptions = 0;
	protected boolean isRefreshStarted = false; 
	protected IRefreshListner refreshListnerInstance;
	protected ColorFilter colorFilter;

	protected static CustomComponent component = new CustomComponent();

	private CustomComponent(){}

	protected static CustomComponent CustomComponentInstance() {
		if(component == null){
			component = new CustomComponent();
		}
		return component;
	}

	protected void setActionBarDisplayOptions() {
		if(actionBarView != null){
			actionBarView.setDisplayOptions(actionBarDisplayOptions);
		}
	}

}
