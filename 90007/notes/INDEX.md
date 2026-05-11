# COMP90007 复习笔记总索引

本文件登记所有 md 笔记，**每次新增笔记必须同步更新此索引**，避免为同一知识点重复创建多个 md 文件。

> 命名惯例：按**学习组 (group)** 组织，每组包含若干 md。新增前请先检查下方清单中的 `covers` 列表，判断是否已有现成文件可补充，再决定是否新建。

---

## Group 1 — MAC & Network Layer（已完成 · 2026-04-21）

**学习范围**：MAC 子层（两份 PDF）+ Network 层（四份 PDF），共 6 份 PDF 合并为 2 份 md。

### [MAC-sublayer.md](./MAC-sublayer.md)

- **源 PDF**：`Week4-MAC-sublayer-1.pdf`、`Week5-MAC-sublayer-2.pdf`
- **Covers**：
  - Medium Access Control 的作用与背景
  - 静态信道分配（TDM、FDM）
  - 动态信道分配的假设条件（单信道、独立站点、冲突、时间模型、载波监听）
  - 多路访问协议分类（Contention / Collision-Free / Limited Contention / Wireless）
  - ALOHA、Slotted ALOHA
  - CSMA（1-persistent / non-persistent / p-persistent）
  - CSMA/CD（带冲突检测）
  - Bit Map Protocol、Binary Countdown（无冲突）
  - Adaptive Tree Walk（有限竞争）
  - 无线 LAN：隐藏/暴露终端、MACA（RTS/CTS）
  - Ethernet 案例：经典以太网、帧格式、MAC 地址、最小帧长、信道效率公式、交换式以太网
  - BEB（二进制指数退避）

### [Network-layer.md](./Network-layer.md)

- **源 PDF**：`Week5-network-layer-1.pdf`、`Network-Layer-2-1.pdf`、`Network-Layer-3.pdf`、`Network-Layer-4.pdf`
- **Covers**：
  - 网络层在 Internet 中的位置与职责
  - Store-and-Forward 分组交换
  - 服务类型：Connectionless (Datagram) vs Connection-Oriented (Virtual Circuit) 全面对比
  - Internetworking：动机与挑战
  - Tunneling（IPv6 over IPv4 为例）
  - Fragmentation（透明 vs 非透明，IP 式分片）
  - Path MTU Discovery
  - IPv4 Datagram 结构（各字段含义：IHL、Total Length、Identification、DF、MF、Fragment Offset、TTL、Protocol、Checksum、Source/Destination、Options）
  - IP 地址、点分十进制、层次结构、前缀
  - Subnet Mask 与按位 AND 提取网络部分
  - 路由表三元组结构
  - Subnetting 子网划分
  - CIDR（Classless Inter-Domain Routing）与最长前缀匹配
  - Classful Addressing（A/B/C/D/E 类）
  - 私有 IP 网段
  - NAT（Network Address Translation）原理与利弊
  - IPv6 基础（128 bit、40 B 头部、过渡机制）
  - 控制协议：ICMP（ping/traceroute）、ARP（IP→MAC）、DHCP（自动分配 IP）
  - 路由基础：Optimality Principle、Sink Tree
  - 路由算法分类：Non-Adaptive vs Adaptive、Hierarchical、Broadcast
  - Shortest Path Routing + Dijkstra 算法（含详细示例）
  - Flooding、Selective Flooding
  - Distance Vector Routing（含 Count-to-Infinity 问题）
  - Link State Routing（OSPF 示例、LSP 构造与触发时机）
  - Hierarchical Routing
  - Broadcast Routing：Multi-destination、Flooding、Reverse Path Forwarding (RPF)

---

## Group 2 — Transport Layer（已完成 · 2026-05-11）

**学习范围**：传输层（两份 PDF），共 2 份 PDF 合并为 1 份 md。

### [Transport-layer.md](./Transport-layer.md)

- **源 PDF**：`Transport-Layer-1.pdf`、`Transport-Layer-2&3&4.pdf`
- **Covers**：
  - 传输层在协议栈中的位置与职责（与网络层的分工、Transport Entity 位置）
  - Transport Layer Encapsulation：TPDU / Segment 的封装关系
  - Transport Service Primitives：LISTEN / CONNECT / SEND / RECEIVE / DISCONNECT
  - Connection Establishment：三次握手 (Three-way Handshake)，防延迟/重复包
  - Connection Release：非对称 vs 对称断开，两军问题 (Two-Army Problem)，计时器与有限重试
  - Addressing：NSAP / TSAP / Port，三种分配方式（Static / Portmapper / inetd）
  - Multiplexing（多路复用）
  - Programming using Sockets：Berkeley Socket 流程（socket→bind→listen→accept→read/write→close），多线程服务器
  - UDP：无连接特点、8 字节 Segment 结构、Checksum 伪头部、端口号三段分类（Well-Known / Registered / Dynamic）、RPC（Client Stub / Server Stub）
  - TCP：面向连接全双工字节流、20 字节固定头部全字段（Sequence / Acknowledgement / Flags / Window Size / Checksum / Options）
  - TCP 控制标志：SYN / FIN / ACK / RST / PSH / URG / ECE / CWR 的含义与触发场景
  - TCP Connection Management：三次握手建立、对称四次挥手释放、FSM 状态机
  - TCP Window Management：滑动窗口 (Sliding Window)，流量控制机制
  - TCP Timer Management：SRTT（平滑往返时间）动态计时器公式
  - QoS 四指标：Bandwidth / Reliability / Delay / Jitter
  - Jitter Control：接收端缓冲、慢包优先发送
  - QoS 实现技术：Over-provisioning / Buffering / Traffic Shaping (Leaky Bucket) / Resource Reservation / Admission Control / Proportional Routing / Packet Scheduling (WFQ)
  - Flow Control vs Congestion Control 对比
  - TCP Congestion Control：Slow Start（慢启动，指数增长）、AIMD（加法增大乘法减小）
  - TCP Tahoe：慢启动 + AIMD，丢包后重启慢启动
  - TCP Reno：Tahoe + Fast Recovery（三重复 ACK 后快速恢复，不重启慢启动）
  - 无线网络拥塞控制：传输错误 vs 拥塞的区分问题 (Masking Strategy)

---

## 待学习 / 计划中的分组

> 后续学习新一组时在此新增条目；每组处理完后移到上方"已完成"部分。

- Group 3：*（待定，等待用户指定下一组 PDF）*

---

## 使用约定

1. **一组 PDF → 一组 md 文件**，不机械"一 PDF 一 md"。
2. 每个 md 顶部必须包含 frontmatter：`group / topic / source_pdfs / covers / language / last_updated`。
3. 新增前先查本 INDEX 的 `covers`，若已涵盖某知识点，优先**更新既有文件**而非新建。
4. 所有笔记默认放在 `notes/` 子文件夹下，与源 PDF 分离。
5. 笔记语言：**中文为主，英文术语保留**（便于与 PDF 原文、考试题对照）。
