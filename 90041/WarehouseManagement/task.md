# COMP90041 Assignment 1 — Warehouse Manager 🛺📦

> 来源：EdStem Lesson "Assignment 1 Warehouse Manager"（COMP90041 – Programming and Software Development）
> 本文件汇总了该 Lesson 的全部 15 张 slide 内容与要点。

---

## 目录

Here is a comprehensive summary of slides 1 through 7 of your COMP90041 Assignment 1 (Warehouse Manager) so you can fully understand the homework task.

## Slide 1 — Warehouse Manager Console (Overview)

You will build a console-based (no GUI) warehouse simulation in Java. A forklift operates inside a grid-based warehouse, interacting with shelves and items. The grid's dimensions and layout come from command-line arguments (rows, columns, and a random seed). The seed makes the layout deterministic and reproducible.

Core features during a shift: navigate with U/D/L/R, pause and return to main menu, view shelves and pick exactly one item at a time, carry at most one item, deliver only at the START cell, resume a paused shift from the exact last position, view a full operation history, reset the warehouse (new layout), and abandon and exit.

## Slide 2 — Classes / Entities

The Warehouse Map is a rectangular grid with a unique Warehouse ID starting at 1, generated from the rows/columns/seed arguments. Cells use symbols: `#` Wall, `.` Aisle, `X` Restricted, `S` Shelf, `O` Start, `F` Forklift. The forklift symbol temporarily replaces the symbol of the cell it occupies and the original is restored when it leaves. The START cell is always at (1,1) and is the only place where deliveries happen. Shelves hold one or more items initially; they can be visited multiple times, items can be viewed and picked, and once empty they remain on the map as `S`. When the forklift moves onto a shelf, the Shelf Menu appears automatically. Items have names (e.g., Toolkit, Printer, Box) and can only exist on a shelf or being carried — they cannot be dropped or returned to a shelf. The forklift tracks its position, number of moves, number of hits (blocked moves), and whether it carries an item (max one). A warehouse shift is a continuous session: it starts via "Start warehouse shift", can be paused with `Q`, and ends automatically when all required work is done. The Operation History records all actions across all shifts in the program run, persists across resets, and is only lost when the program quits.

## Slide 3 — Warehouse Management Features

Forklift navigation rules: cannot enter walls (`#`) or restricted cells (`X`); blocked attempts print an error, increment the hit counter, and leave the forklift in place. Valid moves update position and increment the move counter. The forklift carries at most one item, items cannot be dropped or returned to shelves, and the only way to remove an item from the forklift is to deliver it at START. Session management: a shift starts on "Start warehouse shift", pauses on `Q`, and resumes from the main menu. A shift completes automatically when all shelves have been visited, all shelf items processed, and the forklift is empty — after which the warehouse map resets and control returns to the main menu. Operation history records: Warehouse ID, operation type (move, hit, view shelf, pick item, deliver item), item name (if any), forklift position, and total moves and hits at that time. The reset option clears shift state, generates a new layout, increments the Warehouse ID, and preserves history. Abandon must terminate gracefully — do not use `System.exit()`.

## Slide 4 — Map Initialisation

The program takes exactly three command-line arguments: rows, columns, and seed (e.g., `java WarehouseManagerEngine 5 5 100`). Do not hard-code these values. If the count of arguments is wrong, print: `Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>` and terminate. If rows or columns are less than 4, print: `Error: Rows and columns must be at least 4 to allow proper map layout.` and terminate. With valid arguments, print the welcome message, initialise the map, and display the main menu.

To initialise the empty map: create a 2D array of size rows × columns, increment the warehouseID each time the map is initialised/reinitialised, set boundary walls (`#`) on the first/last row and first/last column, mark START at (1,1) with `O`, and mark all other inner cells as aisles (`.`).

Adding shelves and restricted areas is order-sensitive. Step 1: use the provided `WarehouseGenerator` (do not modify it) to generate the number of shelves (between MIN_SHELVES=1 and aisle cells + 1), then compute remaining cells, then generate the number of restricted places (between MIN_RESTRICTED=1 and remaining + 1; if remaining < 2, none are generated). This is provided in `WarehouseMap.java`. Step 2: place restricted cells (`X`) — loop the required number of times, generating random positions via `findRandomEmptyCell` in `WarehouseMap.java`; only accept positions currently marked AISLE, otherwise let the while loop generate again, then update the cell to `X`. Step 3: place shelves (`S`) the same way, then for each accepted shelf generate a random number of items between 1 and 4 (inclusive) and use the provided method in `WarehouseGenerator` to generate random item names that you add to the shelf.

After the map is built, place the forklift at START (1,1); `F` temporarily replaces `O`, which is restored when the forklift moves away. For map reinitialisation, do not recreate `WarehouseGenerator` or `WarehouseMap`; instead implement a `reset()` method in `WarehouseMap` that updates the warehouse ID and refills the aisles, boundaries, and special cells.

## Slide 5 — Program Run: Main Menu

After validating arguments and initialising the map, the program prints the welcome message and main menu with five options: Start warehouse shift, Resume last shift, View operation history, Reset shift and warehouse, Abandon the shift and exit.

Option 1 (Start) begins a shift and shows the movement submenu. When all shelves are visited and empty and the forklift carries nothing, print: `Shift completed: all shelves visited and all items processed.` then return to the main menu and reset the map for a new shift. Option 2 (Resume) resumes from the exact last forklift position if a paused shift exists; otherwise print `No shift to resume.` and return to the main menu. Option 3 (View history) prints `No operation history available.` if empty, otherwise prints a header and one row per recorded operation in chronological order. Columns are: Warehouse, Type, Item (`-` if not applicable), Moves, Hits, Position (printed as `(row, col)`). Option 4 (Reset) prints `Shift and warehouse reset.`, generates a new layout, increments Warehouse ID, returns the forklift to START (1,1), resets moves and hits to 0, ensures the forklift is empty, and preserves the history; if the user picks option 2 immediately after, it prints `No shift to resume.`. Option 5 (Exit) prints `Session abandoned. Goodbye!` and terminates gracefully (no `System.exit()` — penalty of −0.5 marks). For an invalid menu input (e.g., 9 or a non-integer like `p`), print `Invalid input.` and re-prompt. The menu expects integer input but must not crash on simple non-integer input; you do not need to handle decimals or full strings.

## Slide 6 — Movement SubMenu (Forklift Navigation)

After Option 1 or 2, the movement prompt is printed exactly as: U-Up, D-Down, L-Left, R-Right, T-Deliver carried item at START (O), Q-Quit to main menu. Inputs are case-insensitive. A successful move into an aisle (`.`) updates position, increments move counter, and reprints the map and prompt. When the forklift reaches a shelf, the Shelf Menu is automatically printed after showing the updated map; this happens even if the shelf has been emptied previously, so the user must exit the shelf menu to continue moving.

Invalid moves into walls (`#`) or restricted cells (`X`): the forklift does not move, a hit is recorded, the program prints `You cannot enter that area.` and reprints the map and prompt. Invalid direction input (e.g., `Z`, empty): the forklift does not move, a hit is recorded, and the program prints `Invalid input.` and reprints map and prompt.

`T` attempts to deliver the currently carried item; it does not move the forklift. On a successful delivery: remove the item from the shelf permanently, remove it from the forklift, keep the forklift at START, and record a delivery in the history (do not change moves or hits). On success, print `Item delivered successfully.`. `Q` pauses the shift, prints `Shift paused.`, and returns to the main menu.

## Slide 7 — Shelf Interaction (Shelf Menu)

When the forklift moves onto a shelf cell `S`, the Shelf Menu appears automatically with options: V (view items), P (pick an item), Q (exit shelf menu).

V: if items exist, list them in order starting from 1 (e.g., 1. Book, 2. Monitor, …) and reprint the shelf menu; if the shelf is empty, print `No items on this shelf.` and reprint the menu.

P: if the forklift is not carrying anything, prompt `Enter item number to pick (e.g., 1):` and on success print `Item picked successfully.` (you must record this in the operation history). When an item is removed from the middle of the list, the indices are reordered on the next view. If the forklift is already carrying an item, print `You are already carrying an item. Place it before picking another.` and reprint the menu. If the shelf has no items, print `No items on this shelf.` and reprint the menu. If the user enters an invalid item number, print `Invalid input.` and reprint the menu.

Q: exits the shelf menu and returns to movement mode (map + movement prompt). The forklift remains on that shelf cell until the user moves it.

That is the complete picture: you implement a Java console program that validates command-line arguments, builds a deterministic warehouse map, runs a main menu with five options, supports a movement submenu and a shelf submenu with strict input handling and message wording, tracks an operation history, supports pause/resume and reset, and terminates gracefully without `System.exit()`.