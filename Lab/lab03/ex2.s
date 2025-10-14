.globl main

.data
source:
    .word   3
    .word   1
    .word   4
    .word   1
    .word   5
    .word   9
    .word   0
dest:
    .word   0
    .word   0
    .word   0
    .word   0
    .word   0
    .word   0
    .word   0
    .word   0
    .word   0
    .word   0

.text
fun:
    addi t0, a0, 1
    sub t1, x0, a0
    mul a0, t0, t1
    jr ra

main:
    # BEGIN PROLOGUE
    addi sp, sp, -20
    sw s0, 0(sp)
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw ra, 16(sp)
    # END PROLOGUE
    addi t0, x0, 0
    addi s0, x0, 0
    la s1, source
    la s2, dest
loop:
    slli s3, t0, 2
    add t1, s1, s3
    lw t2, 0(t1)
    beq t2, x0, exit
    add a0, x0, t2
    addi sp, sp, -8
    sw t0, 0(sp)
    sw t2, 4(sp)
    jal fun
    lw t0, 0(sp)
    lw t2, 4(sp)
    addi sp, sp, 8
    add t2, x0, a0
    add t3, s2, s3
    sw t2, 0(t3)
    add s0, s0, t2
    addi t0, t0, 1
    jal x0, loop
exit:
    add a0, x0, s0
    # BEGIN EPILOGUE
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw ra, 16(sp)
    addi sp, sp, 20
    # END EPILOGUE
    jr ra

# The register representing the variable k.
# register t0 (x5), it's incremented before every jump back to label 'loop'

# The register representing the variable sum.
# register s0 (x8), it's gets the value returned from the function fun, which is
# saved to a0, then to t2, then sum += dest[k] is add s0, s0, t2

# The registers acting as pointers to the source and dest arrays.
# registers s1 and s2 respectively

# The assembly code for the loop found in the C code.
# loop:
#     slli s3, t0, 2
#     add t1, s1, s3
#     lw t2, 0(t1)
#     beq t2, x0, exit
#     add a0, x0, t2
#     addi sp, sp, -8
#     sw t0, 0(sp)
#     sw t2, 4(sp)
#     jal fun
#     lw t0, 0(sp)
#     lw t2, 4(sp)
#     addi sp, sp, 8
#     add t2, x0, a0
#     add t3, s2, s3
#     sw t2, 0(t3)
#     add s0, s0, t2
#     addi t0, t0, 1
#     jal x0, loop

# How the pointers are manipulated in the assembly code.
# an offset is computed using k * 4, given that the next element is a k-words away (a word is 4 bytes)
# the address of, for example, the fifth element of the source array which is 5 is:
# sourceBaseAddress + k(which is 4 now) << 2
# (shifting a number to the left multiplies it by 2, so we multiply k by 4 or by 2 * 2)