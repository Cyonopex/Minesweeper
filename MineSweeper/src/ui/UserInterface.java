package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import logic.BoardLogic;

public class UserInterface implements Runnable {
	

	private JFrame frame;
	private Image[] img;
	private BoardLogic board;
	private int width, height; //number of mines in x and y axis
	private static final int NUM_IMAGES = 14;
	public static final int CELL_SIZE = 20; //size of cell in px
	
	
	public UserInterface(BoardLogic board) {
		this.width = board.getWidth();
		this.height = board.getHeight();
		this.board = board;
		
		this.loadImages();
		
		
	}
	
	@Override
	public void run() {
	    try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    }
	    
		frame = new JFrame("Minesweeper");
		frame.setPreferredSize(new Dimension((width+1)*CELL_SIZE,(height+4)*CELL_SIZE));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		createComponents(frame.getContentPane());
		
		frame.pack();
		frame.setVisible(true);
	}
	

	
	private void createComponents(Container container) {

		//create drawingBoard(inherits JPanel) to draw mines
		DrawingBoard drawingBoard = new DrawingBoard(img, board);
		drawingBoard.setPreferredSize(new Dimension(CELL_SIZE*width, CELL_SIZE*height));
		drawingBoard.addMouseListener(new ClickListener(board, drawingBoard));
		
		container.add(drawingBoard);
		
		//create JPanel below board to hold buttons
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		
		//create both buttons
		JButton newGameButton = new JButton("New Game");
		JButton optionsButton = new JButton("Options");
			
		//add listener to buttons

		newGameButton.addActionListener(new NewGameListener(board, drawingBoard, frame));
		optionsButton.addActionListener(new OptionButtonListener(drawingBoard));
		
		//add everything to panel, then add to container
		panel.add(newGameButton);
		panel.add(optionsButton);
		container.add(panel, BorderLayout.SOUTH);
	}

	private class OptionButtonListener implements ActionListener {
		
		DrawingBoard drawingboard;
		
		public OptionButtonListener(DrawingBoard drawingboard) {
			this.drawingboard = drawingboard;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			SettingsDialog dialog = new SettingsDialog(board, frame, drawingboard);
			dialog.showDialog();
		}
		
	}

	
	private void loadImages() {
		img = new Image[NUM_IMAGES];
		
		try {
		//loop through all images
		for(int i=0; i<NUM_IMAGES; i++) {
			URL url = UserInterface.class.getResource("/img/"+i+".png");
			img[i] = new ImageIcon(url).getImage();
		} 
		
		} catch (Exception e) {
				System.out.println("Unable to load images");
				e.printStackTrace();
		}
	}
}
