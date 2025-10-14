.globl factorial

.data
n: .word 8

.text
main:
    la t0, n
    lw a0, 0(t0)
    jal ra, factorial

    addi a1, a0, 0
    addi a0, x0, 1
    ecall # Print Result

    addi a1, x0, '\n'
    addi a0, x0, 11
    ecall # Print newline

    addi a0, x0, 10
    ecall # Exit

factorial:
    addi t0, x0, 2
    blt a0, t0, exit # if a0 is less than 2, jump to exit
    addi s0, x0, a0
    addi sp, sp, -8
    sw ra, 0(sp)
    sw s0, 4(sp)
    addi a0, a0, -1
    jal factorial
    lw ra, 0(sp)
    lw s0, 4(sp)
    add t2, x0, a0
    mul a0, a0, t2
    jr ra


exit:
    jr ra