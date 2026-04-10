# INFO90002 Database Systems & Information Modelling — Mid-Term 复习指南

> University of Melbourne | 基于 Week 1–4 全部 Lecture 内容整理，附补充常考知识点

---

## 目录

1. [数据库基础概念 (Week 1)](#1-数据库基础概念-week-1)
2. [数据建模与 ER 模型 (Week 2)](#2-数据建模与-er-模型-week-2)
3. [键 (Keys) 与逻辑设计 (Week 3)](#3-键-keys-与逻辑设计-week-3)
4. [物理设计与 MySQL 数据类型 (Week 4-1)](#4-物理设计与-mysql-数据类型-week-4-1)
5. [规范化 Normalisation (Week 4-2)](#5-规范化-normalisation-week-4-2)
6. [常考题型与补充知识点](#6-常考题型与补充知识点)

---

## 1. 数据库基础概念 (Week 1)

### 1.1 Data vs Information

- **Data（数据）**：原始的、未经处理的事实和数字，本身没有意义。例如：`42`, `Melbourne`, `2024-03-01`
- **Information（信息）**：经过处理、组织后具有意义和上下文的数据。例如："Melbourne 在 2024-03-01 的温度为 42°C"
- 核心关系：**Data + Context + Processing = Information**

### 1.2 数据的分类

| 类型 | 定义 | 示例 |
|------|------|------|
| **Structured（结构化数据）** | 有预定义格式，存储在表格/行列中 | 关系数据库中的表 |
| **Semi-Structured（半结构化数据）** | 不完全符合表格格式，但有标签或标记 | XML, JSON, HTML |
| **Unstructured（非结构化数据）** | 没有预定义格式 | 图片、视频、文本文件、电子邮件 |

### 1.3 DBMS（数据库管理系统）

**定义**：DBMS 是一组管理数据库的创建、维护和使用的程序集合。

**DBMS 的核心优势**：

- **Data Independence（数据独立性）**：应用程序不需要知道数据的物理存储方式
- **Efficient Data Access（高效数据访问）**：使用专门的技术高效存储和检索数据
- **Data Integrity and Security（数据完整性与安全性）**：强制实施完整性约束，控制访问权限
- **Concurrent Access（并发访问）**：允许多个用户同时访问数据而不冲突
- **Crash Recovery（崩溃恢复）**：在系统故障后能恢复数据到一致状态
- **Reduced Application Development Time（减少开发时间）**：提供高层接口，简化编程

### 1.4 关系数据模型

- 由 **Ted Codd** 于 **1970年** 提出
- 数据以 **表 (Table/Relation)** 的形式组织
- 每一行是一个 **元组 (Tuple/Row/Record)**
- 每一列是一个 **属性 (Attribute/Column/Field)**

**关系数据库术语对照表**：

| 正式术语 | 替代术语 1 | 替代术语 2 |
|---------|----------|----------|
| Relation | Table | — |
| Attribute | Column | Field |
| Tuple | Row | Record |

### 1.5 Schema vs Instance

- **Schema（模式）**：数据库的结构定义（表名、列名、数据类型、约束等），相对稳定不变
- **Instance（实例）**：数据库在某一时刻的实际数据内容，经常变化

### 1.6 Cardinality vs Degree (Arity)

- **Cardinality（基数）**：表中行（记录）的数量
- **Degree / Arity（度）**：表中列（属性）的数量

### 1.7 Client-Server Architecture（客户端-服务器架构）

- **Server（服务器端）**：MySQL Server — 存储和管理数据
- **Client（客户端）**：MySQL Workbench — 用户操作界面，发送 SQL 查询

---

## 2. 数据建模与 ER 模型 (Week 2)

### 2.1 数据库开发生命周期

```
Conceptual Design（概念设计）
    ↓
Logical Design（逻辑设计）
    ↓
Physical Design（物理设计）
    ↓
Implementation（实施）
    ↓
Instance（数据库实例/运行）
```

### 2.2 ER 模型核心概念

#### Entity（实体）

- 代表现实世界中可区分的对象或概念
- 在 ER 图中通常用矩形表示
- **Strong Entity（强实体）**：有自己的主键，可以独立存在
- **Weak Entity（弱实体）**：没有自己的完整主键，依赖于强实体存在（需要借助强实体的主键来唯一标识）

#### Attributes（属性）

| 属性类型 | 定义 | 示例 |
|---------|------|------|
| **Simple（简单属性）** | 不可再分的原子值 | FirstName |
| **Composite（复合属性）** | 可以分解为更小的子部分 | Address → Street, City, State |
| **Derived（派生属性）** | 可从其他属性计算得出 | Age（从 DateOfBirth 计算）|
| **Multivalued（多值属性）** | 一个实体可以有多个值 | Phone Numbers |
| **Single-valued（单值属性）** | 每个实体只有一个值 | StudentID |

#### Relationships（关系）

- 描述实体之间的关联
- **Binary Relationship（二元关系）**：两个实体之间的关系（最常见）
- **Unary Relationship（一元关系/递归关系）**：同一个实体内部的关系（如 Employee manages Employee）

### 2.3 Connectivity（连接性/对应关系）

| 类型 | 含义 | 示例 |
|------|------|------|
| **1:1 (One-to-One)** | A 的一个实例最多对应 B 的一个实例 | Person — Passport |
| **1:M (One-to-Many)** | A 的一个实例可以对应 B 的多个实例 | Department — Employee |
| **M:M (Many-to-Many)** | A 的多个实例可以对应 B 的多个实例 | Student — Course |

### 2.4 Cardinality / Participation（参与度）

- **Mandatory（强制参与/Total Participation）**：实体的每个实例都必须参与关系
  - Crow's Foot 表示法中用 **实线 + 竖线（|）** 表示
- **Optional（可选参与/Partial Participation）**：实体的实例可以不参与关系
  - Crow's Foot 表示法中用 **虚线 + 圆圈（○）** 表示

### 2.5 Crow's Foot Notation（鸦爪表示法）

这是本课程主要使用的 ER 图表示法。关键符号：

- **||** （两条竖线）= 一 (one)，且必须参与 (mandatory)
- **O|** （圆圈 + 竖线）= 一 (one)，可选参与 (optional)
- **>|** 或 **⋈|** （鸦爪 + 竖线）= 多 (many)，且必须参与 (mandatory)
- **>O** 或 **⋈O** （鸦爪 + 圆圈）= 多 (many)，可选参与 (optional)

### 2.6 Business Rules（业务规则）

- 业务规则定义了数据的约束条件和组织中的运作规范
- ER 模型通过 connectivity 和 cardinality 来体现业务规则
- 例如："每个部门必须有一个经理" → Department 对 Manager 是 mandatory participation

---

## 3. 键 (Keys) 与逻辑设计 (Week 3)

### 3.1 键的层次结构

```
Superkey（超键）
  ↓ 去掉冗余属性
Candidate Key（候选键）
  ↓ 选择一个
Primary Key（主键）
```

#### 各类键的定义

| 键类型 | 定义 |
|-------|------|
| **Superkey（超键）** | 能唯一标识关系中每个元组的属性集合（可包含冗余属性） |
| **Candidate Key（候选键）** | 最小超键——去掉任何一个属性都不能再唯一标识（无冗余） |
| **Primary Key（主键）** | 从候选键中选出的、用于唯一标识每个元组的键 |
| **Foreign Key（外键）** | 引用另一个关系主键的属性，用于建立表间联系 |
| **Surrogate Key（代理键）** | 系统生成的无业务含义的键（如自增 ID） |
| **Natural Key（自然键）** | 具有实际业务含义的键（如身份证号、学号） |

### 3.2 主键的性质

- **唯一性 (Uniqueness)**：每个值必须在表中唯一
- **非空性 (NOT NULL)**：主键值不能为空
- **不可变性 (Immutability)**：一旦赋值，理想情况下不应更改
- **最小性 (Minimality)**：使用尽可能少的属性

### 3.3 Composite Key（复合键）

- 由两个或多个属性组合构成的主键
- 常见于 M:M 关系的关联表（Associative Entity）中
- 例如：LOAN 表的 PK 为 (MemberNo, ToyNo, DateBorrowed)

### 3.4 Foreign Key（外键）与 Referential Integrity（引用完整性）

- 外键的值必须在被引用表的主键中存在，或者为 NULL
- **Referential Integrity（引用完整性）**：保证外键引用的一致性
- 违反引用完整性的情况：外键值不在被引用表的主键列中

### 3.5 Toy Library Case Study 核心要点

这是课程中的一个重要案例，展示了如何测试和验证键的选择：

- Member-Toy 是 **M:M** 关系，optional participation on both sides
- 初始设计 LOAN(MemberNo, ToyNo, DateBorrowed, DateReturned)，PK = (MemberNo, ToyNo)
  - 问题：同一个会员可以多次借同一个玩具 → PK 重复 → 必须加入 DateBorrowed
- 修正后：PK = (MemberNo, ToyNo, **DateBorrowed**)
- 是否把 DateReturned 也加入 PK？**不应该**，因为 PK 不能包含 NULL 值，而未归还的借阅记录 DateReturned 为 NULL

### 3.6 从概念模型到逻辑模型

- **Entity → Relation（表）**
- **Attributes → Columns（列）**
- **Primary Key → 下划线标注**
- **Foreign Key → 斜体标注**

逻辑模型的标准写法：

```
RELATION_NAME (PK_Attribute, Attribute1, Attribute2, FK_Attribute)
  PK: PK_Attribute
  FK: FK_Attribute references OTHER_TABLE
```

---

## 4. 物理设计与 MySQL 数据类型 (Week 4-1)

### 4.1 从逻辑设计到物理设计

**Inputs（输入）**：规范化关系、属性定义、响应时间需求、数据安全需求、备份/恢复需求、完整性需求、DBMS 技术

**Decisions（决策）**：属性数据类型、物理记录描述、文件组织、索引和数据库架构、查询优化

**Physical Record（物理记录）**：一组存储在相邻内存位置中并作为一个单元检索的字段

### 4.2 Binary Relationships 的映射规则

#### One-to-Many (1:M)

- **规则**：将 "一" 方的主键作为外键放入 "多" 方
- 例如：Department(1) — Employee(M) → Employee 表中添加 DepartmentID 外键

#### Many-to-Many (M:M)

- **规则**：创建 Associative Entity（关联实体/桥接表），包含两个实体的主键作为组合主键
- 例如：Student — Course → 创建 Enrollment(StudentID, CourseID, Grade)

#### One-to-One (1:1)

- **规则**：将 mandatory 方的主键作为外键放入 optional 方
- 原则：**把 FK 放在 NULL 值最少的一方**
- 如果两边都是 optional 或都是 mandatory，可以任选一方

### 4.3 Strong and Weak Entity — Identifying Relationship（标识关系）

- Weak Entity 的外键成为其主键的**一部分** (PFK - Primary Foreign Key)
- 映射方式与普通 1:M 相同，只是外键同时是主键的组成部分
- 例如：Loan(LoanID) — Payment(PaymentNumber, *LoanID*) → LoanID 既是 FK 也是 PK 的一部分

### 4.4 Unary Relationships（一元关系/递归关系）

**定义**：关系的两个参与者是同一个实体

#### Unary 1:1
- 在关系中添加一个外键引用自身
- 例如：Person 表中 SpouseID 引用 Person(ID)

#### Unary 1:M
- 在关系中添加一个外键引用自身
- 例如：Employee 表中 ManagerID 引用 Employee(ID)

#### Unary M:M
- 创建 Associative Entity，两个 FK 都引用同一张表（需要不同的列名）
- 例如：Item 自身的 contains 关系 → Component(ID, ComponentID, Quantity)，两个 FK 都引用 Item(ID)

### 4.5 MySQL 数据类型

#### 字符/文本类型

| 数据类型 | 说明 | 范围 |
|---------|------|------|
| **CHAR(M)** | 固定长度字符串，右补空格 | M: 0–255 |
| **VARCHAR(M)** | 可变长度字符串，不补空格 | M: 0–65535 |
| **TEXT** | 长文本 | 最多 65535 字符 |
| **LONGTEXT** | 超长文本 | 最多约 4GB |
| **BLOB** | 二进制大对象 | 最多 65535 字节 |
| **LONGBLOB** | 超大二进制对象 | 最多约 4GB |
| **ENUM('v1','v2',...)** | 枚举类型，只能取预定义值之一 | 最多 65535 个值 |
| **SET('v1','v2',...)** | 集合类型，可取多个预定义值 | 最多 64 个值 |

**CHAR vs VARCHAR 的区别（常考）**：
- CHAR(10) 存储 "abc" → 占用 10 字节（右补 7 个空格）
- VARCHAR(10) 存储 "abc" → 占用 3+1 字节（只存实际字符 + 长度前缀）
- CHAR 适合长度固定的数据（如邮编、国家代码）
- VARCHAR 适合长度可变的数据（如姓名、地址）

#### 整数类型

| 数据类型 | Signed 范围 | Unsigned 范围 |
|---------|-----------|-------------|
| **TINYINT** | -128 to 127 | 0 to 255 |
| **SMALLINT** | -32,768 to 32,767 | 0 to 65,535 |
| **MEDIUMINT** | -8,388,608 to 8,388,607 | 0 to 16,777,215 |
| **INT / INTEGER** | -2,147,483,648 to 2,147,483,647 | 0 to 4,294,967,295 |
| **BIGINT** | ±9.2×10¹⁸ | 0 to 1.8×10¹⁹ |

**注意**：整数类型不要使用 "(M)" 语法指定显示宽度

#### 浮点/小数类型

| 数据类型 | 说明 |
|---------|------|
| **FLOAT** | 单精度浮点数，适合科学数据 |
| **DOUBLE / REAL** | 双精度浮点数，适合科学数据 |
| **DECIMAL(M,D)** | 定点数，M=总位数，D=小数位数。适合**金额**等精确数值 |

**Boolean 值**：MySQL 中没有原生 Boolean 类型，用 **TINYINT** 表示（1=true, 0=false）

#### 日期时间类型

| 数据类型 | 格式 | 范围 |
|---------|------|------|
| **DATE** | 'YYYY-MM-DD' | 1000-01-01 to 9999-12-31 |
| **TIME** | 'hh:mm:ss' | -838:59:59 to 838:59:59 |
| **DATETIME** | 'YYYY-MM-DD hh:mm:ss' | 1000-01-01 00:00:00 to 9999-12-31 23:59:59 |
| **TIMESTAMP** | 'YYYY-MM-DD hh:mm:ss' | 1970-01-01 00:00:00 to 2038-01-19 03:14:07 |
| **YEAR** | YYYY | 1901 to 2155 |

**DATETIME vs TIMESTAMP（常考）**：
- DATETIME 存储本地时间，不随时区变化
- TIMESTAMP 存储 UTC 时间，检索时转换为当前时区

### 4.6 SQL CREATE TABLE 语法

```sql
CREATE TABLE TableName (
    column1  datatype  [NOT NULL],
    column2  datatype  [NOT NULL],
    ...
    PRIMARY KEY (column1),
    FOREIGN KEY (column2) REFERENCES OtherTable(column)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);
```

**ON DELETE / ON UPDATE 选项**：

| 选项 | 行为 |
|------|------|
| **RESTRICT / NO ACTION** | 阻止删除/更新（默认行为） |
| **CASCADE** | 级联删除/更新，子表跟随变化 |
| **SET NULL** | 将外键值设为 NULL |
| **SET DEFAULT** | 将外键值设为默认值 |

---

## 5. 规范化 Normalisation (Week 4-2)

### 5.1 为什么需要规范化？

**Normalisation（规范化）**：一种用于消除数据库中不必要冗余的技术，将一张大表分解为多张较小的表。

**不规范化会导致的三种异常（Anomalies）**：

| 异常类型 | 定义 | 示例 |
|---------|------|------|
| **Insertion Anomaly（插入异常）** | 不能独立插入某些数据 | 新增一门课程时，必须有学生选课才能录入 |
| **Deletion Anomaly（删除异常）** | 删除数据时意外丢失其他信息 | 删除某学生的选课记录时，丢失了课程信息 |
| **Update Anomaly（更新异常）** | 更新数据时需要修改多处 | 修改课程名称需要更新所有选了该课的行 |

### 5.2 Functional Dependency（函数依赖）

**定义**：属性 Y 完全函数依赖于属性 X，当且仅当 X 的每个值唯一确定 Y 的一个值。

**记法**：X → Y（读作 "X determines Y" / "X 决定 Y"）

**关键术语**：
- **Determinant（决定因素）**：箭头左边的属性 (X,Y → Z 中的 X,Y)
- **Key Attribute（键属性）**：属于主键的属性
- **Non-Key Attribute（非键属性）**：不属于主键的属性
- **Partial Functional Dependency（部分函数依赖）**：非键属性依赖于复合主键的一部分
  - 例如：PK=(StudentID, SubjectCode)，StudentName 只依赖于 StudentID
- **Transitive Dependency（传递依赖）**：非键属性依赖于另一个非键属性
  - 例如：LecturerID → LecturerName（两者都是非键属性）

### 5.3 Armstrong's Axioms（阿姆斯特朗公理）

用于推导函数依赖的三条基本公理：

| 公理 | 表述 | 含义 |
|------|------|------|
| **Reflexivity（自反性）** | 若 B ⊆ A，则 A → B | 属性集决定其自身的子集 |
| **Augmentation（增广性）** | 若 A → B，则 AC → BC | 两边同时加入相同属性，依赖仍成立 |
| **Transitivity（传递性）** | 若 A → B 且 B → C，则 A → C | 函数依赖可传递 |

### 5.4 三大范式（The Three Normal Forms）

#### UNF → 1NF: First Normal Form（第一范式）

**条件**：表中每个单元格只有一个值（消除多值属性和重复组）

**步骤**：
1. 识别 repeating groups（重复组）
2. 将重复组移出，创建新关系
3. 用外键连接两个关系
4. 确定每个关系的主键

**示例**：
```
UNF: Invoice(InvoiceNo, Date, CustomerNo, CustomerName, CustomerAddress,
      ClerkNo, ClerkName, (ProductNo, ProductDesc, UnitPrice, Qty))

1NF:
Invoice(InvoiceNo, Date, CustomerNo, CustomerName, CustomerAddress,
        ClerkNo, ClerkName)
InvoiceLineItem(InvoiceNo, ProductNo, ProductDesc, UnitPrice, Qty)
  FK: InvoiceNo references Invoice
  PK: (InvoiceNo, ProductNo)
```

#### 1NF → 2NF: Second Normal Form（第二范式）

**条件**：在 1NF 的基础上，消除所有 partial functional dependencies（部分函数依赖）

**关键规则**：
- **只对具有 composite PK（复合主键）的关系才需要做 2NF**
- 如果关系只有 single-attribute PK（单属性主键），则自动满足 2NF

**步骤**：
1. 找出依赖于复合主键某一部分（而非全部）的非键属性
2. 将这些属性和它们依赖的那部分键移出，创建新关系
3. 原关系中保留外键

**示例**：
```
1NF: InvoiceLineItem(InvoiceNo, ProductNo, ProductDesc, UnitPrice, Qty)
  ProductDesc 和 UnitPrice 只依赖于 ProductNo（部分依赖）

2NF:
Product(ProductNo, ProductDesc, UnitPrice)
InvoiceLineItem(InvoiceNo, ProductNo, Qty)
  FK: ProductNo references Product
```

#### 2NF → 3NF: Third Normal Form（第三范式）

**条件**：在 2NF 的基础上，消除所有 transitive dependencies（传递依赖）——即非键属性不能依赖于另一个非键属性

**步骤**：
1. 找出非键属性之间的函数依赖
2. 将被依赖的非键属性及其依赖属性移出，创建新关系
3. 原关系中保留外键

**示例**：
```
2NF: Invoice(InvoiceNo, Date, CustomerNo, CustomerName, CustomerAddress,
            ClerkNo, ClerkName)
  CustomerNo → CustomerName, CustomerAddress（传递依赖）
  ClerkNo → ClerkName（传递依赖）

3NF:
Customer(CustomerNo, CustomerName, CustomerAddress)
Clerk(ClerkNo, ClerkName)
Invoice(InvoiceNo, CustomerNo, ClerkNo, Date)
  FK1: CustomerNo references Customer
  FK2: ClerkNo references Clerk
```

### 5.5 完整的 3NF 示例结果

```
Customer(CustomerNo, CustomerName, CustomerAddress)
Clerk(ClerkNo, ClerkName)
Product(ProductNo, ProductDesc, UnitPrice)
Invoice(InvoiceNo, CustomerNo, ClerkNo, Date)
InvoiceLineItem(InvoiceNo, ProductNo, Qty)
```

### 5.6 Beyond 3NF

- BCNF (Boyce-Codd Normal Form), 4NF, 5NF, 6NF 存在但**不在考试范围内**
- 一般来说，达到 3NF 就足够了

---

## 6. 常考题型与补充知识点

### 6.1 常见考试题型

#### 题型一：ER 图绘制与阅读
- 给定业务描述，画出 ER 图（使用 Crow's Foot notation）
- 正确标识实体、属性、关系、connectivity (1:1, 1:M, M:M)、participation (mandatory/optional)
- 识别 strong entity 和 weak entity

#### 题型二：从 ER 图到关系模式
- 将 ER 图转换为 relational schema（逻辑设计）
- 正确放置外键（1:M 的 FK 放在 Many 方；M:M 创建桥接表；1:1 的 FK 放在 optional 方）
- 标注 PK（下划线）和 FK（斜体）

#### 题型三：规范化
- 给定一个 UNF 的关系，逐步规范化到 1NF → 2NF → 3NF
- 识别 repeating groups、partial dependencies、transitive dependencies
- 写出每一步的关系模式

#### 题型四：SQL CREATE TABLE
- 将逻辑/物理设计转为 SQL DDL
- 正确使用 PRIMARY KEY, FOREIGN KEY, NOT NULL, 数据类型
- 理解 ON DELETE / ON UPDATE 的不同选项

#### 题型五：函数依赖分析
- 识别给定关系中的函数依赖
- 判断是部分依赖还是传递依赖
- 运用 Armstrong's Axioms 推导函数依赖

### 6.2 易混淆概念对比

#### CHAR vs VARCHAR
| 特征 | CHAR | VARCHAR |
|------|------|---------|
| 长度 | 固定 | 可变 |
| 存储 | 右补空格至指定长度 | 只存实际字符 |
| 性能 | 读取更快（固定偏移） | 更节省空间 |
| 适用 | 长度固定的数据 | 长度不定的数据 |

#### Primary Key vs Foreign Key
| 特征 | Primary Key | Foreign Key |
|------|-------------|-------------|
| 唯一性 | 必须唯一 | 可以重复 |
| NULL | 不允许 NULL | 允许 NULL（如果是 optional participation） |
| 数量 | 每个表只有一个 PK | 可以有多个 FK |
| 作用 | 唯一标识本表的行 | 引用另一张表的 PK |

#### Surrogate Key vs Natural Key
| 特征 | Surrogate Key | Natural Key |
|------|--------------|-------------|
| 业务含义 | 无 | 有 |
| 示例 | Auto-increment ID | Student Number, SSN |
| 稳定性 | 非常稳定 | 可能随业务变化 |
| 优点 | 简单、不变 | 直观、有意义 |
| 缺点 | 无意义、需额外存储 | 可能变更、可能较长 |

### 6.3 关系模式书写规范（考试格式）

```
RELATION_NAME (PK_Attribute, Attribute1, Attribute2, FK_Attribute)
```

- **PK（主键）**：使用下划线标注
- **FK（外键）**：使用斜体标注
- **PFK（既是 PK 又是 FK）**：同时下划线和斜体
- 在下方列出：
  - PK: 列出主键属性
  - FK: 列出外键及其引用的表

### 6.4 Integrity Constraints（完整性约束）汇总

| 约束类型 | 定义 |
|---------|------|
| **Entity Integrity（实体完整性）** | 主键不能为 NULL |
| **Referential Integrity（引用完整性）** | 外键值必须在被引用表中存在或为 NULL |
| **Domain Integrity（域完整性）** | 属性值必须在允许的范围/类型内 |
| **NOT NULL Constraint** | 列不接受空值 |
| **UNIQUE Constraint** | 列中的值必须唯一 |
| **CHECK Constraint** | 限制列的值范围 |
| **DEFAULT Constraint** | 为列设置默认值 |

### 6.5 选择数据类型的原则

选择数据类型时应考虑：
1. **Enforce data integrity（强制数据完整性）**：选择能验证数据质量的类型
2. **Represent all possible values（表示所有可能值）**：范围要够大
3. **Support required manipulations（支持所需操作）**：如日期比较、数值计算
4. **Minimise storage space（最小化存储空间）**：不浪费空间
5. **Maximise performance（最大化性能）**：固定长度类型通常更快

### 6.6 关系映射速查表

| 关系类型 | 映射方法 | FK 位置 |
|---------|---------|--------|
| 1:M (Binary) | PK of "1" side → FK in "M" side | Many 方 |
| M:M (Binary) | 创建 Associative Entity | 新表中（组合 PK） |
| 1:1 (Binary) | PK of mandatory side → FK in optional side | Optional 方 |
| 1:M (Unary) | 添加 FK 引用自身 | 自身表 |
| 1:1 (Unary) | 添加 FK 引用自身 | 自身表 |
| M:M (Unary) | 创建 Associative Entity，两个 FK 引用同一表 | 新表中（需不同列名） |
| Identifying (Weak Entity) | FK 放入 Weak Entity 并成为 PK 的一部分 | Weak Entity |

### 6.7 规范化流程速查

```
Step 0: 写出 UNF 关系（用括号标注 repeating groups）
         ↓
Step 1 → 1NF: 消除重复组 → 创建新表 → 用 FK 连接 → 确定 PK
         ↓
Step 2 → 2NF: 消除部分依赖（仅针对复合主键的表）→ 创建新表
         ↓
Step 3 → 3NF: 消除传递依赖 → 创建新表 → 保留 FK
```

---

## 考试重点提醒

根据课程 slides 中明确标注的可考内容：

1. **Normalisation Process (1NF → 2NF → 3NF)** — 必考
2. **Functional Dependencies** — 必考
3. **Armstrong's Axioms** — 必考
4. ER 建模与 Crow's Foot 表示法
5. 键的类型与识别
6. 关系映射规则（各种 connectivity 如何转为表结构）
7. MySQL 数据类型选择
8. SQL CREATE TABLE 语法
9. ON DELETE / ON UPDATE 行为
10. Integrity Constraints（完整性约束）

**Beyond 3NF (BCNF, 4NF, 5NF, 6NF) 不在考试范围内。**

---

*本指南基于 INFO90002 Week 1–4 所有 Lecture slides 整理，涵盖全部知识点并补充常考内容。祝复习顺利！*
