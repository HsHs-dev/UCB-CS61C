# Floating Point Numbers

## 1 Pre-Check

1.1: false, because the goals of floating point numbers is
  
* have a large range of values
* HIGH amount of percision
* real arithmetic results

1.2: true, because the representation of floating point numbers uses exponent, the gap doubles every time the exponent does

1.3: false, because of rounding errors

```c
x = 3 1.5 x 10^38, y = 1.5 x 10^38, and z = 1.0
x + (y + z) = -1.5 x 10^38 + (1.5x10^38 + 1.0)
= -1.5x10^38 + (1.5x10^38) = 0.0
 (x + y) + z = (31.5x10^38 + 1.5x10^38) + 1.0
= (0.0) + 1.0 = 1.0
```

## 2 Floating Point

2.1: two zeros, positive zero; all zeros (significand and exponent) and sign bit off, and a negative zero; all zeros and negative bit on

2.2: 

formula: $(-1)^{sign} * (1 + significand) * 2^{exponent - 127}$

hence

largest finite positive number $= (-1)^{0} \times (1 + (1 - 2^{-23})) \times 2^{127}$

2.3: $(-1)^{0} \times 2^{-23} \times 2^{-126}$

2.4: $(-1)^{0} \times  2^{-126}$

2.5:

* 0x00000000 = 0
* 8.25 = 0x41040000
* 0x00000F00 = $(2^{-12} + 2^{-13} + 2^{-14} + 2^{-15}) \times 2 ^ {-126}$
* 39.5625 = 0x421E4000
* 0xFF94BEEF = NaN
* $-∞ =$ 0xFF800000

## 3 More Floating Point Representation

3.1: $(-1)^{0} \times (1 + 2^{-23}) \times 2$

3.2: $(-1)^{0} \times (1 + 2^{-23}) \times 2^{2}$

3.3: $2^{-22}$ and $2^{-21}$

3.4: $2^{e - 23}$ where $e$ is the exponent (unbiased)

