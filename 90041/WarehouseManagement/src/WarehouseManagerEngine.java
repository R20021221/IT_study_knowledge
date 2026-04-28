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
        WarehouseManagerEngine engine = new WarehouseManagerEngine();
        //Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift
        //Add your code here
        Messages mes = null;


        int MenuNum;

        do {
            Messages.printMainMenuCommands();
            MenuNum = SCANNER.nextInt();
            SCANNER.nextLine();


            if(MenuNum != 1| MenuNum != 2| MenuNum != 3| MenuNum != 4| MenuNum != 5){
                Messages.CheckNumReminder();
            }

        }
        while(MenuNum != 5);
        Messages.QuitProgrammer();


    }

}

