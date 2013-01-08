package com.jforeach.mazegame;

import com.jforeach.mazegame.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.shephertz.app42leaderboard.App42Connect;

public class Menu extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button newGame = (Button)findViewById(R.id.bNew);
        Button exit = (Button)findViewById(R.id.bExit);
        newGame.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    public void onApp42LeaderboardClicked(View v){
    	App42Connect.launchApp42Connect(this, "Maze");
    }
    
	@Override
	public void onClick(View view) {
		//check which button was clicked with its id
		switch(view.getId()) {
			case R.id.bExit:
				finish();
				break;
			case R.id.bNew:		    	
				Intent game = new Intent(Menu.this,Game.class);  //create an Intent to launch the Game Activity
				Maze maze = MazeCreator.getMaze(1);    //use helper class for creating the Maze
				game.putExtra("maze", maze);			//add the maze to the intent which we'll retrieve in the Maze Activity
				startActivity(game);			
				break;
		}
	}
}