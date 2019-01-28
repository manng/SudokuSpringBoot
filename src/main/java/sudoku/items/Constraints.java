package sudoku.items;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Constraints class for sudoku solver which stores the constrained cells.
 *
 * @author Gary Mann
 */
@XmlRootElement(name = "Cells")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cells", propOrder = {"cells"})
public class Constraints {

    @XmlElement(name = "Cell")
    private Set<Cell> cells;
    @XmlAttribute(name = "dim", required = true)
    private int dim;
    @XmlTransient
    private int dimSq;
    
    public Constraints(){}

    public Constraints(int dim) {
        this.dim = dim;
        dimSq = dim * dim;
    }

    public Constraints(Constraints constraints) {
        this(constraints.getDim());
        cells = new HashSet<Cell>();
        for (Cell cell:constraints.getCells()) {
            cells.add(cell);
        }
    }

    public boolean contains(int x, int y) {
        for (Cell testCell : cells) {
            if ((testCell.getxCoord() == x) && (testCell.getyCoord() == y)) {
                return true;
             }
        }
        return false;
    }

    public void draw(final boolean printOutput) {
        Integer[][] output = new Integer[getDimSq()][getDimSq()];
        for (Cell cell : getCells()) {
            if (!cell.getValues().isEmpty()) {
                for (Integer value:cell.getValues()) {
                    output[cell.getxCoord()][cell.getyCoord()] = value;
                }
            }
        }
        if (printOutput) {
        	System.out.println("Latest constraint values are:");
        	for (int y = 0; y < getDimSq(); y++) {
        		for (int x = 0; x < getDimSq(); x++) {
        			if (output[x][y] == null) {
        				System.out.print(" 0  ");
        			} else if (output[x][y].intValue() < 10) {
        				System.out.print(" " + output[x][y].intValue() + "  ");
        			} else {
        				System.out.print(output[x][y].intValue() + "  ");
        			}
        			if (((x + 1)% dim) == 0) {
        				System.out.print("  ");
        			}
        		}
        		System.out.println();
        		if (((y + 1) % dim) == 0) {
        			System.out.println();
        		}
        	}
        }
    }

    /**
     * @return the cells
     */
    public Set<Cell> getCells() {
        return cells;
    }

    /**
     * @return the dim
     */
    public int getDim() {
        return dim;
    }

    /**
     * @return the dimSq
     */
    public int getDimSq() {
        return dimSq;
    }

    /**
     * @param cells the cells to set
     */
    public void setCells(Set<Cell> cells) {
        this.cells = cells;
    }
    
    public void update(){
    	dimSq = dim * dim;
    }
    

}
