/**
 * 
 */
package com.shephertz.app42leaderboard;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.shephertz.app42leaderboard.AsyncApp42Service.App42LeaderBoardListener;
import com.shephertz.app42leaderboard.FacebookService.FacebookFriendListRequester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author dhruvchopra
 *
 */
public class FriendsActivityAdapter extends BaseAdapter implements FacebookFriendListRequester, App42LeaderBoardListener {

	private ArrayList<JSONObject> feedList = new ArrayList<JSONObject>();
	private Context context;
	
	public FriendsActivityAdapter(Context owner){
		context = owner;
	}
	
	public void loadFeed(){
		feedList.clear();
		FacebookService.instance().getFacebookFriends(this);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feedList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return feedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_friendsfeed, null);
        }
        
        JSONObject item = feedList.get(position);
        String name = item.optString("name");
        ((TextView)convertView.findViewById(R.id.name)).setText(name);
        
        String activity = item.optString("activity");
        ((TextView)convertView.findViewById(R.id.feed)).setText(activity);
        
        String time = item.optString("time");
        ((TextView)convertView.findViewById(R.id.time)).setText(time);
        
        String picUrl = item.optString("picUrl");
        ImageView profile = (ImageView) convertView.findViewById(R.id.profile_pic);
        Utils.loadImageFromUrl(profile, picUrl);
        
        return convertView;
	}

	@Override
	public void onListFetched(ArrayList<JSONObject> gameFriends) {
		if(gameFriends == null || gameFriends.size() <= 0){
			((FriendsActivity)context).handleNoFriends();
			return;
		}
		// make array of friends in to array of usernames
		for(int i=0; i<gameFriends.size(); i++){
			JSONObject friendObj = gameFriends.get(i);
			String username = friendObj.optString("id");
			String name = friendObj.optString("name");
			
			AsyncApp42Service.instance().getLastActivityOfUser(i, username, this);
			
			JSONObject feedObj = new JSONObject();
			try {
	        	JSONObject picObj = friendObj.getJSONObject("picture");
	            JSONObject dataObj = picObj.getJSONObject("data");
	            String picUrl = dataObj.getString("url");			
				feedObj.put("picUrl", picUrl);
				feedObj.put("name", name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			feedList.add(feedObj);
		}		
		notifyDataSetChanged();
		
	}

	@Override
	public void onGetTopRankings(ArrayList<JSONObject> scoreDataList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetLastActivityOfUser(String gameName, String points, String time, int pos) {
		JSONObject item = feedList.get(pos);
		try {
			if(points.length() >0){
				item.put("activity", points+" points in "+gameName);
			}
			else{
				item.put("activity", "not yet started playing");
			}
			item.put("time", time);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyDataSetChanged();
		
	}

	@Override
	public void onGetTopRankingsToday(ArrayList<JSONObject> scoreDataList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetTopRankingsInGroup(ArrayList<JSONObject> scoreDataList) {
		// TODO Auto-generated method stub
		
	}

}
