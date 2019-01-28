package sudoku.exceptions;

/**
 * This Exception is thrown whenever the program attempts to set a cell to a
 * value which is forbidden by the Constraint object.
 *
 * Note that this does not mean the code has failed, it simply means that the
 * current iteration is wrong and should be rejected.
 *
 * @author Gary Mann
 */
public class BadCellException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
