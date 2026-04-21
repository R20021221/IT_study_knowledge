---
group: MAC & Network Layer (Group 1)
topic: MAC Sub-Layer
source_pdfs:
  - Week4-MAC-sublayer-1.pdf
  - Week5-MAC-sublayer-2.pdf
covers:
  - Medium Access Control 的作用与背景
  - 静态信道分配 (TDM / FDM)
  - 动态信道分配的假设条件
  - 多路访问协议分类 (Contention / Collision-Free / Limited Contention / Wireless)
  - ALOHA 与 Slotted ALOHA
  - CSMA (1-persistent / non-persistent / p-persistent)
  - CSMA/CD (带冲突检测)
  - Bit Map & Binary Countdown (无冲突协议)
  - Adaptive Tree Walk (有限竞争)
  - 无线 LAN：隐藏/暴露终端、MACA (RTS/CTS)
  - Ethernet 案例：经典以太网、帧格式、MAC 地址、最小帧长、信道效率、交换式以太网
language: 中文为主，英文术语保留
last_updated: 2026-04-21
---

# MAC Sub-Layer（介质访问控制子层）

> COMP90007 Internet Technologies · Week 4-5 复习笔记
> 对应 PDF：`Week4-MAC-sublayer-1.pdf`、`Week5-MAC-sublayer-2.pdf`

---

## 0. 背景：为什么需要 MAC 子层

Data Link Layer（数据链路层）包含两个子层：

- **LLC (Logical Link Control)**：负责流量控制、差错控制等。
- **MAC (Medium Access Control)**：**决定谁可以使用共享信道来发送数据**。

两种网络类型：

- **Point-to-point network（点对点网络）**：发送方-接收方是专用的一对，**不存在传输争用 (transmission contention)**。
- **Broadcast network（广播网络）**：多个站点共享一个信道，**必须解决"谁能发、何时发"的问题**。

> MAC 子层的核心任务：**协调多个站点对共享介质的访问，解决冲突 (collision)**。

---

## 1. 信道分配 (Channel Allocation)

把一个广播信道分给多个用户的思路分两大类：

| 分类 | 思路 | 特点 |
|------|------|------|
| **Static Channel Allocation（静态）** | 事先把信道划分好，每个用户固定占一份 | 简单，但不灵活 |
| **Dynamic Channel Allocation（动态）** | 实时根据需求分配 | 灵活，适合突发流量 |

### 1.1 静态信道分配

#### TDM (Time Division Multiplexing，时分复用)

- 把时间分成固定时隙 (time slot)，按**固定的时间表**轮流发送。
- 每个用户在自己的时隙里**独占整个带宽**，以**全速**发送。
- Slot 之间有 **guard time（保护时间）**，避免时隙重叠。
- 示例：**2G 移动网络**。

```
时间轴 →  [1|2|3|1|2|3|1|2|3|...]
```

#### FDM (Frequency Division Multiplexing，频分复用)

- 把频段切成多个子频段，每个用户只能在**自己专属的频率**上持续发送。
- 连续访问，但**每个用户的速率较低**（因为只占一段频率）。
- 示例：**电视 / 广播、ADSL、4G**。

```
频率轴 ↑ [用户1的频段]
         [用户2的频段]
         [用户3的频段]
```

#### 静态分配的局限

- 用户数 > 划分段数 → 新用户没法接入。
- 用户数不固定 → 资源严重浪费。
- **网络流量通常是突发的 (bursty)**，而静态分配给每人恒定带宽，效率低。

### 1.2 动态信道分配的基本假设

动态分配协议讨论时通常基于以下假设：

1. **单信道假设**：所有通信共用一个信道。
2. **独立站点**：每个站独立产生帧，互不协调。
3. **冲突假设 (Collision Assumption)**：同时发送 → 冲突 → 帧损坏（必须重传）。
4. **时间模型**：
   - **Continuous time**：随时可以开始发送。
   - **Slotted time**：只能在预定义的时隙起点开始发送。
5. **载波监听 (Carrier Sense)**：
   - **Carrier Sense**：发送前先监听信道是否空闲。
   - **No Carrier Sense**：不监听直接发。

> 这 5 条假设是后面所有动态协议的分析基础，理解它们才能区分各协议的差异。

---

## 2. 多路访问协议 (Multiple Access Protocols) 分类

| 大类 | 代表协议 | 特点 |
|------|----------|------|
| **Contention（竞争类）** | ALOHA、Slotted ALOHA、CSMA 家族 | 可能冲突，冲突后重传 |
| **Collision-Free（无冲突）** | Bit Map、Binary Countdown | 事先协商好顺序，永不冲突 |
| **Limited Contention（有限竞争）** | Adaptive Tree Walk | 把站点分组，组内竞争 |
| **Wireless LAN** | MACA / MACAW（802.11） | 专门解决无线的隐藏/暴露终端问题 |

---

## 3. 竞争类协议 (Contention)

### 3.1 ALOHA（纯 ALOHA）

- **规则**：**有数据就发**；如果发生冲突或收不到 ACK，**等一个随机时间再重试**。
- **优点**：无需中央控制。
- **缺点**：负载高时冲突严重，效率极低。
- **最大吞吐率理论值**：约 18.4%（S = G·e^(-2G) 在 G=0.5 时取最大值 ≈ 0.184）。

### 3.2 Slotted ALOHA（分时隙 ALOHA）

- **规则**：只能在**时隙起点**开始发送。
- **效果**：把"可能发生冲突的时间"从 2 帧时长缩短为 1 帧时长，冲突概率降低。
- **最大吞吐率**：约 36.8%（≈ 1/e），是纯 ALOHA 的 2 倍。

### 3.3 Carrier Sense Multiple Access (CSMA)

核心思想：**发送前先听一下信道**（Carrier Sense），不傻冲。

根据"听到忙时怎么办"，CSMA 有三种变体：

#### 1-persistent CSMA（1-坚持型）

- **持续监听信道**；一旦变空闲，**立刻以概率 1 发送**。
- 发送后检查冲突；若冲突，等随机时间再重来。
- **问题**：多个等待者会同时抢 → 冲突率偏高。

#### Non-persistent CSMA（非坚持型）

- 发现信道忙时，**等一个随机时间再监听**（而不是死盯着）。
- 空闲时立刻发。
- **优点**：多个等待者不会同时抢 → 冲突少。
- **缺点**：信道空闲时可能没人在听，**浪费空闲时间**。

#### p-persistent CSMA（p-坚持型）

- 针对 **slotted time**。信道空闲时：
  - 以概率 **p** 发送；
  - 以概率 **(1-p)** 等到下一个时隙再检查。
- 冲突后同样等随机时间。
- **通过调 p 在两种极端之间做权衡**。

#### 效率对比（定性）

- **CSMA > ALOHA**（"先听再说"肯定比瞎说强）。
- **高负载下，less persistent 更好**（1-persistent 在高负载下冲突极多）。
- **低负载下，1-persistent 更好**（反应快，利用率高）。

### 3.4 CSMA/CD（带冲突检测）

- **动机**：即便监听了，两个站几乎同时开始仍会冲突。冲突浪费的时间越长越浪费。
- **规则**：
  1. 发送中**持续监听信道**。
  2. 一旦检测到冲突，**立刻中止传输**（而不是把整个帧发完）。
  3. 等一个随机时间后重试。
- **好处**：大幅降低冲突浪费的时间，是**经典 Ethernet** 采用的协议。

---

## 4. 无冲突协议 (Collision-Free)

核心思想：**在真正发送数据之前，先用某种约定把"发送权"确定下来**，这样发送时就不会冲突。

### 4.1 Bit Map Protocol（位图协议）

- **预约型协议 (Reservation-based)**：
  - 设 N 个站，每轮预约阶段有 **N 个 contention slots**，**每个站占 1 bit**。
  - 想发数据的站在自己的那一 bit 里填 1。
  - 预约阶段结束后，按编号顺序依次发送。
- **优点**：永不冲突（把"预约"和"发送"分开）。
- **缺点**：N 很大但实际想发的站很少时，预约阶段开销大。

### 4.2 Binary Countdown（二进制倒计数）

- 用**二进制地址**来决定谁先发：**地址大的优先级高**。
- 竞争阶段每个时隙传一位，总共 **log₂ N** 个时隙。
- **信道执行按位 OR**：
  - 每站从**高位**开始广播自己的地址。
  - 一旦某站发出 **0 却看到信道上是 1**（说明有更高地址的站在竞争），**立刻放弃**。
- 竞争结束后，**看到自己完整地址**的站就是胜者，开始发送。

> Bit Map 需要 N 个 slot，Binary Countdown 只需 log₂N 个 slot，**效率随站数增长更好**。

### 4.3 竞争 vs 无冲突，如何取舍

- **低负载**下：冲突本来就少，无冲突协议的"预约开销"反而成了负担 → 竞争类更好。
- **高负载**下：冲突非常频繁，竞争类浪费大量带宽 → 无冲突类更好。
- **两类协议都会在某个点变低效**。

---

## 5. 有限竞争协议 (Limited Contention)

### 5.1 思路

把竞争类和无冲突类**融合**：

- **把站分成若干组**，组内站数少 → 组内发生冲突的概率低 → 可以继续用竞争；
- 组间有序调度 → 避免全网混战。

### 5.2 Adaptive Tree Walk（自适应树遍历）

- 所有站在根节点竞争，若冲突，**用二分法把站分成两半**（像深度优先搜索 DFS 一棵二叉树）。
- 每个"组节点"相当于树的一个子树，遇到冲突就下探到子树继续解决。

**例子 1：D 和 G 想发**

```
Slot 1: {D,G} 都发 → 冲突
Slot 2: D 单独发 → 成功
Slot 3: G 单独发 → 成功
```

**例子 2：B、D、G 想发**

```
Slot 1: {B,D,G} → 冲突
Slot 2: {B,D} → 冲突
Slot 3: B → 成功
Slot 4: D → 成功
Slot 5: G → 成功
```

- 优势：**只在出现冲突的子树里继续搜**，空闲子树直接跳过，浪费少。

---

## 6. 无线 LAN 协议

### 6.1 无线的特殊难题

- 无线站点只有**有限的覆盖范围**。
- 若某站位于**两个发射者的覆盖范围内**，信号会在它那里产生干扰。
- 单纯依赖 carrier sense 不够，**必须感知"接收端附近"的传输情况**。

### 6.2 隐藏终端 (Hidden Terminal)

- **定义**：两个发送者**互相听不到彼此**，但却会在**同一个接收者**处造成冲突。
- 拓扑示例：A — B — C，A 和 C 都在 B 的范围内，但 A、C 之间互不可见。
- 结果：A 和 C 以为信道空闲，同时发往 B → B 处冲突。
- **属于损失效率，必须避免**。

### 6.3 暴露终端 (Exposed Terminal)

- **定义**：两个发送者**能互相听到**，但它们的接收者不同，其实**可以并发发送**而不互相干扰。
- 拓扑示例：A — B — C — D，B 发给 A，C 发给 D，互不干扰。
- 但如果 C 听到 B 在发就延迟，就**浪费了可并发的机会**。
- **属于可取的并发，应该允许**。

> 关键洞察：在无线环境中，应**基于接收端的情况**来决定能否发送，而不是基于发送端监听。

### 6.4 MACA (Multiple Access with Collision Avoidance)

通过 **RTS/CTS 握手**来通知接收端周围的站：

- **RTS (Request-to-Send)**：发送方先发一个短控制帧请求发送。
- **CTS (Clear-to-Send)**：接收方回一个短控制帧允许发送。
- **接收方周围的站听到 CTS 后都会让路**（延迟自己的发送）。
- 发送方听到 CTS 后才开始传数据。

#### MACA 工作示例

场景：A 想给 B 发数据。

```
Step 1: A → RTS → B
        C、E 听到 RTS，延迟自己的发送，等待 CTS 看看结果。

Step 2: B → CTS → A
        D、E 听到 CTS，延迟发送，等待 A→B 的数据交换完成。

Step 3: A → 数据 → B
```

- 这样**隐藏终端被抑制**（C 听到 RTS 会退让）。
- **暴露终端不会被抑制**（某些站听到 RTS 但没听到 CTS，说明与接收方无关，可以发）。

---

## 7. 案例研究：Ethernet（以太网）

### 7.1 Classic Ethernet（经典以太网）

- 速率：3 ~ 10 Mbps。
- 每种 Ethernet 有**每段线缆的最大长度**（例如 100 m）。
- 多段可以用 **repeater（中继器）** 连起来：物理设备，**接收、放大并双向重发信号**，不做任何过滤。

### 7.2 Ethernet 帧格式 (IEEE 802.3)

| 字段 | 长度 | 作用 |
|------|------|------|
| Preamble | 7B | 同步发送方与接收方 |
| Start of Frame (SFD) | 1B | FLAG byte，标记帧起始 |
| Destination Address | 6B | 目的 MAC 地址 |
| Source Address | 6B | 源 MAC 地址 |
| Type or Length | 2B | 指明上层协议或长度 |
| Data | 0 ~ 1500B | 载荷 |
| Pad | 0 ~ 46B | 填充以满足**最小帧 64B** |
| CRC | 4B | 32 位校验 |

- **MAC 协议**：1-persistent CSMA/CD。
- **冲突后的随机延迟**由 **BEB (Binary Exponential Backoff，二进制指数退避)** 计算：第 i 次冲突后从 `[0, 2^i - 1]` 中随机取一个数作为延迟（`i` 有上限，防止发散）。

### 7.3 MAC 地址

- 为物理网卡提供**全球唯一标识**。
- **48 位**，在帧中编码，**十六进制**表示，例如 `00:02:2D:66:7C:2C`。
- 十六进制 ↔ 二进制速查：

| Hex | Bin | Hex | Bin |
|-----|-----|-----|-----|
| 0 | 0000 | 8 | 1000 |
| 1 | 0001 | 9 | 1001 |
| 2 | 0010 | A | 1010 |
| 3 | 0011 | B | 1011 |
| 4 | 0100 | C | 1100 |
| 5 | 0101 | D | 1101 |
| 6 | 0110 | E | 1110 |
| 7 | 0111 | F | 1111 |

### 7.4 经典以太网的最小帧长

- **为什么需要最小帧长？** 可靠检测冲突**至少需要 2τ** 的时间（τ 是信号在整条线上传播一次的时间）：
  - 最坏情况下，A 刚发完最后一位时，B 那头的冲突信号才传回来。
- 如果帧太短，A 已经发完帧走人了，还没等到冲突信号 → **漏检**。
- 所以发送帧的时长必须 ≥ 2τ，从而得到**最小帧长**约束。
- Ethernet 规定**最小帧 64B**（头 + 数据 + CRC）。

### 7.5 Ethernet 信道效率

$$
\text{Channel Efficiency} = \frac{1}{1 + \dfrac{2BLe}{cF}}
$$

- **F**：帧长度 (bit)
- **B**：带宽 (bps)
- **L**：电缆长度 (m)
- **c**：信号传播速度 (m/s)
- **e** ≈ 2.71828（最优情况下每帧有 e 个争用时隙）

**解读**：

- 当 `cF` 很大（帧长或光速大），效率高。
- 带宽 B 或距离 L 变大，效率反而下降（更多冲突窗口）。
- **启示**：高速 + 长距离网络 + CSMA/CD 不太合适 → 推动了**交换式以太网**的出现。

### 7.6 Switched Ethernet（交换式以太网）

- 速率：100 Mbps / 1 Gbps / 10 Gbps 等。
- **Hub vs Switch**：

| 设备 | 冲突域 | 原理 |
|------|--------|------|
| **Hub（集线器）** | 所有端口共用**一个冲突域** | 广播到所有端口 |
| **Switch（交换机）** | **每个端口是独立的冲突域** | 基于 MAC 地址表转发 |

- Switch 的好处：
  - **吞吐量大幅提升**（多对端口可同时收发）。
  - **全双工 (full-duplex)** 时**不需要 CSMA/CD**（没有冲突可言）。

---

## 8. 章节总结速查

### 8.1 协议分类一览

```
Multiple Access Protocols
├─ Contention
│   ├─ ALOHA
│   ├─ Slotted ALOHA
│   └─ CSMA
│        ├─ 1-persistent
│        ├─ non-persistent
│        ├─ p-persistent
│        └─ CSMA/CD（Ethernet 使用）
├─ Collision-Free
│   ├─ Bit Map
│   └─ Binary Countdown
├─ Limited Contention
│   └─ Adaptive Tree Walk
└─ Wireless LAN
    └─ MACA / MACAW（RTS/CTS）
```

### 8.2 常考对比表

| 维度 | 静态 (TDM/FDM) | 竞争类 | 无冲突 | 有限竞争 |
|------|----------------|--------|--------|----------|
| 是否可能冲突 | 否 | 是 | 否 | 低（组内可能） |
| 低负载效率 | 低 | 高 | 低 | 中 |
| 高负载效率 | 固定 | 低 | 高 | 中高 |
| 代表协议 | TDM、FDM | CSMA/CD | Binary Countdown | Tree Walk |

### 8.3 易混点

- **Hidden vs Exposed 终端**：
  - Hidden：互相听不到 → 错误冲突（坏事，要避免）。
  - Exposed：互相能听到但能并发 → 错失机会（好事被放弃了，要允许）。
- **1-persistent vs non-persistent**：一个是死等，一个是等一会儿再看。
- **TDM vs Slotted ALOHA**：TDM 是预先固定分配时隙，Slotted ALOHA 是所有人抢时隙。
- **Hub vs Switch**：Hub 是一大锅，Switch 是隔开的小锅。

---

## 9. 快速自测问题

1. 静态分配为什么在突发流量下低效？
2. Slotted ALOHA 的吞吐为什么是纯 ALOHA 的 2 倍？
3. 三种 CSMA（1/non/p-persistent）在高/低负载下分别表现如何？
4. CSMA/CD 如何判断"碰撞发生了"？为什么要最小帧长？
5. Binary Countdown 为什么不会冲突？它需要几个竞争时隙？
6. 什么是隐藏终端和暴露终端？MACA 如何解决它们？
7. RTS/CTS 具体传输了什么信息？为什么只有 CTS 能"保护"数据传输？
8. Hub 和 Switch 在冲突域方面的区别是什么？为什么 full-duplex 不需要 CSMA/CD？

---

*本文档对应 Group 1（MAC & Network Layer）复习，另见 `Network-layer.md` 与 `INDEX.md`。*
