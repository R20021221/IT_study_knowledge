# INFO90002 期末复习 — SQL · 事务 · DBA 知识整理

> **覆盖文件**：Week5-1 SQL1（34 页）、Week5-2 SQL2（33 页）、Week5-3 SQL3（28 页）、Week7-1 Transactions（44 页）、Week8-1 DBA（34 页）
>
> **使用说明**：每个知识点后会用 `[文件简称 pX]` 标注对应 PDF 页码，方便回查原 slide。例如 `[SQL1 p13]` 指 Week5-1 第 13 页。
>
> **配套外部资源**（贯穿全文）：
> - W3Schools SQL 教程（最推荐，可直接练习）：<https://www.w3schools.com/sql/>
> - MySQL 8.0 官方手册：<https://dev.mysql.com/doc/refman/8.0/en/>
> - LeetCode SQL 50（中文界面也有）：<https://leetcode.com/studyplan/top-sql-50/>
> - 90 分钟 SQL 入门视频（讲师推荐）：<https://www.youtube.com/watch?v=uRdIdd-UkTc>

---

## 目录

1. [SQL 基础概念](#1-sql-基础概念)
2. [DDL — 创建与修改表](#2-ddl--创建与修改表)
3. [DML — SELECT 查询](#3-dml--select-查询)
4. [JOIN 连接](#4-join-连接)
5. [DML — INSERT / UPDATE / DELETE / REPLACE](#5-dml--insert--update--delete--replace)
6. [子查询与视图](#6-子查询与视图)
7. [DCL — 数据控制语言](#7-dcl--数据控制语言)
8. [事务 Transactions](#8-事务-transactions)
9. [并发控制与锁](#9-并发控制与锁)
10. [DBA 角色与 DBMS 架构](#10-dba-角色与-dbms-架构)
11. [索引与性能](#11-索引与性能)
12. [考试要点速查](#12-考试要点速查)

---

## 1. SQL 基础概念

### 1.1 什么是 SQL `[SQL1 p3]`

- **SQL** = Structured Query Language（"sequel"），用于关系型数据库。
- 所有 DBMS 都支持 **CRUD**：Create、Read、Update、Delete。
- SQL 实现 CRUD：`(CREATE, INSERT)`、`SELECT`、`UPDATE`、`(DELETE, DROP)`。

### 1.2 SQL 的四大子语言 `[SQL1 p4]`

| 子语言 | 用途 | 主要命令 |
|---|---|---|
| **DDL**（Data Definition Language） | 定义/搭建数据库结构 | `CREATE`、`ALTER`、`DROP` |
| **DML**（Data Manipulation Language） | 维护和使用数据 | `SELECT`、`INSERT`、`UPDATE`、`DELETE` |
| **DCL**（Data Control Language） | 控制访问权限 | `GRANT`、`REVOKE` |
| **事务/管理类** | 事务控制 | `START TRANSACTION`、`BEGIN`、`COMMIT`、`ROLLBACK` |

### 1.3 SQL 书写规则 `[SQL2 p3]`

- **关键字大小写不敏感**，但约定全部 **大写**（提高可读性）。
- **表名是否区分大小写取决于操作系统**：Linux/Unix 区分（`Account != ACCOUNT`），Windows 不区分。
- **字段名大小写不敏感**：`AccountID == ACCOUNTID == AcCoUnTID`。
- SQL 也能做数学：`SELECT 1*1+1/1-1;`
- 可以创建表中没有的"虚拟列"：`SELECT '123459999' AS MyID;`

> **参考**：ANSI SQL 标准 <https://blog.ansi.org/ansi/sql-standard-iso-iec-9075-2023-ansi-x3-135/>

---

## 2. DDL — 创建与修改表

### 2.1 CREATE TABLE 基本语法 `[SQL1 p7-8]`

```sql
CREATE TABLE BankHQ (
    BankHQID         INT            AUTO_INCREMENT,
    HQAddress        VARCHAR(120)   NOT NULL,
    OtherHQDetails   VARCHAR(100),
    PRIMARY KEY(BankHQID)
);
```

**查看表结构** 三种等价写法：

```sql
DESCRIBE Customer;
DESC Customer;
SHOW COLUMNS FROM Customer;
```

### 2.2 带外键的 CREATE `[SQL1 p11]`

```sql
CREATE TABLE Account (
    AccountID           INT             AUTO_INCREMENT,
    AccountName         VARCHAR(12),
    OutstandingBalance  DECIMAL(10,2)   NOT NULL,
    CustomerID          INT             NOT NULL,
    PRIMARY KEY (AccountID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);
```

外键引用动作（`ON DELETE` / `ON UPDATE`）选项：
- `RESTRICT`：阻止删除/更新被引用的行（默认）
- `CASCADE`：连带删除/更新引用方
- `SET NULL`：将引用方设为 NULL
- `NO ACTION`：与 RESTRICT 类似

### 2.3 ALTER TABLE — 修改表结构 `[SQL2 p30]`

```sql
-- 添加列
ALTER TABLE Customer ADD Email VARCHAR(100);

-- 删除列
ALTER TABLE Customer DROP Email;

-- 重命名表（两种方式）
ALTER TABLE OldName RENAME NewName;
RENAME TABLE OldName TO NewName,
             OldName2 TO NewName2;
```

### 2.4 DROP / TRUNCATE / CTAS `[SQL2 p31]`

| 命令 | 作用 | 是否可回滚 | 危险度 |
|---|---|---|---|
| `DROP TABLE T` | 删除表结构和数据 | **不能**（需备份恢复） | 极高 |
| `TRUNCATE TABLE T` | 删除全部行，保留表结构 | **不能 ROLLBACK**，但比 DELETE 快 | 中 |
| `DELETE FROM T` | 删除全部行 | 可在事务中 ROLLBACK | 中 |

**CTAS — CREATE TABLE AS SELECT**：根据查询结果建新表

```sql
-- 复制表结构 + 数据
CREATE TABLE New_BankHQ AS
SELECT * FROM BankHQ;

-- 只复制结构（无行）— 使用永远为假的条件
CREATE TABLE New_BankHQ AS
SELECT * FROM BankHQ WHERE 1=0;
```

---

## 3. DML — SELECT 查询

### 3.1 SELECT 完整语法骨架 `[SQL1 p13]`

```sql
SELECT [ALL | DISTINCT] select_expr [, select_expr ...]
[FROM table_references]
[WHERE where_condition]
[GROUP BY {col_name | expr}, ...]
[HAVING where_condition]
[ORDER BY {col_name | expr | position} [ASC | DESC], ...]
[LIMIT {[offset,] row_count | row_count OFFSET offset}]
```

> **顺序非常重要**！`HAVING` 不能写在 `GROUP BY` 或 `WHERE` 之前。
> **逻辑执行顺序**（不同于书写顺序）：FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT

### 3.2 投影（Projection）与选择（Selection） `[SQL1 p15-16]`

- **Projection 投影**：决定显示哪些**列** → 在 `SELECT` 子句指定
- **Selection 选择**：决定显示哪些**行** → 在 `WHERE` 子句指定

```sql
SELECT CustLastName             -- 投影：只取 CustLastName 列
FROM Customer
WHERE CustLastName = "Smith";   -- 选择：只取姓 Smith 的行
```

### 3.3 LIKE 模糊匹配 `[SQL1 p17]`

| 通配符 | 含义 |
|---|---|
| `%` | 零个、一个或多个字符 |
| `_` | **单个**字符 |

```sql
WHERE name LIKE 'a%'      -- 以 a 开头
WHERE name LIKE '%a'      -- 以 a 结尾
WHERE name LIKE '%or%'    -- 任意位置含 or
WHERE name LIKE '_r%'     -- 第 2 个字符是 r
WHERE name LIKE 'a_%_%'   -- 以 a 开头且至少 3 字符
WHERE name LIKE 'a%o'     -- a 开头 o 结尾
```

### 3.4 比较与逻辑运算符 `[SQL2 p6]`

**比较**：`=`、`<`、`>`、`<=`、`>=`、`<>` 或 `!=`（不等于）

**逻辑**：`AND`、`OR`、`NOT`

```sql
SELECT * FROM Staff
WHERE (LastName='Nguyen' OR LastName='Smith') AND DeptNo=170;
```

> **易错点**：`OR` 多条件时**一定要加括号**，避免和 `AND` 优先级混淆。

### 3.5 字符串函数 `[SQL2 p7]`

- `UPPER(str)` / `LOWER(str)` — 大小写转换（仅影响 A-Z）
- `LEFT(str, X)` / `RIGHT(str, X)` — 取左/右 X 个字符
- 函数 **不会** 改变表中的真实数据。

### 3.6 聚合函数（Aggregate Functions） `[SQL1 p18-19]`

| 函数 | 含义 | NULL 处理 |
|---|---|---|
| `COUNT(*)` | 行数 | **包含** NULL 行 |
| `COUNT(col)` | col 列非 NULL 值的个数 | **忽略** NULL |
| `SUM(col)` | 求和 | 忽略 NULL |
| `AVG(col)` | 平均值 | 忽略 NULL |
| `MIN(col)` / `MAX(col)` | 最小/最大值 | 忽略 NULL |

> **重要区别**：`COUNT(*)` vs `COUNT(column)`，前者数行（含 NULL），后者数非空值。
>
> **官方文档**：<https://dev.mysql.com/doc/refman/8.0/en/aggregate-functions.html>

### 3.7 GROUP BY / HAVING `[SQL1 p20]`、`[SQL2 p5]`

- `GROUP BY` 把记录按某列分组，常配合聚合函数使用。
- `HAVING` 在分组后过滤"组"，相当于 `WHERE` 但作用于聚合结果。

```sql
-- 每个客户的平均余额，且只看平均余额低于 10000 的
SELECT CustomerID, AVG(OutstandingBalance)
FROM Account
GROUP BY CustomerID
HAVING AVG(OutstandingBalance) < 10000;
```

> **WHERE vs HAVING**：
> - `WHERE` 过滤**行**，在分组前执行
> - `HAVING` 过滤**组**，在分组后执行
> - `WHERE` 不能用聚合函数；`HAVING` 通常包含聚合函数

### 3.8 别名 AS `[SQL1 p21]`

```sql
SELECT custtype AS "Customer Type",
       COUNT(customerid) AS CUST_TOTAL
FROM customer
GROUP BY custtype;
```

含空格的别名要用**双引号**。

### 3.9 ORDER BY、LIMIT、OFFSET `[SQL1 p22-23]`

```sql
-- 默认升序 ASC，多列排序时各列独立指定方向
ORDER BY departmentid DESC, lastname ASC;

-- LIMIT 6: 取前 6 条
-- LIMIT 6 OFFSET 3: 跳过前 3 条，取接下来的 6 条
SELECT ... ORDER BY ... LIMIT 6 OFFSET 3;
```

> **关键点** `[SQL2 p4]`：如果不写 `ORDER BY`，**返回顺序是未定义的**（可能是插入顺序，也可能因优化而变）。需要顺序时**必须**写 `ORDER BY`，不需要时**别写**（影响性能）。

### 3.10 集合运算 `[SQL2 p8-11]`

| 运算 | 含义 | MySQL 支持？ |
|---|---|---|
| `UNION` | 合并去重 | 支持 |
| `UNION ALL` | 合并保留重复 | 支持 |
| `INTERSECT` | 交集 | **不支持**（标准 SQL 支持） |
| `EXCEPT` / `MINUS` | 差集 | **不支持** |

要求：两个查询的列数相同、对应列的数据类型一致（长度可不同）。

```sql
-- 处理列不匹配时使用字面量（literal）
SELECT name, 'Unknown' FROM customer WHERE gender IS NULL
UNION
SELECT name, gender FROM customer WHERE gender IS NOT NULL;
```

---

## 4. JOIN 连接

### 4.1 表名限定与别名 `[SQL1 p25]`

```sql
-- 全限定
SELECT Customer.CustLastName, Customer.CustType FROM Customer;

-- 表别名（推荐，多表 JOIN 时必用）
SELECT c.CustLastName, c.CustType FROM Customer c;
```

> 别名只在**当前 SQL 语句**有效。一旦定义别名，就不能再用原表名引用。

### 4.2 笛卡尔积（Cross Product） `[SQL1 p26]`

```sql
SELECT * FROM Table1 JOIN Table2;
```

每行匹配每行，结果为 m × n 行。**几乎无用**，因为没有连接条件。

### 4.3 INNER JOIN（内连接 / 等值连接） `[SQL1 p27]`

返回**两边都匹配**的行。

```sql
SELECT c.CustomerFirstName, c.CustomerLastName, a.OutstandingBalance
FROM Customer c
INNER JOIN Account a
ON c.CustomerID = a.CustomerID;
```

### 4.4 NATURAL JOIN `[SQL1 p28]`

自动对**同名列**做 INNER JOIN，无需 `ON`。要求两表有相同列名。性能比 INNER JOIN 慢。

### 4.5 OUTER JOIN（外连接） `[SQL1 p30]`、`[SQL2 p16-22]`

| 类型 | 含义 |
|---|---|
| `LEFT [OUTER] JOIN` | 保留左表所有行，右表无匹配填 NULL |
| `RIGHT [OUTER] JOIN` | 保留右表所有行，左表无匹配填 NULL |
| `FULL OUTER JOIN` | 两边全保留 — **MySQL 不支持** |

**找"只在左表、不在右表"的行**（典型场景：找无 boss 的员工）：

```sql
SELECT emp.firstname
FROM employee emp
LEFT OUTER JOIN employee boss ON emp.bossid = boss.employeeid
WHERE boss.employeeid IS NULL;
```

### 4.6 JOIN 性能与可读性 `[SQL2 p23-24]`

性能层级（一般规律）：
- `INNER JOIN` 比 `NATURAL JOIN` 快
- `UNION` 比 `JOIN` 快
- `JOIN` 比子查询快

**EXISTS vs IN**：
- 子查询结果**很大** → `EXISTS` 更快
- 子查询结果**很小** → `IN` 更快
- `IN` **不能**与 NULL 比较；`EXISTS` 可以

**ON vs WHERE**：
- `ON` 用于定义**连接条件**
- `WHERE` 用于**过滤数据**
- 把过滤写到 `ON` 里可能把 OUTER JOIN 误转成 INNER JOIN 行为

### 4.7 旧式 JOIN（避免使用） `[SQL1 p29]`

```sql
-- 不推荐：把连接写在 WHERE 里
SELECT c.CustomerFirstName, a.OutstandingBalance
FROM Customer c, Account a
WHERE c.CustomerID = a.CustomerID;
```

---

## 5. DML — INSERT / UPDATE / DELETE / REPLACE

### 5.1 INSERT 三种写法 `[SQL1 p9]`、`[SQL2 p25]`

```sql
-- 1) 指定列：未列出列用默认值/NULL，主键 AUTO_INCREMENT
INSERT INTO Customer (CustFirstName, CustLastName, CustType)
VALUES ("Peter", "Smith", "Personal");

-- 2) 全字段：必须按顺序提供所有列
INSERT INTO Customer
VALUES (DEFAULT, "James", NULL, "Jones", "JJ Enterprises", "Company");

-- 3) 多行批量插入
INSERT INTO Customer VALUES
  (DEFAULT, "A", NULL, "X", "", "Personal"),
  (DEFAULT, "B", NULL, "Y", "", "Company");

-- 4) 从另一张表插入（表必须已存在）
INSERT INTO ArchiveCustomer SELECT * FROM Customer WHERE inactive = 1;
```

### 5.2 UPDATE 与 CASE `[SQL2 p26-27]`

```sql
-- 一定要写 WHERE，不然全表更新！
UPDATE Employee SET Salary = Salary * 1.05 WHERE Salary <= 100000;

-- 用 CASE 表达式一次性处理多分支
UPDATE Employee
SET Salary = CASE
    WHEN Salary < 100000 THEN Salary * 1.05
    ELSE Salary * 1.10
END;
```

> **顺序错误的坑**：先 `UPDATE Salary>100000 SET Salary=Salary*1.10` 再 `UPDATE Salary<=100000 SET Salary=Salary*1.05` 会让原本 95000 的人加薪后变 99750，被第二条再加 5%。**用 CASE 一次完成更安全**。

### 5.3 DELETE / REPLACE `[SQL2 p28]`

```sql
-- 危险！删除全表数据
DELETE FROM Customer;

-- 安全：加 WHERE
DELETE FROM Customer WHERE CustomerID = 5;

-- REPLACE：与 INSERT 几乎相同；如果主键已存在，则覆盖该行
REPLACE INTO Customer VALUES (5, "New", NULL, "Name", "", "Personal");
```

注意外键约束：`ON DELETE CASCADE` / `ON DELETE RESTRICT`。

---

## 6. 子查询与视图

### 6.1 子查询比较运算符 `[SQL3 p5]`

| 运算符 | 含义 |
|---|---|
| `IN` / `NOT IN` | 属性值是否在子查询结果中 |
| `ANY` | 任一值满足条件即为 TRUE |
| `ALL` | 所有值都满足条件才为 TRUE |
| `EXISTS` | 子查询返回 ≥1 行就为 TRUE |

> **参考**：<https://www.w3schools.com/sql/sql_any_all.asp> · <https://www.w3schools.com/sql/sql_exists.asp>

### 6.2 子查询示例 `[SQL3 p8-13]`

```sql
-- 列出对 Artefact 1 出过价的所有买家
SELECT * FROM Buyer
WHERE BuyerID IN (
    SELECT BuyerID FROM Offer WHERE ArtefactID = 1
);

-- 等价的 JOIN 写法（一般更高效）
SELECT *
FROM Buyer b
INNER JOIN Offer o ON b.BuyerID = o.BuyerID
WHERE o.ArtefactID = 1;

-- 等价的 EXISTS 写法（关联子查询）
SELECT * FROM Buyer
WHERE EXISTS (
    SELECT * FROM Offer
    WHERE Buyer.BuyerID = Offer.BuyerID AND ArtefactID = 1
);

-- 反向：没有任何出价的 artefact
SELECT * FROM Artefact
WHERE ArtefactID NOT IN (SELECT ArtefactID FROM Offer);
```

### 6.3 视图 VIEW `[SQL3 p15-18]`

> **视图**：物理模型中不存在但作为虚拟关系提供给用户使用的关系。

**好处**：
- 隐藏查询复杂度
- 隐藏敏感数据（不同用户给不同视图，提升安全性）
- 视图始终展示**最新数据**（存的是查询定义，不是数据）

```sql
CREATE VIEW v_DEPT_SALARY AS
SELECT department.departmentid, department.name, SUM(employee.salary) AS DEPTSAL
FROM department
INNER JOIN employee ON department.departmentid = employee.departmentid
GROUP BY department.departmentid, department.name;

-- 之后可以像查表一样用
SELECT * FROM v_DEPT_SALARY WHERE DEPTSAL > 500000;
```

### 6.4 写好查询的思路 `[SQL3 p22-24]`

1. 把数据库设计图当作**地图**
2. 把 `SELECT` 结构当作**模板**
3. 一步步**填充**：
   - 要哪些字段？ → SELECT
   - 来自哪些表？ → FROM + JOIN
   - 过滤条件是什么？ → WHERE
   - 是否要分组、聚合？ → GROUP BY / HAVING
   - 是否要排序？ → ORDER BY

---

## 7. DCL — 数据控制语言

### 7.1 用户与权限 `[SQL3 p20]`

```sql
-- 用户管理
CREATE USER 'username'@'host' IDENTIFIED BY 'password';
DROP USER 'username'@'host';
SET PASSWORD = 'newpassword';

-- 权限管理
GRANT SELECT, INSERT ON dbname.* TO 'username'@'host';
REVOKE INSERT ON dbname.* FROM 'username'@'host';
```

### 7.2 其他管理命令

- `BACKUP TABLE` / `RESTORE TABLE`（数据库备份与恢复）
- `ANALYZE TABLE`（更新统计信息）
- `DESCRIBE tablename`（查看表结构）
- `USE db_name`（切换数据库）

---

## 8. 事务 Transactions

### 8.1 什么是事务 `[Trans p5-6]`

> **事务（Transaction）**：必须**完整完成或完全放弃**（不可分割、原子）的逻辑工作单元。

- 单条 DML 已经是原子的（隐式事务）。
- RDBMS 还支持**用户自定义事务**：把多条 DML 包成一个原子单元。

**业务事务的典型例子**：
- 下订单：插入 Order 行 + 多条 OrderItem 行
- 转账：检查余额 → 一边减 → 另一边加 → 记录流水
- 群发月结：遍历 Customer，生成对账单

### 8.2 ACID 四大特性 `[Trans p11]`（**重点考点**）

| 特性 | 含义 |
|---|---|
| **A**tomicity 原子性 | 事务是不可分割的逻辑单元；要么全部完成，要么全部回滚 |
| **C**onsistency 一致性 | 事务前后约束都成立；数据库从一个一致状态变到另一个一致状态 |
| **I**solation 隔离性 | 多事务并发时，中间状态对其他事务不可见，直到提交 |
| **D**urability 持久性 | 事务一旦提交，更改永久保存（即使系统崩溃） |

### 8.3 事务控制语法 `[Trans p8]`

```sql
START TRANSACTION;   -- 或 BEGIN;
    SQL 语句 1;
    SQL 语句 2;
    ...
COMMIT;              -- 永久保存全部更改

-- 或者出错时
ROLLBACK;            -- 撤销整个事务
```

### 8.4 事务范例（Exercise 2 解答） `[Trans p14]`

记录一笔 G43546 数量为 2 的销售：

```sql
START TRANSACTION;
SET @qty = 2;
SET @price = (SELECT RetailPrice FROM Product WHERE ProdID='G43546');
SET @amount = @price * @qty;

INSERT INTO Action VALUES (DEFAULT, '2024-04-15', "Purchase", "G43546", @qty, @amount);
UPDATE ProductStockLevel SET QtyInStock = QtyInStock - @qty WHERE ProdID='G43546';
COMMIT;
```

> **设计原则**：事务里的操作必须**互相相关**。例如"更新库存"和"促销改价"不应放在同一事务。

---

## 9. 并发控制与锁

### 9.1 并发的三大经典问题 `[Trans p17-20]`（**重点考点**）

| 问题 | 描述 | 违反的特性 |
|---|---|---|
| **Lost Update 丢失更新** | 两事务都更新同一数据，后写者覆盖了前写者 | 隔离性 |
| **Uncommitted Data 脏读** | 读到了另一事务尚未提交、且最终回滚的数据 | 隔离性 |
| **Inconsistent Retrieval 不一致检索** | 一事务做聚合时，另一事务在中间更新数据，结果一半旧一半新 | 一致性 |

#### 例：Lost Update

| 时间 | Alice | Bob |
|---|---|---|
| t1 | 读余额 = 1000 | 读余额 = 1000 |
| t2 | 取 100，余额 = 900 | 取 800，余额 = 200 |
| t3 | 写入 900 | 写入 200（覆盖了 Alice 的 900） |

实际余额应为 100，但变成 200 — Alice 的取款丢了。

### 9.2 可串行化（Serializability） `[Trans p21]`

并发事务执行的结果，与按某种串行顺序执行的结果一致。**调度器（Scheduler）** 是 DBMS 中负责安排并发操作顺序、保证 isolation 与可串行化的进程。

### 9.3 并发控制方法 `[Trans p24]`

- **锁（Locking）** — 主流方法
- **时间戳（Timestamping）**
- **乐观（Optimistic）方法**

### 9.4 锁的粒度 `[Trans p26-27]`

| 粒度 | 描述 | 优缺点 / 典型 DBMS |
|---|---|---|
| **数据库级** | 锁整库 | 适合批处理，**不适合多用户**（SQLite, Access） |
| **表级** | 锁整表 | 适合大批量更新；**T1、T2 用不同表可并发**；可能瓶颈 |
| **页级** | 锁一个磁盘页（含多行） | 中等粒度 |
| **行级** | 锁单行 | 并发好但开销高，是当前主流（**MySQL、Oracle**） |
| **字段级** | 只锁某行的某列 | 最细，**开销极高**，很少用 |

### 9.5 锁的类型 `[Trans p28-30]`

**二进制锁（Binary Lock）**：锁/未锁两态，过于严格（即使两个 READ 也互斥）。

**共享锁（Shared / Read Lock）** vs **排他锁（Exclusive / Write Lock）**：

| 持有锁 ↓ \ 想加锁 → | Shared | Exclusive |
|---|---|---|
| **Shared** 已持有 | ✅ 可加 | ❌ 不能 |
| **Exclusive** 已持有 | ❌ 不能 | ❌ 不能 |
| **无锁** | ✅ 可加 | ✅ 可加 |

**MySQL 语法**：
```sql
-- 排他锁（写）
SELECT ... FOR UPDATE;
LOCK TABLES t WRITE;

-- 共享锁（读）
SELECT ... FOR SHARE;       -- 旧版叫 LOCK IN SHARE MODE
LOCK TABLES t READ;
```

> **参考**：<https://www.geeksforgeeks.org/lock-based-concurrency-control-protocol-in-dbms/>

### 9.6 死锁（Deadlock） `[Trans p31-34]`

> 两个事务互相等对方解锁的环。

- T1 锁 X 想要 Y；T2 锁 Y 想要 X → 死锁
- 也可能是循环：T1→T2→T3→T1
- **死锁只发生在排他锁之间**；共享锁不会死锁

**应对方法**：
- **超时（Timeout）**
- **预防（Prevention）**：例如总是按相同顺序加锁
- **检测（Detection）**：周期扫描事务日志找死锁环，选一个事务回滚（"victim"）
- **避免（Avoidance）**：用算法判断是否会形成环

### 9.7 严格两阶段锁定 Strict 2PL `[Trans p35]`（**重点**）

两个阶段：
1. **Growing（增长阶段）**：事务必须在读/写前**获得所有锁**，期间不释放任何锁
2. **Shrinking（收缩阶段）**：事务**完成时**才释放所有锁

> 严格 2PL 保证了可串行化，但可能导致死锁。

#### 2PL 加锁练习 `[Trans p37]`

对前面的销售事务，行级 2PL 加锁的执行顺序大致为：

| 时序 | 动作 |
|---|---|
| 1 | Lock Product 行（ProdID = G43546） |
| 2 | Lock Action 表 |
| 3 | Lock ProductStockLevel 行（ProdID = G43546） |
| 4 | @qty = 2 |
| 5 | @price = 从 Product 读取 |
| 6 | INSERT 一行到 Action |
| 7 | UPDATE ProductStockLevel |
| 8-10 | 全部 Unlock |

### 9.8 锁的替代方案 `[Trans p38-39]`

**多版本并发控制（MVCC）**：MySQL 用此机制，写不阻塞读，读不阻塞写。每次更新创建新版本，读者看旧版本直到写者提交。

**时间戳法**：给每个事务全局唯一时间戳；旧事务不能读新事务改过的数据，旧事务也不能写新事务读/改过的数据。

**乐观法**：假设大部分事务不冲突，先执行，提交时再检查是否有冲突，有则回滚。

### 9.9 事务日志与恢复 `[Trans p40-41]`

事务日志记录：
- 事务开始标记
- 每条 SQL：操作类型 + 受影响对象 + **before/after** 值 + 前后日志指针
- 事务结束（COMMIT）

**作用**：
- **回滚（ROLLBACK）**：用 before 值恢复
- **崩溃恢复**：检查未提交/未完成事务，回到先前一致状态

---

## 10. DBA 角色与 DBMS 架构

### 10.1 DA vs DBA `[DBA p5]`（**考点**）

| 数据管理员 DA（Data Administrator / CDO） | 数据库管理员 DBA（Database Administrator） |
|---|---|
| 管理角色 | 技术角色 |
| 数据政策、流程、标准 | 分析与设计 DB |
| 法规合规（GDPR、隐私法） | 选 DBMS、工具、供应商 |
| 公司政策合规 | 安装与升级 DBMS |
| 规划 | 调优性能 |
| 数据冲突解决 | 管理安全、隐私、完整性 |
| 信息库管理 | 备份与恢复 |
| 内部宣传与培训 | — |

> 在小公司，开发者可能兼任 DBA；云数据库（DBaaS）会让 DBA 角色变小。

### 10.2 DBMS 架构总览 `[DBA p9-13]`

DBMS 由三大类组件组成：

```
┌──── Query Processing ────┐    ┌──── Concurrency Control ────┐
│  Parser / Compiler       │    │  Transaction Manager        │
│  Optimizer               │    │  Lock Manager               │
│  Executor                │    │                             │
└──────────────────────────┘    └─────────────────────────────┘
                       Crash Recovery │ Log Manager

                ┌──── Storage Layer ────┐
                │  File & Access Methods │
                │  Buffer Pool (Memory)  │
                │  Disk Space Mgt        │
                └────────────────────────┘
```

### 10.3 缓存（Buffer Pool） `[DBA p11]`

- RAM 快但易失，磁盘慢但持久 → **持久存盘 + 缓存内存 + 减少磁盘读**
- **Buffer Pool**：缓存表数据和索引数据
- **Buffer Manager**：尽量保留热数据在内存

### 10.4 查询处理流程 `[DBA p13]`

| 阶段 | 任务 |
|---|---|
| **Parsing 解析** | 检查 SQL 语法 + 用户权限 |
| **Optimising 优化** | 制定执行计划，评估索引 vs 全表扫描，最小化 cost |
| **Execution 执行** | 保证 ACID；按需在内存与磁盘间移动数据 |

### 10.5 各管理器职责 `[DBA p15-17]`

- **Transaction Manager**：跟踪谁要哪个资源
- **Lock Manager**：维护锁的分配和等待队列
- **Log Manager**：记录所有更改
  - **UNDO**：旧值（用于回滚）
  - **REDO**：变更细节（用于崩溃恢复）
  - 也记录数据字典变化

### 10.6 一次完整事务的 9 步 `[DBA p17]`

1. Parser 解析、检查权限、创建并优化执行计划
2. 检查表数据是否已在 Buffer Pool（命中跳到 4，否则 3）
3. 从磁盘读入数据
4. Transaction Manager 确认数据已就绪
5. Lock Manager 给请求行加锁
6. 执行 SQL
7. 记录 undo（旧值到 undo 日志）+ buffer 中写新值；Log Manager 记录 redo
8. COMMIT 使变更永久
9. 写事务日志到磁盘，释放锁；最终新数据写盘

### 10.7 影响数据库性能的因素 `[DBA p18]`（**考点**）

- **Buffer Pool 缓存**
- 数据文件在磁盘上的**摆放**
- **快速可靠的存储**（SSD、RAID）
- **复制和集群**
- **索引**加速查找和连接
- 选择合适的**数据类型**（尤其主键）
- 程序逻辑（避免长事务）
- 避免死锁
- 良好的查询执行计划

---

## 11. 索引与性能

### 11.1 为什么需要索引 `[DBA p20-22]`

- 表扫描（Table Scan）一行一行从头读，**极慢**
- 10 万行平均要 5 万次磁盘访问才能找到一行
- 维持表本身有序代价高（每次插入都要重排）
- **索引的解法**：把"搜索字段 → 行位置"映射，单独存成一个**有序的小结构**

### 11.2 索引结构与查找 `[DBA p23-25]`

索引存：`Key | RowLoc`，按 Key 排序。

**二分查找（Binary Search）**：每次砍一半，O(log n)。

找到 Key 后，跳到对应 RowLoc 拿整行。

### 11.3 索引的维护 `[DBA p26-28]`

- 表有主键时，**自动创建** PK 索引。
- 每次 INSERT/UPDATE/DELETE，索引必须自动维护排序。

### 11.4 索引的代价 `[DBA p29]`

为什么不给每列都建索引？
- 磁盘空间：小问题（盘便宜）
- **更新时间：大问题** — 每次写表，每个相关索引都要更新
- **索引太多 → 写入变慢**

### 11.5 何时该建索引 `[DBA p30]`（**重点**）

为以下列建索引：
- WHERE 子句频繁查询的列
- JOIN 用的列（PK ↔ FK）
- 主键（多数 DBMS 自动建）
- 外键（**MySQL** 自动建）
- 唯一列（多数 DBMS 自动建）

**额外原则**：
- 只给**大表**建索引（小表不需要）
- 只在**经常返回 < 15% 行**时才用索引

### 11.6 优化器是否使用索引 `[DBA p31-32]`

DBMS 有 **Optimiser**，根据 cost 选最优执行路径：
- 表很小 → 全表扫一两次磁盘读就够了，比走索引还快
- 选择性差（命中行很多）→ 索引未必有用

```sql
-- 给 WHERE 经常用的列建索引
CREATE INDEX cust_city_idx ON Customers(cust_city);
```

> **进一步**：MySQL 索引底层是 **B+ Tree**（部分场景用 Hash）。可读 <https://dev.mysql.com/doc/refman/8.0/en/optimization-indexes.html>

---

## 12. 考试要点速查

### 12.1 各 PDF 末尾标的 examinable 部分

- **SQL1** `[p31]`：DDL、DML、SELECT
- **SQL2** `[p32]`：SELECT、DML（INSERT/UPDATE/DELETE/REPLACE）、DDL（CREATE/ALTER/DROP）
- **SQL3** `[p27]`：写 SQL —— DDL、DML
- **Transactions** `[p43]`：
  - 为何需要用户自定义事务
  - 事务属性（ACID）
  - 事务用法（BEGIN/START TRANSACTION/COMMIT/ROLLBACK）
  - 并发访问策略
  - 锁类型（Binary、Shared）
  - 数据库恢复基础
- **DBA** `[p33]`：
  - DBA 与 DA 的区别
  - 数据库架构组件
  - 影响性能的因素
  - 何时建索引

### 12.2 易错点 / 高频陷阱

1. **`COUNT(*)` vs `COUNT(col)`**：前者算行（含 NULL），后者算非 NULL 值
2. **`WHERE` 不能用聚合函数**，要用 `HAVING`
3. **`OR` 多条件忘加括号**会被 `AND` 优先级吃掉
4. **`UPDATE`/`DELETE` 不写 `WHERE`** = 全表炸
5. **`TRUNCATE` 不能 ROLLBACK**，`DELETE` 可以
6. **`LIKE` 通配符**：`%` 多字符、`_` 单字符
7. **MySQL 不支持 `INTERSECT`、`EXCEPT`、`FULL OUTER JOIN`**
8. **`IN` 不能比较 NULL**；`EXISTS` 可以
9. **死锁只发生在排他锁之间**
10. **2PL 两阶段**：增长阶段只加锁，收缩阶段只放锁
11. **MySQL 默认行级锁** + **MVCC**（写不阻塞读）
12. **不写 `ORDER BY` 时，结果顺序不可预测**

### 12.3 重要语法对照备忘

```sql
-- 完整 SELECT
SELECT [DISTINCT] cols
FROM tables
[INNER/LEFT/RIGHT JOIN ... ON ...]
WHERE row_condition
GROUP BY cols
HAVING group_condition
ORDER BY cols [ASC|DESC]
LIMIT n OFFSET m;

-- 事务
START TRANSACTION;
  -- SQL ...
COMMIT;  -- 或 ROLLBACK;

-- 显式锁定
SELECT ... FOR UPDATE;   -- 排他
SELECT ... FOR SHARE;    -- 共享
```

---

## 配套学习路径建议

1. **复习时**：从本文目录开始，每节点开 PDF 对应页核对原图
2. **练手**：W3Schools 在线练 → LeetCode SQL 50（前 30 题足够）
3. **难点专项**：
   - JOIN 不熟 → 看 W3Schools JOIN 章 + Venn 图
   - 事务/锁不熟 → 重看 Trans p17-20 三个经典并发问题动画
   - 索引不熟 → 看 DBA p20-28 的可视化插入过程

> 本文按"主题合并"组织，对应的"知识树"结构请见同目录的 `INFO90002_知识树.md`。
