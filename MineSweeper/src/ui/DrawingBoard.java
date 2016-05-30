package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import logic.BoardLogic;

public class DrawingBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Image[] img;
	private BoardLogic logic;
	
	public DrawingBoard(Image[] img, BoardLogic logic) {
		this.img = img;
		this.logic = logic;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		int[][] board = logic.getBoard();
		
		//iterate through the board and draw images for state of current board
		for(int i=0; i<logic.getHeight(); i++) {
			for(int j=0; j<logic.getWidth(); j++) {
				int cell = board[j][i];
				graphics.drawImage(img[cell], j*UserInterface.CELL_SIZE, i*UserInterface.CELL_SIZE, null);
			}
		}
	}
	
	public void setSize() {
		super.setPreferredSize(new Dimension(UserInterface.CELL_SIZE * logic.getWidth() , UserInterface.CELL_SIZE * logic.getHeight()));
	}
}
