import java.util.Scanner;

/**
 * WarehouseMap represents a 2D warehouse grid that can be navigated by a forklift.
 */
public class WarehouseMap {

    private final int rows;
    private final int cols;

    // add the map variable here
    private final WarehouseGenerator generator;
    public String[][] map;


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

        int[][] map = new int[rows][cols];
        //TODO: set other variables here 

        generateMap();
    }

    //DO NOT MODIFY THIS METHOD
    private void generateMap() {
        initialiseGrid();
        fillSpecialCells();
    }

    private void initialiseGrid() {
        for(int i = 0; i < cols; i++ ){
            for(int j = 0; j < rows; j++){
                if(i == 0 | j == 0 | i == cols -1 | j == rows -1){
                    map[i][j] = "#";
                }
                else if(i == 1 && j == 1){
                    map[i][j] = "O";
                }
                else{
                    map[i][j] = ".";
                }
            }
        }

        // TODO: initialise map by looping through Array
        // TODO: set the boundary, start position and mark everything else as open position
   
    }

    //DO NOT MODIFY THIS METHOD
    // get count of shelfCount and remaining
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
        for(int i = 0; i < count; i++){
            int m = findRandomEmptyCell();
            int r = m / cols;
            int c = m % cols;
            map[r][c] = "X";
        }
        //TODO: use the generator to generate random position for rows/cols that are open spaces to fill restricted places.
        // The maximum number of restricted places are defined by the count parameter in this method.
    }

    private void placeShelves(int count) {
        for(int i = 0; i < count; i++) {
            int m = findRandomEmptyCell();
            int r = m / cols;
            int c = m % cols;
            map[r][c] = "S";
            populateShelf(r, c);
        }



        //TODO: the total shelves to be created defined by the count parameter
        //TODO: based on number of shelves to be created, generate random row/col positions and fill up with Shelves.
        //TODO: for each shelf generated you need add items to the shelf
        // can modify this method to add parameters required to place items to shelf.
        
    }

    private int findRandomEmptyCell() {
        int attempts = 0;
        int maxAttempts = rows * cols * 10;

        while (attempts < maxAttempts) {
            int r = generator.generateInt(1, rows - 1);
            int c = generator.generateInt(1, cols - 1);


            if (map[r][c].equals(".")) {

                return r * cols + c;

            }
            attempts++;
        }

        System.out.println("Error: No empty AISLE cell available to place an object.");
        return -1;
    }

    private void populateShelf(int r, int c) {
        int itemCount = generator.generateInt(Constants.MIN_ITEMS_PER_SHELF, Constants.MAX_ITEMS_PER_SHELF + 1);
        itemShelves itemOnShelf = new itemShelves(r, c, itemCount);
        // shelves_have_item[] --> map[][] && item[] class --> row&col
        // map[][]--> map[][][0] X
        for(int i = 0; i < itemCount; i++) {
            String item = generator.randomItemName();
            itemOnShelf.Item[i] = item;
        }
        // TODO: add items to the shelf
    }


    public static void printArray(String[][] array) {
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
                System.out.print(array[i][j] + " ");
            }
            System.out.println("\n");
        }
    }
  
}

