"""
Section 2.2 — Great-circle distance from Melbourne CBD to each iperf server.

This is a reference Python implementation of the Haversine / spherical
law-of-cosines formula that is used in the Excel workbook
(Section2.2_Analysis.xlsx, cells K5:K14).

Usage:
    python3 distance_calculation.py
"""

from math import radians, sin, cos, acos

R_EARTH_KM = 6371.0                 # IUGG mean Earth radius
ORIGIN = (-37.8136, 144.9631)       # Melbourne CBD (privacy-preserving origin)


def great_circle_km(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
    """Return the great-circle distance in kilometres between two
    (lat, lon) points, using the spherical law of cosines [Sinnott 1984]."""
    phi1, phi2 = radians(lat1), radians(lat2)
    dlam       = radians(lon2 - lon1)
    cos_c      = min(1.0, sin(phi1) * sin(phi2) +
                          cos(phi1) * cos(phi2) * cos(dlam))
    return R_EARTH_KM * acos(cos_c)


# The 10 destinations, as looked up on db-ip.com (22 April 2026).
destinations = [
    ("speedtest.awbb.net",          33.9845,  -101.338),
    ("spd-desrv.hostkey.com",       50.1109,    8.68213),
    ("speedtest.wobcom.de",         50.1109,    8.68213),
    ("rychlost.poda.cz",            49.8314,   18.2776),
    ("spd-fisrv.hostkey.com",       60.1699,   24.9384),
    ("speed.itgate.net",            45.4642,    9.18998),
    ("porto.speedtest.net.zon.pt",  38.7599,   -9.15765),
    ("speedtest.masnet.ec",         -2.94017, -78.92),
    ("dal.speedtest.clouvider.net", 32.7892,  -96.8217),
    ("iperf.he.net",                37.49,   -121.931),
]


if __name__ == "__main__":
    print(f"{'Hostname':<34} {'Lat':>9} {'Lon':>9} {'Dist (km)':>10}")
    print("-" * 66)
    for host, lat, lon in destinations:
        d = great_circle_km(*ORIGIN, lat, lon)
        print(f"{host:<34} {lat:>9.4f} {lon:>9.4f} {round(d):>10}")
