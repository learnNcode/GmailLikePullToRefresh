GmailLikePullToRefresh
======================

Gmail like pull to refresh implementation.


Screenshot
=========

![swipe down action](https://dl.dropboxusercontent.com/u/61919232/learnNcode/GmailLikePullToRefresh/gmailLikeLoading.gif)




ChangeLog
=========
Version 2.0

__1]__ Bug fixes.

__2]__ Added method  ```ActivateActionBar(boolean value)```.

__3]__ Showing loading page; If no data.

Usage
=====

Check the attached demo sample app.

__*__ Here is the code snippet
```java
CustomView view = new CustomView(getApplicationContext(), actionBar);
setContentView(view);
view.setRefreshListner(SampleForGmailLIkePullToRefresh.this);
view.setActionBar(SampleForGmailLIkePullToRefresh.this); 
view.getListView().setAdapter(new DummyAdapter(this));  //set adapter to list.
```
 

You can implement `IRefreshListner` interface which has two methods `preRefresh()` & `postRefresh()` which will be invoked when loading starts and stops respectively. You can use these methods to write logic which will be executed before and after refreshing.

We have exposed following  useful methods of CustomView class.
```java
startLoading()  // to start refreshing/loading explicitly.
stopLoading()  // to stop refreshing/loading explicitly.
getListView()  // this will return listview.
```
Dependency
==============
 Works with actionbar only.
    
License
======

    Copyright 2013 learnNcode (learnncode@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Thank You
========

  If you like our work say a hi :)
  <br>
  Happy Coding Happy Learning.
