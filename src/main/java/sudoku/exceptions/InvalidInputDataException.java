package sudoku.exceptions;

/**
 * This exception is thrown when the input constraints matrix is invalid.
 * 
 * @author Gary Mann
 *
 */
public class InvalidInputDataException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidInputDataException(String message) {
		super(message);
	}

}
