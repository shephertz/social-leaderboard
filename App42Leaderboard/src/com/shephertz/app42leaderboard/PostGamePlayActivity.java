package com.shephertz.app42leaderboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PostGamePlayActivity extends FacebookProfileRequesterActivity {

	private TextView authHeader;
	private ImageView profileImage;
	private TextView noAuthLine1;
	private TextView noAuthLine2;
	private TextView authLine1;
	
	private long score;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_postgameplay);
        
        authHeader = (TextView)findViewById(R.id.auth_header);
        profileImage = (ImageView)findViewById(R.id.profile_pic);
        authLine1 = (TextView)findViewById(R.id.auth_line1);
        
        noAuthLine1 = (TextView)findViewById(R.id.no_auth_line1);
        noAuthLine2 = (TextView)findViewById(R.id.no_auth_line2);
        
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
		authHeader.setText(UserContext.MyDisplayName);
		Utils.loadImageFromUrl(profileImage, UserContext.MyPicUrl);
		authLine1.setText("You scored "+score+" points");
		AsyncApp42Service.instance().saveScore(UserContext.MyUserName, score, App42Connect.currentGameName, null);
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
                     PostGamePlayActivity.this.finish();
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
    	
    	score = getIntent().getLongExtra("score", 0);
    	
    	if(!Utils.isInternetConnected(this)){
    		showDialog(Constants.INTERNET_ALERT_DIALOG_ID);
    		return;
    	}
    	
    	noAuthLine1.setText("Sign-in to earn rewards");
    	noAuthLine2.setText("You scored "+score+" points");
    	
    	if(App42Connect.isAuthenticated()){
    		showPostFacebookUI();    		
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
		AsyncApp42Service.instance().storeUserProfile();
	}
}
