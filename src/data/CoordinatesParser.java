package data;

import exception.IncorrectInputException;

public class CoordinatesParser {

	private static String ERROR_MSG = "Niepoprawny format wejscia!";

	public static MoveWrapper parseOneMove(String sourceText) { // format of sourceText must be {x1;y1},{x2;y2}

		int x1, y1, x2, y2;
		int commaPos;
		int curlyBraceClosePos;
		int semiColonPos;

		commaPos = sourceText.indexOf(",");
		String firstFieldCoordinates = sourceText.substring(0, commaPos);
		String secondFieldCoordinates = sourceText.substring(commaPos + 1);

		semiColonPos = firstFieldCoordinates.indexOf(";");
		curlyBraceClosePos = firstFieldCoordinates.indexOf("}");
		try {
			x1 = Integer.parseInt(firstFieldCoordinates.substring(1, semiColonPos));
			y1 = Integer.parseInt(firstFieldCoordinates.substring(semiColonPos + 1, curlyBraceClosePos));
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(ERROR_MSG);
		}

		semiColonPos = secondFieldCoordinates.indexOf(";");
		curlyBraceClosePos = secondFieldCoordinates.indexOf("}");
		try {
			x2 = Integer.parseInt(secondFieldCoordinates.substring(1, semiColonPos));
			y2 = Integer.parseInt(secondFieldCoordinates.substring(semiColonPos + 1, curlyBraceClosePos));
		} catch (NumberFormatException e) {
			throw new IncorrectInputException(ERROR_MSG);
		}

		MoveWrapper result = new MoveWrapper(x1, y1, x2, y2);
		return result;
	}

	public static void parseAndFillManyMoves(String sourceText, boolean[][] targetBoard) {// format of sourceText must
																							// be {x1;y1},{x2;y2}
																							// ...etc.
		StringBuilder xBuilder = new StringBuilder();
		StringBuilder yBuilder = new StringBuilder();
		boolean xPreparation = true; // true when parsing x coordinate and false when parsing y cordinate
		for (int i = 0; i < sourceText.length(); i++) {
			char c = sourceText.charAt(i);
			switch (c) {
			case ';':
				xPreparation = false;
				break;
			case ',':
				xPreparation = true;
				break;
			case '}':
				try {
					int x = Integer.parseInt(xBuilder.toString());
					int y = Integer.parseInt(yBuilder.toString());
					targetBoard[x][y] = true;
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					throw new IncorrectInputException(ERROR_MSG);
				}
				xBuilder.setLength(0);
				yBuilder.setLength(0);
				break;
			default:
				if (c != '{') {
					if (xPreparation) {
						xBuilder.append(c);
					} else {
						yBuilder.append(c);
					}
				}
			}
		}
	}
}
