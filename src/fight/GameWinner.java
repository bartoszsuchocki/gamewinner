package fight;

import data.GameBoard;
import data.MoveWrapper;
import data.TreeNode;
import exception.NoPossibleMoveException;

public class GameWinner {

	private static String NO_POSSIBLE_MOVE_MSG = "Nie mozna wykonac wiecej ruchow!";

	private TreeNode moveTreeRoot;
	private GameBoard gameBoard;
	private boolean[][] simulationBoard;
	private boolean[][] tmpCalculationBoard; // only to save the state of simulationBoard sometimes
	private int whichTurnIsMyFirst; // if this program starts, it will be 0, otherwise 1
	private int actualMoveCounter;
	private long currentMoveTimeStamp;

	private final long TIME_FOR_MOVE_MILIS = 500;
	private final long TIME_FOR_CALCULATION = TIME_FOR_MOVE_MILIS - 15;

	public GameWinner(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		moveTreeRoot = new TreeNode(-1, -1, -2, -2); // this will be only parent for all possible moves, so it has not
														// existing coordinates
		actualMoveCounter = 0;
		tmpCalculationBoard = new boolean[gameBoard.getSize()][gameBoard.getSize()];
	}

	public void addOpponentMove(int x1, int y1, int x2, int y2) {
		gameBoard.getBoard()[x1][y1] = true;
		gameBoard.getBoard()[x2][y2] = true;

		if (simulationBoard != null) {
			simulationBoard[x1][y1] = true;
			simulationBoard[x2][y2] = true;
		}
		actualMoveCounter++;
	}

	public void addOpponentMove(MoveWrapper move) {
		addOpponentMove(move.getX1(), move.getY1(), move.getX2(), move.getY2());
	}

	public MoveWrapper generateMove() {
		currentMoveTimeStamp = System.currentTimeMillis();
		clearTree();
		generateAllPossibleMoves();
		TreeNode move = getMoveWithMaxSuccessfulPaths();

		gameBoard.getBoard()[move.getX1()][move.getY1()] = true;
		gameBoard.getBoard()[move.getX2()][move.getY2()] = true;

		simulationBoard[move.getX1()][move.getY1()] = true;
		simulationBoard[move.getX2()][move.getY2()] = true;

		actualMoveCounter++;

		return new MoveWrapper(move.getX1(), move.getY1(), move.getX2(), move.getY2());
	}

	private TreeNode getMoveWithMaxSuccessfulPaths() {

		if (moveTreeRoot.getChildren().size() == 0) {
			throw new NoPossibleMoveException(NO_POSSIBLE_MOVE_MSG);
		}

		TreeNode resultNode = moveTreeRoot.getChild(0);
		for (TreeNode child : moveTreeRoot.getChildren()) {
			if (child.getSuccessfulPaths() > resultNode.getSuccessfulPaths()) {
				resultNode = child;
			}
		}

		return resultNode;
	}

	private void generateAllPossibleMoves() {
		initSimulationBoard();
		moveTreeRoot = generateAllPossibleMovesInOneTurn(moveTreeRoot);

		long timeForEachChild = TIME_FOR_CALCULATION / moveTreeRoot.getChildren().size();
		for (TreeNode child : moveTreeRoot.getChildren()) {
			child.setSuccessfulPaths(
					generateAllPossibleMoves(child, actualMoveCounter, System.currentTimeMillis() + timeForEachChild));
		}

	}

	private void clearTree() {
		moveTreeRoot.getChildren().clear();
	}

	private void copySquareSameSizeTable(boolean[][] to, boolean[][] from) {
		for (int i = 0; i < from.length && i < to.length; i++) {
			for (int j = 0; j < from.length && i < to.length; j++) {
				to[i][j] = from[i][j];
			}
		}
	}

	private TreeNode generateAllPossibleMovesInOneTurn(TreeNode treeNode) {
		copySquareSameSizeTable(tmpCalculationBoard, simulationBoard);
		for (int i = 0; i < tmpCalculationBoard.length; i++) {
			for (int j = 0; j < tmpCalculationBoard.length; j++) {
				if (!tmpCalculationBoard[i][j]) {
					treeNode = generateMovesAfterCheckingNeighbours(treeNode, i, j);
					tmpCalculationBoard[i][j] = true;
				}
			}
		}
		return treeNode;
	}

	private int generateAllPossibleMoves(TreeNode treeNode, int moveCounter, long whenToStop) {
		if (System.currentTimeMillis() - currentMoveTimeStamp >= TIME_FOR_CALCULATION) {
			return 0;
		}

		treeNode = generateAllPossibleMovesInOneTurn(treeNode);

		if (treeNode.getChildren().size() == 0) {
			if ((moveCounter - whichTurnIsMyFirst) % 2 == 0) {
				return 1; // won - last turn was mine
			} else {
				return 0;
			}
		}

		simulationBoard[treeNode.getX1()][treeNode.getY1()] = true;
		simulationBoard[treeNode.getX2()][treeNode.getY2()] = true;

		int result = 0;

		long timeLeft = whenToStop - System.currentTimeMillis();
		for (TreeNode child : treeNode.getChildren()) {
			timeLeft = whenToStop - System.currentTimeMillis();
			if (timeLeft < 0) {
				break;
			}
			try {
				treeNode.addSuccessfulPaths(generateAllPossibleMoves(child, moveCounter + 1, whenToStop));
			} catch (StackOverflowError e) {
				return treeNode.getSuccessfulPaths();
			}
		}
		result = treeNode.getSuccessfulPaths();

		treeNode.getChildren().clear();
		// clearing move
		simulationBoard[treeNode.getX1()][treeNode.getY1()] = false;
		simulationBoard[treeNode.getX2()][treeNode.getY2()] = false;

		return result;
	}

	private TreeNode generateMovesAfterCheckingNeighbours(TreeNode treeNode, int x, int y) {
		boolean neighbour = true;
		neighbour = x - 1 >= 0 ? simulationBoard[x - 1][y] : true;// west neighbour
		if (!neighbour) {
			treeNode.addChild(new TreeNode(x, y, x - 1, y));
		}

		neighbour = x + 1 < simulationBoard.length ? simulationBoard[x + 1][y] : true;// east neighbour
		if (!neighbour) {
			treeNode.addChild(new TreeNode(x, y, x + 1, y));
		}

		neighbour = y - 1 >= 0 ? simulationBoard[x][y - 1] : true;// north neighbour
		if (!neighbour) {
			treeNode.addChild(new TreeNode(x, y, x, y - 1));
		}

		neighbour = y + 1 < simulationBoard.length ? simulationBoard[x][y + 1] : true;// south neighbour
		if (!neighbour) {
			treeNode.addChild(new TreeNode(x, y, x, y + 1));
		}

		// left or right opposite neighbour
		if (x == 0) {
			neighbour = simulationBoard[simulationBoard.length - 1][y];
			if (!neighbour) {
				treeNode.addChild(new TreeNode(x, y, simulationBoard.length - 1, y));
			}
		} else if (x == simulationBoard.length - 1) {
			neighbour = simulationBoard[0][y];
			if (!neighbour) {
				treeNode.addChild(new TreeNode(x, y, 0, y));
			}
		} else {
			neighbour = true;
		}

		// top or down opposite neighbour
		if (y == 0) {
			neighbour = simulationBoard[x][simulationBoard.length - 1];
			if (!neighbour) {
				treeNode.addChild(new TreeNode(x, y, x, simulationBoard.length - 1));
			}
		} else if (y == simulationBoard.length - 1) {
			neighbour = simulationBoard[x][0];
			if (!neighbour) {
				treeNode.addChild(new TreeNode(x, y, x, 0));
			}
		} else {
			neighbour = true;
		}

		return treeNode;
	}

	private void initSimulationBoard() {
		if (gameBoard == null) {
			return; // Mozna rzucic jakims wyjatkiem
		}
		simulationBoard = new boolean[gameBoard.getSize()][gameBoard.getSize()];
		for (int i = 0; i < gameBoard.getSize(); i++) {
			for (int j = 0; j < gameBoard.getSize(); j++) {
				simulationBoard[i][j] = gameBoard.getBoard()[i][j];
			}
		}
	}

	public TreeNode getMoveTreeRoot() {
		return moveTreeRoot;
	}

	public int getWhichTurnIsMyFirst() {
		return whichTurnIsMyFirst;
	}

	public void setWhichTurnIsMyFirst(int whichTurnIsMyFirst) {
		this.whichTurnIsMyFirst = whichTurnIsMyFirst;
	}

}
