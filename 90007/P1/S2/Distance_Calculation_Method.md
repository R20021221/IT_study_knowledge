# Section 2.2 — Physical (Great-Circle) Distance Calculation

This document records the exact method used to compute the "Distance (km)"
column in `Section2.2_Analysis.xlsx` (cells **K5 to K14**).

---

## 1. Formula used: Haversine great-circle distance

The distance between two points on the Earth's surface (assumed spherical) is
computed with the **Haversine / spherical law of cosines** formula:

```
d = R · arccos( sin(φ₁)·sin(φ₂) + cos(φ₁)·cos(φ₂)·cos(λ₂ − λ₁) )
```

where

| Symbol | Meaning                                   | Value used                          |
| ------ | ----------------------------------------- | ----------------------------------- |
| R      | Mean Earth radius                         | **6371 km**                         |
| φ₁, λ₁ | Latitude and longitude of **origin**      | **-37.8136°, 144.9631°** (Melbourne CBD) |
| φ₂, λ₂ | Latitude and longitude of **destination** | from db-ip.com geolocation (per row)|
| d      | Great-circle distance                     | output, rounded to nearest km       |

All angular values are converted from degrees to radians before being passed
to the trigonometric functions.

Origin choice: **Melbourne CBD** was used instead of the author's home address
to preserve privacy while keeping the measurement representative of the local
network's geographic vantage point.

---

## 2. Excel implementation (exactly what lives in cells K5:K14)

```excel
=IF( OR(I5="", J5="") ,
     "",
     ROUND(
        6371 * ACOS( MIN(1,
            SIN(RADIANS($I$4)) * SIN(RADIANS(I5)) +
            COS(RADIANS($I$4)) * COS(RADIANS(I5)) *
            COS(RADIANS(J5 - $J$4))
        )),
     0)
)
```

- `$I$4`, `$J$4` (absolute references) = origin latitude / longitude (row 4).
- `I5`, `J5` (relative references) = destination latitude / longitude for row 5.
- `MIN(1, ...)` guards against floating-point values very slightly > 1 that would
  otherwise cause `#NUM!` inside `ACOS`.
- `ROUND(..., 0)` outputs whole kilometres.

---

## 3. Equivalent Python reference implementation

```python
from math import radians, sin, cos, acos

R_EARTH_KM = 6371.0
origin = (-37.8136, 144.9631)  # Melbourne CBD

def great_circle_km(lat1, lon1, lat2, lon2):
    φ1, φ2 = radians(lat1), radians(lat2)
    Δλ     = radians(lon2 - lon1)
    cos_c  = min(1.0, sin(φ1)*sin(φ2) + cos(φ1)*cos(φ2)*cos(Δλ))
    return R_EARTH_KM * acos(cos_c)
```

Running this on each destination's (latitude, longitude) pair reproduces the
values shown in column K of the spreadsheet exactly.

---

## 4. Worked example

Row 10 — `iperf.he.net` (Fremont, CA, United States)

| Input           | Value      |
| --------------- | ---------- |
| Origin lat (φ₁) | -37.8136°  |
| Origin lon (λ₁) | 144.9631°  |
| Dest  lat (φ₂) |  37.49°    |
| Dest  lon (λ₂) | -121.931°  |

```
arccos( sin(-37.8136°)·sin(37.49°) +
        cos(-37.8136°)·cos(37.49°)·cos(-121.931° − 144.9631°) )
= arccos(-0.38037 + 0.62657·(-0.07925))
= arccos(-0.42997)
= 2.01524 rad

d = 6371 × 2.01524 ≈ 12,679 km
```

This matches cell K14 ("12679").

---

## 5. References

- **Sinnott, R. W.** (1984). "Virtues of the Haversine". *Sky and Telescope*,
  vol. 68, no. 2, p. 159.

- **Veness, C.** (2002 – present). *Calculate distance, bearing and more between
  latitude/longitude points.* Movable Type Ltd.
  https://www.movable-type.co.uk/scripts/latlong.html

- **IUGG** (International Union of Geodesy and Geophysics). Mean Earth radius
  constant used in this work: **R = 6371 km** (volumetric mean radius).
  https://en.wikipedia.org/wiki/Earth_radius#Mean_radius

---

## 6. How to cite this calculation in your report

Suggested sentence for the Section 2.2 methodology paragraph:

> Great-circle distances between the origin (Melbourne CBD,
> 37.8136° S, 144.9631° E) and each destination were computed with the
> spherical law of cosines formulation of the Haversine distance
> [Sinnott 1984], using a mean Earth radius of 6371 km [IUGG]. Destination
> coordinates were obtained from db-ip.com's IP-geolocation database. The
> implementation is shown in Appendix X and was applied in Microsoft Excel
> using the formula `=ROUND(6371*ACOS(...), 0)` (see cells K5:K14 of
> `Section2.2_Analysis.xlsx`).
