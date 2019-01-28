package sudoku.solver;

import java.util.HashSet;
import java.util.Set;
import sudoku.items.Cell;
import sudoku.items.Constraints;
import sudoku.items.CurrentSolution;

/**
 * This class contains static methods which find new constrained values for
 * cells.
 *
 * The methods in this class look for cells which are the only ones in a row,
 * column or box which can take a particular value.
 *
 * @author Gary Mann
 */
public class Finder {

/**
 * This method returns a cell which is the only one in a row which can take a
 * particular value.
 *
 * If such a cell exists, this method will return it with its value set
 * containing this value.
 *
 * @param currentSolution   the CurrentSolution object storing which values each
 *                          cell can take
 * @param constraints       Constraints object indicating which cells are
 *                          constrained
 * @return                  Cell which is the only one in a row which can take a
 *                          particular value, null if no such cell exists
 */
    public static Cell findUniqueCelly(final CurrentSolution currentSolution,
                                       final Constraints constraints) {

        int[] frequency = new int[constraints.getDimSq()];

        //loop through all rows
        for (int y = 0; y < constraints.getDimSq(); y++) {

            //initialise frequency values to be zero
            for (int i = 0; i < constraints.getDimSq(); i++) {
                frequency[i] = 0;
            }

            //for cells which are not constrained, increment the frequency for
            //every value they can take
           for (Cell possibleCell : currentSolution.getCells()) {
               if (!constraints.getCells().contains(possibleCell)) {
                    if (possibleCell.getyCoord() == y) {
                       for (Integer value : possibleCell.getValues()) {
                               frequency[value.intValue() - 1]++;
                        }
                    }
               }
           }

           //loop through all values in the frequency array.  If the frequency
           //for any value is exactly one, then find the cell which can take
           //that value, set its allowable value to this value and return it
           for (int i = 0; i < constraints.getDimSq(); i++) {
               if (frequency[i] == 1) {
                    for (Cell possibleCell : currentSolution.getCells()) {
                        if (!constraints.getCells().contains(possibleCell)) {
                            if (possibleCell.getyCoord() == y) {
                                Integer val = new Integer(i + 1);
                                if (possibleCell.getValues().contains(val)) {
                                    Cell cell =
                                        new Cell(possibleCell.getxCoord(), y);
                                    Set<Integer> set = new HashSet<Integer>();
                                    set.add(val);
                                    cell.setValues(set);
                                    return cell;
                                }
                            }
                        }
                    }
               }
           }
        }
        return null;
    }

/**
 * This method returns a cell which is the only one in a box which can take a
 * particular value.
 *
 * If such a cell exists, this method will return it with its value set
 * containing this value.
 *
 * @param currentSolution   the CurrentSolution object storing which values each
 *                          cell can take
 * @param constraints       Constraints object indicating which cells are
 *                          constrained
 * @return                  Cell which is the only one in a box which can take a
 *                          particular value, null if no such cell exists
 */
    public static Cell
            findUniqueCellBoxes(final CurrentSolution currentSolution,
                                final Constraints constraints) {
        int dim = constraints.getDim();
        int[] frequency = new int[constraints.getDimSq()];

        //loop through all boxes
        for (int box = 0; box < constraints.getDimSq(); box++) {

           //initialise frequency values to be zero
           for (int i = 0; i < constraints.getDimSq(); i++) {
                frequency[i] = 0;
           }

           //for cells which are not constrained, increment the frequency for
           //every value they can take
           for (Cell possibleCell : currentSolution.getCells()) {
               if (!constraints.getCells().contains(possibleCell)) {
                    if (possibleCell.getBox(dim) == box) {
                       for (Integer value : possibleCell.getValues()) {
                               frequency[value.intValue() - 1]++;
                        }
                    }
               }
           }

           //loop through all values in the frequency array.  If the frequency
           //for any value is exactly one, then find the cell which can take
           //that value, set its allowable value set to this value and return it
           for (int i = 0; i < constraints.getDimSq(); i++) {
               if (frequency[i] == 1) {
                    for (Cell possibleCell : currentSolution.getCells()) {
                        if (!constraints.getCells().contains(possibleCell)) {
                            if (possibleCell.getBox(dim) == box) {
                                Integer val = new Integer(i + 1);
                                if (possibleCell.getValues().contains(val)) {
                                    Cell cell =
                                            new Cell(possibleCell.getxCoord(),
                                                     possibleCell.getyCoord());
                                    Set<Integer> set = new HashSet<Integer>();
                                    set.add(val);
                                    cell.setValues(set);
                                    return cell;
                                }
                            }
                        }
                    }
               }
           }
        }
        return null;
    }

/**
 * This method returns a cell which is the only one in a column which can take a
 * particular value.
 *
 * If such a cell exists, this method will return it with its value set
 * containing this value.
 *
 * @param currentSolution   the CurrentSolution object storing which values each
 *                          cell can take
 * @param constraints       Constraints object indicating which cells are
 *                          constrained
 * @return                  Cell which is the only one in a column which can
 *                          take a particular value, null if no such cell exists
 */
    public static Cell findUniqueCellx(final CurrentSolution currentSolution,
                                       final Constraints constraints) {

        int[] frequency = new int[constraints.getDimSq()];

        //loop through all columns
        for (int x = 0; x < constraints.getDimSq(); x++) {

           //initialise frequency values to be zero
           for (int i = 0; i < constraints.getDimSq(); i++) {
                frequency[i] = 0;
           }

           //for cells which are not constrained, increment the frequency for
           //every value they can take
           for (Cell possibleCell : currentSolution.getCells()) {
               if (!constraints.getCells().contains(possibleCell)) {
                    if (possibleCell.getxCoord() == x) {
                       for (Integer value : possibleCell.getValues()) {
                               frequency[value.intValue() - 1]++;
                        }
                    }
               }
           }

           //loop through all values in the frequency array.  If the frequency
           //for any value is exactly one, then find the cell which can take
           //that value, set its allowable value set to this value and return it
           for (int i = 0; i < constraints.getDimSq(); i++) {
               if (frequency[i] == 1) {
                    for (Cell possibleCell : currentSolution.getCells()) {
                        if (!constraints.getCells().contains(possibleCell)) {
                            if (possibleCell.getxCoord() == x) {
                                Integer val = new Integer(i + 1);
                                if (possibleCell.getValues().contains(val)) {
                                    Cell cell =
                                          new Cell(x, possibleCell.getyCoord());
                                    Set<Integer> set = new HashSet<Integer>();
                                    set.add(val);
                                    cell.setValues(set);
                                    return cell;
                                }
                            }
                        }
                    }
               }
           }
        }
        return null;
    }

}
