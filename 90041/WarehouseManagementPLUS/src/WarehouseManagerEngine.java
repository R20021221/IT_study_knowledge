import java.util.Scanner;

/**
 * Main engine for the Warehouse Manager console application.
 */
public class WarehouseManagerEngine {

    public static final Scanner SCANNER = new Scanner(System.in);

    private final int NUM_OF_CMD_ARGS = 3;
    private final int MIN_DIMENSION = Constants.MIN_DIMENSION;

    /**
     * Main method.
     *
     * @param args command line args to the program
     */
    public static void main(String[] args) {
        WarehouseManagerEngine engine = new WarehouseManagerEngine();
        if (engine.validateCmdArgs(args)) {
            Messages.printWelcome();
            engine.runMainMenuLoop(args);
        }
    }

    /**
     * Validates the command-line arguments.
     *
     * @param args command-line arguments
     * @return true if valid
     */
    private boolean validateCmdArgs(String[] args) {
        if (args == null || args.length != NUM_OF_CMD_ARGS) {
            System.out.println(Messages.INVALID_ARGS_USAGE);
            return false;
        }

        // Assumption: correct numeric arguments (same approach as CampusNavigator).
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);

        if (rows < MIN_DIMENSION || cols < MIN_DIMENSION) {
            System.out.println(Messages.INVALID_ARGS_DIMENSIONS);
            return false;
        }

        return true;
    }

    /**
     * Runs the main menu loop.
     *
     * @param args command-line args
     */
    private void runMainMenuLoop(String[] args) {
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        long seed = Long.parseLong(args[2]);

        Forklift forklift = new Forklift();
        WarehouseMap warehouseMap = new WarehouseMap(rows, cols, seed);

        boolean exit = false;
        while (!exit) {
            Messages.printMainMenuCommands();
            String choice = SCANNER.nextLine().trim();

            switch (choice) {
                case Constants.MENU_START_SHIFT:
                    warehouseMap.startShift(SCANNER, forklift);
                    break;

                case Constants.MENU_RESUME_SHIFT:
                    if (forklift.isSessionPaused()) {
                        warehouseMap.startShift(SCANNER, forklift);
                    } else {
                        System.out.println(Messages.NO_SHIFT_TO_RESUME);
                    }
                    break;

                case Constants.MENU_VIEW_HISTORY:
                    forklift.printHistory();
                    break;

                case Constants.MENU_RESET:
                    forklift.resetSessionState();
                    warehouseMap.reset();
                    System.out.println(Messages.RESET_DONE);
                    break;

                case Constants.MENU_EXIT:
                    System.out.println(Messages.GOODBYE);
                    exit = true;
                    break;

                default:
                    System.out.println(Messages.INVALID_INPUT);
            }
        }
    }
}
