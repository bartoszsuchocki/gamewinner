package exception;

public class NoPossibleMoveException extends RuntimeException {
	public NoPossibleMoveException(String message) {
		super(message);
	}
}
