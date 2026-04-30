/**
 Student Name - Guancheng Rong
 Student Id - 1856981
 Student email - guancheng.rong@student.unimelb.edu.au
 AI Usage Declaration -
*/

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

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

        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int seed = Integer.parseInt(args[2]);

        WarehouseMap warehouse = new WarehouseMap(rows, cols, seed);
        ForkPosition fork = new ForkPosition();

        WarehouseManagerEngine engine = new WarehouseManagerEngine();


        //Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift
        //Add your code here



        int MenuNum;

        do {
            Messages.printMainMenuCommands();
            MenuNum = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (MenuNum){
                case 1: // Start warehouse shift
                    fork.initialize(); // initialize fork in(1,1)
                    WarehouseMap.printArray(fork.F_row, fork.F_col,warehouse.map);// print F position and whole map
                    Messages.printMoveMenu();
                    String order = SCANNER.nextLine();
                    fork.setF_order(order);
                    fork.movement(fork.F_row, fork.F_col, order);
                    // movement ...

                    break;
                case 2: // resume last shift




                    break;
                case 3: // view operation and history

                    break;
                case 4:// reset shift&warehouse

                    break;
                case 5:// exit
                    System.out.println("Session abandoned. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid input.");
            }

        }
        while(MenuNum != 5);



    }

}

