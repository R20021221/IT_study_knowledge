"""
Section 3.2 — Average RTT & Jitter (Standard Deviation) Calculation
====================================================================
Data source: Section3.2_PingData.xlsx → "Packets" sheet
Method:      For each host, collect ALL 12 individual RTT values
             (3 runs × 4 packets), then compute:
               - Mean RTT:  x̄ = Σxᵢ / N
               - Jitter (sample SD):  σ = √[ Σ(xᵢ - x̄)² / (N - 1) ]
             where N = 12
"""

import openpyxl
import math
from collections import defaultdict

# ── 1. Read raw packet-level data ──────────────────────────────────
wb = openpyxl.load_workbook("Section3.2_PingData.xlsx", data_only=True)
ws = wb["Packets"]

# Collect all individual RTT values per host
hosts = defaultdict(lambda: {"name": "", "rtts": []})

for row in ws.iter_rows(min_row=2, values_only=True):
    # Columns: Host#, Hostname, Resolved, IP, Run#, icmp_seq, bytes, ttl, time(ms)
    host_num  = row[0]
    hostname  = row[1]
    time_ms   = row[8]          # individual RTT in ms

    if host_num is None or time_ms is None:
        continue

    hosts[host_num]["name"] = hostname
    hosts[host_num]["rtts"].append(float(time_ms))

# ── 2. Calculate Mean RTT and Jitter (σ) for each host ─────────────
print(f"{'#':<4} {'Host':<35} {'N':>3}  {'Mean RTT (ms)':>14}  {'Jitter σ (ms)':>14}")
print("-" * 78)

for num in sorted(hosts.keys()):
    h      = hosts[num]
    rtts   = h["rtts"]
    N      = len(rtts)

    # Mean:  x̄ = Σxᵢ / N
    mean_rtt = sum(rtts) / N

    # Sample standard deviation:  σ = √[ Σ(xᵢ - x̄)² / (N-1) ]
    sum_sq_diff = sum((x - mean_rtt) ** 2 for x in rtts)
    jitter      = math.sqrt(sum_sq_diff / (N - 1))

    print(f"{num:<4} {h['name']:<35} {N:>3}  {mean_rtt:>14.3f}  {jitter:>14.3f}")

    # Show detailed breakdown for verification
    print(f"     RTT values: {[round(x, 3) for x in rtts]}")
    print(f"     Σ(xᵢ - x̄)² = {sum_sq_diff:.4f}")
    print(f"     σ = √({sum_sq_diff:.4f} / {N-1}) = {jitter:.3f} ms")
    print()
