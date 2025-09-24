#!/usr/bin/env python3
from sys import argv

def print_ppm(file):
    with open(file) as f:
        lines = f.readlines()
    # Skip comments
    lines = [l for l in lines if not l.startswith('#')]
    if lines[0].strip() != 'P3':
        print("Not a P3 PPM")
        return
    w, h = map(int, lines[1].split())
    maxval = int(lines[2])
    pixels = ' '.join(lines[3:]).split()
    for y in range(h):
        for x in range(w):
            r = int(pixels[(y*w + x)*3 + 0])
            g = int(pixels[(y*w + x)*3 + 1])
            b = int(pixels[(y*w + x)*3 + 2])
            print(f"\x1b[48;2;{r};{g};{b}m ", end='')
        print("\x1b[0m")

print_ppm(argv[1])

