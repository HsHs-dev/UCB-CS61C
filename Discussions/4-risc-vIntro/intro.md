# RISC-V Intro & Control Flow

## 1 Pre-Check

1.1: false, `t` registers (which stands for temporary), and `a` registers (argument registers) which hold return values from functions can both be changed by the calling function.

1.2: false, this assumes that the data type of the array is 4 bytes long (like `int` and `float` types)

1.3: true, if no constraints

1.4: true, *d* is 100 in ASCII, and the int array is 4 bytes for each element so that's $100 \div 4 = 25$

1.5: false, actually it saves the return address to ra if not specified

1.6: false, it should be `jal x0, label`

## 2 RISC-V: A Rundown

2.1:

* int x = 5 -> addi s0, x0, 5
* y[0] = x -> sw s0, 0(s1)
* y[1] = x * x -> mul t0, s0, s0 then sw t0, 4(s1)

## 3 Registers

3.1:  

* add s0, zero, a1 -> add x8, x0, x11
* or x18, x1, x30 -> or s2, ra, t5

## 4 Basic Instructions

4.1:

* a) lw t0, 12(s0) -> t0 = arr[3]
* b) sw t0, 16(s0) -> arr[4] = t0
* c) slli t1, t0, 2
     add t2, s0, t1
     lw t3, 0(t2)    --> arr[t0] = arr[t0] + 1
     addi t3, t3, 1
     sw t3, 0(t2)

* d) lw t0, 0(s0)
     xori t0, t0, 0xFFF --> t0 = arr[0] * -1
     addi t0, t0, 1

## 5 C to RISC-V

5.1:

*

```assembly
addi s0, s0, 4
addi s1, s1, 5
addi s2, s2, 6
add s3, s0, s1
add s3, s3, s2
addi s3, s3, 10
```

*

```assembly
add t0, x0, x0 
sw t0, 0(s0)
addi s1, s1, 2
add t0, x0, s1
slli t1, t0, 2
add t1, s0, t1
sw t0, 0(t1)
sw t0, 4(s0)
```

*

```assembly
addi s0, x0, 5
addi s1, x0, 10
add t0, s0, s0
bne t0, s1, else
add s0, x0, x0
j exit
else:
     addi t1, s0, -1
     add s1, x0, t1
exit:
```

*

```C
// i -> s0, a -> s1
int a = 1
for (int i = 0; i < 30; i++) {
     a += a;
}
```

*

```assembly
add s1, x0, x0
loop:
beq s0, zero, exit
add s1, s1, s0
addi s0, s0, -1
jal x0, loop
exit:
```

## 6 RISC-V with Arrays and Lists

6.1:

* load the first array element to t0
* load the third array element to t1
* add them together and put the result in t2
* store t2 to the second array element

6.2: increase all the linked list values by one

6.3: negates all array elements

## 7 RISC-V Calling Conventions

7.1: we use argument registers (a0 -> a7)

7.2: using a0 and a1

7.3: `sp` stands for *stack pointer* and it used to keep track of stack frames in memory

7.4: any registers that might be overwritten by the called function (a0 -> a7, t0 -> t6, and ra)

7.5: any used *saved registers*, `sp`, and `ra` if the callee had called any functions

7.6: saved registers will be the same, argument and temporary ones might change

## 9 More Translating between C and RISC-V

9.1

```C
int func(int x, int y) {
     int result = 1;
     for (y; y > 0; y--) {
          result *= x;
     }
     return result;
}
```
