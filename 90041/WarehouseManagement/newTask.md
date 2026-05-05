# Warehouse Manager — Vibe Coding Task

> Java console app. No ArrayList/Arrays.copyOf/System.arraycopy. No System.exit(). Use raw arrays + manual resize.

---

## 0. Constraints

- Only 1D/2D primitive arrays. Manual array resize required (demo it).
- Enums required (e.g., cell type, operation type).
- Max 3 static methods (incl. main, excl. Messages class). Max 4 static vars.
- No method > 100 lines. Max 3 nesting levels.
- Encapsulation: fields private, no privacy leaks (deep copy arrays/objects in getters).
- `final static` constants for magic numbers & repeated strings.
- Do NOT modify `WarehouseGenerator.java`.

---

## 1. Entry & Args Validation

3 cmd args: `rows`, `cols`, `seed` → `java WarehouseManagerEngine <rows> <cols> <seed>`

**Error cases (print & terminate, no menu):**

Wrong arg count:
```
Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>
```

rows or cols < 4:
```
Error: Rows and columns must be at least 4 to allow proper map layout.
```

Valid → print welcome → init map → show main menu:
```
Welcome to Warehouse Manager Console.
```

---

## 2. Map Init

- 2D char/cell array `[rows][cols]`.
- `warehouseID` starts at 1, increments on every init/reset.
- Boundary = `#` (first/last row, first/last col).
- `(1,1)` = START `O`.
- All other inner = `.` aisle.

### Adding X and S (ORDER MATTERS)

1. Use `WarehouseGenerator` to get shelf count (MIN_SHELVES=1, bound=aisles+1).
2. Remaining = aisles - shelves. Get restricted count (MIN_RESTRICTED=1, bound=remaining+1; skip if remaining<2).
3. **Place X first**: loop restricted count, call `findRandomEmptyCell`, accept only if cell==AISLE, else retry. Set cell to X.
4. **Place S second**: same random logic. For each shelf, generate 1-4 items (inclusive), use generator's item name method, add to shelf.

### Forklift Placement

- Place `F` at `(1,1)`, overlaying `O`. Restore `O` when forklift leaves.

### Reset (`reset()` method in WarehouseMap)

- Do NOT recreate WarehouseGenerator or WarehouseMap objects.
- Increment warehouseID, re-init grid, re-place X/S/items, forklift to (1,1), clear moves/hits/carried item.

---

## 3. Main Menu

```
=== Warehouse Manager Menu ===
1. Start warehouse shift.
2. Resume last shift.
3. View operation history.
4. Reset shift and warehouse.
5. Abandon the shift and exit.

>
```

- Invalid int → `Invalid input.` → reprint menu.
- Non-int single char (e.g. `p`) → must not crash → `Invalid input.`
- No need to handle decimals or full strings.

### Option 1: Start Shift

Print map with header:
```
Warehouse ID: <id>
Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift
Forklift at: (<row>,<col>)
```
Then print grid, then movement submenu.

Shift auto-completes when: all shelves visited AND all items picked+delivered AND forklift not carrying.
On complete:
```
Shift completed: all shelves visited and all items processed.
```
Then return to main menu and reset map for new shift.

### Option 2: Resume

If paused shift exists → resume from exact last position, reprint map+movement menu.
If no paused shift:
```
No shift to resume.
```

### Option 3: History

If empty:
```
No operation history available.
```

If exists, print table (exact format):
```
| Warehouse | Type         | Item       | Moves | Hits  | Position            |
|===========|==============|============|=======|=======|=====================|
|         2 | MOVE         | -          | 1     | 0     | (2,1)               |
```
Item = `-` when N/A. Position = `(row, col)`.

Operation types: `MOVE`, `HIT_WALL`, `HIT_RESTRICTED`, `VIEW_SHELF`, `PICK_ITEM`, `PLACE_ITEM`

> **⚠ HIT 计数器规则（已确认）：**
> - 撞墙 `#` → 记录 `HIT_WALL` + **hits++**
> - 撞受限区 `X` → 记录 `HIT_RESTRICTED` + **hits++**
> - 无效方向输入 → 记录 `HIT_WALL`，但 **hits 不递增**
>
> 即：只有实际撞到墙或 X 才增加 hit 计数器，无效输入只记录历史不加计数。

### Option 4: Reset

```
Shift and warehouse reset.
```
- New layout, warehouseID++, forklift→(1,1), moves=0, hits=0, no carried item.
- Clear paused shift (option 2 after reset → `No shift to resume.`).
- Preserve history.

### Option 5: Exit

```
Session abandoned. Goodbye!
```
Graceful loop exit, NO `System.exit()`.

---

## 4. Movement SubMenu

```
Enter direction:
U - Up.
D - Down.
L - Left.
R - Right.
T - Deliver carried item at START (O).
Q - Quit to main menu.

>
```

- Case-insensitive.

### Valid move (target is `.` or `S` or `O`)

- Update forklift pos, moves++, reprint map+prompt.
- If target is `S` → auto-show Shelf Menu after map.

### Blocked move (target is `#`)

- No move, **hits++**, record `HIT_WALL`.
```
You cannot enter that area.
```
Reprint map+prompt.

### Blocked move (target is `X`)

- No move, **hits++**, record `HIT_RESTRICTED`.
```
You cannot enter that area.
```
Reprint map+prompt.

### Invalid direction input

- No move, **hits 不递增**, record `HIT_WALL`.
```
Invalid input.
```
Reprint map+prompt.

### T — Deliver

- Does NOT move forklift. Does NOT change moves/hits.
- Only works at START `(1,1)` with carried item.
- On success:
```
Item delivered successfully.
```
- Remove item from forklift, record PLACE_ITEM in history.
- If not at START or not carrying → appropriate error (see spec context).

### Q — Pause

```
Shift paused.
```
Return to main menu. Shift is resumable.

---

## 5. Shelf Menu

Auto-triggered when forklift moves onto `S`. Also triggers on empty shelves.

```
Shelf Menu:
Press V to view items.
Press P to pick an item.
Press Q to exit shelf menu.

>
```

### V — View

Items exist → list `1. ItemName`, `2. ItemName`, ...
Empty shelf:
```
No items on this shelf.
```
Record VIEW_SHELF in history.

### P — Pick

Already carrying:
```
You are already carrying an item. Place it before picking another.
```

Shelf empty:
```
No items on this shelf.
```

Otherwise prompt:
```
Enter item number to pick (e.g., 1):
```

Valid number → pick item, remove from shelf array (reindex), forklift carries it:
```
Item picked successfully.
```
Record PICK_ITEM in history.

Invalid number:
```
Invalid input.
```

### Q — Exit shelf menu

Return to movement mode. Forklift stays on shelf cell. Reprint map+movement prompt.

---

## 6. Operation History

Persistent across shifts and resets. Lost only on program exit.

Each record: warehouseID, type, itemName (or `-`), moves, hits, position `(r,c)`.

Record on: every valid MOVE, every HIT (wall/restricted/invalid input), every VIEW_SHELF, every PICK_ITEM, every PLACE_ITEM (delivery).

History uses a resizable array (demonstrate array resize).

---

## 7. Classes Structure

Required classes (minimum, add more as needed):

| Class | Purpose |
|---|---|
| `WarehouseManagerEngine` | Main, args validation, menu loop |
| `WarehouseGenerator` | PROVIDED, do not modify |
| `WarehouseMap` | Grid, cell placement, forklift bridge, reset() |
| `ForkPosition` | Forklift state: row, col, moves, hits, carriedItem |
| `itemShelves` | Shelf data: items array, add/remove/view |
| `history` | Single history record |
| `Messages` | Static string constants for all output messages |
| `Constants` | Static final constants (symbols, limits, etc.) |

Enums to create:
- Cell type enum (`WALL`, `AISLE`, `RESTRICTED`, `SHELF`, `START`)
- Operation type enum (`MOVE`, `HIT_WALL`, `VIEW_SHELF`, `PICK_ITEM`, `PLACE_ITEM`)

---

## 8. Key Implementation Notes

1. `F` overlays current cell; restore original symbol on move.
2. Shelf data lives separate from grid — grid always shows `S` even when empty.
3. Each shelf needs its own item array (parallel to grid, or stored in shelf objects at grid coords).
4. Shift completion check runs after every delivery and shelf exit.
5. On shift complete → auto reset map (same as option 4 but no "reset" message, just completion message).
6. History table column widths must match spec exactly (use formatted printf).
7. `findRandomEmptyCell` — only accept AISLE positions, let while loop retry otherwise.
8. Non-int menu input handling: use `Scanner.hasNextInt()` before `nextInt()`, consume bad input with `next()`.
