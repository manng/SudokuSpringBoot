package sudoku.exceptions;

public class UnknownXMLElementException extends Exception {
	
/**
 * This exception is thrown if the XML file contains an unrecognized element.
 * 
 * @author Gary Mann
 */
	private static final long serialVersionUID = 1L;

	public  UnknownXMLElementException(String message){
		super(message);
	}

}
