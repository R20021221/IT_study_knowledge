# S4 · iperf server probe sheet

> 在每个 host 的 **working command** 栏里，填入你 probe 后第一个能稳定连上 10 秒的命令。
> 找到工作命令后，在该 host 的文件夹 `4.1/<n>/run{1,2,3}/` 下各跑一次，截图保存。
> spec 原则：如果 iperf2 和 iperf3 都能用，**优先选 iperf2**。

---

## 0. 前置安装（只做一次）

```bash
brew install iperf iperf3
iperf -v          # 应该是 iperf 2.x
iperf3 -v         # 应该是 iperf 3.x
```

---

## 1. Probe 流程（每个 host 依次试，能连且 10 秒不断开就停）

每次探测 `-t 5` 让它只跑 5 秒（省时）。看到 `connected` + 有 `Mbits/sec` 输出就算成功。

```bash
# Step A 默认 iperf2（端口 5001）
iperf -c <HOST> -t 5

# Step B 默认 iperf3（端口 5201）—— 如果 A 失败
iperf3 -c <HOST> -t 5

# Step C 指定端口（如果 A 和 B 都失败，试这些常见端口）
iperf3 -c <HOST> -t 5 -p 5002
iperf3 -c <HOST> -t 5 -p 5200
iperf3 -c <HOST> -t 5 -p 80
iperf  -c <HOST> -t 5 -p 5002
```

**注意**：
- Clouvider（host 9）通常用 **5200–5209** 端口范围中的任意一个
- HostKey（host 2 和 5）通常是 iperf3 默认 5201
- 如果某 host **完全连不上**，去 https://iperf.fr/iperf-servers.php 挑一个同国/同洲备选，**记录替换原因**

---

## 2. 10 个 Host 的 working command 填空表

| # | Host | 国家/地区 | iperf version | Port | Working command |
|---|---|---|---|---|---|
| 1 | speedtest.awbb.net | USA (TX) |  |  |  |
| 2 | spd-desrv.hostkey.com | Germany |  |  |  |
| 3 | speedtest.wobcom.de | Austria/Germany |  |  |  |
| 4 | rychlost.poda.cz | Czech Republic |  |  |  |
| 5 | spd-fisrv.hostkey.com | Finland |  |  |  |
| 6 | speed.itgate.net | Italy |  |  |  |
| 7 | porto.speedtest.net.zon.pt | Portugal (Lisbon) |  |  |  |
| 8 | speedtest.masnet.ec | Ecuador |  |  |  |
| 9 | dal.speedtest.clouvider.net | USA (Dallas) |  |  |  |
| 10 | iperf.he.net | USA (Fremont) |  |  |  |

---

## 3. 4.1 正式测量（探测完后，跑 3 次每 host，10 秒/次，默认参数）

```bash
# 模板：
<working cmd with default -t 10>

# 例（假设 host 1 是 iperf3 默认）：
iperf3 -c speedtest.awbb.net
# 截图 → 存到 /P1/S4/4.1/1/run1/
# 等 ~30 秒
iperf3 -c speedtest.awbb.net
# 截图 → 存到 /P1/S4/4.1/1/run2/
iperf3 -c speedtest.awbb.net
# 截图 → 存到 /P1/S4/4.1/1/run3/
```

每次运行之间至少间隔 **30 秒**，让 TCP 窗口和服务器侧的连接池重置。

---

## 4. 应急替换记录（如有）

| 原 host | 替换为 | 原因 | 日期 |
|---|---|---|---|
|  |  |  |  |

---

## 5. 4.2 准备（明天 4/23 用）

确认目标：
- **Furthest**: porto.speedtest.net.zon.pt （17,761 km）→ 明天用表 #7 的 working command
- **Closest**: speedtest.masnet.ec （13,621 km）→ 明天用表 #8 的 working command
