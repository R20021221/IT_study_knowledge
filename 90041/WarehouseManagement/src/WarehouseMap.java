import java.util.Scanner;

/**
 * WarehouseMap represents a 2D warehouse grid that can be navigated by a forklift.
 */
public class WarehouseMap {

    private final int rows;
    private final int cols;

    // add the map variable here
    private final WarehouseGenerator generator;

    /**
     * Constructs a new WarehouseMap.
     *
     * @param rows number of rows
     * @param cols number of cols
     * @param seed seed for random generation
     */
    public WarehouseMap(int rows, int cols, long seed) {
        this.rows = rows;
        this.cols = cols;
        this.generator = new WarehouseGenerator(seed);

        //TODO: set other variables here 

        generateMap();
    }

    //DO NOT MODIFY THIS METHOD
    private void generateMap() {
        initialiseGrid();
        fillSpecialCells();
    }

    private void initialiseGrid() {
        // TODO: initialise map by looping through Array
        // TODO: set the boundary, start position and mark everything else as open position
   
    }

    //DO NOT MODIFY THIS METHOD
    private void fillSpecialCells() {
        int inner = availableInnerCells();

        // shelves: between MIN_SHELVES and inner (inclusive)
        int shelfCount = generator.generateInt(Constants.MIN_SHELVES, inner + 1);

        // remaining cells after shelves
        int remaining = inner - shelfCount;

        // restricted: allow 0 if no space remains, otherwise at least MIN_RESTRICTED
        int restrictedMin = (remaining > 2) ? Constants.MIN_RESTRICTED : 0;
        int restrictedCount = generator.generateInt(restrictedMin, remaining + 1);

        placeRestrictedCells(restrictedCount);
        placeShelves(shelfCount);
    }

    private int availableInnerCells() {
        return (rows - Constants.BOUNDARY_THICKNESS)
                * (cols - Constants.BOUNDARY_THICKNESS) - Constants.START_OFFSET;
    }

    private void placeRestrictedCells(int count) {
        //TODO: use the generator to generate random position for rows/cols that are open spaces to fill restricted places. 
        // The maximum number of restricted places are defined by the count parameter in this method.
    }

    private void placeShelves(int count) {
        //TODO: the total shelves to be created defined by the count parameter
        //TODO: based on number of shelves to be created, generate random row/col positions and fill up with Shelves.
        //TODO: for each shelf generated you need add items to the shelf
        populateShelf(); // can modify this method to add parameters required to place items to shelf.
        
    }

    private WarehouseCell findRandomEmptyCell() {
        int attempts = 0;
        int maxAttempts = rows * cols * 10;

        while (attempts < maxAttempts) {
            int r = generator.generateInt(/*set the right arguments*/);
            int c = generator.generateInt(/*set the right arguments*/);

            WarehouseCell cell = grid[r][c];
            if (/*TODO: Set the correct condition here*/) {
                return cell;
            }
            attempts++;
        }

        System.out.println("Error: No empty AISLE cell available to place an object.");
        return null;
    }

    private void populateShelf(/*TODO: add parameters here if required*/) {
        int itemCount = generator.generateInt(Constants.MIN_ITEMS_PER_SHELF, Constants.MAX_ITEMS_PER_SHELF + 1);

        // TODO: add items to the shelf
    }

  
}

