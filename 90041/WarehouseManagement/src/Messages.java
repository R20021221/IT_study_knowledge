/**
 Student Name - Guancheng Rong
 Student Id - 1856981
 Student email - guancheng.rong@student.unimelb.edu.au
*/

/**
 * Class for user-facing messages.
 */
public final class Messages {

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

    public static void printMoveMenu(){
        System.out.println("\nShelf Menu:");
        System.out.println("Press V to view items.");
        System.out.println("Press P to pick an item.");
        System.out.println("Press Q to exit shelf menu.");
        System.out.print("> ");
    }



    public static void titleGrid(){
        System.out.println("Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift\n" + "Forklift at: (1,1)");
    }



    /*
                Enter direction:
                U - Up.
                D - Down.
                L - Left.
                R - Right.
                T - Deliver carried item at START (O).
                Q - Quit to main menu.
                >
    */
    public static void operationFkMenu(){
        System.out.println("\nEnter direction:");
        System.out.println("U - Up.");
        System.out.println("D - Down.");
        System.out.println("L - Left.");
        System.out.println("R - Right.");
        System.out.println("T - Deliver carried item at START (O).");
        System.out.println(" - Quit to main menu.");
        System.out.print("> ");
    }

    public static void endGrid(){
        System.out.println("Shift completed: all shelves visited and all items processed.");
    }
}

