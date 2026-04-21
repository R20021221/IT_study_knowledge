---
group: MAC & Network Layer (Group 1)
topic: Network Layer
source_pdfs:
  - Week5-network-layer-1.pdf
  - Network-Layer-2-1.pdf
  - Network-Layer-3.pdf
  - Network-Layer-4.pdf
covers:
  - 网络层在 Internet 中的位置与职责
  - Store-and-Forward 分组交换
  - 网络层服务类型 (Connectionless 数据报 / Connection-oriented 虚电路)
  - Datagram vs Virtual-Circuit 对比 (时间、内存、持续性、脆弱性)
  - Internetworking 动机与挑战
  - Tunneling（IPv6-in-IPv4 隧道等）
  - Fragmentation（透明 / 非透明）
  - Path MTU Discovery
  - IPv4 Datagram 结构（各字段含义）
  - IP 地址、点分十进制、层次结构、前缀与子网掩码
  - Subnetting 子网划分
  - CIDR（无类域间路由）与最长前缀匹配
  - Classful Addressing（A/B/C/D/E）
  - 私有 IP 与 NAT
  - IPv6 基础
  - 控制协议：ICMP、ARP、DHCP
  - 路由算法（Shortest Path/Dijkstra、Flooding、Distance Vector、Link State、Hierarchical、Broadcast/RPF）
  - Optimality Principle 与 Sink Tree
language: 中文为主，英文术语保留
last_updated: 2026-04-21
---

# Network Layer（网络层）

> COMP90007 Internet Technologies · Week 5-6 复习笔记
> 对应 PDF：`Week5-network-layer-1.pdf`、`Network-Layer-2-1.pdf`、`Network-Layer-3.pdf`、`Network-Layer-4.pdf`

---

## 0. 网络层在协议栈中的位置

从下往上看 TCP/IP 模型：

| 层 | 负责 | 关键词 |
|----|------|--------|
| Physical | 传输介质、信号、调制 | 比特流 |
| Data Link | Framing、差错/流量控制、MAC | 帧、相邻节点间 |
| **Network** | **连接不同网络 (internetworking)、路由** | 数据包、跨网络 |
| Transport | 端到端传输（TCP/UDP） | 进程到进程 |
| Application | HTTP、DNS 等 | 用户数据 |

网络层的核心任务：

1. **Internetworking**：把**不同类型的网络**连起来，形成一个更大的网络（"网络的网络"）。
2. **Routing（路由）**：决定数据包从源主机到目的主机应该走哪条路径。

---

## 1. Internet 的网络层

- **Internet** = 一堆互联的网络，彼此使用 **IP (Internet Protocol)** 通信。
- **IP 提供 best-effort（尽力而为）服务**：
  - 尽量把数据报（datagram）从源主机送到目的主机；
  - **不保证成功、不保证顺序、不保证不重复**。
- 源和目的主机**可能在不同网络**上。
- 下方承载多样：SONET、ADSL、4G、5G 等都行。

### 1.1 Store-and-Forward Packet Switching（存储转发分组交换）

- **Host（主机）**生成包（packet），把包注入网络。
- **Router（路由器）**把包路由到目的地：
  - 把包当成消息先 **receive/store（接收并暂存）**；
  - 再根据目的地址 **forward（转发）**。

### 1.2 网络层对传输层的承诺

- 网络层**向上为传输层服务**，并且**独立于路由器技术**。
- 传输层应该**对路由器的数量、类型、拓扑毫无感知**。
- **地址方案**必须统一（uniform numbering scheme）。

---

## 2. 服务类型：Connectionless vs Connection-Oriented

| 对比项 | Connectionless（无连接） | Connection-Oriented（面向连接） |
|--------|--------------------------|----------------------------------|
| 另一个名字 | Datagram（数据报） | Virtual Circuit，VC（虚电路） |
| 哲学 | Internet 社区 | 电信社区 |
| 包的路径 | **每个包独立路由**，可走不同路径 | 所有包**沿同一条路径** |
| 路由决策频率 | 每包一次 | **只在建立连接时一次** |
| 可靠性 | **不保证**，QoS 靠其他层实现（流量/差错控制） | **保证**，QoS 是头等公民 |
| 类比 | Post office（邮政） | 电话网络 |
| 代表 | IP（Internet） | MPLS（Multiprotocol Label Switching） |

### 2.1 Datagram Subnet 中的路由

- 所谓"邮政模型"：**根据目的地址独立转发每个包**。
- 每个路由器维护**路由表 (routing table)**；**路由算法 (routing algorithm)** 负责更新这张表。
- 例：P1 发一个长消息给 P2，分成多个包，每个包可能走不同路。

### 2.2 Virtual-Circuit Subnet 中的路由

- 建立连接时选定一条路径，**该连接的所有包都走这条路径**。
- 路由表的关键是**连接 ID**（connection id），而不是目的地址：

```
A's table:  connection_id_in → connection_id_out
```

- 典型应用：**MPLS** 网络。

### 2.3 两种方式的取舍

**时间 (Time consumption)**：
- **VC**：**需要建立时间和资源**，但建立后数据包传输**非常快**（只看标签就转发）。
- **Datagram**：每个包都要**解析地址查表**，单包更慢。

**路由器内存 (Memory)**：
- **VC**：每条虚电路**占一个表项**。
- **Datagram**：要保存**每个可能目的地**的条目，表很大。

**长期性 (Longevity)**：
- **VC**：适合**长期、重复**的连接（e.g. Permanent VC）。
- **Datagram**：适合**频繁变化**的连接。

**脆弱性 (Vulnerability)**：
- **VC**：对硬件/软件崩溃特别敏感，一挂所有 VC 全挂，**重建前毫无流量**。
- **Datagram**：**可以走备用路径**。

> 记忆技巧：VC 像 ** 租用电话线**（建立慢但之后快、易断），Datagram 像**寄信**（每封都查地址、但丢一封不影响别的）。

### 2.4 不同网络的差异（为什么互联难？）

连接不同网络时会遇到：
- **服务类型**：connectionless vs connection-oriented；
- **分组大小**：不同的最大大小（MTU）；
- **寻址**：长度不同、扁平/层次不同；
- **QoS**：有的有、有的没有；
- **安全**：隐私规则、加密方式差异。

---

## 3. Internetworking（互联网络）

### 3.1 目标与挑战

- **Internetworking**：把**多个不同的网络**连成一个更大的网络。
- 挑战：
  - 网络类型与协议不同；
  - 硬件软件技术互不兼容；
  - 设计目标与动机不同。
- **解决方案**：使用**统一的网络层协议 IP** 把它们联通。

### 3.2 Tunneling（隧道）

- **适用场景**：源和目的在**同一种网络**上，但中间隔着**另一种不同类型**的网络。
- **做法**：**封装 (encapsulate)**——把源网络的包整体塞进中间网络的包里，穿过去以后再剥壳。
- 经典例子：**IPv6 over IPv4 Tunneling**
  - 两端 IPv6，中间只支持 IPv4；
  - 发送端把 IPv6 包作为 IPv4 包的 payload；
  - 接收端解封装，还原 IPv6 包。

### 3.3 Fragmentation（分片）

- **MTU (Maximum Transmission Unit)**：每个网络规定能传输的**最大包大小**，受限于：
  - 硬件与操作系统；
  - 协议与标准；
  - 传输效率。
- 当大包需要穿过 MTU 较小的网络时，必须**分片**。

两种分片方式：

#### Transparent Fragmentation（透明分片）

- 每进入一个小 MTU 网络就**分片**，离开时在**出口网关重组 (reassemble)**。
- 下一个网络再按需重新分片。
- **路由受限**（所有片必须在同一个出口网关会合）。
- 示意：`G1 fragments → 穿越网络 → G2 reassembles → G3 fragments → G4 reassembles`。

#### Non-Transparent Fragmentation（非透明分片，IP 使用这种）

- **分片后一路不重组，直到目的地才拼回去**。
- 路由器工作量小；
- 每个片必须带：**包编号 (packet number)、字节偏移 (byte offset)、是否最后一个 (end of packet flag)**。

#### IP 式分片示例（非透明）

```
原包：10 数据字节
→ 分成 8 字节/片后（实际还要加头部开销）
→ 再次分成 5 字节/片

每个片都记录自己的 byte offset。
```

### 3.4 Path MTU Discovery（路径 MTU 发现）

- **是分片的替代方案**。
- **思路**：**在发送端事先发现整条路径上最小的 MTU**，让源直接发不超过这个大小的包，**网络中就不必分片**。
- 做法：发大包，路由器若容不下就返回 ICMP 错误包告诉源"太大了"；源逐步缩小。
- 若路径或路径 MTU 发生变化，会再次触发错误包，源再适配。
- **示例**：试 1200 → 报错 → 试 900 → 报错 → ...

> 现代 IPv4 和 IPv6 都偏好 Path MTU Discovery 而非分片，因为分片损失效率、复杂度高、IPv6 干脆**禁止路由器分片**。

---

## 4. Internet Protocol（IP）

### 4.1 IPv4 Datagram 结构

IPv4 数据报 = **Header + Payload**。
- **Header**：20 字节**固定部分** + 可选部分。

主要字段：

| 字段 | 含义 |
|------|------|
| **Version** | 4 (IPv4) |
| **IHL (Internet Header Length)** | 头部长度（单位 32-bit 字），**min=5, max=15** |
| **Differentiated Services** | 区分服务，用于不同服务等级（QoS） |
| **Total Length** | 整个数据报长度（头 + 载荷），**最大 65,535 字节** |
| **Identification** | 标识符，用于判断分片归属；同一原始数据报的所有片 **ID 相同** |
| **DF (Don't Fragment)** | 1 表示**不允许分片**，是 Path MTU Discovery 的关键 |
| **MF (More Fragment)** | 1 表示**后面还有片**；最后一片 MF=0 |
| **Fragment Offset** | 当前片在原始数据报中的**位置**（以 8 字节为单位） |
| **TTL (Time to Live)** | 最大存活时间，以跳数或秒为单位，每经过一个路由器 -1，防止包在网络中无限循环 |
| **Protocol** | 上层协议，如 TCP (6) / UDP (17) |
| **Header Checksum** | **仅校验头部**的完整性 |
| **Source Address** | 32-bit 源 IP |
| **Destination Address** | 32-bit 目的 IP |
| **Options** | 可选，如 security、strict/loose source routing、record route、timestamp |

> 易考点：Checksum **只覆盖 header**，payload 完整性由上层（TCP）或链路层（CRC）保证。

### 4.2 IP 地址基础

- **IPv4 地址** = **32 位**，写成**点分十进制 (dotted decimal notation)**：`128.18.3.11`。
- 每段 0-255（8 bit）。
- 分配由 **ICANN (Internet Corporation for Assigned Names and Numbers)** 管理。
- **IPv4 地址耗尽问题**：约 2^32 ≈ 43 亿，早已不够，催生了 NAT 和 IPv6。

### 4.3 层次结构与前缀

- IP 地址分两部分：**网络部分 (network portion) + 主机部分 (host portion)**。
- **按块分配**：一块里所有主机**共享相同的网络部分**。
  - 例：256 个地址的块 `128.18.3.0 – 128.18.3.255`。
- **前缀表示法 (Prefix)**：`lowest_address / bit_length`
  - `128.18.3.0/24`（24 位网络部分，256 个地址）
  - `18.2.0.0/16`（16 位网络部分，65536 个地址）

### 4.4 Subnet Mask（子网掩码）

- **网络部分全 1，主机部分全 0**。
- 例：`/24` 的 mask 是 `255.255.255.0`。
- **提取网络部分**：对 IP 地址和子网掩码做**按位 AND**。

```
IP:   128.18.3.11  = 10000000.00010010.00000011.00001011
Mask: 255.255.255.0 = 11111111.11111111.11111111.00000000
AND:  128.18.3.0   = 10000000.00010010.00000011.00000000  ← 网络部分
```

### 4.5 路由表结构

路由表通常基于三元组：

| 字段 | 说明 |
|------|------|
| Network Destination | 目的网络 |
| Subnet Mask | 子网掩码（决定匹配多少位） |
| Outgoing Line / Interface | 出接口（物理或虚拟） |

示例一行：

| Destination | Subnet Mask | Interface |
|-------------|-------------|-----------|
| 128.18.3.0 | 255.255.255.0 | Eth 0 |

### 4.6 Subnetting（子网划分）

- **内部**将一大块网络**拆成若干个子网**，**对外仍表现为单一网络**。
- 例：ISP 给墨尔本大学一个 `128.208.0.0/16`（2^16 = 65,536 个地址）。
  - 内部按院系拆分：
    - `128.208.0xxxxxxxxxxxxxxx`（前两位 `00`，2^14 = 16,384 个）→ 院系 A
    - `128.208.010xxxxxxxxxxxxx`（2^13）→ CS/EE
    - `128.208.011xxxxxxxxxxxxx`（2^13）→ Art
    - `128.208.1xxxxxxxxxxxxxxx`（2^15）→ 其它
  - **对外**仍然一个 `/16`。
- **前缀越长 → 地址块越小**。

### 4.7 CIDR（Classless Inter-Domain Routing，无类域间路由）

**问题**：全球主干路由器连接约 30 万个网络，路由表爆炸。

**解决**：
- **Aggregation（聚合）**：把**多个小前缀合并成一个大前缀**，减少表项。
- **Longest Prefix Match（最长前缀匹配）**：转发时选**匹配最长前缀**（即**最小的地址块**）的条目。

示例：

| Destination | Subnet Mask | Interface |
|-------------|-------------|-----------|
| 192.24.12.0 | 255.255.252.0 | Eth 0 (to SF) |
| 192.24.0.0  | 255.255.224.0 | Eth 1 (to London) |

- 先看是否匹配 `192.24.12.0/22`（更长、更具体）；
- 不匹配才回退到 `192.24.0.0/19`。
- **具体 > 笼统**。

> 好处：既能做大聚合减表项，又能**精确地为个别子块单独开路**（"Except for this part!"）。

### 4.8 Classful Addressing（早期分类寻址）

- 早期 IP 地址按**固定大小的块**分配，分为 A/B/C/D/E 类：
  - Class A：`0...`，大网络（2^24 主机）。
  - Class B：`10...`，中等网络（2^16 主机）。
  - Class C：`110...`，小网络（2^8 主机）。
  - Class D：`1110...`，多播（multicast）。
  - Class E：`1111...`，保留（实验）。
- **问题**：**每类编码在地址里，缺乏灵活性**——小公司拿 C 不够、拿 B 又太浪费。
- **现状**：已被 **CIDR 取代**。

### 4.9 Private IP Ranges（私有 IP）

**不在公网可路由**，只用于私有网络：

| 网段 | 主机数 |
|------|--------|
| `10.0.0.0/8` | 2^24 ≈ 16,777,216 |
| `172.16.0.0/12` | 2^20 ≈ 1,048,576 |
| `192.168.0.0/16` | 2^16 = 65,536 |

### 4.10 NAT（Network Address Translation，网络地址转换）

- **NAT 盒**把**一个外部 IP** 映射到**多个内部 IP**。
- 用 **TCP/UDP 端口号** 区分不同内部连接：
  - 内部 `10.0.0.5:1234` → 外部 `203.0.113.7:40001`
  - 内部 `10.0.0.6:1234` → 外部 `203.0.113.7:40002`
- **优点**：极大节省全球 IPv4 地址。
- **缺点**：
  - **违反分层原则**（网络层窥探传输层端口）。
  - 端到端透明性被破坏，某些 P2P / 入向连接困难。

### 4.11 IPv6

- **128 位地址**，用**冒号分隔的十六进制**表示：
  ```
  2001:0db8:85a3:0000:0000:8a2e:0370:7334
  ```
- **地址空间巨大**（2^128 ≈ 3.4 × 10^38）。
- 内置更多**安全支持**：加密、认证。
- **头部固定 40 字节**（IPv4 是 20 字节 + options，IPv6 头部设计为定长以加速处理，可选功能放扩展头）。
- **过渡方案**：
  - **Dual Stack（双栈）**：同时支持 IPv4/IPv6。
  - **Tunneling**：如 IPv6-in-IPv4 隧道。
  - **Header Translation**：在边界做头部翻译。

---

## 5. Internet 控制协议

IP 本身很"傻"，需要一系列控制协议辅助：

### 5.1 ICMP（Internet Control Message Protocol）

- **伴随 IP 存在**，用于**返回错误信息**和**网络状态探测**。
- 典型用途：
  - **`traceroute`**：利用 TTL 逐跳递增，收集沿途路由器的 ICMP 错误包来画出路径。
  - **`ping`**：发送 Echo Request，等待 Echo Reply，测试连通性与延迟。
- 用于主机和路由器间**测试与监控**网络环境。

### 5.2 ARP（Address Resolution Protocol）

- **从本地 IP 地址找到对应的 MAC 地址**。
- **流程**：
  1. 主机在本地广播"**谁是 192.168.1.5？请告诉我你的 MAC**"。
  2. 拥有该 IP 的主机回复自己的 MAC。
- 结果通常缓存到 **ARP cache**。

> 为什么需要 ARP？IP 是网络层的逻辑地址，数据帧在链路层传输必须用 MAC 地址。ARP 就是这两层之间的翻译。

### 5.3 DHCP（Dynamic Host Configuration Protocol）

- **自动给主机分配本地 IP 地址**及网络配置（网关、DNS 等）。
- 流程：主机向 DHCP 服务器发请求，服务器**租用 (lease)** 一个 IP 给主机，带租期。
- 优点：**即插即用**，避免手工配 IP。

---

## 6. 路由算法（Routing Algorithms）

### 6.1 路由基础

- **Routing**：**发现网络路径**的过程。
- 把网络看成**图**：节点 = 路由器，边 = 链路。
- **优化指标**：跳数（hops）、延迟、带宽、费用等。
- 拓扑变化（路由器故障、链路断等）时要**更新路由**。

### 6.2 分类

| 类别 | 特点 | 代表算法 |
|------|------|----------|
| **Non-Adaptive（非自适应，静态）** | 决策不随网络变化 | Shortest Path、Flooding |
| **Adaptive（自适应，动态）** | 根据拓扑/流量实时调整 | Distance Vector、Link State |
| **Hierarchical（分级）** | 把路由器分区，降低表大小 | — |
| **Broadcast（广播）** | 一对多发送 | Multi-destination、RPF |

### 6.3 最优化原理 (Optimality Principle)

> 如果 B 在 A 到 C 的最优路径上，那么 **B 到 C 的最优路径**也沿**同一条路线**走。

- 这是**动态规划**式的最优子结构，也是 Dijkstra 等算法的理论基础。

### 6.4 Sink Tree（汇集树）

- 对于某个目的节点 D，**所有源到 D 的最优路径**构成一棵**以 D 为根的树**。
- 路由算法的目标：**发现并利用每个路由器对应的 sink tree**，为每个包选最优下一跳。

### 6.5 Shortest Path Routing（最短路径路由，非自适应）

- 图上每条边赋一个**非负权重 / 距离**。
- 最短路径 = 总权重最小。
- 权重都取 1 → **最少跳数 (fewest hops)** 的路径。

#### Dijkstra 算法

> 用于在**单源最短路径**问题上构造 sink tree。

算法步骤（以 sink 节点为原点向外扩散）：

1. 建一个**集合 P**，记录已加入最短路径树的节点，初始为空。
2. 对每个节点，维护到 sink 的**距离估计 d**，初始全为 ∞。
3. 从 **sink 节点开始**，把它的 d 设为 0。
4. 循环直到 P 包含所有节点：
   - i. 对所有不在 P 中的节点，比较 d；
   - ii. 选 d 最小的节点 **v**，加入 P；
   - iii. 用 v 更新其所有邻居的 d（"是否经过 v 更近？"）。

#### Dijkstra 运行示例（构造到 A 的 sink tree 的反向过程）

初始表（以到 A 的距离为例，其它节点无穷）：

| step | A | B | C | D | E | F | G | H | 加入 P |
|------|---|---|---|---|---|---|---|---|--------|
| 1 | **0** | ∞ | ∞ | ∞ | ∞ | ∞ | ∞ | ∞ | {A} |
| 2 | — | **2** | ∞ | ∞ | ∞ | ∞ | 6 | ∞ | {A, B} |
| 3 | — | — | 9 | ∞ | **4** | ∞ | 6 | ∞ | {A, B, E} |
| 4 | — | — | 9 | ∞ | — | 6 | **5** | ∞ | {A, B, E, G} |
| 5 | — | — | 9 | ∞ | — | **6** | — | 9 | {A, B, E, G, F} |
| 6 | — | — | 9 | ∞ | — | — | — | **8** | {A, B, E, G, F, H} |
| 7 | — | — | **9** | 10 | — | — | — | — | {A, B, E, G, F, H, C} |
| 8 | — | — | — | **10** | — | — | — | — | {A, B, E, G, F, H, C, D} |

- **直觉**：每轮把"当前看起来离 A 最近的未处理节点"锁定，然后用它去松弛 (relax) 邻居。

### 6.6 Flooding（泛洪，非自适应）

- **规则**：每个到达包被**从所有其它链路**发出去（除了来的那条）。
- **缺点**：产生大量**重复包**，浪费带宽。
- **改进：Selective Flooding（选择性泛洪）**
  - 只在**大致正确方向**的链路上转发。
- **常见用途**：
  - 军事/健壮性场景（节点可能失效）；
  - **Link State 协议**内部用泛洪传播 LSP。

### 6.7 Distance Vector Routing（距离向量，自适应）

- **思想**：每个路由器维护一张表，记录**到每个目的地的最小已知距离**以及**走哪条线**。
- **通过与邻居交换信息**来更新表。
- **全局信息、局部共享**。

#### 算法（每个路由器）

1. 知道到每个**邻居**的距离（直连度量）；
2. 向所有邻居**广播**自己当前的**距离向量**（自己到所有目的地的最小距离）；
3. 接收邻居发来的向量，**更新自己**的距离向量：
   - `d_me_to_X = min( d_me_to_neighbor + d_neighbor_to_X )`（遍历所有邻居）
4. **周期性重复**，以反映网络变化。

#### 示例

- 路由器 J 收到邻居 A、I、H、K 的向量：
  - JA = 8, JI = 10, JH = 12, JK = 6
- 将自己到某个目的地的距离更新为经过 4 个邻居后最小的那个。

#### 常见问题

- **Count-to-Infinity（计数到无穷）**：链路断了以后，坏消息传播得慢，可能绕圈加距离。
- 解决：**Split Horizon**、**Poison Reverse** 等技巧。

### 6.8 Link State Routing（链路状态，自适应）

- **动机**：Distance Vector **收敛慢**，大网络不好用。
- 代表协议：**OSPF (Open Shortest Path First)** — 广泛用于 Internet。
- **本地信息，全局共享**（泛洪分发）。
- **比 Distance Vector 计算量大**，但收敛快、可扩展。

#### 算法（每个路由器）

1. **发现邻居**，知道它们的网络地址（如发 Hello 包）；
2. **测量到每个邻居的距离**（可基于延迟、带宽、跳数）；
3. **构造 Link State Packet (LSP)**：列出自己的邻居及其距离；
4. **把 LSP 发给所有其它路由器**（用 flooding）；
5. **每个路由器自己用 Dijkstra 计算**到所有其它路由器的最短路径。

#### LSP 的触发时机

- **周期性**：按固定间隔发送；
- **事件触发**：链路 up/down、度量显著变化时立刻发。

### 6.9 Hierarchical Routing（分级路由）

- **问题**：网络增长 → 路由表膨胀 → **CPU 和内存压力大**。
- **解决**：**分区 (regions)**。
  - 每个路由器**只知道自己区内的细节**，对其它区**只知道一个汇总条目**；
  - **跨区路由器**作为交换点。
- **代价**：路径可能**略长**于完全平坦的最优路径（换取规模可扩展）。

### 6.10 Broadcast Routing（广播路由）

广播 = 一个源**发给所有其它主机**。

几种实现：

1. **Single distinct packet to each destination**
   - 源为每个目的地都发一份独立的包。
   - **低效 & 需要所有目的地址**。
2. **Multi-destination Routing（多目的路由）**
   - 路由器在每条出链路上**复制一份**包。
   - **带宽用得更好**，但源仍需知道所有目的地址。
3. **Flooding**
   - 每个路由器把包从所有链路转发。
4. **Reverse Path Forwarding (RPF，反向路径转发)** — 广播的明星算法。

#### Reverse Path Forwarding (RPF)

- **想法**：广播包到达时检查"**这个包是不是沿着我通常发往源的那条线到来的？**"
- **Yes**：这很可能是**最佳路径 & 第一份副本** → **复制并转发到其它所有链路**。
- **No**：很可能是**重复副本** → **丢弃**。
- **本质**：利用对称性把"走向源"的最优线作为"来自源"的最优线，简单有效地抑制重复。

---

## 7. 章节总结与速查

### 7.1 大图

```
Network Layer
├─ Service Types
│   ├─ Connectionless (Datagram) — 每包独立
│   └─ Connection-Oriented (Virtual Circuit) — 同路径
├─ Internetworking
│   ├─ Tunneling (封装过异质网络)
│   ├─ Fragmentation (透明 / 非透明-IP)
│   └─ Path MTU Discovery
├─ Internet Protocol
│   ├─ IPv4 Header fields
│   ├─ Addressing (dotted decimal, prefix, subnet mask)
│   ├─ Subnetting
│   ├─ CIDR + Longest Prefix Match
│   ├─ Classful (A/B/C/D/E)
│   ├─ Private IP + NAT
│   └─ IPv6
├─ Control Protocols
│   ├─ ICMP (ping, traceroute)
│   ├─ ARP  (IP → MAC)
│   └─ DHCP (分配 IP)
└─ Routing Algorithms
    ├─ Non-Adaptive: Shortest Path (Dijkstra), Flooding
    ├─ Adaptive: Distance Vector, Link State (OSPF)
    ├─ Hierarchical Routing
    └─ Broadcast: Multi-destination, Flooding, RPF
```

### 7.2 易考对比

| 对比 | 关键区别 |
|------|----------|
| Datagram vs VC | 每包独立 vs 同路径；路由器表大 vs 表小；易换路 vs 易全断 |
| Transparent vs Non-transparent Fragmentation | 网络内重组 vs 终点重组；IP 用后者 |
| Fragmentation vs Path MTU Discovery | 网络内分片 vs 源适配大小 |
| Classful vs CIDR | 固定 ABC 大小 vs 任意前缀长度 |
| Distance Vector vs Link State | 全局信息局部共享 vs 本地信息全局共享；慢收敛 vs 快收敛；计算量小 vs 大 |
| Flat vs Hierarchical Routing | 全图信息，路径最优 vs 分区信息，路径稍长但可扩展 |

### 7.3 关键数字记忆

- IPv4 地址长度：**32 bit**；IPv6：**128 bit**。
- IPv4 头部固定：**20 B**（IHL=5 对应 20 字节；max IHL=15 对应 60 字节）。
- IPv6 头部固定：**40 B**。
- IPv4 Total Length 最大：**65,535 字节**。
- Ethernet 最小帧：**64 字节**（与 MAC 笔记呼应）。
- ICANN：管理 IP 分配的机构。
- 私有 IP：**10/8、172.16/12、192.168/16**。

---

## 8. 快速自测问题

1. 为什么 IP 被设计成 best-effort？带来的代价是什么？由谁来弥补？
2. 什么是 store-and-forward？它对延迟有什么影响？
3. Datagram 和 Virtual Circuit 各在什么场景下更优？分别举 1 个例子。
4. 在 IPv6-over-IPv4 隧道中，外层和内层包的头部分别是什么？
5. 分片的透明和非透明方式优劣在哪？IP 为什么选非透明？
6. Path MTU Discovery 如何利用 ICMP 和 DF 标志？
7. 给定 IP `192.168.1.10` 和 mask `255.255.255.192`，网络号是多少？一共有多少可用主机？
8. CIDR 中 longest prefix match 为什么必要？举一个会用到它的例子。
9. NAT 为什么违反分层原则？它是如何让多个内网主机共享一个公网 IP 的？
10. Dijkstra 和 Distance Vector 用同样的输入，输出一样吗？二者的差别在哪里？
11. Link State 协议中 LSP 的内容是什么？何时触发新的 LSP？
12. RPF 如何用最少的判断抑制广播包的重复副本？

---

*本文档对应 Group 1（MAC & Network Layer）复习，另见 `MAC-sublayer.md` 与 `INDEX.md`。*
