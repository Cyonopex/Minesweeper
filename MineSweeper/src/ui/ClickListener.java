package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import logic.BoardLogic;

public class ClickListener extends MouseAdapter {
	
	private BoardLogic board;
	private DrawingBoard drawingboard;
	int mouseDownX, mouseDownY;
	boolean leftClick, rightClick, bothClicked;
	
	public ClickListener(BoardLogic board, DrawingBoard drawingboard) {
		this.board = board;
		this.drawingboard = drawingboard;
		leftClick = false;
		rightClick = false;
		bothClicked = false; //to check if both left and right buttons have been pressed together

	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		//get cell that was pressed
		mouseDownX = e.getX()/UserInterface.CELL_SIZE;
		mouseDownY = e.getY()/UserInterface.CELL_SIZE;
		
		//reject if click is outside cell
		if(mouseDownX >= board.getWidth() || mouseDownY >= board.getHeight() || mouseDownX <0 || mouseDownY < 0) {
			return;
		}
		
		//if user did left click, show BUTTONDOWN graphic
		if(e.getButton() == MouseEvent.BUTTON1) {
			board.buttonDown(mouseDownX, mouseDownY);
			
			//set leftclick to true
			leftClick = true;
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			//set rightclick to true
			rightClick = true;
		}
		

		drawingboard.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		//undo the BUTTONDOWN graphic
		board.buttonUp();
		
		drawingboard.repaint();
		
		//get position of X and Y
		int x = e.getX();
		int y = e.getY();
		
		//above values are pixel values, convert to actual cell in array
		int mouseUpX = x/UserInterface.CELL_SIZE;
		int mouseUpY = y/UserInterface.CELL_SIZE;
		
		//if click is out of range, reject
		if(mouseUpX >= board.getWidth() || mouseUpY >= board.getHeight() || mouseUpX <0 || mouseUpY < 0) {
			return;
		}
		
		//check if mouse has moved to a different cell
		if(mouseDownX == mouseUpX && mouseDownY == mouseUpY) {
			
			//if both left and right buttons are clicked, perform move on all surrounding mines (if possible)
			
			if(leftClick && rightClick) {
				board.performSurroundTurn(mouseUpX, mouseUpY);
				
			} else if (e.getButton() == MouseEvent.BUTTON1) {
				board.performTurn(mouseUpX, mouseUpY);
				
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				board.flagCell(mouseUpX, mouseUpY);
			}
		}
		
		//set left/rightclick to false
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftClick = false;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			rightClick = false;
		}
		
		drawingboard.repaint();
	}

}
