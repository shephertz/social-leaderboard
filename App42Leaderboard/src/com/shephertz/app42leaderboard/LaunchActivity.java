package com.shephertz.app42leaderboard;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LaunchActivity extends FacebookProfileRequesterActivity {

	private TextView headerMsg;
	private ImageView profileImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_launch);
        headerMsg = (TextView)findViewById(R.id.header);
        profileImage = (ImageView)findViewById(R.id.profile_pic);
        FacebookService.instance().setContext(getApplicationContext());
        findViewById(R.id.post_fb_layout).setVisibility(View.GONE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        FacebookService.instance().authorizeCallback(requestCode, resultCode, data);
    } 
    
    private void showPostFacebookUI(){
    	findViewById(R.id.pre_fb_layout).setVisibility(View.GONE);
    	findViewById(R.id.post_fb_layout).setVisibility(View.VISIBLE);
    }
    
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialog = null;
        AlertDialog.Builder builder;
        switch(id) {
        case Constants.INTERNET_ALERT_DIALOG_ID:
            // do the work to define the pause Dialog
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Please check your internet connectivity and try again")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                     LaunchActivity.this.finish();
                }
            });
            dialog = builder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	
    	if(!Utils.isInternetConnected(this)){
    		showDialog(Constants.INTERNET_ALERT_DIALOG_ID);
    		return;
    	}
    	
    	if(App42Connect.isAuthenticated()){
    		showPostFacebookUI();
    		headerMsg.setText(UserContext.MyDisplayName);
    		Utils.loadImageFromUrl(profileImage, UserContext.MyPicUrl);
    		//AsyncApp42Service.instance().storeUserProfile();
    	}
    }

    public void onSeeLeaderBoardsClicked(View v){
        Intent mainIntent = new Intent(this, LeaderBoardActivity.class);       
        this.startActivity(mainIntent);  
    }

    public void onFriendsClicked(View v){
        Intent mainIntent = new Intent(this, FriendsActivity.class);       
        this.startActivity(mainIntent); 
    }

    public void onFacebookConnectClicked(View v){
    	FacebookService.instance().fetchFacebookProfile(this);
    }
    
	public void onFacebookProfileRetreived(boolean b) {
		showPostFacebookUI();
		headerMsg.setText(UserContext.MyDisplayName);
		Utils.loadImageFromUrl(profileImage, UserContext.MyPicUrl);
		AsyncApp42Service.instance().storeUserProfile();
	}
}
