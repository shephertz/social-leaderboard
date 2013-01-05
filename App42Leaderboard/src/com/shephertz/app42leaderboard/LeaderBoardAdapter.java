package com.shephertz.app42leaderboard;

import java.util.ArrayList;

import org.json.JSONObject;

import com.shephertz.app42leaderboard.AsyncApp42Service.App42LeaderBoardListener;
import com.shephertz.app42leaderboard.Constants.RankingMode;
import com.shephertz.app42leaderboard.FacebookService.FacebookFriendListRequester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaderBoardAdapter extends BaseAdapter implements App42LeaderBoardListener, FacebookFriendListRequester {

	private ArrayList<JSONObject> topScoresAllTime = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> topScoresToday = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> topScoresFriends = new ArrayList<JSONObject>();
	
	private Context context;
	
	private RankingMode activeMode;
	
	LeaderBoardAdapter(Context c){
		this.context = c;
		activeMode = RankingMode.AllTime;
	}
	
	@Override
	public int getCount() {	
		if(activeMode == RankingMode.AllTime){
			return topScoresAllTime.size();
		}
		else if(activeMode == RankingMode.Today){
			return topScoresToday.size();
		}
		else{
			return topScoresFriends.size();
		}
	}

	public void resetMode(RankingMode mode){
		activeMode = mode;
		notifyDataSetChanged();
	}
	
	@Override
	public Object getItem(int arg0) {
		if(activeMode == RankingMode.AllTime){
			return topScoresAllTime.get(arg0);
		}
		else if(activeMode == RankingMode.Today){
			return topScoresToday.get(arg0);
		}
		else{
			return topScoresFriends.get(arg0);
		}
		
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
            convertView = vi.inflate(R.layout.item_score, null);
        }
        
        JSONObject item;
        if(activeMode == RankingMode.AllTime){
        	item = topScoresAllTime.get(position);
        }
        else if(activeMode == RankingMode.Today){
        	item = topScoresToday.get(position);
        }
        else {
        	item = topScoresFriends.get(position);
        }
        if (item != null) {
        	TextView tvScorerName = (TextView) convertView.findViewById(R.id.name);
        	TextView tvScore = (TextView) convertView.findViewById(R.id.score);
        	ImageView picture = (ImageView) convertView.findViewById(R.id.profile_pic);
        	
        	tvScorerName.setText(Integer.valueOf(position) + 1 + ". "+ item.optString("DisplayName")); 
        	tvScore.setText(item.optString("Score") + " points");
        	Utils.loadImageFromUrl(picture, item.optString("PicUrl"));
        }
        return convertView;	
	}

	@Override
	public void onGetTopRankings(ArrayList<JSONObject> topScoreObjs) {
		if(topScoreObjs != null){
			this.topScoresAllTime = topScoreObjs;
			if(activeMode == RankingMode.AllTime){
				notifyDataSetChanged();
			}
		}
	}

	public void reloadScores() {
		AsyncApp42Service.instance().getTopRankings(App42Connect.currentGameName, this);
		AsyncApp42Service.instance().getTopRankingsToday(App42Connect.currentGameName, this);
		FacebookService.instance().getFacebookFriends(this);
		
		// clear up existing scores		
		topScoresAllTime.clear();
		topScoresToday.clear();
		topScoresFriends.clear();
		
		notifyDataSetChanged();
	}

	@Override
	public void onGetLastActivityOfUser(String gameName, String points, String time, int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetTopRankingsToday(ArrayList<JSONObject> scoreDataList) {
		if(scoreDataList != null){
			this.topScoresToday = scoreDataList;
			if(activeMode == RankingMode.Today){
				notifyDataSetChanged();
			}
		}
		
	}

	@Override
	public void onGetTopRankingsInGroup(ArrayList<JSONObject> scoreDataList) {
		if(scoreDataList != null){
			this.topScoresFriends = scoreDataList;
			if(activeMode == RankingMode.Friends){
				notifyDataSetChanged();
			}
		}		
	}

	@Override
	public void onListFetched(ArrayList<JSONObject> gameFriends) {

		if(gameFriends == null){
			return;
		}
		// make array of friends in to array of usernames
		ArrayList<String> friendsGroup = new ArrayList<String>();
		for(int i=0; i<gameFriends.size(); i++){
			JSONObject friendObj = gameFriends.get(i);
			String username = friendObj.optString("id");
			friendsGroup.add(username);
		}
		friendsGroup.add(UserContext.MyUserName);
		AsyncApp42Service.instance().getTopRankingsInGroup(App42Connect.currentGameName, friendsGroup, this);
	}

}
