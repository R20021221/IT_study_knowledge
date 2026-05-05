# Hidden TC1 Checklist — Input Validation & Edge Cases

This file covers: argument validation, every menu level's input handling, movement validation, and shelf interaction. Use it to audit your code against the spec line by line.

---

## AREA 1: Program Startup (args validation)

### CHECK 1.1: Exactly 3 args required
- `args.length != 3` → print EXACTLY:
  `Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>`
- Then terminate. No menu, no map, no welcome message.

### CHECK 1.2: rows or cols < 4
- If `rows < 4 || cols < 4` → print EXACTLY:
  `Error: Rows and columns must be at least 4 to allow proper map layout.`
- Then terminate. No menu, no map, no welcome message.

### CHECK 1.3: Valid args
- Print: `Welcome to Warehouse Manager Console.`
- Init map.
- Display main menu.

---

## AREA 2: Main Menu Input Validation

### CHECK 2.1: Valid options are integers 1-5 only
- Any integer outside 1-5 (e.g. 9, 0, -1) → print: `Invalid input.` → reprint menu.

### CHECK 2.2: Non-integer single character input (e.g. "p")
- Must NOT crash.
- Print: `Invalid input.` → reprint menu.
- Implementation hint: use `scanner.hasNextInt()` before `scanner.nextInt()`. If false, consume with `scanner.next()`.

### CHECK 2.3: No need to handle decimals or multi-char strings
- Spec says: "You do not need to handle more complex inputs such as decimal numbers or full strings."

---

## AREA 3: Movement SubMenu Input Validation

### CHECK 3.1: Case insensitivity
- `u` and `U` must behave the same. Same for d/D, l/L, r/R, t/T, q/Q.

### CHECK 3.2: Valid move into aisle `.` or start `O`
- Update forklift position.
- Increment move counter (moves++).
- Reprint map header + grid + movement prompt.

### CHECK 3.3: Valid move into shelf `S`
- Update forklift position.
- Increment move counter (moves++).
- Reprint map header + grid.
- Then AUTOMATICALLY show Shelf Menu (no movement prompt yet).
- This applies EVEN IF the shelf has been emptied previously. The shelf menu still appears.

### CHECK 3.4: Blocked move into wall `#`
- Forklift does NOT move.
- Increment hit counter (hits++).
- Record `HIT_WALL` in history (with current moves and hits values).
- Print: `You cannot enter that area.`
- Reprint map + movement prompt.

### CHECK 3.5: Blocked move into restricted `X`
- Forklift does NOT move.
- Increment hit counter (hits++).
- Record `HIT_RESTRICTED` in history (with current moves and hits values).
- Print: `You cannot enter that area.`
- Reprint map + movement prompt.

### CHECK 3.6: Invalid direction input (e.g. "Z", "K", empty)
- Forklift does NOT move.
- Do NOT increment hit counter. (Confirmed by instructor.)
- Record `HIT_WALL` in history (with current moves and hits values — hits unchanged).
- Print: `Invalid input.`
- Reprint map + movement prompt.

### CHECK 3.7: T command — deliver item
- T does NOT move the forklift.
- T does NOT change moves or hits.
- Success condition: forklift is at START (1,1) AND carrying an item.
- On success:
  - Remove item from forklift.
  - Remove item from shelf permanently.
  - Record `PLACE_ITEM` in history with item name.
  - Print: `Item delivered successfully.`
  - Reprint map + movement prompt.
- On failure (not at START, or not carrying):
  - Spec says "Different messages are shown based on the condition" but the exact error messages for these sub-cases are in the original EdStem slide table (not fully in the md). Check EdStem slides for the exact wording.

### CHECK 3.8: Q command — quit to main menu
- Print: `Shift paused.`
- Return to main menu.
- The shift becomes resumable via Option 2.

---

## AREA 4: Shelf Menu Input Validation

### CHECK 4.1: Shelf menu trigger
- Shelf Menu appears AUTOMATICALLY when forklift moves onto any `S` cell.
- Exact prompt:
  ```
  Shelf Menu:
  Press V to view items.
  Press P to pick an item.
  Press Q to exit shelf menu.

  >
  ```

### CHECK 4.2: V — View items
- If shelf has items → list them numbered starting from 1:
  ```
  1. Book
  2. Monitor
  ```
  Then reprint shelf menu.
- If shelf is empty → print: `No items on this shelf.` → reprint shelf menu.
- Record `VIEW_SHELF` in history.

### CHECK 4.3: P — Pick item (NOT carrying, shelf has items)
- Print: `Enter item number to pick (e.g., 1):`
- User enters a valid number → remove item from shelf, forklift carries it.
- Print: `Item picked successfully.`
- Record `PICK_ITEM` in history with the item name.
- Reprint shelf menu.
- On next V, the picked item is gone and indices are reordered (no gaps).

### CHECK 4.4: P — Already carrying an item
- Do NOT prompt for item number.
- Print: `You are already carrying an item. Place it before picking another.`
- Reprint shelf menu.

### CHECK 4.5: P — Shelf is empty
- Do NOT prompt for item number.
- Print: `No items on this shelf.`
- Reprint shelf menu.

### CHECK 4.6: P — Invalid item number
- Prompt appears: `Enter item number to pick (e.g., 1):`
- User enters out-of-range number (e.g. 5 when only 4 items) → print: `Invalid input.`
- Reprint shelf menu.

### CHECK 4.7: Q — Exit shelf menu
- Return to movement mode.
- Reprint full map + movement prompt.
- Forklift remains on the shelf cell until user moves.

---

## AREA 5: Output Format Details (easy to get wrong)

### CHECK 5.1: Map display header (every time map is shown)
```
Warehouse ID: <id>
Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift
Forklift at: (<row>,<col>)
```
Then the grid. Then either movement prompt or shelf menu.

### CHECK 5.2: Grid printing
- Each cell separated by a space.
- `F` overlays the current cell; original symbol (`O`, `S`, `.`) restored when forklift leaves.

### CHECK 5.3: Movement prompt (exact text, every line)
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

### CHECK 5.4: Main menu title
- Use: `=== Warehouse Manager Menu ===`
- Note: Section 6.6 example shows `=== Warehouse Manager ===` (missing "Menu") — this is likely a typo. Use the version with "Menu" as it appears in all other sections.

---

## Quick Self-Test Scenarios

1. Run with 0 args → expect arg count error, no welcome message.
2. Run with `3 3 100` → expect rows/cols error, no welcome message.
3. Start shift, press invalid key (e.g. K) in movement menu → expect `Invalid input.`, map reprint, hits NOT incremented.
4. Move into wall → `You cannot enter that area.`, hits incremented.
5. Move into X → `You cannot enter that area.`, hits incremented.
6. Move onto shelf → map reprints, shelf menu auto-appears.
7. Move onto empty shelf → shelf menu still appears, V shows `No items on this shelf.`
8. P while carrying → `You are already carrying an item. Place it before picking another.` (no item number prompt).
9. P on empty shelf → `No items on this shelf.` (no item number prompt).
10. P with invalid number → prompt appears, then `Invalid input.`
11. Main menu: enter `p` → `Invalid input.`, no crash.
12. Main menu: enter `9` → `Invalid input.`, menu reprints.
