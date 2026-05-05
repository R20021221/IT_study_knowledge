# Warehouse Manager Implementation Goals

This document整理 Assignment 1 的英文要求，用作后续实现和检查清单。

Status markers:

- `[x]` done or structurally in place
- `[~]` partially done / in progress
- `[ ]` not implemented yet
- `[!]` needs attention before it can be counted as done

## 1. Program Entry and Argument Validation

- [x] The program is a Java console application.
- [~] The main class should accept exactly three command-line arguments:
  - `rows`
  - `cols`
  - `seed`
- [ ] If the number of arguments is not exactly 3, print:

```text
Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>
```

- [ ] If `rows` or `cols` is less than 4, print:

```text
Error: Rows and columns must be at least 4 to allow proper map layout.
```

- [x] Do not hard-code rows, columns, or seed.
- [ ] The program must terminate gracefully after invalid startup input.

## 2. Warehouse Map Initialisation

- [x] The warehouse is a rectangular 2D grid.
- [~] Each warehouse has a unique Warehouse ID starting from 1.
- [ ] Warehouse ID increments every time the map is initialised or reset.
- [x] Cell symbols:
  - `#`: wall
  - `.`: aisle
  - `X`: restricted cell
  - `S`: shelf
  - `O`: start cell
  - `F`: forklift
- [!] Boundary cells must be walls.
- [x] START is always at `(1, 1)` and represented by `O`.
- [x] All other inner cells start as aisles before random placement.
- [x] The forklift starts at START and temporarily replaces `O` with `F`.
- [~] When the forklift moves away, the previous cell symbol must be restored.

Current notes:

- `WarehouseMap` now owns and initialises `ForkPosition`.
- `Engine` uses `warehouse.initializeFork()` and `warehouse.printCurrentWarehouse()` as the Map-layer bridge.
- Boundary logic should be rechecked for non-square maps because row/column comparisons are easy to mix up.

## 3. Random Shelves and Restricted Cells

- [x] Use the provided `WarehouseGenerator`.
- [x] Do not modify `WarehouseGenerator`.
- [x] Restricted cells must be placed before shelves.
- [x] Use the required random empty-cell logic from `WarehouseMap`.
- [x] Restricted cells can only be placed on aisle cells.
- [x] Shelves can only be placed on aisle cells.
- [x] Each shelf must initially contain 1 to 4 random items.
- [x] Shelf items come from the provided item generation method.
- [x] Empty shelves remain visible as `S`.
- [ ] For reset, implement `reset()` in `WarehouseMap`.
- [ ] Do not recreate `WarehouseGenerator` or `WarehouseMap` during reset.

Current notes:

- Map generation flow is in place.
- Shelf item storage currently needs more work before multiple shelves can each keep their own item list.

## 4. Main Menu Goals

The main menu has five options:

1. Start warehouse shift
2. Resume last shift
3. View operation history
4. Reset shift and warehouse
5. Abandon the shift and exit

Rules:

- [x] Main menu loop exists.
- [x] Option 1 starts a warehouse shift path.
- [ ] Options 2, 3, and 4 still need implementation.
- [x] Option 5 exits gracefully with the required message.
- [x] Invalid integer input should print:

```text
Invalid input.
```

- [ ] Simple non-integer input must not crash the program.
- [x] Option 1 starts a shift and shows the movement submenu.
- [ ] Option 2 resumes a paused shift from the exact previous forklift position.
- [ ] If there is no paused shift, print:

```text
No shift to resume.
```

- [ ] Option 3 displays operation history.
- [ ] Option 4 resets the current shift and warehouse.
- [x] Option 5 exits gracefully and prints:

```text
Session abandoned. Goodbye!
```

- [x] Do not use `System.exit()`.

## 5. Shift State

- [x] A shift begins from main menu option 1.
- [~] A shift can be paused from the movement menu with `Q`.
- [ ] A paused shift can be resumed from main menu option 2.
- [ ] Reset clears the current shift state.
- [ ] After reset, option 2 must print `No shift to resume.`
- [~] Move count, hit count, forklift position, carried item, shelf contents, and visited shelves are part of shift state.

Current notes:

- Forklift position state exists inside `WarehouseMap`.
- Moves, hits, carried item, pause/resume state, and visited shelves still need implementation.

## 6. Movement Menu Goals

The movement prompt must be:

```text
U-Up, D-Down, L-Left, R-Right, T-Deliver carried item at START (O), Q-Quit to main menu.
```

Input rules:

- [ ] Prompt text exactly matches the assignment.
- [ ] Inputs are case-insensitive.
- [~] `U`, `D`, `L`, `R` attempt to move the forklift.
- [ ] `T` attempts delivery at START.
- [~] `Q` pauses the shift and returns to the main menu.

Movement rules:

- [~] Moving into `.` is valid.
- [ ] Moving into `S` is valid and opens the shelf menu automatically.
- [x] Moving into `#` or `X` is blocked.
- [ ] A valid move increments move count.
- [ ] A blocked move increments hit count.
- [ ] Invalid movement input increments hit count.
- [x] Blocked movement prints:

```text
You cannot enter that area.
```

- [x] Invalid movement input prints:

```text
Invalid input.
```

- [~] After each movement action, reprint the map and movement prompt unless another menu takes over.

Current notes:

- `U/D/L/R` direction math has been moved into `WarehouseMap`.
- The current `movement()` method checks candidate coordinates but does not yet write successful movement back to `fork.F_row` and `fork.F_col`.
- The movement loop condition still needs attention so `Q` exits the movement loop rather than continuing it.

## 7. Shelf Menu Goals

- [ ] The shelf menu appears automatically when the forklift moves onto a shelf.

Options:

- [ ] `V`: view items
- [ ] `P`: pick an item
- [ ] `Q`: exit shelf menu

Rules:

- [ ] Inputs are case-insensitive.
- [ ] Viewing a non-empty shelf lists items from 1.
- [ ] Viewing an empty shelf prints:

```text
No items on this shelf.
```

- [ ] Picking is allowed only if the forklift is not already carrying an item.
- [ ] If already carrying an item, print:

```text
You are already carrying an item. Place it before picking another.
```

- [ ] If the shelf is empty when picking, print:

```text
No items on this shelf.
```

- [ ] Before picking, prompt:

```text
Enter item number to pick (e.g., 1):
```

- [ ] On successful pick, print:

```text
Item picked successfully.
```

- [ ] Invalid item number prints:

```text
Invalid input.
```

- [ ] Picked items are permanently removed from the shelf.
- [ ] Item indexes must be recalculated on the next view.
- [ ] `Q` exits shelf mode and returns to movement mode.
- [ ] The forklift remains on the shelf cell after exiting the shelf menu.

## 8. Item Carrying and Delivery

- [ ] The forklift can carry at most one item.
- [ ] Items cannot be dropped.
- [ ] Items cannot be returned to shelves.
- [ ] The only way to remove an item from the forklift is delivery at START.
- [~] Delivery uses movement menu command `T`.
- [ ] Delivery does not move the forklift.
- [ ] Delivery does not increment moves or hits.
- [ ] On successful delivery, print:

```text
Item delivered successfully.
```

- [ ] Delivery should remove the carried item from the forklift.
- [ ] Delivery should be recorded in operation history.

## 9. Shift Completion

A shift completes automatically only when all of these are true:

- [ ] All shelves have been visited.
- [ ] All shelf items have been processed.
- [ ] The forklift is not carrying any item.

When complete, print:

```text
Shift completed: all shelves visited and all items processed.
```

After completion:

- [ ] Return to the main menu.
- [ ] Reset the warehouse for a new shift.
- [ ] Preserve operation history.

## 10. Operation History

- [ ] Operation history persists across shifts and resets.

- [ ] History is only lost when the program exits.

Record these operation types:

- [ ] move
- [ ] hit
- [ ] view shelf
- [ ] pick item
- [ ] deliver item

Each record must include:

- [ ] Warehouse ID
- [ ] operation type
- [ ] item name, or `-` if not applicable
- [ ] total moves at that moment
- [ ] total hits at that moment
- [ ] forklift position as `(row, col)`

- [ ] If history is empty, print:

```text
No operation history available.
```

- [ ] If history exists, print a header and then all records in chronological order.

## 11. Reset Behaviour

Main menu option 4 must:

- [ ] Print:

```text
Shift and warehouse reset.
```

- [ ] Generate a new layout.
- [ ] Increment Warehouse ID.
- [~] Return forklift to START `(1, 1)`.
- [ ] Reset moves to 0.
- [ ] Reset hits to 0.
- [ ] Clear carried item.
- [ ] Clear paused/resumable shift state.
- [ ] Preserve operation history.

## 12. Important Implementation Notes

- [~] Be strict with the required output messages.
- [x] Avoid `System.exit()`.
- [ ] Keep menu loops stable and do not crash on simple invalid input.
- [x] Keep map display separate from map data if possible, because `F` temporarily overlays the real cell symbol.
- [~] Store shelf data separately from the visible grid so empty shelves can remain as `S`.
- [ ] Track whether each shelf has been visited.
- [ ] Record history immediately after each required operation.
- [ ] Reset must not erase history.
- [ ] Resume must restore the exact forklift position and current shift state.
- [ ] Completion checks should run after moves, shelf actions, and delivery.
- [x] Be careful with row/column indexing. The assignment uses `(row, col)`, and START is `(1, 1)`.

## 13. Suggested Development Order

1. [ ] Validate command-line arguments.
2. [x] Build and print the warehouse map.
3. [x] Add main menu loop.
4. [~] Implement forklift movement and collision handling.
5. [~] Implement shelf storage and shelf menu.
6. [ ] Implement item carrying and delivery.
7. [ ] Add pause and resume.
8. [ ] Add reset.
9. [ ] Add operation history.
10. [ ] Add shift completion detection.
11. [ ] Test invalid inputs and required messages.

## 14. Current Design Checkpoints

- [x] `WarehouseManagerEngine` creates one `WarehouseMap`.
- [x] `WarehouseMap` owns `ForkPosition`.
- [x] `WarehouseMap` initialises forklift through `initializeFork()`.
- [x] `Engine` uses `warehouse.printCurrentWarehouse()` instead of directly reading forklift coordinates.
- [x] `Engine` uses `warehouse.moveOperation(order)` as the movement bridge.
- [~] `WarehouseMap` is now the main bridge between map display and forklift state.
- [ ] `WarehouseMap` still needs to become the bridge between shelf coordinates and each shelf's item list.
- [ ] `WarehouseMap` still needs to update forklift coordinates after successful movement.
