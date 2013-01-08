package com.jforeach.mazegame;

public class MazeCreator {
	
	public static Maze getMaze(int mazeNo) {
		Maze maze = null;
		if(mazeNo == 1) {
			maze = new Maze();
			boolean[][] vLines = new boolean[][]{
									{true ,false,false,false,true ,false,false},
									{true ,false,false,true ,false,true ,true },
									{false,true ,false,false,true ,false,false},
									{false,true ,true ,false,false,false,true },
									{true ,false,false,false,true ,true ,false},
									{false,true ,false,false,true ,false,false},
									{false,true ,true ,true ,true ,true ,false},
									{false,false,false,true ,false,false,false}
								 };
			boolean[][] hLines = new boolean[][]{
									{false,false,true ,true ,false,false,true ,false},
									{false,false,true ,true ,false,true ,false,false},
									{true ,true ,false,true ,true ,false,true ,true },
									{false,false,true ,false,true ,true ,false,false},
									{false,true ,true ,true ,true ,false,true ,true },
									{true ,false,false,true ,false,false,true ,false},
									{false,true ,false,false,false,true ,false,true }
								};
			maze.setVerticalLines(vLines);
			maze.setHorizontalLines(hLines);
			maze.setStartPosition(0, 0);
			maze.setFinalPosition(7, 7);
		}
		if(mazeNo == 2) {
			maze = new Maze();
			boolean[][] vLines = new boolean[][]{
									{false,false,false,true ,false,false,false},
									{false,false,true ,false,true ,false,false},
									{false,false,true ,true ,false,false,false},
									{false,false,true ,true ,true ,false,false},
									{false,false,true ,false,true ,false,false},
									{true ,false,false,true ,false,true ,false},
									{true ,false,true ,true ,false,false,false},
									{false,false,true ,false,false,false,true }
								 };
			boolean[][] hLines = new boolean[][]{
									{false,true ,true ,true ,false,true ,true ,true },
									{true ,true ,false,false,true ,true ,true ,false},
									{false,true ,true ,false,false,false,true ,true },
									{true ,true ,false,false,false,true ,true ,false},
									{false,true ,true ,true ,true ,false,true ,false},
									{false,false,true ,false,false,true ,true ,true },
									{false,true ,false,false,true ,true ,false,false}
								};
			maze.setVerticalLines(vLines);
			maze.setHorizontalLines(hLines);
			maze.setStartPosition(0, 7);
			maze.setFinalPosition(7, 0);
		}
		if(mazeNo == 3) {
			maze = new Maze();
			boolean[][] vLines = new boolean[][]{
									{false,false,true ,false,false,false,true ,false,false,false,false,false},
									{false,true ,false,false,false,true ,false,false,false,false,true ,true },
									{true ,false,false,false,false,true ,false,false,false,false,true ,true },
									{true ,true ,false,false,false,true ,true ,true ,false,false,true ,true },
									{true ,true ,true ,false,false,true ,true ,false,true ,false,true ,true },
									{false,true ,true ,true ,false,true ,false,false,false,true ,false,false},
									{false,false,false,true ,false,true ,false,true ,false,false,false,false},
									{false,false,true ,false,true ,false,true ,true ,false,true ,false,false},
									{true ,true ,true ,true ,false,true ,true ,false,false,true ,false,false},
									{false,false,false,true ,false,false,true ,true ,false,true ,true ,false},
									{false,false,true ,false,true ,false,true ,false,false,false,false,false},
									{true ,true ,true ,true ,true ,true ,true ,false,false,true ,false,false},
									{false,false,true ,false,false,true ,false,false,false,false,true ,false}
								 };
			boolean[][] hLines = new boolean[][]{
									{true ,false,false,true ,true ,false,false,false,true ,true ,true ,true ,false},
									{false,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,false},
									{false,false,true ,true ,true ,false,false,true ,true ,true ,true ,false,false},
									{false,false,false,true ,true ,true ,false,false,false,true ,false,false,false},
									{false,false,false,false,true ,false,false,true ,true ,true ,false,false,false},
									{true ,true ,false,false,false,true ,true ,true ,true ,false,true ,true ,true },
									{false,true ,true ,true ,true ,true ,false,false,false,true ,true ,true ,false},
									{true ,false,false,false,true ,false,true ,false,true ,false,false,true ,true },
									{false,true ,false,false,false,true ,false,true ,true ,true ,true ,true ,false},
									{true ,true ,false,true ,false,true ,true ,false,false,true ,false,true ,false},
									{false,true ,true ,false,true ,false,false,true ,true ,false,true ,true ,true },
									{false,true ,false,false,true ,false,false,true ,true ,true ,false,false,true }
								};
			maze.setVerticalLines(vLines);
			maze.setHorizontalLines(hLines);
			maze.setStartPosition(0, 0);
			maze.setFinalPosition(12, 12);
		}
		return maze;
	}
}
