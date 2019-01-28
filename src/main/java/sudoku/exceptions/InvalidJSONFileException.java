package sudoku.exceptions;

public class InvalidJSONFileException extends Exception {
	
/**
 * This exception is thrown when the content of a JSON input file is invalid. 
 *
 * @author Gary Mann
 */
	private static final long serialVersionUID = 1L;

	public InvalidJSONFileException(String message) {
		super(message);
	}

}
