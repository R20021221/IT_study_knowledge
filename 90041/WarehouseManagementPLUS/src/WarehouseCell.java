/**
 * Represents a single cell in the warehouse grid.
 *
 * Each cell has:
 *  - a fixed position (row, column)
 *  - a mutable type (wall, aisle, shelf, start, restricted)
 *  - an optional Shelf object if the cell is a shelf
 *
 * This class is responsible ONLY for storing cell state.
 * It does not handle movement, input, or rendering logic.
 */
public class WarehouseCell {

    /* ===================== POSITION (IMMUTABLE) ===================== */

    // Row index of this cell in the warehouse grid (never changes)
    private final int row;

    // Column index of this cell in the warehouse grid (never changes)
    private final int col;

    /* ===================== CELL STATE ===================== */

    // Current type of the cell (WALL, AISLE, SHELF, START, RESTRICTED)
    private CellType type;

    // Shelf stored in this cell (null if the cell is not a shelf)
    private Shelf shelf;

    /**
     * Creates a new warehouse cell at a specific position with an initial type.
     *
     * @param row  the row index of the cell
     * @param col  the column index of the cell
     * @param type the initial type of the cell
     */
    public WarehouseCell(int row, int col, CellType type) {
        this.row = row;
        this.col = col;
        this.type = type;

        // Shelf is only created explicitly when needed
        this.shelf = null;
    }

    /* ===================== ACCESSORS ===================== */

    /**
     * Returns the row index of this cell.
     *
     * @return row index
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column index of this cell.
     *
     * @return column index
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Returns the current type of this cell.
     *
     * @return cell type
     */
    public CellType getType() {
        return this.type;
    }

    /* ===================== MUTATORS ===================== */

    /**
     * Sets the type of this cell.
     *
     * If the cell is changed to a non-SHELF type, any existing shelf
     * reference is cleared to maintain a consistent state.
     *
     * @param type the new cell type
     */
    public void setType(CellType type) {
        this.type = type;

        // Enforce invariant: non-shelf cells must not hold shelves
        if (type != CellType.SHELF) {
            this.shelf = null;
        }
    }

    /**
     * Attaches a new empty shelf to this cell.
     *
     * This method:
     *  - changes the cell type to SHELF
     *  - creates a new Shelf object
     *
     * Used during warehouse generation.
     */
    public void attachNewShelf() {
        this.type = CellType.SHELF;
        this.shelf = new Shelf();
    }

    /* ===================== SHELF QUERIES ===================== */

    /**
     * Returns true if this cell currently contains a shelf.
     *
     * @return true if shelf exists, false otherwise
     */
    public boolean hasShelf() {
        return this.shelf != null;
    }

    /**
     * Returns the shelf stored in this cell.
     *
     * May return null if the cell is not a shelf.
     * Callers are expected to check hasShelf() first.
     *
     * @return shelf or null
     */
    public Shelf getShelf() {
        return new Shelf(this.shelf); //IMP_NOTE: return a copy of the shelf to avoid privacy leaks
    }

    //IMP_NOTE: Shelf belongs to cell, if we need to modify a shelf in the cell, we need to pass the control (ie invoking a method) to cell first then and then to shelf

    /**
     * Adds an item to shelf
     * @param item Item to be added
     */
    public void addItemtoShelf(Item item) {
        this.shelf.addItem(item);
    }

    /**
     * Removes an item from the shelf
     * @param index removes the item based on the index passed.
     * @return returns the removed item
     */
    public Item removeItemfromShelf(int index) {
        return this.shelf.removeItemByUserIndex(index);
    }
}
