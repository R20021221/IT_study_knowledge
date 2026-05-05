/**
Student Name - Guancheng Rong
Student Id - 1856981
Student email - guancheng.rong@student.unimelb.edu.au
*/

public final class Constants {

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

    public static final int MIN_SHELVES = 1;
    public static final int MIN_RESTRICTED = 1;

    public static final int MIN_ITEMS_PER_SHELF = 1;
    public static final int MAX_ITEMS_PER_SHELF = 4;

    public static final int BOUNDARY_THICKNESS = 2;
    public static final int START_OFFSET = 1;

    public static final String TYPE_MOVE = "MOVE";
    public static final String TYPE_HIT_WALL = "HIT_WALL";
    public static final String TYPE_HIT_RESTRICTED = "HIT_RESTRICTED";
    public static final String TYPE_VIEW_SHELF = "VIEW_SHELF";
    public static final String TYPE_PICK_ITEM = "PICK_ITEM";
    public static final String TYPE_PLACE_ITEM = "PLACE_ITEM";

}
