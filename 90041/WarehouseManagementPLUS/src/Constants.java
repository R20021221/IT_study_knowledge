/**
 * Stores all constants for the Warehouse Manager assignment.
 */
public final class Constants {

    private Constants() { }

    public static final int MIN_DIMENSION = 4;

    public static final int START_ROW = 1;
    public static final int START_COL = 1;

    public static final int INITIAL_WAREHOUSE_ID = 1;

    // Used to compute available inner cells (rows-2)*(cols-2) - 1 start cell
    public static final int BOUNDARY_THICKNESS = 2;
    public static final int START_OFFSET = 1;

    public static final int MIN_SHELVES = 1;
    public static final int MIN_RESTRICTED = 1;

    public static final int MIN_ITEMS_PER_SHELF = 1;
    public static final int MAX_ITEMS_PER_SHELF = 4;

    public static final int INITIAL_HISTORY_CAPACITY = 2;
    public static final int INITIAL_SHELF_CAPACITY = 2;
    public static final int SIZE_MULTIPLIER = 2;

    public static final char FORKLIFT_SYMBOL = 'F';

    public static final String MOVE_UP = "U";
    public static final String MOVE_DOWN = "D";
    public static final String MOVE_LEFT = "L";
    public static final String MOVE_RIGHT = "R";

    public static final String MOVE_DELIVER = "T";



    public static final String MENU_START_SHIFT = "1";
    public static final String MENU_RESUME_SHIFT = "2";
    public static final String MENU_VIEW_HISTORY = "3";
    public static final String MENU_RESET = "4";
    public static final String MENU_EXIT = "5";

    public static final String MENU_QUIT = "Q";

    public static final String SHELF_MENU_VIEW = "V";
    public static final String SHELF_MENU_PICK = "P";
    public static final String SHELF_MENU_PLACE = "L";
    public static final String SHELF_MENU_QUIT = "Q";

    public static final String[] DEFAULT_ITEM_NAMES = {
            "Box", "Pallet", "Monitor", "Keyboard", "Chair",
            "Cable", "Book", "Toolkit", "Printer", "Router"
    };

    public static final String HISTORY_HEADER_FORMATTER =
            "| %9s | %-15s | %-10s | %-5s | %-5s | %-19s |\n";

    public static final String HISTORY_ROW_FORMATTER =
            "| %9d | %-15s | %-10s | %-5d | %-5d | %-19s |\n";

    public static final String HISTORY_DIVIDER =
            "|===========|=================|============|=======|=======|=====================|";

    /**
     * Maps a cell type to a printable symbol.
     *
     * @param type cell type
     * @return symbol
     */
    public static char getSymbol(CellType type) {
        switch (type) {
            case WALL:
                return '#';
            case AISLE:
                return '.';
            case RESTRICTED:
                return 'X';
            case SHELF:
                return 'S';
            case START:
                return 'O';
            default:
                return ' ';
        }
    }
}
