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

    //private int warehouseId;
    //private String item;

    /**
     * Main method.
     *
     * @param args command line args to the program
     */
    public static void main(String[] args) {

        if(args.length != 3){
            System.out.println("Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>");
            return;
        }

        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int seed = Integer.parseInt(args[2]);

        if(rows < 4 || cols < 4){
            System.out.println("Error: Rows and columns must be at least 4 to allow proper map layout.");
            return;
        }

        Messages.printWelcome();

        int ID = 1;
        WarehouseMap warehouse = new WarehouseMap(rows, cols, seed, SCANNER, ID);

        WarehouseManagerEngine engine = new WarehouseManagerEngine();

        int MenuNum = 0;

        do {
            Messages.printMainMenuCommands();
            String input = SCANNER.nextLine();
            try {
                MenuNum = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }
            switch (MenuNum){
                case 1: // Start warehouse shift
                    warehouse.startShift();
                    ID = warehouse.WarehouseID;
                    break;
                case 2: // resume last shift
                    warehouse.resumeShift();
                    ID = warehouse.WarehouseID;
                    break;
                case 3: // view operation and history
                    warehouse.printHistory();
                    break;
                case 4: // reset shift&warehouse
                    ID += 1;
                    warehouse.reset(ID);
                    System.out.println("Shift and warehouse reset.");
                    break;
                case 5:// exit
                    break;
                default:
                    System.out.println("Invalid input.");
            }

        }
        while(MenuNum != 5);
        System.out.println("Session abandoned. Goodbye!");



    }

}

