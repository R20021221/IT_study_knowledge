# Warehouse Manager — Refactoring Task for Grading Criteria

> **Goal**: Fix all code-style and structure issues to maximise the 12 non-test marks (Presentation 4 + Structure 8). Do NOT change any program logic or output — all test cases already pass.
>
> **Files in scope** (under `src/`): `WarehouseManagerEngine.java`, `WarehouseMap.java`, `ForkPosition.java`, `itemShelves.java`, `history.java`, `Constants.java`, `Messages.java`
>
> **DO NOT modify**: `WarehouseGenerator.java`

---

## TASK 1: Fix Class Names (PascalCase)

Java class names must be PascalCase.

| Current | Rename to | File rename |
|---|---|---|
| `itemShelves` | `ItemShelves` | `itemShelves.java` → `ItemShelves.java` |
| `history` | `History` | `history.java` → `History.java` |

After renaming, update **every reference** in all files:
- `WarehouseMap.java` uses `itemShelves` and `history` extensively as types — all must change.
- Constructor calls: `new itemShelves(...)` → `new ItemShelves(...)`, `new history(...)` → `new History(...)`
- Array declarations: `history[]` → `History[]`, `itemShelves[]` → `ItemShelves[]`

---

## TASK 2: Fix Field and Variable Names (camelCase)

Java fields and local variables must be camelCase (start with lowercase, no underscores).

### 2a. `ForkPosition` fields

| Current | Rename to |
|---|---|
| `F_row` | `row` |
| `F_col` | `col` |
| `F_item` | `carriedItem` |
| `F_moves` | `moves` |
| `F_hits` | `hits` |

Update every reference in `WarehouseMap.java` (heavily used: `fork.F_row` → `fork.getRow()`, etc. — see Task 5 for getter usage).

### 2b. `WarehouseMap` fields

| Current | Rename to |
|---|---|
| `WarehouseID` | `warehouseId` |
| `ShelvesIndex` | `shelvesCount` |
| `His` | `historyRecords` |
| `TempCount` | `currentBatchCount` |
| `TempHis` | `tempHistory` |
| `PausedSituation` | `isPaused` |

### 2c. `itemShelves` (→ `ItemShelves`) fields

| Current | Rename to |
|---|---|
| `Item` (String array) | `items` |

### 2d. `WarehouseManagerEngine` local variables

| Current | Rename to |
|---|---|
| `MenuNum` | `menuChoice` |
| `ID` | `warehouseId` |

---

## TASK 3: Create Enums (worth 1.0 mark)

The spec requires enums for cell types and operation types. Create two new files.

### 3a. `CellType.java`

```java
/**
 * Enum representing different cell types in the warehouse grid.
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
```

### 3b. `OperationType.java`

```java
/**
 * Enum representing different operation types recorded in history.
 */
public enum OperationType {
    MOVE,
    HIT_WALL,
    HIT_RESTRICTED,
    VIEW_SHELF,
    PICK_ITEM,
    PLACE_ITEM
}
```

### 3c. Integrate enums into existing code

- **`WarehouseMap.java`**: Replace all raw string comparisons for cell symbols:
  - `map[i][j] = "#"` → `map[i][j] = CellType.WALL.getSymbol()`
  - `map[i][j] = "."` → `map[i][j] = CellType.AISLE.getSymbol()`
  - `map[i][j] = "O"` → `map[i][j] = CellType.START.getSymbol()`
  - `map[i][j] = "X"` → `map[i][j] = CellType.RESTRICTED.getSymbol()`
  - `map[i][j] = "S"` → `map[i][j] = CellType.SHELF.getSymbol()`
  - `map[r][c].equals(".")` → `map[r][c].equals(CellType.AISLE.getSymbol())`
  - `case "#":` → `case "#":` (switch on String is fine, but add a comment referencing the enum, OR refactor the switch to use if/else with enum comparison)

- **`History.java`** (renamed): Change the `type` field from `String` to `OperationType`.
  - Constructor parameter: `String type` → `OperationType type`
  - In `WarehouseMap.updateHistory()`: pass `OperationType.MOVE` instead of `Constants.TYPE_MOVE`, etc.
  - In `printHistory()`: use `currentRecord.type.name()` to print the string representation (e.g. `"MOVE"`).

- **`Constants.java`**: After enum integration, remove the `TYPE_MOVE`, `TYPE_HIT_WALL`, `TYPE_HIT_RESTRICTED`, `TYPE_VIEW_SHELF`, `TYPE_PICK_ITEM`, `TYPE_PLACE_ITEM` string constants — they are replaced by `OperationType` enum values.

---

## TASK 4: Make All Fields Private + Add Getters/Setters (worth 1.0 mark for encapsulation)

Every field in every class (except `Constants` which is all `public static final`) must be `private`. Add getters (and setters only where external write access is needed).

### 4a. `ForkPosition.java`

```
private int row;
private int col;
private String carriedItem;
private int moves;
private int hits;
```

Add:
```java
public int getRow() { return row; }
public int getCol() { return col; }
public String getCarriedItem() { return carriedItem; }
public int getMoves() { return moves; }
public int getHits() { return hits; }

public void setRow(int row) { this.row = row; }
public void setCol(int col) { this.col = col; }
public void setCarriedItem(String carriedItem) { this.carriedItem = carriedItem; }
public void incrementMoves() { this.moves++; }
public void incrementHits() { this.hits++; }
```

Then in `WarehouseMap.java`, replace:
- `fork.F_row` → `fork.getRow()` (reads) or `fork.setRow(r)` (writes)
- `fork.F_col` → `fork.getCol()` (reads) or `fork.setCol(c)` (writes)
- `fork.F_item` reads → `fork.getCarriedItem()`
- `fork.F_item = xxx` → `fork.setCarriedItem(xxx)`
- `fork.F_moves++` → `fork.incrementMoves()`
- `fork.F_hits++` → `fork.incrementHits()`
- `fork.F_moves` reads → `fork.getMoves()`
- `fork.F_hits` reads → `fork.getHits()`

### 4b. `ItemShelves.java` (renamed from `itemShelves`)

```
private String[] items;
private int row;
private int col;
private int itemCount;
private boolean visited;
```

Add:
```java
public int getRow() { return row; }
public int getCol() { return col; }
public int getItemCount() { return itemCount; }
public boolean isVisited() { return visited; }
public void setVisited(boolean visited) { this.visited = visited; }

// Deep copy to prevent privacy leak
public String[] getItems() {
    String[] copy = new String[itemCount];
    for (int i = 0; i < itemCount; i++) {
        copy[i] = items[i];
    }
    return copy;
}

public String getItem(int index) { return items[index]; }
public void setItem(int index, String value) { items[index] = value; }
public void decrementItemCount() { itemCount--; }
```

Update all direct field accesses in `WarehouseMap.java`:
- `shelves[i].row` → `shelves[i].getRow()`
- `shelves[i].col` → `shelves[i].getCol()`
- `shelves[i].itemCount` → `shelves[i].getItemCount()`
- `shelves[i].visited` → `shelves[i].isVisited()`
- `shelves[i].visited = true` → `shelves[i].setVisited(true)`
- `shelves[i].Item[x]` → `shelves[i].getItem(x)`
- `shelves[i].Item[x] = val` → `shelves[i].setItem(x, val)`
- `shelves[i].itemCount--` → `shelves[i].decrementItemCount()`

### 4c. `History.java` (renamed from `history`)

```
private int warehouseId;
private OperationType type;
private String item;
private int moves;
private int hits;
private int row;
private int col;
```

Add getters only (history records are immutable after creation):
```java
public int getWarehouseId() { return warehouseId; }
public OperationType getType() { return type; }
public String getItem() { return item; }
public int getMoves() { return moves; }
public int getHits() { return hits; }
public int getRow() { return row; }
public int getCol() { return col; }
```

Update `WarehouseMap.printHistory()` to use getters:
- `currentRecord.warehouseId` → `currentRecord.getWarehouseId()`
- `currentRecord.type` → `currentRecord.getType().name()`
- `currentRecord.item` → `currentRecord.getItem()`
- etc.

### 4d. `WarehouseMap.java` — make internal fields private

Change these from `public` to `private`:
```
private int warehouseId;           // was public WarehouseID
private History[] historyRecords;  // was public His
private History record;            // was public record
private int currentBatchCount;     // was public TempCount
private int limits;                // was public limits
private int totalOperationCount;   // was public totalOperationCount
private History[] tempHistory;     // was public TempHis
private boolean isPaused;          // was public PausedSituation
```

Add only the getters/setters that `WarehouseManagerEngine.java` actually needs:
```java
public int getWarehouseId() { return warehouseId; }
public boolean isPaused() { return isPaused; }
```

Then update `WarehouseManagerEngine.java`:
- `warehouse.WarehouseID` → `warehouse.getWarehouseId()`
- `warehouse.PausedSituation` is not directly read from Engine (it's used inside WarehouseMap), so no external getter needed unless Engine reads it.

---

## TASK 5: Fix Privacy Leaks (worth 1.0 mark)

Anywhere a getter returns an array or mutable object, it must return a **deep copy**.

### 5a. `ItemShelves.getItems()` — already handled in Task 4b (returns a copy).

### 5b. `WarehouseMap` — if any getter returns `historyRecords[]` or `shelves[]`, it must copy:

```java
// Example: if a getHistoryRecords() is needed
public History[] getHistoryRecords() {
    History[] copy = new History[totalOperationCount];
    for (int i = 0; i < totalOperationCount; i++) {
        copy[i] = historyRecords[i]; // History is immutable, shallow copy is fine
    }
    return copy;
}
```

However, since `printHistory()` is inside `WarehouseMap` itself, it accesses `historyRecords` directly — no external getter is needed. Just make sure no public getter exposes the raw array.

### 5c. General rule

Audit every `public` method that returns an array or object reference. If the returned value is an internal field, return a copy instead. If no external code needs the array, don't expose a getter at all.

---

## TASK 6: Move Remaining Magic Numbers/Strings to Constants (worth 0.5 mark)

### 6a. Cell symbol strings → already replaced by `CellType` enum in Task 3.

### 6b. Add to `Constants.java`:

```java
public static final int START_ROW = 1;
public static final int START_COL = 1;
public static final int INITIAL_HISTORY_CAPACITY = 10;
public static final int HISTORY_GROWTH_FACTOR = 10;
```

Update references:
- `ForkPosition.initialize()`: `this.row = Constants.START_ROW; this.col = Constants.START_COL;`
- `WarehouseMap` constructor: `this.historyRecords = new History[Constants.INITIAL_HISTORY_CAPACITY];`
- `updateHistory()`: replace all `10` with `Constants.INITIAL_HISTORY_CAPACITY` or `Constants.HISTORY_GROWTH_FACTOR`

### 6c. Move remaining inline output strings to `Messages.java`

Add to `Messages.java`:
```java
public static final String INVALID_INPUT = "Invalid input.";
public static final String CANNOT_ENTER = "You cannot enter that area.";
public static final String NO_ITEMS = "No items on this shelf.";
public static final String ALREADY_CARRYING = "You are already carrying an item. Place it before picking another.";
public static final String ITEM_PICKED = "Item picked successfully.";
public static final String ITEM_DELIVERED = "Item delivered successfully.";
public static final String NOT_CARRYING = "You are not carrying any item.";
public static final String MUST_BE_AT_START = "You must stand on the START cell (O) to deliver.";
public static final String NO_HISTORY = "No operation history available.";
public static final String SHIFT_PAUSED = "Shift paused.";
public static final String SHIFT_RESET = "Shift and warehouse reset.";
public static final String NO_SHIFT = "No shift to resume.";
```

Then replace all `System.out.println("You cannot enter that area.")` etc. with `System.out.println(Messages.CANNOT_ENTER)` throughout `WarehouseMap.java` and `WarehouseManagerEngine.java`.

---

## TASK 7: Add Authorship Statement to All Files (worth 0.5 mark)

These 4 files are **missing** the authorship block:

- `ForkPosition.java`
- `ItemShelves.java` (renamed)
- `History.java` (renamed)
- `WarehouseMap.java`

Add to the top of each file (before any `import`):
```java
/**
 * Student Name - Guancheng Rong
 * Student Id - 1856981
 * Student email - guancheng.rong@student.unimelb.edu.au
 * AI Usage Declaration -
 */
```

---

## TASK 8: Add Javadoc Comments to All Public Methods (worth 0.5 mark)

Every public method needs at least a one-line Javadoc. Every class needs a class-level Javadoc.

### Priority files (most public methods, least comments):

**`WarehouseMap.java`** — add Javadoc to:
- `updateHistory(OperationType type)` — Records an operation in the history array.
- `checkHistory()` — Returns true if any history records exist.
- `printHistory()` — Prints the full operation history table.
- `printMapArray(...)` — Prints the warehouse grid with forklift overlay.
- `initializeFork()` — Resets the forklift to the start position.
- `printItemOnShelves(int shelvesNum)` — Prints items on a specific shelf.
- `checkShelves(int shelvesNum)` — Prompts and validates item pick from a shelf.
- `PickItemProgram(int shelvesNum)` — Handles the pick-item flow including carry check.
- `PickItem(int choice, int shelvesNum)` — Executes item pick and updates shelf.
- `checkShelves()` (no-arg) — Finds which shelf the forklift is on, returns index or -1.
- `checkVisitedShelves()` — Returns true if all shelves have been visited.
- `checkItem()` — Returns true if all items processed and forklift not carrying.
- `checkShiftCompletion()` — Returns true if the shift is complete.
- `ShelfOperation()` — Runs the shelf menu loop.
- `readCommand()` — Reads, trims, and uppercases user input.
- `checkPosition(int r, int c)` — Evaluates the target cell and handles move/hit logic.
- `movement(String order)` — Translates direction to coordinate change.
- `forkDeliver()` — Attempts item delivery at START.
- `moveOperation(String order)` — Dispatches a movement command.
- `startShift()` — Runs the main shift loop.
- `resumeShift()` — Resumes a paused shift.
- `printCurrentWarehouse()` — Prints map header, legend, and grid.
- `reset(int newWarehouseId)` — Resets warehouse to a fresh layout.

**`ForkPosition.java`** — add class Javadoc + method Javadoc for `initialize()` and all getters/setters.

**`ItemShelves.java`** — add class Javadoc + constructor Javadoc + all getters/setters.

**`History.java`** — add class Javadoc + constructor Javadoc + all getters.

Also: **Remove all leftover `//TODO` comments** from all files.

---

## TASK 9: Fix Method Naming (camelCase) and Reduce Public Scope

### 9a. Method names that violate camelCase:

| Current | Rename to |
|---|---|
| `PickItemProgram(...)` | `pickItemProgram(...)` |
| `PickItem(...)` | `pickItem(...)` |
| `ShelfOperation()` | `shelfOperation()` |

### 9b. Methods that should be `private` (not called from outside `WarehouseMap`):

These methods are only used internally by `WarehouseMap` and should be `private`:
- `updateHistory`
- `checkHistory`
- `printMapArray`
- `initializeFork`
- `printItemOnShelves`
- `checkShelves` (both overloads)
- `pickItemProgram`
- `pickItem`
- `checkVisitedShelves`
- `checkItem`
- `checkShiftCompletion`
- `shelfOperation`
- `readCommand`
- `checkPosition`
- `movement`
- `forkDeliver`
- `moveOperation`

Only these `WarehouseMap` methods need to stay `public` (called from Engine):
- `startShift()`
- `resumeShift()`
- `printHistory()`
- `reset(int)`
- `getWarehouseId()`

---

## TASK 10: Clean Up Code File Organisation (worth 0.5 mark)

Reorder members in each class to follow the standard Java convention:

```
1. Static constants / static fields
2. Instance fields
3. Constructors
4. Public methods (getters/setters first, then business methods)
5. Private/helper methods
```

This mainly affects `WarehouseMap.java` — group related methods:
1. Fields at top
2. Constructor
3. Public API (`startShift`, `resumeShift`, `printHistory`, `reset`, getters)
4. Map generation (`generateMap`, `initialiseGrid`, `fillSpecialCells`, etc.)
5. Movement logic (`moveOperation`, `movement`, `checkPosition`)
6. Shelf logic (`shelfOperation`, `pickItemProgram`, `pickItem`, etc.)
7. History logic (`updateHistory`, `checkHistory`, `printHistory` helper)
8. Utility (`readCommand`, `printMapArray`, `printCurrentWarehouse`)

---

## TASK 11: Rename Overloaded `checkShelves` Methods

Currently there are two methods both named `checkShelves`:
- `checkShelves()` — returns the shelf index at forklift position (returns `int`)
- `checkShelves(int shelvesNum)` — handles the pick-item prompt for a given shelf

These do completely different things. Rename them:
- `checkShelves()` → `findCurrentShelfIndex()`
- `checkShelves(int shelvesNum)` → `promptPickItem(int shelfIndex)`

---

## TASK 12: Verify After Refactoring

After all changes:
1. **Compile**: `javac *.java` must succeed with no errors.
2. **Run existing test cases**: All must still pass — output must be byte-identical.
3. **Checklist**:
   - [ ] No `public` fields in `ForkPosition`, `ItemShelves`, `History`
   - [ ] No `public` fields in `WarehouseMap` (except getters)
   - [ ] Two enum files exist: `CellType.java`, `OperationType.java`
   - [ ] All class names PascalCase
   - [ ] All field/variable names camelCase (no underscores)
   - [ ] All public methods have Javadoc
   - [ ] All 8 files have authorship block
   - [ ] No raw string literals for cell symbols (use `CellType`)
   - [ ] No raw string literals for output messages (use `Messages`)
   - [ ] No magic numbers (use `Constants`)
   - [ ] Array getters return deep copies
   - [ ] No `//TODO` comments remain
   - [ ] `WarehouseGenerator.java` is unmodified

---

## Priority Order

If time is limited, do these first (highest mark recovery):

1. **Task 3** (Enums) → +1.0 mark
2. **Task 4** (Private fields + getters/setters) → +1.0 mark
3. **Task 5** (Privacy leak / deep copy) → +1.0 mark
4. **Task 1 + 2** (Naming conventions) → +0.5 mark
5. **Task 6** (Constants/Messages) → +0.5 mark
6. **Task 7** (Authorship) → +0.5 mark
7. **Task 8 + 9** (Comments + method scope) → +0.5 mark
8. **Task 10 + 11** (Organisation + rename) → +0.5 mark
