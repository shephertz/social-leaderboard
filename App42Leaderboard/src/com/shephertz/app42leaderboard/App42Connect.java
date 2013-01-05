package com.shephertz.app42leaderboard;

import android.content.Context;
import android.content.Intent;

public class App42Connect {

	private static Context appContext;
	public static String currentGameName;
	
	/*
	 * Launches the app42 connect activity. 
	 * callerContext is the context of the current activity
	 * gameName is the name of the game whose leader board is to
	 * be shown post connect.
	 */
	public static void launchApp42Connect(Context callerContext,String gameName){
		Intent mainIntent = new Intent(callerContext, LaunchActivity.class);	 
		appContext = callerContext.getApplicationContext();
		callerContext.startActivity(mainIntent);
		currentGameName = gameName;
	}
	
	/*
	 * Submit the score of the user
	 * callerContext is the context of the current activity
	 * score is the value of the points scored by the user in the game
	 * gameName is the name of the game in which the score was made
	 * caller is the object which will be used to invoke the result of the operation
	 * fShow determines whether the App42Leader UI has to be shown or not. If false, score will be submitted silently.
	 */
	public static void submitScore(Context callerContext, long score, String gameName, App42ResultHandler caller, boolean fShow){
		
		appContext = callerContext.getApplicationContext();
		FacebookService.instance().setContext(appContext);
		
		if(Utils.isInternetConnected(callerContext) && caller!=null && !fShow){
			caller.onSubmitScore(App42ResultHandler.CONNECTION_ERROR);
			return;
		}
		
		if((!isAuthenticated() || UserContext.MyUserName.length() <= 0) && caller!=null && !fShow){
			caller.onSubmitScore(App42ResultHandler.AUTH_ERROR);
			return;
		}
		currentGameName = gameName;
		
		if(fShow){
			Intent mainIntent = new Intent(callerContext, PostGamePlayActivity.class);	
			mainIntent.putExtra("score", score);
			callerContext.startActivity(mainIntent);
		}
		else{
			AsyncApp42Service.instance().saveScore(UserContext.MyUserName, score, App42Connect.currentGameName, null);
		}
		
	}
	
	/*
	 * Informs whether the user's App42 connect session is valid or not.
	 * Application needs to launchApp42Connect to do the authentication.
	 * 
	 */
	public static boolean isAuthenticated(){
		return (FacebookService.instance().isFacebookSessionValid() && UserContext.MyUserName.length() > 0);
	}
}
