#!/bin/bash
# S4 iperf probe helper — 不是自动化脚本（spec 禁止），这是给你**逐条复制粘贴**用的命令清单
# 每条命令 5 秒完成；连续贴 3 行（A→B→C）快速判定一个 host
# 看到 "connected to ... port" + 有 Mbits/sec 输出 = 成功，把命令记到 iperf_servers.md

echo "==== 推荐探测顺序：先 iperf2，再 iperf3，最后试端口备选 ===="
echo ""

# 1. speedtest.awbb.net (USA)
echo "# Host 1: speedtest.awbb.net"
echo "iperf  -c speedtest.awbb.net -t 5"
echo "iperf3 -c speedtest.awbb.net -t 5"
echo "iperf3 -c speedtest.awbb.net -t 5 -p 5002"
echo ""

# 2. spd-desrv.hostkey.com
echo "# Host 2: spd-desrv.hostkey.com"
echo "iperf  -c spd-desrv.hostkey.com -t 5"
echo "iperf3 -c spd-desrv.hostkey.com -t 5"
echo "iperf3 -c spd-desrv.hostkey.com -t 5 -p 5201"
echo ""

# 3. speedtest.wobcom.de
echo "# Host 3: speedtest.wobcom.de"
echo "iperf  -c speedtest.wobcom.de -t 5"
echo "iperf3 -c speedtest.wobcom.de -t 5"
echo ""

# 4. rychlost.poda.cz
echo "# Host 4: rychlost.poda.cz"
echo "iperf  -c rychlost.poda.cz -t 5"
echo "iperf3 -c rychlost.poda.cz -t 5"
echo "iperf3 -c rychlost.poda.cz -t 5 -p 5201"
echo ""

# 5. spd-fisrv.hostkey.com
echo "# Host 5: spd-fisrv.hostkey.com"
echo "iperf  -c spd-fisrv.hostkey.com -t 5"
echo "iperf3 -c spd-fisrv.hostkey.com -t 5"
echo ""

# 6. speed.itgate.net
echo "# Host 6: speed.itgate.net"
echo "iperf  -c speed.itgate.net -t 5"
echo "iperf3 -c speed.itgate.net -t 5"
echo ""

# 7. porto.speedtest.net.zon.pt (4.2 furthest)
echo "# Host 7: porto.speedtest.net.zon.pt   [4.2 FURTHEST]"
echo "iperf  -c porto.speedtest.net.zon.pt -t 5"
echo "iperf3 -c porto.speedtest.net.zon.pt -t 5"
echo ""

# 8. speedtest.masnet.ec (4.2 closest)
echo "# Host 8: speedtest.masnet.ec          [4.2 CLOSEST]"
echo "iperf  -c speedtest.masnet.ec -t 5"
echo "iperf3 -c speedtest.masnet.ec -t 5"
echo ""

# 9. dal.speedtest.clouvider.net (Clouvider uses port range 5200-5209)
echo "# Host 9: dal.speedtest.clouvider.net   (Clouvider 端口范围 5200-5209)"
echo "iperf3 -c dal.speedtest.clouvider.net -t 5"
echo "iperf3 -c dal.speedtest.clouvider.net -t 5 -p 5200"
echo "iperf3 -c dal.speedtest.clouvider.net -t 5 -p 5201"
echo "iperf3 -c dal.speedtest.clouvider.net -t 5 -p 5202"
echo "iperf  -c dal.speedtest.clouvider.net -t 5"
echo ""

# 10. iperf.he.net
echo "# Host 10: iperf.he.net"
echo "iperf3 -c iperf.he.net -t 5"
echo "iperf  -c iperf.he.net -t 5"
echo ""
