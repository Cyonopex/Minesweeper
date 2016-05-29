package ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.BoardLogic;

public class SettingsDialog {
	
	BoardLogic logic;
	JFrame frame;
	DrawingBoard drawingboard;
	
	public SettingsDialog(BoardLogic logic, JFrame frame, DrawingBoard drawingboard) {
		this.drawingboard = drawingboard;
		this.frame = frame;
		this.logic = logic;
	}
	
	public void showDialog() {
		JPanel settings = new JPanel();
		GridLayout layout = new GridLayout(3,3);
		settings.setLayout(layout);
		
		//Initialise labels
		JLabel width = new JLabel("Width");
		JLabel height = new JLabel("Height");
		JLabel mines = new JLabel("Mines");
		
		//initialise inputfields
		JTextField widthInput = new JTextField(logic.getWidth()+"");
		JTextField heightInput = new JTextField(logic.getHeight()+"");
		JTextField mineInput = new JTextField(logic.getMines()+"");
		
		//add all to the JPanel
		settings.add(width);
		settings.add(widthInput);
		settings.add(height);
		settings.add(heightInput);
		settings.add(mines);
		settings.add(mineInput);
		
		int result = JOptionPane.showConfirmDialog(null, settings, "Options", JOptionPane.OK_CANCEL_OPTION);
		
		if(result == JOptionPane.OK_OPTION) {
			
			//if user inputs non-integers, just use old value
			//store old values first
			int oldWidth = logic.getWidth();
			int oldHeight = logic.getHeight();
			int oldMines = logic.getMines();
			
			//attempt to parse String inputs as integers
			int newWidth, newHeight, newMines;
			
			try {
				newWidth = Integer.parseInt(widthInput.getText());
				newHeight = Integer.parseInt(heightInput.getText());
				newMines = Integer.parseInt(mineInput.getText());
				
			} catch (Exception e) {
				
				newWidth = oldWidth;
				newHeight = oldHeight;
				newMines = oldMines;
				//relabel the JTextFields with old numbers
				width.setText(oldWidth+"");
				height.setText(oldHeight+"");
				mines.setText(oldMines+"");
			}
			
			//if mines are more than available blocks, this method will throw an exception
			try {
				logic.setNewDimensions(newWidth, newHeight, newMines);
			} catch (Exception e) {
				//reset fields to original settings
				width.setText(oldWidth+"");
				height.setText(oldHeight+"");
				mines.setText(oldMines+"");
			}
			
			logic.newGame();
			
			frame.setPreferredSize(new Dimension((logic.getWidth()+1)*UserInterface.CELL_SIZE,(logic.getHeight()+4)*UserInterface.CELL_SIZE));
			
			frame.pack();
			
			drawingboard.repaint();

		}
	}
}
