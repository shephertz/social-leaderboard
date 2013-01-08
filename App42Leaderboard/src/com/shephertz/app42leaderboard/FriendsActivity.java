/**
 * 
 */
package com.shephertz.app42leaderboard;

import java.util.ArrayList;

import org.json.JSONObject;

import com.shephertz.app42leaderboard.AsyncApp42Service.MessageQueueListener;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author dhruvchopra
 *
 */
public class FriendsActivity extends ListActivity implements MessageQueueListener {
	private FriendsActivityAdapter adapter;

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);            
        
        adapter = new FriendsActivityAdapter(this);
        this.setListAdapter(adapter);       
        
    }  

	public void onNotificationsClicked(View v){
        Intent mainIntent = new Intent(this, WallActivity.class);       
        this.startActivity(mainIntent);
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
	
	@Override
	public void onListItemClick(ListView l, View v, final int position, long id){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Message "+adapter.getFriendName(position));
		//alert.setMessage("Message");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getEditableText().toString();
		  AsyncApp42Service.instance().sendNotification(adapter.getFriendId(position), value, true, FriendsActivity.this);
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	@Override
	public void onSendNotification(int result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetMessages(ArrayList<JSONObject> msgList) {
		// TODO Auto-generated method stub
		
	}
}
