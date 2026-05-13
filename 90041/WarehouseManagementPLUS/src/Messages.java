/**
 * Class for user-facing messages.
 */
public final class Messages {

    private Messages() { }

    public static final String HYPHEN = "-";

    public static final String INVALID_ARGS_USAGE =
            "Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>";

    public static final String INVALID_ARGS_DIMENSIONS =
            "Error: Rows and columns must be at least 4 to allow proper map layout.";

    public static final String INVALID_INPUT =
            "Invalid input.";

    public static final String NO_SHIFT_TO_RESUME =
            "No shift to resume.";

    public static final String GOODBYE =
            "Session abandoned. Goodbye!";

    public static final String SHIFT_COMPLETE =
            "Shift completed: all shelves visited and all items processed.";

    public static final String SESSION_PAUSED =
            "Shift paused.";

    public static final String HIT_WALL =
            "You cannot enter that area.";

    public static final String HIT_RESTRICTED =
            "You cannot enter that area.";

    public static final String NO_ITEMS_ON_SHELF =
            "No items on this shelf.";

    public static final String NOT_CARRYING =
            "You are not carrying any item.";

    public static final String ALREADY_CARRYING =
            "You are already carrying an item. Place it before picking another.";

    public static final String ENTER_ITEM_INDEX =
            "Enter item number to pick (e.g., 1): ";

    public static final String ITEM_PICKED =
            "Item picked successfully.";

    public static final String ITEM_PLACED =
            "Item placed successfully.";

    public static final String NO_HISTORY =
            "No operation history available.";

    public static final String RESET_DONE =
            "Shift and warehouse reset.";

    public static void printWelcome() {
        System.out.println("Welcome to Warehouse Manager Console.");
    }

    public static void printMainMenuCommands() {
        System.out.println("\n=== Warehouse Manager Menu ===");
        System.out.println("1. Start warehouse shift.");
        System.out.println("2. Resume last shift.");
        System.out.println("3. View operation history.");
        System.out.println("4. Reset shift and warehouse.");
        System.out.println("5. Abandon the shift and exit.");
        System.out.print("> ");
    }

    public static void printMovementOptions() {
        System.out.println("Enter direction:");
        System.out.println("U - Up.");
        System.out.println("D - Down.");
        System.out.println("L - Left.");
        System.out.println("R - Right.");
        System.out.println("T - Deliver carried item at START (O).");
        System.out.println("Q - Quit to main menu.");
        System.out.print("> ");
    }


    public static void printShelfMenu() {
        System.out.println("Shelf Menu:");
        System.out.println("Press V to view items.");
        System.out.println("Press P to pick an item.");
        System.out.println("Press Q to exit shelf menu.");
        System.out.print("> ");
    }

    public static void printLegend() {
        System.out.println("Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift");
    }
}
