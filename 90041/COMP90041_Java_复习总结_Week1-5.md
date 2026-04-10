# COMP90041 Java 复习总结 Week 1–5
> 基于：Week 1–5 讲义 PDF × 双语笔记 MD
> 重点：**定义 + 常考点**（中英对照）

---

## 目录
1. [Java 基础工具链](#1-java-基础工具链)
2. [数据类型与变量](#2-数据类型与变量)
3. [运算符与表达式](#3-运算符与表达式)
4. [控制流](#4-控制流)
5. [类与对象（基础）](#5-类与对象基础)
6. [方法](#6-方法)
7. [封装与访问控制](#7-封装与访问控制)
8. [Static & final](#8-static--final)
9. [引用与内存](#9-引用与内存)
10. [枚举 Enum](#10-枚举-enum)
11. [包装类 Wrapper Class](#11-包装类-wrapper-class)
12. [数组 Array](#12-数组-array)
13. [⚠️ 高频易混对比（必考）](#13-️-高频易混对比必考)
14. [📋 考点速查清单](#14--考点速查清单)

---

## 1. Java 基础工具链

### ★ 定义速查

| 工具 | 英文定义 | 中文定义 |
|------|----------|----------|
| **javac** (Java Compiler) | Translates source code (`.java`) into bytecode (`.class`) | 将源代码编译为字节码 |
| **JVM** (Java Virtual Machine) | Executes bytecode; has interpreter + JIT compiler | 执行字节码；包含解释器和 JIT 编译器 |
| **JRE** (Java Runtime Environment) | Runtime environment + system libraries (e.g., `System.out`) | 运行环境 + 系统库 |
| **JDK** (Java Development Kit) | Full development kit: JRE + compiler + dev tools | 完整开发包：JRE + 编译器 + 开发工具 |

### ★ 常考点

- **JDK ⊃ JRE ⊃ JVM**：包含关系，JDK 最大
- JVM 执行字节码有两种方式：**解释执行（Interpreter）** 和 **JIT 即时编译（Just-In-Time Compiler）**
- Java 源码 → `javac` 编译 → 字节码 `.class` → JVM 执行
- 口诀：**JDK 开发，JRE 运行，JVM 执行**

### 程序基本结构

```java
class ClassName {
    public static void main(String[] args) {
        // 代码从这里开始执行
    }
}
```

> ★ `main` 方法是 Java 程序的**唯一入口点**，JVM 启动时必须找到它。

---

## 2. 数据类型与变量

### ★ 定义速查

**Data Type / 数据类型**：描述变量或数据能保存什么类型的值。Java 数据类型分为三组：

| 分组 | 例子 | 特点 |
|------|------|------|
| **Primitive（基本类型）** | `int`, `double`, `char`, `boolean`, `float`, `long`, `byte`, `short` | 直接存储值，固定大小，按值传递 |
| **Class Type（类类型）** | `String`, `Scanner`, `Car` | 引用类型，变量保存内存地址 |
| **Array Type（数组类型）** | `int[]`, `double[]` | 引用类型，连续存储同类型元素 |

### ★ 常用基本类型

| 类型 | 大小 | 范围/说明 |
|------|------|-----------|
| `int` | 32-bit | 整数，最常用 |
| `double` | 64-bit | 浮点数，最常用 |
| `char` | 16-bit | 单个字符（Unicode） |
| `boolean` | — | 只有 `true` / `false` |
| `float` | 32-bit | 浮点数（精度低于 double） |
| `long` | 64-bit | 大整数 |

### ★ 变量 (Variable)

**定义**：变量是一个有名字的数据存储位置。
**定义（EN）**：A variable is a named location that stores data.

```java
int number;       // 声明（Declaration）
number = 3;       // 赋值（Assignment）
int count = 5;    // 声明 + 赋值合并
```

### ★ 常考点

- 变量**使用前必须声明**，声明时需指定类型
- 变量名以**字母开头**，可包含字母、数字、下划线
- **整数除法**：`5/2 = 2`（截断）；**浮点除法**：`5.0/2 = 2.5`
- **类型转换（Type Conversion）**：
  - **Widening（宽化）**：自动发生，小类型 → 大类型 `byte → short → int → long → float → double`
  - **Narrowing（窄化）**：需要强制转换 `(int) 5.9` → `5`
- `char` 类型可以转换为 `int`（对应 ASCII/Unicode 编码）：`(int)'A'` → `65`

---

## 3. 运算符与表达式

### ★ 定义速查

**Expression / 表达式**：由变量、运算符、方法调用等组成，**求值后得到一个值**的结构。

**Operator / 运算符**：执行某种操作的符号（如 `+`, `-`, `*`, `/`, `%`）

**Operand / 操作数**：运算符作用的值

### ★ 运算符分类

| 类别 | 运算符 | 说明 |
|------|--------|------|
| 算术 | `+  -  *  /  %` | `%` 是取余（模运算） |
| 比较 | `<  <=  >  >=  ==  !=` | 结果是 `boolean` |
| 逻辑 | `&&  \|\|  !` | AND、OR、NOT |
| 自增/自减 | `++  --` | 前缀/后缀 |
| 赋值 | `=  +=  -=  *=  /=` | 复合赋值 |
| 三元 | `condition ? a : b` | 条件运算符 |

### ★ 常考点

- `&&` 和 `||` 是**短路运算符（Short-circuit）**：
  - `false && anything` → 不再计算右边
  - `true || anything` → 不再计算右边
- **前置 vs 后置自增**：
  - `++x`：先加再返回（返回 6，若 x=5）
  - `x++`：先返回再加（返回 5，若 x=5）
- **运算符优先级**：括号 > 单目（`!`, `++`） > 算术（`*`, `/`） > 加减 > 比较 > 逻辑（`&&` > `||`） > 赋值
- **String 拼接**：`+` 若有一个 String，则另一个操作数自动转为 String：`"x = " + 1` → `"x = 1"`

---

## 4. 控制流

### ★ 定义速查

**Flow Control / 控制流**：决定语句执行顺序的分支和循环机制。

**Statement / 语句**：驱动程序执行某个具体动作的完整指令（不同于"产生值"的表达式）。

**Block / 代码块**：用花括号 `{}` 括起来的零条或多条语句，可创建局部变量作用域。

### ★ 分支结构

#### `if` / `if-else`

```java
if (condition) {
    // 条件为真时执行
} else if (condition2) {
    // 多路分支
} else {
    // 其他情况
}
```

**定义**：`if-else` 在两个可选分支之间做选择，条件为 `boolean` 表达式。

#### 条件运算符（Ternary Operator）

```java
int max = (a > b) ? a : b;  // 相当于 if-else，但是一个表达式（有返回值）
```

#### `switch` 语句

```java
switch (controlExpression) {
    case VALUE1:
        // 代码
        break;       // ⚠️ 必须有 break，否则会继续执行下一个 case（fall-through）
    case VALUE2:
        break;
    default:
        // 没有匹配时执行
}
```

**定义**：根据控制表达式的值进行多路分支。

### ★ 循环结构

#### `while` 语句

```java
int count = 10;
while (count >= 0) {   // 先判断条件
    System.out.println(count);
    count--;
}
```

**定义**：先检查条件，条件为真才执行循环体。**可能执行 0 次**。

#### `do-while` 语句

```java
int count = 0;
do {
    count++;         // 先执行循环体
} while (count < 1); // 再检查条件
```

**定义**：先执行循环体，再检查条件。**至少执行 1 次**。

#### `for` 语句

```java
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}
```

**定义**：把初始化、条件检查、更新三部分放在同一个循环头中。

#### `for-each` 语句（增强 for 循环）

```java
int[] values = {1, 2, 3};
for (int value : values) {   // 读作：对 values 中的每个 value
    System.out.println(value);
}
```

**定义**：遍历数组或集合的每个元素，语法简洁，但**无法使用索引**。

### ★ `break` / `continue`

| 关键字 | 作用 |
|--------|------|
| `break` | **退出**最近的循环或 switch |
| `continue` | **跳过**当前迭代的剩余代码，进入下一轮 |
| `break label` | 退出指定层的循环（嵌套循环专用） |

### ★ 调试 (Debugging)

- **Bug**：导致程序错误的缺陷
- **Debugging**：找到并修复 bug 的过程
- 常用方法：输出语句追踪变量 `System.out.println("count = " + count);`
- **Assertion（断言）**：`assert Boolean_Expression;`
  - 若表达式为 `false`，程序报错终止
  - 默认**关闭**；用 `java -enableassertions ClassName` 启用

### ★ 常考点

- `switch` 的 `case` 标签类型必须与控制表达式匹配
- **Nested loop（嵌套循环）**：内层循环对外层每一次迭代都完整执行一遍
- `if (x < 2);` 中的分号导致 `if` 语句体为空，后面的语句**无条件执行**（陷阱！）
- `System.exit(0)` 正常退出；`System.exit(非0)` 表示出错退出

---

## 5. 类与对象（基础）

### ★ 定义速查

**Class / 类**：用来创建对象的**蓝图或模板**，定义了对象拥有的数据（字段）和行为（方法）。
**EN**: A class is a blueprint or template for creating objects and defining their data and actions.

**Object / 对象**：类的一个**实例（Instance）**，包含实际数据并可执行动作。
**EN**: An object is an instance of a class; it contains actual data and performs actions.

**Instantiation / 实例化**：使用 `new` 创建对象的过程。

```java
Car myCar = new Car();   // 声明 + 实例化
```

**Field / Instance Variable / 实例变量**：在类内部声明、为每个对象保存数据的变量。
每个对象有**自己独立的**字段值，但所有对象共享同样的字段定义。

### ★ 类的典型结构

```java
public class Car {
    // 实例变量（Fields）
    private String manufacturer;
    private int yearBuilt;

    // 构造器（Constructor）
    public Car(String manufacturer, int yearBuilt) {
        this.manufacturer = manufacturer;
        this.yearBuilt = yearBuilt;
    }

    // 方法（Methods）
    public String getManufacturer() { return manufacturer; }
    public void setYearBuilt(int year) { this.yearBuilt = year; }

    // toString
    public String toString() {
        return yearBuilt + " " + manufacturer;
    }

    // equals
    public boolean equals(Car other) {
        return this.yearBuilt == other.yearBuilt &&
               this.manufacturer.equals(other.manufacturer);
    }
}
```

### ★ 构造器 (Constructor)

**定义**：用于初始化新创建对象的特殊成员。
**EN**: A constructor is a special member used to initialize a newly created object.

| 特征 | 说明 |
|------|------|
| 名称 | 必须与类名**完全相同** |
| 返回类型 | **无**（不写 void） |
| 调用方式 | `new` 创建对象时自动调用 |
| 不同于方法 | 不能用点运算符调用，不能被继承 |

#### 三种构造器类型

```java
public Car() { }                                 // 1. 默认构造器（无参）
public Car(String manufacturer, int year) { ... } // 2. 带参构造器
public Car(Car other) { ... }                     // 3. 拷贝构造器（复制另一个对象）
```

> ★ **默认构造器**：只有在类中**完全没有**定义任何构造器时，Java 才自动生成。一旦你写了带参构造器，Java 就**不再**自动生成无参构造器！

#### 默认初始化值（无显式初始化时）

| 类型 | 默认值 |
|------|--------|
| `boolean` | `false` |
| 数值类型 (`int`, `double`…) | `0` |
| 类类型 / 数组类型 | `null` |

### ★ `toString()` 方法

**定义**：提供对象的文本表示形式。**打印对象或字符串拼接时自动调用**。

```java
System.out.println(myCar);         // 自动调用 myCar.toString()
String s = "Car: " + myCar;       // 也自动调用 toString()
```

### ★ `equals()` 方法

**定义**：判断两个对象在内容或逻辑上是否相等。

```java
myCar.equals(yourCar);  // 比较内容
myCar == yourCar;       // 比较引用（是否同一对象）
```

### ★ `this` 关键字

**定义**：在实例方法或构造器中，`this` 指向**当前对象**。
主要用途：区分同名的实例变量和形参。

```java
public void setYearBuilt(int yearBuilt) {
    this.yearBuilt = yearBuilt;  // this.yearBuilt = 实例变量；yearBuilt = 形参
}
```

---

## 6. 方法

### ★ 定义速查

**Method / 方法**：一段有名字的代码块，定义对象或类可以执行的行为。方法分为**方法头**和**方法体**。

**Void Method / 无返回值方法**：执行动作但不返回值，用 `void` 声明。
**Returning Method / 有返回值方法**：返回某种指定类型的值，必须有 `return` 语句。

```java
public void printName() { ... }           // void 方法
public int square(int x) { return x*x; } // 有返回值方法
```

### ★ 参数（Parameter）vs 实参（Argument）

**Parameter（形参）**：写在方法定义里的变量。
**Argument（实参）**：调用方法时传入的实际值。

```java
public double myMethod(int param1, double param2) { ... }  // param1, param2 是形参
double result = myMethod(1, 3.0);                          // 1, 3.0 是实参
```

### ★ Java 参数传递机制：Pass-by-Value（值传递）

> ★★★ **Java 只有值传递（Pass-by-Value）！**

| 参数类型 | 传递的内容 | 方法内修改是否影响原变量 |
|----------|-----------|--------------------------|
| 基本类型 | 值的**副本** | ❌ 不影响 |
| 引用类型（对象） | **引用的副本**（地址） | ✅ 可以通过引用修改对象内容 |
| `String` | 引用的副本，但 String 不可变 | ❌ 不影响（创建新 String） |

### ★ 局部变量（Local Variable）与作用域（Scope）

**定义**：在方法或代码块内部声明的变量；作用域是它可以被使用的范围。
代码块结束时，局部变量消失；`for` 循环初始化部分的变量作用域也只在该循环内。

### ★ 方法重载（Overloading）

**定义**：在同一个类中定义两个或多个**同名但签名不同**的方法。
**Signature（签名）** = 方法名 + 参数列表（类型 + 数量 + 顺序）。

```java
void setCar(Car other) { }                    // 签名：setCar(Car)
void setCar(String name, int year) { }        // 签名：setCar(String, int)
// ❌ 仅返回类型不同，不构成合法重载！
```

---

## 7. 封装与访问控制

### ★ 定义速查

**Encapsulation / 封装**：把数据和方法打包在一起，同时限制外部直接访问内部细节的做法。
**EN**: Bundling data and methods together while controlling direct access to internal details.

**Access Modifier / 访问修饰符**：控制类成员可见性的关键字。

| 修饰符 | 可访问范围 | 常用场景 |
|--------|----------|----------|
| `public` | 任何地方 | 方法、构造器 |
| `private` | 仅本类内部 | **实例变量（推荐）** |
| `protected` | 本类 + 子类（继承相关，后续课程） | — |
| 默认（无修饰符） | 同包内 | — |

> ★ **实例变量默认应设为 `private`**，通过 getter/setter 访问。

**Accessor / Getter / 访问器**：返回私有字段值，不允许外部直接修改。
**Mutator / Setter / 修改器**：以受控方式修改私有字段值，可在修改前进行验证。

```java
public int getYearBuilt() { return yearBuilt; }             // Getter

public void setYearBuilt(int yearBuilt) {
    if (yearBuilt >= 0) {                                    // 验证输入
        this.yearBuilt = yearBuilt;
    }
}                                                            // Setter
```

---

## 8. Static & final

### ★ `static` 静态成员

**Static Variable / 静态变量**：属于类本身，**所有对象共享同一份**。
**Static Method / 静态方法**：属于类，不需要创建对象即可调用，通过类名调用。

```java
class Test {
    static int count = 0;             // 静态变量：每个类只有一份
    int id;                           // 实例变量：每个对象一份

    public static void hello() { }    // 静态方法
    Test() { count++; }               // 构造器中可访问静态变量
}

Test.hello();    // 通过类名调用静态方法
```

### ★ 常考点

- 静态方法**不能直接访问**实例变量和实例方法（因为没有 `this`）
- 静态变量可以被静态方法访问
- `main` 方法是静态的，所以在 `main` 中不能直接调用非静态方法，需先创建对象
- 多个类可以各有自己的 `main`，`java ClassName` 指定从哪个 `main` 开始

### ★ `final` 常量

**Constant / 常量**：赋值后值不能改变的变量，用 `final` 声明。

```java
public static final double PI = 3.14159;  // 类级别常量，命名全大写
```

> ★ 常量命名惯例：**全大写 + 下划线分隔**，如 `MAX_SIZE`、`PI`。

---

## 9. 引用与内存

### ★ 定义速查

**Primitive Variable / 基本类型变量**：**直接存储值**本身，大小固定。
**Reference Variable / 引用类型变量**：存储**指向对象的引用（内存地址）**，对象本身存储在内存其他地方。

```java
int a = 42;          // a 直接存 42
Car myCar = new Car(); // myCar 存的是 Car 对象的内存地址
```

### ★ 赋值 vs 别名共享（Aliasing）

```java
// 基本类型赋值 — 复制值
int a = 42;
int b = a;  // b 有自己独立的 42，互不影响

// 引用类型赋值 — 复制引用（共享同一对象！）
Car carA = new Car();
Car carB = carA;    // carB 和 carA 指向同一个对象！
carB.yearBuilt = 2025;  // 会改变 carA.yearBuilt！
```

### ★ `null`

**定义**：表示引用变量不指向任何对象的特殊常量。
- 测试 `null` 用 `==` 或 `!=`，不用 `equals()`
- 访问 `null` 引用的方法或字段会抛出 **`NullPointerException`**

```java
Car myCar = null;
if (myCar == null) { ... }   // 正确做法
```

### ★ 匿名对象（Anonymous Object）

```java
if (car1.equals(new Car("Toyota", 2023))) { ... }  // new Car(...) 是匿名对象，用后即废
```

---

## 10. 枚举 Enum

### ★ 定义速查

**Enum / 枚举**：定义一组**固定的、带名字的常量值**的特殊类。
**EN**: An enum is a special kind of class that defines a fixed set of named constant values.

```java
enum WorkDay { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY }

WorkDay today = WorkDay.MONDAY;
```

### ★ 内建方法

| 方法 | 说明 |
|------|------|
| `name()` | 返回常量名（字符串，不可覆盖） |
| `toString()` | 返回字符串表示（可覆盖） |
| `ordinal()` | 返回常量位置（从 0 开始） |
| `values()` | 返回所有枚举常量的数组 |
| `valueOf(String)` | 字符串转对应枚举常量 |
| `compareTo(other)` | 比较枚举位置 |
| `equals(other)` | 判断两个枚举值是否相同 |

### ★ 常考点

- 枚举常量命名**全大写**，如 `MONDAY`
- 枚举可以用 `==` 或 `equals()` 比较
- 枚举在 `switch` 中使用时，`case` 后面**只写常量名**，不写类型名：

```java
switch (today) {
    case MONDAY:           // ✅ 正确
    // case WorkDay.MONDAY:  ❌ 错误！switch 里不加类型前缀
        break;
}
```

- 枚举 vs 常量（`final`）：枚举类型安全、支持方法、自带迭代

---

## 11. 包装类 Wrapper Class

### ★ 定义速查

**Wrapper Class / 包装类**：把基本类型值封装成对象的类，使基本类型能像对象一样使用。

| 基本类型 | 包装类 |
|----------|--------|
| `int` | `Integer` |
| `double` | `Double` |
| `char` | `Character`（注意大写C，不是Char） |
| `boolean` | `Boolean` |
| `byte` / `short` / `long` / `float` | `Byte` / `Short` / `Long` / `Float` |

### ★ 装箱与拆箱

**Boxing / 装箱**：基本类型 → 包装类对象
**Unboxing / 拆箱**：包装类对象 → 基本类型
Java 5.0+ 支持**自动装箱/拆箱（Autoboxing）**：

```java
Integer obj = 42;    // 自动装箱（Auto-boxing）
int x = obj;         // 自动拆箱（Auto-unboxing）
```

### ★ 常用静态方法

```java
int n = Integer.parseInt("100");         // String → int
String s = Double.toString(123.99);      // double → String

// Character 类方法
Character.toUpperCase('a');     // → 'A'
Character.isDigit('5');         // → true
Character.isLetter('A');        // → true
Character.isWhitespace(' ');    // → true
```

---

## 12. 数组 Array

### ★ 定义速查

**Array / 数组**：用来存储和处理**同类型数据集合**的数据结构。
**EN**: A data structure used to store and process a collection of same-type data.

- **Element（元素）**：数组中的单个项目
- **Index（下标）**：元素的位置编号，**从 0 开始**
- **Size / Length（大小/长度）**：数组能容纳的元素总数

### ★ 声明与创建

```java
// 一步法
int[] scores = new int[5];             // 创建 5 个 int，默认为 0
double[] data = {1.3, 5.2, 5.3};      // 直接初始化，不需要 new

// 两步法
double[] score;
score = new double[5];
```

### ★ 访问与遍历

```java
score[0] = 10.5;                        // 赋值（下标 0 ~ length-1）
System.out.println(score[3]);           // 读取

// 普通 for 循环（需要索引时使用）
for (int i = 0; i < score.length; i++) {
    System.out.println(score[i]);
}

// for-each 循环（只需要值时使用）
for (double s : score) {
    System.out.println(s);
}
```

### ★ 数组是对象

- 数组变量存储的是**引用**（内存地址），不是数据本身
- `array.length` 是**实例变量**（不是方法，不加括号！）
- 打印数组名输出的是地址（如 `[D@659e0bfd`），不是内容

### ★ 数组赋值与拷贝

```java
double[] A = {1.0, 2.0, 3.0};
double[] B = A;          // ⚠️ 浅拷贝！B 和 A 指向同一个数组

// 深拷贝（内容复制）
for (int i = 0; i < A.length && i < B.length; i++) {
    B[i] = A[i];
}

// 或使用 clone()
B = A.clone();
```

### ★ 隐私泄漏（Privacy Leak）与深拷贝

**隐私泄漏**：getter 直接返回私有数组的引用，外部代码可以修改私有数据。

```java
// ❌ 不安全（隐私泄漏）
public double[] getArray() { return anArray; }

// ✅ 安全（返回深拷贝）
public double[] getArray() { return anArray.clone(); }
```

### ★ 方法接收/返回数组

```java
// 方法接收数组参数
public static void printArray(int[] arr) { ... }

// 方法返回数组
public static int[] doubleValues(int[] input) {
    int[] result = new int[input.length];
    for (int i = 0; i < input.length; i++) {
        result[i] = input[i] * 2;
    }
    return result;
}
```

### ★ `char[]` 与 `String` 的区别

```java
char[] a = {'A', 'B', 'C'};
// String s = a;        // ❌ 非法！

String s = new String(a);       // ✅ 转换为 "ABC"
System.out.println(a);          // ✅ char[] 可直接 println，输出 ABC
```

### ★ 常考点

- 下标越界 → 运行时抛出 **`ArrayIndexOutOfBoundsException`**
- `array.length` vs `string.length()` vs `list.size()` — 注意括号的有无
- `B = A` 不创建新数组，只是让 B 指向同一个数组
- `B == A` 为 `true` 当且仅当 B 和 A 指向**同一个数组对象**

---

## 13. ⚠️ 高频易混对比（必考）

| 对比点 | 一个 | 另一个 | 关键区别 |
|--------|------|--------|----------|
| **表达式 vs 语句** | Expression（有值） | Statement（执行动作） | `x + 1` 是表达式；`x = 1;` 是语句 |
| **类 vs 对象 vs 实例** | Class = 蓝图 | Object/Instance = 运行中的具体实体 | `Car` 是类；`new Car()` 是对象 |
| **形参 vs 实参** | Parameter（方法定义中的变量） | Argument（调用时传入的值） | 定义 vs 使用 |
| **实例变量 vs 静态变量** | 每个对象一份 | 整个类共享一份 | `static` 关键字区分 |
| **基本类型 vs 引用类型** | 直接存值 | 存引用（地址） | `int` vs `Car` |
| **`while` vs `do-while`** | 可能 0 次执行 | 至少 1 次执行 | 条件判断在前 vs 在后 |
| **`==` vs `equals()`** | 比较引用（地址） | 比较内容/逻辑相等 | 对象用 `equals()`，基本类型用 `==` |
| **`array.length` vs `str.length()`** | 数组：属性（无括号） | 字符串：方法（有括号） | 一个是变量，一个是方法 |
| **浅拷贝 vs 深拷贝** | `B = A`（共享同一数组） | 逐元素复制 / `.clone()` | 改 A 会影响 B（浅）vs 不影响（深） |
| **`break` vs `continue`** | 退出整个循环 | 跳过本次迭代，继续下一轮 | 完全终止 vs 跳过一轮 |
| **static 方法 vs 实例方法** | 不能访问实例变量，用类名调用 | 可以访问实例变量，用对象调用 | 有无 `static` 关键字 |
| **String 的 `==` vs `equals()`** | `==` 比较引用 | `equals()` 比较字符序列 | 字符串比较必须用 `equals()` |

---

## 14. 📋 考点速查清单

### Week 1 — Java 基础

- [ ] `javac` / `JVM` / `JRE` / `JDK` 各自的职责和层级关系
- [ ] `main` 方法签名：`public static void main(String[] args)`
- [ ] 基本类型分类及默认值
- [ ] 整数除法 vs 浮点除法：`5/2=2` vs `5.0/2=2.5`
- [ ] 类型宽化（自动）vs 窄化（强制转换）
- [ ] 前置/后置自增：`++x` vs `x++` 的区别
- [ ] 短路运算符 `&&` 和 `||`
- [ ] `char` 与 `int` 的转换（ASCII）
- [ ] `System.out.print` vs `println` vs `printf` 的区别
- [ ] `Scanner` 读取输入的基本用法

### Week 2 — 控制流

- [ ] `if` / `if-else` / 多路 `if-else` 的结构
- [ ] 条件运算符 `? :` 的用法（它是表达式，有值）
- [ ] `switch` 语句：`break` 的重要性；`case` 标签类型限制；`default`
- [ ] `while` 可能 0 次；`do-while` 至少 1 次
- [ ] `for` 循环三部分：初始化；条件；更新
- [ ] `break` 退出循环；`continue` 跳过本轮
- [ ] 无限循环的成因
- [ ] `assert` 断言的默认关闭状态

### Week 3 — 类与方法 I

- [ ] 类 = 蓝图；对象 = 实例；实例化 = `new`
- [ ] 方法头 + 方法体；`void` vs 有返回值方法
- [ ] `return` 在有返回值方法中是必须的；在 void 中用于提前退出
- [ ] 形参 vs 实参（Parameter vs Argument）
- [ ] `this` 的作用：区分实例变量和形参
- [ ] 三种构造器：默认 / 带参 / 拷贝
- [ ] 没有构造器时 Java 自动生成默认构造器；有了带参构造器就不再自动生成
- [ ] `toString()` 打印时自动调用
- [ ] `==` 比较引用；`equals()` 比较内容

### Week 4 — 类与方法 II

- [ ] `public` vs `private`：实例变量应 `private`
- [ ] `getter` 方法以 `get` 开头；`setter` 方法以 `set` 开头
- [ ] 重载（Overloading）：同名方法，签名（参数）不同；仅返回类型不同不构成重载
- [ ] `static` 方法：不能访问实例变量；通过类名调用
- [ ] `static` 变量：类共享一份
- [ ] `final`：值不可变，命名全大写
- [ ] 基本类型变量存值；引用类型变量存地址
- [ ] `B = A`（引用类型）：共享同一对象，改 A 影响 B
- [ ] Java 是**值传递**：传基本类型复制值；传对象复制引用
- [ ] `String` 不可变：方法内修改不影响原 String
- [ ] `null`：测试用 `==`；访问 null 引用 → `NullPointerException`

### Week 5 — 枚举、包装类、数组

- [ ] `enum`：固定常量集合，类型安全；常量名全大写
- [ ] `enum` 在 `switch` 中：`case` 后只写常量名，不写类型前缀
- [ ] `enum` 内置方法：`values()`, `ordinal()`, `name()`, `valueOf()`
- [ ] 包装类：`int→Integer`, `char→Character`, `double→Double` 等
- [ ] 自动装箱/拆箱（Java 5.0+）
- [ ] `Integer.parseInt()` / `Double.toString()` 等静态方法
- [ ] `Character` 方法：`isDigit`, `isLetter`, `toUpperCase`, `isWhitespace`
- [ ] 数组：下标从 0 开始；`length` 是属性（无括号）
- [ ] `ArrayIndexOutOfBoundsException` 触发条件
- [ ] `B = A` 是浅拷贝；用循环或 `.clone()` 做深拷贝
- [ ] `==` 对数组比较引用；内容比较需逐元素
- [ ] 数组 getter 直接返回引用 → 隐私泄漏；用 `.clone()` 安全返回
- [ ] `for-each` 语法：`for (Type var : array)`；无法使用索引
- [ ] `char[]` 不是 `String`；转换用 `new String(charArray)`

---

*整理自 COMP90041 Week 1–5 讲义 + 双语笔记 | 2026 Semester 1*
