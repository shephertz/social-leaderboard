package com.jforeach.mazegame;

import java.util.Date;

import com.jforeach.mazegame.R;
import com.shephertz.app42leaderboard.App42Connect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

public class GameView extends View {
	
	//width and height of the whole maze and width of lines which
	//make the walls
	private int width, height, lineWidth;
	//size of the maze i.e. number of cells in it
	private int mazeSizeX, mazeSizeY;
	//width and height of cells in the maze
	float cellWidth, cellHeight;
	//the following store result of cellWidth+lineWidth 
	//and cellHeight+lineWidth respectively 
	float totalCellWidth, totalCellHeight;
	//the finishing point of the maze
	private int mazeFinishX, mazeFinishY;
	private Maze maze;
	private Activity context;
	private Paint line, red, background;
	private Date startDate, endDate;
	
	public GameView(Context context, Maze maze) {
		super(context);
		this.context = (Activity)context;
		this.maze = maze;
		mazeFinishX = maze.getFinalX();
		mazeFinishY = maze.getFinalY();
		mazeSizeX = maze.getMazeWidth();
		mazeSizeY = maze.getMazeHeight();
		line = new Paint();
		line.setColor(getResources().getColor(R.color.line));
		red = new Paint();
		red.setColor(getResources().getColor(R.color.position));
		background = new Paint();
		background.setColor(getResources().getColor(R.color.game_bg));
		setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.startDate = new Date();
		System.out.print("GameView constructed at "+ startDate.toString());
	}
	
	
	/*
	 * formula is 1000/time taken (sec)
	 */
	private long calcScore(){
		this.endDate = new Date();
		long diffSeconds = (endDate.getTime() - startDate.getTime())/1000;
		long score = 1000/diffSeconds;
		return score;
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = (w < h)?w:h;
		height = width;         //for now square mazes
		lineWidth = 1;          //for now 1 pixel wide walls
		cellWidth = (width - ((float)mazeSizeX*lineWidth)) / mazeSizeX;
		totalCellWidth = cellWidth+lineWidth;
		cellHeight = (height - ((float)mazeSizeY*lineWidth)) / mazeSizeY;
		totalCellHeight = cellHeight+lineWidth;
		red.setTextSize(cellHeight*0.75f);
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	protected void onDraw(Canvas canvas) {
		//fill in the background
		canvas.drawRect(0, 0, width, height, background);
		
		boolean[][] hLines = maze.getHorizontalLines();
		boolean[][] vLines = maze.getVerticalLines();
		//iterate over the boolean arrays to draw walls
		for(int i = 0; i < mazeSizeX; i++) {
			for(int j = 0; j < mazeSizeY; j++){
				float x = j * totalCellWidth;
				float y = i * totalCellHeight;
				if(j < mazeSizeX - 1 && vLines[i][j]) {
					//we'll draw a vertical line
					canvas.drawLine(x + cellWidth,   //start X
									y,               //start Y
									x + cellWidth,   //stop X
									y + cellHeight,  //stop Y
									line);
				}
				if(i < mazeSizeY - 1 && hLines[i][j]) {
					//we'll draw a horizontal line
					canvas.drawLine(x,               //startX 
									y + cellHeight,  //startY 
								    x + cellWidth,   //stopX 
								    y + cellHeight,  //stopY 
									line);
				}
			}
		}
		int currentX = maze.getCurrentX(),currentY = maze.getCurrentY();
		//draw the ball
		canvas.drawCircle((currentX * totalCellWidth)+(cellWidth/2),   //x of center
						  (currentY * totalCellHeight)+(cellWidth/2),  //y of center
						  (cellWidth*0.45f),                           //radius
						  red);
		//draw the finishing point indicator
		canvas.drawText("F",
						(mazeFinishX * totalCellWidth)+(cellWidth*0.25f),
						(mazeFinishY * totalCellHeight)+(cellHeight*0.75f),
						red);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent evt) {
		boolean moved = false;
		switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				moved = maze.move(Maze.UP);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				moved = maze.move(Maze.DOWN);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				moved = maze.move(Maze.RIGHT);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				moved = maze.move(Maze.LEFT);
				break;
			default:
				return super.onKeyDown(keyCode,evt);
		}
		if(moved) {
			//the ball was moved so we'll redraw the view
			invalidate();
			if(maze.isGameComplete()) {							
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(context.getText(R.string.finished_title));
				LayoutInflater inflater = context.getLayoutInflater();
				View view = inflater.inflate(R.layout.finish, null);
				builder.setView(view);
				View closeButton =view.findViewById(R.id.closeGame);
				closeButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View clicked) {
						if(clicked.getId() == R.id.closeGame) {
							App42Connect.submitScore(getContext(), calcScore(), "Maze", null, false);
							context.finish();
						}
						if(clicked.getId() == R.id.app42Leaderboard){
							App42Connect.submitScore(getContext(), calcScore(), "Maze", null, true);
						}
					}
				});
				View app42Button = view.findViewById(R.id.app42Leaderboard);
				app42Button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View clicked) {
						if(clicked.getId() == R.id.app42Leaderboard){
							App42Connect.submitScore(getContext(), calcScore(), "Maze", null, true);
						}
					}
				});				
				AlertDialog finishDialog = builder.create();
				finishDialog.show();
			}
		}
		return true;
	}
}
