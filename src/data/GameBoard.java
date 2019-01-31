package data;

import exception.IncorrectInputException;

public class GameBoard {

	private static String WRONG_DIM_ERROR_MSG = "Niepoprawny wymiar planszy";

	private boolean[][] board;
	private int size; // this is size of one side of board. GameBoard will have size*size cells

	public GameBoard(String n) {
		try {
			size = Integer.parseInt(n);
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(WRONG_DIM_ERROR_MSG);
		}
		board = new boolean[size][size];
	}

	public boolean[][] getBoard() {
		return board;
	}

	public void fillBoard(String busyFieldsList) {
		CoordinatesParser.parseAndFillManyMoves(busyFieldsList, board);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
