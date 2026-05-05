import java.util.Scanner;

/**
 * WarehouseMap represents a 2D warehouse grid that can be navigated by a forklift.
 */
public class WarehouseMap {

    public int WarehouseID;

    private final int rows;
    private final int cols;

    // add the map variable here
    private final WarehouseGenerator generator;
    private String[][] map;

    private itemShelves itemOnShelf;    // Only was used in generator shelves period >>> Temporary existence
    private itemShelves[] shelves;      // one array for all shelves; any shelves[X] >>> one itemShelves class >>> row, col, item[]
    private int ShelvesIndex;           // Under one map situation, there is only one ShelvesIndex to record the number of shelves

    private ForkPosition fork; // new fork position

    private Scanner scanner;  // claim scanner

    public history[] His;  // history for every fork and its every operation;any His[X] >>> one history class(record) >>> row, col, item...
    public history record; // Temporary existence for His[]
    public int TempCount; // operation counts
    public int limits;
    public int totalOperationCount;
    public history[] TempHis;
    public boolean PausedSituation;


    /**
     * Constructs a new WarehouseMap.
     *
     * @param rows number of rows
     * @param cols number of cols
     * @param seed seed for random generation
     */
    public WarehouseMap(int rows, int cols, long seed, Scanner scanner, int ID) { // get scanner from engine
        this.rows = rows;
        this.cols = cols;
        this.generator = new WarehouseGenerator(seed);
        this.scanner = scanner;

        this.WarehouseID = ID;
        this.TempCount = 0;
        this.limits = 0;
        this.totalOperationCount = 0;
        this.His = new history[10];
        this.PausedSituation = false;


        this.fork = new ForkPosition();
        this.fork.initialize();
        this.map = new String[rows][cols];
        //TODO: set other variables here 

        generateMap(); // include new itemOnShelf, when generate map, the item was stored in the itemShelves class
    }

    public void updateHistory(String type){
        String itemName = null;

        if(type.equals(Constants.TYPE_PICK_ITEM)){
            itemName = fork.F_item;
        }
        else if(type.equals(Constants.TYPE_PLACE_ITEM)){
            itemName = fork.F_item;
        }

        record = new history(WarehouseID, type, itemName, fork.F_moves, fork.F_hits, fork.F_row, fork.F_col);
        if(TempCount < 10) {
            His[limits*10 + TempCount] = record;
            TempCount++;
        }else{
            TempCount = TempCount - 10;
            limits++;
            TempHis = new history[(limits+1)*10];
            for(int i = 0; i< His.length; i++){
                TempHis[i] = His[i]; // transfer His into tempHis array for update one new His array
            }

            His = TempHis; // transfer the data of tempHis Array in His Array
            His[limits*10 + TempCount] = record; // add one new record on new His Array
            TempCount++;
        }
        totalOperationCount = limits*10 + TempCount;
    }

    public boolean checkHistory(){

        if(totalOperationCount > 0){
            return true;
        }
        return false;
    }

    public void printHistory(){
        if(!checkHistory()){
            System.out.println("No operation history available.");
            return;
        }

        System.out.printf(Constants.HISTORY_HEADER_FORMATTER, "Warehouse", "Type", "Item", "Moves", "Hits", "Position");
        System.out.println(Constants.HISTORY_DIVIDER);

        for(int i = 0; i < totalOperationCount; i++){
            history currentRecord = His[i];
            String itemName = currentRecord.item;
            if(itemName == null){
                itemName = "-";
            }
            String position = String.format("(%d,%d)", currentRecord.row, currentRecord.col);

            System.out.printf(Constants.HISTORY_ROW_FORMATTER, currentRecord.warehouseId, currentRecord.type, itemName, currentRecord.moves, currentRecord.hits, position);
        }
    }


    //DO NOT MODIFY THIS METHOD
    private void generateMap() {
        initialiseGrid();
        fillSpecialCells();
    }

    private void initialiseGrid() {
        for(int i = 0; i < rows; i++ ){
            for(int j = 0; j < cols; j++){
                if(i == 0 | j == 0 | i == rows -1 | j == cols -1){
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
        shelves = new itemShelves[count];
        ShelvesIndex = 0;
        for(int i = 0; i < count; i++) {
            int m = findRandomEmptyCell();
            int r = m / cols;
            int c = m % cols;
            map[r][c] = "S";
            populateShelf(r, c, ShelvesIndex);
            ShelvesIndex++;
        }
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

    private void populateShelf(int r, int c, int index) {
        int itemCount = generator.generateInt(Constants.MIN_ITEMS_PER_SHELF, Constants.MAX_ITEMS_PER_SHELF + 1); // item number
        itemOnShelf = new itemShelves(r, c, itemCount);
        // shelves_have_item[] --> map[][] && item[] class --> row&col
        // map[][]--> map[][][0] X
        for(int i = 0; i < itemCount; i++) {
            String item = generator.randomItemName();
            itemOnShelf.Item[i] = item; // store
        }
        shelves[index] = itemOnShelf;
        // TODO: add items to the shelf
    }



    public void printMapArray(int r, int c,String[][] array) {
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++) {

                if (i == r && j == c) {
                    System.out.print("F" + " ");
                } else {
                    System.out.print(array[i][j] + " ");
                }
            }
            System.out.print("\n");
        }
    }

    public void initializeFork(){
        fork.initialize();
    }



    /*public void checkItem(){  // int r = fork.r  int c = fork.c
        int check_r = itemOnShelf.row;
        int check_c = itemOnShelf.col;

        int r = fork.F_row;
        int c = fork.F_col;

        if(r == check_r && c == check_c){

            String order = scanner.nextLine();
            ShelfOperation(order);
        }
    }*/



    public void printItemOnShelves(int shelvesNum){ // input the No. of shelves
        int itemCount = shelves[shelvesNum].itemCount;
        if(itemCount == 0){
            System.out.println("No items on this shelf.");
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            System.out.printf("%d. %s\n", i + 1, shelves[shelvesNum].Item[i]);
        }
    }


    public void checkShelves(int shelvesNum){
        if (shelves[shelvesNum].itemCount == 0) { // judge whether there is anything on shelf
            System.out.println("No items on this shelf.");
        } else {
            int itemCount = shelves[shelvesNum].itemCount;
            System.out.print("Enter item number to pick (e.g., 1): ");
            String input = scanner.nextLine();
            int choice;
            try { // judge the input whether is Invalid input
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                return;
            }
            if (choice > itemCount || choice <= 0) {
                System.out.println("Invalid input.");
            } else {
                PickItem(choice, shelvesNum);
            }
        }
    }

    public void PickItemProgram(int shelvesNum){ // input the No. of shelves
        if(fork.F_item == null) {
            checkShelves(shelvesNum);
        }
        else{
            System.out.println("You are already carrying an item. Place it before picking another.");
        }
    } // whether the forklifter can pick one item

    public void PickItem(int choice, int shelvesNum){   // input the item No. and shelf No.

        fork.F_item = shelves[shelvesNum].Item[choice - 1]; // fork get one target item
        System.out.println("Item picked successfully.");
        int target; // update the items on shelf
        for (target = choice - 1; target < shelves[shelvesNum].itemCount - 1; target++) {
            shelves[shelvesNum].Item[target] = shelves[shelvesNum].Item[target + 1];
        }
        shelves[shelvesNum].Item[shelves[shelvesNum].itemCount - 1] = null;
        shelves[shelvesNum].itemCount--;
        updateHistory(Constants.TYPE_PICK_ITEM);

    }  // pickitem detail


    public int checkShelves(){
        int check_r;
        int check_c;

        for(int i = 0; i < ShelvesIndex; i++){
            check_r = shelves[i].row;
            check_c = shelves[i].col;
            if(fork.F_row == check_r && fork.F_col == check_c){
                return i;
            }
        }
        return -1;
    }

    public boolean checkVisitedShelves(){
        for(int i = 0; i < ShelvesIndex; i++){
            if(!shelves[i].visited){
                return false;
            }
        }
        return true;
    }

    public boolean checkItem(){
        if(fork.F_item != null){
            return false;
        }

        for(int i = 0; i < ShelvesIndex; i++){
            if(shelves[i].itemCount != 0){
                return false;
            }
        }
        return true;
    }

    public boolean checkShiftCompletion(){
        if(checkVisitedShelves() && checkItem()){
            return true;
        }
        return false;
    }


    public void ShelfOperation(){

        String order;
        if(checkShelves() != -1) {
            int shelvesNum = checkShelves();
            shelves[shelvesNum].visited = true;
            do {
                Messages.operationFkMenu();
                order = readCommand();
                if (order.equals("V")) {
                    printItemOnShelves(shelvesNum);
                    updateHistory(Constants.TYPE_VIEW_SHELF);
                } else if (order.equals("P")) {
                    PickItemProgram(shelvesNum);

                }
                else if(order.equals("Q")){
                    // do nothing, exit loop
                }
                else{
                    System.out.println("Invalid input.");
                }
            } while (!order.equals("Q"));
        }

    }

    //Shelf Menu:
    //Press V to view items.
    //Press P to pick an item.
    //Press Q to exit shelf menu.
    //>

    public String readCommand(){  // remove "SPACE" when people input character
        String order = scanner.nextLine();
        order = order.trim();
        order = order.toUpperCase();
        return order;
    }

    public void checkPosition(int r, int c){   // Now fork position
        switch(map[r][c]){
            case "#":
                fork.F_hits++;
                System.out.println("You cannot enter that area.");
                updateHistory(Constants.TYPE_HIT_WALL);
                break;          //  "#"
            case "X":
                fork.F_hits++;
                System.out.println("You cannot enter that area.");
                updateHistory(Constants.TYPE_HIT_RESTRICTED);
                break;          //  "X"
            case "S": // FL at Shelves
                fork.F_moves++;
                fork.F_row = r;
                fork.F_col = c;
                updateHistory(Constants.TYPE_MOVE);
                printCurrentWarehouse();
                ShelfOperation();
                break;          //  "S"
            default:
                fork.F_moves++;
                fork.F_row = r;
                fork.F_col = c;
                updateHistory(Constants.TYPE_MOVE);
                break;          //  "." / "O"
        }
    }

    public void movement(String order){
        int r = fork.F_row;
        int c = fork.F_col;
        switch(order){
            case "U": // go up
                r -= 1;
                checkPosition(r, c);
                break;

            case "D":
                r += 1;
                checkPosition(r, c);
                break;

            case "L":
                c -= 1;
                checkPosition(r, c);
                break;

            case "R":
                c += 1;
                checkPosition(r, c);
                break;
        }
    }

    public void forkDeliver(){
        if(fork.F_item == null){
            System.out.println("You are not carrying any item.");
        }
        else if(fork.F_row == 1 && fork.F_col == 1){
            System.out.println("Item delivered successfully.");
            updateHistory(Constants.TYPE_PLACE_ITEM);
            fork.F_item = null; // remove item on forkLifter
        }
        else{
            System.out.println("You must stand on the START cell (O) to deliver.");
        }
    }

    public void moveOperation(String order){

        if (order.equals("U") || order.equals("R") || order.equals("L") || order.equals("D")) { // check movement order
            movement(order);

        }
        else if (order.equals("T")) { // check forklifter drop item order
            forkDeliver();

        }
        else if (order.equals("Q")) {
            PausedSituation = true;  // find "Q" order change the pause situation
            System.out.println("Shift paused.");
            // quit the shift menu
        }
        else {
            // Invalid input don‘t need to be calculated?
            System.out.println("Invalid input.");
            updateHistory(Constants.TYPE_HIT_WALL);
        }
    }

    public void startShift(){
        String order;
        do {
            printCurrentWarehouse();
            Messages.moveFkMenu();

            order = readCommand();

            moveOperation(order);

            if(checkShiftCompletion()){
                printCurrentWarehouse();
                Messages.endGrid();
                WarehouseID += 1;
                reset(WarehouseID);
                break;
            }
        }while (!order.equals("Q"));
    }

    public void resumeShift(){
        if(!PausedSituation){
            System.out.println("No shift to resume.");
            return;
        }
        PausedSituation = false;
        startShift();
    }



    public void printCurrentWarehouse(){
        System.out.printf("Warehouse ID: %d\n", WarehouseID);
        System.out.println("Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift");
        System.out.printf("Forklift at: (%d,%d)\n", fork.F_row, fork.F_col); // Fork position
        printMapArray(fork.F_row, fork.F_col, map);// print F position and whole map
    }

    public void reset(int newWarehouseID){
        WarehouseID = newWarehouseID;
        map = new String[rows][cols];
        generateMap();
        initializeFork();
        PausedSituation = false;
    }
}

