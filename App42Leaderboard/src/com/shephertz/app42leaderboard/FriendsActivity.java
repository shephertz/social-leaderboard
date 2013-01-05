/**
 * 
 */
package com.shephertz.app42leaderboard;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author dhruvchopra
 *
 */
public class FriendsActivity extends ListActivity {
	private FriendsActivityAdapter adapter;

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);            
        
        adapter = new FriendsActivityAdapter(this);
        this.setListAdapter(adapter);       
        
    }  
    
    protected void onStart(){
    	super.onStart();
    	adapter.loadFeed();
    	
    }
    
    protected void onStop(){
    	super.onStop();
    }

	public void handleNoFriends() {
		((TextView)findViewById(R.id.notification_msg)).setText("No friends are playing");		
	}
}
