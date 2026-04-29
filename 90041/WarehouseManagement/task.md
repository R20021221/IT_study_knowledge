# COMP90041 Assignment 1 — Warehouse Manager 🛺📦

> 来源：EdStem Lesson "Assignment 1 Warehouse Manager"（COMP90041 – Programming and Software Development）
> 本文件汇总了该 Lesson 的全部 15 张 slide 内容与要点。

---

## 目录

1. [Introduction](#1-introduction)
2. [Preamble: The Specifications](#2-preamble-the-specifications)
3. [Preamble: Intended Learning Outcome](#3-preamble-intended-learning-outcome)
4. [Preamble: Academic Honesty 🎓](#4-preamble-academic-honesty-)
5. [Preamble: Java Coding Conventions](#5-preamble-java-coding-conventions)
6. [1. Warehouse Manager Console](#6-1-warehouse-manager-console)
7. [2. Classes or Entities in Warehouse](#7-2-classes-or-entities-in-warehouse)
8. [3. Warehouse Management Features](#8-3-warehouse-management-features)
9. [4. Map Initialisation (Warehouse Manager)](#9-4-map-initialisation-warehouse-manager)
10. [5. Program Run: Main Menu](#10-5-program-run-main-menu)
11. [6. Movement SubMenu: Forklift Navigation](#11-6-movement-submenu-forklift-navigation)
12. [7. Shelf Interaction (Shelf Menu)](#12-7-shelf-interaction-shelf-menu)
13. [Submission and Marking Rubrics](#13-submission-and-marking-rubrics)
14. [Warehouse Manager (容器页)](#14-warehouse-manager-容器页)
15. [FAQ](#15-faq)

---

## 1. Introduction

- 这是 COMP90041 Programming and Software Development 的第一个作业。
- 要在控制台（terminal）实现一个仓库管理（warehouse manager）程序。
- 项目分两阶段：
  - **Assignment 1**：基于目前学到的面向对象基础，搭建系统的初版。
  - **Assignment 2**：在 A1 的基础上扩展功能，并用新学的 OOP 概念重构代码，让代码更优雅。
- 目的：练习 Java 基本概念、OOP 软件开发与设计的基础，体验一个系统从零开始按需求逐步演进的过程。

---

## 2. Preamble: The Specifications

- 这个 Lesson 本身就是规格说明书（specifications）。
- 在真实软件开发中，规格说明详述系统必须实现的功能与需求，由你（开发者）和客户共同确认。要仔细阅读，确保程序实现所有需求。

### Testing and Submission

- 程序会自动测试是否符合规格。点击 **Mark** 按钮即自动提交、编译、测试。可在截止前多次提交。
- 截止后提交标记为延迟，除非有有效 extension，否则不评分；批准的延期会按延期方案评分。
- 提供 **10 个可见测试** + **6 个隐藏测试**。隐藏测试失败看不到原因，但能看到失败结果。强烈建议自己再多写测试。

### How to read the specifications?

文档使用四种颜色提示框：

- 🟩 **Tip（绿色）**：建议使用某些 Java 内置库方法或可做的假设。
  例：`Tip: Use a specific string method like toLowerCase or something.`
- 🟨 **Warning（黄色）**：必须遵守，否则可能输出错误。
  例：`Warning: Read this section carefully. Some additional texts to tell you what can go wrong.`
- 🟦 **Note（蓝色）**：信息性提示，引导下一步操作，不影响正确性。
  例：`Note: Perhaps re-read this section first and then go to the other section.`
- 🟥 **Out of Scope（红色）**：故意排除的场景，不需要处理。
  例：`Out of Scope Scenario: This scenario ........ is out of scope for this assignment.`

> **Assumption**：测试用例只产生规格中明确定义的输出。可见和隐藏测试都不会产生规格之外的输出。规格里有 warnings/notes 的地方需要特别处理，这些都会被评分。

代码片段中 **粗体** 表示用户输入或与上次输出不同的部分，用以帮助辨认重点。

---

## 3. Preamble: Intended Learning Outcome

预期学习成果（ILOs）：

- 用 Java 分析现实问题并设计解决方案，包括解读规格说明。
- 通过类和对象应用 OOP 设计概念。
- 阅读并理解中小型 Java 程序，包括如何使用与扩展提供的脚手架代码。
- 编写中小型 Java 程序，包含若干类和控制台用户界面，按规格实现功能。

涉及概念来自 Week 1 – Week 6：

- 数据类型：char/bool/int/double/String/Objects 等
- 控制台 I/O：Scanner、格式化输出
- 分支：if/else、switch
- 循环：for/while/do-while
- 类：识别实体并把数据与行为封装在一起
- 常量：找出不变的值并定义为常量
- 枚举（Enums）：识别枚举类型并实现
- 数组：1D 和 2D 数组、数组扩容（resizing）

> **禁止**使用 `ArrayList`、`Arrays.copyOf`、`System.arraycopy()` 等便捷方法，目的是测试上述 ILOs。

> **警告**：有 C/Python 等过程式语言经验的同学，注意使用面向对象方式开发，否则会在结构分上失分。

---

## 4. Preamble: Academic Honesty 🎓

### General Guidelines

- 所有评估项目（assignments、tests、exams）必须独立、原创完成。
- 提交的代码会用相似度检测软件与其他同学和已知代码源比对。
- 抄袭或提交非自己写的代码可能导致正式学术不端听证，处罚包括该作业 0 分、挂科，甚至开除。
- 详见大学的 Academic Honesty and Plagiarism 网站，或问讲师。Canvas 中也有 Academic Integrity Module。

### Use of Generative AI

可接受的用途：

- 询问概念解释（如数组、类、枚举）
- 帮助理解错误信息或调试代码
- 请求小例子（如循环、条件判断）
- 用 AI 规划或构建你的方法

不可接受的用途：

- 让 AI 生成完整解答或大段代码
- 复制 AI 代码当作自己的提交
- 提交自己看不懂、解释不了的代码
- 使用本课程范围外或被禁用的特性、库、技巧（即使 AI 推荐）

> AI 可能建议高级方法（如 ArrayList、外部库、复杂模式），如果它们不在课程范围或被禁用，就不能用。

可接受 prompt 示例：

- "Explain how a 2D array works in Java"
- "Why does this error message appear: ArrayIndexOutOfBoundsException?"
- "What does the keyword 'final' do in Java?"
- "How do I check if a position is inside a grid in general terms?"

不可接受 prompt 示例：

- "Write the full Warehouse Manager program"
- "Generate all classes for this assignment"
- "Here is my Shelf class, fix it so the forklift picks up items correctly"
- "Rewrite this method so it compiles"
- 使用依赖被禁用特性（如 ArrayList 或 System.arraycopy）的 AI 解答

你对自己的提交负责，可能被要求解释代码。

### AI Usage Declaration（必填）

在 `WarehouseManagerEngine.java` 末尾必须加一段注释说明 AI 使用方式。**没有声明而又用了 AI**，会被视为潜在学术不端。

示例：

```java
/*
 * AI Usage Declaration:
 * I used ChatGPT to understand how 2D arrays work and to debug a movement
 * issue (understanding the error message only). All class design, program
 * logic, and code were written by me.
 */
```

---

## 5. Preamble: Java Coding Conventions

也会评估代码结构和风格：

- 适当使用方法简化代码、避免重复。
- 类名、变量名、方法名遵守 Java 命名规范，且组织良好、可读。
- 名字要有意义，提升可读性。
- 会提供评分方案指引。

---

## 6. 1. Warehouse Manager Console

- 不需要图形界面，做控制台仓库模拟：叉车 (forklift) 在网格仓库中操作，与货架交互、管理物品。
- 仓库是 row × column 的网格，尺寸和布局由命令行参数 (rows, columns, seed) 决定。相同 seed 总产生相同布局。

### Core Features

- 用方向命令 U/D/L/R 在网格中导航
- 任意时刻暂停 shift 回主菜单
- 进入货架位置可查看物品并一次只拿一件
- 同一时刻最多搬 1 件物品
- 只能在 START 单元格交付物品
- 暂停的 shift 可从原叉车位置精确恢复
- 查看详细操作历史表
- 重置仓库生成新布局
- 放弃 shift 并退出程序

---

## 7. 2. Classes or Entities in Warehouse

本节定义系统的核心领域实体。

### 2.1 Warehouse Map

- 是矩形、网格化布局，显示在控制台上。
- 每张地图有唯一 **Warehouse ID**，从 1 开始。
- 用命令行参数 rows、columns、seed 在运行时生成。布局随机但同 seed 可复现。

5×5 仓库示例：

```
# # # # #
# F . S #
# X X S #
# S S X #
# # # # #
```

### 2.2 Cells and Symbols

- 叉车 `F`：临时占据某格并替换该格符号；离开后原符号 (如 `O` 或 `.`) 恢复。
- **START 单元格**：固定在 `row=1, column=1`，作用：
  - 叉车每次开始 shift 的起点
  - 唯一可以交付物品的位置（其他位置不允许交付）
- **Shelves（货架）**：仓库内的存储位置。
  - 初始包含一个或多个物品（除非后来被搬空）
  - 可被多次访问
  - 物品可查看和拿取
  - 即使货架被搬空，仍然存在并以 `S` 表示
  - 叉车走到货架格上时，自动显示 Shelf Menu
- **Items（物品）**：存放在货架上的对象。
  - 每个物品有名字（如 Toolkit、Printer、Box）
  - 物品要么在货架上，要么被叉车搬运
  - 物品**不能丢在地上**也不能放回货架
  - 拿起后从货架上移除
- **Forklift（叉车）**：代表用户。维护：
  - 当前位置 (row, column)
  - 成功移动次数 (moves)
  - 移动被挡次数 (hits)
  - 是否在搬运物品
  - 不能同时搬两件

### 2.3 Warehouse Session

- 一个 **shift（轮班）** = 一段连续的工作。
- 选择 "Start warehouse shift" 时开始。
- 用 `Q` 暂停后续可恢复。
- 所有必要工作完成后自动结束。

### 2.4 Operation History

- 系统维护一份完整的操作历史，跨当前程序运行内的所有 shift。
- 任意时刻可在主菜单查看，warehouse reset 后仍保留，**程序运行期间不会被清除**（程序退出时会丢失）。

---

## 8. 3. Warehouse Management Features

定义运行时的操作规则、会话行为、历史记录、生命周期控制。

### 3.1 Forklift Navigation Rules

- 叉车不能进入墙 `#` 或受限格 `X`。
- 试图进入被挡格时：
  - 显示错误信息
  - **hit 计数 +1**
  - 叉车留在原位
- 每一次有效移动：
  - 把叉车移到目标
  - **move 计数 +1**
- 同时最多搬 1 件物品。
- 物品不能被丢、不能放在地上、不能放回货架。
- 物品只在 START 处交付时才会从叉车上消失。

### 3.2 Session Management（Warehouse Shift）

- shift = 一次工作会话。
- 一个 shift：
  - 选 "Start warehouse shift" 时开始
  - 用户在移动菜单按 `Q` 暂停
  - 之后可在主菜单恢复
- 自动结束条件（同时满足）：
  - 所有货架都被访问过
  - 所有货架物品都已处理
  - 叉车没有搬任何物品
- 完成后地图重置（见 3.4），回到主菜单。

### 3.3 Operation History

记录字段：

- Warehouse ID
- 操作类型（如 move、hit、view shelf、pick item、deliver item）
- 物品名（如适用）
- 叉车位置
- 该操作发生时累计的 moves 和 hits

操作历史：

- 任意时刻可在主菜单查看
- 跨 warehouse reset 保留
- 程序运行中不会被删除（退出时才丢）

### 3.4 Resetting and Exiting

**Reset Shift and Warehouse**：

- 清掉当前 shift 状态
- 生成新仓库布局
- Warehouse ID +1
- 保留操作历史

**Abandon Shift and Exit**：

- 优雅终止程序
- 显示告别信息
- **不要使用 `System.exit()`**（强制退出）

---

## 9. 4. Map Initialisation (Warehouse Manager)

### 4.1 Command Line Arguments

程序必须接受**恰好 3 个**命令行参数：

- `rows` – 仓库行数（如 5）
- `columns` – 仓库列数（如 5）
- `seed` – 用于确定性生成布局的整数（如 100）

运行方式：

```bash
javac WarehouseManagerEngine.java
java WarehouseManagerEngine 5 5 100
```

> **Warning**：不能硬编码地图尺寸或 seed，EdStem 评分时会自动提供。本地（如 IntelliJ）运行时手动配置参数。

#### 4.1.1 Invalid Arguments

满足以下任一情况，打印指定错误并立即终止；不显示菜单或地图。

- **Case 1**：参数数量错误

```
  Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <rows> <cols> <seed>
```

- **Case 2**：rows 或 columns 小于 4

```
  Error: Rows and columns must be at least 4 to allow proper map layout.
```

#### 4.1.2 Valid Arguments

参数有效时程序必须：

1. 打印欢迎信息
2. 初始化仓库地图
3. 显示主菜单

示例输出：

```
Welcome to Warehouse Manager Console.
=== Warehouse Manager Menu ===
1. Start warehouse shift.
2. Resume last shift.
3. View operation history.
4. Reset shift and warehouse.
5. Abandon the shift and exit.
>
```

### 4.2 Initialising the Empty Warehouse Map

- 仓库地图是 2D 网格，大小由 rows、columns 决定。
- 在放置货架或限制区前，地图必须按以下方式初始化：
  1. 用 rows × columns 初始化一个二维数组。
  2. 每张地图有 warehouseID。每次初始化或重新初始化时，**warehouseID 自增**。
  3. 把第一行、最后一行、第一列、最后一列设为边界墙 `#`。
  4. 在 (1,1) 标记 START 单元格 `O`。
  5. 其余内部格子先暂时全部标记为通道 `.` (Aisle)。

5×5 空仓库示例（随机摆放货架前）：

```
# # # # #
# O . . #
# . . . #
# . . . #
# # # # #
```

### 4.3 Adding Shelves and Restricted Places to the Map

> ⚠️ **Important**：本过程**顺序敏感**。下列步骤必须严格按给定顺序执行，否则初始化错误，自动测试会失败。

#### Step 1：根据 seed 决定数量

- 脚手架已提供 `WarehouseGenerator` 类。**必须**用这个类的方法来生成：货架数、限制区数、货架物品、货架/限制区位置。
- **不能修改** `WarehouseGenerator.java`。
- 货架数 = 用 `MIN_SHELVES = 1` 与 `aisle cells + 1` 生成。
- 剩余格子 = aisle cells 数 - 已生成的货架数。
- 限制区数 = 用 `MIN_RESTRICTED = 1` 与 `remaining cells + 1` 生成；如果剩余格子少于 2，**不生成**限制区。

> **Assumption**：上述代码已在脚手架的 `WarehouseMap.java` 中提供。

#### Step 2：放置受限单元格 `X`

从 1 迭代到 Step 1 算出的限制区数量，循环内：

- 调用 `WarehouseMap.java` 的 `findRandomEmptyCell` 生成 row、column。
- **关键**：随机生成器不知道某格是否已被占用。**只有当该位置当前是 AISLE 时，才接受这次生成的 row/column**；否则让代码里已有的 `while` 循环再随机生成一对，直到找到空 AISLE 为止。
- 你的任务就是把这个判断加进去，然后把该格更新为 RESTRICTED 类型 `X`。

#### Step 3：放置货架 `S`

- 同样用 Step 2 的机制生成位置，检查是否为 AISLE，是则接受，否则继续 while 循环。
- 在该位置放上货架。
- 接受位置后，随机生成 1 到 4（含两端）个物品。
- 从 1 迭代到生成的物品数，每次调用 `WarehouseGenerator` 提供的方法生成一个随机物品名，加到货架上。

完全初始化后的示例（取决于 seed）：

```
# # # # #
# O . S #
# X . S #
# S . X #
# # # # #
```

### 4.4 放置叉车

- 地图完全初始化后，叉车被放在 START (`row=1, column=1`)。
- 叉车用 `F` 显示，**临时替换** `O`。
- 叉车移走后 `O` 恢复。

示例：

```
# # # # #
# F . S #
# X X S #
# S S X #
# # # # #
```

### 4.5 Map Reinitialisation

- 某些情况下需要重新初始化地图。
- **不要重新创建** `WarehouseGenerator` 对象，也**不要改变 rows/columns**，所以 `WarehouseMap` 对象**也不要重新创建**。
- 应该写一个 `reset()` 方法选择性地重置地图。脚手架里已有空壳，按需修改：

```java
// This method should be in the WarehouseMap class
public void reset(){
  // reset the map by updating the warehouse ID and the WarehouseMap grid
  // you should fill the aisle, boundaries and special cells again
  // do not reinitialise the WarehouseGenerator object
}
```

---

## 10. 5. Program Run: Main Menu

- 程序启动时先校验命令行参数（见 4.1）。校验通过先打印：

```
  Welcome to Warehouse Manager Console.
```

- 然后初始化地图（见 §4），再打印主菜单：

```
  Welcome to Warehouse Manager Console.
  === Warehouse Manager Menu ===
  1. Start warehouse shift.
  2. Resume last shift.
  3. View operation history.
  4. Reset shift and warehouse.
  5. Abandon the shift and exit.
  >
```

### 5.1 Option 1：Start Warehouse Shift

选 1 时开始 shift，显示移动子菜单。例：

```
=== Warehouse Manager Menu ===
1. Start warehouse shift.
2. Resume last shift.
3. View operation history.
4. Reset shift and warehouse.
5. Abandon the shift and exit.
> 1
Warehouse ID: 1
Legend: # Wall | . Aisle | X Restricted | S Shelf | O Start | F Forklift
Forklift at: (1,1)
# # # # #
# F X X #
# . . . #
# S S X #
# # # # #
Enter direction:
U - Up.