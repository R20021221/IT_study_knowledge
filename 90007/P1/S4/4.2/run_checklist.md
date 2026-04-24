# S4.2 · 四时段测量 checklist

> Spec: 4.2 — Pick furthest & closest from Table 1, run iperf across 4 time slots.
> 每个时段 ≈ 2–3 分钟总耗时。

## 两个 host

| 角色 | Hostname | 距离 (km) | 备注 |
|---|---|---|---|
| **Furthest** | `porto.speedtest.net.zon.pt` | 17,761 | S4.1 三次稳定在 ~13.8 Mbps |
| **Closest** | `speedtest.masnet.ec` | 13,621 | S4.1 三次 11.4 / 18.7 / 12.6 Mbps |

---

## 四个时段命令（每个 slot 都是这 4 条，按顺序跑）

每个时段打开终端，**按下列顺序复制粘贴**。看到结果后 ⌘+shift+4 截图两个终端窗口（或整窗口）。

```bash
# ---------- Furthest: porto.speedtest.net.zon.pt ----------
iperf -c porto.speedtest.net.zon.pt -p 80

traceroute porto.speedtest.net.zon.pt

# ---------- Closest: speedtest.masnet.ec ----------
iperf -c speedtest.masnet.ec -p 80

traceroute speedtest.masnet.ec
```

截图存放位置（每个 slot 4 张图，可以合并到一两张）：

- `furthest_porto/<slot>/` ← iperf + traceroute 的 porto 截图
- `closest_masnet/<slot>/` ← iperf + traceroute 的 masnet 截图

---

## Checklist

### Morning (09:00–10:00 AEST)

- [ ] porto iperf
- [ ] porto traceroute
- [ ] masnet iperf
- [ ] masnet traceroute
- [ ] 截图存进 `furthest_porto/morning/` 和 `closest_masnet/morning/`

### Afternoon (13:00–14:00 AEST)

- [ ] porto iperf
- [ ] porto traceroute
- [ ] masnet iperf
- [ ] masnet traceroute
- [ ] 截图存进 `furthest_porto/afternoon/` 和 `closest_masnet/afternoon/`

### Evening (18:00–19:00 AEST)

- [ ] porto iperf
- [ ] porto traceroute
- [ ] masnet iperf
- [ ] masnet traceroute
- [ ] 截图存进 `furthest_porto/evening/` 和 `closest_masnet/evening/`

### Night (23:00–24:00 AEST)

- [ ] porto iperf
- [ ] porto traceroute
- [ ] masnet iperf
- [ ] masnet traceroute
- [ ] 截图存进 `furthest_porto/night/` 和 `closest_masnet/night/`

---

## 注意事项

1. **命令要一字不差**：iperf2、端口 80（和 S4.1 保持一致），这样跨时段可对比。
2. **每张截图要包含命令本身**：截图时把 `iperf -c ...` 这一行也带上，marker 要看到你跑的是什么。
3. **不需要跑 3 次**：spec 原文是 "run **the** iperf command"（单数），每个 slot 跑一次就够。
4. **traceroute 如果卡在 `* * *` 很久**：至少等到能看到**接近目的地**的几跳再停（⌃C），不需要等到最后一跳——中间 hop 的地理分布才是 4.2 讨论的重点。
5. **网络环境保持一致**：四个时段尽量在同一个 Wi-Fi / 同一台设备 / 同样的其他应用使用情况下跑，不然带宽变化就不知道是时段因素还是环境因素了。
6. **万一某个 host 某个时段连不上**：iperf 报错截图留着，作为 4.4 challenge 素材。
