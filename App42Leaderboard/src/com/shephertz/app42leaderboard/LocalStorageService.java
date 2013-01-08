package com.shephertz.app42leaderboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorageService {

	private SharedPreferences mPrefNotifications = null;    
	private Context appContext = null;
    private static LocalStorageService _instance = null;
    
    public static LocalStorageService instance(){
    	if(_instance == null){
    		_instance = new LocalStorageService();
    	}
    	return _instance;
    }
    
    public void setContext(Context context){
    	_instance.appContext = context;
    	if(mPrefNotifications == null){
    		mPrefNotifications = appContext.getSharedPreferences("MyNotifications", android.content.Context.MODE_PRIVATE);
    	}
    }
    
    private LocalStorageService(){
    	// private constructor for singleton
    }
    
    public void deleteNotification(String id){
    	SharedPreferences.Editor editor = mPrefNotifications.edit();   
    	editor.remove(id);
    	editor.commit();
    }
    
    public void addNotifications(ArrayList<JSONObject> notifications) throws JSONException{
        SharedPreferences.Editor editor = mPrefNotifications.edit();        
        for(int i=0;i<notifications.size();i++){
        	JSONObject message = notifications.get(i);
        	editor.putString(message.getString("id"), message.toString());
        }
        editor.commit();
    }
    
    public ArrayList<JSONObject> getNotifications() throws JSONException{

    	Map<String, String> allNotifications = (Map<String, String>) mPrefNotifications.getAll();    	
    	ArrayList<JSONObject> notificationObjs = new ArrayList<JSONObject>();
    	for (Map.Entry<String, String> entry : allNotifications.entrySet()){    	
    		notificationObjs.add(new JSONObject(entry.getValue()));
    	}
    	
    	return notificationObjs;
    }
}
