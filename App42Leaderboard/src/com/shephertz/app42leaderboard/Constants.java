package com.shephertz.app42leaderboard;

public class Constants {

	public static final String App42ApiKey = "50b2fe59bacf43c33664420119be049b911ef8e956cffc6c26e44e76941c0ebc";
	public static final String App42ApiSecret = "a596c3a06641cd3d847d9f379e382d16bff792d44563d63ddab9baab9317d2c3";
	public static final String FB_APP_ID = "525488524141861";
	
	public static final String USER_DB_NAME = "App42ConnectUser";
	public static final String USER_PROFILE_COLLECTION = "Profiles";
	
	public static final int INTERNET_ALERT_DIALOG_ID = 0;
	
	public static final long MILLISECONDS_DAY = 1000*60*60*24;
	public static final long MILLISECONDS_HOUR = 1000*60*60;
	public static final long MILLISECONDS_MINUTE = 1000*60;
	
	public enum RankingMode {
		AllTime, Today, Friends
	}
}
