package logic;

import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

public class BoardLogic {
	
	private Random random;
	private int width, height, mines;
	private int[][] mineLayout;
	private boolean[][] pressedLayout;
	private boolean[][] flagLayout;
	private boolean gameOver;
	private boolean buttonDown;
	private boolean firstTurn;
	private boolean win;
	private int buttonDownX, buttonDownY;
	private static final int[] X_OFFSET = {-1, 0, 1, -1, 1, -1, 0, 1};
	private static final int[] Y_OFFSET = {-1, -1, -1, 0, 0, 1, 1, 1};
	
	
	/** MineLayout:
	 * 0 = no mine
	 * 1-8 = number of surrounding mines
	 * 9 = mine
	 * 10 = unselected
	 * 11 = flagged
	 * 12 = mine that killed player
	 * 13 = wrongly flagged cells
	 * 
	 * X/Y Offset:
	 * selects mines surrounding current mine from top left to bottom right eg.
	 * 
	 * 1 2 3
	 * 4 X 5
	 * 6 7 8
	 */
	
	public BoardLogic(int width, int height, int mines) {
		this.width = width;
		this.height = height;
		this.mines = mines;
		this.random = new Random();
		this.buttonDown = false;
		
		//throw exception if there are more mines than squares
		if(mines>width*height) {
			throw new IllegalArgumentException("There are more mines than cells on board");
		}
		
		mineLayout = new int[width][height];
		pressedLayout = new boolean[width][height];
		flagLayout = new boolean[width][height];
	}
	
	//returns width and height
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getMines() {
		return mines;
	}
	
	public void setNewDimensions(int width, int height, int mines) {
		//if user changes dimensions in the settings panel
		
		//throw exception if there are more mines than squares
		if(mines>=width*height) {
			throw new IllegalArgumentException("There are more mines than cells on board");
		}
		
		this.width = width;
		this.height = height;
		this.mines = mines;
		this.buttonDown = false;
		
		//reset game board
		mineLayout = new int[width][height];
		pressedLayout = new boolean[width][height];
		flagLayout = new boolean[width][height];
	}
	
	public void newGame() {
		//initialises mines randomly and resets all pressed squares
		
		firstTurn = true;
		gameOver = false;
		win = false;
		
		//reset all arrays
		for(boolean[] row : pressedLayout) {
			Arrays.fill(row, false);
		}
		for(int[] row : mineLayout) {
			Arrays.fill(row, 0);
		}
		for(boolean[] row : flagLayout) {
			Arrays.fill(row, false);
		}
		
		initialiseMines();
		initialiseNumbers();
	}
	
	private void initialiseMines() {
		//Adds specified number of mines to the board, randomly
		
		for(int i=0; i<mines; i++) {
			
			//get a random x and y coordinate
			boolean successful;
			do {
				successful = false;
				
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				
				//if there is no mine, add mine and iterate to next loop
				if(mineLayout[x][y] != 9 ) {
					mineLayout[x][y] = 9;
					successful = true;
				}
				
			} while(!successful);
		}
	}
	
	private void initialiseNumbers() {
	//Once board has mines, add all numbers to the board
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				
				//if it is not a mine, get number of neighbouring mines and replace value with it
				if(mineLayout[j][i] != 9) {
					mineLayout[j][i] = getNeighbours(j, i);
				}
			}
		}
	}
	

	private int getNeighbours(int x, int y) {
		//counts number of neighbouring mines
		
		int noOfNeighbours = 0;
		
		for(int i=0; i<X_OFFSET.length; i++) {
			
			//get offset values 
			int offsetX = x + X_OFFSET[i];
			int offsetY = y + Y_OFFSET[i];
			
			//bounds checking if it is outside the grid
			if(offsetX < 0 || offsetY < 0 || offsetX >= width || offsetY >= height) {
				continue; //don't check array, continue to next loop iteration
			}
			
			//check if is a mine, and add to counter
			if(mineLayout[offsetX][offsetY] == 9) {
				noOfNeighbours++;
			}
		}
		return noOfNeighbours;
	}
	
	public void printBoard() { //for testing
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				if(flagLayout[j][i]){
					System.out.print("F ");
				} else {
					System.out.print(". ");
				}

			}
			System.out.println("");
		}
	}
	
	public int[][] getBoard() {
		/** combines both boards and outputs layout
		 * 
		 *  if mousebutton is down on a cell, it will output the same board, but with 
		 *  the cell depressed for showing visual feedback to the user.
		 */
		
		//create new board and pull values from the pressedLayout and mineLayout
		int[][] combinedLayout = new int[width][height];
		
		//while still in game
		if (!gameOver && !win) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (pressedLayout[j][i]) {
						combinedLayout[j][i] = mineLayout[j][i];
					} else if (flagLayout[j][i]) {
						combinedLayout[j][i] = 11;
					} else {
						combinedLayout[j][i] = 10;
					}
				}
			}
			//returns board with depressed button (don't show graphic if game is won)
			if (buttonDown && !pressedLayout[buttonDownX][buttonDownY] && !flagLayout[buttonDownX][buttonDownY]) {
				//if mousebutton is down, and cell has not been pressed or flagged before, set graphics to mousedown graphic
				combinedLayout[buttonDownX][buttonDownY] = 0;
				
			//if game over, show all mines
			} 
			
		//if game over, show all mines
		} else if(gameOver && !win) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (mineLayout[j][i] != 9 && flagLayout[j][i]) { //first fill in wrongly marked mines
						combinedLayout[j][i] = 13;
					} else if (mineLayout[j][i] == 9 && flagLayout[j][i]) { //then fill in correctly marked mines
						combinedLayout[j][i] = 11;
					} else if (mineLayout[j][i] == 9) { //then mark all remaining mines
						combinedLayout[j][i] = 9;
					} else if (pressedLayout[j][i]) {
						combinedLayout[j][i] = mineLayout[j][i];
					} else {
						combinedLayout[j][i] = 10;
					}
				}
			}
			
		//if game over AND win, show all mines as flags
		} else if(win) {
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (mineLayout[j][i] == 9) {
						combinedLayout[j][i] = 11;
					} else if (pressedLayout[j][i]) {
						combinedLayout[j][i] = mineLayout[j][i];
					} else if (flagLayout[j][i]) {
						combinedLayout[j][i] = 11;
					} else {
						combinedLayout[j][i] = 10;
					}
				}
			}
		}
		
		return combinedLayout;
	}
	
	public void performTurn(int x, int y) {
		//this method returns false if game has not won yet

		do {
			// if game over or game has won, do nothing
			if (gameOver == true || win == true) {
				return;
			}
			// if player clicks already activated cell or flagged cell, nothing
			// happens
			if (pressedLayout[x][y] || flagLayout[x][y]) {
				return;
			}
			// change it to true (set as pressed)
			pressedLayout[x][y] = true;
			int currentCell = mineLayout[x][y];
			// if it is a mine, end the game;
			if (currentCell == 9) {
				gameOver = true;
				mineLayout[x][y] = 12;
			}
			// if it is a blank square, perform turn on all surrounding cells
			if (currentCell == 0) {

				for (int i = 0; i < X_OFFSET.length; i++) {

					// get offset values
					int offsetX = x + X_OFFSET[i];
					int offsetY = y + Y_OFFSET[i];

					// bounds checking if it is outside the grid
					if (offsetX < 0 || offsetY < 0 || offsetX >= width || offsetY >= height) {
						continue; // don't check array, continue to next loop
									// iteration
					}

					// use method on the surrounding squares
					this.performTurn(offsetX, offsetY);
				}
			}
			// if player loses on first turn, regenerate all mines and try again
			// (not very nice for player to click on mine and die)
			if (gameOver && firstTurn) {
				this.newGame();
				continue;
			} 
			
			firstTurn = false;

		} while (firstTurn);
		
		
		
		//if the move is the last move that wins the game
		if(numberOfUnplayedCellsLeft() == mines && gameOver == false) {
			win = true;
			gameOver = true;
			
			//show dialog if game has been won
			JOptionPane.showMessageDialog(null, "You've won!");
				
			return;
		}		
	}
	
	private int numberOfUnplayedCellsLeft() {
		//used to check if player has pressed everything that isn't a mine
		int counter = 0;
		for(int i=0; i < height; i++) {
			for(int j=0; j<width; j++) {
				if(!pressedLayout[j][i]) {
					counter++;
				}
			}
		}
		return counter;
	}
	
	public void flagCell(int x, int y) {
		
		//don't allow user to flag when game over
		if(gameOver) {
			return;
		}
		
		//only flag if cell is not uncovered yet
		if (!pressedLayout[x][y]) {
			flagLayout[x][y] = !flagLayout[x][y];
		}
	}
	
	public void buttonDown(int x, int y) {
		buttonDown = true;
		buttonDownX = x;
		buttonDownY = y;
	}
	
	public void buttonUp() {
		buttonDown = false;
	}

	public void performSurroundTurn(int x, int y) {
		this.performTurn(x, y);
		
		if (getNeighbourFlags(x, y) == getNeighbours(x, y)) {
			//perform turn on surrounding only if conditions are valid
			for (int i = 0; i < X_OFFSET.length; i++) {

				//get offset values 
				int offsetX = x + X_OFFSET[i];
				int offsetY = y + Y_OFFSET[i];

				//bounds checking if it is outside the grid
				if (offsetX < 0 || offsetY < 0 || offsetX >= width || offsetY >= height) {
					continue; //don't check array, continue to next loop iteration
				}

				//use method on the surrounding squares that are not flagged
				if(!flagLayout[offsetX][offsetY]) {
					this.performTurn(offsetX, offsetY);
				}
			} 
		}
		
	}

	private int getNeighbourFlags(int x, int y) {
		//counts number of neighbouring flags
		
		int noOfNeighbours = 0;
		
		for(int i=0; i<X_OFFSET.length; i++) {
			
			//get offset values 
			int offsetX = x + X_OFFSET[i];
			int offsetY = y + Y_OFFSET[i];
			
			//bounds checking if it is outside the grid
			if(offsetX < 0 || offsetY < 0 || offsetX >= width || offsetY >= height) {
				continue; //don't check array, continue to next loop iteration
			}
			
			//check if is a flag, and add to counter
			if(flagLayout[offsetX][offsetY]) {
				noOfNeighbours++;
				
			}
		}
		return noOfNeighbours;
	}
}
