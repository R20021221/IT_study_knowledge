# INFO90002 Database Systems & Information Modelling — Mid-Term 复习指南

> University of Melbourne | 基于 Week 1–4 全部 Lecture 内容整理，附补充常考知识点

---

## 完整知识点目录

### Week 1 — 数据库基础概念
- 1.1 Data vs Information（数据 vs 信息）
- 1.2 数据的三种分类（Structured / Semi-Structured / Unstructured）
- 1.3 DBMS 与 Database 的区别、CRUD 操作、DBMS 核心优势（含 Logical/Physical Data Independence）
- 1.4 关系数据模型（Ted Codd 1970、二维表属性、术语对照表）
- 1.5 Schema vs Instance、Schema on Write
- 1.6 Cardinality vs Degree (Arity)
- 1.7 Client-Server Architecture
- 1.8 SQL 语言分类（DDL / DML / DCL）
- 1.9 Constraints（约束）
- 1.10 数据库开发生命周期概述（各阶段含义）

### Week 2 — 数据建模与 ER 模型
- 2.1 数据库开发生命周期（完整版 11 步）
- 2.2 ER 模型核心概念：Entity（Strong/Weak）、Attributes（5 种类型 + Domain + Required/Optional + 命名规范 + MySQL Workbench 符号）、Relationships（Relationship vs Relationship Set、双向性）、FK 放在 MANY 方
- 2.3 Connectivity vs Cardinality（(min, max) 表示法）
- 2.4 Participation（Mandatory / Optional）
- 2.5 Crow's Foot Notation（四种符号 + 实线/虚线的区别）
- 2.6 M:M 关系的处理（拆解为两个 1:M + 弱实体）
- 2.7 开发 ER 图的 8 步流程
- 2.8 Entity vs Attribute 的设计选择
- 2.9 Noun-Verb 分析法
- 2.10 Business Rules（业务规则）

### Week 3 — 键 (Keys) 与逻辑/物理设计
- 3.1 Good Entity Selection（好的实体 vs 不应是实体的概念）
- 3.2 键的层次结构（Superkey → Candidate Key → Primary Key）
- 3.3 各类键的定义（Superkey / Candidate Key / PK / FK / Surrogate Key / Natural Key）
- 3.4 Natural Key vs Surrogate Key（建模阶段 vs 实施阶段）
- 3.5 主键的四个性质（Uniqueness / NOT NULL / Immutability / Minimality）
- 3.6 Composite Key（复合键）
- 3.7 Foreign Key 与 Referential Integrity（ON DELETE 选项、Weak Entity 用 CASCADE）
- 3.8 Mandatory Participation = NOT NULL
- 3.9 Toy Library Case Study（测试验证键的选择、DateReturned 不能做 PK）
- 3.10 Relationship Degree（Unary / Binary / Ternary / N-ary）
- 3.11 Ternary Relationship 转逻辑设计
- 3.12 从概念模型到逻辑模型（标注规范 PK/FK/PFK、完整转换示例）
- 3.13 Multi-valued Attributes 的处理（展开 / 新建关联表）
- 3.14 从逻辑模型到物理模型（加入数据类型）
- 3.15 Schema on Write（IC 在写入时检查）
- 3.16 Integrity Constraints（IC）

### Week 4-1 — 物理设计与 MySQL 数据类型
（待复习）

### Week 4-2 — 规范化 Normalisation
（待复习）

### 常考题型与补充知识点
（待复习）

---

## 1. 数据库基础概念 (Week 1)

### 1.1 Data vs Information

- **Data（数据）**：原始的、未经处理的事实和数字，本身没有意义。例如：`42`, `Melbourne`, `2024-03-01`
- **Information（信息）**：经过处理、组织后具有意义和上下文的数据。例如："Melbourne 在 2024-03-01 的温度为 42°C"
- 核心关系：**Data + Context + Processing = Information**

### 1.2 数据的分类

| 类型 | 定义 | 示例 |
|------|------|------|
| **Structured（结构化数据）** | 有预定义的重复格式，存储在表格/行列中 | 关系数据库中的表、大学的学生数据 |
| **Semi-Structured（半结构化数据）** | 不完全符合表格格式，但有标签或标记，使其更容易分析 | XML, JSON, NoSQL |
| **Unstructured（非结构化数据）** | 没有预定义格式，组织不知道数据的格式和内容 | 图片、视频、社交媒体内容、电子邮件 |

### 1.3 DBMS（数据库管理系统）

**DBMS 与 Database 不是同一个概念！**
- **Database（数据库）** = 存储数据的地方
- **DBMS（数据库管理系统）** = 管理数据库的一组程序，允许开发者/用户存储和检索关系数据库中的数据

**RDBMS（关系数据库管理系统）** 允许用户执行 **CRUD** 操作：
- **C**reate（创建）
- **R**ead（读取）
- **U**pdate（更新）
- **D**elete（删除）

**DBMS 的核心优势**：

- **Data Independence（数据独立性）**：应用程序不需要知道数据的物理存储方式
  - **Logical Data Independence（逻辑数据独立性）**：修改逻辑模式不需要重写应用程序（如增删改属性/实体/关系）
  - **Physical Data Independence（物理数据独立性）**：更换存储方式（如从本地服务器迁移到云端）不影响应用程序
- **Efficient Data Access（高效数据访问）**：比平面文件（flat files）更高效地存储和检索数据
- **Data Integrity and Security（数据完整性与安全性）**：强制实施完整性约束，控制访问权限，不仅依赖操作系统
- **Uniform Data Administration（统一数据管理）**：由专业人员管理数据，降低数据风险
- **Concurrent Access and Crash Recovery（并发访问与崩溃恢复）**：调度并发访问，保护数据免受系统故障影响
- **Reduced Application Development Time（减少开发时间）**：提供高层接口，简化编程

### 1.4 关系数据模型

- 由 **Ted Codd** 于 **1970年** 提出
- **关系数据库（Relational Database）** = 一组相关表的集合
- 数据以 **二维表 (Table/Relation)** 的形式组织

**二维表的属性**：
- 列名必须唯一
- 行没有编号，行的顺序无关紧要
- 每个单元格只允许一个值
- 所有行必须是不同的（no duplicates）

**关系数据库术语对照表**：

| 正式术语 | MS Access 术语 | MySQL/Oracle 术语 |
|---------|---------------|------------------|
| Relation | Table | Table |
| Attribute | Field | Column |
| Tuple | Record | Row |

### 1.5 Schema vs Instance

- **Schema（模式）**：数据库的结构定义（关系名称、每列的名称和类型），相对稳定不变
  - 例如：`Pizza(pID: string, pizzaName: string, price: decimal)`
- **Instance（实例）**：数据库在某一时刻的实际数据内容（即表中的行），经常变化
- **Schema on Write**：表结构在数据写入前就已定义，数据写入时必须符合 schema 规定

### 1.6 Cardinality vs Degree (Arity)

- **Cardinality（基数）**：表中行（记录）的数量（不含表头）
- **Degree / Arity（度）**：表中列（属性）的数量

### 1.7 Client-Server Architecture（客户端-服务器架构）

- **Server（服务器端）**：MySQL Server — 存储和管理数据，数据只有一份（不含备份）
- **Client（客户端）**：MySQL Workbench — 用户操作界面，发送 SQL 查询
- 多个客户端可以同时访问同一个服务器

### 1.8 SQL 语言分类

| 类别 | 全称 | 操作 | 用途 |
|------|------|------|------|
| **DDL** | Data Definition Language | CREATE, DROP, ALTER, RENAME | 定义/修改表结构 |
| **DML** | Data Manipulation Language | SELECT, INSERT, UPDATE, DELETE | 操作表中的数据 |
| **DCL** | Data Control Language | GRANT, REVOKE | 控制用户权限 |

### 1.9 Constraints（约束）

基于业务规则，可以为表添加约束来验证数据：
- 例如：Student ID 必须是正确长度
- 例如：Student type 只能是 PG 或 UG
- 例如：学生只能选当前学期开设的课程

### 1.10 数据库开发生命周期概述（详见 Week 2）

完整的生命周期包括：Database Planning → Systems Definition → Requirements Analysis → Conceptual Design → Logical Design → Physical Design → Application Design → Implementation → Data Conversion and Loading → Testing → Operational Maintenance

各阶段的具体含义：
- **Database Planning**：规划项目如何执行
- **Systems Definition**：定义系统范围、边界、用户和应用领域
- **Requirements Analysis**：收集和分析新系统的需求
- **Conceptual Design**：高层次的实体与关系模型（first-pass），通常省略属性，可以包含 M:M、重复组、复合属性
- **Logical Design**：基于关系数据库设计，包含列和键，独立于特定 DBMS 厂商
- **Physical Design**：为特定 DBMS 实现逻辑设计，包括数据类型、索引、完整性约束、文件组织、安全措施
- **Application Design**：设计使用数据库的界面和应用程序
- **Implementation**：将设计实现为可运行的数据库
- **Data Conversion and Loading**：将现有数据迁移到新数据库
- **Testing**：测试数据库设计中的错误，检查性能、健壮性、可恢复性、安全性
- **Operational Maintenance**：上线后的监控和维护

---

## 2. 数据建模与 ER 模型 (Week 2)

### 2.1 数据库开发生命周期（完整版）

```
Database Planning（数据库规划）
    ↓
Systems Definition（系统定义）
    ↓
Requirements Definition and Analysis（需求定义与分析）
    ↓
Conceptual Design（概念设计）← 画 ER 图
    ↓
Logical Design（逻辑设计）← 转为关系模式
    ↓
Physical Design（物理设计）← 决定数据类型、索引等
    ↓
Application Design（应用设计）← 设计应用程序界面与逻辑
    ↓
Implementation（实施）← 写 SQL 建表
    ↓
Data Conversion and Loading（数据迁移与导入）
    ↓
Testing（测试）
    ↓
Operational Maintenance（运维与维护）
```

- 数据建模是一个**迭代渐进的过程（iterative, progressive process）**
- 数据模型是一种**沟通工具（communication tool）**，帮助不同角色（开发者、管理者、用户）理解数据结构

### 2.2 ER 模型核心概念

#### Entity（实体）

- 代表现实世界中可区分的对象或概念，在 ER 图中用矩形表示
- 指的是 **entity set（实体集合）**，而不是单个实体实例
- 对应关系数据库中的**表（table）**，而不是某一行

**Strong Entity（强实体）**
- 有自己完整的主键，可以独立存在
- 与其他实体通过 **non-identifying（非标识性）关系** 连接，在 Crow's Foot 中用**虚线**表示

**Weak Entity（弱实体）**
- 满足以下两个条件：
  1. 没有父实体就无法存在
  2. 主键部分或全部来自父实体
- 与父实体通过 **identifying（标识性）关系** 连接，在 Crow's Foot 中用**实线**表示
- Weak entity 必须 **mandatory participation** 于该关系
- 弱实体借用父实体的 PK 作为自己 PK 的一部分，该属性称为 **PFK（Primary Foreign Key）**

**判断方法**：问"如果删掉父实体，这个实体还有意义吗？"如果没有意义 → Weak Entity

**Partial Identifier（部分标识符）**：弱实体自身的属性，需结合 PFK 才能唯一标识一个实例

#### Attributes（属性）

**属性命名规范**：不能有空格，使用字母数字加下划线或 CamelCase，例如 `firstName`、`year_of_birth`

**属性的 Domain（域）**：每个属性有一个允许值的集合，由业务规则定义
- 例如：Grade 在 UniMelb 只能是 H1, H2A, H2B, H3, P, N, NH

**Required vs Optional 属性**：
- **Required（必填属性）**：必须有值，对应 NOT NULL
- **Optional（可选属性）**：可以为空

| 属性类型 | 定义 | 示例 | MySQL Workbench 符号 |
|---------|------|------|---------------------|
| **Simple（简单属性）** | 不可再分的原子值（不能拆解） | FirstName, gender | 普通属性 |
| **Composite（复合属性）** | 可以分解为更小的子部分 | Address → Street, City, State | 属性名后加 () |
| **Derived（派生属性）** | 可从其他属性计算得出，不直接存储 | Age（从 DOB 和 CURDATE() 计算）| 属性名加 [] |
| **Multivalued（多值属性）** | 一个实体可以有多个值 | CarColour（车可有多种颜色）| 属性名加 {} |
| **Single-valued（单值属性）** | 每个实体只有一个值 | StudentID | 普通属性 |

**注意**：`date_of_birth` 是 Composite 属性（可拆成 Day/Month/Year），而 `age` 是 Derived 属性（从 DOB 计算得出）

#### Relationships（关系）

- **Relationship（关系）**：两个实体实例之间的单次关联，例如 "John places a Pizza order"
- **Relationship Set（关系集合）**：同类型关系的集合，例如 "Customers place orders"
- 关系两端的实体称为 **participants（参与者）**
- 关系是双向的：Customer places Order ↔ Order belongs to Customer
- **Binary Relationship（二元关系）**：两个实体之间的关系（最常见）
- **Unary Relationship（一元关系/递归关系）**：同一个实体内部的关系（如 Employee manages Employee）

#### Foreign Key（外键）在 ER 模型中的引入

- FK **始终放在 MANY 方**（Many side）
- 例如：Customer(1) — Order(M)，则 customerID 成为 Order 的外键

### 2.3 Connectivity（连接性）vs Cardinality（基数）

**Connectivity** 描述关系的类型（1:1、1:M、M:M）：

| 类型 | 含义 | 示例 |
|------|------|------|
| **1:1 (One-to-One)** | A 的一个实例最多对应 B 的一个实例 | Person — Passport |
| **1:M (One-to-Many)** | A 的一个实例可以对应 B 的多个实例 | Department — Employee |
| **M:M (Many-to-Many)** | A 的多个实例可以对应 B 的多个实例 | Student — Course |

**Cardinality** 描述具体的最小和最大参与数量，用 **(min, max)** 表示：
- 例如：一个顾客可以下 0 到多个订单 → (0, N)；一个订单只能属于 1 个顾客 → (1, 1)
- 注意：本课程解题中通常不要求写 (min, max)，用 Crow's Foot 符号表示即可

### 2.4 Participation（参与度）

- **Mandatory（强制参与/Total Participation）**：实体的每个实例都必须参与关系
- **Optional（可选参与/Partial Participation）**：实体的实例可以不参与关系

### 2.5 Crow's Foot Notation（鸦爪表示法）

这是本课程主要使用的 ER 图表示法。关键符号：

- **||** （两条竖线）= 一 (one)，且必须参与 (mandatory)
- **O|** （圆圈 + 竖线）= 一 (one)，可选参与 (optional)
- **>|** 或 **⋈|** （鸦爪 + 竖线）= 多 (many)，且必须参与 (mandatory)
- **>O** 或 **⋈O** （鸦爪 + 圆圈）= 多 (many)，可选参与 (optional)

**关系线类型**：
- **虚线**：Non-identifying relationship（强实体之间）
- **实线**：Identifying / Strong relationship（弱实体与父实体之间）

### 2.6 M:M 关系的处理

**重要**：M:M 关系在逻辑设计和物理设计中**无法直接实现**，必须拆解！

处理方法：
1. 插入一个 **Weak Entity（弱实体）** 来解析 M:M
2. 将 M:M 拆成两个 1:M 关系
3. 弱实体的 PK 由两边强实体的 PK 组合而成（PFK）
4. 弱实体可以有自己的附加属性（如 quantity、date 等）

```
Order  ||————>O  Pizza        (M:M，不能实现)

解析后：
Order  ||————>|  OrderLine  |<————||  Pizza
              (弱实体，PK = OrderNo + PizzaNo)
```

### 2.7 开发 ER 图的 8 步流程

1. 列出系统中的主要实体
2. 用矩形在图中表示实体
3. 找出实体之间的关系并用符号连接
4. 为每个实体添加属性，确定主键
5. 建模每对实体之间的 connectivity（1:1, 1:M, M:M）
6. 建模每对实体之间的 cardinality（mandatory/optional）
7. 判断是否存在弱实体，处理 M:M 关系
8. 验证 ERD 是否符合业务规则

### 2.8 Entity vs Attribute 的设计选择

同一个概念，何时建模为实体，何时建模为属性？取决于业务需求：

- 如果一个顾客有多个地址（送货地址、账单地址），**address 应该是实体**
- 如果地址结构（city, street 等）很重要，**address 可以是实体**
- 如果只允许一个地址且结构不重要，**address 作为属性集合即可**

**Noun-Verb 分析法**：从业务描述中识别实体和关系的方法：
- **名词（Noun）** → 通常是实体或属性
- **动词（Verb）** → 通常是关系

### 2.9 Business Rules（业务规则）

- 业务规则定义了数据的约束条件和组织运作规范
- ER 模型通过 connectivity 和 cardinality 来体现业务规则
- 业务规则因组织不同而不同，数据库设计必须满足特定组织的业务规则
- 例如："每个部门必须有一个经理" → Department 对 Manager 是 mandatory participation

---

## 3. 键 (Keys) 与逻辑设计 (Week 3)

### 3.1 什么是好的实体（Good Entity Selection）

一个好的实体应当满足：
- 在数据库中会有**很多实例**
- 由**很多属性**组成
- 是系统需要建模的对象

**实体不是**：
- 系统的用户（user of the system）
- 系统的输出（如报表）
- 系统本身

### 3.2 键的层次结构

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

**关于 Natural Key vs Surrogate Key**：
- 建模阶段（与客户沟通时）通常使用 **Natural Key**，避免引入客户不熟悉的概念
- 数据库实施阶段，开发者可以选择加入 Surrogate Key
- 加入 Surrogate Key 后，Natural Key 的数据**不会被删除**，只是不再作为主键

### 3.3 主键的性质

- **唯一性 (Uniqueness)**：每个值必须在表中唯一
- **非空性 (NOT NULL)**：主键值不能为空
- **不可变性 (Immutability)**：一旦赋值，理想情况下不应更改
- **最小性 (Minimality)**：使用尽可能少的属性

### 3.4 Composite Key（复合键）

- 由两个或多个属性组合构成的主键
- 常见于 M:M 关系的关联表（Associative Entity）中
- 例如：LOAN 表的 PK 为 (MemberNo, ToyNo, DateBorrowed)

### 3.5 Foreign Key（外键）与 Referential Integrity（引用完整性）

- 外键的值必须在被引用表的主键中存在，或者为 NULL
- **Referential Integrity（引用完整性）**：保证外键引用的一致性，如果所有 FK 约束都被强制执行，则达到引用完整性
- 违反引用完整性：外键值不在被引用表的主键列中（插入时应拒绝）

**违反时的处理方式（ON DELETE）**：

| 选项 | 行为 |
|------|------|
| **NO ACTION / RESTRICT** | 阻止删除（默认）|
| **CASCADE** | 级联删除子表相关记录 |
| **SET NULL** | 将外键值设为 NULL |
| **SET DEFAULT** | 将外键值设为默认值 |

**弱实体（Weak Entity）被删除时**应使用 `ON DELETE CASCADE`，因为弱实体不能在没有父实体的情况下存在。

### 3.6 Mandatory Participation 与 NOT NULL

在 SQL 中，Mandatory Participation（强制参与）用 `NOT NULL` 来实现：
```sql
CREATE TABLE Order (
  orderID INTEGER,
  CustID CHAR(11) NOT NULL,   -- 强制参与，每个订单必须有顾客
  ...
  FOREIGN KEY (CustID) REFERENCES Customer(CustomerID)
  ON DELETE NO ACTION
)
```

### 3.7 Toy Library Case Study 核心要点

这是课程中的一个重要案例，展示了如何测试和验证键的选择：

- Member-Toy 是 **M:M** 关系，**optional participation on both sides**
- 初始设计 LOAN(MemberNo, ToyNo, DateBorrowed, DateReturned)，PK = (MemberNo, ToyNo)
  - 问题：同一个会员可以多次借同一个玩具 → PK 重复 → 必须加入 DateBorrowed
- 修正后：PK = (MemberNo, ToyNo, **DateBorrowed**)
- 是否把 DateReturned 也加入 PK？**不应该**，因为 PK 不能包含 NULL 值，而未归还的借阅记录 DateReturned 为 NULL

### 3.8 Relationship Degree（关系的度）

| 度 | 名称 | 定义 | 示例 |
|----|------|------|------|
| 1 | **Unary（一元/递归）** | 同一实体内部的关系 | Employee manages Employee |
| 2 | **Binary（二元）** | 两个实体之间（最常见） | Customer places Order |
| 3 | **Ternary（三元）** | 三个实体之间 | Supplier-Part-Department |
| N | **N-ary** | N 个实体之间 | — |

**Ternary Relationship 转逻辑设计**：关联表的属性包含所有参与实体的 PK（作为 FK）加上关系本身的描述属性：

```
CONTRACT (SupplierID, PartID, DepCode, Quantity, Date)
  PK: (SupplierID, PartID, DepCode)
  FK1: SupplierID references Supplier
  FK2: PartID references Part
  FK3: DepCode references Department
```

### 3.9 从概念模型到逻辑模型

#### 标注规范

- **PK（主键）**：下划线标注
- **FK（外键）**：斜体标注
- **PFK（既是 PK 又是 FK）**：下划线 + 斜体

**注意**：FK 名称不必与父实体的 PK 名称相同。

#### 逻辑模型的标准写法

```
RELATION_NAME (PK_Attribute, Attribute1, FK_Attribute)
  PK: PK_Attribute
  FK: FK_Attribute references OTHER_TABLE
```

#### 完整转换示例

```
Pizza (Code, PizzaName, Price)
  PK: Code

Order (OrderID, CustomerID, DateTime, TotalDue)
  PK: OrderID
  FK: CustomerID references Customer

OrderItem (OrderID, PizzaCode, Quantity)
  PK: (OrderID, PizzaCode)       ← 复合 PFK
  FK1: OrderID references Order
  FK2: PizzaCode references Pizza(Code)
```

#### Multi-valued Attributes 的处理

多值属性在转换为逻辑设计时需要**展开（flatten）**：
- 如果值的数量已知（如 home/work/mobile），展开为多个独立属性
- 如果值的数量不确定（如资质、技能），应创建新的关联表

```
-- 展开为独立属性（数量已知时）
Customer (CustID, Surname, FirstName, home_num, work_num, mobile)
```

### 3.10 从逻辑模型到物理模型

物理设计在逻辑设计基础上加入**数据类型**：

```
-- 逻辑设计
Building (bCode, bName)

-- 物理设计
Building (
  bCode CHAR(2),
  bName VARCHAR(25)
)
```

### 3.11 Schema on Write

- 在数据插入之前，表结构（schema）已经预先定义好
- DBMS 在数据写入时检查 IC（Integrity Constraints）
- 非法数据（违反约束的数据）不被允许插入

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
| **BIGINT** | -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 | 0 to 18,446,744,073,709,551,615 |
| **BIT(M)** | 存储 M 位的位值（bit values），M 范围 1–64 | — |

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

**写 UNF 的注意事项**：
- **不要包含 Derived 属性**（如 Age、TotalDue 可以计算得出，不应列入）
- 用括号 `()` 标注 repeating groups
- 下划线标注主键

**步骤**：
1. 识别 repeating groups（重复组）
2. 将重复组移出，**给新关系起一个有意义的名字**（命名错误会影响后续 2NF/3NF 的分析）
3. 用外键连接两个关系（思考哪一方加 FK）
4. 为两个关系确定主键（确认后再想一想——这真的是唯一标识符吗？）

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
