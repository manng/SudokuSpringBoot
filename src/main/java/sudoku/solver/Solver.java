package sudoku.solver;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import sudoku.exceptions.BadCellException;
import sudoku.exceptions.InvalidInputDataException;
import sudoku.io.Output;
import sudoku.items.Cell;
import sudoku.items.Constraints;
import sudoku.items.CurrentSolution;
import sudoku.items.Holder;
import sudoku.util.Utilities;

public class Solver {

/**
 * Run method for main class which calls the solver, which also does top-level
 * work.
 *
 * This method first calls Solver.iterativeUpdateConstraints() on the initial
 * constraints, to create a Holder with the extended Constraints and
 * CurrentSolution objects.  If the solution is not found at once, the method calls
 * Solver.twoDegreesOfFreedom() a set number of times (currently 1000) to see if
 * any call finds the solution.  Each call to Solver.twoDegreesOfFreedom() picks
 * two cells at random to test, so 1000 trys is enough for most problems.
 *
 * @param initConstraints    initial Constraints object
 * @param writer             a writer object containing the file used to store the result (null if not used)
 * @param outputFormat		 format of the output file if used (XML, JSON or text)
 * @param printOutput		 true if running text output is required, false otherwise
 * @return					 true if the solver finds a solution, false otherwise
 * @throws IOException
 * @throws JAXBException 
 *                    
*/
	public static boolean run(final Constraints initConstraints, 
						      Writer writer, 
						      final int outputFormat,
						      final boolean printOutput) throws IOException, JAXBException {
    	Holder holder = new Holder(initConstraints, null);
    	holder = iterativeUpdateConstraints(holder, true, printOutput);
    	Constraints constraints = holder.getConstraints();
    	if (constraints.getCells().size() == (constraints.getDimSq() * constraints.getDimSq())) {
    		if (printOutput) {
    			System.out.println("This is the final solution, no test trys were required.");
    		}
    		Output.saveResultsToFile(constraints, writer, outputFormat);
    		return true;
    	}
    	if (printOutput) {
    		System.out.println("This is the last construction solution.");
    		System.out.println("Now starting two degree of freedom trials.");
    	}
    	int maxTrys = 1000;
    	Holder oldHolder = new Holder(holder);
    	Constraints solution;
    	for (int i = 0; i < maxTrys; i++) {
    		solution = twoDegreesOfFreedom(holder, printOutput);
    		if (solution != null) {
    			if (printOutput) {
    				if (i==0){
    					System.out.println("This is the final solution after 1 try.");
    				} else {
    					System.out.println("This is the final solution after " + (i + 1) + " tries.");
    				}
    			}
    			Output.saveResultsToFile(solution, writer, outputFormat);
    			return true;
    		} else {
    			holder = new Holder(oldHolder);
    		}
    	}
    	if (printOutput) {
    		System.out.println("Failed to find a solution after " + maxTrys + " trys.");
    	}
    	return false;
    }

/**
 * This method takes a current version of the Constraints and CurrentSolution
 * and tests whether these can lead to the final solution.
 *
 * This method determines that the final solution has not been found using two
 * tests.  Firstly, if a BadCellException has been thrown, this means that the
 * calculations led to a cell which could not take any value at all.  This is
 * clearly impossible and indicates that the Constraints object contained
 * incorrect values.  Secondly, if the output Constraints object does not cover
 * every cell in the problem, it cannot be determined if the current Constraints
 * object is part of the final solution, so this is taken as a failure.
 *
 * If the output Constraints object covers all cells in the problem space, this
 * is the final solution and the Holder object is updated and returned.
 *
 * @param holder       Holder storing the current Constraint and CurrentSolution
 *                     objects.
 * @param firstPass    true if this is the first time this method is called,
 *                     false otherwise
 * @param			   true if running text output is required, false otherwise
 * @return             the Holder object, either the original or holding the
 *                     final result if this has been found
*/
    public static Holder iterativeUpdateConstraints(final Holder holder,
                                                    final boolean firstPass,
                                                    final boolean printOutput) {
        final Holder originalHolder = new Holder(holder);
        Holder newHolder;
        try {
            newHolder = updateConstraints(holder.getConstraints(), printOutput);
        } catch (BadCellException bce) {
            return originalHolder;
        }
        if ((firstPass)|| 
        	(newHolder.getConstraints().getCells().size()          
        	== (newHolder.getConstraints().getDimSq() * newHolder.getConstraints().getDimSq()))) {
                return newHolder;
        } else {
         	if (printOutput) {
           		System.out.println("INCOMPLETE: Test change failed to find a complete solution");
           	}
            return originalHolder;
        }
    }

    /**
     * This method updates the Constraints object and find the first CurrentSolution
     * object based on the initial constraints.
     *
     * This method uses a do-while loop which looks for new constraints in every
     * iteration.  Whenever it finds a new constraint, it updates the Constraints
     * object and sets a flag to continue the looping (every new constrained cell
     * can lead to more new constraints which must be tested for).  If a cell is not
     * fully constrained, those values it can take are added to the CurrentSolution
     * object.
     *
     * @param constraintsOrig        the original Constraints object
     * @param printOutput			 true if running text output is required, false otherwise
     * @return                       Holder storing the updated Constraints and
     *                               CurrentSolution objects
     * @throws BadCellException      thrown if looping generates a cell which can
     *                               contain no values
     */
        public static Holder updateConstraints(final Constraints constraintsOrig, 
        									   final boolean printOutput) throws BadCellException {
            Constraints constraints = constraintsOrig;
            int dim = constraints.getDim();
            CurrentSolution currentSolution = new CurrentSolution(dim);
            boolean loopFinished;
            do {
                loopFinished = true;
                constraints.draw(printOutput);

                currentSolution.reset(constraints);
                if (printOutput) {
                	System.out.println("No constrained cells = " + constraints.getCells().size());
                }
                //test how many values each cell can take
                Set<Cell> cells = currentSolution.getCells();
                for (Cell cell:cells){
                    if (!constraints.getCells().contains(cell)) {
                        Set<Integer> allowedValues =
                                     allowedValuesForCell(constraints, cell);

                        //if a cell cannot take any values, throw a BadCellException
                        if (allowedValues.isEmpty()){
                        	if (printOutput) {
                        		System.out.println("ERROR: FOUND A CELL WHICH CAN CONTAIN NO VALUES");
                        		System.out.println("Cell is at x = " + (cell.getxCoord()+1)  + " y = " + (cell.getyCoord()+1));
                        	}
                            throw new BadCellException();
                        }

                        //if a cell can take exactly one value, add this cell and value to the Constraints
                        if (allowedValues.size() == 1){
                            constraints =
                                updateConstraints(constraints, cell, allowedValues);
                            if (printOutput) {
                            	System.out.println(
                                    "\n\nCell found with only one possible value");
                            	System.out.println(
                                    "Updated cell at x = " + (cell.getxCoord()+1)
                                  + " y = " + (cell.getyCoord()+1));
                            }
                            loopFinished = false;
                            break;
                        } else {

                            //update the CurrentSolution with the values this cell can take
                            currentSolution = updateSolution(currentSolution, cell,
                                                             allowedValues);
                        }
                    }
                }

                //look for a cell which is the only one in a column which can take a particular value
                if (loopFinished){
                    Cell cell = Finder.findUniqueCellx(currentSolution,
                                                       constraints);
                    if (cell != null){
                        loopFinished = false;
                        if (printOutput) {
                        	System.out.println("\n\nCell found where a value can only appear once in a column.  "
                        						+ "Column " + (cell.getxCoord() + 1));
                        	System.out.println("Updated cell at x = " + (cell.getxCoord()+1)
                                        		+ " y = " + (cell.getyCoord() + 1));
                        }
                        constraints = updateConstraints(constraints, cell,
                                                        cell.getValues());
                    }
                }

                //look for a cell which is the only one in a row which can take a particular value
                if (loopFinished){
                    Cell cell = Finder.findUniqueCelly(currentSolution,
                                                       constraints);
                    if (cell != null){
                        loopFinished = false;
                        if (printOutput) {
                        	System.out.println("\n\nCell found where a value can only appear once in a row.  "
                        						+ "Row " + (cell.getyCoord() + 1));
                        	System.out.println("Updated cell at x = " + (cell.getxCoord() + 1)
                                        		+ " y = " + (cell.getyCoord() + 1));
                        }
                        constraints = updateConstraints(constraints, cell,
                                                        cell.getValues());
                    }
                }

                //look for a cell which is the only one in a box which can take a particular value
                if (loopFinished){
                    Cell cell = Finder.findUniqueCellBoxes(currentSolution,
                                                           constraints);
                    if (cell != null){
                        loopFinished = false;
                        if (printOutput) {
                        	System.out.println("\n\nCell found where a value can only appear once in a box.  "
                        						+ "Box " + cell.getBox(constraints.getDim()));
                        	System.out.println("Updated cell at x = " + (cell.getxCoord() + 1)
                                        		+ " y = " + (cell.getyCoord() + 1));
                        }
                        constraints = updateConstraints(constraints, cell,
                                                        cell.getValues());
                    }
                }
            } while (!loopFinished);
            return new Holder(constraints, currentSolution);
        }

/**
 * This method finds the values a given cell can take.  Any values of
 * constrained cells in the same row, column or box are excluded.
 *
 * @param constraints       Constraints object storing the constrained values
 * @param cell              the cell being tested
 * @return                  Set of Integers which are the values this cell can
 *                          take
*/
    public static Set<Integer> allowedValuesForCell(Constraints constraints, Cell cell) {
        Set<Integer> allowedValues = new HashSet<Integer>();
        for (int i = 1; i <= constraints.getDimSq(); i++) {
            allowedValues.add(new Integer(i));
        }
        int dim = constraints.getDim();
        int x = cell.getxCoord();
        int y = cell.getyCoord();
        int box = cell.getBox(dim);
        Set<Cell> comparisonCells = new HashSet<Cell>();
        for (Cell constrainedCell:constraints.getCells()) {
            if (!constrainedCell.getValues().isEmpty()) {
                int xConstr = constrainedCell.getxCoord();
                int yConstr = constrainedCell.getyCoord();
                int boxConstr = constrainedCell.getBox(dim);
                if ((xConstr == x) || (yConstr == y) || (box == boxConstr)) {
                    comparisonCells.add(constrainedCell);
                }
            }
        }
        for (Cell constrainedCell:comparisonCells) {
            for (Integer forbiddenValue:constrainedCell.getValues()) {
                if (allowedValues.contains(forbiddenValue)) {
                    allowedValues.remove(forbiddenValue);
                }
            }
        }
        return allowedValues;
    }
            
/**
 * This method adds a new cell to the Constraints object.
 *
 * @param constraints       the input Constraints object
 * @param cell              the cell to be added to the Constraints object
 * @param allowedValues     set of allowed values for this cell (must be of
 *                          size 1)
 * @return                  the output, updated Constraints object
*/
   public static Constraints updateConstraints(Constraints constraints,
                                               Cell cell,
                                               Set<Integer> allowedValues){
       cell.setValues(allowedValues);
       constraints.getCells().add(cell);
       return constraints;
   }

/**
 * This method adds a new cell to the CurrentSolution object.
 *
 * @param currentSolution      the initial CurrentSolution object
 * @param cell                 the cell to be added
 * @param allowedValues        allowable values for this cell
 * @return                     the output, updated CurrentSolution object
 */
    public static CurrentSolution updateSolution(CurrentSolution currentSolution,
                              					 Cell cell,
                              					 Set<Integer> allowedValues) {
       cell.setValues(allowedValues);
       currentSolution.getCells().add(cell);
       return currentSolution;
    }

/**
 * This method performs a two-degree-of-freedom search.
 *
 * This method takes the initial partial solution stored in the holder parameter
 * (which contains a CurrentSolution object and a Constraints object) and
 * randomly takes two cells which are not in the Constraints object.  It then
 * loops through all the values these two cells can take (as stored in the
 * CurrentSolution object), add these values to the Constraints object, and
 * calls iterativeUpdateConstraints() for this "test" Constraints object.  If
 * the two cells chosen and the values being looped through contain values which
 * actually fit the final solution, iterativeUpdateConstraints() may go on to
 * find the complete final solution.
 *
 * If this try finds the final solution, the updated Constraints object is sent
 * to the display methods and this method returns true. Otherwise the two cells
 * being tried are removed from the trial Constraints object and this method
 * returns false.
 *
 * Selecting two cells and looping through all the values they can take ensures
 * that each try significantly reduces the complexity of the problem.  One of
 * the pairs of values being looped through must be part of the final solution,
 * and having two more correct cells greatly reduces the complexity of the
 * problem.
 *
 * Although one combination of values for the two cells under test must be part
 * of the final solution, it may lead only to an "indeterminate" solution in
 * which the Constraints object does not cover the whole solution space.
 * iterativeUpdateConstraints() counts such a situation as a failure.  Thus it
 * is not certain that the first pair of cells being tested will generate the
 * final solution.
 *
 * @param holderOrig    Holder object storing the initial Constraints and
 *                      CurrentSolution objects
 * @param printOutput	true if running text output is desired, false otherwise
 * @return              a Constraints object containing the final result, null if no result found
 * @throws IOException 
 */
        public static Constraints twoDegreesOfFreedom(final Holder holderOrig,
        										  	  final boolean printOutput) throws IOException{

            //initialize the running solution and constraints
            Holder holder = new Holder(holderOrig);
            Constraints constraints = holder.getConstraints();
            CurrentSolution currentSolution = holder.getCurrentSolution();
            List<Cell> candidateCells = new ArrayList<Cell>();
            for (Cell candidateCell : currentSolution.getCells()) {
                if (!constraints.getCells().contains(candidateCell)) {
                    candidateCells.add(candidateCell);
                }
            }

            //select two different cells at random
            Cell changedCell1;
            Cell changedCell2;
            int cellPos1 = (int) Math.round(Math.random()
                              * (candidateCells.size() - 1));
            int cellPos2;
            int dim = constraints.getDim();
            do {
              cellPos2 = (int) Math.round(Math.random()
                           * (candidateCells.size() - 1));
              changedCell1 = candidateCells.get(cellPos1);
              changedCell2 = candidateCells.get(cellPos2);
            } while ((cellPos1 == cellPos2) ||
                     (changedCell1.getxCoord() == changedCell2.getxCoord()) ||
                     (changedCell1.getyCoord() == changedCell2.getyCoord()) ||
                     (changedCell1.getBox(dim) == changedCell2.getBox(dim))
                    );

            //set up lists of possible values for the test cells
            List<Integer> candidateValues1 = new ArrayList<Integer>();
            for (Integer candidateVal : changedCell1.getValues()) {
                candidateValues1.add(candidateVal);
            }
            List<Integer> candidateValues2 = new ArrayList<Integer>();
            for (Integer candidateVal : changedCell2.getValues()) {
                candidateValues2.add(candidateVal);
            }

            //iterate through possible values of the two test cells, and test what
            //result each combination of values leads to
            for (Integer candidateVal1 : candidateValues1) {
                changedCell1.getValues().clear();
                changedCell1.getValues().add(candidateVal1);
                for (Integer candidateVal2 : candidateValues2) {
                    changedCell2.getValues().clear();
                    changedCell2.getValues().add(candidateVal2);
                    if (printOutput) {
                    	System.out.println("Setting cell at x = "
                    			+ (changedCell1.getxCoord() + 1)
                    			+ " y = " + (changedCell1.getyCoord() + 1)
                    			+ " to value " + candidateVal1.toString());
                    	System.out.println("Setting cell at x = "
                    			+ (changedCell2.getxCoord() + 1)
                    			+ " y = " + (changedCell2.getyCoord() + 1)
                    			+ " to value " + candidateVal2.toString());
                    }
                    constraints = updateConstraints(constraints, changedCell1,
                                                    changedCell1.getValues());
                    constraints = updateConstraints(constraints, changedCell2,
                                                    changedCell2.getValues());
                    holder.setConstraints(constraints);

                    //test whether current values of test cells lead to a final solution
                    holder = iterativeUpdateConstraints(holder, false, printOutput);
                    constraints = holder.getConstraints();
                    if (constraints.getCells().size()
                        == (constraints.getDimSq() * constraints.getDimSq())) {
                    	return constraints;
                    } else {
                        constraints.getCells().remove(changedCell1);
                        constraints.getCells().remove(changedCell2);
                    }
                }
            }
            return null;
        }

/**
 * General method to validate the initial entry for input constraints.  This
 * returns no value if the initial conditions are valid, but throws an exception
 * if they are not.
 *  
 * @param constraints			the initial set of input constraints
 * @throws InvalidDataInputException
 * @throws ArrayIndexOutOfBoundsException
 */
    public static void validateInitialConstraints(Constraints constraints) 
    throws InvalidInputDataException, ArrayIndexOutOfBoundsException { 
          	
       	int[][] constraintsArray = Utilities.convertSolutionToArray(constraints);
       	//check that no non-zero number appears twice in a row
       	for (int x=0; x<constraints.getDimSq(); x++) {
       		List<Integer> cellList = new ArrayList<Integer>();
       		for(int y=0; y<constraints.getDimSq(); y++) {
            	if (constraintsArray[x][y] != 0){
            		if ((constraintsArray[x][y] < 0) || (constraintsArray[x][y] > constraints.getDimSq())) {
            			throw new InvalidInputDataException("Out-of-range value " + constraintsArray[x][y] + " found at cell x = " + x + " y = " + y);
            		}
            		Integer currentValue = new Integer(constraintsArray[x][y]);
            		if (cellList.contains(currentValue)){
            			throw new InvalidInputDataException("Value " + currentValue.intValue() + " found twice in row " + x);
            		}
            		cellList.add(currentValue);
            	}
            }
        }
        //check that non-zero number appears twice in a column
        for (int y=0; y<constraints.getDimSq(); y++) {
            List<Integer> cellList = new ArrayList<Integer>();
            for(int x=0; x<constraints.getDimSq(); x++) {
            	if (constraintsArray[x][y] != 0){
            		Integer currentValue = new Integer(constraintsArray[x][y]);
            		if (cellList.contains(currentValue)) {
            			throw new InvalidInputDataException("Value " + currentValue.intValue() + " found twice in column " + y);
            		}
             		cellList.add(currentValue);
            	}
            }
        }
        //check that no non-zero number appears twice in the same box
        for (int bx=0;bx<constraints.getDim();bx++){
            for (int by=0;by<constraints.getDim();by++) {
            	List<Integer> cellList = new ArrayList<Integer>();
            	for (int x=0; x<constraints.getDimSq(); x++) {
            	    for(int y=0; y<constraints.getDimSq(); y++) {
            	    	if ((x / constraints.getDim() == bx) && (y / constraints.getDim() == by)){
            	        	if (constraintsArray[x][y] != 0){
            	        		Integer currentValue = new Integer(constraintsArray[x][y]);
            	        		if (cellList.contains(currentValue)) {
            	        			throw new InvalidInputDataException("Value " + currentValue.intValue() + " found twice in box " + bx + " " + by);
            	        		}
            	        		cellList.add(currentValue);
            	        	}
            	    	}
            	    }
            	}
            }
        }
    }
}
