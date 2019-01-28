/**
 * This package contains the classes which store intermediate cell values and constraints during
 * the problem solving.
 *
 * A feature of the Cell object is that is overrides the equals() and hashcode() objects, so that
 * cells are compared solely on their x- and y-coordinates (cell values are not included in the
 * comparison).  This means that cells can easily be stored in Sets, without the same cell being
 * stored twice due to changed values.
 *
 */
package sudoku.items;
