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
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;


public class CustomView extends RelativeLayout implements INotifyClass, OnTouchListener {

    private ActionBar actionBarView;
    private CustomListView listview;
    private GestureDetector gestureDetector;
    private CustomComponent component;
    private boolean isActionBarDisabled = false;
    private LayoutInflater layoutInflater;
    private ViewGroup mViewGroup;


    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isActionBarDisabled = true;
        init(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isActionBarDisabled = true;
        init(context);
    }

    public CustomView(Context context) {
        super(context);
        isActionBarDisabled = true;
        init(context);
    }

    public CustomView(Context context, ActionBar actionbar) {
        super(context);
        component = CustomComponent.CustomComponentInstance();

        if (actionbar == null) {
            isActionBarDisabled = true;
        } else {
            actionBarView = actionbar;
            component.actionBarDisplayOptions = actionBarView.getDisplayOptions();
        }
        init(context);
    }

    private CustomView() {
        super(null);
    }

    private void init(Context context) {

        LinearLayout loadingBars;
        ProgressBar loadingBarLeft;
        ProgressBar loadingBarRight;
        ProgressBar loadingBarIndeterminate;

        component = CustomComponent.CustomComponentInstance();
        gestureDetector = new GestureDetector(context, new GestureListener(CustomView.this));

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.main, this);
        loadingBars = (LinearLayout) findViewById(R.id.loadingBars_gmaillikepulltorefresh);
        loadingBarLeft = (ProgressBar) findViewById(R.id.loadingBarLeft_gmaillikepulltorefresh);
        loadingBarRight = (ProgressBar) findViewById(R.id.loadingBarRight_gmaillikepulltorefresh);
        loadingBarIndeterminate = (ProgressBar) findViewById(R.id.loadingBarInDeterminate_gmaillikepulltorefresh);
        listview = (CustomListView) findViewById(R.id.listView_gmaillikepulltorefresh);
        mViewGroup = (ViewGroup) listview.getParent().getParent();


        component.loadingBars = loadingBars;
        component.loadingBarLeft = loadingBarLeft;
        component.loadingBarRight = loadingBarRight;
        component.loadingBarIndeterminate = loadingBarIndeterminate;

        if (actionBarView != null && !isActionBarDisabled) {
            component.actionBarView = actionBarView;
        }

        mViewGroup.setOnTouchListener(this);

    }

    public void setEmptyView(View emptyView) {
        if (listview != null) {

            if (emptyView == null) {
                View dummy = layoutInflater.inflate(R.layout.view_listview_loading, null);
                RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                params.setMargins(0, 5, 0, 0);
                dummy.setLayoutParams(params);
                mViewGroup.addView(dummy, 0, params);
            } else {
                mViewGroup.addView(emptyView, 0);
            }
        }
    }

    /**
     * To set {@link IRefreshListner} instance.
     *
     * @param refreshListnerInstance {@link IRefreshListner} instance.
     */
    public void setRefreshListner(IRefreshListner refreshListnerInstance) {
        if (listview != null) {
            listview = (CustomListView) findViewById(R.id.listView_gmaillikepulltorefresh);
        }
        if (refreshListnerInstance != null) {
            component.refreshListnerInstance = refreshListnerInstance;
        }
    }

    /**
     * To set {@link ActionBar} content after refresh.
     *
     * @param onActionBarReset {@link IActionBarReset} instance
     */
    public void setActionBar(IActionBarReset onActionBarReset) {
        if (listview != null) {
            listview = (CustomListView) findViewById(R.id.listView_gmaillikepulltorefresh);
        }
        if (onActionBarReset != null) {
            listview.setOnActionBarReset(onActionBarReset);
        }
    }

    /**
     * @return {@link List} instance.
     */
    public CustomListView getListView() {
        return listview;
    }

    /**
     * To stop refreshing.
     */
    public void stopLoading() {

        if (component.isRefreshStarted) {
            component.loadingBars.setVisibility(View.GONE);
            component.loadingBarIndeterminate.setVisibility(View.GONE);
            component.loadingBarIndeterminate.setIndeterminate(false);
            component.isRefreshStarted = false;
            if (mViewGroup.getChildCount() > 1) {
                mViewGroup.removeViewAt(0);
            }

            if (component.refreshListnerInstance != null) {
                component.refreshListnerInstance.postRefresh();
            }
        }
    }


    /**
     * To start refreshing.
     */
    public void startLoading() {
        if (!component.isRefreshStarted) {
            listview.startLoading();
        }
    }

    /*
     * To check whether to show action bar while loading
     *
     * @param value set to true to show loading text while  data is loading else set to true.
     */
    public void ActivateActionBar(boolean value) {
        isActionBarDisabled = value;
    }


    @Override
    public void isProcessStart(boolean value) {
    }

    @Override
    public void isLoadingBarVisible(boolean value) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP: {

                new AnimateBackProgressBars().execute(Math.abs(component.loadingBarLeft.getProgress()));

            }
            case MotionEvent.ACTION_MOVE: {
                gestureDetector.onTouchEvent(event);
            }
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void getY(int y) {
        if (y > 0) {
            if (!component.isRefreshStarted) {
                component.loadingBarLeft.setProgress(Math.abs(y));
                component.loadingBarRight.setProgress(Math.abs(y));
                component.loadingBars.setVisibility(View.VISIBLE);
                component.loadingBarIndeterminate.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showtLoading() {
        if (!component.isRefreshStarted) {
            listview.startLoading();
        }
    }
}
