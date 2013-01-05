package com.shephertz.app42leaderboard;

import java.io.InputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.ImageView;

public class Utils {

	public static void loadImageFromUrl(final ImageView view, final String url){
		final Handler callerThreadHandler = new Handler();
        new Thread(){
            @Override public void run() {                               
        		final Bitmap bitmap = Utils.loadBitmap(url);
        		// callback is not the main UI thread. So post message to UI thread
        		// through its handler. Android UI elements can't be accessed from
        		// non-UI threads.
        		callerThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {    
                    	if(bitmap != null){
                    		view.setImageBitmap(bitmap);
                    	}
                    }            			
        		});               
            }
        }.start();
	}

	public static Bitmap loadBitmap(String url) {

        Bitmap bitmap = null;

        try {
            InputStream in = new java.net.URL(url).openStream();   
            bitmap = BitmapFactory.decodeStream(in);
        } 
        catch (Exception e) {

        } 

        return bitmap;
    }
	
    public static boolean isInternetConnected(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
