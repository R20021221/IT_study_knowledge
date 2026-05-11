/**
 Student Name - Guancheng Rong
 Student Id - 1856981
 Student email - guancheng.rong@student.unimelb.edu.au
 AI Usage Declaration -
*/

import java.util.Scanner;

/**
 * Main engine for the Warehouse Manager console application.
 */
public class WarehouseManagerEngine {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Main method.
     *
     * @param args command line args to the program
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>");
            return;
        }

        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int seed = Integer.parseInt(args[2]);

        if (rows < 4 || cols < 4) {
            System.out.println("Error: Rows and columns must be at least 4 to allow proper map layout.");
            return;
        }

        Messages.printWelcome();

        int warehouseId = 1;
        WarehouseMap warehouse = new WarehouseMap(rows, cols, seed, SCANNER, warehouseId);

        int menuChoice = 0;

        do {
            Messages.printMainMenuCommands();
            String input = SCANNER.nextLine();
            try {
                menuChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(Messages.INVALID_INPUT);
                continue;
            }
            switch (menuChoice) {
                case 1: // Start warehouse shift. 
                    warehouse.startShift();
                    warehouseId = warehouse.getWarehouseId();
                    break;
                case 2: // Resume last shift. 
                    warehouse.resumeShift();
                    warehouseId = warehouse.getWarehouseId();
                    break;
                case 3: // View operation history.
                    warehouse.printHistory();
                    break;
                case 4: // Reset shift and warehouse.
                    warehouseId += 1;
                    warehouse.reset(warehouseId);
                    System.out.println("Shift and warehouse reset.");
                    break;
                case 5: // Exit program.
                    break;
                default:
                    System.out.println(Messages.INVALID_INPUT);
            }

        }
        while (menuChoice != 5);
        System.out.println("Session abandoned. Goodbye!");
    }

}

