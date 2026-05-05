/**
 * Student Name - Guancheng Rong
 * Student Id - 1856981
 * Student email - guancheng.rong@student.unimelb.edu.au
 * AI Usage Declaration -
 */

/**
 * Represents visible warehouse cell symbols.
 */
public enum CellType {
    WALL("#"),
    AISLE("."),
    RESTRICTED("X"),
    SHELF("S"),
    START("O"),
    FORKLIFT("F");

    private final String symbol;

    CellType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
