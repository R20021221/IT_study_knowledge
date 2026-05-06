# COMP90038 算法与复杂度：Divide-and-Conquer & Heap 知识点梳理

> Week 6 Lecture 11 & 12 + Week 7 Lecture 13 | Junhao Gan | Semester 1, 2026

---

## 一、Divide-and-Conquer 概述

**分治策略（Divide-and-Conquer）** 是递归思想的延伸，把递归这种问题求解技术发挥到极致。它的核心三步骤为：

1. **Divide（分）**：将原问题实例划分为若干个规模更小的子实例。
2. **Solve（治）**：递归地求解每个子实例。
3. **Combine（合）**：将子实例的解合并起来，得到原问题的解。

**最佳工作条件**：当子实例可以划分为**等大或近似等大**的部分时，分治效果最好。

### Split-Solve-and-Join 流程图

```
            problem of size n
               /          \
   sub-problem 1          sub-problem 2
   (size n/2)             (size n/2)
        |                      |
   solution to            solution to
   sub-problem 1          sub-problem 2
               \          /
              combine
                  |
        solution to original problem
```

本部分讨论的算法包括：**Mergesort、Quicksort、Master Theorem、Tree Traversal、Closest Pair (revisited)**，以及 **Heap 与 Heapsort**。

---

## 二、Lecture 11：基于分治的排序

### 2.1 排序问题（The Sorting Problem）

**问题定义**：给定包含 $n$ 个整数的数组 $A$，目标是将它们以**非递减顺序**排序后存回 $A$。

之前介绍的 Selection Sort 最坏情况时间复杂度为 $O(n^2)$。本节介绍两种高效的分治排序算法：**Merge Sort** 与 **Quick Sort**。

---

### 2.2 Merge Sort（归并排序）

#### 算法步骤

给定 $n$ 个整数的数组 $A$：

- **基础情况**：若 $n = 0$ 或 $n = 1$，已排好序，直接返回。
- **递归情况**：
  1. 将 $A$ 划分为两个（近似）等大的两半 $A_{\text{left}}$ 和 $A_{\text{right}}$，前半含 $A$ 的前 $n/2$ 个元素，后半含其余元素。
  2. 递归排序 $A_{\text{left}}$。
  3. 递归排序 $A_{\text{right}}$。
  4. **合并** 已排好序的 $A_{\text{left}}$ 和 $A_{\text{right}}$，结果存回 $A$。

#### 合并两个已排好序数组（Merge）

给定两个已排好序的数组 $A_1$ 与 $A_2$（各含 $n/2$ 个元素），合并步骤如下：

1. 创建新数组 $A'$，长度为 $|A_1| + |A_2| = n$。
2. 维护三个指针：$p_1$ 指向 $A_1$，$p_2$ 指向 $A_2$，$i$ 指向 $A'$，初值均为 0。
3. 当 $p_1 < |A_1|$ 且 $p_2 < |A_2|$ 时，重复：
   - 若 $A_1[p_1] \le A_2[p_2]$：$A'[i] \leftarrow A_1[p_1]$，$p_1 \leftarrow p_1 + 1$。
   - 否则：$A'[i] \leftarrow A_2[p_2]$，$p_2 \leftarrow p_2 + 1$。
   - $i \leftarrow i + 1$。
4. 若 $p_1 = |A_1|$，把 $A_2$ 中剩余元素全部追加到 $A'$；否则把 $A_1$ 中剩余元素追加到 $A'$。

合并的时间复杂度为 $O(n)$，因为每次比较都把一个元素放入 $A'$。

#### 代价分析

设 $C(n)$ 为 Merge Sort 在最坏情况下排序 $n$ 个整数的代价：

$$
\begin{cases}
C(n) = O(1), & n = 0 \text{ 或 } n = 1 \\
C(n) = 2 \cdot C(n/2) + O(n), & n > 1
\end{cases}
$$

通过展开法解此递推得到 $C(n) \in O(n \log n)$。

> **结论**：Merge Sort 的最坏情况时间复杂度为 $O(n \log n)$。

#### 比较模型下的下界

**比较计算模型（Comparison Computation Model）**：算法只允许通过比较两个元素来判断它们的大小关系（大于、小于、等于）。

**关键定理**：在比较模型下，任何**正确**的比较型排序算法都至少需要 $\Omega(n \log n)$ 次比较来排序 $n$ 个元素。

> **重要结论**：因此，Merge Sort 在比较模型下是**最优的**比较型排序算法。

---

### 2.3 Quick Sort（快速排序）

#### 算法步骤

给定 $n$ 个整数的数组 $A$：

- **基础情况**：若 $n = 0$ 或 $n = 1$，直接返回。
- **递归情况**：
  1. 从 $A$ 中选取一个 **pivot**（枢轴）$p$。
  2. 将 $A$ 中元素分区（partition）：
     - 所有 $\le p$ 的元素放在 $p$ 之前；
     - 所有 $> p$ 的元素放在 $p$ 之后。
  3. 递归排序 $A_1$（$p$ 之前部分）。
  4. 递归排序 $A_2$（$p$ 之后部分）。

#### 最坏情况分析

与 $k$-Selection 问题类似，最坏情况下 pivot 选得**非常糟糕**——使 $A_1$ 或 $A_2$ 为空。
此时需要 $n$ 次迭代，每次代价 $O(n)$。

> Quick Sort 的最坏情况时间复杂度为 $O(n^2)$。

#### 改进：随机化 Quick Sort（Randomized Quick Sort）

借鉴 Randomized Quick-Selection 的思想：

1. 在当前数组 $A$ 中**均匀随机**选取一个元素 $p$ 作为 pivot。
2. 用 $p$ 对 $A$ 分区。
3. 若 $|A_1| \le \frac{2}{3} |A|$ **且** $|A_2| \le \frac{2}{3} |A|$ 同时成立：
   - 接受 $p$，继续递归 Quick Sort。
4. 否则，拒绝 $p$，从第 1 步重试。

#### 期望代价分析

- 选到"好" pivot（即满足 2/3 条件）的概率至少为 $1/3$。
- 期望最多 3 次尝试就能选到好 pivot。
- 在递归树的每一层，所有递归的总分区代价期望为 $O(n)$。
- 递归树最多 $O(\log n)$ 层。

> Randomized Quick Sort 的**期望时间复杂度**为 $O(n \log n)$。

#### 代价分析的另一种表述（更精细）

若用如下递推：

$$C(n) \le 2 \cdot C\left(\tfrac{2}{3} n\right) + O(n)$$

由于两个子问题总规模 $\frac{2}{3} n + \frac{2}{3} n = \frac{4}{3} n > n$，**高估**了实际代价。
解此递推得到 $O\left(n^{\log_{3/2} 2}\right)$，比 $O(n \log n)$ 还差。

实际上两子问题的真实总规模为 $n$，应为 $\alpha n$ 与 $(1 - \alpha) n$，其中 $\alpha \in [\tfrac{1}{3}, \tfrac{2}{3}]$：

$$
C(n) \le \max_{\alpha \in [1/3, 2/3]} \{ C(\alpha n) + C((1 - \alpha) n) \} + O(n)
$$

---

### 2.4 Master Theorem（主定理）

#### 通用分治递推

许多分治问题的递推形式为：

$$T(n) = a \cdot T(n/b) + f(n)$$

其中：
- $b$：将问题分成 $b$ 个子实例（每个规模 $n/b$）。
- $a$：需要求解的子实例个数（$1 \le a$，并允许 $a > b$）。
- $f(n)$：分割与合并的总代价。

常见情形：$T(n) = 2 T(n/2) + n$。

#### 主定理（Master Theorem）

对于整数常数 $a \ge 1$、$b > 1$，函数 $f(n) \in \Theta(n^d)$（$d \ge 0$），递推

$$T(n) = a T(n/b) + f(n)$$

（且 $T(1) = c$）的解为：

$$
T(n) \in
\begin{cases}
\Theta(n^d), & a < b^d \\
\Theta(n^d \log n), & a = b^d \\
\Theta\left(n^{\log_b a}\right), & a > b^d
\end{cases}
$$

> **主定理对 big-$O$ 与 big-$\Omega$ 同样成立。**

#### 例题

| 递推 | $a$ | $b$ | $d$ | 比较 | 结果 |
|------|-----|-----|-----|------|------|
| $T(n) = 2T(n/2) + n$ | 2 | 2 | 1 | $a = b^d$ | $\Theta(n \log n)$ |
| $T(n) = 4T(n/4) + n$ | 4 | 4 | 1 | $a = b^d$ | $\Theta(n \log n)$ |
| $T(n) = T(n/2) + n$ | 1 | 2 | 1 | $a < b^d$ | $\Theta(n)$ |
| $T(n) = 2T(n/2) + n^2$ | 2 | 2 | 2 | $a < b^d$ | $\Theta(n^2)$ |

直观上，$a < b^d$ 时合并主导（顶层代价最大），$a > b^d$ 时叶子主导，$a = b^d$ 时各层代价均匀。

---

## 三、Lecture 12：More Divide-and-Conquer

### 3.1 二叉树（Binary Trees）

**定义**：根树 $T$ 是**二叉树**，当且仅当每个节点至多有 2 个子节点。

**高度（Height）**：单节点树的高度为 0。空树（null）高度约定为 -1。

#### 二叉树的两类特殊形态

| 类型 | 定义 |
|------|------|
| **Full Binary Tree（满二叉树）** | 每个节点要么有 0 个子节点，要么有 2 个（非空）子节点 |
| **Complete Binary Tree（完全二叉树）** | 除最后一层外，每层都被填满；最后一层从左到右填充 |

#### 计算高度（递归）

```
function Height(T)
    if T = null then
        return -1
    if T 由单节点组成 then
        return 0
    else
        return max(Height(T.left), Height(T.right)) + 1
```

---

### 3.2 二叉树遍历（Binary Tree Traversal）

非空二叉树 $T$ 包含：根 $T.\text{root}$、左子树 $T.\text{left}$、右子树 $T.\text{right}$。

通用递归方式：访问根、递归遍历左子树、递归遍历右子树。**根的访问时机不同** 形成不同遍历方式。

#### 三种深度遍历

| 遍历 | 访问顺序 | 访问示例（见下示例树） |
|------|---------|----------------------|
| **Preorder（前序）** | Root → Left → Right | 17, 33, 19, 16, 38, 31, 48, 11, 14 |
| **Inorder（中序）** | Left → Root → Right | 19, 33, 38, 16, 31, 17, 11, 48, 14 |
| **Postorder（后序）** | Left → Right → Root | 19, 38, 31, 16, 33, 11, 14, 48, 17 |

示例树：
```
            17
           /  \
         33    48
        /  \   / \
       19  16 11 14
           / \
          38  31
```

**伪代码（以 Preorder 为例）**：

```
procedure PreorderTraverse(T)
    if T ≠ null then
        visit T.root
        PreorderTraverse(T.left)
        PreorderTraverse(T.right)
```

#### 遍历与 DFS 的关系

- **Preorder**：节点被**压入栈**的顺序（即 DFS 的发现/访问顺序）。
- **Postorder**：节点被**弹出栈**的顺序（即 DFS 的回溯顺序）。

#### Level-Order（层序遍历）

逐层访问节点，从根开始；示例树访问顺序：17, 33, 48, 19, 16, 11, 14, 38, 31。

> **实现方式**：BFS（广度优先搜索），借助队列。

---

### 3.3 2D Closest Pair Problem（非考点，了解）

> 本节为 Lec 12 标记的**非考核内容**，但理解其分治思想对掌握 Master Theorem 的应用有帮助。

#### 问题定义

两点 $p_1 = (a_1, \ldots, a_d)$、$p_2 = (b_1, \ldots, b_d)$ 的**欧氏距离**：

$$\text{dist}(p_1, p_2) = \sqrt{\sum_{i=1}^{d} (a_i - b_i)^2}$$

**2D Closest Pair Problem**：给定二维空间中 $n$ 个点的集合 $S$，找到一对点 $(p_1^*, p_2^*)$，使其距离不大于 $S$ 中任何其他点对的距离。

Lec 5 中给出的暴力算法时间复杂度为 $O(n^2)$。本节用分治法做到 $O(n \log n)$。

#### 高层思想

1. 用 $x$ 坐标的中位数 $\tau$ 把 $S$ 分成两个等大的部分 $S_L$ 与 $S_R$（$x \le \tau$ 入 $S_L$）。
2. 递归求 $S_L$ 中最近对距离 $d_L$。
3. 递归求 $S_R$ 中最近对距离 $d_R$。
4. 令 $d_{\min} = \min(d_L, d_R)$，记录对应点对 $(p_1^*, p_2^*)$。
5. 检查所有跨越 $S_L$ 和 $S_R$、可能 $\text{dist} \le d_{\min}$ 的候选点对，必要时更新 $d_{\min}$。
6. 返回 $d_{\min}$ 与 $(p_1^*, p_2^*)$。

#### 正确性

最近对必属于以下三种情况之一：
1. 两点都来自 $S_L$；
2. 两点都来自 $S_R$；
3. 一点来自 $S_L$，一点来自 $S_R$。

#### 代价分析（关键 Claim）

设 $L_x(S)$ 为按 $x$ 坐标升序排序的列表，$L_y(S)$ 为按 $y$ 坐标升序排序的列表。

**Claim 1**：给定 $L_x(S)$ 与 $L_y(S)$，可在 $O(n)$ 时间内将问题分成 $S_L$、$S_R$ 两个子问题，并准备好对应的排序列表。

**Claim 2**：给定 $d_{\min}$，跨越 $S_L$ 和 $S_R$ 的候选点对至多 $8n$ 对——
- 候选点必落在 $x \in [\tau - d_{\min}, \tau + d_{\min}]$ 的"竖条"中；
- 对竖条中任一点 $p_1 \in S_L$，与之配对的 $p_2 \in S_R$ 必须满足 $|p_1.y - p_2.y| \le d_{\min}$；
- 这些 $p_2$ 之间相互距离 $\ge d_{\min}$，因此每个小格至多含一点；故每个 $p_1$ 至多有 8 个候选 $p_2$。

**Claim 3**：给定 $L_y(S)$ 与 $d_{\min}$，所有候选跨界点对可在 $O(n)$ 时间内全部检查完毕，即 $g(n) \in O(n)$。

#### 递推与解

$$
\begin{cases}
C(n) = O(1), & n \le 2 \\
C(n) = 2 \cdot C(n/2) + f(n) + g(n), & n > 2
\end{cases}
$$

由 Claim 1、3 知 $f(n), g(n) \in O(n)$。由 Master Theorem，$C(n) \in O(n \log n)$。

排序 $L_x(S)$ 与 $L_y(S)$ 用 Merge Sort，代价 $O(n \log n)$，不影响整体复杂度。

> **结论**：2D Closest Pair 的总最坏代价为 $O(n \log n)$。

---

## 四、Lecture 13：Priority Queues, Heaps, Heapsort

### 4.1 Priority Queue（优先队列）

**Priority Queue** 是一种**抽象数据类型（ADT）**。每个元素都关联一个**优先级（priority）**，支持以下操作：

| 操作 | 含义 |
|------|------|
| `getTop()` | 返回优先级**最大**的元素 |
| `pop()` | 返回并**移除** 优先级最大的元素 |
| `push(x)` | 插入新元素 $x$ 及其优先级 |
| `isEmpty()` | 判空 |

可选扩展操作：
- `update(x, p)`：将元素 $x$ 的优先级改为 $p$。
- `construct()`：从一个元素列表构建优先队列。
- `join(Q1, Q2)`：合并两个优先队列。

#### Stack 与 Queue 是 Priority Queue 的特例

若以"被压入时的时间戳"作为优先级：
- **Stack**：时间戳**越大**（越晚入栈）优先级越大 → LIFO。
- **Queue**：时间戳**越小**（越早入队）优先级越大 → FIFO。

#### 不同实现的复杂度对比

设优先级即键值 key，元素总数为 $n$：

| 实现 | `push(x)` | `pop()` |
|------|-----------|---------|
| 无序数组 | $O(1)$ | $O(n)$ |
| 有序链表 | $O(n)$（用 BST 可降为 $O(\log n)$）| $O(1)$ |
| **Binary Heap（二叉堆）** | $O(\log n)$ | $O(\log n)$ |

---

### 4.2 Maximum Binary Heap（大根二叉堆）

#### 定义

一个键值多重集合上的**大根（二叉）堆（Max-Heap）** 是满足以下条件的根二叉树：

1. 每个键值至少出现在树中一个节点；
2. **Heap Condition（堆性质）**：对树中任意节点 $v$，其键值**不小于**其每个子节点的键值。

> 由定义，**任意子树的根** 都拥有该子树中的**最大**键值。

#### 完全二叉树式的数组实现

利用 **Complete Binary Tree** 的结构特性，可将节点按**层序**存放在数组 $H$ 中：

- 索引从 1 开始（$H[0]$ 留空）。
- 对索引 $i$ 的节点：左孩子在 $2i$，右孩子在 $2i + 1$（如存在）。
- 父节点在 $\lfloor i/2 \rfloor$。

**简化的堆性质**：对所有 $i \in \{2, \ldots, n\}$，

$$H[i] \le H[\lfloor i/2 \rfloor]$$

**堆的高度**：$O(\log n)$。

---

### 4.3 堆性质维护：Bubble-Up 与 Bubble-Down

这两个操作分别将 $H[u]$ **上浮**（朝根方向）或**下沉**（朝叶方向）到合适位置以恢复堆性质。

#### Bubble-Up（上浮）

```
bubble-up(u):
(a) i ← u
(b) 若 i = 1，停止
(c) 若 H[i] ≤ H[⌊i/2⌋]，停止
(d) 否则交换 H[i] 与 H[⌊i/2⌋]，i ← ⌊i/2⌋
(e) 回到 (b)
```

最坏代价：$O(\log n)$。

#### Bubble-Down（下沉）

```
bubble-down(u):
(a) i ← u, j ← null
(b) 若 i = n，停止
(c) 若 H[i] 有左孩子（索引 ℓ = 2i），j ← ℓ
(d) 若 H[i] 有右孩子（索引 r = 2i+1）且 H[r] > H[ℓ]，j ← r
(e) 若 j = null 或 H[i] ≥ H[j]，停止
(f) 否则交换 H[i] 与 H[j]；i ← j, j ← null；回到 (b)
```

最坏代价：$O(\log n)$。

> **关键点**：bubble-down 时要选择**较大** 的孩子来交换，否则会破坏另一边的堆性质。

---

### 4.4 优先队列的核心操作实现

#### `push(x)`

1. 把 $x$ 放到 $H[n+1]$；$u \leftarrow n+1$；$n \leftarrow n + 1$。
2. 调用 `bubble-up(u)`。

#### `pop()`

1. 若 $n = 1$，直接移除 $H[1]$ 并停止。
2. 否则交换 $H[1]$ 与最后一个元素 $H[n]$。
3. 移除 $H[n]$，$n \leftarrow n - 1$。
4. $u \leftarrow 1$，调用 `bubble-down(u)`。

#### `update-key(u, p)`

1. 若 $p = H[u]$，停止。
2. 若 $p < H[u]$，置 $H[u] \leftarrow p$，调用 `bubble-down(u)`。
3. 若 $p > H[u]$，置 $H[u] \leftarrow p$，调用 `bubble-up(u)`。

> 上述三个操作的最坏时间复杂度都是 $O(\log n)$。

---

### 4.5 构建大根堆：Heapify

#### 朴素思路

对 $n$ 个元素逐一 `push` 到初始为空的堆中——总代价 $O(n \log n)$。

#### Heapify 算法

巧妙做法：**从倒数第二层开始**，自底向上对每个内部节点调用 `bubble-down`：

```
for i = ⌊n/2⌋, …, 1:
    bubble-down(i)
```

#### 复杂度分析（关键洞察）

表面看：$n/2$ 次 `bubble-down`，每次 $O(\log n)$，似乎是 $O(n \log n)$。

**精细分析**：处于"层 $\ell$"（叶子层为 0）的节点上，bubble-down 的最坏代价仅为 $O(\ell)$。
而层 $\ell$ 上的节点数至多为 $\dfrac{n}{2^\ell}$。设根所在层 $h = \lfloor \log_2 n \rfloor$：

$$
\sum_{\ell = 1}^{h} \sum_{\text{nodes at level } \ell} \ell
\;\le\; \sum_{\ell = 1}^{h} \ell \cdot \frac{n}{2^\ell}
\;\le\; 2 n \;\in\; O(n)
$$

> **结论**：Heapify 可在 **$O(n)$** 时间内完成，比逐个 `push` 更优。

---

### 4.6 Heapsort（堆排序）

给定无序数组 $H[1] \ldots H[n]$：

**Step 1**：对 $H$ 调用 **Heapify**，构建大根堆（$O(n)$）。
**Step 2**：调用 `pop()` 共 $n - 1$ 次，每次将堆顶（当前最大值）取出（$O(\log n)$ × $n$ = $O(n \log n)$）。

> Heapsort 的最坏情况时间复杂度为 **$O(n \log n)$**。

#### Heapsort 的特性

| 特性 | 说明 |
|------|------|
| 平均速度 | 略**慢于** Quicksort（常数因子较大），但有**更强的最坏情况性能保证** |
| **In-place（原地）** | 是真正的原地排序——空间复杂度 $O(1)$ |
| **Stable（稳定性）** | **不稳定** |

---

## 五、本部分核心要点速查

### 排序算法对比

| 算法 | 最坏时间 | 期望/平均时间 | 空间 | 稳定 | 原地 |
|------|---------|--------------|------|------|------|
| Selection Sort | $O(n^2)$ | $O(n^2)$ | $O(1)$ | 否 | 是 |
| Merge Sort | $O(n \log n)$ | $O(n \log n)$ | $O(n)$ | 是 | 否 |
| Quick Sort | $O(n^2)$ | — | $O(\log n)$ 栈 | 否 | 是 |
| Randomized Quick Sort | — | $O(n \log n)$ 期望 | $O(\log n)$ | 否 | 是 |
| **Heapsort** | $O(n \log n)$ | $O(n \log n)$ | $O(1)$ | 否 | **是** |

> 比较模型下排序的**理论下界**为 $\Omega(n \log n)$，因此 Merge Sort、Heapsort 都是渐进最优。

### Master Theorem 速记

对 $T(n) = a T(n/b) + \Theta(n^d)$，比较 $a$ 与 $b^d$：

- $a < b^d$ → $\Theta(n^d)$（顶层主导）
- $a = b^d$ → $\Theta(n^d \log n)$（各层均衡）
- $a > b^d$ → $\Theta(n^{\log_b a})$（叶子主导）

### 二叉树遍历记忆口诀

- **Pre = Root 在前**（NLR）
- **In = Root 在中**（LNR）
- **Post = Root 在后**（LRN）
- **Level = BFS**

### Heap 关键复杂度

| 操作 | 代价 |
|------|------|
| `push` | $O(\log n)$ |
| `pop` | $O(\log n)$ |
| `update-key` | $O(\log n)$ |
| **`heapify`（构建）** | **$O(n)$** ← 关键考点 |
| `getTop` | $O(1)$ |
