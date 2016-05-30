package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

import logic.BoardLogic;

public class NewGameListener implements ActionListener {
	
	private BoardLogic logic;
	private DrawingBoard drawingboard;
	JTextField width, height, mines;
	JFrame frame;
	
	public NewGameListener(BoardLogic logic, DrawingBoard drawingboard, JFrame frame) {
		this.logic = logic;
		this.drawingboard = drawingboard;
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		drawingboard.setSize();
		
		logic.newGame();
		
		frame.pack();
		
		drawingboard.repaint();
	}

}
