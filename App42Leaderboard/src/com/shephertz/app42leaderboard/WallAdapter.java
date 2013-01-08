package com.shephertz.app42leaderboard;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.shephertz.app42leaderboard.AsyncApp42Service.MessageQueueListener;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WallAdapter extends BaseAdapter implements MessageQueueListener{

	private Context context;
	
	private ArrayList<JSONObject> myNotifications = new ArrayList<JSONObject>();
	
	public WallAdapter(Context c) {
		this.context = c;
		try {
			myNotifications = LocalStorageService.instance().getNotifications();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {
		return myNotifications.size();
	}

	@Override
	public Object getItem(int arg0) {
		return myNotifications.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_wall, null);
        }
        
        JSONObject item = myNotifications.get(position);
        
        JSONObject sender = item.optJSONObject("sender");
        ((TextView)convertView.findViewById(R.id.name)).setText(sender.optString("displayName"));
        String notification = "";
        if(item.has("message")){
        	notification = item.optString("message");
        }
        else{
        	notification = item.optString("challenge");
        }
        ((TextView)convertView.findViewById(R.id.notification)).setText(notification);
        
        String picUrl = sender.optString("picUrl");
        ImageView profile = (ImageView) convertView.findViewById(R.id.sender_pic);
        Utils.loadImageFromUrl(profile, picUrl);
        
        return convertView;
	}

	public void loadNotifications() {
		AsyncApp42Service.instance().getMyNotifications(this);
	}

	@Override
	public void onSendNotification(int result) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onGetMessages(ArrayList<JSONObject> msgList) {
		if(msgList != null && msgList.size()>0){
			myNotifications.addAll(0, msgList);
			try {
				// save the new messages to local storage
				LocalStorageService.instance().addNotifications(msgList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			notifyDataSetChanged();
		}
	}

}
