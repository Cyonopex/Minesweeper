package ui;

import javax.swing.SwingUtilities;

import logic.BoardLogic;

public class Main {
	public static void main(String[] args) {
		BoardLogic logic = new BoardLogic(30, 16, 99);
		logic.newGame();
		UserInterface ui = new UserInterface(logic);
		SwingUtilities.invokeLater(ui);
	}
}
