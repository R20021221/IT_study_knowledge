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

    public static void printShelfMenu(){
        System.out.println("\nShelf Menu:");
        System.out.println("Press V to view items.");
        System.out.println("Press P to pick an item.");
        System.out.println("Press Q to exit shelf menu.");
        System.out.print(">");
    }
    public static void QuitProgrammer(){
        System.out.println("Session abandoned. Goodbye!");
    }

    public static void CheckNumReminder(){
        System.out.println("Invalid input.");
    }
}

