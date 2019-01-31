package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import data.CoordinatesParser;
import data.GameBoard;
import fight.GameWinner;

public class MainGameFlow {

	private static String ERROR_MSG = "Nie udalo sie rozpoczac gry.";

	// {1;1},{2;4},{4;2},{4;5},{4;6}
	public static void main(String[] args) {

		GameBoard gameBoard;
		GameWinner gameWinner;
		String judgeResponse;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			judgeResponse = reader.readLine();
			gameBoard = new GameBoard(judgeResponse);
			System.out.println("ok");

			judgeResponse = reader.readLine();
			gameBoard.fillBoard(judgeResponse);
			gameWinner = new GameWinner(gameBoard);
			System.out.println("ok");

			judgeResponse = reader.readLine();
			if (!judgeResponse.equals("start")) {
				gameWinner.addOpponentMove(CoordinatesParser.parseOneMove(judgeResponse));
			}
			System.out.println(gameWinner.generateMove());

			while ((judgeResponse = reader.readLine()) != null) {
				gameWinner.addOpponentMove(CoordinatesParser.parseOneMove(judgeResponse));
				System.out.println(gameWinner.generateMove());
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(ERROR_MSG);
		}

	}

}
