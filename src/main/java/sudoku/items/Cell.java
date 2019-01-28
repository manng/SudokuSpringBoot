package sudoku.items;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Cell class for the sudoku solver which stores the x- and y-coordinates of the
 * cell and the possible values it can take.
 *
 * Cell is used in both CurrentSolution and Constraints objects, but in slightly
 * different ways for each.  In CurrentSolution, the values set will contain
 * several values, representing the possible values a cell can take.  In the
 * Constraints object, the cell can only take one value and this is only entry
 * in the values set.
 *
 * The equals() method is overridden for the Cell class, so that cells are
 * compared solely using their x- and y-coordinate values.  This allows cells to
 * be stored in Sets which can easily be updated after cell contents have
 * changed.
 *
 * @author Gary Mann
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cell", propOrder = {"xCoord","yCoord","values"})
public class Cell {

	@XmlAttribute(name="x")
    private int xCoord;
	@XmlAttribute(name="y")
    private int yCoord;
	@XmlAttribute(name="value")
    private Set<Integer> values;
    
    public Cell(){}

/**
 * Constructs a cell a the given location.
 *
 * @param xCoord    the x-coordinate of the cell
 * @param yCoord    the y-coordinate of the cell
 */
    public Cell(final int xCoord, final int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        values = new HashSet<Integer>();
    }

/**
 * Returns the x-coordinate of the cell.
 *
 * @return the x-coordinate of the cell
 */
    public final int getxCoord() {
        return xCoord;
    }

/**
 * Returns the y-coordinate of the cell.
 *
 * @return the y-coordinate of the cell
 */
    public final int getyCoord() {
        return yCoord;
    }

/**
 * Returns the box this cell is in.
 *
 * @param dim   the dimension of this problem set
 * @return      the box this cell is in
 */
    public final int getBox(final int dim) {
        return (xCoord / dim) * dim + (yCoord / dim);
    }

/**
 * Returns the possible values of this cell.
 *
 * @return a Set of possible values this cell can take
 */
    public final Set<Integer> getValues() {
        return values;
    }

/**
 * Sets possible values of this cell.
 *
 * @param values    a Set of possible values of this cell
 */
    public final void setValues(final Set<Integer> values) {
        this.values = values;
    }

/**
 * Overriden hashcode method.
 *
 * @return  the hashcode for this cell
 */
    @Override public int hashCode(){
        return xCoord * yCoord;
    }

/**
 * Overriden equals method, which compares cells solely on location.  (The
 * contents of the cell are not used)
 *
 * @param cell      cell being compared to the current one
 * @return          true if the cells are at the same location, false otherwise
 */
    @Override public boolean equals(Object cell){
        if (!(cell instanceof Cell)) {
            return false;
        }
        Cell testCell = (Cell) cell;
        if (testCell.getxCoord() != xCoord) {
            return false;
        }
        if (testCell.getyCoord() != yCoord) {
            return false;
        }
        return true;
    }

}
