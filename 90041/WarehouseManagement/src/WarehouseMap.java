/**
 * Student Name - Guancheng Rong
 * Student Id - 1856981
 * Student email - guancheng.rong@student.unimelb.edu.au
 * AI Usage Declaration -
 */

import java.util.Scanner;

/**
 * WarehouseMap represents a 2D warehouse grid that can be navigated by a forklift.
 */
public class WarehouseMap {

    private int warehouseId;

    private final int rows;
    private final int cols;

    private final WarehouseGenerator generator;
    private String[][] map;

    private ItemShelves[] shelves;      // Shelves in the current warehouse. / 当前仓库中的所有货架。
    private int shelvesCount;           // Number of shelves in the current map. / 当前地图中的货架数量。

    private ForkPosition fork; // Forklift state. / 叉车状态。

    private Scanner scanner;  // Shared input scanner from engine. / 从引擎传入的共用输入扫描器。

    private History[] historyRecords;  // Resizable operation history array. / 可扩容的操作历史数组。
    private int totalOperationCount;
    private boolean isPaused;

    /**
     * Constructs a new WarehouseMap.
     *
     * @param rows number of rows
     * @param cols number of cols
     * @param seed seed for random generation
     */
    public WarehouseMap(int rows, int cols, long seed, Scanner scanner, int warehouseId) { // Scanner is shared by the engine. / Scanner 由引擎共用传入。
        this.rows = rows;
        this.cols = cols;
        this.generator = new WarehouseGenerator(seed);
        this.scanner = scanner;

        this.warehouseId = warehouseId;
        this.totalOperationCount = 0;
        this.historyRecords = new History[Constants.INITIAL_HISTORY_CAPACITY];
        this.isPaused = false;

        this.fork = new ForkPosition();
        this.fork.initialize();
        this.map = new String[rows][cols];

        generateMap(); // Build initial warehouse layout and shelf items. / 生成初始仓库布局和货架物品。
    }

    private void updateHistory(OperationType type) {
        String itemName = null;

        if (type == OperationType.PICK_ITEM) {
            itemName = fork.getCarriedItem();
        } else if (type == OperationType.PLACE_ITEM) {
            itemName = fork.getCarriedItem();
        }

        if (totalOperationCount == historyRecords.length) {
            History[] expandedHistory = new History[historyRecords.length + Constants.HISTORY_GROWTH_SIZE];
            for (int i = 0; i < historyRecords.length; i++) {
                expandedHistory[i] = historyRecords[i]; // Copy old records into the expanded array. / 将旧记录复制到扩容后的数组。
            }
            historyRecords = expandedHistory; // Replace history with the expanded array. / 用扩容后的数组替换历史数组。
        }

        historyRecords[totalOperationCount] = new History(warehouseId, type, itemName,
                fork.getMoves(), fork.getHits(), fork.getRow(), fork.getCol());
        totalOperationCount++;
    }

    private boolean hasHistory() {
        return totalOperationCount > 0;
    }

    public void printHistory() {
        if (!hasHistory()) {
            System.out.println("No operation history available.");
            return;
        }

        System.out.printf(Constants.HISTORY_HEADER_FORMATTER, "Warehouse", "Type", "Item", "Moves", "Hits", "Position");
        System.out.println(Constants.HISTORY_DIVIDER);

        for (int i = 0; i < totalOperationCount; i++) {
            History currentRecord = historyRecords[i];
            String itemName = currentRecord.getItem();
            if (itemName == null) {
                itemName = "-";
            }
            String position = String.format("(%d,%d)", currentRecord.getRow(), currentRecord.getCol());

            System.out.printf(Constants.HISTORY_ROW_FORMATTER, currentRecord.getWarehouseId(), currentRecord.getType().name(), itemName, currentRecord.getMoves(), currentRecord.getHits(), position);
        }
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void startShift() {
        String order;
        do {
            printCurrentWarehouse();
            Messages.moveFkMenu();

            order = readCommand();

            moveOperation(order);

            if (isShiftComplete()) {
                printCurrentWarehouse();
                Messages.endGrid();
                warehouseId += 1;
                reset(warehouseId);
                break;
            }
        } while (!order.equals("Q"));
    }

    public void resumeShift() {
        if (!isPaused) {
            System.out.println("No shift to resume.");
            return;
        }
        isPaused = false;
        startShift();
    }

    public void reset(int newWarehouseId) {
        warehouseId = newWarehouseId;
        map = new String[rows][cols];
        generateMap();
        initializeFork();
        isPaused = false;
    }

    //DO NOT MODIFY THIS METHOD
    private void generateMap() {
        initialiseGrid();
        fillSpecialCells();
    }

    private void initialiseGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                    map[i][j] = CellType.WALL.getSymbol();
                } else if (i == 1 && j == 1) {
                    map[i][j] = CellType.START.getSymbol();
                } else {
                    map[i][j] = CellType.AISLE.getSymbol();
                }
            }
        }
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
        for (int i = 0; i < count; i++) {
            int cellIndex = findRandomEmptyCell();
            int row = cellIndex / cols;
            int col = cellIndex % cols;
            map[row][col] = CellType.RESTRICTED.getSymbol();
        }
    }

    private void placeShelves(int count) {
        shelves = new ItemShelves[count];
        shelvesCount = 0;
        for (int i = 0; i < count; i++) {
            int cellIndex = findRandomEmptyCell();
            int row = cellIndex / cols;
            int col = cellIndex % cols;
            map[row][col] = CellType.SHELF.getSymbol();
            populateShelf(row, col, shelvesCount);
            shelvesCount++;
        }
    }

    private int findRandomEmptyCell() {
        int attempts = 0;
        int maxAttempts = rows * cols * 10;

        while (attempts < maxAttempts) {
            int r = generator.generateInt(1, rows - 1);
            int c = generator.generateInt(1, cols - 1);

            if (map[r][c].equals(CellType.AISLE.getSymbol())) {
                return r * cols + c;
            }
            attempts++;
        }
        System.out.println("Error: No empty AISLE cell available to place an object.");
        return -1;
    }

    private void populateShelf(int row, int col, int index) {
        int itemCount = generator.generateInt(Constants.MIN_ITEMS_PER_SHELF, Constants.MAX_ITEMS_PER_SHELF + 1); // Generated item count. / 生成的物品数量。
        ItemShelves itemOnShelf = new ItemShelves(row, col, itemCount);
        // Create shelf data separately from the grid symbol. / 货架数据与地图符号分开保存。
        for (int i = 0; i < itemCount; i++) {
            String item = generator.randomItemName();
            itemOnShelf.setItem(i, item); // Store generated item. / 存储生成的物品。
        }
        shelves[index] = itemOnShelf;
    }

    private void printMapArray(int forkRow, int forkCol, String[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == forkRow && j == forkCol) {
                    System.out.print(CellType.FORKLIFT.getSymbol() + " ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.print("\n");
        }
    }

    private void initializeFork() {
        fork.initialize();
    }

    private void printItemOnShelves(int shelfIndex) { // Print items by shelf index. / 根据货架编号打印物品。
        int itemCount = shelves[shelfIndex].getItemCount();
        if (itemCount == 0) {
            System.out.println(Messages.NO_ITEMS_ON_SHELF);
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            System.out.printf("%d. %s\n", i + 1, shelves[shelfIndex].getItem(i));
        }
    }

    private void checkShelves(int shelfIndex) {
        if (shelves[shelfIndex].getItemCount() == 0) { // Check whether the shelf has items. / 检查货架是否有物品。
            System.out.println(Messages.NO_ITEMS_ON_SHELF);
        } else {
            int itemCount = shelves[shelfIndex].getItemCount();
            System.out.print("Enter item number to pick (e.g., 1): ");
            String input = scanner.nextLine();
            int choice;
            try { // Parse selected item number. / 解析选择的物品编号。
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(Messages.INVALID_INPUT);
                return;
            }
            if (choice > itemCount || choice <= 0) {
                System.out.println(Messages.INVALID_INPUT);
            } else {
                pickItem(choice, shelfIndex);
            }
        }
    }

    private void pickItemProgram(int shelfIndex) { // Handle pick flow by shelf index. / 根据货架编号处理取货流程。
        if (fork.getCarriedItem() == null) {
            checkShelves(shelfIndex);
        } else {
            System.out.println("You are already carrying an item. Place it before picking another.");
        }
    }

    private void pickItem(int choice, int shelfIndex) { // Pick by item number and shelf index. / 根据物品编号和货架编号取货。
        fork.setCarriedItem(shelves[shelfIndex].getItem(choice - 1)); // Forklift carries selected item. / 叉车携带选中的物品。
        System.out.println("Item picked successfully.");
        int targetIndex; // Shift remaining shelf items left. / 将货架剩余物品向前移动。
        for (targetIndex = choice - 1; targetIndex < shelves[shelfIndex].getItemCount() - 1; targetIndex++) {
            shelves[shelfIndex].setItem(targetIndex, shelves[shelfIndex].getItem(targetIndex + 1));
        }
        shelves[shelfIndex].setItem(shelves[shelfIndex].getItemCount() - 1, null);
        shelves[shelfIndex].decrementItemCount();
        updateHistory(OperationType.PICK_ITEM);
    }

    private int searchShelves() {
        int shelfRow;
        int shelfCol;

        for (int i = 0; i < shelvesCount; i++) {
            shelfRow = shelves[i].getRow();
            shelfCol = shelves[i].getCol();
            if (fork.getRow() == shelfRow && fork.getCol() == shelfCol) {
                return i;
            }
        }
        return -1;
    }

    private boolean areAllShelvesVisited() {
        for (int i = 0; i < shelvesCount; i++) {
            if (!shelves[i].isVisited()) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllItemsProcessed() {
        if (fork.getCarriedItem() != null) {
            return false;
        }

        for (int i = 0; i < shelvesCount; i++) {
            if (shelves[i].getItemCount() != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isShiftComplete() {
        return areAllShelvesVisited() && areAllItemsProcessed();
    }

    private void shelfOperation() {
        int shelfIndex = searchShelves();
        if (shelfIndex == -1) {
            return;
        }

        String order;
        shelves[shelfIndex].setVisited(true);
        do {
            Messages.operationFkMenu();
            order = readCommand();
            if (order.equals("V")) {
                printItemOnShelves(shelfIndex);
                updateHistory(OperationType.VIEW_SHELF);
            } else if (order.equals("P")) {
                pickItemProgram(shelfIndex);
            } else if (order.equals("Q")) {
                // Q exits the shelf menu. / Q 退出货架菜单。
            } else {
                System.out.println(Messages.INVALID_INPUT);
            }
        } while (!order.equals("Q"));
    }

    private String readCommand() {  // Trim spaces and normalize command case. / 去除空格并统一命令大小写。
        return scanner.nextLine().trim().toUpperCase();
    }

    private void checkPosition(int row, int col) { // Handle target cell at the requested position. / 处理目标位置的格子。
        String targetCell = map[row][col];
        if (targetCell.equals(CellType.WALL.getSymbol())) {
            fork.incrementHits();
            System.out.println(Messages.CANNOT_ENTER_AREA);
            updateHistory(OperationType.HIT_WALL);
        } else if (targetCell.equals(CellType.RESTRICTED.getSymbol())) {
            fork.incrementHits();
            System.out.println(Messages.CANNOT_ENTER_AREA);
            updateHistory(OperationType.HIT_RESTRICTED);
        } else if (targetCell.equals(CellType.SHELF.getSymbol())) { // Forklift reaches a shelf. / 叉车到达货架。
            fork.incrementMoves();
            fork.setRow(row);
            fork.setCol(col);
            updateHistory(OperationType.MOVE);
            printCurrentWarehouse();
            shelfOperation();
        } else {
            fork.incrementMoves();
            fork.setRow(row);
            fork.setCol(col);
            updateHistory(OperationType.MOVE);
        }
    }

    private void movement(String order) {
        int nextRow = fork.getRow();
        int nextCol = fork.getCol();
        switch (order) {
            case "U": // Move up. / 向上移动。
                nextRow -= 1;
                checkPosition(nextRow, nextCol);
                break;

            case "D":
                nextRow += 1;
                checkPosition(nextRow, nextCol);
                break;

            case "L":
                nextCol -= 1;
                checkPosition(nextRow, nextCol);
                break;

            case "R":
                nextCol += 1;
                checkPosition(nextRow, nextCol);
                break;
        }
    }

    private void forkDeliver() {
        if (fork.getCarriedItem() == null) {
            System.out.println("You are not carrying any item.");
        } else if (fork.getRow() == Constants.START_ROW && fork.getCol() == Constants.START_COL) {
            System.out.println("Item delivered successfully.");
            updateHistory(OperationType.PLACE_ITEM);
            fork.setCarriedItem(null); // Remove item from forklift. / 从叉车上移除物品。
        } else {
            System.out.println("You must stand on the START cell (O) to deliver.");
        }
    }

    private void moveOperation(String order) {
        if (order.equals("U") || order.equals("R") || order.equals("L") || order.equals("D")) { // Dispatch movement command. / 分发移动命令。
            movement(order);
        } else if (order.equals("T")) { // Handle delivery command. / 处理交货命令。
            forkDeliver();
        } else if (order.equals("Q")) {
            isPaused = true;  // Q pauses the current shift. / Q 会暂停当前班次。
            System.out.println("Shift paused.");
        } else {
            // Invalid command is recorded but does not increase hits. / 无效命令会记录历史，但不增加命中数。
            System.out.println(Messages.INVALID_INPUT);
            updateHistory(OperationType.HIT_WALL);
        }
    }

    private void printCurrentWarehouse() {
        System.out.printf("Warehouse ID: %d\n", warehouseId);
        System.out.println("Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift");
        System.out.printf("Forklift at: (%d,%d)\n", fork.getRow(), fork.getCol()); // Forklift position. / 叉车当前位置。
        printMapArray(fork.getRow(), fork.getCol(), map); // Print forklift overlay and grid. / 打印叉车覆盖位置和地图。
    }
}

