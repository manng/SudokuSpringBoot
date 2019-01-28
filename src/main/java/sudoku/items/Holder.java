package sudoku.items;

/**
 * This class stores the latest Constraints and CurrentSolution objects between
 * calls to the solver methods.
 *
 * @author Gary Mann
 */
public class Holder {
    private CurrentSolution currentSolution;
    private Constraints constraints;

/**
 * Constructs an empty holder object of given dimension
 *
 * @param dim   the dimension of the current problem
 */
    public Holder(final int dim) {
        this.constraints = new Constraints(dim);
        this.currentSolution = new CurrentSolution(dim);
    }

/**
 * Constructs a new holder based on an existing one
 *
 * @param holder  existing holder, whose Constraints and CurrentSolution fields
 *                will be saved in this one
 */
    public Holder(final Holder holder) {
        this(holder.getConstraints(), holder.getCurrentSolution());
    }

/**
 * Constructs a new holder with a given Constraints and CurrentSolution
 *
 * @param constraints       Constraints object to be saved in this holder
 * @param currentSolution   CurrentSolution object to be saved in this holder
 */
    public Holder(final Constraints constraints,
                  final CurrentSolution currentSolution) {
        this(constraints.getDim());
        this.constraints = new Constraints(constraints);
        if (currentSolution != null) {
            this.currentSolution = new CurrentSolution(currentSolution);
        } else {
            this.currentSolution = new CurrentSolution(constraints.getDim());
        }
    }

/**
 * Returns the CurrentSolution object stored in this holder.
 *
 * @return the CurrentSolution object stored in this holder
 */
    public CurrentSolution getCurrentSolution() {
        return currentSolution;
    }

/**
 * Returns the Constraints object stored in this holder
 *
 * @return the Constraints object stored in this holder
 */
    public Constraints getConstraints() {
        return constraints;
    }

/**
 * Sets a new Constraints object to be stored in this holder.
 *
 * @param constraints      Constraints object to be stored in this holder
 */
    public void setConstraints(Constraints constraints){
        this.constraints = new Constraints(constraints);
    }
}
