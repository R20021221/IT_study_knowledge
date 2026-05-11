---
group: Transport Layer (Group 2)
topic: Transport Layer
source_pdfs:
  - Transport-Layer-1.pdf
  - Transport-Layer-2&3&4.pdf
covers:
  - 传输层在协议栈中的位置与职责
  - Transport Layer Service：连接类型（TCP/UDP）、Transport Entity 所在位置
  - Transport Layer Encapsulation：TPDU / Segment
  - Transport Service Primitives（LISTEN / CONNECT / SEND / RECEIVE / DISCONNECT）
  - Elements of Transport Protocols
  - Connection Establishment：三次握手 (Three-way Handshake)
  - Connection Release：非对称 / 对称、两军问题 (Two-Army Problem)、计时器与重传
  - Addressing：NSAP / TSAP / Port、Static / Portmapper / Mediated 分配
  - Multiplexing（多路复用）
  - Programming using Sockets：Berkeley Sockets、服务器多线程模型
  - UDP：Segment 结构、Checksum、Port Numbers、RPC（远程过程调用）
  - TCP：Service Model、Segment Header 全字段（SYN/FIN/ACK/RST/PSH/URG/CWR/ECE）
  - TCP Connection Management：三次握手建立、对称释放、FSM 状态机
  - TCP Window Management（滑动窗口）
  - TCP Timer Management：SRTT、动态计时器
  - Quality of Service (QoS)：带宽、可靠性、延迟、Jitter
  - Jitter Control：接收端缓冲、慢包优先
  - QoS 实现技术：Over-provisioning、Buffering、Traffic Shaping (Leaky Bucket)、Resource Reservation、Admission Control、Proportional Routing、Packet Scheduling
  - Congestion Control vs Flow Control
  - TCP Congestion Control：Slow Start、AIMD、TCP Tahoe、TCP Reno
  - 无线网络拥塞控制：传输错误 vs 拥塞的区分
language: 中文为主，英文术语保留
last_updated: 2026-05-11
---

# Transport Layer（传输层）

> COMP90007 Internet Technologies · Week 7-8 复习笔记
> 对应 PDF：`Transport-Layer-1.pdf`、`Transport-Layer-2&3&4.pdf`

---

## 0. 传输层在协议栈中的位置

| 层 | 负责 | 关键词 |
|----|------|--------|
| Application | HTTP、DNS 等用户应用 | 用户数据 |
| **Transport** | **端到端的可靠数据传输，进程到进程** | 段 (Segment)、端口 |
| Network | 跨网络路由 (IP) | 数据报、IP 地址 |
| Data Link | 相邻节点间传输、MAC | 帧 |
| Physical | 比特流、信号 | 传输介质 |

传输层的核心任务：

> **为应用层的进程提供高效、可靠、划算的数据传输服务，独立于物理层或数据链路层。**

---

## 1. 传输层服务 (Transport Layer Service)

### 1.1 服务模型

- 传输层位于**应用层与网络层之间**，对两层都提供接口。
- **Transport Entity（传输实体）**：实现传输层协议的软件或硬件，可位于：
  - OS kernel（操作系统内核）
  - System library（系统库，绑定到网络应用程序）
  - User process（用户进程）
  - Network interface card（网卡）

### 1.2 为什么有了网络层还需要传输层？

| 对比维度 | 网络层 (Network Layer) | 传输层 (Transport Layer) |
|----------|------------------------|--------------------------|
| 代码运行位置 | 主要在**路由器**上 | 完全在**端主机**上 |
| 用户控制权 | 用户**几乎没有**控制权 | 用户**可以通过传输层改善 QoS** |
| 功能 | 尽力而为 (best-effort) | 可在网络层上方**修复可靠性问题** |

- 关键结论：传输层让用户程序可以在**不可靠的网络层**之上获得**可靠的、面向进程的**数据传输。

### 1.3 传输层提供的服务类型

| 服务 | 代表协议 | 特点 |
|------|----------|------|
| **Connection-oriented（面向连接）** | TCP | 先建立连接，再传数据，保证可靠性 |
| **Connectionless（无连接）** | UDP | 不建立连接，不保证可靠性，但速度快 |

### 1.4 Transport Layer Encapsulation（封装）

- **TPDU (Transport Protocol Data Unit)** = **Segment（段）**：传输实体间发送的消息单元。
- 封装关系：`Segment` 封入 `Packet（数据包）`，`Packet` 再封入 `Frame（帧）`。

```
┌─────────────────────────────┐
│       Frame (链路层)         │
│  ┌───────────────────────┐  │
│  │    Packet (网络层)     │  │
│  │  ┌─────────────────┐  │  │
│  │  │ Segment (传输层) │  │  │
│  │  │ [Header|Payload] │  │  │
│  │  └─────────────────┘  │  │
│  └───────────────────────┘  │
└─────────────────────────────┘
```

---

## 2. Transport Service Primitives（传输服务原语）

面向连接服务的典型调用流程：

| Primitive（原语） | 含义 |
|-------------------|------|
| **LISTEN** | 服务器阻塞等待入站连接 |
| **CONNECT** | 客户端发起连接请求，发送 CONNECTION REQUEST 段 |
| **SEND** | 双方发送数据 |
| **RECEIVE** | 双方接收数据 |
| **DISCONNECT** | 任意一方发起断开，发送 DISCONNECT 段 |

**示例伪代码：**

```java
// 客户端
Socket A_Socket = createSocket("TCP");
connect(A_Socket, 128.255.16.0, 80);
send(A_Socket, "My first message!");
disconnect(A_Socket);
```

---

## 3. Elements of Transport Protocols（传输协议要素）

### 3.1 Connection Establishment（连接建立）

**挑战**：网络中的包可能**延迟、重复或丢失**，旧的重复包可能被误认为新连接请求。

**解决方案：Three-way Handshake（三次握手）**

- **目标**：确保即使存在延迟/重复包，也能可靠建立连接。
- **核心手段**：双方各自贡献**新鲜的序列号 (sequence number)**，不复用旧连接的序列号。

```
客户端 (Client)                      服务器 (Server)
     |                                    |
     |—— CR (seq=x) ———————————————————→ |  第1步：客户端发 Connection Request
     |                                    |
     | ←—— CR+ACK (seq=y, ack=x+1) ————— |  第2步：服务器确认并发自己的 seq
     |                                    |
     |—— ACK (ack=y+1) ————————————————→ |  第3步：客户端确认服务器的 seq
     |                                    |
     |          [连接建立成功]              |
```

**三次握手如何防止错误？**

| 错误场景 | 三次握手的处理 |
|----------|---------------|
| 延迟的重复 CR 到达 | 服务器发 CR+ACK，客户端发现 seq 号对不上，不回 ACK，连接不成立 |
| 延迟的 CR 和 DATA 同时到达 | DATA 被拒绝（ACK 号错误） |

### 3.2 Connection Release（连接释放）

#### 非对称断开 (Asymmetric Disconnection)

- **任意一方**发出 DISCONNECT → 该方向立即停止传输（双向都断）。
- **缺点**：可能造成**数据丢失**（对方还没接收完）。

#### 对称断开 (Symmetric Disconnection)

- **双方各自**发出 DISCONNECT，**每次只关闭一个方向**。
- 一方关闭后仍可接收另一方的数据（更灵活，不易丢失数据）。
- **TCP 采用对称断开**。

#### 两军问题 (Two-Army Problem)

> 没有任何协议能**完美解决**"最后一条消息是否成功送达"的歧义。

**解决策略：**

- **三次握手释放** + **有限重试 (Finite Retry)** + **计时器 (Timer)**：
  - 发出 Disconnect Request (DR) 后，等待 ACK；
  - 若超时未收到 ACK，重传 DR；
  - 超过重试上限后，**强制断开**（接受可能的数据丢失）。

```
Host 1                       Host 2
  |—— DR ———————————————————→ |
  | ←—— DR ——————————————————  |
  |—— ACK ——————————————————→ |
  | (Host 2 等待 ACK，超时后断开)|
```

### 3.3 Addressing（地址与端口）

| 概念 | 全称 | Internet 中对应 |
|------|------|-----------------|
| **NSAP** | Network Service Access Point | IP 地址 |
| **TSAP** | Transport Service Access Point | **端口号 (Port Number)** |

- **TSAP 分配方式**：

| 方式 | 说明 |
|------|------|
| **Static（静态）** | 知名服务使用固定端口（内嵌于 OS），如 HTTP=80 |
| **Portmapper（端口映射）** | 新服务向 portmapper 注册名字和 TSAP，客户端先查询 portmapper |
| **Mediated（中介，inetd）** | 进程服务器 (process server) 拦截入站连接，启动对应服务器并转接 |

### 3.4 Multiplexing（多路复用）

- 支持**多个会话复用同一条网络连接**，传输层通过**端口号**区分不同进程的数据流。

---

## 4. Programming using Sockets（Socket 编程）

### 4.1 Socket 基础

- **Socket（套接字）**：传输层的**端点 (endpoint)**，应用程序通过它访问传输层服务。
- **Berkeley Sockets** 是 Internet 应用最主流的接口。

### 4.2 面向连接 Socket（TCP）调用流程

```
服务器 (Server)                        客户端 (Client)
socket()                               socket()
bind()                                    |
listen()    ←— Connection Request —— connect()
accept()    —— Connection Established ——→  |
read()      ←————————————————— write()
write()     ————————————————→  read()
close()                                close()
```

### 4.3 多线程服务器模型 (Multi-Threading)

```java
ServerSocket serverSocket = new ServerSocket([parameters]);
while (true) {
    Socket socket = serverSocket.accept();     // 等待连接
    MultiThreadMyServer server = new MultiThreadMyServer();
    server.setSocket(socket);
    new Thread(server).start();               // 为每个连接新建线程
}
```

- 主线程**持续等待新连接**，每条连接由**独立线程**处理，互不阻塞。

---

## 5. UDP（User Datagram Protocol）

### 5.1 特点

- **无连接 (Connectionless)**：不建立连接、不维护状态。
- **不提供**：流量控制 (flow control)、拥塞控制 (congestion control)、重传 (retransmission)。
- 以上功能若需要，**由应用层自行实现**。
- **适用场景**：视频流、在线游戏、DNS——需要**高速或精确控制**的应用。

### 5.2 UDP Segment 结构

```
 0      16     32
 ┌──────┬───────┐
 │ 源端口│目的端口│  各 16 bit
 ├──────┴───────┤
 │  Length      │  包含头部+数据的总长度
 ├──────────────┤
 │  Checksum    │  校验和（覆盖头部 + 数据 + 伪头部）
 ├──────────────┤
 │   Payload    │
 └──────────────┘
```

- **头部仅 8 字节**（极简设计）。
- **Checksum 伪头部 (Pseudoheader)**：包含源/目的 IP、协议号、UDP 长度（用于校验，不传输）。

### 5.3 Port Numbers（端口号）

- 范围：**0 – 65535**，分三段：

| 类别 | 范围 | 说明 |
|------|------|------|
| **Well-Known Ports** | 0 – 1023 | 知名服务，如 HTTP=80, DNS=53 |
| **Registered Ports** | 1024 – 49151 | 注册服务 |
| **Dynamic Ports** | 49152 – 65535 | 动态/临时分配 |

参考：http://www.iana.org/assignments/port-numbers

### 5.4 UDP 的优势与劣势

| 维度 | 说明 |
|------|------|
| **优势** | 提供**多路复用/解复用**能力（进程级寻址），接口比裸 IP 更友好 |
| **劣势** | 无流控、无拥塞控制、无重传 |
| **结论** | 高速或需要精细控制的应用场景首选 |

### 5.5 RPC（Remote Procedure Call，远程过程调用）

- **思想**：让客户端像调用**本地函数**一样调用**远程主机上的函数**，基于 UDP 实现。
- **Client Stub（客户端桩）**：在客户端地址空间中**代表**远程过程，隐藏网络细节。
- **Server Stub（服务器桩）**：在服务器端接收请求、调用真正的函数并返回结果。

```
客户端                               服务器
 应用代码
   ↓
 Client Stub  ——— UDP消息 ———→  Server Stub
 (打包参数)                     (解包参数)
   ↑                                ↓
 Client Stub  ←—— UDP返回 ———  Server Stub
 (解包结果)                     (打包结果)
```

---

## 6. TCP（Transmission Control Protocol）

### 6.1 TCP Service Model（服务模型）

- **面向连接 (Connection-oriented)**：发送前必须建立连接。
- **可靠性 (Reliability)**：保证数据到达、无重复、按序。
- **全双工 (Full Duplex)**：双向同时传输。
- **点对点 (Point-to-point)**：一对发送方与接收方。
- **字节流 (Byte Stream)**：不保留消息边界（不是消息流）。
- **Socket = IP 地址 + 端口号**，连接 = `(src-ip, src-port, dst-ip, dst-port)` 四元组。

**TCP 实体工作流程：**

- **发送端 TCP 实体**：接收用户字节流 → 切割成 < 64KB 的片段 (pieces) → 封装成独立 IP 数据报发送。
- **接收端 TCP 实体**：从 IP 数据报重组字节流交付给应用。

### 6.2 TCP Segment Header（TCP 段头部）

**固定 20 字节头部**（+ 可选 Options + 数据）：

```
 0        16       32
 ┌────────┬─────────┐
 │ 源端口  │ 目的端口 │
 ├─────────────────┤
 │   Sequence Number（序列号）    │
 ├─────────────────────────────┤
 │   Acknowledgement Number（确认号）│
 ├──┬──┬──────────┬────────────┤
 │头长│保│  Flags  │ Window Size│
 ├──┴──┴──────────┴────────────┤
 │  Checksum      │ Urgent Ptr  │
 ├─────────────────────────────┤
 │         Options（可选）       │
 ├─────────────────────────────┤
 │           Data               │
 └─────────────────────────────┘
```

**各字段详解：**

| 字段 | 说明 |
|------|------|
| **Source / Destination Port** | 标识连接的本地端点，各 16 bit |
| **Sequence Number** | 本段数据第一个字节的序号，用于排序和重传 |
| **Acknowledgement Number** | 期望收到的下一个字节序号（确认已收到的数据） |
| **TCP Header Length** | 头部长度（以 32-bit 字为单位） |
| **Window Size** | 从已确认字节开始，接收方愿意接收的字节数（流量控制） |
| **Checksum** | 覆盖**头部和数据**的完整性校验 |
| **Options** | 可选扩展，如 MSS（最大段大小）、Window Scale、Timestamp、SACK |

**控制标志位 (Flags)：**

| Flag | 含义 |
|------|------|
| **ACK** | 置 1 表示 Acknowledgement Number 有效 |
| **SYN** | 建立连接；连接请求：SYN=1, ACK=0；连接回应：SYN=1, ACK=1 |
| **FIN** | 释放连接；发送方表示没有更多数据要发（但仍可接收） |
| **RST** | 强制重置连接（连接混乱或拒绝无效段） |
| **PSH** | 请求接收方**立即**将数据交付应用层，不要缓冲 |
| **URG** | 紧急数据标志，配合 Urgent Pointer 使用 |
| **ECE** | ECN 回显，告知发送方网络发生拥塞，请降速 |
| **CWR** | 拥塞窗口已减小，告知接收方发送方已响应 ECN |

**段大小限制（两个上限取其小）：**

- IP 包载荷上限：**65,515 字节**
- Ethernet 帧载荷上限：一般 **1,500 字节**

### 6.3 TCP Connection Management（连接管理）

#### 建立：三次握手

```
客户端 (Client)                       服务器 (Server)
CLOSED                                LISTEN
   |                                     |
   |—— SYN (seq=x) ———————————————————→ |   SYN_SENT → SYN_RCVD
   |                                     |
   | ←—— SYN+ACK (seq=y, ack=x+1) —————  |
   |                                     |
   |—— ACK (ack=y+1) ————————————————→  |   → ESTABLISHED
   |                                     |
ESTABLISHED                          ESTABLISHED
```

#### 释放：对称四次挥手

```
Client                               Server
  |—— FIN ——————————————————————————→ |   (Client 关闭发送方向)
  | ←—— ACK ——————————————————————————  |
  |                                     |
  | ←—— FIN ——————————————————————————  |   (Server 关闭发送方向)
  |—— ACK ——————————————————————————→ |
  |                                     |
[CLOSED]                            [CLOSED]
```

- **两个同时发起连接**的尝试 → 最终只建立**一条连接**。
- **连接释放**使用**计时器**处理丢失的 FIN 或 ACK。

#### FSM（有限状态机）

TCP 连接管理通过有限状态机描述所有可能的状态转移（CLOSED → LISTEN → SYN_RCVD / SYN_SENT → ESTABLISHED → FIN_WAIT / CLOSE_WAIT → TIME_WAIT → CLOSED）。

### 6.4 TCP Window Management（窗口管理/流量控制）

- **滑动窗口 (Sliding Window)**：
  - 发送方在计时器超时前可以连续发送多个段。
  - **接收方**在 ACK 中告知**剩余缓冲空间** (Window Size)，发送方不能超出此窗口。
- **目的**：防止发送方把接收方淹没（Flow Control）。

```
发送方视角：
[已确认 | 已发未确认 | 可发未发 | 不可发]
         ←————窗口————→
```

### 6.5 TCP Timer Management（计时器管理）

- **核心挑战**：往返时间 (RTT) 是动态变化的，重传计时器不能定死：
  - **太短**：过多不必要的重传，浪费带宽。
  - **太长**：可靠性代价过高，恢复慢。

- **解决方案：SRTT（Smoothed Round-Trip Time，平滑往返时间）**

$$
SRTT = \alpha \cdot SRTT + (1 - \alpha) \cdot RTT_{sample}
$$

- 同时追踪 SRTT 的**方差 (deviation)**，设置计时器为：

$$
Timeout = SRTT + 4 \times Deviation
$$

- 网络变化时，SRTT 和 Deviation 会自适应调整。

---

## 7. Quality of Service（QoS，服务质量）

### 7.1 四个核心指标

| 指标 | 含义 |
|------|------|
| **Bandwidth（带宽）** | 单位时间内可传输的数据量 |
| **Reliability（可靠性）** | 数据是否能准确、完整地送达 |
| **Delay（延迟）** | 数据从源到目的所花的时间 |
| **Jitter（抖动）** | 数据包到达时间的**方差/变化量** |

### 7.2 Jitter Control（抖动控制）

- **Jitter** = 包到达时间的不规律性，对视频/语音等实时应用影响极大。

**控制方法：**

- **接收端缓冲 (Buffering)**：先缓冲一段时间再播放，平滑抖动（增加延迟，降低抖动）。
- **慢包优先发送 (Shuffle Transmission)**：慢速包先发，快速包在队列中等待，保证到达顺序更均匀。

### 7.3 不同应用对 QoS 的需求

| 应用 | 带宽 | 可靠性 | 延迟 | 抖动 |
|------|------|--------|------|------|
| 文件传输 | 中 | 高 | 低 | 低 |
| 视频会议 | 高 | 中 | 高 | 高 |
| 在线游戏 | 中 | 中 | 极高 | 高 |
| 流媒体 | 高 | 中 | 低 | 高 |

（"高"表示对该指标要求严格）

### 7.4 QoS 实现技术

| 技术 | 说明 |
|------|------|
| **Over-provisioning（超额供给）** | 提供超出需求的缓冲、CPU 和带宽，简单粗暴但昂贵 |
| **Buffering（缓冲）** | 接收方缓冲后再交付，增加延迟但平滑抖动，不影响带宽 |
| **Traffic Shaping（流量整形）** | 限制发送速率与突发量，代表：**Leaky Bucket（漏桶）** |
| **Resource Reservation（资源预留）** | 提前预留带宽、缓冲、CPU |
| **Admission Control（准入控制）** | 路由器根据流量模式决定是否接受新流，拒绝或重路由 |
| **Proportional Routing（比例路由）** | 将同一目的的流量分散到多条路径上 |
| **Packet Scheduling（数据包调度）** | 按优先级创建队列（公平队列 Fair Queueing、加权公平队列 WFQ） |

#### Leaky Bucket（漏桶算法）

```
突发输入流量           漏桶（容量 B）           平稳输出（速率 R）
→→→→→→→→→→  →  [BUFFER]  →  ─────────────────→
(bursty)         (capacity B)    (constant rate R)
```

- 无论输入多么突发，**输出始终以恒定速率 R** 发送。
- 桶满时多余数据**丢弃**。
- 在**发送端**实施，平滑流量曲线。

---

## 8. Congestion Control（拥塞控制）

### 8.1 Flow Control vs Congestion Control

| 维度 | Flow Control（流量控制） | Congestion Control（拥塞控制） |
|------|--------------------------|--------------------------------|
| 问题范围 | **点对点**，防止发送方淹没**接收方** | **全局**，防止网络**子网**因流量过载而崩溃 |
| 关注对象 | 接收方缓冲区 | 网络整体承载能力 |
| 体现 | TCP Window Size | 拥塞窗口 (Congestion Window) |

### 8.2 拥塞的危害

- 流量 > 网络承载能力 → 丢包 → 重传 → 流量更多 → 更多丢包（恶性循环）。
- **Goodput（有效吞吐量）** 在拥塞后急剧下降（重传包占用大量带宽）。

### 8.3 拥塞控制方法

| 方法 | 说明 |
|------|------|
| **Provisioning（提升资源）** | 升级链路和路由器，添加资源 |
| **Traffic-aware Routing** | 根据流量动态调整路由 |
| **Admission Control** | 拥塞时拒绝新虚电路 |
| **Load Shedding（丢包）** | 其他方法失败时主动丢包；应用可标记优先级以保护关键包 |

### 8.4 TCP Congestion Control（TCP 拥塞控制）

**两个窗口共同约束发送速率：**

| 窗口 | 含义 | 受限于 |
|------|------|--------|
| **Flow Control Window** | 接收方缓冲区大小 | 接收方 |
| **Congestion Window** | 网络中在途字节数的上限 | 网络 |

$$
\text{实际发送速率} = \min(\text{Flow Control Window},\ \text{Congestion Window})
$$

**拥塞信号**：丢包（通过**重复 ACK** 检测到）→ 假定丢包由拥塞引起。

### 8.5 Slow Start（慢启动）

**目的**：连接建立初期，**指数增长**探测网络容量。

```
步骤：
1. 初始化 Congestion Window = 1 segment
2. 每收到一个 ACK，窗口 +1 segment
   → 实际上每轮 (RTT) 窗口翻倍（指数增长）
3. 直到：
   a. 超时（丢包）→ 慢启动阈值减半，重置窗口，重启
   b. 到达接收方 Window 上限
   c. 超过 ssthresh（慢启动阈值）→ 切换为加法增大
```

```
窗口大小
  ↑
  │                    ╱
  │               ╱╲ ╱  （遇到阈值后线性增长）
  │           ╱         
  │       ╱  （指数增长阶段）
  │   ╱
  └─────────────────→ 时间
      Slow Start    AI
```

### 8.6 AIMD（Additive Increase Multiplicative Decrease）

| 阶段 | 操作 |
|------|------|
| **Additive Increase（加法增大）** | 每 RTT 窗口 **+1 segment**（线性增长，探测剩余容量） |
| **Multiplicative Decrease（乘法减小）** | 检测到丢包 → 窗口**减半** |

### 8.7 TCP Tahoe vs TCP Reno

| 维度 | TCP Tahoe | TCP Reno |
|------|-----------|----------|
| **基础** | 慢启动 + 加法增大 | Tahoe + 快速恢复 (Fast Recovery) |
| **丢包检测** | 重复 ACK 或超时 | 重复 ACK 或超时 |
| **收到重复 ACK** | 阈值减半，**重新慢启动** | 阈值减半，**快速恢复**（窗口减半，不重新慢启动） |
| **超时** | 阈值减半，重新慢启动 | 阈值减半，重新慢启动 |
| **恢复速度** | 慢（重新从 1 开始） | 快（从阈值开始线性增长） |

#### TCP Tahoe 状态图

```
连接建立 → ssthresh = 接收方窗口
              ↓
         慢启动（指数增长）
              ↓ 达到 ssthresh
         加法增大（线性增长）
              ↓ 丢包
         ssthresh = 当前窗口 / 2，窗口 = 1，重启慢启动
```

#### TCP Reno 快速恢复

```
收到 3 个重复 ACK（Triple Duplicate ACK）：
  ssthresh = 当前窗口 / 2
  窗口 = ssthresh（不回到 1！）
  继续线性增长（Fast Recovery）
```

### 8.8 无线网络的拥塞控制

- **挑战**：无线链路的信号质量不稳定（信噪比变化、Wi-Fi vs 卫星延迟不同）。
- **问题**：TCP 将**传输错误 (bit error)** 误认为**拥塞**，触发不必要的窗口缩减。
- **解决方向**：**Masking Strategy（掩蔽策略）**——区分传输错误和真正的拥塞，避免误判。

---

## 9. 章节总结速查

### 9.1 大图

```
Transport Layer
├─ 服务
│   ├─ Connection-Oriented (TCP)
│   └─ Connectionless (UDP)
├─ 协议要素
│   ├─ Connection Establishment: Three-way Handshake
│   ├─ Connection Release: Symmetric / Asymmetric + 两军问题
│   ├─ Addressing: TSAP / Port（静态 / portmapper / inetd）
│   └─ Multiplexing
├─ Socket 编程
│   └─ Berkeley Socket: socket→bind→listen→accept→read/write→close
├─ UDP
│   ├─ 无连接，8字节头部
│   ├─ Port Numbers (0-65535)
│   └─ RPC（Client Stub / Server Stub）
├─ TCP
│   ├─ 20字节固定头部，全字段标志（SYN/FIN/ACK/RST/PSH/URG/ECE/CWR）
│   ├─ Connection Management: 三次握手 + 四次挥手 + FSM
│   ├─ Window Management (Sliding Window / Flow Control)
│   └─ Timer Management (SRTT)
└─ QoS & 拥塞控制
    ├─ 四指标: Bandwidth / Reliability / Delay / Jitter
    ├─ Jitter Control: Buffering / Shuffle Transmission
    ├─ QoS 技术: Over-provisioning / Buffering / Leaky Bucket / 资源预留 / 准入控制 / WFQ
    ├─ Flow Control vs Congestion Control
    ├─ Slow Start + AIMD
    ├─ TCP Tahoe: 慢启动 + AIMD，丢包重启慢启动
    └─ TCP Reno: Tahoe + 快速恢复，减少不必要的慢启动
```

### 9.2 常考对比表

| 对比 | 关键区别 |
|------|----------|
| **TCP vs UDP** | 可靠面向连接 vs 不可靠无连接；头部 20B vs 8B；有拥塞控制 vs 无 |
| **三次握手 vs 两次** | 三次确保双方各自序列号都被对方确认，防止旧包误认为新连接 |
| **Symmetric vs Asymmetric Release** | 对称逐方向关闭不丢数据 vs 非对称双向立即关闭可能丢数据 |
| **Flow Control vs Congestion Control** | 点对点接收方保护 vs 全局子网保护 |
| **TCP Tahoe vs Reno** | 丢包后重启慢启动 vs 快速恢复，Reno 恢复更快 |
| **Slow Start vs Additive Increase** | 指数增长探测容量 vs 线性增长避免过载 |

### 9.3 关键数字记忆

- UDP 头部：**8 字节**
- TCP 头部固定部分：**20 字节**
- Port 范围：**0 – 65535**，Well-Known：**0 – 1023**
- TCP 段受以太网限制：一般 **≤ 1,500 字节**
- TCP 段受 IP 限制：**≤ 65,515 字节**
- 慢启动触发条件：连接建立初期 / 超时丢包后
- Tahoe 丢包后窗口归 **1**；Reno 三重复 ACK 后窗口归 **ssthresh**

---

## 10. 快速自测问题

1. 为什么有了网络层还需要传输层？传输层代码运行在哪里，网络层代码运行在哪里？
2. 三次握手如何防止延迟的重复 CR 引起错误的新连接？
3. 对称断开和非对称断开的区别是什么？TCP 用哪种？
4. 两军问题说明了什么？TCP 连接释放如何在实际中"够好地"解决它？
5. TSAP 的三种分配方式（Static / Portmapper / inetd）各适合什么场景？
6. UDP 的 Checksum 包含哪些内容？为什么要用伪头部？
7. TCP 的 SYN、FIN、ACK、RST、PSH 各在什么情况下置 1？
8. TCP 四次挥手为什么需要四次，而不是三次？（提示：FIN 和 ACK 不能合并的原因）
9. 什么是 Jitter？为什么对视频会议影响比对文件传输大得多？
10. Leaky Bucket 和令牌桶的核心区别是什么（提示：突发处理方式）？
11. TCP Tahoe 和 Reno 在收到三个重复 ACK 时的行为有何不同？
12. 无线网络中为什么不能直接用丢包作为拥塞信号？

---

*本文档对应 Group 2（Transport Layer）复习，另见 `MAC-sublayer.md`、`Network-layer.md` 与 `INDEX.md`。*
