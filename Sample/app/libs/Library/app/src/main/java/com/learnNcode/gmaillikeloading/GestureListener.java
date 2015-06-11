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

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureListener extends SimpleOnGestureListener {


	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private INotifyClass iNotifyClass;

	public GestureListener(INotifyClass instance){
		iNotifyClass = instance;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2,float velocityX, float velocityY) {

		iNotifyClass.isProcessStart(false);

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

			iNotifyClass.isLoadingBarVisible(false);
			return true;
		}  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE &&
				Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
			//From Top to Bottom

			iNotifyClass.isLoadingBarVisible(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

		if(! CustomComponent.CustomComponentInstance().isRefreshStarted){
			int count = (int) (e2.getY() - e1.getY());

			iNotifyClass.getY((int) count);

			if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
				//From Top to Bottom
				iNotifyClass.showtLoading();
				return true;
			}
		}
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

}
