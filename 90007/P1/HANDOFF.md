# COMP90007 Project 1 — 工作进度 & 交接文档

> 最后更新：2026-04-24 (Fri)  
> Deadline：2026-04-27 (Mon)  
> 剩余：3 天，主力工作 = Report 写作

---

## 0. 一句话现状

**所有数据采集、Excel 建表、图表都完成了**，只剩 **Word/PDF 报告写作** 一件事。报告约 8–9 页正文 + 封面 + 附录，按 spec 要求 A4 / 10pt / 1.5 行距 / 单栏 / ≤12 页正文。

---

## 1. 已完成工作清单

### 数据采集

| Section | 内容 | 方法 | 原始文件位置 |
|---|---|---|---|
| **S2.1** | 1 次 traceroute (目标 UniMelb ?) Wireshark 抓包 | Wireshark + traceroute | `P1/S2/S2.1/1.pcapng` + `1.png` |
| **S2.2** | 10 个 iperf servers 的跳数 + 地理距离 | `traceroute -I <host>` × 10 | `P1/S2/S2.2/*.png`（每个 host 一张跳数截图，配合 geolocation） |
| **S3.1** | 1 次 ping Wireshark 抓包 | Wireshark + ping | `P1/S3/3.1/1.pcapng` + `.png` |
| **S3.2** | 10 hosts × 3 次 ping（每次 `-c 10`） | `ping -c 10 <host>` | `P1/S3/3.2/*.png`（30 张 ping 结果） |
| **S4.1** | 10 hosts × 3 次 iperf（单次测带宽） | `iperf -c <host> -p 80`（Host 10 用 `iperf3 -c iperf.he.net -R` 反向） | `P1/S4/4.1/*.png`（30 张 iperf 结果） |
| **S4.2** | Furthest (porto) + Closest (masnet) × 4 时段 | `iperf -c <host> -p 80` + `sudo traceroute -I <host>` | `P1/S4/4.2/furthest_porto/{morning,afternoon,evening,night}/*.png` + `P1/S4/4.2/closest_masnet/.../*.png`（共 16 张，命名：`<slot>_{further,cloest}_{iperf,traceroute}.png`） |

**关键命名约定**：S4.2 截图保留 "cloest" 拼写（之前已改好，不要动）。

### Excel 交付物

| 文件 | 内容 | 公式数 | 图表 |
|---|---|---|---|
| `P1/S2/Section2.2_Analysis.xlsx` | 10 hosts 的跳数 + 距离 + 相关性 | ~ | ScatterPlot + ScatterPlot_byContinent (PNG 已导出) |
| `P1/S3/3.2/Section3.2_PingData.xlsx` | 30 行 ping 数据（Packets 表）+ RunSummary | — | AvgRTT_vs_Distance.png + Jitter_vs_Distance.png |
| `P1/S4/4.1/Section4.1_Bandwidth.xlsx` | 30 行 iperf (Runs 表) + HostSummary + 散点图带线性 trendline | 40 | Scatter + linear trendline (disp equation + R²) |
| `P1/S4/4.2/Section4.2_TimeSlots.xlsx` | TimeSlots 表（8 行，2 host × 4 slot） + Summary 表（列式布局 Slot/Portugal/Ecuador，含 mean/SD/min/max/range） + LineChart | 10 | LineChart：porto 红色平顶、masnet 蓝色 U 型 |
| `P1/S4/4.3/Section4.3_BDP.xlsx` | 10 hosts 的 BDP = BW × RTT（kilobits 与 KBytes 两种单位）+ Average 行 | 25 | Bar chart 线性 + bar chart 对数刻度（因为最大 / 最小差 6 倍） |

所有 xlsx 文件：**0 公式错误**（都跑过 recalc.py 验证）。

### 已改名的 S4.2 截图（16 张）

```
furthest_porto/morning/   morning_further_iperf.png   morning_further_traceroute.png
furthest_porto/afternoon/ afternoon_further_iperf.png afternoon_further_traceroute.png
furthest_porto/evening/   evening_further_iperf.png   evening_further_traceroute.png
furthest_porto/night/     night_further_iperf.png     night_further_traceroute.png
closest_masnet/morning/   morning_cloest_iperf.png    morning_cloest_traceroute.png
closest_masnet/afternoon/ afternoon_cloest_iperf.png  afternoon_cloest_traceroute.png
closest_masnet/evening/   evening_cloest_iperf.png    evening_cloest_traceroute.png
closest_masnet/night/     night_cloest_iperf.png      night_cloest_traceroute.png
```

---

## 2. 待完成工作（剩余全部）

### Task A · Report 正文（.docx → PDF）

建议按以下顺序写，从内容最丰富的段落（S4.2）切入容易找感觉：

| 段 | 内容要点 | 预计篇幅 |
|---|---|---|
| **封面** | Name、Student ID、login username（spec 要求 cover page） | 1 页 |
| **2.1** | Wireshark 观察 traceroute：UDP 包目的端口逐跳递增、TTL 递增、ICMP Time Exceeded 回包、最后一跳 ICMP Port Unreachable | ~0.5 页 |
| **2.2** | 10 hosts 跳数 vs 距离，附散点 + 相关性系数；说明跳数不严格随距离单调（路由选择 + 骨干网拓扑） | ~1 页 |
| **3.1** | Wireshark 观察 ping：ICMP Echo Request / Reply、identifier/sequence 字段配对、TTL 往返 | ~0.5 页 |
| **3.2** | 30 次 ping 的 min/avg/max/mdev 表；RTT–distance 散点、Jitter–distance 散点；sample SD 公式 (B 形式)：σ = √[Σ(xi−x̄)² / (N−1)] | ~1 页 |
| **3.3** | 相关性分析 + 网络环境讨论（Wi-Fi / 本地负载 / 时段） | ~1 页 |
| **4.1** | 定义 Bandwidth / Throughput / Goodput 区别（注：iperf3 打印 "Bitrate" 但在应用层仍作 bandwidth 理解，因为是单连接 TCP 尽力跑）；30 次 iperf 的散点 + 趋势线 + R² | ~1 页 |
| **4.2** | **最重内容段**。四时段数据（porto: 13.4–13.8 Mbps SD 0.19；masnet: 20.6–24.4 Mbps SD 1.75）。两大论点：(1) porto 被 ISP / 服务端口限速卡在 ~13.8，时段几乎无关；(2) masnet 呈 U 型，晚餐时段（AEST 18:09 = Ecuador 03:00）反而最快 24.4 Mbps，夜间 hop 24 VoIP 抖动 ~413 ms 拖慢尾跳。用 SD 对比（9×）量化稳定性差异。 | ~1.5 页 |
| **4.3** | BDP = BW × RTT 分析。最高 Host 2 spd-desrv.hostkey.com = 3347 KB（高 BW × 高 RTT）；最低 Host 7 porto = 535 KB（被 ISP 带宽限死）。反直觉：Host 9 dal.clouvider RTT 最小（193 ms）但 BDP 只排第 5，说明"BDP 小不等于网络差" — BDP 是理论上 in-flight bytes 的上限。 | ~1 页 |
| **4.4** | 两个 Challenge：① Host 10 iperf.he.net 默认（发送端模式）会被服务器拒绝，需要 `-R` 反向模式；② masnet 夜间 traceroute hop 24 抖动激烈（524/327/388 ms），是 VoIP 网关拥塞特征。 | ~0.5 页 |

**格式 spec**（务必遵守）：
- A4 纸张
- 10pt 正文字号
- 1.5 倍行距
- 单栏
- 正文 ≤ 12 页（封面 + appendix + references 不计入 12 页）
- IEEE 引用风格（数字式 `[1]`、`[2]`，**按首次出现的顺序**排序，不是字母顺序）

### Task B · References 列表

至少要引用的：
- RFC 791 (IP) / RFC 792 (ICMP) / RFC 1349 (TOS) —— 3.1 Wireshark 观察 Echo Request/Reply 时用
- Stevens《TCP/IP Illustrated》Vol. 1 或 Kurose & Ross《Computer Networking》—— 普适引用，讲 ping/traceroute 原理时用
- iperf2 vs iperf3 差异 —— 引 iperf 官方 docs，解释为什么 Host 10 要 `-R`
- BDP 定义 —— 引 RFC 1323 或教材
- Haversine 距离公式 —— 引 Sinnott 1984 或 movable-type.co.uk 的常用实现

### Task C · Appendix

- 所有原始截图合集（建议用表格形式：`Figure A.1`, `Figure A.2` 顺序）
- 保留命令原文、终端输出关键区段（不要全部贴，挑代表性 4–6 张 + 一段命令清单）

### Task D · PDF 导出

- 从 Word 导出为 PDF 前，确认：目录、分页、图表清晰、代码字体对齐
- 文件命名建议：`COMP90007_Project1_<StudentID>.pdf`

---

## 3. 关键数据速查（写报告时不用再翻 Excel）

### S4.1 带宽均值（10 hosts，Mbps）

| # | Host | Mean BW |
|---|---|---|
| 1 | speedtest.awbb.net | 61.4 |
| 2 | spd-desrv.hostkey.com | 85.1 |
| 3 | speedtest.wobcom.de | 75.6 |
| 4 | rychlost.poda.cz | 38.8 |
| 5 | spd-fisrv.hostkey.com | 57.9 |
| 6 | speed.itgate.net | 33.3 |
| 7 | porto.speedtest.net.zon.pt | 13.8 |
| 8 | speedtest.masnet.ec | 14.2 |
| 9 | dal.speedtest.clouvider.net | 98.0 |
| 10 | iperf.he.net | 26.5 |

### S4.2 四时段（Mbps）

| Host | Morning | Afternoon | Evening | Night | Mean | SD |
|---|---|---|---|---|---|---|
| Portugal (Furthest) | 13.8 | 13.4 | 13.8 | 13.6 | **13.65** | 0.19 |
| Ecuador (Closest) | 20.6 | 22.2 | 24.4 | 20.8 | **22.00** | 1.75 |

### S4.3 BDP (KBytes，排序)

| 排名 | Host | BDP |
|---|---|---|
| 1 (最高) | spd-desrv.hostkey.com | 3347 |
| 2 | speedtest.wobcom.de | 2991 |
| 3 | speedtest.awbb.net | 2767 |
| 4 | spd-fisrv.hostkey.com | 2425 |
| 5 | dal.clouvider | 2308 |
| 6 | rychlost.poda.cz | 1710 |
| 7 | speed.itgate.net | 1407 |
| 8 | iperf.he.net | 1077 |
| 9 | masnet | 615 |
| 10 (最低) | porto | 535 |

---

## 4. 重点异常（写 4.4 Challenges 和 3.3/4.1 分析时用）

1. **Host 10 iperf.he.net 默认模式被拒** → 加 `-R` 反向模式成功。这是 iperf3 服务端策略问题，不是网络问题。
2. **S4.1 Host 4 run3 / Host 6 run1 数值异常** → 归类为正常波动（Wi-Fi / 本地干扰），已采纳不单独处理。
3. **S4.2 masnet 夜间 hop 24 抖动尖峰 524/327/388 ms** → VoIP 网关拥塞特征；尾跳 RTT 从白天 ~326 ms 飙到 413 ms；但 iperf 带宽（20.8 Mbps）没崩，说明 TCP 窗口扛住了抖动。
4. **porto ISP 端口限速假说** → 四时段波动 SD 0.19 Mbps，基本就是量化噪声；说明瓶颈不在路径拥塞，而在终端链路（家宽/服务端口上限 ~13.8 Mbps）。

---

## 5. 明天接手时的建议工作流

1. **先读 `P1/COMP90007_2026S1_Project1.pdf` 中 Section 5 submission requirements**，再次对齐格式要求。
2. **从 S4.2 / S4.3 分析段开写**（素材最丰富、AI 协作效率最高），然后往前铺 S2/S3。
3. **引用表边写边记**（比一次性到最后拼凑省一小时）。
4. **Appendix 留到最后**，只放精选图，不要无脑堆。
5. **deadline 前 6 小时**：内部通读一遍，检查图片编号、表格编号、页码、引用数字是否全部闭合。

---

## 6. 环境 / 设备信息（新电脑接手需要）

- 所有文件都在 GitHub 仓库里：`IT_study_knowledge/90007/P1/...`（如果 push 了的话），否则需要通过 iCloud / U 盘同步。
- 报告撰写工具：Word / Pages / Google Docs 皆可，最后导出 PDF。
- 如果新电脑没有 LibreOffice，`recalc.py` 跑不了 — 但 Excel 公式已经全部算好值存盘，直接打开就能看到数字。
- 无需重跑任何 iperf / ping / traceroute —— 数据已全部采集完毕。

---

## 7. 文件一览（绝对路径）

```
P1/
├── COMP90007_2026S1_Project1.pdf          ← spec
├── HANDOFF.md                             ← 本文件
├── S2/
│   ├── S2.1/1.pcapng, 1.png
│   ├── S2.2/*.png                         (10 张 traceroute 截图)
│   ├── Section2.2_Analysis.xlsx
│   ├── Section2.2_ScatterPlot.png
│   ├── Section2.2_ScatterPlot_byContinent.png
│   ├── Distance_Calculation_Method.md
│   └── distance_calculation.py
├── S3/
│   ├── 3.1/1.pcapng, *.png
│   └── 3.2/
│       ├── Section3.2_PingData.xlsx
│       ├── Section3.2_AvgRTT_vs_Distance.png
│       └── Section3.2_Jitter_vs_Distance.png
└── S4/
    ├── iperf_servers.md                   (10 服务器列表 + 坐标)
    ├── probe_commands.sh                  (参考命令集)
    ├── 4.1/
    │   ├── Section4.1_Bandwidth.xlsx
    │   └── *.png                          (30 张 iperf 结果)
    ├── 4.2/
    │   ├── run_checklist.md
    │   ├── Section4.2_TimeSlots.xlsx
    │   ├── furthest_porto/{morning,afternoon,evening,night}/*.png
    │   └── closest_masnet/{morning,afternoon,evening,night}/*.png
    └── 4.3/
        └── Section4.3_BDP.xlsx
```
