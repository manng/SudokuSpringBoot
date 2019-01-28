package sudoku.items;

import java.util.HashSet;
import java.util.Set;

/**
 * This class stores the current solution.  It mainly stores a Set of possible
 * values for each cell.
 *
 * @author Gary
 */
public class CurrentSolution {

    private Set<Cell> cells;
    private int dimSq;
    private int dim;

/**
 * Constructs an empty object for a given problem size.
 *
 * @param dim       the size of the current problem
 */
    public CurrentSolution(final int dim){
        this.dim = dim;
        cells = new HashSet<Cell>();
        dimSq = dim * dim;
        for (int i = 0; i < dimSq; i++) {
            for (int j = 0; j < dimSq; j++) {
                Cell cell = new Cell(i, j);
                cells.add(cell);
            }
        }
    }

/**
 * Constructs a new CurrentSolution object from an existing one.
 *
 * @param currentSolution   the CurrentSolution object whose cell values are
 *                          stored in the current one
 */
    public CurrentSolution(final CurrentSolution currentSolution) {
        this(currentSolution.getDim());
        cells = new HashSet<Cell>();
        for (Cell cell : currentSolution.getCells()) {
            cells.add(cell);
        }
    }

/**
 * Resets the current solution based on the given Constraints.  Cells which are
 * not constrained can take all possible values
 *
 * @param constraints   the Constraints object used to initialise this solution
 */
    public void reset(Constraints constraints) {
        Set<Cell> blankCells = new HashSet<Cell>();
        for (int i = 0; i < dimSq; i++) {
            for (int j = 0; j < dimSq; j++) {
                Cell cell = new Cell(i, j);
                Set<Integer> values = new HashSet<Integer>();
                for (int z = 1; z <= dimSq; z++) {
                    Integer value = new Integer(z);
                    values.add(value);
                }
                cell.setValues(values);
                blankCells.add(cell);
            }
        }
        Set<Cell> constrainedCells = constraints.getCells();
        for (Cell constrainedCell : constrainedCells) {
            for (Cell blankCell : blankCells) {
                if (blankCell.equals(constrainedCell)) {
                    cells.remove(blankCell);
                    cells.add(constrainedCell);
                }
            }
        }
    }

/**
 * Returns the set of cells for the current solution.
 *
 * @return the set of cells for the current solution
*/
    public final Set<Cell> getCells() {
        return cells;
    }

/**
 * Returns the square of dimension of the current problem set.
 *
 * @return the square of the dimension of the current problem set
 */
    public final int getDimSq() {
        return dimSq;
    }

    /**
     * @return the dim
     */
    public final int getDim() {
        return dim;
    }

/**
 * Returns the dimension of the current problem set.
 *
 * @param dim   the dimension of the current problem set
 */
    public final void setDim(final int dim) {
        this.dim = dim;
    }

}
