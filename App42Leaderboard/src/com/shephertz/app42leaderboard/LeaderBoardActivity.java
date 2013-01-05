/**
 * 
 */
package com.shephertz.app42leaderboard;

import com.shephertz.app42leaderboard.Constants.RankingMode;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author dhruvchopra
 *
 */
public class LeaderBoardActivity extends ListActivity{

	private LeaderBoardAdapter adapter;

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);            
        
        // create adapter after fetching userName parameter from intent.
        adapter = new LeaderBoardAdapter(this);
        this.setListAdapter(adapter);       
        
    }  
    
    protected void onStart(){
    	super.onStart();
    	((TextView)findViewById(R.id.header)).setText(App42Connect.currentGameName+" top scores");
    	adapter.reloadScores();
    }
    
    protected void onStop(){
    	super.onStop();
    }
    
    public void onTodayClicked(View v){
    	adapter.resetMode(RankingMode.Today);
    }
    
    public void onAllTimeClicked(View v){
    	adapter.resetMode(RankingMode.AllTime);
    }
    
    public void onFriendsClicked(View v){
    	adapter.resetMode(RankingMode.Friends);
    }
}
