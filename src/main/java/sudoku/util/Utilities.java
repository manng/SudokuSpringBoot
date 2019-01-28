package sudoku.util;

import java.util.HashSet;
import java.util.Set;
import sudoku.items.Cell;
import sudoku.items.Constraints;

/**
 * This class contains static methods which are helpful during data input and
 * output.
 *
 * @author Gary
 */
public class Utilities {

 /**
 * Converts a Constraints object into an array of int values
 *
 * This conversion is needed at the output stage.
  * @param constraints      the Constraints object to be converted into ints
  * @return                 the final result as an array of ints
  * @throws ArrayIndexOutOfBoundsException
  */
    public static int[][] convertSolutionToArray(final Constraints constraints)
    throws ArrayIndexOutOfBoundsException {
        int dimSq = constraints.getDimSq();
        int[][] array = new int[dimSq][dimSq];
        for (Cell cell : constraints.getCells()) {
                for (Integer value : cell.getValues()) {
                    array[cell.getyCoord()][cell.getxCoord()]
                            = value.intValue();
                }
        }
        return array;
    }

/**
 * Converts an array of int values into a Constraints object
 *
 * This conversion is used at the data input stage.  Only non-zero values are
 * entered into the Constraints object.  A zero input value is taken to mean the
 * cell is unconstrained.
 *
 * @param array     the array of initial constraint values
 * @param dim       the dimension of the problem set
 * @return          the Constraints object
 */
    public static Constraints convertArrayToConstraints(final int[][]array,
                                                        final int dim) {
        int dimSq = dim * dim;
        Constraints constraints = new Constraints(dim);
        Set<Cell> cells = new HashSet<Cell>();
        for (int x = 0; x < dimSq; x++) {
            for (int y = 0; y < dimSq; y++) {
                Cell cell = new Cell(x, y);
                Set<Integer> values = new HashSet<Integer>();
                if (array[x][y] > 0) {
                    values.add(new Integer(array[x][y]));
                    cell.setValues(values);
                    cells.add(cell);
                }
            }
        }
        constraints.setCells(cells);
        return constraints;
    }

}
