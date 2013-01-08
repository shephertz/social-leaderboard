package com.shephertz.app42leaderboard;

import android.app.ListActivity;
import android.os.Bundle;

public class WallActivity extends ListActivity{
	private WallAdapter adapter;

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);      
        LocalStorageService.instance().setContext(this);
        adapter = new WallAdapter(this);
        this.setListAdapter(adapter);        
    }  
    
    protected void onStart(){
    	super.onStart();
    	adapter.loadNotifications();	
    }
    
    protected void onStop(){
    	super.onStop();
    }
}
