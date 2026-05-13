import java.util.Scanner;

/**
 * WarehouseMap represents a 2D warehouse grid that can be navigated by a forklift.
 */
public class WarehouseMap {

    private final int rows;
    private final int cols;

    private final WarehouseCell[][] grid;
    private final WarehouseGenerator generator;

    private boolean[][] visitedShelves;

    private int warehouseId;

    /**
     * Constructs a new WarehouseMap.
     *
     * @param rows number of rows
     * @param cols number of cols
     * @param seed seed for random generation
     */
    public WarehouseMap(int rows, int cols, long seed) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new WarehouseCell[rows][cols];
        this.generator = new WarehouseGenerator(seed);

        this.warehouseId = Constants.INITIAL_WAREHOUSE_ID;
        this.visitedShelves = new boolean[rows][cols];

        generateMap();
    }

    /**
     * Displays the warehouse grid and forklift position.
     *
     * @param forklift forklift
     */
    public void display(Forklift forklift) {
        System.out.println("Warehouse ID: " + this.warehouseId);
        Messages.printLegend();

        if (forklift != null) {
            System.out.println("Forklift at: (" + forklift.getRow() + "," + forklift.getCol() + ")");
        }

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                if (forklift != null && r == forklift.getRow() && c == forklift.getCol()) {
                    System.out.print(Constants.FORKLIFT_SYMBOL + " ");
                } else {
                    System.out.print(Constants.getSymbol(this.grid[r][c].getType()) + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Starts (or resumes) a warehouse shift.
     *
     * @param scanner scanner for input
     * @param forklift forklift object
     */
    public void startShift(Scanner scanner, Forklift forklift) {
        display(forklift);

        boolean shiftOver = false;
        while (!shiftOver) {

            if (isShiftComplete(forklift)) {
                System.out.println(Messages.SHIFT_COMPLETE);
                forklift.setSessionPaused(false);
                reset();
                return;
            }

            Messages.printMovementOptions();
            String input = scanner.nextLine().trim().toUpperCase();

            // Quit to main menu
            if (Constants.MENU_QUIT.equals(input)) {
                forklift.setSessionPaused(true);
                System.out.println(Messages.SESSION_PAUSED);
                shiftOver = true;
                continue;
            }

            // Deliver carried item at START (Rule A) - NOT a movement
            if (Constants.MOVE_DELIVER.equals(input)) {
                deliverAtStart(forklift);
                display(forklift);
                continue;
            }

            // Otherwise treat input as a movement direction
            boolean moved = forklift.move(input, getGridSnapshot());

            if (moved) {
                forklift.addHistory(new OperationRecord(
                        this.warehouseId,
                        OperationType.MOVE,
                        Messages.HYPHEN,
                        forklift.getRow(),
                        forklift.getCol(),
                        forklift.getMoves(),
                        forklift.getHits()
                ));
                handleVisit(scanner, forklift);
            } else {
                OperationType hitType = getHitTypeFromAttempt(input, forklift);
                forklift.addHistory(new OperationRecord(
                        this.warehouseId,
                        hitType,
                        Messages.HYPHEN,
                        forklift.getRow(),
                        forklift.getCol(),
                        forklift.getMoves(),
                        forklift.getHits()
                ));
            }

            display(forklift);
        }
    }

    /**
     * Resets the map and regenerates a new warehouse, incrementing warehouse ID.
     */
    public void reset() {
        this.warehouseId++;
        this.visitedShelves = new boolean[this.rows][this.cols];
        initialiseGrid();
        fillSpecialCells();
    }

    /**
     * Returns a snapshot of the grid types (privacy-safe).
     *
     * @return snapshot of CellType values
     */
    public CellType[][] getGridSnapshot() {
        CellType[][] snapshot = new CellType[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                snapshot[r][c] = this.grid[r][c].getType(); //IMP_NOTE: no privacy leaks with enums
            }
        }
        return snapshot;
    }

    /* ================= PUBLIC SUPPORT METHODS ================= */

    /**
     * Handles the interaction after moving onto a cell (e.g., shelf).
     *
     * @param scanner scanner
     * @param forklift forklift
     */
    public void handleVisit(Scanner scanner, Forklift forklift) {
        WarehouseCell current = this.grid[forklift.getRow()][forklift.getCol()];

        if (current.getType() == CellType.SHELF) {
            display(forklift);
            this.visitedShelves[current.getRow()][current.getCol()] = true;
            runShelfMenu(scanner, forklift, current);
        }
    }

    /* ================= PRIVATE METHODS ================= */

    private boolean isForkliftAtStart(Forklift forklift) {
        return forklift.getRow() == Constants.START_ROW && forklift.getCol() == Constants.START_COL;
    }

    /**
     * Rule A delivery: you can ONLY deliver when standing on START (O).
     * Delivered item is removed from the game (not placed back on any shelf).
     */
    private void deliverAtStart(Forklift forklift) {
        if (!forklift.isCarrying()) {
            System.out.println(Messages.NOT_CARRYING);
            return;
        }

        if (!isForkliftAtStart(forklift)) {
            System.out.println("You must stand on the START cell (O) to deliver.");
            return;
        }

        Item delivered = forklift.drop();

        forklift.addHistory(new OperationRecord(
                this.warehouseId,
                OperationType.PLACE_ITEM,   // you can rename this to DELIVER_ITEM if you want
                delivered.getName(),
                forklift.getRow(),
                forklift.getCol(),
                forklift.getMoves(),
                forklift.getHits()
        ));

        System.out.println("Item delivered successfully.");
    }

    private void generateMap() {
        initialiseGrid();
        fillSpecialCells();
    }

    private void initialiseGrid() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                CellType type = determineBaseCellType(r, c);
                this.grid[r][c] = new WarehouseCell(r, c, type);
            }
        }
    }

    private CellType determineBaseCellType(int row, int col) {
        if (isBoundary(row, col)) {
            return CellType.WALL;
        }
        if (row == Constants.START_ROW && col == Constants.START_COL) {
            return CellType.START;
        }
        return CellType.AISLE;
    }

    private boolean isBoundary(int row, int col) {
        return row == 0 || col == 0 || row == this.rows - 1 || col == this.cols - 1;
    }

    private void fillSpecialCells() {
        int inner = availableInnerCells();

        // shelves: between MIN_SHELVES and inner (inclusive)
        int shelfCount = this.generator.generateInt(Constants.MIN_SHELVES, inner + 1);

        // remaining cells after shelves
        int remaining = inner - shelfCount;

        // restricted: allow 0 if no space remains, otherwise at least MIN_RESTRICTED
        int restrictedMin = (remaining > 2) ? Constants.MIN_RESTRICTED : 0;
        int restrictedCount = this.generator.generateInt(restrictedMin, remaining + 1);

        placeRestrictedCells(restrictedCount);
        placeShelves(shelfCount);
    }

    private int availableInnerCells() {
        return (this.rows - Constants.BOUNDARY_THICKNESS)
                * (this.cols - Constants.BOUNDARY_THICKNESS) - Constants.START_OFFSET;
    }

    private void placeRestrictedCells(int count) {
        for (int i = 0; i < count; i++) {
            WarehouseCell cell = findRandomEmptyCell();
            cell.setType(CellType.RESTRICTED);
        }
    }

    private void placeShelves(int count) {
        for (int i = 0; i < count; i++) {
            WarehouseCell cell = findRandomEmptyCell();
            cell.attachNewShelf();
            populateShelf(cell);
        }
    }

    private WarehouseCell findRandomEmptyCell() {
        int attempts = 0;
        int maxAttempts = this.rows *this.cols * 10;

        while (attempts < maxAttempts) {
            int r = this.generator.generateInt(1, this.rows - 1);
            int c = this.generator.generateInt(1, this.cols - 1);

            WarehouseCell cell = this.grid[r][c];
            if (cell.getType() == CellType.AISLE) {
                return cell;
            }
            attempts++;
        }

        System.out.println("Error: No empty AISLE cell available to place an object.");
        return this.grid[Constants.START_ROW][Constants.START_COL]; //IMP_NOTE:Copy constructor not needed, privat method here
    }

    private void    populateShelf(WarehouseCell cell) {
        int itemCount = this.generator.generateInt(Constants.MIN_ITEMS_PER_SHELF, Constants.MAX_ITEMS_PER_SHELF + 1);

        for (int i = 0; i < itemCount; i++) {
            cell.addItemtoShelf(new Item(this.generator.randomItemName())); //To modify cell -> shelf, invoke a method from cell, that invokes a method from shelf
        }
    }

    // RULE A completion: visited all shelves AND removed all items from all shelves AND not carrying
    private boolean isShiftComplete(Forklift forklift) {
        return allShelvesVisited()
                && allShelvesEmpty()
                && !forklift.isCarrying();
    }

    private boolean allShelvesVisited() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                if (this.grid[r][c].getType() == CellType.SHELF && !this.visitedShelves[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean allShelvesEmpty() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                if (this.grid[r][c].getType() == CellType.SHELF) {
                    Shelf shelf = this.grid[r][c].getShelf();
                    if (shelf != null && shelf.getSize() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void runShelfMenu(Scanner scanner, Forklift forklift, WarehouseCell cell) {
        boolean inShelfMenu = true;
        while (inShelfMenu) {
            Messages.printShelfMenu();
            String input = scanner.nextLine().trim().toUpperCase();
            inShelfMenu = handleShelfMenuInput(scanner, forklift, cell, input);
        }
    }

    private boolean handleShelfMenuInput(Scanner scanner, Forklift forklift, WarehouseCell cell, String input) {
        switch (input) {
            case Constants.SHELF_MENU_VIEW:
                forklift.addHistory(new OperationRecord(
                        this.warehouseId,
                        OperationType.VIEW_SHELF,
                        Messages.HYPHEN,
                        forklift.getRow(),
                        forklift.getCol(),
                        forklift.getMoves(),
                        forklift.getHits()
                ));
                printShelfItems(cell);
                return true;

            case Constants.SHELF_MENU_PICK:
                pickItemFromShelf(scanner, forklift, cell);
                return true;

            case Constants.SHELF_MENU_QUIT:
                return false;

            default:
                System.out.println(Messages.INVALID_INPUT);
                return true;
        }
    }

    private void printShelfItems(WarehouseCell cell) {
        Shelf shelf = cell.getShelf();
        if (shelf == null || shelf.getSize() == 0) {
            System.out.println(Messages.NO_ITEMS_ON_SHELF);
            return;
        }

        Item[] items = shelf.getItemsSnapshot();
        for (int i = 0; i < items.length; i++) {
            if(items[i] != null && items[i].getName() != null) {
                System.out.printf("%d. %s%n", i + 1, items[i].getName());
            }
        }
    }

    private void pickItemFromShelf(Scanner scanner, Forklift forklift, WarehouseCell cell) {
        if (forklift.isCarrying()) {
            System.out.println(Messages.ALREADY_CARRYING);
            return;
        }

        Shelf shelf = cell.getShelf();
        if (shelf == null || shelf.getSize() == 0) {
            System.out.println(Messages.NO_ITEMS_ON_SHELF);
            return;
        }

        System.out.print(Messages.ENTER_ITEM_INDEX);
        String input = scanner.nextLine().trim();

        int index = parsePositiveInt(input);
        if (index < 1) {
            System.out.println(Messages.INVALID_INPUT);
            return;
        }

        //IMP_NOTE: To remove an item from cell> shelf, invoke a method from cell that invokes a method from shelf to modify the array.
        Item removed = cell.removeItemfromShelf(index);
        if (removed == null) {
            System.out.println(Messages.INVALID_INPUT);
            return;
        }

        forklift.pickUp(removed);
        forklift.addHistory(new OperationRecord(
                this.warehouseId,
                OperationType.PICK_ITEM,
                removed.getName(),
                forklift.getRow(),
                forklift.getCol(),
                forklift.getMoves(),
                forklift.getHits()
        ));

        System.out.println(Messages.ITEM_PICKED);
    }

    private int parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : -1;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private OperationType getHitTypeFromAttempt(String direction, Forklift forklift) {
        int nextRow = forklift.getRow();
        int nextCol = forklift.getCol();

        if (Constants.MOVE_UP.equals(direction)) nextRow--;
        else if (Constants.MOVE_DOWN.equals(direction)) nextRow++;
        else if (Constants.MOVE_LEFT.equals(direction)) nextCol--;
        else if (Constants.MOVE_RIGHT.equals(direction)) nextCol++;
        else {
            System.out.println(Messages.INVALID_INPUT);
            return OperationType.HIT_WALL; // safe fallback
        }

        CellType type = this.grid[nextRow][nextCol].getType();
        if (type == CellType.RESTRICTED) {
            System.out.println(Messages.HIT_RESTRICTED);
            return OperationType.HIT_RESTRICTED;
        }

        System.out.println(Messages.HIT_WALL);
        return OperationType.HIT_WALL;
    }
}
