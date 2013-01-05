package com.shephertz.app42leaderboard;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import com.shephertz.app42.paas.sdk.android.App42NotFoundException;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.customcode.CustomCodeService;
import com.shephertz.app42.paas.sdk.android.game.Game;
import com.shephertz.app42.paas.sdk.android.game.GameService;
import com.shephertz.app42.paas.sdk.android.game.ScoreBoardService;
import com.shephertz.app42.paas.sdk.android.game.Game.Score;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder.Operator;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;


public class AsyncApp42Service {
	
	
	private ScoreBoardService scoreBoardService = null;
	private StorageService storageService = null;
	private GameService gameService = null;
	
	private static AsyncApp42Service mInstance = null;
	
	public static AsyncApp42Service instance()
	{
		if(mInstance == null)
		{
			mInstance = new AsyncApp42Service();
		}
		
		return mInstance;
	}
	
	private AsyncApp42Service(){
		// initialize the singletons.
    	ServiceAPI sp = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);
    	this.scoreBoardService = sp.buildScoreBoardService(); 	
    	this.storageService = sp.buildStorageService();
    	this.gameService = sp.buildGameService();
	}	
	
	public void saveScore(final String name, final long score, final String gameName, final App42ResultHandler callBack){
		final Handler callingThreadHandler = new Handler();
        new Thread(){
            @Override public void run() {
                try {         	
                	scoreBoardService.saveUserScore(gameName, name, score);         	            		
            		callingThreadHandler.post(new Runnable() {
    	                @Override
    	                public void run() {
    	                	if(callBack != null){
    	                		callBack.onSubmitScore(App42ResultHandler.SUCCESS);
    	                	}
    	                }
    	            });
                }  
            	catch (Exception ex) {
            		callingThreadHandler.post(new Runnable() {
    	                @Override
    	                public void run() {
    	                	if(callBack != null){
    	                		callBack.onSubmitScore(App42ResultHandler.UNKNOWN_ERROR);
    	                	}
    	                }
    	            });
            	}
            }
        }.start();		
	}
	
	// callback interface
	public static interface App42LeaderBoardListener {
		public void onGetTopRankings(ArrayList<JSONObject> scoreDataList);

		public void onGetLastActivityOfUser(String gameName, String points, String time, int reqID);

		public void onGetTopRankingsToday(ArrayList<JSONObject> scoreDataList);

		public void onGetTopRankingsInGroup(ArrayList<JSONObject> scoreDataList);
	}

	public void storeUserProfile() {
		
		new Thread(){
			@Override public void run() {
				// check if it already exists
				try{
					if(UserContext.MyUserName.length() <= 0){
						throw new App42NotFoundException();
					}
					storageService.findDocumentByKeyValue(Constants.USER_DB_NAME, Constants.USER_PROFILE_COLLECTION, 
																		"UserName", UserContext.MyUserName);
				}
				catch(App42NotFoundException e){
					// need to store the my username, displayname and picUrl
					JSONObject userProfile = new JSONObject();
					try {
						userProfile.put("UserName", UserContext.MyUserName);
						userProfile.put("DisplayName", UserContext.MyDisplayName);
						userProfile.put("PicUrl", UserContext.MyPicUrl);
						storageService.insertJSONDocument(Constants.USER_DB_NAME, Constants.USER_PROFILE_COLLECTION, userProfile.toString());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}	
			}
		}.start();
	}
	

	
	public void getTopRankingsToday(final String currentGameName, final App42LeaderBoardListener callBack) {
		final Handler callerThreadHandler = new Handler();
        new Thread(){
            @Override public void run() {
                try {  
                	Date today = new Date();
                	Date yesterday = new Date(today.getTime() - 1000*60*60*10);
                	final Game game = scoreBoardService.getTopRankings(currentGameName, yesterday, today);
                	ArrayList<Score> topScoresOfTheDay = game.getScoreList();
                	final ArrayList<JSONObject> scoreDataList = buildScoreDataList(topScoresOfTheDay);
            		if(callBack != null){
	            		callerThreadHandler.post(new Runnable() {
	                        @Override
	                        public void run() {      
	                        	callBack.onGetTopRankingsToday(scoreDataList);
	                        }
	                    });                    
            		}
                }  
            	catch (Exception ex) {
            		System.out.println(ex.toString());    	
        			callBack.onGetTopRankingsToday(null);
            	}
            }
        }.start();
	}
	
	public void getTopRankings(final String currentGameName, final App42LeaderBoardListener callBack) {
		final Handler callerThreadHandler = new Handler();
        new Thread(){
            @Override public void run() {
                try {  
                	final Game game = scoreBoardService.getTopNRankings(currentGameName, 20);
                	ArrayList<Score> topScores = game.getScoreList();
                	final ArrayList<JSONObject> scoreDataList = buildScoreDataList(topScores);
            		if(callBack != null){
	            		callerThreadHandler.post(new Runnable() {
	                        @Override
	                        public void run() {      
	                        	callBack.onGetTopRankings(scoreDataList);
	                        }
	                    });                    
            		}
                }  
            	catch (Exception ex) {
            		System.out.println(ex.toString());    	
        			callBack.onGetTopRankings(null);
            	}
            }
        }.start();
	}

	public void getTopRankingsInGroup(final String currentGameName, final ArrayList<String> group, final App42LeaderBoardListener callBack) {
		final Handler callerThreadHandler = new Handler();
        new Thread(){
            @Override public void run() {
                try {      	
                	final Game game = scoreBoardService.getTopRankingsByGroup(currentGameName, group);
                	ArrayList<Score> topScores = game.getScoreList();
                	final ArrayList<JSONObject> scoreDataList = buildScoreDataList(topScores);
            		if(callBack != null){
	            		callerThreadHandler.post(new Runnable() {
	                        @Override
	                        public void run() {      
	                        	callBack.onGetTopRankingsInGroup(scoreDataList);
	                        }
	                    });                    
            		}
                }  
            	catch (Exception ex) {
            		System.out.println(ex.toString());    	
            		if(callBack != null){
	            		callerThreadHandler.post(new Runnable() {
	                        @Override
	                        public void run() {      
	                        	callBack.onGetTopRankingsInGroup(null);
	                        }
	                    });                    
            		}
            	}
            }
        }.start();
	}
	
	public void getLastActivityOfUser(final int reqID, final String username, final App42LeaderBoardListener callBack) {
		final Handler callerThreadHandler = new Handler();
        new Thread(){
            @Override public void run() {
                try {  
                	/* TODO: fix once new API is ready
                	ArrayList<Game> allGames = gameService.getAllGames();
                	Game lastUserGame = null;
                	Score lastUserScore = null;
                	for(int i=0; i<allGames.size(); i++){
                		String gameName = allGames.get(i).getName();
                		try{
                			Game game = scoreBoardService.getLastScoreByUser(gameName, username);
                			Score lastGameScore = game.getScoreList().get(0);
                			if(gameName.equals(App42Connect.currentGameName)) { //TODO: fix lastUserScore == null || lastUserScore.createdOn.before(lastGameScore.createdOn)){
                				lastUserScore = lastGameScore;
                				lastUserGame = game;
                			}
                		}
                		catch(App42NotFoundException e){
                			continue;
                		}
                	}*/
                	
                	Game lastUserGame = scoreBoardService.getLastScoreByUser(App42Connect.currentGameName, username);
                	Score lastUserScore = lastUserGame.getScoreList().get(0);
                	
                	final String lastUserGameName = lastUserGame!=null? lastUserGame.name : "";
                	final String points =  lastUserScore!=null ? (lastUserScore.getValue()).toString() : "";
                	final String time = lastUserScore!=null ? getTimeDifference(lastUserScore.createdOn) : "";
            		if(callBack != null){
	            		callerThreadHandler.post(new Runnable() {
	                        @Override
	                        public void run() {      
	                        	callBack.onGetLastActivityOfUser(lastUserGameName, points, time, reqID);
	                        }
	                    });                    
            		}
                }  
            	catch (Exception ex) {
            		if(callBack != null){
	            		callerThreadHandler.post(new Runnable() {
	                        @Override
	                        public void run() {      
	                        	callBack.onGetLastActivityOfUser("", "", "", reqID);
	                        }
	                    });                    
            		}	
            	}
            }
        }.start();
	}
	
	private String getTimeDifference(Date oldTime){
		
		Date now = new Date();
		
		long offSet = now.getTimezoneOffset() * Constants.MILLISECONDS_MINUTE; 
		
		long diff = now.getTime() - oldTime.getTime() + offSet;

		if(diff < Constants.MILLISECONDS_MINUTE){
			return "less than a minute ago";
		}
		else if(diff < Constants.MILLISECONDS_HOUR){
			return (int)(diff/Constants.MILLISECONDS_MINUTE)+" minutes ago";
		}
		else if(diff < Constants.MILLISECONDS_DAY){
			return (int)(diff/Constants.MILLISECONDS_HOUR)+" hours ago";
		}
		else{
			return (int)(diff/Constants.MILLISECONDS_DAY)+" days ago";
		}
	}
	
	protected ArrayList<JSONObject> buildScoreDataList(ArrayList<Score> topScores) throws JSONException {
		// build an array of json so that each has DisplayName and Score keys
		
		ArrayList<String> usernames = new ArrayList<String>();
		ArrayList<Query> queries = new ArrayList<Query>();
		for(int i=0; i<topScores.size(); i++){
			Score score = topScores.get(i);
			usernames.add(score.getUserName());
			Query q = QueryBuilder.build("UserName",score.getUserName(), Operator.EQUALS);
			queries.add(q);
		}
		
		// build a query for user profiles
		Query finalQuery = queries.get(0);
		for(int i=1; i<queries.size(); i++){
			Query q = queries.get(i);
			finalQuery = QueryBuilder.compoundOperator(q, Operator.OR, finalQuery);
		}
		
		Storage storageObj = storageService.findDocumentsByQuery(Constants.USER_DB_NAME, Constants.USER_PROFILE_COLLECTION, finalQuery);
		ArrayList<Storage.JSONDocument> list = storageObj.getJsonDocList();
		
		// construct the profile map
		HashMap<String, JSONObject> profileDocMap = new HashMap<String, JSONObject>();
		for(int i=0; i<list.size(); i++){
			JSONObject obj = new JSONObject(list.get(i).getJsonDoc());
			profileDocMap.put(obj.optString("UserName"), obj);
		}
		
		// Now build the final JSONObject ArrayList to be returned
		ArrayList<JSONObject> retList = new ArrayList<JSONObject>();
		for(int i=0; i<topScores.size(); i++){
			Score score = topScores.get(i);
			String username = score.getUserName();
			String points = (score.getValue()).toString();
			JSONObject profile = profileDocMap.get(username);
			JSONObject retListObj = new JSONObject(profile.toString());
			retListObj.put("Score", points);
			retList.add(retListObj);
		}
		return retList;
	}
	
}
