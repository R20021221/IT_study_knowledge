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

    private int warehouseId;
    private String item;

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


        WarehouseManagerEngine engine = new WarehouseManagerEngine();


        //Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift
        //Add your code here
        Messages mes = null;


        int MenuNum;

        do {
            Messages.printMainMenuCommands();
            MenuNum = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (MenuNum){
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:
                    System.out.println("Session abandoned. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid input.");
            }

        }
        while(MenuNum != 5);



    }

}

